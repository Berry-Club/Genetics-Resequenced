package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = GeneticsResequenced.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object DataGenerators {

    /**
     * To actually generate this, run in gradle
     *  Tasks/forgegradle runs/runData
     */
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator = event.generator
        val helper = event.existingFileHelper

        generator.addProvider(true, ModRecipeProvider(generator))
//        generator.addProvider(true, ModBlockLootTables())
        generator.addProvider(true, ModBlockStateProvider(generator, helper))
        generator.addProvider(true, ModItemModelProvider(generator, helper))

    }

}