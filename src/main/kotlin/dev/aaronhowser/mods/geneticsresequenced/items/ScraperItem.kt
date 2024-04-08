package dev.aaronhowser.mods.geneticsresequenced.items

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

        val organicStack = ItemStack(ModItems.ORGANIC_MATTER)

        OrganicMatterItem.setMobId(
            organicStack,
            ForgeRegistries.ENTITY_TYPES.getKey(pInteractionTarget.type).toString()
        )
        OrganicMatterItem.setMobTranslationKey(organicStack, pInteractionTarget.type.toString())

        if (!pPlayer.inventory.add(organicStack)) {
            pPlayer.drop(organicStack, false)
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand)
    }

}