package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockEntityTypeRegistry
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.block_entity.*
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModBlockEntityTypes : AaronBlockEntityTypeRegistry() {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GeneticsResequenced.MOD_ID)

	override fun getBlockEntityRegistry(): DeferredRegister<BlockEntityType<*>> = BLOCK_ENTITY_REGISTRY

	val COAL_GENERATOR: RegistryObject<BlockEntityType<CoalGeneratorBlockEntity>> =
		register("coal_generator", ::CoalGeneratorBlockEntity, ModBlocks.COAL_GENERATOR)

	val CELL_ANALYZER: RegistryObject<BlockEntityType<CellAnalyzerBlockEntity>> =
		register("cell_analyzer", ::CellAnalyzerBlockEntity, ModBlocks.CELL_ANALYZER)

	val DNA_EXTRACTOR: RegistryObject<BlockEntityType<DnaExtractorBlockEntity>> =
		register("dna_extractor", ::DnaExtractorBlockEntity, ModBlocks.DNA_EXTRACTOR)

	val DNA_DECRYPTOR: RegistryObject<BlockEntityType<DnaDecryptorBlockEntity>> =
		register("dna_decryptor", ::DnaDecryptorBlockEntity, ModBlocks.DNA_DECRYPTOR)

	val PLASMID_INFUSER: RegistryObject<BlockEntityType<PlasmidInfuserBlockEntity>> =
		register("plasmid_infuser", ::PlasmidInfuserBlockEntity, ModBlocks.PLASMID_INFUSER)

	val PLASMID_INJECTOR: RegistryObject<BlockEntityType<PlasmidInjectorBlockEntity>> =
		register("plasmid_injector", ::PlasmidInjectorBlockEntity, ModBlocks.PLASMID_INJECTOR)

	val BLOOD_PURIFIER: RegistryObject<BlockEntityType<BloodPurifierBlockEntity>> =
		register("blood_purifier", ::BloodPurifierBlockEntity, ModBlocks.BLOOD_PURIFIER)

	val INCUBATOR: RegistryObject<BlockEntityType<IncubatorBlockEntity>> =
		register("incubator", ::IncubatorBlockEntity, ModBlocks.INCUBATOR)

	val ADVANCED_INCUBATOR: RegistryObject<BlockEntityType<AdvancedIncubatorBlockEntity>> =
		register("advanced_incubator", ::AdvancedIncubatorBlockEntity, ModBlocks.ADVANCED_INCUBATOR)

}