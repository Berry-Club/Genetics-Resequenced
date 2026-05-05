package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.setUnit
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModItemLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModDamageTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer
import java.util.*

open class SyringeItem(properties: Properties) : Item(properties) {

	override fun getUseDuration(pStack: ItemStack, pHolder: LivingEntity): Int = 40
	override fun getUseAnimation(pStack: ItemStack): UseAnim = UseAnim.BOW

	override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val realStack = pPlayer.getItemInHand(pUsedHand)
		pPlayer.startUsingItem(pUsedHand)
		return InteractionResultHolder.consume(realStack)
	}

	override fun onUseTick(pLevel: Level, pLivingEntity: LivingEntity, pStack: ItemStack, pRemainingUseDuration: Int) {

		if (pRemainingUseDuration <= 1) {
			pLivingEntity.stopUsingItem()
			releaseUsing(pStack, pLevel, pLivingEntity, pRemainingUseDuration)
		}

	}

	override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeCharged: Int) {

		if (pLivingEntity !is Player || pTimeCharged > 1) return
		if (pLivingEntity is FakePlayer) return

		if (isContaminated(pStack)) {
			if (!pLevel.isClientSide) {
				pLivingEntity.sendSystemMessage(
					ModMessageLang.SYRINGE_CONTAMINATED.toComponent()
				)
			}
			return
		}

		if (hasBlood(pStack)) {
			injectEntity(pStack, pLivingEntity)
		} else {
			setEntity(pStack, pLivingEntity)
		}

		pLivingEntity.apply {
			hurt(damageSourceUseSyringe(pLevel, pLivingEntity), 1f)
			addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 3))

			cooldowns.addCooldown(ModItems.SYRINGE.get(), 10)
		}
	}

	override fun getName(pStack: ItemStack): Component {
		return if (hasBlood(pStack)) {
			ModItemLang.SYRINGE_FULL.toComponent()
		} else {
			ModItemLang.SYRINGE_EMPTY.toComponent()
		}
	}

	override fun appendHoverText(
		pStack: ItemStack,
		pContext: TooltipContext,
		pTooltipComponents: MutableList<Component>,
		pTooltipFlag: TooltipFlag
	) {

		val bloodOwner = getEntityName(pStack)
		if (hasBlood(pStack) && bloodOwner != null) {
			pTooltipComponents.add(
				ModTooltipLang.SYRINGE_OWNER
					.toComponent(bloodOwner)
					.withStyle(ChatFormatting.GRAY)
			)
		}

		if (isContaminated(pStack)) {
			pTooltipComponents.add(
				ModTooltipLang.SYRINGE_CONTAMINATED
					.toComponent()
					.withStyle(ChatFormatting.DARK_GREEN)
			)
		}

		val addingGenes = getGenes(pStack)
		if (addingGenes.isNotEmpty()) {
			pTooltipComponents.add(
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

				pTooltipComponents.add(component)
			}
		}

		val removingGenes = getAntigenes(pStack)
		if (removingGenes.isNotEmpty()) {
			pTooltipComponents.add(
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

				pTooltipComponents.add(component)
			}
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		fun ItemStack.isSyringe(): Boolean = this.isItem(ModItemTagsProvider.SYRINGES)

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

			syringeStack.remove(ModDataComponents.GENE_SET)
			syringeStack.remove(ModDataComponents.ANTIGENE_SET)

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
					entity.sendSystemMessage(
						ModMessageLang.SYRINGE_REMOVE_GENES_SUCCESS.toComponent(
							removedGeneHolder.getName()
						)
					)
				}

				for (notRemovedGeneHolder in genesNotRemoved) {
					entity.sendSystemMessage(
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
					entity.sendSystemMessage(
						ModMessageLang.SYRINGE_INJECTED.toComponent(
							Gene.getNameComponent(addedGeneHolder)
						)
					)
				}

				for (notAddedGeneHolder in genesNotAdded) {
					entity.sendSystemMessage(
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

		fun isContaminated(syringeStack: ItemStack): Boolean {
			return syringeStack.has(ModDataComponents.IS_CONTAMINATED)
		}

		fun setContaminated(syringeStack: ItemStack, value: Boolean) {
			if (value) {
				syringeStack.setUnit(ModDataComponents.IS_CONTAMINATED)
			} else {
				syringeStack.remove(ModDataComponents.IS_CONTAMINATED)
			}
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
			return level.damageSources().source(ModDamageTypeProvider.STEP_ON_SYRINGE, thrower)
		}

		fun damageSourceUseSyringe(level: Level, thrower: LivingEntity?): DamageSource {
			return level.damageSources().source(ModDamageTypeProvider.USE_SYRINGE, thrower)
		}
	}

}