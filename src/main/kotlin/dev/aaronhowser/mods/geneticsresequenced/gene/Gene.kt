package dev.aaronhowser.mods.geneticsresequenced.gene

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.gene.ModGeneProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModGeneTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.HolderSetCodec
import net.minecraft.resources.RegistryFileCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class Gene(
    val dnaPointsRequired: Int,
    val allowedEntities: HolderSet<EntityType<*>>,
    val potionDetails: Optional<PotionDetails>,
    val attributeModifiers: List<AttributeEntry>,
    val scaresEntitiesWithTag: Optional<TagKey<EntityType<*>>>
) {

    data class AttributeEntry(
        val attribute: Holder<Attribute>,
        val modifier: AttributeModifier
    ) {
        companion object {
            val DIRECT_CODEC: Codec<AttributeEntry> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Attribute.CODEC
                        .fieldOf("attribute")
                        .forGetter(AttributeEntry::attribute),
                    AttributeModifier.CODEC
                        .fieldOf("modifier")
                        .forGetter(AttributeEntry::modifier)
                ).apply(instance, Gene::AttributeEntry)
            }

            val DIRECT_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, AttributeEntry> =
                StreamCodec.composite(
                    Attribute.STREAM_CODEC, AttributeEntry::attribute,
                    AttributeModifier.STREAM_CODEC, AttributeEntry::modifier,
                    Gene::AttributeEntry
                )
        }
    }

    data class PotionDetails(
        val effect: Holder<MobEffect>,
        val level: Int = 1,
        val duration: Int = -1,
        val showIcon: Boolean = false
    ) {
        companion object {
            val DIRECT_CODEC: Codec<PotionDetails> = RecordCodecBuilder.create { instance ->
                instance.group(
                    MobEffect.CODEC
                        .fieldOf("effect")
                        .forGetter(PotionDetails::effect),
                    Codec.INT
                        .optionalFieldOf("level", 1)
                        .forGetter(PotionDetails::level),
                    Codec.INT
                        .optionalFieldOf("duration", -1)
                        .forGetter(PotionDetails::duration),
                    Codec.BOOL
                        .optionalFieldOf("show_icon", false)
                        .forGetter(PotionDetails::showIcon)
                ).apply(instance, Gene::PotionDetails)
            }

            val DIRECT_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PotionDetails> =
                StreamCodec.composite(
                    MobEffect.STREAM_CODEC, PotionDetails::effect,
                    ByteBufCodecs.VAR_INT, PotionDetails::level,
                    ByteBufCodecs.VAR_INT, PotionDetails::duration,
                    ByteBufCodecs.BOOL, PotionDetails::showIcon,
                    Gene::PotionDetails
                )
        }
    }

    val allowsMobs = allowedEntities.any { it.value() != EntityType.PLAYER }

    fun canEntityHave(entity: Entity): Boolean {
        return canEntityTypeHave(entity.type)
    }

    fun canEntityTypeHave(entityType: EntityType<*>): Boolean {
        return allowedEntities.map { it.value() }.contains(entityType)
    }

    fun getPotion(): MobEffectInstance? {
        val potionDetails = potionDetails.getOrNull() ?: return null

        return MobEffectInstance(
            potionDetails.effect,
            potionDetails.duration,
            potionDetails.level - 1,
            true,
            false,
            potionDetails.showIcon
        )
    }

    fun setAttributeModifiers(livingEntity: LivingEntity, isAdding: Boolean) {

        for ((attribute, modifier) in attributeModifiers) {

            val attributeInstance = livingEntity.getAttribute(attribute)

            if (attributeInstance == null) {
                livingEntity.sendSystemMessage(
                    Component.literal("A Gene tried to modify an attribute ${attribute.key} that you don't have!")
                )
                GeneticsResequenced.LOGGER.error("A Gene tried to modify an attribute ${attribute.key} that entity ${livingEntity.name} does not have!")
                continue
            }

            if (isAdding) {
                if (!attributeInstance.hasModifier(modifier.id)) attributeInstance.addPermanentModifier(modifier)
            } else {
                if (attributeInstance.hasModifier(modifier.id)) attributeInstance.removeModifier(modifier)
            }

        }

    }

    companion object {

        val ResourceKey<Gene>.translationKey: String
            get() {
                val namespace = this.location().namespace
                val path = this.location().path

                return "gene.$namespace.$path"
            }

        val Holder<Gene>.translationKey: String
            get() {
                return this.key!!.translationKey
            }

        fun Holder<Gene>?.isGene(geneRk: ResourceKey<Gene>?): Boolean {
            return this != null && geneRk != null && this.key == geneRk
        }

        fun Holder<Gene>?.isGene(geneHolder: Holder<Gene>): Boolean {
            return this == geneHolder || this.isGene(geneHolder.key)
        }

        val Holder<Gene>.isNegative: Boolean
            get() = this.`is`(ModGeneTagsProvider.NEGATIVE)

        val Holder<Gene>.isMutation: Boolean
            get() = this.`is`(ModGeneTagsProvider.MUTATION)

        val Holder<Gene>.isHelixOnly: Boolean
            get() = this.`is`(ModGeneTagsProvider.HELIX_ONLY)

        val Holder<Gene>.isDisabled: Boolean
            get() = this.`is`(ModGeneTagsProvider.DISABLED)

        fun getNameComponent(
            geneRk: ResourceKey<Gene>,
            registries: HolderLookup.Provider = ClientUtil.localRegistryAccess!!
        ): MutableComponent {
            return getNameComponent(geneRk.getHolder(registries)!!)
        }

        fun getNameComponent(geneHolder: Holder<Gene>): MutableComponent {
            val color = if (geneHolder.isDisabled) {
                ChatFormatting.DARK_RED
            } else if (geneHolder.isNegative) {
                ChatFormatting.RED
            } else if (geneHolder.isMutation) {
                ChatFormatting.DARK_PURPLE
            } else {
                ChatFormatting.GRAY
            }

            val component = geneHolder.translationKey
                .toComponent()
                .withStyle {
                    it
                        .withColor(color)
                        .withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ModLanguageProvider.Tooltips.COPY_GENE.toComponent(
                                    geneHolder.key!!.location().toString()
                                )
                            )
                        )
                        .withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.COPY_TO_CLIPBOARD,
                                geneHolder.key!!.location().toString()
                            )
                        )
                }

            if (geneHolder.isDisabled) {
                component.append(
                    ModLanguageProvider.Genes.DISABLED.toComponent()
                )
            }

            return component
        }

        val unknownGeneComponent: MutableComponent = ModLanguageProvider.Genes.UNKNOWN.toComponent()

        val defaultAllowedEntities = AnyHolderSet(BuiltInRegistries.ENTITY_TYPE.asLookup())

        val DIRECT_CODEC: Codec<Gene> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT
                        .optionalFieldOf("dna_points_required", 1)
                        .forGetter(Gene::dnaPointsRequired),
                    RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE)
                        .optionalFieldOf(
                            "allowed_entities",
                            defaultAllowedEntities
                        )
                        .forGetter(Gene::allowedEntities),
                    PotionDetails.DIRECT_CODEC
                        .optionalFieldOf("potion_details")
                        .forGetter(Gene::potionDetails),
                    AttributeEntry.DIRECT_CODEC.listOf()
                        .optionalFieldOf("attribute_modifiers", emptyList())
                        .forGetter(Gene::attributeModifiers),
                    TagKey.codec(Registries.ENTITY_TYPE)
                        .optionalFieldOf("scares_entities_with_tag")
                        .forGetter(Gene::scaresEntitiesWithTag)
                ).apply(instance, ::Gene)
            }

        val DIRECT_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Gene> = StreamCodec.composite(
            ByteBufCodecs.INT, Gene::dnaPointsRequired,
            ByteBufCodecs.holderSet(Registries.ENTITY_TYPE), Gene::allowedEntities,
            ByteBufCodecs.optional(PotionDetails.DIRECT_STREAM_CODEC), Gene::potionDetails,
            AttributeEntry.DIRECT_STREAM_CODEC.apply(ByteBufCodecs.list()), Gene::attributeModifiers,
            ByteBufCodecs.optional(OtherUtil.tagKeyStreamCodec(Registries.ENTITY_TYPE)), Gene::scaresEntitiesWithTag,
            ::Gene
        )

        val CODEC: Codec<Holder<Gene>> = RegistryFileCodec.create(ModGenes.GENE_REGISTRY_KEY, DIRECT_CODEC)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Holder<Gene>> =
            ByteBufCodecs.holder(ModGenes.GENE_REGISTRY_KEY, DIRECT_STREAM_CODEC)

        val HOLDER_SET_CODEC: Codec<HolderSet<Gene>> =
            HolderSetCodec.create(ModGenes.GENE_REGISTRY_KEY, CODEC, false)

        val HOLDER_SET_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HolderSet<Gene>> =
            ByteBufCodecs.holderSet(ModGenes.GENE_REGISTRY_KEY)

    }

    override fun toString(): String {
        return StringBuilder()
            .append("Gene{")
            .append("dnaPointsRequired=").append(dnaPointsRequired)
            .append(", allowedEntities=").append(
                when (allowedEntities) {
                    defaultAllowedEntities -> "any"
                    ModGeneProvider.noEntities -> "none"
                    ModGeneProvider.onlyPlayers -> "players"
                    else -> allowedEntities
                }
            )
            .append(", potionDetails=").append(potionDetails)
            .append(", attributeModifiers=").append(attributeModifiers)
            .append(", scaresEntitiesWithTag=").append(scaresEntitiesWithTag)
            .append("}")
            .toString()
    }

}
