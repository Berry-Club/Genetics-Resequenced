package dev.aaronhowser.mods.geneticsresequenced.gene_behaviors

import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.items.AntiFieldOrbItem
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.LightLayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerXpEvent
import java.util.*
import kotlin.math.max

object TickGenes {

    fun handleBioluminescence(entity: LivingEntity) {

        if (entity.tickCount % ServerConfig.bioluminescenceCooldown.get() != 0) return

        if (entity.level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) > 8) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.BIOLUMINESCENCE)) return

        val headBlock = entity.level.getBlockState(entity.blockPosition().above())
        if (!headBlock.isAir) return

        entity.level.setBlockAndUpdate(
            entity.blockPosition().above(),
            ModBlocks.BIOLUMINESCENCE_BLOCK.defaultBlockState()
        )
    }

    fun handlePhotosynthesis(entity: LivingEntity) {
        if (entity !is Player) return
        if (entity.tickCount % ServerConfig.photosynthesisCooldown.get() != 0) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.PHOTOSYNTHESIS)) return

        val foodData = entity.foodData

        if (!foodData.needsFood()) return

        val inDirectSunlight = entity.level.canSeeSky(entity.blockPosition())
        val isDay = entity.level.isDay
        if (!inDirectSunlight || !isDay) return

        foodData.eat(
            ServerConfig.photoSynthesisHungerAmount.get(),
            ServerConfig.photoSynthesisSaturationAmount.get().toFloat()
        )
    }

    fun handleNoHunger(entity: Player) {

        if (entity.tickCount % ServerConfig.noHungerCooldown.get() != 0) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.NO_HUNGER)) return

        val foodData = entity.foodData

        foodData.foodLevel = max(foodData.foodLevel, ServerConfig.noHungerMinimum.get())
    }

    fun handleEffects(entity: LivingEntity) {
        if (entity.tickCount % ServerConfig.passivesCheckCooldown.get() != 0) return
        if (entity !is Mob && entity !is Player) return

        val genes = entity.getGenes() ?: return

        val potionGenes = mutableListOf<Gene>()

        for (gene in genes.getGeneList()) {
            if (gene.getPotion() != null) potionGenes.add(gene)

            when (gene) {
                DefaultGenes.WATER_BREATHING -> entity.airSupply = entity.maxAirSupply
                DefaultGenes.FLAMBE -> entity.setSecondsOnFire(5)
                DefaultGenes.LAY_EGG -> handleLayEgg(entity)
                DefaultGenes.MEATY_2 -> handleMeaty2(entity)
                else -> {}
            }
        }

        handlePotionGenes(entity, potionGenes)
    }

    private fun handlePotionGenes(entity: LivingEntity, potionGenes: MutableList<Gene>) {
        if (potionGenes.isEmpty()) return

        val mapOfGeneToInferiorGenes = mapOf(
            DefaultGenes.SPEED_4 to listOf(DefaultGenes.SPEED, DefaultGenes.SPEED_2),
            DefaultGenes.SPEED_2 to listOf(DefaultGenes.SPEED),
            DefaultGenes.REGENERATION_4 to listOf(DefaultGenes.REGENERATION),
            DefaultGenes.HASTE_2 to listOf(DefaultGenes.HASTE),
            DefaultGenes.RESISTANCE_2 to listOf(DefaultGenes.RESISTANCE),
            DefaultGenes.STRENGTH_2 to listOf(DefaultGenes.STRENGTH),
            DefaultGenes.POISON_4 to listOf(DefaultGenes.POISON),
            DefaultGenes.SLOWNESS_4 to listOf(DefaultGenes.SLOWNESS),
            DefaultGenes.SLOWNESS_6 to listOf(DefaultGenes.SLOWNESS, DefaultGenes.SLOWNESS_4)
        )

        val genesToSkip = mutableListOf<Gene>()

        for (gene in potionGenes.toList()) {
            mapOfGeneToInferiorGenes[gene]?.let { redundantGenes ->
                genesToSkip.addAll(redundantGenes)
            }
        }

        potionGenes.removeAll(genesToSkip)

        for (gene in potionGenes) {
            val potion = gene.getPotion() ?: continue
            entity.removeEffect(potion.effect)
            entity.addEffect(potion)
        }
    }

    fun handlePotionGeneRemoved(entity: LivingEntity, removedGene: Gene) {
        val potion = removedGene.getPotion() ?: return
        entity.removeEffect(potion.effect)
    }

    private val recentlyMeated = mutableSetOf<LivingEntity>()
    private fun handleMeaty2(entity: LivingEntity) {
        val isAbleToMeat = recentlyMeated.add(entity)
        if (!isAbleToMeat) return
        ModScheduler.scheduleTaskInTicks(ServerConfig.meaty2Cooldown.get()) {
            recentlyMeated.remove(entity)
        }

        val luck = entity.activeEffects.find { it.effect == MobEffects.LUCK }?.amplifier ?: 0

        val meatEntity = ItemEntity(
            entity.level,
            entity.x,
            entity.y,
            entity.z,
            ItemStack(Items.COOKED_PORKCHOP, 1 + luck)
        )

        entity.level.addFreshEntity(meatEntity)
    }

    private val recentlyLaidEgg = mutableSetOf<LivingEntity>()
    private fun handleLayEgg(entity: LivingEntity) {
        val isAbleToDropEgg = recentlyLaidEgg.add(entity)
        if (!isAbleToDropEgg) return
        ModScheduler.scheduleTaskInTicks(ServerConfig.eggCooldown.get()) {
            recentlyLaidEgg.remove(entity)
        }

        val luck = entity.activeEffects.find { it.effect == MobEffects.LUCK }?.amplifier ?: 0

        val eggEntity = ItemEntity(
            entity.level,
            entity.x,
            entity.y,
            entity.z,
            ItemStack(Items.EGG, 1 + luck)
        )

        entity.level.addFreshEntity(eggEntity)
    }

    fun handleMobSight(entity: LivingEntity) {

        if (entity.tickCount % ServerConfig.mobSightCooldown.get() != 0) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.MOB_SIGHT)) return

        if (entity.tickCount % ServerConfig.mobSightCooldown.get() != 0) return

        val searchArea = entity.boundingBox.inflate(ServerConfig.mobSightRadius.get())
        val nearbyLivingEntities = entity.level.getEntities(entity, searchArea).filterIsInstance<Mob>()

        val glowingEffect = MobEffectInstance(
            MobEffects.GLOWING,
            ServerConfig.mobSightCooldown.get() * 4,
            0,
            false,
            false
        )

        nearbyLivingEntities.forEach {
            it.addEffect(glowingEffect)
        }
    }

    private val flyablePlayers = mutableSetOf<UUID>()
    fun handleFlight(player: Player) {

        if (player.level.isClientSide) return
        if (player.isCreative || player.isSpectator) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.FLIGHT)) {
            if (flyablePlayers.contains(player.uuid)) {
                player.abilities.mayfly = false
                player.abilities.flying = false
                player.onUpdateAbilities()
                flyablePlayers.remove(player.uuid)
            }
            return
        }

        player.abilities.mayfly = true
        player.onUpdateAbilities()
        flyablePlayers.add(player.uuid)
    }

    fun handleItemMagnet(player: Player) {
        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return

        if (player.tickCount % ServerConfig.itemMagnetCooldown.get() != 0) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.ITEM_MAGNET)) return

        if (AntiFieldOrbItem.isActiveForPlayer(player)) return

        val nearbyItems = player.level.getEntitiesOfClass(
            ItemEntity::class.java,
            player.boundingBox.inflate(ServerConfig.itemMagnetRadius.get())
        )

        for (itemEntity in nearbyItems) {
            if (itemEntity.item.count <= 0) continue
            if (itemEntity.item.`is`(ModTags.MAGNET_BLACKLIST)) continue

            val pickupEvent = EntityItemPickupEvent(player, itemEntity)
            MinecraftForge.EVENT_BUS.post(pickupEvent)
            if (pickupEvent.isCanceled) continue

            itemEntity.playerTouch(player)
        }
    }

    fun handleXpMagnet(player: Player) {
        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return

        if (player.tickCount % ServerConfig.xpMagnetCooldown.get() != 0) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.XP_MAGNET)) return

        if (AntiFieldOrbItem.isActiveForPlayer(player)) return

        val nearbyXpOrbs = player.level.getEntitiesOfClass(
            ExperienceOrb::class.java,
            player.boundingBox.inflate(ServerConfig.xpMagnetRadius.get())
        )

        for (xpOrb in nearbyXpOrbs) {

            val pickupEvent = PlayerXpEvent.PickupXp(player, xpOrb)
            MinecraftForge.EVENT_BUS.post(pickupEvent)
            if (pickupEvent.isCanceled) continue

            xpOrb.playerTouch(player)
            player.takeXpDelay = 1
        }
    }

}