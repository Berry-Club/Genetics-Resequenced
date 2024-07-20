package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.ShearedPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.entity.animal.MushroomCow
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.entity.animal.goat.Goat
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.SmallFireball
import net.minecraft.world.item.ArrowItem
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent
import net.neoforged.neoforge.event.entity.player.ArrowNockEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import kotlin.random.Random

object ClickGenes {

    val recentlySheered = GeneCooldown(
        ModGenes.WOOLY.get(),
        ServerConfig.woolyCooldown.get()
    )

    fun handleWooly(event: PlayerInteractEvent.EntityInteract) {
        if (!ModGenes.WOOLY.get().isActive) return

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        if (target.level().isClientSide) return

        when (target) {
            is Sheep, is MushroomCow -> return
        }

        if (!target.hasGene(ModGenes.WOOLY.get())) return

        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
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
        ModGenes.MEATY.get(),
        ServerConfig.meatyCooldown.get()
    )

    fun handleMeaty(event: PlayerInteractEvent.EntityInteract) {
        if (!ModGenes.MEATY.get().isActive) return

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        if (target.level().isClientSide) return

        if (!target.hasGene(ModGenes.MEATY.get())) return

        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
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
        ModGenes.MILKY.get(),
        ServerConfig.milkyCooldown.get()
    )

    fun handleMilky(event: PlayerInteractEvent.EntityInteract) {
        if (!ModGenes.MILKY.get().isActive) return

        val target = event.target as? LivingEntity ?: return
        if (target.level().isClientSide) return

        when (target) {
            is Cow, is Goat -> return
        }

        if (!target.hasGene(ModGenes.MILKY.get())) return

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
        if (!ModGenes.MILKY.get().isActive) return

        val player = event.entity
        if (player.level().isClientSide) return

        if (!player.isCrouching) return
        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        if (!player.hasGene(ModGenes.MILKY.get())) return

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
        if (!ModGenes.WOOLY.get().isActive) return

        val player = event.entity

        if (player.level().isClientSide) return

        if (!player.isCrouching) return
        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        if (!player.hasGene(ModGenes.WOOLY.get())) return

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
        if (!ModGenes.MEATY.get().isActive) return

        val player = event.entity

        if (player.level().isClientSide) return

        if (!player.isCrouching) return
        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        if (!player.hasGene(ModGenes.MEATY.get())) return

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
        if (!ModGenes.SHOOT_FIREBALLS.get().isActive) return

        val player = event.entity
        if (!player.hasGene(ModGenes.SHOOT_FIREBALLS.get())) return

        if (!player.isCrouching) return
        if (!event.itemStack.`is`(ModTags.FIREBALL_ITEM_TAG)) return

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
        if (!ModGenes.EAT_GRASS.get().isActive) return

        if (!event.itemStack.isEmpty) return

        val player = event.entity
        if (!player.hasGene(ModGenes.EAT_GRASS.get())) return

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
            GeneCooldown.tellCooldownEnded(player, ModGenes.WOOLY.get())
        }

    }

    fun cureCringe(event: PlayerInteractEvent.RightClickBlock) {
        if (event.level.getBlockState(event.pos).block != Blocks.GRASS_BLOCK) return

        val player = event.entity

        if (!player.hasGene(ModGenes.CRINGE.get())) return

        player.removeGene(ModGenes.CRINGE.get())
        if (!player.level().isClientSide) {
            player.sendSystemMessage(ModLanguageProvider.Messages.CRINGE_GRASS.toComponent())
        }
    }

    private val infinityDummyStack = Items.STICK.itemStack
    fun handleInfinityGetProjectile(event: LivingGetProjectileEvent) {
        if (!event.projectileItemStack.isEmpty) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.INFINITY.get())) return

        val weapon = event.projectileWeaponItemStack

        val infinityEnchant = OtherUtil.getEnchantHolder(entity, Enchantments.INFINITY)
        if (weapon.getEnchantmentLevel(infinityEnchant) != 0) return

        event.projectileItemStack = infinityDummyStack
    }

    fun handleInfinityStart(event: ArrowNockEvent) {
        if (!ModGenes.INFINITY.get().isActive) return

        if (event.hasAmmo()) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.INFINITY.get())) return

        val infinityEnchant = OtherUtil.getEnchantHolder(entity, Enchantments.INFINITY)
        if (event.bow.getEnchantmentLevel(infinityEnchant) != 0) return

        entity.startUsingItem(event.entity.usedItemHand)
        event.action = InteractionResultHolder.success(event.bow)
    }

    /**
     * @see BowItem.releaseUsing
     * @see BowItem.getPowerForTime
     */
    fun handleInfinityEnd(event: ArrowLooseEvent) {
        if (!ModGenes.INFINITY.get().isActive) return

        val player = event.entity
        if (!player.hasGene(ModGenes.INFINITY.get())) return

        val bowStack = event.bow

        val ammo = CommonHooks.getProjectile(player, bowStack, ItemStack.EMPTY)
        if (ammo != infinityDummyStack) return

        val charge = event.charge
        var velocity = charge / 20.0f
        velocity = ((velocity * velocity + velocity * 2.0f) / 3.0f).coerceAtMost(1.0f)

        if (velocity < 0.1f) return

        val arrowItem = Items.ARROW as ArrowItem
        val arrowStack = arrowItem.itemStack
        val abstractArrow = arrowItem.createArrow(
            player.level(),
            arrowStack,
            player,
            bowStack
        )

        val powerEnchantment = OtherUtil.getEnchantHolder(player, Enchantments.POWER)
        val flameEnchantment = OtherUtil.getEnchantHolder(player, Enchantments.FLAME)

        val powerLevel = bowStack.getEnchantmentLevel(powerEnchantment)
        val flameLevel = bowStack.getEnchantmentLevel(flameEnchantment)

        abstractArrow.apply {
            isCritArrow = velocity == 1f
            pickup = AbstractArrow.Pickup.DISALLOWED

            if (powerLevel > 0) baseDamage += powerLevel * 0.5 + 0.5
            if (flameLevel > 0) remainingFireTicks = 9999

            shootFromRotation(player, player.xRot, player.yRot, 0.0f, velocity * 3.0f, 1.0f)
        }

        player.level().addFreshEntity(abstractArrow)

        event.isCanceled = true
    }

}