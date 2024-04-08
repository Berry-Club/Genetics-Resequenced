package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.ModTags
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraftforge.registries.ForgeRegistries

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

        val mobResourceLocation = ForgeRegistries.ENTITY_TYPES.getKey(pInteractionTarget.type)
        if (mobResourceLocation == null) {
            GeneticsResequenced.LOGGER.error("Failed to get mob id for ${pInteractionTarget.type}")
            return InteractionResult.FAIL
        }

        OrganicMatterItem.setMobRl(organicStack, mobResourceLocation)

        if (!pPlayer.inventory.add(organicStack)) {
            pPlayer.drop(organicStack, false)
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand)
    }

}