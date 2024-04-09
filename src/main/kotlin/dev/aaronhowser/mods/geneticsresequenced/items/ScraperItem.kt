package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object ScraperItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
        .stacksTo(1)
) {

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {

        if (pInteractionTarget.type.`is`(ModTags.SCRAPER_BLACKLIST)) {
            if (pPlayer.level.isClientSide) return InteractionResult.FAIL
            val component = Component.literal("This mob cannot be scraped.")
            pPlayer.sendSystemMessage(component)
            return InteractionResult.FAIL
        }

        val organicStack = ItemStack(ModItems.ORGANIC_MATTER)
        val mobSetSuccessful = EntityDnaItem.setMob(organicStack, pInteractionTarget.type)

        if (!mobSetSuccessful) return InteractionResult.FAIL

        if (!pPlayer.inventory.add(organicStack)) {
            pPlayer.drop(organicStack, false)
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand)
    }

}