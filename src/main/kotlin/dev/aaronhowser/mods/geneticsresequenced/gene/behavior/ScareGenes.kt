package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import kotlin.jvm.optionals.getOrNull

object ScareGenes {

    fun attachScareTask(entity: PathfinderMob) {
        val allGenes = GeneRegistry.getAllGeneHolders(entity.registryAccess())

        for (gene in allGenes) {
            if (gene.isDisabled) continue
            val cowardTag = gene.value().scaresEntitiesWithTag.getOrNull() ?: continue

            if (!entity.type.`is`(cowardTag)) continue

            entity.goalSelector.addGoal(
                1,
                AvoidEntityGoal(
                    entity,
                    LivingEntity::class.java,
                    { otherEntity: LivingEntity -> otherEntity.hasGene(gene) },
                    12.0f,
                    1.2,
                    1.6,
                    EntitySelector.NO_SPECTATORS::test
                )
            )
        }
    }

}