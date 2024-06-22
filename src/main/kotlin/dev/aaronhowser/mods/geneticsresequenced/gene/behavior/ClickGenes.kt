package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneContainer.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.entity.animal.MushroomCow
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.entity.animal.goat.Goat
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
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
            clicker.sendSystemMessage(Component.translatable("message.geneticsresequenced.wooly.recent"))
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

        //TODO
//        if (target is ServerPlayer) {
//            ModPacketHandler.messagePlayer(target, ShearedPacket(removingSkin = true))
//        }

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
            clicker.sendSystemMessage(Component.translatable("message.geneticsresequenced.meaty.recent"))
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
            clicker.sendSystemMessage(Component.translatable("message.geneticsresequenced.milk.recent"))
            return
        }

        target.sendSystemMessage(Component.translatable("message.geneticsresequenced.milk.milked"))

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

        //TODO
//        ModPacketHandler.messagePlayer(player as ServerPlayer, ShearedPacket(removingSkin = true))
    }

}