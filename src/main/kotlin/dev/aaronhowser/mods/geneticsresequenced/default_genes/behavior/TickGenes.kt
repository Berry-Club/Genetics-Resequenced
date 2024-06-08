package dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.blocks.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.AntiFieldOrbItem
import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.util.GeneCooldown
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.monster.piglin.Piglin
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
        if (!DefaultGenes.bioluminescence.isActive) return

        if (entity.tickCount % ServerConfig.bioluminescenceCooldown.get() != 0) return

        if (entity.level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) > 8) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.bioluminescence)) return

        val headBlock = entity.level.getBlockState(entity.blockPosition().above())
        if (!headBlock.isAir) return

        entity.level.setBlockAndUpdate(
            entity.blockPosition().above(),
            ModBlocks.BIOLUMINESCENCE_BLOCK.get().defaultBlockState()
        )
    }

    fun handlePhotosynthesis(entity: LivingEntity) {
        if (!DefaultGenes.photosynthesis.isActive) return

        if (entity !is Player) return
        if (entity.tickCount % ServerConfig.photosynthesisCooldown.get() != 0) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.photosynthesis)) return

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
        if (!DefaultGenes.noHunger.isActive) return

        if (entity.tickCount % ServerConfig.noHungerCooldown.get() != 0) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.noHunger)) return

        val foodData = entity.foodData

        foodData.foodLevel = max(foodData.foodLevel, ServerConfig.noHungerMinimum.get())
    }

    fun handleTickingGenes(entity: LivingEntity) {
        if (entity.tickCount % ServerConfig.passivesCheckCooldown.get() != 0) return
        if (entity !is Mob && entity !is Player) return

        val genes = entity.getGenes() ?: return

        val potionGenes = mutableListOf<Gene>()

        for (gene in genes.getGeneList()) {
            if (!gene.isActive) continue

            if (gene.getPotion() != null) potionGenes.add(gene)

            when (gene) {
                DefaultGenes.waterBreathing -> entity.airSupply = entity.maxAirSupply
                DefaultGenes.flambe -> entity.setSecondsOnFire(ServerConfig.passivesCheckCooldown.get() * 2)
                DefaultGenes.layEgg -> handleLayEgg(entity)
                DefaultGenes.meatyTwo -> handleMeaty2(entity)

                DefaultGenes.greenDeath,
                DefaultGenes.unUndeath,
                DefaultGenes.grayDeath,
                DefaultGenes.whiteDeath,
                DefaultGenes.blackDeath -> {
                    handleDeathGenes(entity, gene)
                }
            }
        }

        handlePotionGenes(entity, potionGenes)
    }

    private fun handleDeathGenes(entity: LivingEntity, gene: Gene) {
        if (gene == DefaultGenes.blackDeath) {
            entity.hurt(virusDamageSource, entity.maxHealth * 1000)
            entity.kill()
        }

        val entityPredicate: (LivingEntity) -> Boolean = when (gene) {
            DefaultGenes.greenDeath -> { it -> it is Creeper }
            DefaultGenes.unUndeath -> { it -> it.type.category == MobType.UNDEAD }
            DefaultGenes.grayDeath -> { it -> it is AgeableMob || it is Zombie || it is Piglin }
            DefaultGenes.whiteDeath -> { it -> it.type.category == MobCategory.MONSTER }
            else -> return
        }

        if (!entityPredicate(entity)) return
        entity.hurt(virusDamageSource, maxOf(entity.health / 2, 2f))
    }

    private val virusDamageSource = DamageSource("virus")

    private fun handlePotionGenes(entity: LivingEntity, potionGenes: MutableList<Gene>) {
        if (potionGenes.isEmpty()) return

        val mapOfGeneToInferiorGenes = mapOf(
            DefaultGenes.speedFour to listOf(DefaultGenes.speed, DefaultGenes.speedTwo),
            DefaultGenes.speedTwo to listOf(DefaultGenes.speed),
            DefaultGenes.regenerationFour to listOf(DefaultGenes.regeneration),
            DefaultGenes.hasteTwo to listOf(DefaultGenes.haste),
            DefaultGenes.resistanceTwo to listOf(DefaultGenes.resistance),
            DefaultGenes.strengthTwo to listOf(DefaultGenes.strength),
            DefaultGenes.poisonFour to listOf(DefaultGenes.poison),
            DefaultGenes.slownessFour to listOf(DefaultGenes.slowness),
            DefaultGenes.slownessSix to listOf(DefaultGenes.slowness, DefaultGenes.slownessFour)
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

    private val recentlyMeated2 = GeneCooldown(
        DefaultGenes.meatyTwo,
        ServerConfig.meaty2Cooldown.get(),
        notifyPlayer = false
    )

    private fun handleMeaty2(entity: LivingEntity) {

        val newlyMeated = recentlyMeated2.add(entity)

        if (!newlyMeated) return

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

    private val recentlyLaidEgg = GeneCooldown(
        DefaultGenes.layEgg,
        ServerConfig.eggCooldown.get(),
        notifyPlayer = false
    )

    private fun handleLayEgg(entity: LivingEntity) {

        val hasNotRecentlyLainEgg = recentlyLaidEgg.add(entity)
        if (!hasNotRecentlyLainEgg) return

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

    fun handleMobSight(entity: Player) {
        if (!DefaultGenes.mobSight.isActive) return
        if (entity.tickCount % ServerConfig.mobSightCooldown.get() != 0) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.mobSight)) return

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
        if (!DefaultGenes.flight.isActive) return

        if (player.level.isClientSide) return
        if (player.isCreative || player.isSpectator) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.flight)) {
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
        if (!DefaultGenes.itemMagnet.isActive) return
        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return

        if (player.tickCount % ServerConfig.itemMagnetCooldown.get() != 0) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.itemMagnet)) return

        if (AntiFieldOrbItem.isActiveForPlayer(player)) return

        val nearbyItems = player.level.getEntitiesOfClass(
            ItemEntity::class.java,
            player.boundingBox.inflate(ServerConfig.itemMagnetRadius.get())
        )

        for (itemEntity in nearbyItems) {
            if (itemEntity.item.count <= 0) continue
            if (itemEntity.item.`is`(ModTags.MAGNET_ITEM_BLACKLIST)) continue

            if (AntiFieldBlock.locationIsNearAntifield(player.level, itemEntity.blockPosition())) continue

            val pickupEvent = EntityItemPickupEvent(player, itemEntity)
            MinecraftForge.EVENT_BUS.post(pickupEvent)
            if (pickupEvent.isCanceled) continue

            itemEntity.playerTouch(player)
        }
    }

    fun handleXpMagnet(player: Player) {
        if (!DefaultGenes.xpMagnet.isActive) return
        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return

        if (player.tickCount % ServerConfig.xpMagnetCooldown.get() != 0) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.xpMagnet)) return

        if (AntiFieldOrbItem.isActiveForPlayer(player)) return

        val nearbyXpOrbs = player.level.getEntitiesOfClass(
            ExperienceOrb::class.java,
            player.boundingBox.inflate(ServerConfig.xpMagnetRadius.get())
        )

        for (xpOrb in nearbyXpOrbs) {
            if (AntiFieldBlock.locationIsNearAntifield(player.level, xpOrb.blockPosition())) continue

            val pickupEvent = PlayerXpEvent.PickupXp(player, xpOrb)
            MinecraftForge.EVENT_BUS.post(pickupEvent)
            if (pickupEvent.isCanceled) continue

            xpOrb.playerTouch(player)
            player.takeXpDelay = 1
        }
    }

}