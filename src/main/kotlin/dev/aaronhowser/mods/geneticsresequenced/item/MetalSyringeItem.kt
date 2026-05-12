package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
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

	override fun getUseDuration(stack: ItemStack, holder: LivingEntity): Int = 40
	override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.BOW

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val itemStack = player.getItemInHand(usedHand)
		player.startUsingItem(usedHand)
		return InteractionResultHolder.consume(itemStack)
	}

	override fun releaseUsing(
		stack: ItemStack,
		level: Level,
		livingEntity: LivingEntity,
		timeCharged: Int
	) {
		if (timeCharged > 1) return
		if (livingEntity !is Player || livingEntity is FakePlayer) return

		val target = OtherUtil.getLookedAtEntity(livingEntity) ?: return

		if (hasBlood(stack)) {
			useFullSyringe(stack, livingEntity, target)
		} else {
			SpecificEntityItemComponent.setEntity(stack, target)

			setContaminated(stack, true)

			target.hurt(getUseSyringeDamageSource(level, livingEntity), 1f)
			target.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 3))
		}
	}

	override fun getName(stack: ItemStack): Component {
		return if (hasBlood(stack)) {
			ModItemLang.METAL_SYRINGE_FULL.toComponent()
		} else {
			ModItemLang.METAL_SYRINGE_EMPTY.toComponent()
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.attributes(
				ItemAttributeModifiers.builder()
					.add(
						Attributes.ENTITY_INTERACTION_RANGE,
						AttributeModifier(
							GeneticsResequenced.modResource("syringe_reach_modifier"),
							3.0,
							AttributeModifier.Operation.ADD_VALUE
						),
						EquipmentSlotGroup.HAND
					)
					.build()
			)

		private fun useFullSyringe(
			syringeStack: ItemStack,
			player: Player,
			target: LivingEntity
		) {
			val uuid = SpecificEntityItemComponent.getEntityUuid(syringeStack)
			if (target.uuid != uuid) return

			if (isContaminated(syringeStack)) {
				if (!player.level().isClientSide) {
					player.tell(
						ModMessageLang.METAL_SYRINGE_CONTAMINATED.toComponent()
					)
				}
				return

			}

			tryInjectBlood(syringeStack, player, target)
		}

		private fun tryInjectBlood(
			syringeStack: ItemStack,
			player: Player,
			target: LivingEntity
		) {
			if (player.isClientSide) return

			val entityUuid = SpecificEntityItemComponent.getEntityUuid(syringeStack) ?: return

			if (entityUuid != target.uuid) {
				player.tell(ModMessageLang.METAL_SYRINGE_MISMATCH.toComponent())
				return
			}

			if (target !is Player) {
				val syringeGenes = getGenes(syringeStack)
				val incompatibleGenes = syringeGenes.filterNot { it.value().canEntityHave(target) }
				for (geneHolder in incompatibleGenes) {
					player.tell(
						ModMessageLang.METAL_SYRINGE_NO_MOBS.toComponent(
							geneHolder.getName()
						)
					)
				}
			}

			injectEntity(syringeStack, target)

			return
		}

	}

}