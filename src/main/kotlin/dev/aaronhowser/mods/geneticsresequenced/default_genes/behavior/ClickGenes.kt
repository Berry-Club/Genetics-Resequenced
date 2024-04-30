package dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
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
import net.minecraft.world.entity.player.PlayerModelPart
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.SmallFireball
import net.minecraft.world.item.ArrowItem
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.event.entity.player.ArrowLooseEvent
import net.minecraftforge.event.entity.player.ArrowNockEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import kotlin.random.Random

object ClickGenes {

    fun playerSkinSheared(player: LocalPlayer) {
        val options = Minecraft.getInstance().options
        try {
            val modelPartsField = options::class.java.getDeclaredField("modelParts")
            modelPartsField.isAccessible = true
            val modelParts = modelPartsField.get(options) as MutableSet<PlayerModelPart>

            for (part in modelParts) {
                options.toggleModelPart(part, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val recentlySheered = OtherUtil.GeneCooldown(
        DefaultGenes.WOOLY,
        ServerConfig.woolyCooldown.get()
    )

    fun wooly(event: PlayerInteractEvent.EntityInteract) {

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        when (target) {
            is Sheep, is MushroomCow -> return
        }

        val genes = target.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.WOOLY)) return

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

        event.itemStack.hurtAndBreak(1, clicker) { }

        event.level.playSound(
            null,
            target,
            SoundEvents.SHEEP_SHEAR,
            target.soundSource,
            1.0f,
            1.0f
        )

        if (target is LocalPlayer) {
            playerSkinSheared(target)
        }

    }

    private val recentlyMeated = OtherUtil.GeneCooldown(
        DefaultGenes.MEATY,
        ServerConfig.meatyCooldown.get()
    )

    fun meaty(event: PlayerInteractEvent.EntityInteract) {

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        val genes = target.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.MEATY)) return

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

        event.itemStack.hurtAndBreak(1, clicker) { }

        event.level.playSound(
            null,
            target,
            SoundEvents.SHEEP_SHEAR,
            target.soundSource,
            1.0f,
            1.0f
        )
    }

    private val recentlyMilked = OtherUtil.GeneCooldown(
        DefaultGenes.MILKY,
        ServerConfig.milkyCooldown.get()
    )

    fun milky(event: PlayerInteractEvent.EntityInteract) {

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        when (target) {
            is Cow, is Goat -> return
        }

        val genes = target.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.MILKY)) return

        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        val newlyMilked = recentlyMilked.add(target)

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
    }

    fun milkyItem(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity

        if (!player.isCrouching) return
        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.MILKY)) return

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
    }

    fun woolyItem(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity

        if (!player.isCrouching) return
        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.WOOLY)) return

        val newlySheared = recentlySheered.add(player)

        if (!newlySheared) return

        if (player is LocalPlayer) {
            playerSkinSheared(player)
            return
        }

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

        event.itemStack.hurtAndBreak(1, player) { }

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
        val player = event.entity
        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.SHOOT_FIREBALLS)) return

        if (!player.isCrouching) return
        if (!event.itemStack.`is`(ModTags.FIREBALL_ITEM_TAG)) return

        val lookVec = player.lookAngle

        val fireball = SmallFireball(
            event.level,
            player,
            lookVec.x,
            lookVec.y,
            lookVec.z
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

        if (!event.itemStack.isEmpty) return

        val player = event.entity
        val genes = event.entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.EAT_GRASS)) return

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
            OtherUtil.tellCooldownEnded(player, DefaultGenes.WOOLY)
        }

    }

    fun handleInfinityStart(event: ArrowNockEvent) {
        if (event.hasAmmo()) return

        val genes = event.entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.INFINITY)) return

        event.entity.startUsingItem(event.entity.usedItemHand)
        event.action = InteractionResultHolder.success(event.bow)
    }

    /**
     * @see BowItem.releaseUsing
     * @see BowItem.getPowerForTime
     */
    fun handleInfinityEnd(event: ArrowLooseEvent) {
        if (event.hasAmmo()) return

        val player = event.entity

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.INFINITY)) return

        val bowStack = event.bow

        val charge = event.charge
        var velocity = charge / 20.0f
        velocity = ((velocity * velocity + velocity * 2.0f) / 3.0f).coerceAtMost(1.0f)

        if (velocity < 0.1f) return

        val arrowItem = Items.ARROW as ArrowItem
        val arrowStack = arrowItem.defaultInstance
        val abstractArrow = arrowItem.createArrow(player.level, arrowStack, player)
        abstractArrow.shootFromRotation(player, player.xRot, player.yRot, 0.0f, velocity * 3.0f, 1.0f)

        if (velocity == 1f) abstractArrow.isCritArrow = true

        val powerLevel = bowStack.getEnchantmentLevel(Enchantments.POWER_ARROWS)
        if (powerLevel > 0) abstractArrow.baseDamage += powerLevel * 0.5 + 0.5

        val punchLevel = bowStack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS)
        if (punchLevel > 0) abstractArrow.knockback = punchLevel

        if (bowStack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) abstractArrow.setSecondsOnFire(100)

        abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY

        player.level.addFreshEntity(abstractArrow)
    }

}