package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.stream.Stream

class ModModelProvider(
	output: PackOutput
) : ModelProvider(output, GeneticsResequenced.MOD_ID) {

	//	override fun getKnownBlocks(): Stream<out Holder<Block>> = ModBlocks.BLOCK_REGISTRY.entries.stream()
	override fun getKnownBlocks(): Stream<out Holder<Block>> = Stream.empty()
	override fun getKnownItems(): Stream<out Holder<Item>> = ModItems.ITEM_REGISTRY.entries.stream().filter { it.get() !is BlockItem && it != ModItems.FRIENDLY_SLIME_SPAWN_EGG }

	override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
		makeItemModels(itemModels)
	}

	private fun makeItemModels(itemModels: ItemModelGenerators) {
		val flatItems = listOf(
			ModItems.ORGANIC_MATTER, ModItems.CELL, ModItems.GMO_CELL,
			ModItems.DNA_HELIX, ModItems.PLASMID, ModItems.ANTI_PLASMID,
			ModItems.OVERCLOCKER, ModItems.ANTI_FIELD_ORB, ModItems.DRAGON_HEALTH_CRYSTAL,
			ModItems.GENE_CHECKER
		)

		for (item in flatItems) {
			itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM)
		}

		val flatHandheldItems = listOf(
			ModItems.SCRAPER, ModItems.METAL_SYRINGE
		)

		for (item in flatHandheldItems) {
			itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_HANDHELD_ITEM)
		}

		syringe(itemModels)
	}

	private fun syringe(itemModels: ItemModelGenerators) {
		val template = ModelTemplates.createItem(
			modLocation("syringe").toString(),
			TextureSlot.TEXTURE
		)

		val item = ModItems.SYRINGE.get()

		val emptyUnused = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item))
		val emptyUsed = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, "_empty_flipped", template))
		val fullUnused = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, "_full", template))
		val fullUsed = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, "_full_flipped", template))

		val used = ItemModelUtils.conditional(
			ItemModelUtils.hasComponent(ModDataComponents.SPECIFIC_ENTITY.get()),
			fullUsed, emptyUsed
		)

		val unused = ItemModelUtils.conditional(
			ItemModelUtils.hasComponent(ModDataComponents.SPECIFIC_ENTITY.get()),
			fullUnused, emptyUnused
		)

		itemModels.itemModelOutput.accept(
			item,
			ItemModelUtils.conditional(
				ItemModelUtils.isUsingItem(),
				used, unused
			)
		)
	}

}