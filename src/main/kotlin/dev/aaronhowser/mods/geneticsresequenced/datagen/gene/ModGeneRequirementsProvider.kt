package dev.aaronhowser.mods.geneticsresequenced.datagen.gene

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.GeneRequirements
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.PackType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.JsonCodecProvider
import org.antlr.v4.runtime.misc.MultiMap
import org.jetbrains.annotations.ApiStatus.Internal
import java.util.concurrent.CompletableFuture

class ModGeneRequirementsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : JsonCodecProvider<GeneRequirements.GeneRequirementsData>(
    output,
    PackOutput.Target.DATA_PACK,
    GeneRequirements.DIRECTORY,
    PackType.SERVER_DATA,
    GeneRequirements.GeneRequirementsData.CODEC,
    lookupProvider,
    GeneticsResequenced.ID,
    existingFileHelper
) {

    companion object {
        @Internal
        val DEFAULT: MultiMap<ResourceKey<Gene>, ResourceKey<Gene>> = MultiMap()
    }

    private fun addRequirements(
        geneRk: ResourceKey<Gene>,
        vararg requirements: ResourceKey<Gene>
    ) {
        DEFAULT.getOrPut(geneRk) { mutableListOf() }.addAll(requirements)

        this.unconditional(
            geneRk.location(),
            GeneRequirements.GeneRequirementsData(
                geneRk,
                requirements.toList()
            )
        )
    }

    override fun gather() {
        this.addRequirements(ModGenes.CLAWS_TWO, ModGenes.CLAWS)
        this.addRequirements(ModGenes.EFFICIENCY_FOUR, ModGenes.EFFICIENCY)
        this.addRequirements(ModGenes.FLIGHT, ModGenes.TELEPORT, ModGenes.STEP_ASSIST, ModGenes.NO_FALL_DAMAGE)
        this.addRequirements(ModGenes.HASTE_TWO, ModGenes.HASTE)
        this.addRequirements(ModGenes.MEATY_TWO, ModGenes.MEATY)
        this.addRequirements(ModGenes.MORE_HEARTS_TWO, ModGenes.MORE_HEARTS)
        this.addRequirements(ModGenes.PHOTOSYNTHESIS, ModGenes.EAT_GRASS, ModGenes.THORNS)
        this.addRequirements(ModGenes.REGENERATION_FOUR, ModGenes.REGENERATION)
        this.addRequirements(ModGenes.RESISTANCE_TWO, ModGenes.RESISTANCE)
        this.addRequirements(ModGenes.SPEED_FOUR, ModGenes.SPEED)
        this.addRequirements(ModGenes.SPEED_TWO, ModGenes.SPEED)
        this.addRequirements(ModGenes.STRENGTH_TWO, ModGenes.STRENGTH)
    }
}