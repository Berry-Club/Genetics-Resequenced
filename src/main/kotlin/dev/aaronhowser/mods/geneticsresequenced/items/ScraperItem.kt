package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.enchantments.ModEnchantments
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.EntityDamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.FakePlayer

class ScraperItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
        .defaultDurability(200)
) {

    companion object {
        private fun scrapeEntity(
            player: Player,
            stack: ItemStack,
            hand: InteractionHand,
            entity: LivingEntity
        ): Boolean {
            if (player.cooldowns.isOnCooldown(ModItems.SCRAPER.get())) return false
            if (entity.hurtTime > 0) return false

            val organicStack = ItemStack(ModItems.ORGANIC_MATTER.get()).setMob(entity.type) ?: return false

            if (!player.inventory.add(organicStack)) {
                player.drop(organicStack, false)
            }

            // Only put on cooldown if the entity was not damaged
            if (stack.getEnchantmentLevel(ModEnchantments.DELICATE_TOUCH) == 0) {
                entity.hurt(damageSource(player), 1f)
            } else {
                player.cooldowns.addCooldown(ModItems.SCRAPER.get(), 10)
            }

            stack.hurtAndBreak(1, player) { player.broadcastBreakEvent(hand) }

            return true
        }

        fun damageSource(player: Player?): DamageSource {
            return if (player == null) {
                DamageSource("scraper")
            } else {
                //TODO: Implement "%2$s" into the kill message
                EntityDamageSource("scraper", player)
            }
        }
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {

        val stack = pPlayer.getItemInHand(pUsedHand)
        if (!pPlayer.isCrouching || pPlayer is FakePlayer) return InteractionResultHolder.pass(stack)

        val scrapeWorked = scrapeEntity(pPlayer, stack, pUsedHand, pPlayer)

        return if (scrapeWorked) {
            InteractionResultHolder.success(stack)
        } else {
            InteractionResultHolder.pass(stack)
        }
    }

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {

        if (pInteractionTarget.type.`is`(ModTags.SCRAPER_ENTITY_BLACKLIST)) {
            if (pPlayer.level.isClientSide) return InteractionResult.FAIL
            val component = Component.translatable("message.geneticsresequenced.scraper.cant_scrape")
            pPlayer.sendSystemMessage(component)
            return InteractionResult.FAIL
        }

        return if (scrapeEntity(pPlayer, pStack, pUsedHand, pInteractionTarget)) {
            InteractionResult.SUCCESS
        } else {
            InteractionResult.FAIL
        }
    }

}