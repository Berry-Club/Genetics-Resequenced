package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.block.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import net.minecraft.world.entity.LivingEntity
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

}