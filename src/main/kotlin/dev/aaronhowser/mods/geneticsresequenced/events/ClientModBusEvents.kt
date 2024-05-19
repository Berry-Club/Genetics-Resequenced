package dev.aaronhowser.mods.geneticsresequenced.events

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.blood_purifier.BloodPurifierScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.cell_analyzer.CellAnalyzerScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator.CoalGeneratorScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor.DnaDecryptorScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_extractor.DnaExtractorScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator.IncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector.PlasmidInjectorScreen
import dev.aaronhowser.mods.geneticsresequenced.controls.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.entities.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.entities.client.FriendlySlimeRenderer
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.screens.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.client.renderer.item.ItemPropertyFunction
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ModelEvent
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
        registerEntityRenderers()
    }

    private fun registerScreens() {
        MenuScreens.register(ModMenuTypes.CELL_ANALYZER.get(), ::CellAnalyzerScreen)
        MenuScreens.register(ModMenuTypes.COAL_GENERATOR.get(), ::CoalGeneratorScreen)
        MenuScreens.register(ModMenuTypes.DNA_EXTRACTOR.get(), ::DnaExtractorScreen)
        MenuScreens.register(ModMenuTypes.DNA_DECRYPTOR.get(), ::DnaDecryptorScreen)
        MenuScreens.register(ModMenuTypes.PLASMID_INFUSER.get(), ::PlasmidInfuserScreen)
        MenuScreens.register(ModMenuTypes.PLASMID_INJECTOR.get(), ::PlasmidInjectorScreen)
        MenuScreens.register(ModMenuTypes.BLOOD_PURIFIER.get(), ::BloodPurifierScreen)
        MenuScreens.register(ModMenuTypes.INCUBATOR.get(), ::IncubatorScreen)
    }

    private fun registerEntityRenderers() {
        EntityRenderers.register(ModEntityTypes.SUPPORT_SLIME.get(), ::FriendlySlimeRenderer)
    }

    @SubscribeEvent
    fun onModelRegistry(event: ModelEvent.RegisterAdditional) {

        ItemProperties.register(
            ModItems.SYRINGE,
            OtherUtil.modResource("full"),
            ItemPropertyFunction { stack, _, _, _ ->
                if (SyringeItem.hasBlood(stack)) 1f else 0f
            }
        )

        ItemProperties.register(
            ModItems.SYRINGE,
            OtherUtil.modResource("injecting"),
            ItemPropertyFunction { stack, _, entity, _ ->
                if (SyringeItem.isBeingUsed(stack, entity)) 1f else 0f
            }
        )

    }

}