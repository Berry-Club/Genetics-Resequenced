package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanItemComponent
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.ShearedPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.entity.animal.MushroomCow
import net.minecraft.world.entity.animal.Sheep
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
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import kotlin.random.Random

object ClickGenes {

    val recentlySheered = GeneCooldown(
        ModGenes.WOOLY,
        ServerConfig.woolyCooldown.get()
    )

    fun handleWooly(event: PlayerInteractEvent.EntityInteract) {
        val wooly = ModGenes.WOOLY.getHolder(event.entity.registryAccess()) ?: return
        if (wooly.isDisabled) return

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        if (target.level().isClientSide) return

        when (target) {
            is Sheep, is MushroomCow -> return
        }

        if (!target.hasGene(ModGenes.WOOLY)) return

        val clickedWithShears = event.itemStack.`is`(ModItemTagsProvider.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        val newlySheared = recentlySheered.add(target)

        if (!newlySheared) {
            clicker.sendSystemMessage(ModLanguageProvider.Messages.RECENT_WOOLY.toComponent())
            return
        }

        val woolItemStack = ItemStack(Blocks.WHITE_WOOL)

        val woolEntity = ItemEntity(
            event.level,
            target.eyePosition.x,
            target.eyePosition.y,
            target.eyePosition.z,
            woolItemStack
        )
        event.level.addFreshEntity(woolEntity)
        woolEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
        )

        event.itemStack.hurtAndBreak(1, clicker, clicker.getEquipmentSlotForItem(event.itemStack))

        event.level.playSound(
            null,
            target,
            SoundEvents.SHEEP_SHEAR,
            SoundSource.PLAYERS,
            1.0f,
            1.0f
        )

        if (target is ServerPlayer) {
            ModPacketHandler.messagePlayer(target, ShearedPacket(removingSkin = true))
        }

    }

    val recentlyMeated = GeneCooldown(
        ModGenes.MEATY,
        ServerConfig.meatyCooldown.get()
    )

    fun handleMeaty(event: PlayerInteractEvent.EntityInteract) {
        val meaty = ModGenes.MEATY.getHolder(event.level.registryAccess()) ?: return
        if (meaty.isDisabled) return

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        if (target.level().isClientSide) return

        if (!target.hasGene(ModGenes.MEATY)) return

        val clickedWithShears = event.itemStack.`is`(ModItemTagsProvider.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        val newlyMeated = recentlyMeated.add(target)

        if (!newlyMeated) {
            clicker.sendSystemMessage(ModLanguageProvider.Messages.RECENT_MEATY.toComponent())
            return
        }

        val porkEntity = ItemEntity(
            event.level,
            target.eyePosition.x,
            target.eyePosition.y,
            target.eyePosition.z,
            ItemStack(Items.PORKCHOP)
        )
        event.level.addFreshEntity(porkEntity)
        porkEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
        )

        event.itemStack.hurtAndBreak(1, clicker, clicker.getEquipmentSlotForItem(event.itemStack))

        event.level.playSound(
            null,
            target,
            SoundEvents.SHEEP_SHEAR,
            SoundSource.PLAYERS,
            1.0f,
            1.0f
        )
    }

    val recentlyMilked = GeneCooldown(
        ModGenes.MILKY,
        ServerConfig.milkyCooldown.get()
    )

