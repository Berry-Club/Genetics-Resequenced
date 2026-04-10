package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModItemLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
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

	override fun getUseDuration(itemStack: ItemStack, pHolder: LivingEntity): Int = 40
	override fun getUseAnimation(itemStack: ItemStack): UseAnim = UseAnim.BOW

	override fun use(level: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val itemStack = pPlayer.getItemInHand(pUsedHand)
		pPlayer.startUsingItem(pUsedHand)
		return InteractionResultHolder.consume(itemStack)
	}

	override fun onUseTick(level: Level, livingEntity: LivingEntity, itemStack: ItemStack, ticksRemaining: Int) {
		if (ticksRemaining <= 1) {
			livingEntity.stopUsingItem()
			releaseUsing(itemStack, level, livingEntity, ticksRemaining)
		}
	}

	override fun releaseUsing(itemStack: ItemStack, level: Level, livingEntity: LivingEntity, pTimeCharged: Int) {
		if (livingEntity !is Player) return
		if (pTimeCharged > 1) return

		if (livingEntity is FakePlayer) return

		val targetEntity = OtherUtil.getLookedAtEntity(livingEntity) as? LivingEntity ?: return

		if (hasBlood(itemStack)) {
			useFullSyringe(itemStack, livingEntity, targetEntity)
		} else {
			extractBlood(itemStack, targetEntity)

			setContaminated(itemStack, true)

			targetEntity.hurt(damageSourceUseSyringe(level, livingEntity), 1f)
			targetEntity.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 3))
		}
	}

	override fun getName(itemStack: ItemStack): Component {
		return if (hasBlood(itemStack)) {
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