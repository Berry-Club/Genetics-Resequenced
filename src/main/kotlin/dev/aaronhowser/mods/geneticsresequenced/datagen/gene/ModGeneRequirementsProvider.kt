package dev.aaronhowser.mods.geneticsresequenced.datagen.gene

import com.mojang.serialization.JsonOps
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.GeneRequirements
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.common.data.JsonCodecProvider
import java.util.function.BiConsumer

class ModGeneRequirementsProvider(
	output: PackOutput,
	existingFileHelper: ExistingFileHelper
) : JsonCodecProvider<GeneRequirements>(
	output,
	existingFileHelper,
	GeneticsResequenced.MOD_ID,
	JsonOps.INSTANCE,
	PackType.SERVER_DATA,
	"geneticsresequenced/gene_requirements",
	GeneRequirements.CODEC,
	mapOf()
) {

	override fun gather(consumer: BiConsumer<ResourceLocation, GeneRequirements>) {

		fun addRequirements(
			geneRk: ResourceKey<Gene>,
			vararg requirements: ResourceKey<Gene>
		) {
			consumer.accept(
				geneRk.location(),
				GeneRequirements(
					geneRk,
					requirements.toList()
				)
			)
		}

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
		addRequirements(ModGenes.BOUNTIFUL_TWO, ModGenes.BOUNTIFUL)
	}
}