package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTag
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModItemLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.tag.ModDamageTypeTagsProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.level.Level
import java.util.*
import java.util.function.Consumer

open class SyringeItem(properties: Properties) : Item(properties) {

	override fun getUseDuration(itemStack: ItemStack, pHolder: LivingEntity): Int = 40
	override fun getUseAnimation(itemStack: ItemStack): ItemUseAnimation = ItemUseAnimation.BOW

	override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
		player.startUsingItem(hand)
		return InteractionResult.CONSUME
	}

	override fun onUseTick(level: Level, livingEntity: LivingEntity, itemStack: ItemStack, ticksRemaining: Int) {

		if (ticksRemaining <= 1) {
			livingEntity.stopUsingItem()
			releaseUsing(itemStack, level, livingEntity, ticksRemaining)
		}

	}

	override fun releaseUsing(itemStack: ItemStack, level: Level, livingEntity: LivingEntity, remainingTime: Int): Boolean {
		if (livingEntity !is Player || livingEntity.isFakePlayer || remainingTime > 1) return false

		if (isContaminated(itemStack)) {
			if (!level.isClientSide) {
				livingEntity.sendSystemMessage(
					ModMessageLang.SYRINGE_CONTAMINATED.toComponent()
				)
			}

			return true
		}

		if (hasBlood(itemStack)) {
			injectEntity(itemStack, livingEntity)
		} else {
			setEntity(itemStack, livingEntity)
		}

		livingEntity.hurt(damageSourceUseSyringe(level, livingEntity), 1f)
		livingEntity.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 3))
		livingEntity.cooldowns.addCooldown(itemStack, 10)

		return true
	}

	override fun getName(itemStack: ItemStack): Component {
		return if (hasBlood(itemStack)) {
			ModItemLang.SYRINGE_FULL.toComponent()
		} else {
			ModItemLang.SYRINGE_EMPTY.toComponent()
		}
	}

	override fun appendHoverText(
		itemStack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val bloodOwner = getEntityName(itemStack)

		if (hasBlood(itemStack) && bloodOwner != null) {
			builder.accept(
				ModTooltipLang.SYRINGE_OWNER
					.toComponent(bloodOwner)
					.withStyle(ChatFormatting.GRAY)
			)
		}

		if (isContaminated(itemStack)) {
			builder.accept(
				ModTooltipLang.SYRINGE_CONTAMINATED
					.toComponent()
					.withStyle(ChatFormatting.DARK_GREEN)
			)
		}

		val addingGenes = getGenes(itemStack)
		if (addingGenes.isNotEmpty()) {
			builder.accept(
				ModTooltipLang.SYRINGE_ADDING_GENES
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)

			for (geneHolder in addingGenes) {
				val nameComponent = geneHolder.getName()

				val component = Component
					.literal("• ")
					.withStyle {
						it.withColor(nameComponent.style.color)
					}.append(nameComponent)

				builder.accept(component)
			}
		}

		val removingGenes = getAntigenes(itemStack)
		if (removingGenes.isNotEmpty()) {
			builder.accept(
				ModTooltipLang.SYRINGE_REMOVING_GENES
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)

			for (geneHolder in removingGenes) {
				val nameComponent = geneHolder.getName()

				val component = Component
					.literal("• ")
					.withStyle {
						it.withColor(nameComponent.style.color)
					}.append(nameComponent)

				builder.accept(component)
			}
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		fun ItemStack.isSyringe(): Boolean = this.isTag(ModItemTagsProvider.SYRINGES)

		fun isBeingUsed(syringeStack: ItemStack, entity: LivingEntity?): Boolean {
			return entity?.useItem == syringeStack
		}

		fun setEntity(stack: ItemStack, entity: LivingEntity?, setContaminated: Boolean = true) {
			if (entity == null) {
				stack.remove(ModDataComponents.SPECIFIC_ENTITY)
				return
			}

			SpecificEntityItemComponent.setEntity(stack, entity)

			if (setContaminated) {
				setContaminated(stack, true)
			}
		}

		private fun getEntityUuid(syringeStack: ItemStack): UUID? = SpecificEntityItemComponent.getEntityUuid(syringeStack)
		fun getEntityName(syringeStack: ItemStack): Component? = SpecificEntityItemComponent.getEntityName(syringeStack)

		fun injectEntity(syringeStack: ItemStack, entity: LivingEntity) {
			val syringeEntityUuid = getEntityUuid(syringeStack) ?: return
			if (entity.uuid != syringeEntityUuid) return

			val genesToAdd = if (entity is Player) {
				getGenes(syringeStack)
			} else {
				getGenes(syringeStack).filter { it.value().allowsMobs }.toSet()
			}

			val genesToRemove = getAntigenes(syringeStack)

			addGenes(entity, genesToAdd)
			removeGenes(entity, genesToRemove)

			clearGenes(syringeStack)
			clearAntigenes(syringeStack)
			setEntity(syringeStack, null)
		}

		private fun removeGenes(entity: LivingEntity, syringeAntigenes: Set<Holder<Gene>>) {
			val entityGenesBefore = entity.permanentGeneHolders

			for (antigene in syringeAntigenes) {
				entity.removeGene(antigene)
			}

			val entityGenesAfter = entity.permanentGeneHolders
			val genesRemoved = entityGenesBefore - entityGenesAfter
			val genesNotRemoved = syringeAntigenes - genesRemoved

			if (!entity.level().isClientSide) {
				for (removedGeneHolder in genesRemoved) {
					entity.tell(
						ModMessageLang.SYRINGE_REMOVE_GENES_SUCCESS.toComponent(
							removedGeneHolder.getName()
						)
					)
				}

				for (notRemovedGeneHolder in genesNotRemoved) {
					entity.tell(
						ModMessageLang.SYRINGE_REMOVE_GENES_FAIL.toComponent(
							Gene.getNameComponent(notRemovedGeneHolder)
						)
					)
				}
			}

		}

		private fun addGenes(entity: LivingEntity, syringeGenes: Set<Holder<Gene>>) {
			val entityGenesBefore = entity.permanentGeneHolders

			for (gene in syringeGenes) {
				entity.addGene(gene)
			}

			val entityGenesAfter = entity.permanentGeneHolders
			val genesAdded = entityGenesAfter - entityGenesBefore
			val genesNotAdded = syringeGenes - genesAdded

			if (!entity.isClientSide) {
				for (addedGeneHolder in genesAdded) {
					entity.tell(
						ModMessageLang.SYRINGE_INJECTED.toComponent(
							Gene.getNameComponent(addedGeneHolder)
						)
					)
				}

				for (notAddedGeneHolder in genesNotAdded) {
					entity.tell(
						ModMessageLang.SYRINGE_FAILED.toComponent(
							Gene.getNameComponent(notAddedGeneHolder)
						)
					)
				}
			}

		}

		fun hasBlood(syringeStack: ItemStack): Boolean = SpecificEntityItemComponent.hasEntity(syringeStack)

		fun getGenes(syringeStack: ItemStack): Set<Holder<Gene>> {
			return syringeStack.get(ModDataComponents.GENE_SET)?.toSet() ?: emptySet()
		}

		fun getGeneRks(syringeStack: ItemStack): Set<ResourceKey<Gene>> {
			return getGenes(syringeStack).mapNotNull { it.key }.toSet()
		}

		fun canAddGene(syringeStack: ItemStack, gene: Holder<Gene>): Boolean {
			return hasBlood(syringeStack) && gene !in getGenes(syringeStack)
		}

		fun addGene(syringeStack: ItemStack, gene: Holder<Gene>): Boolean {
			if (!canAddGene(syringeStack, gene)) return false

			val currentGenes = getGenes(syringeStack)
			val newGenes = currentGenes + gene
			val newHolderSet = HolderSet.direct(newGenes.toList())

			syringeStack.set(ModDataComponents.GENE_SET, newHolderSet)

			return true
		}

		private fun clearGenes(syringeStack: ItemStack) {
			syringeStack.remove(ModDataComponents.GENE_SET)
		}

		private fun clearAntigenes(syringeStack: ItemStack) {
			syringeStack.remove(ModDataComponents.ANTIGENE_SET)
		}

		fun isContaminated(syringeStack: ItemStack): Boolean {
			return syringeStack.get(ModDataComponents.IS_CONTAMINATED) ?: false
		}

		fun setContaminated(syringeStack: ItemStack, value: Boolean) {
			syringeStack.set(ModDataComponents.IS_CONTAMINATED, value)
		}

		fun getAntigenes(syringeStack: ItemStack): Set<Holder<Gene>> {
			return syringeStack.get(ModDataComponents.ANTIGENE_SET)?.toSet() ?: emptySet()
		}

		fun canAddAntigene(syringeStack: ItemStack, gene: Holder<Gene>): Boolean {
			return hasBlood(syringeStack)
					&& gene !in getAntigenes(syringeStack)
					&& gene !in getGenes(syringeStack)
		}

		fun addAntigene(syringeStack: ItemStack, gene: Holder<Gene>): Boolean {
			if (!canAddAntigene(syringeStack, gene)) return false

			val currentAntigenes = getAntigenes(syringeStack)
			val newGenes = currentAntigenes + gene
			val holderSet = HolderSet.direct(newGenes.toList())

			syringeStack.set(ModDataComponents.ANTIGENE_SET, holderSet)

			return true
		}

		fun damageSourceStepOnSyringe(level: Level, thrower: LivingEntity?): DamageSource {
			return level.damageSources().source(ModDamageTypeTagsProvider.STEP_ON_SYRINGE, thrower)
		}

		fun damageSourceUseSyringe(level: Level, thrower: LivingEntity?): DamageSource {
			return level.damageSources().source(ModDamageTypeTagsProvider.USE_SYRINGE, thrower)
		}
	}

}