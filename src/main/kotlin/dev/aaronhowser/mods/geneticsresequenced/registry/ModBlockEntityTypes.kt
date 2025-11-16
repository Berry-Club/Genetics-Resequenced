package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.block_entity.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBlockEntityTypes {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, GeneticsResequenced.ID)

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

	private fun <T : BlockEntity> register(
		name: String,
		builder: BlockEntityType.BlockEntitySupplier<out T>,
		vararg validBlocks: DeferredBlock<*>
	): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
		return BLOCK_ENTITY_REGISTRY.register(name, Supplier {
			BlockEntityType.Builder.of(
				builder,
				*validBlocks.map(DeferredBlock<*>::get).toTypedArray()
			).build(null)
		})
	}

}