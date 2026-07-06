package dev.aaronhowser.mods.genetics_resequenced.datagen.model

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.model.*
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
		makeBlockModels(blockModels)
	}

	private fun makeBlockModels(blockModels: BlockModelGenerators) {
		antiField(blockModels)
	}

	private fun antiField(blockModels: BlockModelGenerators) {
		val block = ModBlocks.ANTI_FIELD_BLOCK.get()

		val enabledMat = TextureMapping.getBlockTexture(block, "_on")
		val disabledMat = TextureMapping.getBlockTexture(block, "_off")

		val enabled = BlockModelGenerators.plainVariant(
			TexturedModel.CUBE.get(block)
				.updateTextures { it.put(TextureSlot.ALL, enabledMat) }
				.createWithSuffix(block, "_on", blockModels.modelOutput)
		)

		val disabled = BlockModelGenerators.plainVariant(
			TexturedModel.CUBE.get(block)
				.updateTextures { it.put(TextureSlot.ALL, disabledMat) }
				.createWithSuffix(block, "_off", blockModels.modelOutput)
		)

		blockModels.blockStateOutput.accept(
			MultiVariantGenerator.dispatch(block)
				.with(
					BlockModelGenerators.createBooleanModelDispatch(
						AntiFieldBlock.POWERED,
						enabled, disabled
					)
				)
		)

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
			ModItems.SCRAPER
		)

		for (item in flatHandheldItems) {
			itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_HANDHELD_ITEM)
		}

		syringe(itemModels)
		metalSyringe(itemModels)
	}

	private fun syringe(itemModels: ItemModelGenerators) {
		val item = ModItems.SYRINGE.get()

		val emptyUnused = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, ModelTemplates.FLAT_ITEM))
		val emptyUsed = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, "_empty_flipped", ModelTemplates.FLAT_ITEM))
		val fullUnused = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, "_full", ModelTemplates.FLAT_ITEM))
		val fullUsed = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, "_full_flipped", ModelTemplates.FLAT_ITEM))

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

	private fun metalSyringe(itemModels: ItemModelGenerators) {
		val item = ModItems.METAL_SYRINGE.get()

		val empty = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, ModelTemplates.FLAT_ITEM))
		val full = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, "_full", ModelTemplates.FLAT_ITEM))

		itemModels.itemModelOutput.accept(
			item,
			ItemModelUtils.conditional(
				ItemModelUtils.hasComponent(ModDataComponents.SPECIFIC_ENTITY.get()),
				full, empty
			)
		)
	}

}
