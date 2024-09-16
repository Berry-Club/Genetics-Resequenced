package dev.aaronhowser.mods.geneticsresequenced.api.genes

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.RegistryFileCodec
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class Gene(
    val isNegative: Boolean = false,
    val isHidden: Boolean = false,
    val dnaPointsRequired: Int = 1,
    val allowedEntities: HolderSet<EntityType<*>> = AnyHolderSet(BuiltInRegistries.ENTITY_TYPE.asLookup()),
    val mutatesInto: Optional<Holder<Gene>> = Optional.empty(),
    val potionDetails: Optional<PotionDetails> = Optional.empty(),
    val attributeModifiers: List<AttributeEntry> = emptyList()
) {

    data class AttributeEntry(
        val attribute: Holder<Attribute>,
        val modifier: AttributeModifier
    ) {
        companion object {
            val DIRECT_CODEC: Codec<AttributeEntry> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Attribute.CODEC.fieldOf("attribute").forGetter(AttributeEntry::attribute),
                    AttributeModifier.MAP_CODEC.forGetter(AttributeEntry::modifier)
                ).apply(instance, ::AttributeEntry)
            }

            val DIRECT_STREAM_CODEC: StreamCodec<ByteBuf, AttributeEntry> =
                ByteBufCodecs.fromCodec(DIRECT_CODEC)
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
                    MobEffect.CODEC.fieldOf("effect").forGetter(PotionDetails::effect),
                    Codec.INT.optionalFieldOf("level", 1).forGetter(PotionDetails::level),
                    Codec.INT.optionalFieldOf("duration", -1).forGetter(PotionDetails::duration),
                    Codec.BOOL.optionalFieldOf("showIcon", false).forGetter(PotionDetails::showIcon)
                ).apply(instance, ::PotionDetails)
            }

            val DIRECT_STREAM_CODEC: StreamCodec<ByteBuf, PotionDetails> =
                ByteBufCodecs.fromCodec(DIRECT_CODEC)
        }
    }

    val id = OtherUtil.modResource("gene")

    private val requiredGenes: MutableSet<Holder<Gene>> = mutableSetOf()

    fun isMutation(registries: HolderLookup.Provider): Boolean {
        return GeneRegistry.getAllGeneHolders(registries)
            .anyMatch { it.value().mutatesInto.getOrNull()?.value() === this }
            .not()
    }

    fun addRequiredGenes(genes: Collection<Holder<Gene>>) {
        requiredGenes.addAll(genes)
    }

    fun removeRequiredGenes(genes: Collection<Holder<Gene>>) {
        requiredGenes.removeAll(genes.toSet())
    }

    fun getRequiredGeneHolders(): Set<Holder<Gene>> {
        return requiredGenes.toSet()
    }

    fun canEntityHave(entity: Entity): Boolean {
        return canEntityTypeHave(entity.type)
    }

    fun canEntityTypeHave(entityType: EntityType<*>): Boolean {
        return allowedEntities.map { it.value() }.contains(entityType)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    val translationKey: String = "gene.${id.namespace}.${id.path}"

    fun nameComponent(registries: HolderLookup.Provider): MutableComponent {
        val color = if (isActive) {
            if (isNegative) {
                ChatFormatting.RED
            } else if (isMutation(registries)) {
                ChatFormatting.DARK_PURPLE
            } else {
                ChatFormatting.GRAY
            }
        } else {
            ChatFormatting.DARK_RED
        }

        val component = translationKey
            .toComponent()
            .withStyle {
                it
                    .withColor(color)
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            ModLanguageProvider.Tooltips.COPY_GENE.toComponent(id.toString())
                        )
                    )
                    .withClickEvent(
                        ClickEvent(
                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                            id.toString()
                        )
                    )
            }

        if (!isActive) {
            component.append(
                ModLanguageProvider.Genes.GENE_DISABLED.toComponent()
            )
        }

        return component
    }

    var isActive: Boolean = true
        private set

    private fun deactivate() {
        isActive = false
        GeneticsResequenced.LOGGER.info("Deactivated gene $id")
    }

    private fun reactivate() {
        isActive = true
        GeneticsResequenced.LOGGER.info("Reactivated gene $id")
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
                ?: throw IllegalArgumentException("Living Entity does not have attribute $attribute")

            if (isAdding) {
                if (!attributeInstance.hasModifier(modifier.id)) attributeInstance.addPermanentModifier(modifier)
            } else {
                if (attributeInstance.hasModifier(modifier.id)) attributeInstance.removeModifier(modifier)
            }

        }

    }

    companion object {

        val unknownGeneComponent: MutableComponent = ModLanguageProvider.Genes.UNKNOWN.toComponent()

//        fun checkDeactivationConfig() {
//            val disabledGeneStrings = ServerConfig.disabledGenes.get()
//
//            val previouslyDisabledGenes = GeneRegistry.GENE_REGISTRY.filterNot { it.isActive }
//            for (previouslyDisabledGene in previouslyDisabledGenes) {
//                previouslyDisabledGene.reactivate()
//            }
//
//            for (disabledGene in disabledGeneStrings) {
//                val gene = GeneRegistry.fromString(disabledGene)
//
//                if (gene == null) {
//                    GeneticsResequenced.LOGGER.warn("Tried to disable gene $disabledGene, but it does not exist!")
//                    continue
//                }
//
//                if (gene in requiredGenes) {
//                    GeneticsResequenced.LOGGER.warn("Tried to disable gene $disabledGene, but it is required for the mod to function!")
//                    continue
//                }
//
//                gene.deactivate()
//            }
//        }

        private val requiredGenes by lazy {
            setOf(
                ModGenes.BASIC.get()
            )
        }

        val DIRECT_CODEC: Codec<Gene> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.BOOL.optionalFieldOf("negative", false).forGetter(Gene::isNegative),
                    Codec.BOOL.optionalFieldOf("hidden", false).forGetter(Gene::isHidden),
                    Codec.INT.fieldOf("dna_points_required").forGetter(Gene::dnaPointsRequired),
                    RegistryCodecs
                        .homogeneousList(Registries.ENTITY_TYPE)
                        .optionalFieldOf(
                            "allowed_entities",
                            AnyHolderSet(BuiltInRegistries.ENTITY_TYPE.asLookup())
                        )
                        .forGetter(Gene::allowedEntities),
                    RegistryFixedCodec.create(GeneRegistry.GENE_REGISTRY_KEY).optionalFieldOf("mutates_into")
                        .forGetter(Gene::mutatesInto),
                    PotionDetails.DIRECT_CODEC.optionalFieldOf("potion_details").forGetter(Gene::potionDetails),
                    AttributeEntry.DIRECT_CODEC.listOf().optionalFieldOf("attribute_modifiers", emptyList())
                        .forGetter(Gene::attributeModifiers)
                ).apply(instance, ::Gene)
            }

        val DIRECT_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Gene> = NeoForgeStreamCodecs.composite(
            ByteBufCodecs.BOOL, Gene::isNegative,
            ByteBufCodecs.BOOL, Gene::isHidden,
            ByteBufCodecs.INT, Gene::dnaPointsRequired,
            ByteBufCodecs.holderSet(Registries.ENTITY_TYPE), Gene::allowedEntities,
            ByteBufCodecs.optional(Gene.STREAM_CODEC), Gene::mutatesInto,
            ByteBufCodecs.optional(PotionDetails.DIRECT_STREAM_CODEC), Gene::potionDetails,
            AttributeEntry.DIRECT_STREAM_CODEC.apply(ByteBufCodecs.list()), Gene::attributeModifiers,
            ::Gene
        )

        val CODEC: RegistryFileCodec<Gene> = RegistryFileCodec.create(GeneRegistry.GENE_REGISTRY_KEY, DIRECT_CODEC)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Holder<Gene>> =
            ByteBufCodecs.holder(GeneRegistry.GENE_REGISTRY_KEY, DIRECT_STREAM_CODEC)

    }

}
