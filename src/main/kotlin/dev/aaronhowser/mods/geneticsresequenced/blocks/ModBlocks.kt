package dev.aaronhowser.mods.geneticsresequenced.blocks

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
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

    val REGISTRY: DeferredRegister<Block> =
        DeferredRegister.create(ForgeRegistries.BLOCKS, GeneticsResequenced.ID)

    val BIOLUMINESCENCE_BLOCK by register("bioluminescence") { BioluminescenceBlock }
    val CELL_ANALYZER by register("cell_analyzer") { CellAnalyzerBlock }
    val COAL_GENERATOR by register("coal_generator") { CoalGeneratorBlock }
    val DNA_DECRYPTOR by register("dna_decryptor") { DnaDecryptorBlock }

    private fun register(
        name: String,
        itemProperties: Item.Properties = defaultItemProperties,
        supplier: () -> Block
    ): ObjectHolderDelegate<Block> {
        val block = REGISTRY.registerObject(name, supplier)

        ModItems.REGISTRY.register(name) {
            BlockItem(
                block.get(),
                itemProperties
            )
        }

        return block
    }

}