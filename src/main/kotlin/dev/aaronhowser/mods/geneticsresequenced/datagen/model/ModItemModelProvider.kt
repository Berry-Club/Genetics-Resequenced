package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.stream.Stream

class ModItemModelProvider(
	output: PackOutput
) : ModelProvider(output, GeneticsResequenced.MOD_ID) {

	override fun registerModels(
		blockModels: BlockModelGenerators,
		itemModels: ItemModelGenerators
	) {
		for ((itemLike, modelName) in SIMPLE_ITEMS) {
			itemModels.itemModelOutput.accept(itemLike.asItem(), plainItemModel(modelName))
		}

		itemModels.itemModelOutput.accept(ModItems.SYRINGE.get(), syringe())
		itemModels.itemModelOutput.accept(ModItems.METAL_SYRINGE.get(), metalSyringe())
	}

	override fun getKnownBlocks(): Stream<out Holder<Block>> = Stream.empty()

	private fun syringe() =
		ItemModelUtils.conditional(
			ItemModelUtils.isUsingItem(),
			ItemModelUtils.conditional(
				ItemModelUtils.hasComponent(ModDataComponents.SPECIFIC_ENTITY.get()),
				plainItemModel("syringe_full_flipped"),
				plainItemModel("syringe_flipped_empty")
			),
			ItemModelUtils.conditional(
				ItemModelUtils.hasComponent(ModDataComponents.SPECIFIC_ENTITY.get()),
				plainItemModel("syringe_full"),
				plainItemModel("syringe")
			)
		)

	private fun metalSyringe() =
		ItemModelUtils.conditional(
			ItemModelUtils.hasComponent(ModDataComponents.SPECIFIC_ENTITY.get()),
			plainItemModel("metal_syringe_full"),
			plainItemModel("metal_syringe")
		)

	private fun plainItemModel(modelName: String) =
		ItemModelUtils.plainModel(GeneticsResequenced.modResource("item/$modelName"))

	private companion object {
		val SIMPLE_ITEMS: List<Pair<ItemLike, String>> = listOf(
			ModItems.SCRAPER to "scraper",
			ModItems.GENE_CHECKER to "gene_checker",
			ModItems.ORGANIC_MATTER to "organic_matter",
			ModItems.CELL to "cell",
			ModItems.GMO_CELL to "gmo_cell",
			ModItems.DNA_HELIX to "dna_helix",
			ModItems.PLASMID to "plasmid",
			ModItems.ANTI_PLASMID to "anti_plasmid",
			ModItems.OVERCLOCKER to "overclocker",
			ModItems.ANTI_FIELD_ORB to "anti_field_orb",
			ModItems.DRAGON_HEALTH_CRYSTAL to "dragon_health_crystal",
			ModItems.FRIENDLY_SLIME_SPAWN_EGG to "support_slime_spawn_egg",
			ModBlocks.ANTI_FIELD_BLOCK to "anti_field_block",
			ModBlocks.COAL_GENERATOR to "coal_generator",
			ModBlocks.CELL_ANALYZER to "cell_analyzer",
			ModBlocks.DNA_EXTRACTOR to "dna_extractor",
			ModBlocks.DNA_DECRYPTOR to "dna_decryptor",
			ModBlocks.BLOOD_PURIFIER to "blood_purifier",
			ModBlocks.PLASMID_INFUSER to "plasmid_infuser",
			ModBlocks.PLASMID_INJECTOR to "plasmid_injector",
			ModBlocks.INCUBATOR to "incubator",
			ModBlocks.ADVANCED_INCUBATOR to "advanced_incubator"
		)
	}

}