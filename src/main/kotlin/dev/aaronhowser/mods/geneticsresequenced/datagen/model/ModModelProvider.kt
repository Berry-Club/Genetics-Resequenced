package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
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
		val baseTemplate = ModelTemplates.createItem(
			modLocation("syringe_base").toString(),
			TextureSlot.TEXTURE
		)

		val item = ModItems.SYRINGE.get()

//		val emptyUnused = ModelTemplates.FLAT_ITEM.create(
//			ModItems.SYRINGE.get(),
//			TextureMapping.singleSlot(
//				TextureSlot.TEXTURE,
//				Material(modLocation("glass_syringe_empty"), false)
//			),
//			itemModels.itemModelOutput
//		)

//		val emptyUnused = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item))

		val emptyUnused = ItemModelUtils.plainModel(
			ModelTemplates.FLAT_ITEM.create(
				modLocation("item/syringe_empty"),
				TextureMapping.layer0(
					Material(modLocation("glass_syringe_empty"))
				),
				itemModels.modelOutput
			)
		)

		val isUsing = ItemModelUtils.conditional(
			ItemModelUtils.hasComponent(ModDataComponents.SPECIFIC_ENTITY.get()),
			plainItemModel("syringe_full_flipped"),
			plainItemModel("syringe_flipped_empty")
		)

		val isNotUsing = ItemModelUtils.conditional(
			ItemModelUtils.hasComponent(ModDataComponents.SPECIFIC_ENTITY.get()),
			plainItemModel("syringe_full"),
			emptyUnused
		)

		itemModels.itemModelOutput.accept(
			ModItems.SYRINGE.get(),
			ItemModelUtils.conditional(
				ItemModelUtils.isUsingItem(),
				isUsing, isNotUsing
			)
		)
	}

	private fun plainItemModel(modelName: String) =
		ItemModelUtils.plainModel(modLocation("item/$modelName"))

}