package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.block.ModBlocks
import net.minecraft.world.entity.LivingEntity

object TickGenes {

    fun handleBioluminescence(entity: LivingEntity) {
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