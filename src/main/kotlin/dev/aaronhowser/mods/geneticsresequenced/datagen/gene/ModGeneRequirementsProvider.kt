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
	GeneticsResequenced.MOD_ID,
	existingFileHelper
) {

	private fun addRequirements(
		geneRk: ResourceKey<Gene>,
		vararg requirements: ResourceKey<Gene>
	) {
		this.unconditional(
			geneRk.location(),
			GeneRequirements.GeneRequirementsData(
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