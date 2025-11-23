package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.*
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(GeneticsResequenced.ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	val BIOLUMINESCENCE_BLOCK: DeferredBlock<BioluminescenceBlock> =
		registerBlock("bioluminescence", ::BioluminescenceBlock)
	val ANTI_FIELD_BLOCK: DeferredBlock<AntiFieldBlock> =
		registerBlock("anti_field_block", ::AntiFieldBlock)
	val WEB_DEFENSE_BLOCK: DeferredBlock<WebDefenseBlock> =
		registerBlock("web_defense_block", ::WebDefenseBlock)

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