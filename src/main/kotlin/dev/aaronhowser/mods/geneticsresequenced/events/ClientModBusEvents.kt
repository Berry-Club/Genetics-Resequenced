package dev.aaronhowser.mods.geneticsresequenced.events

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.controls.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.entities.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.entities.client.FriendlySlimeRenderer
import dev.aaronhowser.mods.geneticsresequenced.screens.*
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
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
        registerScreens()
        registerRenderers()
    }

    private fun registerScreens() {
        // Funky little function whose meaning is impossible to ascertain
        fun <M : AbstractContainerMenu, U> registerScreen(
            pType: MenuType<out M>,
            pFactory: ScreenConstructor<M, U>
        ) where U : Screen?, U : MenuAccess<M> {
            MenuScreens.register(pType, pFactory)
        }

        registerScreen(ModMenuTypes.CELL_ANALYZER.get(), ::CellAnalyzerScreen)
        registerScreen(ModMenuTypes.COAL_GENERATOR.get(), ::CoalGeneratorScreen)
        registerScreen(ModMenuTypes.DNA_EXTRACTOR.get(), ::DnaExtractorScreen)
        registerScreen(ModMenuTypes.DNA_DECRYPTOR.get(), ::DnaDecryptorScreen)
        registerScreen(ModMenuTypes.PLASMID_INFUSER.get(), ::PlasmidInfuserScreen)
    }

    private fun registerRenderers() {

        EntityRenderers.register(ModEntityTypes.FRIENDLY_SLIME.get(), ::FriendlySlimeRenderer)

    }

}