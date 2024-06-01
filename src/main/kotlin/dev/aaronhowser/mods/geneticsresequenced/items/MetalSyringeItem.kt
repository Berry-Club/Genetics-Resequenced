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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level


class MetalSyringeItem : SyringeItem() {

    companion object {
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

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {

        val realStack = pPlayer.getItemInHand(pUsedHand)

        return if (hasBlood(realStack)) {
            tryInjectBlood(realStack, pPlayer, pInteractionTarget)
        } else {
            extractBlood(realStack, pInteractionTarget)
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

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand))
    }

    override fun getName(pStack: ItemStack): Component {
        return if (hasBlood(pStack)) {
            Component.translatable("item.geneticsresequenced.metal_syringe.full")
        } else {
            super.getName(pStack)
        }
    }

}