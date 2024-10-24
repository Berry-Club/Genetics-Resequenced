package dev.aaronhowser.mods.geneticsresequenced.datagen.gene

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.PackType
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.JsonCodecProvider
import java.util.concurrent.CompletableFuture

class ModEntityGenesProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : JsonCodecProvider<EntityGenes.EntityGenesData>(
    output,
    PackOutput.Target.DATA_PACK,
    EntityGenes.DIRECTORY,
    PackType.SERVER_DATA,
    EntityGenes.EntityGenesData.CODEC,
    lookupProvider,
    GeneticsResequenced.ID,
    existingFileHelper
) {

    private fun addEntityGenes(
        entityType: EntityType<*>,
        vararg geneWeights: Pair<ResourceKey<Gene>, Int>
    ) {
        val entityRk = ResourceKey.create(Registries.ENTITY_TYPE, EntityType.getKey(entityType))

        this.unconditional(
            entityRk.location(),
            EntityGenes.EntityGenesData(
                entityRk,
                geneWeights.toMap()
            )
        )
    }

    override fun gather() {
        this.addEntityGenes(EntityType.ALLAY, ModGenes.BASIC to 3, ModGenes.ITEM_MAGNET to 5)

    }
}