    fun handleMilky(event: PlayerInteractEvent.EntityInteract) {
        val milky = ModGenes.MILKY.getHolder(event.level.registryAccess()) ?: return
        if (milky.isDisabled) return

        val target = event.target as? LivingEntity ?: return
        if (target.level().isClientSide) return

        when (target) {
            is Cow, is Goat -> return
        }

        if (!target.hasGene(ModGenes.MILKY)) return

        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        val newlyMilked = recentlyMilked.add(target)

        val clicker = event.entity
        if (!newlyMilked) {
            clicker.sendSystemMessage(ModLanguageProvider.Messages.RECENT_MILKY.toComponent())
            return
        }

        target.sendSystemMessage(ModLanguageProvider.Messages.MILK_MILKED.toComponent())

        event.itemStack.shrink(1)
        clicker.addItem(ItemStack(Items.MILK_BUCKET))

        val sound = if (target is Player && Random.nextFloat() < 0.05f) {
            SoundEvents.GOAT_SCREAMING_MILK
        } else {
            if (Random.nextBoolean()) SoundEvents.COW_MILK else SoundEvents.GOAT_MILK
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

    fun milkyItem(event: PlayerInteractEvent.RightClickItem) {
        val milky = ModGenes.MILKY.getHolder(event.entity.registryAccess()) ?: return
        if (milky.isDisabled) return

        val player = event.entity
        if (player.level().isClientSide) return

        if (!player.isCrouching) return
        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        if (!player.hasGene(ModGenes.MILKY)) return

        val newlyMilked = recentlyMilked.add(player)

        if (!newlyMilked) return

        event.itemStack.shrink(1)
        player.addItem(ItemStack(Items.MILK_BUCKET))

        val sound = if (Random.nextFloat() < 0.05f) {
            SoundEvents.GOAT_SCREAMING_MILK
        } else {
            if (Random.nextBoolean()) SoundEvents.COW_MILK else SoundEvents.GOAT_MILK
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

    fun woolyItem(event: PlayerInteractEvent.RightClickItem) {
        val wooly = ModGenes.WOOLY.getHolder(event.entity.registryAccess()) ?: return
        if (wooly.isDisabled) return

        val player = event.entity

        if (player.level().isClientSide) return

        if (!player.isCrouching) return
        val clickedWithShears = event.itemStack.`is`(ModItemTagsProvider.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        if (!player.hasGene(ModGenes.WOOLY)) return

        val newlySheared = recentlySheered.add(player)

        if (!newlySheared) return

        val woolItemStack = ItemStack(Blocks.WHITE_WOOL)

        val woolEntity = ItemEntity(
            event.level,
            player.eyePosition.x,
            player.eyePosition.y,
            player.eyePosition.z,
            woolItemStack
        )
        event.level.addFreshEntity(woolEntity)
        woolEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
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

        ModPacketHandler.messagePlayer(player as ServerPlayer, ShearedPacket(removingSkin = true))
    }

    fun meatyItem(event: PlayerInteractEvent.RightClickItem) {
        val meaty = ModGenes.MEATY.getHolder(event.entity.registryAccess()) ?: return
        if (meaty.isDisabled) return

        val player = event.entity

        if (player.level().isClientSide) return

        if (!player.isCrouching) return
        val clickedWithShears = event.itemStack.`is`(ModItemTagsProvider.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        if (!player.hasGene(ModGenes.MEATY)) return

        val newlyMeated = recentlyMeated.add(player)

        if (!newlyMeated) {
            player.sendSystemMessage(ModLanguageProvider.Messages.RECENT_MEATY.toComponent())
            return
        }

        val porkEntity = ItemEntity(
            event.level,
            player.eyePosition.x,
            player.eyePosition.y,
            player.eyePosition.z,
            ItemStack(Items.PORKCHOP)
        )
        event.level.addFreshEntity(porkEntity)
        porkEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
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
    }

    fun shootFireball(event: PlayerInteractEvent.RightClickItem) {
        val shootFireballs = ModGenes.SHOOT_FIREBALLS.getHolder(event.entity.registryAccess()) ?: return
        if (shootFireballs.isDisabled) return

        val player = event.entity
        if (!player.hasGene(ModGenes.SHOOT_FIREBALLS)) return

        if (!player.isCrouching) return
        if (!event.itemStack.`is`(ModItemTagsProvider.FIREBALL_ITEM_TAG)) return

        val lookVec = player.lookAngle

        val fireball = SmallFireball(
            event.level,
            player,
            lookVec
        ).apply {
            setPos(x, player.eyeY, z)
        }

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
        val eatGrass = ModGenes.EAT_GRASS.getHolder(event.entity.registryAccess()) ?: return
        if (eatGrass.isDisabled) return

        if (!event.itemStack.isEmpty) return

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

        if (player.uuid in recentlySheered) {
            recentlySheered.remove(player.uuid)
            GeneCooldown.tellCooldownEnded(player, ModGenes.WOOLY)
        }

    }

    fun cureCringe(event: PlayerInteractEvent.RightClickBlock) {
        if (event.level.getBlockState(event.pos).block != Blocks.GRASS_BLOCK) return

        val player = event.entity

        if (!player.hasGene(ModGenes.CRINGE)) return

        val cringe = ModGenes.CRINGE.getHolder(event.entity.registryAccess()) ?: return
        player.removeGene(cringe)
        if (!player.level().isClientSide) {
            player.sendSystemMessage(ModLanguageProvider.Messages.CRINGE_GRASS.toComponent())
        }
    }

    fun handleInfinityGetProjectile(event: LivingGetProjectileEvent) {
        val player = event.entity as? Player ?: return

        val infinity = ModGenes.INFINITY.getHolder(player.registryAccess()) ?: return
        if (infinity.isDisabled) return
        if (!player.hasGene(ModGenes.INFINITY)) return

        if (!event.projectileItemStack.isEmpty) return

        val weapon = event.projectileWeaponItemStack.item as? ProjectileWeaponItem ?: return
        val defaultAmmo = weapon.getDefaultCreativeAmmo(player, event.projectileItemStack)

        defaultAmmo.set(ModDataComponents.IS_INFINITY_ARROW, BooleanItemComponent(true))

        event.projectileItemStack = defaultAmmo
    }

    fun handleInfinityArrow(event: EntityJoinLevelEvent) {
        val arrow = event.entity as? Arrow ?: return
        if (arrow.level().isClientSide) return

        val arrowStack = arrow.pickupItemStackOrigin

        val isInfinity = arrowStack.get(ModDataComponents.IS_INFINITY_ARROW)?.value ?: false
        if (!isInfinity) return

        arrow.pickup = AbstractArrow.Pickup.DISALLOWED
    }

}