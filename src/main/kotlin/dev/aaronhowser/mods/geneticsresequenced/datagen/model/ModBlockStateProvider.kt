package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.block.CoalGeneratorBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.*
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.*
import java.util.stream.Stream

class ModBlockStateProvider(
	output: PackOutput
) : ModelProvider(output, GeneticsResequenced.MOD_ID) {

	override fun registerModels(
		blockModels: BlockModelGenerators,
		itemModels: ItemModelGenerators
	) {
		antiFieldBlock(blockModels)
		bioluminescence(blockModels)
		webDefense(blockModels)

		coalGenerator(blockModels)
		frontFacingBlock(blockModels, ModBlocks.CELL_ANALYZER, "block/cell_analyzer_front")
		frontFacingBlock(blockModels, ModBlocks.DNA_EXTRACTOR, "block/dna_extractor_front")
		frontFacingBlock(blockModels, ModBlocks.DNA_DECRYPTOR, "block/dna_decryptor_front")
		frontFacingBlock(blockModels, ModBlocks.BLOOD_PURIFIER, "block/blood_purifier_front")
		frontFacingBlock(blockModels, ModBlocks.PLASMID_INFUSER, "block/plasmid_infuser_front")
		frontFacingBlock(blockModels, ModBlocks.PLASMID_INJECTOR, "block/plasmid_injector_front")
		frontFacingBlock(blockModels, ModBlocks.INCUBATOR, "block/incubator_front")
		frontFacingBlock(blockModels, ModBlocks.ADVANCED_INCUBATOR, "block/incubator_front")
	}

	override fun getKnownBlocks(): Stream<out Holder<Block>> = ModBlocks.BLOCK_REGISTRY.entries.stream()

	override fun getKnownItems(): Stream<out Holder<Item>> = Stream.empty()

	override fun getName(): String = "Block Model Definitions - ${GeneticsResequenced.MOD_ID}"

	private fun antiFieldBlock(blockModels: BlockModelGenerators) {
		val block = ModBlocks.ANTI_FIELD_BLOCK.get()
		val enabledModel = cubeAll(blockModels, "anti_field_block_enabled", "block/anti_field_on")
		val disabledModel = cubeAll(blockModels, "anti_field_block_disabled", "block/anti_field_off")

		blockModels.blockStateOutput.accept(
			MultiVariantGenerator.dispatch(block)
				.with(
					PropertyDispatch.initial(AntiFieldBlock.DISABLED)
						.select(false, BlockModelGenerators.plainVariant(enabledModel))
						.select(true, BlockModelGenerators.plainVariant(disabledModel))
				)
		)
	}

	private fun bioluminescence(blockModels: BlockModelGenerators) {
		val block = ModBlocks.BIOLUMINESCENCE_BLOCK.get()
		val modelLocation = ModelTemplate(
			Optional.of(ModelLocationUtils.getModelLocation(Blocks.AIR)),
			Optional.empty()
		).create(blockModelLocation("bioluminescence"), TextureMapping(), blockModels.modelOutput)

		blockModels.blockStateOutput.accept(
			BlockModelGenerators.createSimpleBlock(
				block,
				BlockModelGenerators.plainVariant(modelLocation)
			)
		)
	}

	private fun webDefense(blockModels: BlockModelGenerators) {
		val block = ModBlocks.WEB_DEFENSE_BLOCK.get()
		val modelLocation = ModelTemplate(
			Optional.of(ModelLocationUtils.getModelLocation(Blocks.COBWEB)),
			Optional.empty()
		).create(blockModelLocation("web_defense_block"), TextureMapping(), blockModels.modelOutput)

		blockModels.blockStateOutput.accept(
			BlockModelGenerators.createSimpleBlock(
				block,
				BlockModelGenerators.plainVariant(modelLocation)
			)
		)
	}

	private fun frontFacingBlock(
		blockModels: BlockModelGenerators,
		deferredBlock: DeferredBlock<out Block>,
		frontTexture: String
	) {
		blockModels.createHorizontallyRotatedBlock(
			deferredBlock.get(),
			machineTextures(frontTexture)
		)
	}

	private fun coalGenerator(blockModels: BlockModelGenerators) {
		val block = ModBlocks.COAL_GENERATOR.get()
		val offModel = machineModel(blockModels, "coal_generator_off", "block/coal_generator_front_off")
		val onModel = machineModel(blockModels, "coal_generator_on", "block/coal_generator_front_on")

		blockModels.blockStateOutput.accept(
			MultiVariantGenerator.dispatch(block)
				.with(
					PropertyDispatch.initial(CoalGeneratorBlock.BURNING)
						.select(false, BlockModelGenerators.plainVariant(offModel))
						.select(true, BlockModelGenerators.plainVariant(onModel))
				)
				.with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
		)
	}

	private fun cubeAll(
		blockModels: BlockModelGenerators,
		modelName: String,
		texturePath: String
	): Identifier {
		return ModelTemplates.CUBE_ALL.create(
			blockModelLocation(modelName),
			TextureMapping.cube(texture(texturePath)),
			blockModels.modelOutput
		)
	}

	private fun machineModel(
		blockModels: BlockModelGenerators,
		modelName: String,
		frontTexture: String
	): Identifier {
		return ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(
			blockModelLocation(modelName),
			machineTextureMapping(frontTexture),
			blockModels.modelOutput
		)
	}

	private fun machineTextures(frontTexture: String): TexturedModel.Provider =
		TexturedModel.ORIENTABLE.updateTexture { mapping ->
			mapping
				.put(TextureSlot.SIDE, texture("block/machine_side"))
				.put(TextureSlot.FRONT, texture(frontTexture))
				.put(TextureSlot.TOP, texture("block/machine_top"))
				.put(TextureSlot.BOTTOM, texture("block/machine_bottom"))
				.putForced(TextureSlot.PARTICLE, texture("block/machine_top"))
		}

	private fun machineTextureMapping(frontTexture: String): TextureMapping =
		TextureMapping()
			.put(TextureSlot.SIDE, texture("block/machine_side"))
			.put(TextureSlot.FRONT, texture(frontTexture))
			.put(TextureSlot.TOP, texture("block/machine_top"))
			.put(TextureSlot.BOTTOM, texture("block/machine_bottom"))
			.putForced(TextureSlot.PARTICLE, texture("block/machine_top"))

	private fun blockModelLocation(path: String): Identifier =
		GeneticsResequenced.modResource("block/$path")

	private fun texture(path: String): Material =
		Material(GeneticsResequenced.modResource(path))

}
