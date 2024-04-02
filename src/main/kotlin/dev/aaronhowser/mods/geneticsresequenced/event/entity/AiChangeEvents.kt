package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import net.minecraft.world.entity.monster.*
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.EntityJoinLevelEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object AiChangeEvents {

    @SubscribeEvent
    fun onEntitySpawn(event: EntityJoinLevelEvent) {

        val entity = event.entity

        if (entity is Monster) {
            attachScareTask(entity)
        }
    }

    private fun attachScareTask(entity: PathfinderMob) {

        val scaredOf = when (entity) {
            is Creeper -> EnumGenes.SCARE_CREEPERS
            is Zombie -> EnumGenes.SCARE_ZOMBIES
            is Skeleton -> EnumGenes.SCARE_SKELETONS
            is Spider -> EnumGenes.SCARE_SPIDERS

            else -> return
        }

        entity.goalSelector.addGoal(
            3,
            AvoidEntityGoal(
                entity,
                Player::class.java,
                { otherEntity: LivingEntity -> otherEntity.getGenes()?.hasGene(scaredOf) == true },
                6.0f,
                1.0,
                1.2,
                EntitySelector.NO_SPECTATORS::test
            )
        )

    }

}