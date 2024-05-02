package dev.aaronhowser.mods.geneticsresequenced.items

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

object SyringeItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
        .stacksTo(1)
) {

    fun isBeingUsed(syringeStack: ItemStack, entity: LivingEntity?): Boolean {
        if (entity == null) return false

        return entity.useItem == syringeStack
    }

    fun isFull(syringeStack: ItemStack): Boolean {
        if (!syringeStack.`is`(SyringeItem)) return false

        return syringeStack.getOrCreateTag().getBoolean("dna")
    }

    fun setFull(syringeStack: ItemStack, isFull: Boolean) {
        if (!syringeStack.`is`(SyringeItem)) return

        syringeStack.getOrCreateTag().putBoolean("dna", isFull)
    }

    override fun getUseDuration(pStack: ItemStack): Int = 40

    override fun getUseAnimation(pStack: ItemStack): UseAnim = UseAnim.BOW

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

    override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeLeft: Int) {
        if (pLivingEntity !is Player) return
        if (pTimeLeft > 1) return

        setFull(pStack, !isFull(pStack))
        pLivingEntity.cooldowns.addCooldown(this, 10)
    }

}