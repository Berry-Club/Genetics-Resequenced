package dev.aaronhowser.mods.geneticsresequenced.blocks

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.blood_purifier.BloodPurifierBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.cell_analyzer.CellAnalyzerBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator.CoalGeneratorBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor.DnaDecryptorBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_extractor.DnaExtractorBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator.IncubatorBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator_advanced.AdvancedIncubatorBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector.PlasmidInjectorBlock
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModBlocks {

    private val defaultItemProperties = Item.Properties().tab(ModItems.MOD_TAB)

    val BLOCK_REGISTRY: DeferredRegister<Block> =
        DeferredRegister.create(ForgeRegistries.BLOCKS, GeneticsResequenced.ID)

    val BIOLUMINESCENCE_BLOCK: RegistryObject<Block> =
        register("bioluminescence") { BioluminescenceBlock() }
    val COAL_GENERATOR: RegistryObject<Block> =
        register("coal_generator") { CoalGeneratorBlock() }
    val CELL_ANALYZER: RegistryObject<Block> =
        register("cell_analyzer") { CellAnalyzerBlock() }
    val DNA_EXTRACTOR: RegistryObject<Block> =
        register("dna_extractor") { DnaExtractorBlock() }
    val DNA_DECRYPTOR: RegistryObject<Block> =
        register("dna_decryptor") { DnaDecryptorBlock() }
    val BLOOD_PURIFIER: RegistryObject<Block> =
        register("blood_purifier") { BloodPurifierBlock() }
    val PLASMID_INFUSER: RegistryObject<Block> =
        register("plasmid_infuser") { PlasmidInfuserBlock() }
    val PLASMID_INJECTOR: RegistryObject<Block> =
        register("plasmid_injector") { PlasmidInjectorBlock() }
    val INCUBATOR: RegistryObject<Block> =
        register("incubator") { IncubatorBlock() }
    val ADVANCED_INCUBATOR: RegistryObject<Block> =
        register("advanced_incubator") { AdvancedIncubatorBlock() }
    val ANTIFIELD_BLOCK: RegistryObject<Block> =
        register("anti_field_block") { AntiFieldBlock() }

    private fun register(
        name: String,
        itemProperties: Item.Properties = defaultItemProperties,
        supplier: () -> Block
    ): RegistryObject<Block> {
        val block = BLOCK_REGISTRY.register(name, supplier)

        ModItems.ITEM_REGISTRY.register(name) {
            BlockItem(
                block.get(),
                itemProperties
            )
        }

        return block
    }

}