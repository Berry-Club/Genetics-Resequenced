package dev.aaronhowser.mods.geneticsresequenced.events.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.ScareGenes
import net.minecraft.world.entity.monster.Monster
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
            ScareGenes.attachScareTask(entity)
        }
    }
}