package dev.aaronhowser.mods.genetics_resequenced.gene

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withClickToCopyToClipboard
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withHoverText
import dev.aaronhowser.mods.aaron.serialization.AaronExtraStreamCodecs
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.gene.ModGeneProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModGeneLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.tag.ModGeneTagsProvider
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
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
import java.util.*

data class Gene(
	val dnaPointsRequired: Int,
	val allowedEntities: HolderSet<EntityType<*>>,
	val potionDetails: List<PotionDetails>,
	val attributeModifiers: List<AttributeEntry>,
	val scaresEntitiesWithTag: Optional<TagKey<EntityType<*>>>,
	val incompatibleGenes: List<ResourceKey<Gene>>
) {

	val potions: List<MobEffectInstance> =
		potionDetails.map { potionDetails ->
			MobEffectInstance(
				potionDetails.effect,
				potionDetails.duration,
				potionDetails.level - 1,
				true,
				false,
				potionDetails.showIcon
			)
		}

	val allowsMobs = this.allowedEntities.any { it.value() != EntityType.PLAYER }

	fun canEntityHave(entity: Entity): Boolean {
		return canEntityTypeHave(entity.type)
	}

	fun canEntityTypeHave(entityType: EntityType<*>): Boolean {
		return this.allowedEntities.any { it.value() == entityType }
	}

	fun setAttributeModifiers(livingEntity: LivingEntity, isAdding: Boolean) {
		for ((attribute, modifier) in attributeModifiers) {
			val attributeInstance = livingEntity.getAttribute(attribute)

			if (attributeInstance == null) {
				livingEntity.tell(
					Component.literal("A Gene tried to modify an attribute ${attribute.key} that you don't have!")
				)
				GeneticsResequenced.LOGGER.error("A Gene tried to modify an attribute ${attribute.key} that entity ${livingEntity.name} does not have!")
				continue
			}

			if (isAdding) {
				if (!attributeInstance.hasModifier(modifier.id)) {
					attributeInstance.addPermanentModifier(modifier)
				}
			} else {
				if (attributeInstance.hasModifier(modifier.id)) {
					attributeInstance.removeModifier(modifier)
				}
			}
		}
	}

	companion object {

		val ResourceKey<Gene>.translationKey: String
			get() {
				val namespace = this.identifier().namespace
				val path = this.identifier().path

				return "gene.$namespace.$path"
			}

		val Holder<Gene>.translationKey: String
			get() {
				return this.key!!.translationKey
			}

		fun Holder<Gene>?.isGene(geneRk: ResourceKey<Gene>?): Boolean {
			return this != null && geneRk != null && this.isHolder(geneRk)
		}

		fun Holder<Gene>?.isGene(geneHolder: Holder<Gene>): Boolean {
			return this === geneHolder || this.isGene(geneHolder.key)
		}

		val Holder<Gene>.isNegative: Boolean
			get() = this.isHolder(ModGeneTagsProvider.NEGATIVE)

		val Holder<Gene>.isMutation: Boolean
			get() = this.isHolder(ModGeneTagsProvider.MUTATION)

		val Holder<Gene>.isHelixOnly: Boolean
			get() = this.isHolder(ModGeneTagsProvider.HELIX_ONLY)

		val Holder<Gene>.isDisabled: Boolean
			get() = this.isHolder(ModGeneTagsProvider.DISABLED)

		fun ResourceKey<Gene>.isDisabled(registries: HolderLookup.Provider): Boolean {
			return this.getHolderOrThrow(registries).isDisabled
		}

		fun getNameComponent(
			geneRk: ResourceKey<Gene>,
			registries: HolderLookup.Provider = ClientUtil.localRegistryAccess!!
		): MutableComponent {
			return getNameComponent(geneRk.getHolderOrThrow(registries))
		}

		fun Holder<Gene>.getName(): MutableComponent = getNameComponent(this)

		fun getNameComponent(geneHolder: Holder<Gene>): MutableComponent {
			val color = when {
				geneHolder.isDisabled -> ChatFormatting.DARK_RED
				geneHolder.isNegative -> ChatFormatting.RED
				geneHolder.isMutation -> ChatFormatting.DARK_PURPLE
				else -> ChatFormatting.GRAY
			}

			val component = geneHolder.translationKey
				.toComponent()
				.withStyle(
					Style.EMPTY
						.withColor(color)
						.withHoverText(ModTooltipLang.COPY_GENE.toComponent(geneHolder.key!!.identifier().toString()))
						.withClickToCopyToClipboard(geneHolder.key!!.identifier().toString())
				)

			if (geneHolder.isDisabled) {
				component.append(
					ModGeneLang.DISABLED_SUFFIX.toComponent()
				)
			}

			return component
		}

		val UNKNOWN_GENE_COMPONENT: MutableComponent = ModGeneLang.UNKNOWN.toComponent()

		val DIRECT_CODEC: Codec<Gene> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.INT
						.optionalFieldOf("dna_points_required", 1)
						.forGetter(Gene::dnaPointsRequired),
					RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE)
						.optionalFieldOf(
							"allowed_entities",
							ModGeneProvider.DEFAULT_ALLOWED_ENTITIES
						)
						.forGetter(Gene::allowedEntities),
					PotionDetails.DIRECT_CODEC.listOf()
						.optionalFieldOf("potion_details", emptyList())
						.forGetter(Gene::potionDetails),
					AttributeEntry.DIRECT_CODEC.listOf()
						.optionalFieldOf("attribute_modifiers", emptyList())
						.forGetter(Gene::attributeModifiers),
					TagKey.codec(Registries.ENTITY_TYPE)
						.optionalFieldOf("scares_entities_with_tag")
						.forGetter(Gene::scaresEntitiesWithTag),
					ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
						.listOf()
						.optionalFieldOf("incompatible_genes", emptyList())
						.forGetter(Gene::incompatibleGenes)
				).apply(instance, ::Gene)
			}

		val DIRECT_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Gene> = StreamCodec.composite(
			ByteBufCodecs.INT, Gene::dnaPointsRequired,
			ByteBufCodecs.holderSet(Registries.ENTITY_TYPE), Gene::allowedEntities,
			PotionDetails.DIRECT_STREAM_CODEC.apply(ByteBufCodecs.list()), Gene::potionDetails,
			AttributeEntry.DIRECT_STREAM_CODEC.apply(ByteBufCodecs.list()), Gene::attributeModifiers,
			ByteBufCodecs.optional(AaronExtraStreamCodecs.tagKeyStreamCodec(Registries.ENTITY_TYPE)), Gene::scaresEntitiesWithTag,
			ResourceKey.streamCodec(ModGenes.GENE_REGISTRY_KEY).apply(ByteBufCodecs.list()), Gene::incompatibleGenes,
			::Gene
		)

		val CODEC: Codec<Holder<Gene>> = RegistryFileCodec.create(ModGenes.GENE_REGISTRY_KEY, DIRECT_CODEC, false)

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
			.append("dnaPointsRequired=").append(this.dnaPointsRequired)
			.append(", allowedEntities=").append(
				when (this.allowedEntities) {
					ModGeneProvider.DEFAULT_ALLOWED_ENTITIES -> "any"
					ModGeneProvider.NO_ENTITIES -> "none"
					ModGeneProvider.ONLY_PLAYERS -> "players"
					else -> this.allowedEntities
				}
			)
			.append(", potionDetails=").append(this.potionDetails)
			.append(", attributeModifiers=").append(this.attributeModifiers)
			.append(", scaresEntitiesWithTag=").append(this.scaresEntitiesWithTag)
			.append("}")
			.toString()
	}

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

}
