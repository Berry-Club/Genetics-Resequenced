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

        val geneContainer = ModAttachmentTypes.GENE_CONTAINER.get()

        println("Entity joined level: ${entity.type}")

        val hasData = entity.hasData(geneContainer)

        println("Entity has data: $hasData")

        val data = entity.getData(geneContainer)

        println("Entity data: $data")

        val testGene = GeneRegistry.testGene
        val newGenes = data.genes + GeneRegistry.testGene
        val newContainer = GeneContainer(newGenes)

        entity.setData(geneContainer, newContainer)

        println("Entity data after: ${entity.getData(geneContainer)}")
    }

}