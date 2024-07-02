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
import net.minecraft.world.entity.player.Player

object ScareGenes {

    // Making this a public mutable list so other mods (or even KubeJS etc maybe I think) can add other classes to include
    @Suppress("MemberVisibilityCanBePrivate")
    val classesToRunFrom: MutableList<Class<out LivingEntity>> = mutableListOf(
        Player::class.java
    )

    fun attachScareTask(entity: PathfinderMob) {

        val gene = when (entity) {
            is Creeper -> ModGenes.SCARE_CREEPERS.get()
            is Zombie -> ModGenes.SCARE_ZOMBIES.get()
            is Skeleton -> ModGenes.SCARE_SKELETONS.get()
            is Spider -> ModGenes.SCARE_SPIDERS.get()

            else -> return
        }

        if (!gene.isActive) return

        for (livingEntityClass: Class<out LivingEntity> in classesToRunFrom) {
            entity.goalSelector.addGoal(
                1,
                AvoidEntityGoal(
                    entity,
                    livingEntityClass,
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