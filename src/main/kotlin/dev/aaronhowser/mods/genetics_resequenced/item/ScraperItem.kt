package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.giveOrDropStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.hasEnchantment
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.status
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.genetics_resequenced.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.datapack.ModEnchantmentProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem.Companion.setEntityType
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer

class ScraperItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResult {
		val stack = player.getItemInHand(usedHand)

		if (player !is ServerPlayer) return InteractionResult.PASS

		// If the player is sneaking, try to scrape themselves
		if (player.isCrouching) return tryScrapeSelf(player, stack)

		val lookedAtEntity = OtherUtil.getLookedAtEntity(player) ?: return InteractionResult.PASS
		val scrapeWorked = scrapeEntity(player, stack, lookedAtEntity)

		return if (scrapeWorked) {
			InteractionResult.SUCCESS
		} else {
			InteractionResult.PASS
		}
	}

	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		target: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		if (player !is ServerPlayer) return InteractionResult.PASS

		if (target.isEntity(ModEntityTypeTagsProvider.SCRAPER_ENTITY_BLACKLIST)) {
			player.tell(
				ModMessageLang.SCRAPER_CANT_SCRAPE.toComponent()
			)

			return InteractionResult.FAIL
		}

		return if (scrapeEntity(player, stack, target)) {
			InteractionResult.SUCCESS
		} else {
			InteractionResult.CONSUME
		}

	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().durability(200).enchantable(5)

		private fun tryScrapeSelf(
			player: Player,
			stack: ItemStack
		): InteractionResult {
			if (player is FakePlayer) return InteractionResult.PASS
			if (player !is ServerPlayer) return InteractionResult.PASS

			val scrapeWorked = scrapeEntity(player, stack, player)

			return if (scrapeWorked) {
				InteractionResult.SUCCESS
			} else {
				InteractionResult.PASS
			}
		}

		private fun scrapeEntity(
			player: ServerPlayer,
			stack: ItemStack,
			target: LivingEntity
		): Boolean {
			if (target.hurtTime > 0) return false

			val organicStack = ModItems.ORGANIC_MATTER.toStack()
			val successfullySetEntity = setEntityType(organicStack, target.type)

			if (!successfullySetEntity) {
				player.status(
					ModMessageLang.SCRAPER_CANT_SCRAPE.toComponent(target.type.description)
				)

				return false
			}

			val surgicalPrecision = OtherUtil.getEnchantHolder(player, ModEnchantmentProvider.SURGICAL_PRECISION)
			organicStack.count += EnchantmentHelper.getItemEnchantmentLevel(surgicalPrecision, stack)
			player.giveOrDropStack(organicStack)

			val delicateTouch = OtherUtil.getEnchantHolder(player, ModEnchantmentProvider.DELICATE_TOUCH)
			val hasDelicateTouch = stack.hasEnchantment(delicateTouch)

			if (!hasDelicateTouch) {
				target.hurtServer(player.level(), getUseDamageSource(player.level(), player), 1f)
			}

			val equipmentSlot = player.getEquipmentSlotForItem(stack)
			stack.hurtAndBreak(1, player, equipmentSlot)

			return true
		}

		private fun getUseDamageSource(level: Level, source: LivingEntity? = null): DamageSource {
			return level.damageSources().source(ModDamageTypeProvider.USE_SCRAPER, source)
		}
	}

}
