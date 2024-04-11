package dev.aaronhowser.mods.geneticsresequenced.genebehaviors

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.client.Options
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResultHolder
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
import net.minecraftforge.event.entity.player.ArrowLooseEvent
import net.minecraftforge.event.entity.player.ArrowNockEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import java.util.*
import kotlin.random.Random

object ClickGenes {

    //TODO: Make these into recipes? Would be nice for packs or whatever

    private val recentlySheered = mutableSetOf<UUID>()

    /**
     * TODO: Make this toggle the outer skin layers
     *
     * See [Options.toggleModelPart], but it's client only which means this will need a packet
     */
    fun wooly(event: PlayerInteractEvent.EntityInteract) {
        when (event.target) {
            is Sheep, is MushroomCow -> return
        }

        val genes = event.entity.getGenes() ?: return
        if (!genes.hasGene(Gene.WOOLY)) return

        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_TAG)
        if (!clickedWithShears) return

        if (recentlySheered.contains(event.target.uuid)) {
            event.entity.sendSystemMessage(Component.literal("This entity has already been sheared recently!"))
            return
        }

        recentlySheered.add(event.target.uuid)
        ModScheduler.scheduleTaskInTicks(ServerConfig.woolyCooldown.get()) {
            recentlySheered.remove(event.target.uuid)
        }

        val woolItemStack = ItemStack(Blocks.WHITE_WOOL)

        val woolEntity = ItemEntity(
            event.level,
            event.target.eyePosition.x,
            event.target.eyePosition.y,
            event.target.eyePosition.z,
            woolItemStack
        )
        event.level.addFreshEntity(woolEntity)
        woolEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
        )

        event.itemStack.hurtAndBreak(1, event.entity) { }

        event.level.playSound(
            null,
            event.target,
            SoundEvents.SHEEP_SHEAR,
            event.target.soundSource,
            1.0f,
            1.0f
        )
    }

    private val recentlyMeated = mutableSetOf<UUID>()
    fun meaty(event: PlayerInteractEvent.EntityInteract) {

        val genes = event.entity.getGenes() ?: return
        if (!genes.hasGene(Gene.MEATY)) return

        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_TAG)
        if (!clickedWithShears) return

        if (recentlyMeated.contains(event.target.uuid)) {
            event.entity.sendSystemMessage(Component.literal("This entity has already been meated recently!"))
            return
        }

        recentlyMeated.add(event.target.uuid)
        ModScheduler.scheduleTaskInTicks(ServerConfig.meatyCooldown.get()) {
            recentlyMeated.remove(event.target.uuid)
        }

        val porkItemStack = ItemStack(Items.PORKCHOP)

        val porkEntity = ItemEntity(
            event.level,
            event.target.eyePosition.x,
            event.target.eyePosition.y,
            event.target.eyePosition.z,
            porkItemStack
        )
        event.level.addFreshEntity(porkEntity)
        porkEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
        )

        event.itemStack.hurtAndBreak(1, event.entity) { }

        event.level.playSound(
            null,
            event.target,
            SoundEvents.SHEEP_SHEAR,
            event.target.soundSource,
            1.0f,
            1.0f
        )
    }

    private val recentlyMilked = mutableSetOf<UUID>()
    fun milky(event: PlayerInteractEvent.EntityInteract) {

        when (event.target) {
            is Cow, is Goat -> return
        }

        val genes = event.entity.getGenes() ?: return
        if (!genes.hasGene(Gene.MILKY)) return

        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        if (recentlyMilked.contains(event.target.uuid)) {
            event.entity.sendSystemMessage(Component.literal("This entity has already been milked recently!"))
            return
        }

        recentlyMilked.add(event.target.uuid)
        ModScheduler.scheduleTaskInTicks(ServerConfig.milkyCooldown.get()) {
            recentlyMilked.remove(event.target.uuid)
        }

        event.itemStack.shrink(1)
        event.entity.addItem(ItemStack(Items.MILK_BUCKET))

        val sound = if (event.target is Player && Random.nextFloat() < 0.05f) {
            SoundEvents.GOAT_SCREAMING_MILK
        } else {
            if (Random.nextBoolean()) SoundEvents.COW_MILK else SoundEvents.GOAT_MILK
        }

        event.level.playSound(
            null,
            event.target,
            sound,
            event.target.soundSource,
            1.0f,
            1.0f
        )
    }

    fun milkyItem(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(Gene.MILKY)) return

        if (!player.isCrouching) return

        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        if (recentlyMilked.contains(player.uuid)) return

        recentlyMilked.add(player.uuid)
        ModScheduler.scheduleTaskInTicks(20 * 60 * 5) {
            recentlyMilked.remove(player.uuid)
        }

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

    fun shootFireball(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity
        val genes = player.getGenes() ?: return
        if (!genes.hasGene(Gene.SHOOT_FIREBALLS)) return

        if (!player.isCrouching) return
        if (!event.itemStack.`is`(ModTags.FIREBALL_TAG)) return

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

        event.itemStack.shrink(1)
    }

    fun eatGrass(event: PlayerInteractEvent.RightClickBlock) {

        if (!event.itemStack.isEmpty) return

        val player = event.entity
        val genes = event.entity.getGenes() ?: return
        if (!genes.hasGene(Gene.EAT_GRASS)) return


        val isHungry = player.foodData.foodLevel < 20
        if (!isHungry) return

        val block = event.level.getBlockState(event.pos).block

        val blockAfter = when (block) {
            Blocks.GRASS_BLOCK, Blocks.MYCELIUM -> Blocks.DIRT
            Blocks.WARPED_NYLIUM, Blocks.CRIMSON_NYLIUM -> Blocks.NETHERRACK
            else -> return
        }

        recentlySheered.remove(player.uuid)

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

    }

    fun handleInfinityStart(event: ArrowNockEvent) {
        if (event.hasAmmo()) return

        val genes = event.entity.getGenes() ?: return
        if (!genes.hasGene(Gene.INFINITY)) return

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
        if (!genes.hasGene(Gene.INFINITY)) return

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