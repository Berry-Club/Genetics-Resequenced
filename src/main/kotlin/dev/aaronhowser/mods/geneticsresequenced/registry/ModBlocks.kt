package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.*
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, GeneticsResequenced.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister<Block> = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister<Item> = ModItems.ITEM_REGISTRY

	val ANTI_FIELD_BLOCK: RegistryObject<AntiFieldBlock> =
		registerBlock("anti_field_block", ::AntiFieldBlock)
	val BIOLUMINESCENCE_BLOCK: RegistryObject<BioluminescenceBlock> =
		registerBlockWithoutItem("bioluminescence", ::BioluminescenceBlock)
	val WEB_DEFENSE_BLOCK: RegistryObject<WebDefenseBlock> =
		registerBlockWithoutItem("web_defense_block", ::WebDefenseBlock)

	val COAL_GENERATOR: RegistryObject<CoalGeneratorBlock> =
		registerBlock("coal_generator", ::CoalGeneratorBlock)
	val CELL_ANALYZER: RegistryObject<CellAnalyzerBlock> =
		registerBlock("cell_analyzer", ::CellAnalyzerBlock)
	val DNA_EXTRACTOR: RegistryObject<DnaExtractorBlock> =
		registerBlock("dna_extractor", ::DnaExtractorBlock)
	val DNA_DECRYPTOR: RegistryObject<DnaDecryptorBlock> =
		registerBlock("dna_decryptor", ::DnaDecryptorBlock)
	val BLOOD_PURIFIER: RegistryObject<BloodPurifierBlock> =
		registerBlock("blood_purifier", ::BloodPurifierBlock)
	val PLASMID_INFUSER: RegistryObject<PlasmidInfuserBlock> =
		registerBlock("plasmid_infuser", ::PlasmidInfuserBlock)
	val PLASMID_INJECTOR: RegistryObject<PlasmidInjectorBlock> =
		registerBlock("plasmid_injector", ::PlasmidInjectorBlock)
	val INCUBATOR: RegistryObject<IncubatorBlock> =
		registerBlock("incubator", ::IncubatorBlock)
	val ADVANCED_INCUBATOR: RegistryObject<Block> =
		registerBlock("advanced_incubator", ::AdvancedIncubatorBlock)

}