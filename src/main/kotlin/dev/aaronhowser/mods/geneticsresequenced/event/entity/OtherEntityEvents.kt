package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneContainer
import dev.aaronhowser.mods.geneticsresequenced.registry.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherEntityEvents {

    @SubscribeEvent
    fun onSpawn(event: EntityJoinLevelEvent) {
        val entity = event.entity

        fun log(string: String) {
            if (!entity.level().isClientSide) println(string)
        }

        val geneContainer = ModAttachmentTypes.GENE_CONTAINER.get()

        log("Entity joined level: ${entity.type}")

        val hasData = entity.hasData(geneContainer)

        log("Entity has data: $hasData")

        val data = entity.getData(geneContainer)

        if (data.genes.contains(GeneRegistry.testGene)) {
            log("Already had test gene!")
            log("Entity data: $data")
            return
        }

        log("Entity did not have test gene!")
        log("Entity data: $data")

        val newGenes = data.genes + GeneRegistry.testGene
        val newContainer = GeneContainer(newGenes)

        entity.setData(geneContainer, newContainer)

        log("Entity data after: ${entity.getData(geneContainer)}")
    }

}