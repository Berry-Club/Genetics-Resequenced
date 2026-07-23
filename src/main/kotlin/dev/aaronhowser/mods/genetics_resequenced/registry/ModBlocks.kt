package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.genetics_resequenced.block.BioluminescenceBlock
import dev.aaronhowser.mods.genetics_resequenced.block.CoalGeneratorBlock
import dev.aaronhowser.mods.genetics_resequenced.block.WebDefenseBlock
import dev.aaronhowser.mods.genetics_resequenced.block.base.MachineBlock
import dev.aaronhowser.mods.genetics_resequenced.block_entity.*
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(GeneticsResequenced.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	val ANTI_FIELD_BLOCK: DeferredBlock<AntiFieldBlock> =
		registerBlock("anti_field_block", ::AntiFieldBlock, AntiFieldBlock::defaultProperties)
	val BIOLUMINESCENCE_BLOCK: DeferredBlock<BioluminescenceBlock> =
		registerBlockWithoutItem("bioluminescence", ::BioluminescenceBlock, BioluminescenceBlock::defaultProperties)
	val WEB_DEFENSE_BLOCK: DeferredBlock<WebDefenseBlock> =
		registerBlockWithoutItem("web_defense_block", ::WebDefenseBlock, WebDefenseBlock::defaultProperties)

	val COAL_GENERATOR: DeferredBlock<CoalGeneratorBlock> =
		registerBlock("coal_generator", ::CoalGeneratorBlock, MachineBlock::defaultProperties)
	val CELL_ANALYZER: DeferredBlock<out MachineBlock> =
		registerMachineBlock("cell_analyzer", ::CellAnalyzerBlockEntity)
	val DNA_EXTRACTOR: DeferredBlock<out MachineBlock> =
		registerMachineBlock("dna_extractor", ::DnaExtractorBlockEntity)
	val DNA_DECRYPTOR: DeferredBlock<out MachineBlock> =
		registerMachineBlock("dna_decryptor", ::DnaDecryptorBlockEntity)
	val BLOOD_PURIFIER: DeferredBlock<out MachineBlock> =
		registerMachineBlock("blood_purifier", ::BloodPurifierBlockEntity)
	val PLASMID_INFUSER: DeferredBlock<out MachineBlock> =
		registerMachineBlock("plasmid_infuser", ::PlasmidInfuserBlockEntity)
	val PLASMID_INJECTOR: DeferredBlock<out MachineBlock> =
		registerMachineBlock("plasmid_injector", ::PlasmidInjectorBlockEntity)
	val INCUBATOR: DeferredBlock<out MachineBlock> =
		registerMachineBlock("incubator", ::IncubatorBlockEntity)
	val ADVANCED_INCUBATOR: DeferredBlock<out MachineBlock> =
		registerMachineBlock("advanced_incubator", ::AdvancedIncubatorBlockEntity)

	private fun registerMachineBlock(
		name: String,
		factory: (BlockPos, BlockState) -> BlockEntity
	): DeferredBlock<out MachineBlock> {
		return registerBlock(
			name,
			{ properties -> MachineBlock(factory, properties) },
			MachineBlock::defaultProperties
		)
	}

}
