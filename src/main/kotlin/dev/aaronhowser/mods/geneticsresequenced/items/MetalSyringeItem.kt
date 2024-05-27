package dev.aaronhowser.mods.geneticsresequenced.items

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
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

            val stackEntityUuid = getEntity(pStack)
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
                    Component.translatable("message.geneticsresequenced.syringe.failed.mobs_cant_have", badGene.nameComponent)
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