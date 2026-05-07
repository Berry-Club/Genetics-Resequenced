package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.setUnit
import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.attachment.GeneCooldowns
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.ShearedPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.entity.animal.goat.Goat
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.Arrow
import net.minecraft.world.entity.projectile.SmallFireball
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.ProjectileWeaponItem
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

object ClickGenes {

	fun handleWoolyOther(event: PlayerInteractEvent.EntityInteract) {
		val target = event.target as? LivingEntity ?: return
		val clicker = event.entity

		val level = target.level()
		if (level.isClientSide) return

		val clickedWithShears = event.itemStack.isItem(Tags.Items.TOOLS_SHEAR)
		if (!clickedWithShears) return

		if (!target.hasGene(ModGenes.WOOLY)) return

		val newlySheared = GeneCooldowns.addCooldown(
			target,
			ModGenes.WOOLY.getHolderOrThrow(target.registryAccess()),
			ServerConfig.CONFIG.woolyCooldown.get()
		)

		if (!newlySheared) {
			clicker.sendSystemMessage(ModMessageLang.RECENT_WOOLY.toComponent())
			return
		}

		val woolEntity = ItemEntity(
			level,
			target.eyePosition.x,
			target.eyePosition.y,
			target.eyePosition.z,
			Blocks.WHITE_WOOL.getDefaultInstance()
		)

		level.addFreshEntity(woolEntity)

		woolEntity.setDeltaMovement(
			level.random.nextRange(-0.05, 0.05),
			level.random.nextRange(0.05, 0.1),
			level.random.nextRange(-0.05, 0.05)
		)

		event.itemStack.hurtAndBreak(1, clicker, clicker.getEquipmentSlotForItem(event.itemStack))

		level.playSound(
			null,
			target,
			SoundEvents.SHEEP_SHEAR,
			SoundSource.PLAYERS,
			1.0f,
			1.0f
		)

		if (target is ServerPlayer) {
			val packet = ShearedPacket(removingSkin = true)
			packet.messagePlayer(target)
		}
	}

	fun handleWoolySelf(event: PlayerInteractEvent.RightClickItem) {
		val player = event.entity as? ServerPlayer ?: return
		if (!player.isCrouching) return
		if (!player.hasGene(ModGenes.WOOLY)) return

		val clickedWithShears = event.itemStack.isItem(Tags.Items.TOOLS_SHEAR)
		if (!clickedWithShears) return

		val newlySheared = GeneCooldowns.addCooldown(
			player,
			ModGenes.WOOLY,
			ServerConfig.CONFIG.woolyCooldown.get()
		)

		if (!newlySheared) return

		val level = event.level

		val woolItemStack = ItemStack(Blocks.WHITE_WOOL)

		val woolEntity = ItemEntity(
			level,
			player.eyePosition.x,
			player.eyePosition.y,
			player.eyePosition.z,
			woolItemStack
		)

		level.addFreshEntity(woolEntity)

		woolEntity.setDeltaMovement(
			level.random.nextRange(-0.05, 0.05),
			level.random.nextRange(0.05, 0.1),
			level.random.nextRange(-0.05, 0.05)
		)

		event.itemStack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(event.itemStack))

		event.level.playSound(
			null,
			player,
			SoundEvents.SHEEP_SHEAR,
			SoundSource.PLAYERS,
			1.0f,
			1.0f
		)

