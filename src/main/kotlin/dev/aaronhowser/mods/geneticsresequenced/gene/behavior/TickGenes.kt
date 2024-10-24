package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.BaseModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.BaseModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.item.AntiFieldOrbItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.EntityTypeTags
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
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import kotlin.math.max

object TickGenes {

    fun handleBioluminescence(entity: LivingEntity) {
        val bioluminescence = BaseModGenes.BIOLUMINESCENCE.getHolder(entity.registryAccess()) ?: return

        if (bioluminescence.isDisabled) return

        if (entity.tickCount % ServerConfig.bioluminescenceCooldown.get() != 0) return

        val level = entity.level()

        if (level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) > 8) return

        if (!entity.hasGene(BaseModGenes.BIOLUMINESCENCE)) return

        val headBlock = level.getBlockState(entity.blockPosition().above())
        if (!headBlock.isAir) return

        level.setBlockAndUpdate(
            entity.blockPosition().above(),
            ModBlocks.BIOLUMINESCENCE_BLOCK.get().defaultBlockState()
        )
    }

    fun handlePhotosynthesis(entity: LivingEntity) {
        val photosynthesis = BaseModGenes.PHOTOSYNTHESIS.getHolder(entity.registryAccess()) ?: return

        if (photosynthesis.isDisabled) return

        if (entity !is Player) return
        if (entity.tickCount % ServerConfig.photosynthesisCooldown.get() != 0) return

        if (!entity.hasGene(BaseModGenes.PHOTOSYNTHESIS)) return

        val foodData = entity.foodData

        if (!foodData.needsFood()) return

        val inDirectSunlight = entity.level().canSeeSky(entity.blockPosition())
        val isDay = entity.level().isDay
        if (!inDirectSunlight || !isDay) return

        foodData.eat(
            ServerConfig.photosynthesisHungerAmount.get(),
            ServerConfig.photosynthesisSaturationAmount.get().toFloat()
        )
    }

    fun handleNoHunger(entity: Player) {
        val noHunger = BaseModGenes.NO_HUNGER.getHolder(entity.registryAccess()) ?: return
        if (noHunger.isDisabled) return

        if (entity.tickCount % ServerConfig.noHungerCooldown.get() != 0) return

        if (!entity.hasGene(BaseModGenes.NO_HUNGER)) return

        val foodData = entity.foodData

        foodData.foodLevel = max(foodData.foodLevel, ServerConfig.noHungerMinimum.get())
    }

    fun handleTickingGenes(entity: LivingEntity) {
        if (entity.tickCount % ServerConfig.passivesCheckCooldown.get() != 0) return
        if (entity !is Mob && entity !is Player) return

        val geneHolders = entity.geneHolders

        val potionGenes = mutableListOf<Holder<Gene>>()

        for (geneHolder in geneHolders) {
            if (geneHolder.isDisabled) continue

            if (geneHolder.value().getPotion() != null) potionGenes.add(geneHolder)

            when (geneHolder) {
                BaseModGenes.WATER_BREATHING -> entity.airSupply = entity.maxAirSupply
                BaseModGenes.FLAMBE -> entity.remainingFireTicks = ServerConfig.passivesCheckCooldown.get() * 2 * 20
                BaseModGenes.LAY_EGG -> handleLayEgg(entity)
                BaseModGenes.MEATY_TWO -> handleMeaty2(entity)

                BaseModGenes.GREEN_DEATH,
                BaseModGenes.UN_UNDEATH,
                BaseModGenes.GRAY_DEATH,
                BaseModGenes.WHITE_DEATH,
                BaseModGenes.BLACK_DEATH -> {
                    handleDeathGenes(entity, geneHolder)
                }
            }
        }

        handlePotionGenes(entity, potionGenes)
    }

    private fun handleDeathGenes(entity: LivingEntity, gene: Holder<Gene>) {
        if (gene == BaseModGenes.BLACK_DEATH) {
            entity.hurt(virusDamageSource(entity.level()), entity.maxHealth * 1000)
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
            BaseModGenes.GREEN_DEATH -> { it -> it is Creeper }
            BaseModGenes.UN_UNDEATH -> { it -> it.type.`is`(EntityTypeTags.UNDEAD) }
            BaseModGenes.GRAY_DEATH -> { it -> it is AgeableMob || it is Zombie || it is Piglin }
            BaseModGenes.WHITE_DEATH -> { it -> it.type.category == MobCategory.MONSTER }
            else -> return
        }

        if (!entityPredicate(entity)) return
        entity.hurt(virusDamageSource(entity.level()), maxOf(entity.health / 2, 2f))
    }

    private val virusDamageKey = ResourceKey.create(Registries.DAMAGE_TYPE, OtherUtil.modResource("virus"))
    private fun virusDamageSource(level: Level): DamageSource = level.damageSources().source(virusDamageKey)

    private val mapOfGeneToInferiorGenes: Map<ResourceKey<Gene>, List<ResourceKey<Gene>>> = mapOf(
        BaseModGenes.SPEED_FOUR to listOf(BaseModGenes.SPEED, BaseModGenes.SPEED_TWO),
        BaseModGenes.SPEED_TWO to listOf(BaseModGenes.SPEED),
        BaseModGenes.REGENERATION_FOUR to listOf(BaseModGenes.REGENERATION),
        BaseModGenes.HASTE_TWO to listOf(BaseModGenes.HASTE),
        BaseModGenes.RESISTANCE_TWO to listOf(BaseModGenes.RESISTANCE),
        BaseModGenes.STRENGTH_TWO to listOf(BaseModGenes.STRENGTH),
        BaseModGenes.POISON_FOUR to listOf(BaseModGenes.POISON),
        BaseModGenes.SLOWNESS_FOUR to listOf(BaseModGenes.SLOWNESS),
        BaseModGenes.SLOWNESS_SIX to listOf(BaseModGenes.SLOWNESS, BaseModGenes.SLOWNESS_FOUR)
    )

    private fun handlePotionGenes(entity: LivingEntity, potionGenes: MutableList<Holder<Gene>>) {
        if (potionGenes.isEmpty()) return

        val genesToSkip = mutableListOf<ResourceKey<Gene>>()

        for (geneHolder in potionGenes.toList()) {
            mapOfGeneToInferiorGenes[geneHolder.key]?.let { redundantGenes ->
                genesToSkip.addAll(redundantGenes)
            }
        }

        potionGenes.removeAll(genesToSkip.mapNotNull { it.getHolder(entity.registryAccess()) })

        for (geneHolder in potionGenes) {
            val potion = geneHolder.value().getPotion() ?: continue
            entity.removeEffect(potion.effect)
            entity.addEffect(potion)
        }
    }

    fun handlePotionGeneRemoved(entity: LivingEntity, removedGene: Holder<Gene>) {
        val potion = removedGene.value().getPotion() ?: return
        entity.removeEffect(potion.effect)
    }

    private val recentlyMeated2 = GeneCooldown(
        BaseModGenes.MEATY_TWO,
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
        BaseModGenes.LAY_EGG,
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
        val mobSight = BaseModGenes.MOB_SIGHT.getHolder(entity.registryAccess()) ?: return

        if (mobSight.isDisabled) return
        if (entity.tickCount % ServerConfig.mobSightCooldown.get() != 0) return

        if (!entity.hasGene(BaseModGenes.MOB_SIGHT)) return

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

    fun handleItemMagnet(player: Player) {
        val itemMagnet = BaseModGenes.ITEM_MAGNET.getHolder(player.registryAccess()) ?: return
        if (itemMagnet.isDisabled) return

        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return

        if (player.tickCount % ServerConfig.itemMagnetCooldown.get() != 0) return

        if (!player.hasGene(BaseModGenes.ITEM_MAGNET)) return

        if (AntiFieldOrbItem.isActiveForPlayer(player)) return

        val nearbyItems = player.level().getEntitiesOfClass(
            ItemEntity::class.java,
            player.boundingBox.inflate(ServerConfig.itemMagnetRadius.get())
        )

        for (itemEntity in nearbyItems) {
            if (itemEntity.item.count <= 0) continue
            if (itemEntity.owner == player && itemEntity.age < 20 * 3) continue
            if (itemEntity.item.`is`(ModItemTagsProvider.MAGNET_ITEM_BLACKLIST)) continue

            if (AntiFieldBlock.isNearActiveAntifield(player.level(), itemEntity.blockPosition())) continue

            itemEntity.playerTouch(player)
        }
    }

    fun itemMagnetBlacklistTooltip(event: ItemTooltipEvent) {
        if (!ClientConfig.itemMagnetBlacklistTooltip.get()) return

        val player = event.entity ?: return
        if (!player.hasGene(BaseModGenes.ITEM_MAGNET)) return

        val item = event.itemStack
        if (!item.`is`(ModItemTagsProvider.MAGNET_ITEM_BLACKLIST)) return

        val component = ModLanguageProvider.Tooltips.ITEM_MAGNET_BLACKLIST
            .toComponent()
            .withColor(ChatFormatting.DARK_GRAY)
        event.toolTip.add(component)
    }

    fun handleXpMagnet(player: Player) {
        val xpMagnet = BaseModGenes.XP_MAGNET.getHolder(player.registryAccess()) ?: return
        if (xpMagnet.isDisabled) return

        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return

        if (player.tickCount % ServerConfig.xpMagnetCooldown.get() != 0) return

        if (!player.hasGene(BaseModGenes.XP_MAGNET)) return

        if (AntiFieldOrbItem.isActiveForPlayer(player)) return

        val nearbyXpOrbs = player.level().getEntitiesOfClass(
            ExperienceOrb::class.java,
            player.boundingBox.inflate(ServerConfig.xpMagnetRadius.get())
        )

        for (xpOrb in nearbyXpOrbs) {
            if (AntiFieldBlock.isNearActiveAntifield(player.level(), xpOrb.blockPosition())) continue

            xpOrb.playerTouch(player)
            player.takeXpDelay = 1
        }
    }


}