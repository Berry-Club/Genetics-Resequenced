package dev.aaronhowser.mods.geneticsresequenced.datagen

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

class ModBlockLootTables : Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

//    fun addTables() {
//        dropSelf(ModBlocks.COAL_GENERATOR.get())
//        dropSelf(ModBlocks.CELL_ANALYZER.get())
//        dropSelf(ModBlocks.DNA_EXTRACTOR.get())
//        dropSelf(ModBlocks.DNA_DECRYPTOR.get())
//        dropSelf(ModBlocks.BLOOD_PURIFIER.get())
//        dropSelf(ModBlocks.PLASMID_INFUSER.get())
//        dropSelf(ModBlocks.PLASMID_INJECTOR.get())
//        dropSelf(ModBlocks.AIRBORNE_DISPERSAL_DEVICE.get())
//        dropSelf(ModBlocks.CLONING_MACHINE.get())
//        dropSelf(ModBlocks.INCUBATOR.get())
//        dropSelf(ModBlocks.ANTI_FIELD_BLOCK.get())
//
//        add(
//            ModBlocks.BIOLUMINESCENCE_BLOCK.get(),
//            noDrop()
//        )
//    }


    private lateinit var output: BiConsumer<ResourceLocation, LootTable.Builder>

    private fun toTableLocation(rl: ResourceLocation): ResourceLocation {
        return ResourceLocation(rl.namespace, "blocks/${rl.path}")
    }

    override fun accept(output: BiConsumer<ResourceLocation, LootTable.Builder>) {
        this.output = output


    }

    private fun singleItem(itemLike: ItemLike): LootPool.Builder {
        return LootPool
            .lootPool()
            .setRolls(ConstantValue.exactly(1f))
            .add(LootItem.lootTableItem(itemLike))
    }

    private fun registerSelfDropping(block: Block, vararg pools: LootPool.Builder) {

        @Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
        val withSelf: Array<LootPool.Builder> = Arrays.copyOf(pools, pools.size + 1)
        withSelf[withSelf.size - 1] = singleItem(block)

    }

}