		val packet = ShearedPacket(removingSkin = true)
		packet.messagePlayer(player)
	}

	private val RECENTLY_MEATED_PLAYERS = GeneCooldown(
		ModGenes.MEATY,
		ServerConfig.CONFIG.meatyCooldown.get()
	)

	fun handleMeatyOther(event: PlayerInteractEvent.EntityInteract) {
		val target = event.target as? LivingEntity ?: return
		if (!target.hasGene(ModGenes.MEATY)) return

		val level = target.level()
		if (level.isClientSide) return

		val clickedWithShears = event.itemStack.isItem(Tags.Items.TOOLS_SHEAR)
		if (!clickedWithShears) return

		val newlyMeated = RECENTLY_MEATED_PLAYERS.add(target)

		val clicker = event.entity

		if (!newlyMeated) {
			clicker.sendSystemMessage(ModMessageLang.RECENT_MEATY.toComponent())
			return
		}

		val porkEntity = ItemEntity(
			level,
			target.eyePosition.x,
			target.eyePosition.y,
			target.eyePosition.z,
			ItemStack(Items.PORKCHOP)
		)

		level.addFreshEntity(porkEntity)

		porkEntity.setDeltaMovement(
			level.random.nextRange(-0.05, 0.05),
			level.random.nextRange(0.05, 0.1),
			level.random.nextRange(-0.05, 0.05)
		)

		event.itemStack.hurtAndBreak(1, clicker, clicker.getEquipmentSlotForItem(event.itemStack))

		level.playSound(
			null,
			target,
			SoundEvents.SHEEP_SHEAR,
			SoundSource.PLAYERS,
			1.0f,
			1.0f
		)
	}

	fun handleMeatySelf(event: PlayerInteractEvent.RightClickItem) {
		val player = event.entity
		val level = player.level()

		if (!player.isCrouching
			|| !player.hasGene(ModGenes.MEATY)
			|| level.isClientSide
		) return

		val clickedWithShears = event.itemStack.isItem(Tags.Items.TOOLS_SHEAR)
		if (!clickedWithShears) return

		val newlyMeated = RECENTLY_MEATED_PLAYERS.add(player)

		if (!newlyMeated) {
			player.sendSystemMessage(ModMessageLang.RECENT_MEATY.toComponent())
			return
		}

		val porkEntity = ItemEntity(
			level,
			player.eyePosition.x,
			player.eyePosition.y,
			player.eyePosition.z,
			ItemStack(Items.PORKCHOP)
		)

		level.addFreshEntity(porkEntity)
		porkEntity.setDeltaMovement(
			level.random.nextRange(-0.05, 0.05),
			level.random.nextRange(0.05, 0.1),
			level.random.nextRange(-0.05, 0.05)
		)

		event.itemStack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(event.itemStack))

		level.playSound(
			null,
			player,
			SoundEvents.SHEEP_SHEAR,
			SoundSource.PLAYERS,
			1.0f,
			1.0f
		)
	}

	private val RECENTLY_MILKED_ENTITIES = GeneCooldown(
		ModGenes.MILKY,
		ServerConfig.CONFIG.milkyCooldown.get()
	)

	fun handleMilkyOther(event: PlayerInteractEvent.EntityInteract) {
		val target = event.target as? LivingEntity ?: return
		if (target.level().isClientSide) return

		when (target) {
			is Cow, is Goat -> return
		}

		if (!target.hasGene(ModGenes.MILKY)) return

		val clickedWithBucket = event.itemStack.isItem(Items.BUCKET)
		if (!clickedWithBucket) return

		val newlyMilked = RECENTLY_MILKED_ENTITIES.add(target)

		val clicker = event.entity
		if (!newlyMilked) {
			clicker.sendSystemMessage(ModMessageLang.RECENT_MILKY.toComponent())
			return
		}

		target.sendSystemMessage(ModMessageLang.MILK_MILKED.toComponent())

		event.itemStack.shrink(1)
		clicker.addItem(ItemStack(Items.MILK_BUCKET))

		val sound = if (target is Player && target.random.chance(0.05)) {
			SoundEvents.GOAT_SCREAMING_MILK
		} else {
			if (target.random.nextBoolean()) SoundEvents.COW_MILK else SoundEvents.GOAT_MILK
		}

		event.level.playSound(
			null,
			target,
			sound,
			SoundSource.PLAYERS,
			1.0f,
			1.0f
		)

		if (target is ServerPlayer) {
			AdvancementTriggers.getMilkedAdvancement(target)
		}
	}

	fun handleMilkySelf(event: PlayerInteractEvent.RightClickItem) {
		val player = event.entity
		if (player.level().isClientSide) return

		if (!player.isCrouching) return
		val clickedWithBucket = event.itemStack.isItem(Items.BUCKET)
		if (!clickedWithBucket) return

		if (!player.hasGene(ModGenes.MILKY)) return

		val newlyMilked = RECENTLY_MILKED_ENTITIES.add(player)

		if (!newlyMilked) return

		event.itemStack.shrink(1)
		player.addItem(ItemStack(Items.MILK_BUCKET))

		val sound = if (player.random.chance(0.05f)) {
			SoundEvents.GOAT_SCREAMING_MILK
		} else {
			if (player.random.nextBoolean()) SoundEvents.COW_MILK else SoundEvents.GOAT_MILK
		}

		event.level.playSound(
			null,
			player,
			sound,
			SoundSource.PLAYERS,
			1.0f,
			1.0f
		)

		if (player is ServerPlayer) {
			AdvancementTriggers.getMilkedAdvancement(player)
		}
	}

	fun shootFireball(event: PlayerInteractEvent.RightClickItem) {
		val player = event.entity
		if (!player.hasGene(ModGenes.SHOOT_FIREBALLS)) return

		if (!player.isCrouching) return
		if (!event.itemStack.isItem(ModItemTagsProvider.ACTIVATES_SHOOT_FIREBALL_GENE)) return

		val lookVec = player.lookAngle

		val fireball = SmallFireball(
			event.level,
			player,
			lookVec
		)

		fireball.setPos(fireball.x, player.eyeY, fireball.z)

		event.level.addFreshEntity(fireball)

		event.level.playSound(
			null,
			player,
			SoundEvents.BLAZE_SHOOT,
			SoundSource.PLAYERS,
			1.0f,
			1.0f
		)

		if (!player.isCreative) event.itemStack.shrink(1)
	}

	fun eatGrass(event: PlayerInteractEvent.RightClickBlock) {
		if (event.itemStack.isNotEmpty()) return

		val player = event.entity
		if (!player.hasGene(ModGenes.EAT_GRASS)) return

		val isHungry = player.foodData.foodLevel < 20
		if (!isHungry) return

		val block = event.level.getBlockState(event.pos).block

		val blockAfter = when (block) {
			Blocks.GRASS_BLOCK, Blocks.MYCELIUM -> Blocks.DIRT
			Blocks.WARPED_NYLIUM, Blocks.CRIMSON_NYLIUM -> Blocks.NETHERRACK
			else -> return  // If it's not grass or nylium, then you can't eat it, so return
		}

		event.level.setBlockAndUpdate(event.pos, blockAfter.defaultBlockState())
		player.foodData.eat(1, 0.1f)

		event.level.playSound(
			null,
			player.blockPosition(),
			SoundEvents.PLAYER_BURP,
			SoundSource.PLAYERS,
			1.0f,
			1.0f
		)

		event.level.playSound(
			null,
			event.pos,
			SoundEvents.GRASS_BREAK,
			SoundSource.BLOCKS,
			1.0f,
			1.0f
		)

		GeneCooldowns.removeCooldown(player, ModGenes.EAT_GRASS)
	}

	fun cureCringe(event: PlayerInteractEvent.RightClickBlock) {
		if (event.level.getBlockState(event.pos).block != Blocks.GRASS_BLOCK) return

		val player = event.entity

		if (!player.hasGene(ModGenes.CRINGE)) return

		val cringe = ModGenes.CRINGE.getHolderOrThrow(event.entity.registryAccess())
		player.removeGene(cringe)
		if (!player.level().isClientSide) {
			player.sendSystemMessage(ModMessageLang.CRINGE_GRASS.toComponent())
		}
	}

	fun handleInfinityGetProjectile(event: LivingGetProjectileEvent) {
		val player = event.entity as? Player ?: return

		if (!player.hasGene(ModGenes.INFINITY)) return

		if (event.projectileItemStack.isNotEmpty()) return

		val weapon = event.projectileWeaponItemStack.item as? ProjectileWeaponItem ?: return
		val defaultAmmo = weapon.getDefaultCreativeAmmo(player, event.projectileItemStack)

		defaultAmmo.setUnit(ModDataComponents.IS_INFINITY_ARROW)

		event.projectileItemStack = defaultAmmo
	}

	fun handleInfinityArrow(event: EntityJoinLevelEvent) {
		val arrow = event.entity as? Arrow ?: return
		if (arrow.level().isClientSide) return

		val arrowStack = arrow.pickupItemStackOrigin

		val isInfinity = arrowStack.has(ModDataComponents.IS_INFINITY_ARROW)
		if (!isInfinity) return

		arrow.pickup = AbstractArrow.Pickup.DISALLOWED
	}

}