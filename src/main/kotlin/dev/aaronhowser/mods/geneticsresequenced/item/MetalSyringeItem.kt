package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModItemLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer

class MetalSyringeItem(properties: Properties) : SyringeItem(properties) {

	override fun getUseDuration(pStack: ItemStack, pHolder: LivingEntity): Int = 40
	override fun getUseAnimation(pStack: ItemStack): UseAnim = UseAnim.BOW

	override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val itemStack = pPlayer.getItemInHand(pUsedHand)
		pPlayer.startUsingItem(pUsedHand)
		return InteractionResultHolder.consume(itemStack)
	}

	override fun onUseTick(pLevel: Level, pLivingEntity: LivingEntity, pStack: ItemStack, pRemainingUseDuration: Int) {
		if (pRemainingUseDuration <= 1) {
			pLivingEntity.stopUsingItem()
			releaseUsing(pStack, pLevel, pLivingEntity, pRemainingUseDuration)
		}
	}

	override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeCharged: Int) {
		if (pLivingEntity !is Player) return
		if (pTimeCharged > 1) return

		if (pLivingEntity is FakePlayer) return

		val targetEntity = OtherUtil.getLookedAtEntity(pLivingEntity) as? LivingEntity ?: return

		if (hasBlood(pStack)) {
			useFullSyringe(pStack, pLivingEntity, targetEntity)
		} else {
			extractBlood(pStack, targetEntity)

			setContaminated(pStack, true)

			targetEntity.hurt(damageSourceUseSyringe(pLevel, pLivingEntity), 1f)
			targetEntity.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 3))
		}
	}

	override fun getName(pStack: ItemStack): Component {
		return if (hasBlood(pStack)) {
			ModItemLang.METAL_SYRINGE_FULL.toComponent()
		} else {
			ModItemLang.METAL_SYRINGE_EMPTY.toComponent()
		}
	}

	companion object {
		val SYRINGE_REACH_MODIFIER_RL = GeneticsResequenced.modResource("syringe_reach_modifier")

		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.attributes(
				ItemAttributeModifiers.builder()
					.add(
						Attributes.ENTITY_INTERACTION_RANGE,
						AttributeModifier(
							SYRINGE_REACH_MODIFIER_RL,
							3.0,
							AttributeModifier.Operation.ADD_VALUE
						),
						EquipmentSlotGroup.HAND
					)
					.build()
			)

		private fun useFullSyringe(
			syringeStack: ItemStack,
			pPlayer: Player,
			pTarget: LivingEntity
		) {
			val uuid = SpecificEntityItemComponent.getEntityUuid(syringeStack)
			if (pTarget.uuid != uuid) return

			if (isContaminated(syringeStack)) {
				if (!pPlayer.level().isClientSide) {
					pPlayer.sendSystemMessage(
						ModMessageLang.METAL_SYRINGE_CONTAMINATED.toComponent()
					)
				}
				return

			}

			tryInjectBlood(syringeStack, pPlayer, pTarget)
		}

		private fun tryInjectBlood(
			syringeStack: ItemStack,
			player: Player,
			pInteractionTarget: LivingEntity
		) {
			if (player.level().isClientSide) return

			val entityUuid = SpecificEntityItemComponent.getEntityUuid(syringeStack) ?: return

			if (entityUuid != pInteractionTarget.uuid) {
				player.sendSystemMessage(ModMessageLang.METAL_SYRINGE_MISMATCH.toComponent())
				return
			}

			if (pInteractionTarget !is Player) {
				val syringeGenes = getGenes(syringeStack)
				val genesCantAdd = syringeGenes.filterNot { it.value().canEntityHave(pInteractionTarget) }
				for (geneHolder in genesCantAdd) {
					player.sendSystemMessage(
						ModMessageLang.METAL_SYRINGE_NO_MOBS.toComponent(
							geneHolder.getName()
						)
					)
				}
			}

			injectEntity(syringeStack, pInteractionTarget)

			return
		}

		private fun extractBlood(
			syringeStack: ItemStack,
			pInteractionTarget: LivingEntity
		) {
			SpecificEntityItemComponent.setEntity(syringeStack, pInteractionTarget)
		}

	}

}