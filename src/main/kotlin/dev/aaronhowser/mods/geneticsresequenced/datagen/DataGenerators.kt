package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = GeneticsResequenced.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object DataGenerators {

    /**
     * When Patchouli is enabled in the gradle, runData crashes.
     *
     * Fix this by commenting out the patchouli part in the build.gradle.
     *
     * That's really gross but I cannot fucking be bothered to figure out a better way to do it.
     */
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator = event.generator
        val helper = event.existingFileHelper

        generator.addProvider(true, ModRecipeProvider(generator))
        generator.addProvider(true, ModItemModelProvider(generator, helper))

    }

}