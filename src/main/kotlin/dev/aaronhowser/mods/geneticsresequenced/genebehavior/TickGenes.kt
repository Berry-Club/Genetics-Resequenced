package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.block.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.event.ModScheduler
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.LightLayer

object TickGenes {

    fun handleBioluminescence(entity: LivingEntity) {

        if (entity.tickCount % ServerConfig.bioluminescenceCooldown.get() != 0) return

        if (entity.level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) > 8) return

        val genes = entity.getGenes() ?: return
        if (!genes.hasGene(EnumGenes.BIOLUMINESCENCE)) return

        val feetBlock = entity.level.getBlockState(entity.blockPosition())
        if (!feetBlock.isAir) return

        entity.level.setBlockAndUpdate(
            entity.blockPosition(),
            ModBlocks.BIOLUMINESCENCE.defaultBlockState()
        )
    }

    fun handleEffects(entity: LivingEntity) {
        if (entity.tickCount % 40 != 0) return
        if (entity !is Mob && entity !is Player) return

        val genes = entity.getGenes() ?: return

        val potionGenes = mutableListOf<EnumGenes>()

        for (gene in genes.getGeneList()) {
            if (gene.getPotion() != null) potionGenes.add(gene)

            when (gene) {
                EnumGenes.WATER_BREATHING -> entity.airSupply = entity.maxAirSupply
                EnumGenes.FLAME -> entity.setSecondsOnFire(5)
                EnumGenes.MOB_SIGHT -> handleMobSight(entity)
                EnumGenes.LAY_EGG -> handleLayEgg(entity)
                EnumGenes.MEATY_2 -> handleMeaty2(entity)
                else -> {}
            }
        }

        handlePotionGenes(entity, potionGenes)
    }

    private fun handlePotionGenes(entity: LivingEntity, potionGenes: MutableList<EnumGenes>) {

        if (potionGenes.isEmpty()) return

        val potionLevels = mapOf(
            EnumGenes.SPEED_4 to listOf(EnumGenes.SPEED, EnumGenes.SPEED_2),
            EnumGenes.SPEED_2 to listOf(EnumGenes.SPEED),
            EnumGenes.REGENERATION_4 to listOf(EnumGenes.REGENERATION),
            EnumGenes.HASTE_2 to listOf(EnumGenes.HASTE),
            EnumGenes.RESISTANCE_2 to listOf(EnumGenes.RESISTANCE),
            EnumGenes.STRENGTH_2 to listOf(EnumGenes.STRENGTH),
            EnumGenes.POISON_4 to listOf(EnumGenes.POISON),
            EnumGenes.SLOWNESS_4 to listOf(EnumGenes.SLOWNESS),
            EnumGenes.SLOWNESS_6 to listOf(EnumGenes.SLOWNESS, EnumGenes.SLOWNESS_4)
        )

        val genesToRemove = mutableListOf<EnumGenes>()

        for (gene in potionGenes.toList()) {
            potionLevels[gene]?.let { redundantGenes ->
                genesToRemove.addAll(redundantGenes)
            }
        }

        potionGenes.removeAll(genesToRemove)

        for (gene in potionGenes) {
            val potion = gene.getPotion() ?: continue
            entity.addEffect(potion)
        }
    }

    private val recentlyMeated = mutableSetOf<LivingEntity>()
    private fun handleMeaty2(entity: LivingEntity) {

        val isAbleToMeat = recentlyMeated.add(entity)
        if (!isAbleToMeat) return
        ModScheduler.scheduleTaskInTicks(20 * 60 * 5) {
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
        ModScheduler.scheduleTaskInTicks(20 * 60 * 5) {
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

    private fun handleMobSight(entity: LivingEntity) {
        val searchArea = entity.boundingBox.inflate(128.0)
        val nearbyLivingEntities = entity.level.getEntities(entity, searchArea).filterIsInstance<Mob>()

        val glowingEffect = MobEffectInstance(MobEffects.GLOWING, 100, 0, false, false)

        nearbyLivingEntities.forEach {
            it.addEffect(glowingEffect)
        }
    }

}