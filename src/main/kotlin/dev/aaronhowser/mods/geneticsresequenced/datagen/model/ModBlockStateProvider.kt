package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.block.CoalGeneratorBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.registries.DeferredBlock
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
		frontFacingBlock(blockModels, ModBlocks.CELL_ANALYZER, "cell_analyzer", "block/cell_analyzer_front")
		frontFacingBlock(blockModels, ModBlocks.DNA_EXTRACTOR, "dna_extractor", "block/dna_extractor_front")
		frontFacingBlock(blockModels, ModBlocks.DNA_DECRYPTOR, "dna_decryptor", "block/dna_decryptor_front")
		frontFacingBlock(blockModels, ModBlocks.BLOOD_PURIFIER, "blood_purifier", "block/blood_purifier_front")
		frontFacingBlock(blockModels, ModBlocks.PLASMID_INFUSER, "plasmid_infuser", "block/plasmid_infuser_front")
		frontFacingBlock(blockModels, ModBlocks.PLASMID_INJECTOR, "plasmid_injector", "block/plasmid_injector_front")
		frontFacingBlock(blockModels, ModBlocks.INCUBATOR, "incubator", "block/incubator_front")
		frontFacingBlock(blockModels, ModBlocks.ADVANCED_INCUBATOR, "advanced_incubator", "block/incubator_front")
	}

	override fun getKnownBlocks(): Stream<out Holder<Block>> =
		Stream.of(
			ModBlocks.ANTI_FIELD_BLOCK.get().builtInRegistryHolder(),
			ModBlocks.BIOLUMINESCENCE_BLOCK.get().builtInRegistryHolder(),
			ModBlocks.WEB_DEFENSE_BLOCK.get().builtInRegistryHolder(),
			ModBlocks.COAL_GENERATOR.get().builtInRegistryHolder(),
			ModBlocks.CELL_ANALYZER.get().builtInRegistryHolder(),
			ModBlocks.DNA_EXTRACTOR.get().builtInRegistryHolder(),
			ModBlocks.DNA_DECRYPTOR.get().builtInRegistryHolder(),
			ModBlocks.BLOOD_PURIFIER.get().builtInRegistryHolder(),
			ModBlocks.PLASMID_INFUSER.get().builtInRegistryHolder(),
			ModBlocks.PLASMID_INJECTOR.get().builtInRegistryHolder(),
			ModBlocks.INCUBATOR.get().builtInRegistryHolder(),
			ModBlocks.ADVANCED_INCUBATOR.get().builtInRegistryHolder()
		)

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
		val modelLocation = blockModelLocation("bioluminescence")

		blockModels.modelOutput.accept(modelLocation) {
			JsonObject().apply {
				addProperty("parent", "minecraft:block/air")
			}
		}

		blockModels.blockStateOutput.accept(
			BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelLocation))
		)
	}

	private fun webDefense(blockModels: BlockModelGenerators) {
		val block = ModBlocks.WEB_DEFENSE_BLOCK.get()
		val modelLocation = blockModelLocation("web_defense_block")

		blockModels.modelOutput.accept(modelLocation) {
			JsonObject().apply {
				addProperty("parent", ModelLocationUtils.getModelLocation(Blocks.COBWEB).toString())
				addProperty("render_type", "minecraft:cutout")
			}
		}

		blockModels.blockStateOutput.accept(
			BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelLocation))
		)
	}

	private fun frontFacingBlock(
		blockModels: BlockModelGenerators,
		deferredBlock: DeferredBlock<out Block>,
		name: String,
		frontTexture: String
	) {
		val block = deferredBlock.get()
		val model = machineModel(blockModels, name, frontTexture)

		blockModels.blockStateOutput.accept(
			MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model))
				.with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
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
		val modelLocation = blockModelLocation(modelName)

		blockModels.modelOutput.accept(modelLocation) {
			JsonObject().apply {
				addProperty("parent", "minecraft:block/cube_all")
				add(
					"textures",
					JsonObject().apply {
						addProperty("all", GeneticsResequenced.modResource(texturePath).toString())
					}
				)
			}
		}

		return modelLocation
	}

	private fun machineModel(
		blockModels: BlockModelGenerators,
		modelName: String,
		frontTexture: String
	): Identifier {
		val modelLocation = blockModelLocation(modelName)

		blockModels.modelOutput.accept(modelLocation) {
			JsonObject().apply {
				addProperty("parent", "minecraft:block/cube")
				add(
					"textures",
					JsonObject().apply {
						addProperty("down", GeneticsResequenced.modResource("block/machine_bottom").toString())
						addProperty("up", GeneticsResequenced.modResource("block/machine_top").toString())
						addProperty("north", GeneticsResequenced.modResource(frontTexture).toString())
						addProperty("south", GeneticsResequenced.modResource("block/machine_side").toString())
						addProperty("east", GeneticsResequenced.modResource("block/machine_side").toString())
						addProperty("west", GeneticsResequenced.modResource("block/machine_side").toString())
						addProperty("particle", GeneticsResequenced.modResource("block/machine_top").toString())
					}
				)
			}
		}

		return modelLocation
	}

	private fun blockModelLocation(path: String): Identifier =
		GeneticsResequenced.modResource("block/$path")

}
