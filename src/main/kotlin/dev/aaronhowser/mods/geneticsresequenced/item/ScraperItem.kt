package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.giveOrDropStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.hasEnchantment
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.status
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.geneticsresequenced.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.datapack.ModEnchantmentProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem.Companion.setEntityType
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer

class ScraperItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val stack = player.getItemInHand(usedHand)

		if (player !is ServerPlayer) return InteractionResultHolder.pass(stack)

		// If the player is sneaking, try to scrape themselves
		if (player.isCrouching) return tryScrapeSelf(player, stack)

		val lookedAtEntity = OtherUtil.getLookedAtEntity(player) ?: return InteractionResultHolder.pass(stack)
		val scrapeWorked = scrapeEntity(player, stack, lookedAtEntity)

		return if (scrapeWorked) {
			InteractionResultHolder.success(stack)
		} else {
			InteractionResultHolder.pass(stack)
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

	override fun getEnchantmentValue(stack: ItemStack): Int = 5

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().durability(200)

		private fun tryScrapeSelf(
			player: Player,
			stack: ItemStack
		): InteractionResultHolder<ItemStack> {
			if (player is FakePlayer) return InteractionResultHolder.pass(stack)
			if (player !is ServerPlayer) return InteractionResultHolder.pass(stack)

			val scrapeWorked = scrapeEntity(player, stack, player)

			return if (scrapeWorked) {
				InteractionResultHolder.success(stack)
			} else {
				InteractionResultHolder.pass(stack)
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

			player.giveOrDropStack(organicStack)

			val delicateTouch = OtherUtil.getEnchantHolder(player, ModEnchantmentProvider.DELICATE_TOUCH)
			val hasDelicateTouch = stack.hasEnchantment(delicateTouch)

			if (!hasDelicateTouch) {
				target.hurt(getUseDamageSource(player.level(), player), 1f)
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