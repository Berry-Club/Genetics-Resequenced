package dev.aaronhowser.mods.geneticsresequenced.blocks

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_extractor.DnaExtractorBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.*
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.cell_analyzer.CellAnalyzerBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator.CoalGeneratorBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor.DnaDecryptorBlock
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserBlock
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

object ModBlocks {

    private val defaultItemProperties = Item.Properties().tab(ModItems.MOD_TAB)

    val BLOCK_REGISTRY: DeferredRegister<Block> =
        DeferredRegister.create(ForgeRegistries.BLOCKS, GeneticsResequenced.ID)

    val BIOLUMINESCENCE_BLOCK by register("bioluminescence") { BioluminescenceBlock }
    val COAL_GENERATOR by register("coal_generator") { CoalGeneratorBlock }
    val CELL_ANALYZER by register("cell_analyzer") { CellAnalyzerBlock }
    val DNA_EXTRACTOR by register("dna_extractor") { DnaExtractorBlock }
    val DNA_DECRYPTOR by register("dna_decryptor") { DnaDecryptorBlock }
    val BLOOD_PURIFIER by register("blood_purifier") { BloodPurifierBlock }
    val PLASMID_INFUSER by register("plasmid_infuser") { PlasmidInfuserBlock }
    val PLASMID_INJECTOR by register("plasmid_injector") { PlasmidInjectorBlock }
    val AIR_DISPERSAL by register("air_dispersal") { AirDispersalBlock }
    val CLONING_MACHINE by register("cloning_machine") { CloningMachineBlock }
    val INCUBATOR by register("incubator") { IncubatorBlock }
    val ANTIFIELD_BLOCK by register("antifield_block") { AntiFieldBlock() }

    private fun register(
        name: String,
        itemProperties: Item.Properties = defaultItemProperties,
        supplier: () -> Block
    ): ObjectHolderDelegate<Block> {
        val block = BLOCK_REGISTRY.registerObject(name, supplier)

        ModItems.ITEM_REGISTRY.register(name) {
            BlockItem(
                block.get(),
                itemProperties
            )
        }

        return block
    }

}