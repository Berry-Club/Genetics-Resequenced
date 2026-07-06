package dev.aaronhowser.mods.genetics_resequenced.datagen.tag

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.KeyTagProvider
import net.minecraft.tags.TagKey
import java.util.concurrent.CompletableFuture

class ModGeneTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : KeyTagProvider<Gene>(
	output,
	ModGenes.GENE_REGISTRY_KEY,
	lookupProvider,
	GeneticsResequenced.MOD_ID
) {

	companion object {
		private fun create(name: String): TagKey<Gene> {
			return TagKey.create(ModGenes.GENE_REGISTRY_KEY, GeneticsResequenced.modId(name))
		}

		val HELIX_ONLY = create("helix_only")
		val NEGATIVE = create("negative")
		val MUTATION = create("mutation")
		val DISABLED = create("disabled")
	}

	override fun addTags(pProvider: HolderLookup.Provider) {

		tag(HELIX_ONLY)
			.add(ModGenes.BASIC)

		tag(MUTATION)
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
				ModGenes.STRENGTH_TWO,
				ModGenes.LAVA_PROOF
			)

		tag(NEGATIVE)
			.add(
				ModGenes.BLINDNESS,
				ModGenes.CRINGE,
				ModGenes.CURSED,
				ModGenes.FLAMBE,
				ModGenes.HUNGER,
				ModGenes.INFESTED,
				ModGenes.LEVITATION,
				ModGenes.MINING_FATIGUE,
				ModGenes.NAUSEA,
				ModGenes.POISON,
				ModGenes.POISON_FOUR,
				ModGenes.SLOWNESS,
				ModGenes.SLOWNESS_FOUR,
				ModGenes.SLOWNESS_SIX,
				ModGenes.WEAKNESS,
				ModGenes.WITHER,
				ModGenes.BLACK_DEATH,
				ModGenes.GREEN_DEATH,
				ModGenes.WHITE_DEATH,
				ModGenes.GRAY_DEATH,
				ModGenes.UN_UNDEATH,
			)

		tag(DISABLED)
	}
}
