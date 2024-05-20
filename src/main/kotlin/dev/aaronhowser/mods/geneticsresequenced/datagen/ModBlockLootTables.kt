package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import net.minecraft.data.loot.BlockLoot
import net.minecraft.world.level.block.Block

class ModBlockLootTables : BlockLoot() {

    override fun addTables() {
        dropSelf(ModBlocks.COAL_GENERATOR.get())
        dropSelf(ModBlocks.CELL_ANALYZER.get())
        dropSelf(ModBlocks.DNA_EXTRACTOR.get())
        dropSelf(ModBlocks.DNA_DECRYPTOR.get())
        dropSelf(ModBlocks.BLOOD_PURIFIER.get())
        dropSelf(ModBlocks.PLASMID_INFUSER.get())
        dropSelf(ModBlocks.PLASMID_INJECTOR.get())
        dropSelf(ModBlocks.AIRBORNE_DISPERSAL_DEVICE.get())
        dropSelf(ModBlocks.CLONING_MACHINE.get())
        dropSelf(ModBlocks.INCUBATOR.get())
        dropSelf(ModBlocks.ANTIFIELD_BLOCK.get())

        add(
            ModBlocks.BIOLUMINESCENCE_BLOCK.get(),
            noDrop()
        )
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return ModBlocks.BLOCK_REGISTRY.entries.map { it.get() }.toMutableSet()
    }

}