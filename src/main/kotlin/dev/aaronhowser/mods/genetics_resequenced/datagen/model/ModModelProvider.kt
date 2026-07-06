package dev.aaronhowser.mods.genetics_resequenced.datagen.model

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.genetics_resequenced.block.base.MachineBlock
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.model.*
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.*
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

		orientableMachine(ModBlocks.CELL_ANALYZER.get(), blockModels)
		orientableMachine(ModBlocks.DNA_EXTRACTOR.get(), blockModels)
		orientableMachine(ModBlocks.DNA_DECRYPTOR.get(), blockModels)
		orientableMachine(ModBlocks.BLOOD_PURIFIER.get(), blockModels)
		orientableMachine(ModBlocks.PLASMID_INFUSER.get(), blockModels)
		orientableMachine(ModBlocks.PLASMID_INJECTOR.get(), blockModels)
		orientableMachine(ModBlocks.INCUBATOR.get(), blockModels)
		orientableMachine(ModBlocks.ADVANCED_INCUBATOR.get(), blockModels)

		webDefense(blockModels)
	}

	private fun webDefense(blockModels: BlockModelGenerators) {
		val block = ModBlocks.WEB_DEFENSE_BLOCK.get()

		val modelLocation = ModelTemplate(
			Optional.of(ModelLocationUtils.getModelLocation(Blocks.COBWEB)),
			Optional.empty()
		).create(block, TextureMapping(), blockModels.modelOutput)

		blockModels.blockStateOutput.accept(
			BlockModelGenerators.createSimpleBlock(
				block,
				BlockModelGenerators.plainVariant(modelLocation)
			)
		)
	}

	private fun orientableMachine(block: MachineBlock, blockModels: BlockModelGenerators) {
		val baseTop = Material(modLocation("block/base/top"))
		val baseSide = Material(modLocation("block/base/side"))
		val baseBottom = Material(modLocation("block/base/bottom"))

		val front = TextureMapping.getBlockTexture(block)

		val model = TexturedModel.ORIENTABLE.get(block)
			.updateTextures {
				it.put(TextureSlot.TOP, baseTop)
				it.put(TextureSlot.BOTTOM, baseBottom)
				it.put(TextureSlot.SIDE, baseSide)
				it.put(TextureSlot.FRONT, front)
			}

		val variant = BlockModelGenerators.plainVariant(model.create(block, blockModels.modelOutput))

		blockModels.blockStateOutput.accept(
			BlockModelGenerators.createSimpleBlock(block, variant)
				.with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
		)
	}

	private fun antiField(blockModels: BlockModelGenerators) {
		val block = ModBlocks.ANTI_FIELD_BLOCK.get()

		val enabledMat = TextureMapping.getBlockTexture(block, "/on")
		val disabledMat = TextureMapping.getBlockTexture(block, "/off")

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
