package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.getUuidOrNull
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.FakePlayer
import java.util.*

object SyringeItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
        .stacksTo(1)
) {

    fun isBeingUsed(syringeStack: ItemStack, entity: LivingEntity?): Boolean {
        if (entity == null) return false

        return entity.useItem == syringeStack
    }

    private const val ENTITY_UUID_NBT_KEY = "entity"
    private fun setEntity(syringeStack: ItemStack, entity: LivingEntity) {
        if (!syringeStack.`is`(SyringeItem)) return

        val tag = syringeStack.getOrCreateTag()

        tag.putUUID(ENTITY_UUID_NBT_KEY, entity.uuid)
    }

    private fun injectEntity(syringeStack: ItemStack, entity: LivingEntity) {
        val entityDna = getEntity(syringeStack) ?: return
        if (entity.uuid != entityDna) return
        syringeStack.getOrCreateTag().remove(ENTITY_UUID_NBT_KEY)
    }

    private fun getEntity(syringeStack: ItemStack): UUID? {
        if (!syringeStack.`is`(SyringeItem)) return null

        return syringeStack.getOrCreateTag().getUuidOrNull(ENTITY_UUID_NBT_KEY)
    }

    fun hasBlood(syringeStack: ItemStack): Boolean {
        return getEntity(syringeStack) != null
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

        if (pLivingEntity is FakePlayer) return

        if (hasBlood(pStack)) {
            injectEntity(pStack, pLivingEntity)
        } else {
            setEntity(pStack, pLivingEntity)
        }

        pLivingEntity.cooldowns.addCooldown(this, 10)
    }

}