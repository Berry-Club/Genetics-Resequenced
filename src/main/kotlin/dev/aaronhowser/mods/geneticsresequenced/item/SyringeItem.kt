package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.GenesItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent.Companion.hasEntity
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer

class SyringeItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {

        fun Item.isSyringe(): Boolean {
            return this == ModItems.SYRINGE.get() || this == ModItems.METAL_SYRINGE.get()
        }

        fun ItemStack.isSyringe(): Boolean {
            return this.item.isSyringe()
        }

        fun isBeingUsed(syringeStack: ItemStack, entity: LivingEntity?): Boolean {
            if (entity == null) return false

            return entity.useItem == syringeStack
        }

        fun hasBlood(syringeStack: ItemStack): Boolean {
            return syringeStack.hasEntity()
        }

        fun getGenes(syringeStack: ItemStack): Set<Gene> {
            return syringeStack.get(GenesItemComponent.component)?.genes ?: emptySet()
        }

        fun canAddGene(syringeStack: ItemStack, gene: Gene): Boolean {
            return hasBlood(syringeStack) && !getGenes(syringeStack).contains(gene)
        }

        fun addGene(syringeStack: ItemStack, gene: Gene): Boolean {
            if (!canAddGene(syringeStack, gene)) return false

            val currentGenes = getGenes(syringeStack)
            val newGenes = currentGenes + gene
            val newComponent = GenesItemComponent(newGenes)

            syringeStack.set(GenesItemComponent.component, newComponent)

            return true
        }

        private fun clearGenes(syringeStack: ItemStack) {
            syringeStack.remove(GenesItemComponent.component)
        }

        fun isContaminated(syringeStack: ItemStack): Boolean {
            return syringeStack.get(BooleanItemComponent.isContaminatedComponent)?.value ?: false
        }

        fun setContaminated(syringeStack: ItemStack, value: Boolean) {
            syringeStack.set(BooleanItemComponent.isContaminatedComponent, BooleanItemComponent(value))
        }

    }

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

        if (pLivingEntity !is Player || pTimeCharged < 1) return
        if (pLivingEntity is FakePlayer) return


    }

}