package dev.aaronhowser.mods.geneticsresequenced.items

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.FakePlayer


class MetalSyringeItem : SyringeItem() {

    companion object {

        private fun useFullSyringe(pStack: ItemStack, pPlayer: Player, pTarget: LivingEntity, pLevel: Level) {
            val syringeEntityUuid = getEntityUuid(pStack) ?: return
            if (pTarget.uuid != syringeEntityUuid) return

            if (isContaminated(pStack)) {
                if (!pLevel.isClientSide) {
                    pPlayer.sendSystemMessage(
                        Component.translatable("message.geneticsresequenced.metal_syringe.contaminated")
                    )
                }
                return
            }

            tryInjectBlood(pStack, pPlayer, pTarget)
        }

        private fun tryInjectBlood(
            pStack: ItemStack,
            pPlayer: Player,
            pInteractionTarget: LivingEntity
        ): InteractionResult {

            val stackEntityUuid = getEntityUuid(pStack)
            val targetEntityUuid = pInteractionTarget.uuid
            if (stackEntityUuid == null || targetEntityUuid != stackEntityUuid) {
                return InteractionResult.PASS
            }

            fun sendMessage(message: Component) {
                if (!pPlayer.level.isClientSide) {
                    pPlayer.sendSystemMessage(message)
                }
            }

            val bloodOwner = getEntityName(pStack)?.string
            val entityName = pInteractionTarget.name.string
            if (bloodOwner != entityName) {
                val component =
                    Component.translatable("message.geneticsresequenced.syringe.blood_mismatch", bloodOwner, entityName)
                sendMessage(component)
                return InteractionResult.PASS
            }

            if (isContaminated(pStack)) {
                val component = Component.translatable("message.geneticsresequenced.metal_syringe.contaminated")
                sendMessage(component)
                return InteractionResult.PASS
            }

            val genes = getGenes(pStack)
            val badGenes = genes.filter { !it.canMobsHave }
            for (badGene in badGenes) {
                val component =
                    Component.translatable(
                        "message.geneticsresequenced.syringe.failed.mobs_cant_have",
                        badGene.nameComponent
                    )
                sendMessage(component)
            }

            injectEntity(pStack, pInteractionTarget)
            return InteractionResult.SUCCESS
        }

        private fun extractBlood(
            pStack: ItemStack,
            pInteractionTarget: LivingEntity
        ): InteractionResult {
            setEntity(pStack, pInteractionTarget)
            return InteractionResult.SUCCESS
        }
    }

    override fun inventoryTick(pStack: ItemStack, pLevel: Level, pEntity: Entity, pSlotId: Int, pIsSelected: Boolean) {
        if (!pIsSelected) return
        if (pEntity !is Player) return

        if (pEntity.tickCount % 40 != 0) return

        if (!hasBlood(pStack)) return

        val entityUuid = getEntityUuid(pStack)

        val nearbyEntities =
            pLevel.getNearbyEntities(
                LivingEntity::class.java,
                TargetingConditions.DEFAULT,
                pEntity,
                pEntity.boundingBox.inflate(50.0)
            )

        val target = nearbyEntities.find { it.uuid == entityUuid }

        target?.addEffect(MobEffectInstance(MobEffects.GLOWING, 100, 0, false, false, false))
    }

    override fun getUseDuration(pStack: ItemStack): Int = 40

    override fun getUseAnimation(pStack: ItemStack): UseAnim = UseAnim.BOW

    override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeLeft: Int) {
        if (pLivingEntity !is Player) return
        if (pTimeLeft > 1) return

        if (pLivingEntity is FakePlayer) return

        val entityHitResult = ProjectileUtil.getEntityHitResult(
            pLivingEntity,
            pLivingEntity.eyePosition,
            pLivingEntity.eyePosition.add(pLivingEntity.lookAngle.scale(pLivingEntity.reachDistance)),
            pLivingEntity.boundingBox.inflate(pLivingEntity.reachDistance),
            { true },
            pLivingEntity.reachDistance
        ) ?: return

        val targetEntity = entityHitResult.entity as? LivingEntity ?: return

        if (hasBlood(pStack)) {
            useFullSyringe(pStack, pLivingEntity, targetEntity, pLevel)
        } else {
            extractBlood(pStack, targetEntity)

            targetEntity.apply {
                hurt(damageSourceUseSyringe(pLivingEntity), 1f)
                addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 3))
            }

        }

    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = pPlayer.getItemInHand(pUsedHand)
        pPlayer.startUsingItem(pUsedHand)
        return InteractionResultHolder.consume(itemStack)
    }

    override fun onUsingTick(stack: ItemStack?, player: LivingEntity?, count: Int) {
        if (stack == null) return
        if (player == null) return

        if (count <= 1) {
            player.stopUsingItem()
            releaseUsing(stack, player.level, player, count)
        }
    }

    override fun getName(pStack: ItemStack): Component {
        return if (hasBlood(pStack)) {
            Component.translatable("item.geneticsresequenced.metal_syringe.full")
        } else {
            super.getName(pStack)
        }
    }

}