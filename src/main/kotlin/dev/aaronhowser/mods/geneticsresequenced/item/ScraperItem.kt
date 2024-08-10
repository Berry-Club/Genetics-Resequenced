package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.enchantment.ModEnchantments
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem.Companion.setEntityType
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
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
            player: ServerPlayer,
            stack: ItemStack,
            target: Entity
        ): Boolean {

            if (player.cooldowns.isOnCooldown(ModItems.SCRAPER.get())) return false
            if (target is LivingEntity && target.hurtTime > 0) return false

            val organicStack = ModItems.ORGANIC_MATTER.toStack()
            val successfullySetEntity = setEntityType(organicStack, target.type)

            if (!successfullySetEntity) {
                player.displayClientMessage(
                    ModLanguageProvider.Messages.SCRAPER_CANT_SCRAPE.toComponent(target.type.description),
                    true
                )
                return false
            }

            if (!player.inventory.add(organicStack)) {
                player.drop(organicStack, false)
            }


            val hasDelicateTouch =
                stack.getEnchantmentLevel(ModEnchantments.getDelicateTouchHolder(player)) != 0

            // Only put on cooldown if the entity was not damaged
            if (hasDelicateTouch) {
                player.cooldowns.addCooldown(ModItems.SCRAPER.get(), 10)
            } else {
                target.hurt(getDamageSource(player.level(), player), 1f)
            }

            val equipmentSlot = player.getEquipmentSlotForItem(stack)

            stack.hurtAndBreak(1, player, equipmentSlot)

            return true
        }

        private val useSyringeDamageKey: ResourceKey<DamageType> =
            ResourceKey.create(Registries.DAMAGE_TYPE, OtherUtil.modResource("use_scraper"))

        private fun getDamageSource(level: Level, source: LivingEntity? = null): DamageSource {
            return level.damageSources().source(useSyringeDamageKey, source)
        }
    }

    override fun use(
        pLevel: Level,
        pPlayer: Player,
        pInteractionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val realStack = pPlayer.getItemInHand(pInteractionHand)

        if (pLevel.isClientSide) return InteractionResultHolder.pass(realStack)

        // If the player is sneaking, try to scrape themselves
        if (pPlayer.isCrouching) return tryScrapeSelf(pPlayer, realStack)

        val lookedAtEntity = OtherUtil.getLookedAtEntity(pPlayer) ?: return InteractionResultHolder.pass(realStack)
        val scrapeWorked = scrapeEntity(pPlayer as ServerPlayer, realStack, lookedAtEntity)

        return if (scrapeWorked) {
            InteractionResultHolder.success(realStack)
        } else {
            InteractionResultHolder.pass(realStack)
        }
    }

    private fun tryScrapeSelf(
        pPlayer: Player,
        realStack: ItemStack
    ): InteractionResultHolder<ItemStack> {
        if (pPlayer is FakePlayer) return InteractionResultHolder.pass(realStack)
        if (pPlayer !is ServerPlayer) return InteractionResultHolder.pass(realStack)

        val scrapeWorked = scrapeEntity(pPlayer, realStack, pPlayer)

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

        if (pPlayer !is ServerPlayer) return InteractionResult.PASS

        if (pInteractionTarget.type.`is`(ModTags.SCRAPER_ENTITY_BLACKLIST)) {
            pPlayer.sendSystemMessage(
                ModLanguageProvider.Messages.SCRAPER_CANT_SCRAPE.toComponent()
            )

            return InteractionResult.CONSUME
        }

        return if (scrapeEntity(pPlayer, pStack, pInteractionTarget)) {
            InteractionResult.SUCCESS
        } else {
            InteractionResult.CONSUME
        }

    }

}