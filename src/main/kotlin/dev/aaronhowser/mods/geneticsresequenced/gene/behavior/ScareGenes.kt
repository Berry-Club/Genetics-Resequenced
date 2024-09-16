package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal

object ScareGenes {

    private val geneTagMap = mapOf(
        ModEntityTypeTagsProvider.AVOIDS_SCARE_CREEPER_GENE to ModGenes.SCARE_CREEPERS,
        ModEntityTypeTagsProvider.AVOIDS_SCARE_ZOMBIE_GENE to ModGenes.SCARE_ZOMBIES,
        ModEntityTypeTagsProvider.AVOIDS_SCARE_SKELETON_GENE to ModGenes.SCARE_SKELETONS,
        ModEntityTypeTagsProvider.AVOIDS_SCARE_SPIDER_GENE to ModGenes.SCARE_SPIDERS
    )

    fun attachScareTask(entity: PathfinderMob) {

        for ((tag, geneRk) in geneTagMap) {
            val geneHolder = geneRk.getHolder(entity.registryAccess()) ?: continue

            if (!geneHolder.value().isActive) continue
            if (!entity.type.`is`(tag)) continue

            entity.goalSelector.addGoal(
                1,
                AvoidEntityGoal(
                    entity,
                    LivingEntity::class.java,
                    { otherEntity: LivingEntity -> otherEntity.hasGene(geneRk) },
                    12.0f,
                    1.2,
                    1.6,
                    EntitySelector.NO_SPECTATORS::test
                )
            )
        }
    }

}