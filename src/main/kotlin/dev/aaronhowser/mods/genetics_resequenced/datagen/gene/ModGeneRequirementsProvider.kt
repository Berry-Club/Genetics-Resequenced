package dev.aaronhowser.mods.genetics_resequenced.datagen.gene

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.data.GeneRequirements
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.common.data.JsonCodecProvider
import java.util.concurrent.CompletableFuture

class ModGeneRequirementsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : JsonCodecProvider<GeneRequirements>(
	output,
	PackOutput.Target.DATA_PACK,
	"genetics_resequenced/gene_requirements",
	GeneRequirements.CODEC,
	lookupProvider,
	GeneticsResequenced.MOD_ID
) {

	private fun addRequirements(
		geneRk: ResourceKey<Gene>,
		vararg requirements: ResourceKey<Gene>
	) {
		unconditional(
			geneRk.identifier(),
			GeneRequirements(
				geneRk,
				requirements.toList()
			)
		)
	}

	override fun gather() {
		addRequirements(ModGenes.CLAWS_TWO, ModGenes.CLAWS)
		addRequirements(ModGenes.EFFICIENCY_FOUR, ModGenes.EFFICIENCY)
		addRequirements(ModGenes.FLIGHT, ModGenes.TELEPORT, ModGenes.STEP_ASSIST, ModGenes.NO_FALL_DAMAGE)
		addRequirements(ModGenes.HASTE_TWO, ModGenes.HASTE)
		addRequirements(ModGenes.MEATY_TWO, ModGenes.MEATY)
		addRequirements(ModGenes.MORE_HEARTS_TWO, ModGenes.MORE_HEARTS)
		addRequirements(ModGenes.PHOTOSYNTHESIS, ModGenes.EAT_GRASS, ModGenes.THORNS)
		addRequirements(ModGenes.REGENERATION_FOUR, ModGenes.REGENERATION)
		addRequirements(ModGenes.RESISTANCE_TWO, ModGenes.RESISTANCE)
		addRequirements(ModGenes.SPEED_FOUR, ModGenes.SPEED, ModGenes.SPEED_TWO)
		addRequirements(ModGenes.SPEED_TWO, ModGenes.SPEED)
		addRequirements(ModGenes.STRENGTH_TWO, ModGenes.STRENGTH)
		addRequirements(ModGenes.LAVA_PROOF, ModGenes.FIRE_PROOF)
		addRequirements(ModGenes.WEB_WALKER, ModGenes.WEAVING)
		addRequirements(ModGenes.WEB_DEFENSE, ModGenes.WEAVING)
		addRequirements(ModGenes.BOUNTIFUL_TWO, ModGenes.BOUNTIFUL)
	}
}
