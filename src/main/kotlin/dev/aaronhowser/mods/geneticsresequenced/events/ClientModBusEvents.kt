package dev.aaronhowser.mods.geneticsresequenced.events

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.controls.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.screens.CellAnalyzerScreen
import dev.aaronhowser.mods.geneticsresequenced.screens.CoalGeneratorScreen
import dev.aaronhowser.mods.geneticsresequenced.screens.ModMenuTypes
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = [Dist.CLIENT]
)
object ClientModBusEvents {

    @SubscribeEvent
    fun onKeyRegister(event: RegisterKeyMappingsEvent) {
        event.register(ModKeyMappings.DRAGONS_BREATH)
        event.register(ModKeyMappings.TELEPORT)
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {

        MenuScreens.register(ModMenuTypes.CELL_ANALYZER.get()) { menu, inventory, title ->
            CellAnalyzerScreen(menu, inventory, title)
        }
        MenuScreens.register(ModMenuTypes.COAL_GENERATOR.get()) { menu, inventory, title ->
            CoalGeneratorScreen(menu, inventory, title)
        }

    }

}