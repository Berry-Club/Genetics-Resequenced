package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneContainer.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneContainer.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.tags.EntityTypeTags
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
import java.util.*
import kotlin.math.max

object TickGenes {

    fun handleBioluminescence(entity: LivingEntity) {
        if (!ModGenes.bioluminescence.isActive) return

        if (entity.tickCount % ServerConfig.bioluminescenceCooldown.get() != 0) return

        val level = entity.level()

        if (level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) > 8) return

        if (!entity.hasGene(ModGenes.bioluminescence)) return

        val headBlock = level.getBlockState(entity.blockPosition().above())
        if (!headBlock.isAir) return

        level.setBlockAndUpdate(
            entity.blockPosition().above(),
            ModBlocks.BIOLUMINESCENCE_BLOCK.get().defaultBlockState()
        )
    }

    fun handlePhotosynthesis(entity: LivingEntity) {
        if (!ModGenes.photosynthesis.isActive) return

        if (entity !is Player) return
        if (entity.tickCount % ServerConfig.photosynthesisCooldown.get() != 0) return

        if (!entity.hasGene(ModGenes.photosynthesis)) return

        val foodData = entity.foodData

        if (!foodData.needsFood()) return

        val inDirectSunlight = entity.level().canSeeSky(entity.blockPosition())
        val isDay = entity.level().isDay
        if (!inDirectSunlight || !isDay) return

        foodData.eat(
            ServerConfig.photoSynthesisHungerAmount.get(),
            ServerConfig.photoSynthesisSaturationAmount.get().toFloat()
        )
    }

    fun handleNoHunger(entity: Player) {
        if (!ModGenes.noHunger.isActive) return

        if (entity.tickCount % ServerConfig.noHungerCooldown.get() != 0) return

        if (!entity.hasGene(ModGenes.noHunger)) return

        val foodData = entity.foodData

        foodData.foodLevel = max(foodData.foodLevel, ServerConfig.noHungerMinimum.get())
    }

    fun handleTickingGenes(entity: LivingEntity) {
        if (entity.tickCount % ServerConfig.passivesCheckCooldown.get() != 0) return
        if (entity !is Mob && entity !is Player) return

        val genes = entity.genes

        val potionGenes = mutableListOf<Gene>()

        for (gene in genes) {
            if (!gene.isActive) continue

            if (gene.getPotion() != null) potionGenes.add(gene)

            when (gene) {
                ModGenes.waterBreathing -> entity.airSupply = entity.maxAirSupply
                ModGenes.flambe -> entity.remainingFireTicks = ServerConfig.passivesCheckCooldown.get() * 2 * 20
                ModGenes.layEgg -> handleLayEgg(entity)
                ModGenes.meatyTwo -> handleMeaty2(entity)

                ModGenes.greenDeath,
                ModGenes.unUndeath,
                ModGenes.grayDeath,
                ModGenes.whiteDeath,
                ModGenes.blackDeath -> {
                    handleDeathGenes(entity, gene)
                }
            }
        }

        handlePotionGenes(entity, potionGenes)
    }

    //TODO
    private fun handleDeathGenes(entity: LivingEntity, gene: Gene) {
        if (gene == ModGenes.blackDeath) {
//            entity.hurt(virusDamageSource, entity.maxHealth * 1000)
            entity.kill()

            // I have no idea if this is even necessary
            if (entity.isAlive) {
                val damageSources = entity.level().damageSources()
                entity.hurt(damageSources.fellOutOfWorld(), entity.maxHealth * 1000)
                entity.hurt(damageSources.magic(), entity.maxHealth * 1000)
                entity.hurt(damageSources.wither(), entity.maxHealth * 1000)

                if (entity.isAlive) {
                    entity.remove(Entity.RemovalReason.KILLED)
                }
            }
        }

        val entityPredicate: (LivingEntity) -> Boolean = when (gene) {
            ModGenes.greenDeath -> { it -> it is Creeper }
            ModGenes.unUndeath -> { it -> it.type.`is`(EntityTypeTags.UNDEAD) }
            ModGenes.grayDeath -> { it -> it is AgeableMob || it is Zombie || it is Piglin }
            ModGenes.whiteDeath -> { it -> it.type.category == MobCategory.MONSTER }
            else -> return
        }

        if (!entityPredicate(entity)) return
//        entity.hurt(virusDamageSource, maxOf(entity.health / 2, 2f))
    }

    //TODO:
