package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.item.AntiFieldOrbItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
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
        if (ModGenes.BIOLUMINESCENCE.get().isActive) return

        if (entity.tickCount % ServerConfig.bioluminescenceCooldown.get() != 0) return

        val level = entity.level()

        if (level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) > 8) return

        if (!entity.hasGene(ModGenes.BIOLUMINESCENCE.get())) return

        val headBlock = level.getBlockState(entity.blockPosition().above())
        if (!headBlock.isAir) return

        level.setBlockAndUpdate(
            entity.blockPosition().above(),
            ModBlocks.BIOLUMINESCENCE_BLOCK.get().defaultBlockState()
        )
    }

    fun handlePhotosynthesis(entity: LivingEntity) {
        if (!ModGenes.PHOTOSYNTHESIS.get().isActive) return

        if (entity !is Player) return
        if (entity.tickCount % ServerConfig.photosynthesisCooldown.get() != 0) return

        if (!entity.hasGene(ModGenes.PHOTOSYNTHESIS.get())) return

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
        if (!ModGenes.NO_HUNGER.get().isActive) return

        if (entity.tickCount % ServerConfig.noHungerCooldown.get() != 0) return

        if (!entity.hasGene(ModGenes.NO_HUNGER.get())) return

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
                ModGenes.WATER_BREATHING.get() -> entity.airSupply = entity.maxAirSupply
                ModGenes.FLAMBE.get() -> entity.remainingFireTicks = ServerConfig.passivesCheckCooldown.get() * 2 * 20
                ModGenes.LAY_EGG.get() -> handleLayEgg(entity)
                ModGenes.MEATY_TWO.get() -> handleMeaty2(entity)

                ModGenes.GREEN_DEATH.get(),
                ModGenes.UN_UNDEATH.get(),
                ModGenes.GRAY_DEATH.get(),
                ModGenes.WHITE_DEATH.get(),
                ModGenes.BLACK_DEATH.get() -> {
                    handleDeathGenes(entity, gene)
                }
            }
        }

        handlePotionGenes(entity, potionGenes)
    }

    private fun handleDeathGenes(entity: LivingEntity, gene: Gene) {
        if (gene == ModGenes.BLACK_DEATH.get()) {
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
            ModGenes.GREEN_DEATH.get() -> { it -> it is Creeper }
            ModGenes.UN_UNDEATH.get() -> { it -> it.type.`is`(EntityTypeTags.UNDEAD) }
            ModGenes.GRAY_DEATH.get() -> { it -> it is AgeableMob || it is Zombie || it is Piglin }
            ModGenes.WHITE_DEATH.get() -> { it -> it.type.category == MobCategory.MONSTER }
            else -> return
        }

        if (!entityPredicate(entity)) return
        entity.hurt(virusDamageSource(entity.level()), maxOf(entity.health / 2, 2f))
    }

    private val virusDamageKey = ResourceKey.create(Registries.DAMAGE_TYPE, OtherUtil.modResource("virus"))
    private fun virusDamageSource(level: Level): DamageSource = level.damageSources().source(virusDamageKey)

    private val mapOfGeneToInferiorGenes: Map<Gene, List<Gene>> = mapOf(
        ModGenes.SPEED_FOUR.get() to listOf(ModGenes.SPEED.get(), ModGenes.SPEED_TWO.get()),
        ModGenes.SPEED_TWO.get() to listOf(ModGenes.SPEED.get()),
        ModGenes.REGENERATION_FOUR.get() to listOf(ModGenes.REGENERATION.get()),
        ModGenes.HASTE_TWO.get() to listOf(ModGenes.HASTE.get()),
        ModGenes.RESISTANCE_TWO.get() to listOf(ModGenes.RESISTANCE.get()),
        ModGenes.STRENGTH_TWO.get() to listOf(ModGenes.STRENGTH.get()),
        ModGenes.POISON_FOUR.get() to listOf(ModGenes.POISON.get()),
        ModGenes.SLOWNESS_FOUR.get() to listOf(ModGenes.SLOWNESS.get()),
        ModGenes.SLOWNESS_SIX.get() to listOf(ModGenes.SLOWNESS.get(), ModGenes.SLOWNESS_FOUR.get())
    )

    private fun handlePotionGenes(entity: LivingEntity, potionGenes: MutableList<Gene>) {
        if (potionGenes.isEmpty()) return

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
        ModGenes.MEATY_TWO.get(),
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
        ModGenes.LAY_EGG.get(),
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
        if (!ModGenes.MOB_SIGHT.get().isActive) return
        if (entity.tickCount % ServerConfig.mobSightCooldown.get() != 0) return

        if (!entity.hasGene(ModGenes.MOB_SIGHT.get())) return

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
        if (!ModGenes.ITEM_MAGNET.get().isActive) return
        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return

        if (player.tickCount % ServerConfig.itemMagnetCooldown.get() != 0) return

        if (!player.hasGene(ModGenes.ITEM_MAGNET.get())) return

        if (AntiFieldOrbItem.isActiveForPlayer(player)) return

        val nearbyItems = player.level().getEntitiesOfClass(
            ItemEntity::class.java,
            player.boundingBox.inflate(ServerConfig.itemMagnetRadius.get())
        )

        for (itemEntity in nearbyItems) {
            if (itemEntity.item.count <= 0) continue
            if (itemEntity.owner == player && itemEntity.age < 20 * 3) continue
            if (itemEntity.item.`is`(ModTags.MAGNET_ITEM_BLACKLIST)) continue

            if (AntiFieldBlock.isNearActiveAntifield(player.level(), itemEntity.blockPosition())) continue

            itemEntity.playerTouch(player)
        }
    }

    fun itemMagnetBlacklistTooltip(event: ItemTooltipEvent) {
        if (!ClientConfig.itemMagnetBlacklistTooltip.get()) return

        val player = event.entity ?: return
        if (!player.hasGene(ModGenes.ITEM_MAGNET.get())) return

        val item = event.itemStack
        if (!item.`is`(ModTags.MAGNET_ITEM_BLACKLIST)) return

        val component = ModLanguageProvider.Tooltips.ITEM_MAGNET_BLACKLIST
            .toComponent()
            .withColor(ChatFormatting.DARK_GRAY)
        event.toolTip.add(component)
    }

    fun handleXpMagnet(player: Player) {
        if (!ModGenes.XP_MAGNET.get().isActive) return
        if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return

        if (player.tickCount % ServerConfig.xpMagnetCooldown.get() != 0) return

        if (!player.hasGene(ModGenes.XP_MAGNET.get())) return

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