package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.block.*
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(GeneticsResequenced.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	val ANTI_FIELD_BLOCK: DeferredBlock<AntiFieldBlock> =
		registerBlock("anti_field_block", ::AntiFieldBlock)
	val BIOLUMINESCENCE_BLOCK: DeferredBlock<BioluminescenceBlock> =
		registerBlockWithoutItem("bioluminescence", ::BioluminescenceBlock)
	val WEB_DEFENSE_BLOCK: DeferredBlock<WebDefenseBlock> =
		registerBlockWithoutItem("web_defense_block", ::WebDefenseBlock)

	val COAL_GENERATOR: DeferredBlock<CoalGeneratorBlock> =
		registerBlock("coal_generator", ::CoalGeneratorBlock)
	val CELL_ANALYZER: DeferredBlock<CellAnalyzerBlock> =
		registerBlock("cell_analyzer", ::CellAnalyzerBlock)
	val DNA_EXTRACTOR: DeferredBlock<DnaExtractorBlock> =
		registerBlock("dna_extractor", ::DnaExtractorBlock)
	val DNA_DECRYPTOR: DeferredBlock<DnaDecryptorBlock> =
		registerBlock("dna_decryptor", ::DnaDecryptorBlock)
	val BLOOD_PURIFIER: DeferredBlock<BloodPurifierBlock> =
		registerBlock("blood_purifier", ::BloodPurifierBlock)
	val PLASMID_INFUSER: DeferredBlock<PlasmidInfuserBlock> =
		registerBlock("plasmid_infuser", ::PlasmidInfuserBlock)
	val PLASMID_INJECTOR: DeferredBlock<PlasmidInjectorBlock> =
		registerBlock("plasmid_injector", ::PlasmidInjectorBlock)
	val INCUBATOR: DeferredBlock<IncubatorBlock> =
		registerBlock("incubator", ::IncubatorBlock)
	val ADVANCED_INCUBATOR: DeferredBlock<Block> =
		registerBlock("advanced_incubator", ::AdvancedIncubatorBlock)

}