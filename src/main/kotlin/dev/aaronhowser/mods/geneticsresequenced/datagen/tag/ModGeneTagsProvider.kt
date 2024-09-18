package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModGeneTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : TagsProvider<Gene>(
    output,
    GeneRegistry.GENE_REGISTRY_KEY,
    lookupProvider,
    GeneticsResequenced.ID,
    existingFileHelper
) {

    companion object {
        private fun create(name: String): TagKey<Gene> {
            return TagKey.create(GeneRegistry.GENE_REGISTRY_KEY, OtherUtil.modResource(name))
        }

        val HIDDEN = create("hidden")
        val NEGATIVE = create("negative")
        val MUTATION = create("mutation")
    }

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(HIDDEN)
            .add(ModGenes.BASIC)

        this.tag(MUTATION)
            .add(
                ModGenes.CLAWS_TWO,
                ModGenes.EFFICIENCY_FOUR,
                ModGenes.FLIGHT,
                ModGenes.HASTE_TWO,
                ModGenes.MEATY_TWO,
                ModGenes.MORE_HEARTS_TWO,
                ModGenes.PHOTOSYNTHESIS,
                ModGenes.REGENERATION_FOUR,
                ModGenes.RESISTANCE_TWO,
                ModGenes.SPEED_FOUR,
                ModGenes.SPEED_TWO,
                ModGenes.STRENGTH_TWO
            )

        this.tag(NEGATIVE)
            .add(
                ModGenes.BAD_OMEN,
                ModGenes.BLINDNESS,
                ModGenes.CRINGE,
                ModGenes.CURSED,
                ModGenes.FLAMBE,
                ModGenes.HUNGER,
                ModGenes.INFESTED,
                ModGenes.LEVITATION,
                ModGenes.MINING_FATIGUE,
                ModGenes.NAUSEA,
                ModGenes.OOZING,
                ModGenes.POISON,
                ModGenes.POISON_FOUR,
                ModGenes.SLOWNESS,
                ModGenes.SLOWNESS_FOUR,
                ModGenes.SLOWNESS_SIX,
                ModGenes.WEAVING,
                ModGenes.WEAKNESS,
                ModGenes.WIND_CHARGED,
                ModGenes.WITHER,
                ModGenes.BLACK_DEATH,
                ModGenes.GREEN_DEATH,
                ModGenes.WHITE_DEATH,
                ModGenes.GRAY_DEATH,
                ModGenes.UN_UNDEATH
            )

    }
}