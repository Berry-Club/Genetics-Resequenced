package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.entity.monster.Skeleton
import net.minecraft.world.entity.monster.Spider
import net.minecraft.world.entity.monster.Zombie

object ScareGenes {

    fun attachScareTask(entity: PathfinderMob) {
        val gene = when (entity) {
            is Creeper -> ModGenes.SCARE_CREEPERS.get()
            is Zombie -> ModGenes.SCARE_ZOMBIES.get()
            is Skeleton -> ModGenes.SCARE_SKELETONS.get()
            is Spider -> ModGenes.SCARE_SPIDERS.get()

            else -> return
        }

        if (!gene.isActive) return

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