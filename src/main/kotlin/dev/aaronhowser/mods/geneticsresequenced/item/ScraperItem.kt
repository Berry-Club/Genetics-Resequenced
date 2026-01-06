package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.AaronExtensions.isEntity
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModDamageTypeProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.enchantment.ModEnchantments
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem.Companion.setEntityType
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.FakePlayer

class ScraperItem(properties: Properties) : Item(properties) {

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

	override fun interactLivingEntity(
		pStack: ItemStack,
		pPlayer: Player,
		pInteractionTarget: LivingEntity,
		pUsedHand: InteractionHand
	): InteractionResult {

		if (pPlayer !is ServerPlayer) return InteractionResult.PASS

		if (pInteractionTarget.isEntity(ModEntityTypeTagsProvider.SCRAPER_ENTITY_BLACKLIST)) {
			pPlayer.sendSystemMessage(
				ModMessageLang.SCRAPER_CANT_SCRAPE.toComponent()
			)

			return InteractionResult.CONSUME
		}

		return if (scrapeEntity(pPlayer, pStack, pInteractionTarget)) {
			InteractionResult.SUCCESS
		} else {
			InteractionResult.CONSUME
		}

	}

	override fun getEnchantmentValue(stack: ItemStack): Int = 5

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().durability(200)

		private fun tryScrapeSelf(
			pPlayer: Player,
			realStack: ItemStack
		): InteractionResultHolder<ItemStack> {
			if (pPlayer is FakePlayer || pPlayer !is ServerPlayer) return InteractionResultHolder.pass(realStack)

			val scrapeWorked = scrapeEntity(pPlayer, realStack, pPlayer)

			return if (scrapeWorked) {
				InteractionResultHolder.success(realStack)
			} else {
				InteractionResultHolder.pass(realStack)
			}
		}

		private fun scrapeEntity(
			player: ServerPlayer,
			stack: ItemStack,
			target: Entity
		): Boolean {

			if (player.cooldowns.isOnCooldown(ModItems.SCRAPER.get())) return false
			if (target is LivingEntity && target.hurtTime > 0) return false

			val organicStack = ModItems.ORGANIC_MATTER.getDefaultInstance()
			val successfullySetEntity = setEntityType(organicStack, target.type)

			if (!successfullySetEntity) {
				player.displayClientMessage(
					ModMessageLang.SCRAPER_CANT_SCRAPE.toComponent(target.type.description),
					true
				)
				return false
			}

			if (!player.inventory.add(organicStack)) {
				player.drop(organicStack, false)
			}


			val hasDelicateTouch =
				stack.getEnchantmentLevel(ModEnchantments.getDelicateTouchHolder(player).get()) != 0

			// Only put on cooldown if the entity was not damaged
			if (hasDelicateTouch) {
				player.cooldowns.addCooldown(ModItems.SCRAPER.get(), 10)
			} else {
				target.hurt(getDamageSource(player.level(), player), 1f)
			}

			stack.hurtAndBreak(1, player) {}

			return true
		}

		private fun getDamageSource(level: Level, source: LivingEntity? = null): DamageSource {
			return level.damageSources().source(ModDamageTypeProvider.USE_SCRAPER, source)
		}
	}

}