package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.ShearedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.network.chat.Component
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
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent
import net.neoforged.neoforge.event.entity.player.ArrowNockEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import kotlin.random.Random

object ClickGenes {

    val recentlySheered = GeneCooldown(
        ModGenes.wooly,
        ServerConfig.woolyCooldown.get()
    )

    fun handleWooly(event: PlayerInteractEvent.EntityInteract) {
        if (!ModGenes.wooly.isActive) return

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        if (target.level().isClientSide) return

        when (target) {
            is Sheep, is MushroomCow -> return
        }

        if (!target.hasGene(ModGenes.wooly)) return

        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        val newlySheared = recentlySheered.add(target)

        if (!newlySheared) {
            clicker.sendSystemMessage(Component.translatable(ModLanguageProvider.Messages.RECENT_WOOLY))
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
            target.soundSource,
            1.0f,
            1.0f
        )

        if (target is ServerPlayer) {
            ModPacketHandler.messagePlayer(target, ShearedPacket(removingSkin = true))
        }

    }

    val recentlyMeated = GeneCooldown(
        ModGenes.meaty,
        ServerConfig.meatyCooldown.get()
    )

    fun handleMeaty(event: PlayerInteractEvent.EntityInteract) {
        if (!ModGenes.meaty.isActive) return

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        if (target.level().isClientSide) return

        if (!target.hasGene(ModGenes.meaty)) return

        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        val newlyMeated = recentlyMeated.add(target)

        if (!newlyMeated) {
            clicker.sendSystemMessage(Component.translatable(ModLanguageProvider.Messages.RECENT_MEATY))
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
            target.soundSource,
            1.0f,
            1.0f
        )
    }

    val recentlyMilked = GeneCooldown(
        ModGenes.milky,
        ServerConfig.milkyCooldown.get()
    )

    fun handleMilky(event: PlayerInteractEvent.EntityInteract) {
        if (!ModGenes.milky.isActive) return

        val target = event.target as? LivingEntity ?: return
        if (target.level().isClientSide) return

        when (target) {
            is Cow, is Goat -> return
        }

        if (!target.hasGene(ModGenes.milky)) return

        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        val newlyMilked = recentlyMilked.add(target)

        val clicker = event.entity
        if (!newlyMilked) {
            clicker.sendSystemMessage(Component.translatable(ModLanguageProvider.Messages.RECENT_MILKY))
            return
        }

        target.sendSystemMessage(Component.translatable(ModLanguageProvider.Messages.MILK_MILKED))

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
            target.soundSource,
            1.0f,
            1.0f
        )

        //TODO
//        if (target is ServerPlayer) {
//            AdvancementTriggers.getMilkedAdvancement(target)
//        }
    }

    fun milkyItem(event: PlayerInteractEvent.RightClickItem) {
        if (!ModGenes.milky.isActive) return

        val player = event.entity
        if (player.level().isClientSide) return

        if (!player.isCrouching) return
        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        if (!player.hasGene(ModGenes.milky)) return

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

        //TODO
//        if (player is ServerPlayer) {
//            AdvancementTriggers.getMilkedAdvancement(player)
//        }
    }

    fun woolyItem(event: PlayerInteractEvent.RightClickItem) {
        if (!ModGenes.wooly.isActive) return

        val player = event.entity

        if (player.level().isClientSide) return

        if (!player.isCrouching) return
        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        if (!player.hasGene(ModGenes.wooly)) return

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

    fun shootFireball(event: PlayerInteractEvent.RightClickItem) {
        if (!ModGenes.shootFireballs.isActive) return

        val player = event.entity
        if (!player.hasGene(ModGenes.shootFireballs)) return

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
        if (!ModGenes.eatGrass.isActive) return

        if (!event.itemStack.isEmpty) return

        val player = event.entity
        if (!player.hasGene(ModGenes.eatGrass)) return

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
            GeneCooldown.tellCooldownEnded(player, ModGenes.wooly)
        }

    }

    fun cureCringe(event: PlayerInteractEvent.RightClickBlock) {
        if (event.level.getBlockState(event.pos).block != Blocks.GRASS_BLOCK) return

        val player = event.entity

        if (!player.hasGene(ModGenes.cringe)) return

        player.removeGene(ModGenes.cringe)
        if (!player.level().isClientSide) {
            player.sendSystemMessage(Component.translatable(ModLanguageProvider.Messages.CRINGE_GRASS))
        }
    }

    fun handleInfinityStart(event: ArrowNockEvent) {
        if (!ModGenes.infinity.isActive) return

        if (event.hasAmmo()) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.infinity)) return

        entity.startUsingItem(event.entity.usedItemHand)
        event.action = InteractionResultHolder.success(event.bow)
    }

    /**
     * @see BowItem.releaseUsing
     * @see BowItem.getPowerForTime
     */
    fun handleInfinityEnd(event: ArrowLooseEvent) {
        if (!ModGenes.infinity.isActive) return

        if (event.hasAmmo()) return

        val player = event.entity

        if (!player.hasGene(ModGenes.infinity)) return

        val bowStack = event.bow

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
        abstractArrow.shootFromRotation(player, player.xRot, player.yRot, 0.0f, velocity * 3.0f, 1.0f)

        if (velocity == 1f) abstractArrow.isCritArrow = true

        //TODO: Enchantments
//        val powerLevel = bowStack.getEnchantmentLevel(Enchantments.POWER_ARROWS)
//        if (powerLevel > 0) abstractArrow.baseDamage += powerLevel * 0.5 + 0.5
//
//        val punchLevel = bowStack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS)
//        if (punchLevel > 0) abstractArrow.knockback = punchLevel
//
//        val flameLevel = bowStack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS)
//        if (flameLevel > 0) abstractArrow.setSecondsOnFire(100)

        abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY

        player.level().addFreshEntity(abstractArrow)
    }

}