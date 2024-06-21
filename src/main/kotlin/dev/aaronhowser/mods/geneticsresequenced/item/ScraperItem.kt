package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer

class ScraperItem : Item(
    Properties()
        .durability(200)
) {

    companion object {

        private fun scrapeEntity(
            user: Player,
            stack: ItemStack,
            hand: InteractionHand,
            target: LivingEntity
        ): Boolean {

            if (user.cooldowns.isOnCooldown(ModItems.SCRAPER.get())) return false
            if (target.hurtTime > 0) return false


            return true
        }

    }

    // When not used on an entity, target self instead
    override fun use(
        pLevel: Level,
        pPlayer: Player,
        pInteractionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val realStack = pPlayer.getItemInHand(pInteractionHand)
        if (!pPlayer.isCrouching || pPlayer is FakePlayer) return InteractionResultHolder.pass(realStack)

        return super.use(pLevel, pPlayer, pInteractionHand)
    }

}