package dev.aaronhowser.mods.geneticsresequenced.item

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ScraperItem : Item(
    Properties()
        .durability(200)
) {

    override fun use(
        pLevel: Level,
        pPlayer: Player,
        pInteractionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        return super.use(pLevel, pPlayer, pInteractionHand)
    }

}