package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.BaseModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
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
    ModGenes.GENE_REGISTRY_KEY,
    lookupProvider,
    GeneticsResequenced.ID,
    existingFileHelper
) {

    companion object {
        private fun create(name: String): TagKey<Gene> {
            return TagKey.create(ModGenes.GENE_REGISTRY_KEY, OtherUtil.modResource(name))
        }

        val HIDDEN = create("hidden")
        val NEGATIVE = create("negative")
        val MUTATION = create("mutation")
        val DISABLED = create("disabled")
    }

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(HIDDEN)
            .add(BaseModGenes.BASIC)

        this.tag(MUTATION)
            .add(
                BaseModGenes.CLAWS_TWO,
                BaseModGenes.EFFICIENCY_FOUR,
                BaseModGenes.FLIGHT,
                BaseModGenes.HASTE_TWO,
                BaseModGenes.MEATY_TWO,
                BaseModGenes.MORE_HEARTS_TWO,
                BaseModGenes.PHOTOSYNTHESIS,
                BaseModGenes.REGENERATION_FOUR,
                BaseModGenes.RESISTANCE_TWO,
                BaseModGenes.SPEED_FOUR,
                BaseModGenes.SPEED_TWO,
                BaseModGenes.STRENGTH_TWO
            )

        this.tag(NEGATIVE)
            .add(
                BaseModGenes.BAD_OMEN,
                BaseModGenes.BLINDNESS,
                BaseModGenes.CRINGE,
                BaseModGenes.CURSED,
                BaseModGenes.FLAMBE,
                BaseModGenes.HUNGER,
                BaseModGenes.INFESTED,
                BaseModGenes.LEVITATION,
                BaseModGenes.MINING_FATIGUE,
                BaseModGenes.NAUSEA,
                BaseModGenes.OOZING,
                BaseModGenes.POISON,
                BaseModGenes.POISON_FOUR,
                BaseModGenes.SLOWNESS,
                BaseModGenes.SLOWNESS_FOUR,
                BaseModGenes.SLOWNESS_SIX,
                BaseModGenes.WEAVING,
                BaseModGenes.WEAKNESS,
                BaseModGenes.WIND_CHARGED,
                BaseModGenes.WITHER,
                BaseModGenes.BLACK_DEATH,
                BaseModGenes.GREEN_DEATH,
                BaseModGenes.WHITE_DEATH,
                BaseModGenes.GRAY_DEATH,
                BaseModGenes.UN_UNDEATH
            )

        this.tag(DISABLED)

    }
}