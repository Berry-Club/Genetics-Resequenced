package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockEntityTypeRegistry
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block_entity.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlockEntityTypes : AaronBlockEntityTypeRegistry() {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, GeneticsResequenced.MOD_ID)

	override fun getBlockEntityRegistry(): DeferredRegister<BlockEntityType<*>> = BLOCK_ENTITY_REGISTRY

	val COAL_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<CoalGeneratorBlockEntity>> =
		register("coal_generator", ::CoalGeneratorBlockEntity, ModBlocks.COAL_GENERATOR)

	val CELL_ANALYZER: DeferredHolder<BlockEntityType<*>, BlockEntityType<CellAnalyzerBlockEntity>> =
		register("cell_analyzer", ::CellAnalyzerBlockEntity, ModBlocks.CELL_ANALYZER)

	val DNA_EXTRACTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DnaExtractorBlockEntity>> =
		register("dna_extractor", ::DnaExtractorBlockEntity, ModBlocks.DNA_EXTRACTOR)

	val DNA_DECRYPTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DnaDecryptorBlockEntity>> =
		register("dna_decryptor", ::DnaDecryptorBlockEntity, ModBlocks.DNA_DECRYPTOR)

	val PLASMID_INFUSER: DeferredHolder<BlockEntityType<*>, BlockEntityType<PlasmidInfuserBlockEntity>> =
		register("plasmid_infuser", ::PlasmidInfuserBlockEntity, ModBlocks.PLASMID_INFUSER)

	val PLASMID_INJECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<PlasmidInjectorBlockEntity>> =
		register("plasmid_injector", ::PlasmidInjectorBlockEntity, ModBlocks.PLASMID_INJECTOR)

	val BLOOD_PURIFIER: DeferredHolder<BlockEntityType<*>, BlockEntityType<BloodPurifierBlockEntity>> =
		register("blood_purifier", ::BloodPurifierBlockEntity, ModBlocks.BLOOD_PURIFIER)

	val INCUBATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<IncubatorBlockEntity>> =
		register("incubator", ::IncubatorBlockEntity, ModBlocks.INCUBATOR)

	val ADVANCED_INCUBATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<AdvancedIncubatorBlockEntity>> =
		register("advanced_incubator", ::AdvancedIncubatorBlockEntity, ModBlocks.ADVANCED_INCUBATOR)

}