//    private val virusDamageSource = DamageSource(OtherUtil.modResource("virus").toString())

    private fun handlePotionGenes(entity: LivingEntity, potionGenes: MutableList<Gene>) {
        if (potionGenes.isEmpty()) return

        val mapOfGeneToInferiorGenes = mapOf(
            ModGenes.speedFour to listOf(ModGenes.speed, ModGenes.speedTwo),
            ModGenes.speedTwo to listOf(ModGenes.speed),
            ModGenes.regenerationFour to listOf(ModGenes.regeneration),
            ModGenes.hasteTwo to listOf(ModGenes.haste),
            ModGenes.resistanceTwo to listOf(ModGenes.resistance),
            ModGenes.strengthTwo to listOf(ModGenes.strength),
            ModGenes.poisonFour to listOf(ModGenes.poison),
            ModGenes.slownessFour to listOf(ModGenes.slowness),
            ModGenes.slownessSix to listOf(ModGenes.slowness, ModGenes.slownessFour)
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
        ModGenes.meatyTwo,
        ServerConfig.meaty2Cooldown.get(),
        notifyPlayer = false
    )

    private fun handleMeaty2(entity: LivingEntity) {
        val newlyMeated = recentlyMeated2.add(entity)
        if (!newlyMeated) return

        val luck = entity.activeEffects.find { it.effect == MobEffects.LUCK }?.amplifier ?: 0

        val meatEntity = ItemEntity(
            entity.level(),
            entity.x,
            entity.y,
            entity.z,
            ItemStack(Items.COOKED_PORKCHOP, 1 + luck)
        )

        entity.level().addFreshEntity(meatEntity)
    }

    private val recentlyLaidEgg = GeneCooldown(
        ModGenes.layEgg,
        ServerConfig.eggCooldown.get(),
        notifyPlayer = false
    )

    private fun handleLayEgg(entity: LivingEntity) {
        val hasNotRecentlyLainEgg = recentlyLaidEgg.add(entity)
        if (!hasNotRecentlyLainEgg) return

        val luck = entity.activeEffects.find { it.effect == MobEffects.LUCK }?.amplifier ?: 0

        val eggEntity = ItemEntity(
            entity.level(),
            entity.x,
            entity.y,
            entity.z,
            ItemStack(Items.EGG, 1 + luck)
        )

        entity.level().addFreshEntity(eggEntity)
    }

    fun handleMobSight(entity: Player) {
        if (!ModGenes.mobSight.isActive) return
        if (entity.tickCount % ServerConfig.mobSightCooldown.get() != 0) return

        if (!entity.hasGene(ModGenes.mobSight)) return

        val searchArea = entity.boundingBox.inflate(ServerConfig.mobSightRadius.get())
        val nearbyLivingEntities = entity.level().getEntities(entity, searchArea).filterIsInstance<Mob>()

        val glowingEffect = MobEffectInstance(
            MobEffects.GLOWING,
            maxOf(ServerConfig.mobSightCooldown.get() * 4, 20 * 30),
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
        if (!ModGenes.flight.isActive) return

        if (player.level().isClientSide) return
        if (player.isCreative || player.isSpectator) return

        if (!player.hasGene(ModGenes.flight)) {
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

    //TODO
    fun handleItemMagnet(player: Player) {
//        if (!ModGenes.itemMagnet.isActive) return
//        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return
//
//        if (player.tickCount % ServerConfig.itemMagnetCooldown.get() != 0) return
//
//        if (!player.hasGene(ModGenes.itemMagnet)) return
//
//        if (AntiFieldOrbItem.isActiveForPlayer(player)) return
//
//        val nearbyItems = player.level.getEntitiesOfClass(
//            ItemEntity::class.java,
//            player.boundingBox.inflate(ServerConfig.itemMagnetRadius.get())
//        )
//
//        for (itemEntity in nearbyItems) {
//            if (itemEntity.item.count <= 0) continue
//            if (itemEntity.item.`is`(ModTags.MAGNET_ITEM_BLACKLIST)) continue
//
//            if (AntiFieldBlock.locationIsNearAntifield(player.level, itemEntity.blockPosition())) continue
//
//            val pickupEvent = EntityItemPickupEvent(player, itemEntity)
//            MinecraftForge.EVENT_BUS.post(pickupEvent)
//            if (pickupEvent.isCanceled) continue
//
//            itemEntity.playerTouch(player)
//        }
    }

    //TODO
    fun handleXpMagnet(player: Player) {
//        if (!ModGenes.xpMagnet.isActive) return
//        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return
//
//        if (player.tickCount % ServerConfig.xpMagnetCooldown.get() != 0) return
//
//        if (!player.hasGene(ModGenes.xpMagnet)) return
//
//        if (AntiFieldOrbItem.isActiveForPlayer(player)) return
//
//        val nearbyXpOrbs = player.level.getEntitiesOfClass(
//            ExperienceOrb::class.java,
//            player.boundingBox.inflate(ServerConfig.xpMagnetRadius.get())
//        )
//
//        for (xpOrb in nearbyXpOrbs) {
//            if (AntiFieldBlock.locationIsNearAntifield(player.level, xpOrb.blockPosition())) continue
//
//            val pickupEvent = PlayerXpEvent.PickupXp(player, xpOrb)
//            MinecraftForge.EVENT_BUS.post(pickupEvent)
//            if (pickupEvent.isCanceled) continue
//
//            xpOrb.playerTouch(player)
//            player.takeXpDelay = 1
//        }
    }


}