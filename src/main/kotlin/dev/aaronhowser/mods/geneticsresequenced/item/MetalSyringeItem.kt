package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer

class MetalSyringeItem : SyringeItem() {

    companion object {

        private fun useFullSyringe(
            syringeStack: ItemStack,
            pPlayer: Player,
            pTarget: LivingEntity
        ) {
            val uuid = syringeStack.get(SpecificEntityItemComponent.component)?.entityUuid ?: return
            if (pTarget.uuid != uuid) return

            if (isContaminated(syringeStack)) {
                if (!pPlayer.level().isClientSide) {
                    pPlayer.sendSystemMessage(
                        ModLanguageProvider.Messages.METAL_SYRINGE_CONTAMINATED.toComponent()
                    )
                }
                return

            }

            tryInjectBlood(syringeStack, pPlayer, pTarget)
        }

        private fun tryInjectBlood(
            syringeStack: ItemStack,
            pPlayer: Player,
            pInteractionTarget: LivingEntity
        ) {

            val component = syringeStack.get(SpecificEntityItemComponent.component) ?: return
            val entityUuid = component.entityUuid

            fun sendMessage(message: Component) {
                if (!pPlayer.level().isClientSide) {
                    pPlayer.sendSystemMessage(message)
                }
            }

            if (entityUuid != pInteractionTarget.uuid) {
                sendMessage(ModLanguageProvider.Messages.METAL_SYRINGE_MISMATCH.toComponent())
                return
            }

            if (pInteractionTarget !is Player) {

                val syringeGenes = getGenes(syringeStack)
                val genesCantAdd = syringeGenes.filter { it.isNegative }
                for (gene in genesCantAdd) {
                    sendMessage(
                        ModLanguageProvider.Messages.METAL_SYRINGE_NO_MOBS.toComponent(
                            gene.nameComponent
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
            val itemComponent = SpecificEntityItemComponent(pInteractionTarget.uuid, pInteractionTarget.name.string)
            syringeStack.set(SpecificEntityItemComponent.component, itemComponent)
        }

    }

    override fun inventoryTick(pStack: ItemStack, pLevel: Level, pEntity: Entity, pSlotId: Int, pIsSelected: Boolean) {
        if (!pIsSelected) return
        if (pEntity !is Player) return

        if (pEntity.tickCount % 40 != 0) return

        if (!hasBlood(pStack)) return

        val entityUuid = pStack.get(SpecificEntityItemComponent.component)?.entityUuid ?: return
        val target = OtherUtil.getNearbyEntityFromUuid(entityUuid, pEntity) ?: return

        target.addEffect(
            MobEffectInstance(
                MobEffects.GLOWING,
                40 * 3,
                0,
                false,
                false,
                false
            )
        )
    }

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

    override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeLeft: Int) {
        if (pLivingEntity !is Player) return
        if (pTimeLeft > 1) return

        if (pLivingEntity is FakePlayer) return

        val reachDistance = pLivingEntity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE)?.value ?: 3.0

        val entityHitResult = ProjectileUtil.getEntityHitResult(
            pLivingEntity,
            pLivingEntity.eyePosition,
            pLivingEntity.eyePosition.add(pLivingEntity.lookAngle.scale(reachDistance)),
            pLivingEntity.boundingBox.inflate(reachDistance),
            { true },
            reachDistance
        ) ?: return

        val targetEntity = entityHitResult.entity as? LivingEntity ?: return

        if (hasBlood(pStack)) {
            useFullSyringe(pStack, pLivingEntity, targetEntity)
        } else {
            extractBlood(pStack, targetEntity)

            targetEntity.apply {
//                hurt(damageSourceUseSyringe(pLivingEntity), 1f) TODO
                addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 3))
            }

        }
    }

    override fun getName(pStack: ItemStack): Component {
        return if (hasBlood(pStack)) {
            ModLanguageProvider.Items.METAL_SYRINGE_FULL.toComponent()
        } else {
            ModLanguageProvider.Items.METAL_SYRINGE_EMPTY.toComponent()
        }
    }

}