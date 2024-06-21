package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
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
            player: Player,
            stack: ItemStack,
            hand: InteractionHand,
            target: LivingEntity
        ): Boolean {

            if (player.cooldowns.isOnCooldown(ModItems.SCRAPER.get())) return false
            if (target.hurtTime > 0) return false

            val organicStack = ModItems.ORGANIC_MATTER.itemStack.setMob(target.type)

            if (!player.inventory.add(organicStack)) {
                player.drop(organicStack, false)
            }

            // Only put on cooldown if the entity was not damaged
//            if (stack.getEnchantmentLevel(ModEnchantments.DELICATE_TOUCH) == 0) {
//                entity.hurt(damageSourceScraper(player), 1f)
//            } else {
            player.cooldowns.addCooldown(ModItems.SCRAPER.get(), 10)
//            }

            val equipmentSlot = player.getEquipmentSlotForItem(stack)

            stack.hurtAndBreak(1, player, equipmentSlot)

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

        val scrapeWorked = scrapeEntity(pPlayer, realStack, pInteractionHand, pPlayer)

        return if (scrapeWorked) {
            InteractionResultHolder.success(realStack)
        } else {
            InteractionResultHolder.pass(realStack)
        }
    }

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {

        if (pInteractionTarget.type.`is`(ModTags.SCRAPER_ENTITY_BLACKLIST)) {
            if (!pPlayer.level().isClientSide) {
                pPlayer.sendSystemMessage(
                    Component.translatable("message.geneticsresequenced.scraper.cant_scrape")
                )
            }

            return InteractionResult.FAIL
        }

        return if (scrapeEntity(pPlayer, pStack, pUsedHand, pInteractionTarget)) {
            InteractionResult.SUCCESS
        } else {
            InteractionResult.PASS
        }

    }

}