package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.enchantments.ModEnchantments
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object ScraperItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
        .defaultDurability(200)
) {

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {

        if (pInteractionTarget.hurtTime > 0) return InteractionResult.FAIL
        if (pPlayer.cooldowns.isOnCooldown(this)) return InteractionResult.FAIL

        if (pInteractionTarget.type.`is`(ModTags.SCRAPER_ENTITY_BLACKLIST)) {
            if (pPlayer.level.isClientSide) return InteractionResult.FAIL
            val component = Component.translatable("message.geneticsresequenced.scraper.cant_scrape")
            pPlayer.sendSystemMessage(component)
            return InteractionResult.FAIL
        }

        val organicStack =
            ItemStack(ModItems.ORGANIC_MATTER).setMob(pInteractionTarget.type) ?: return InteractionResult.FAIL

        if (!pPlayer.inventory.add(organicStack)) {
            pPlayer.drop(organicStack, false)
        }

        if (pStack.getEnchantmentLevel(ModEnchantments.DELICATE_TOUCH) == 0) {
            pInteractionTarget.hurt(DamageSource.playerAttack(pPlayer), 1f)
        } else {
            pPlayer.cooldowns.addCooldown(this, 10)
        }
        pStack.hurtAndBreak(1, pPlayer) { pPlayer.broadcastBreakEvent(pUsedHand) }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand)
    }

}