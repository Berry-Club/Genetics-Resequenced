package dev.aaronhowser.mods.geneticsresequenced.genebehaviors

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
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

        val scaredOf = when (entity) {
            is Creeper -> Gene.SCARE_CREEPERS
            is Zombie -> Gene.SCARE_ZOMBIES
            is Skeleton -> Gene.SCARE_SKELETONS
            is Spider -> Gene.SCARE_SPIDERS

            else -> return
        }

        for (livingEntityClass: Class<out LivingEntity> in classesToRunFrom) {
            entity.goalSelector.addGoal(
                1,
                AvoidEntityGoal(
                    entity,
                    livingEntityClass,
                    { otherEntity: LivingEntity -> otherEntity.getGenes()?.hasGene(scaredOf) == true },
                    6.0f,
                    1.0,
                    1.2,
                    EntitySelector.NO_SPECTATORS::test
                )
            )
        }

    }

}