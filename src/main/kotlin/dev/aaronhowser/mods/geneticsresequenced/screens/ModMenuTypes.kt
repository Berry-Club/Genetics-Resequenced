package dev.aaronhowser.mods.geneticsresequenced.screens

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.blood_purifier.BloodPurifierMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.blood_purifier.BloodPurifierScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.cell_analyzer.CellAnalyzerMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.cell_analyzer.CellAnalyzerScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator.CoalGeneratorScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor.DnaDecryptorMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor.DnaDecryptorScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_extractor.DnaExtractorMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_extractor.DnaExtractorScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator.IncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator.IncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserScreen
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector.PlasmidInjectorMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector.PlasmidInjectorScreen
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModMenuTypes {

    val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, GeneticsResequenced.ID)

    val CELL_ANALYZER: RegistryObject<MenuType<CellAnalyzerMenu>> =
        MENU_TYPE_REGISTRY.register("cell_analyzer_menu") {
            IForgeMenuType.create(IContainerFactory { id, inv, buf ->
                CellAnalyzerMenu(id, inv, buf)
            })
        }

    val COAL_GENERATOR: RegistryObject<MenuType<CoalGeneratorMenu>> =
        MENU_TYPE_REGISTRY.register("coal_generator_menu") {
            IForgeMenuType.create(IContainerFactory { id, inv, buf ->
                CoalGeneratorMenu(id, inv, buf)
            })
        }

    val DNA_EXTRACTOR: RegistryObject<MenuType<DnaExtractorMenu>> = MENU_TYPE_REGISTRY.register("dna_extractor_menu") {
        IForgeMenuType.create(IContainerFactory { id, inv, buf ->
            DnaExtractorMenu(id, inv, buf)
        })
    }

    val DNA_DECRYPTOR: RegistryObject<MenuType<DnaDecryptorMenu>> = MENU_TYPE_REGISTRY.register("dna_decryptor_menu") {
        IForgeMenuType.create(IContainerFactory { id, inv, buf ->
            DnaDecryptorMenu(id, inv, buf)
        })
    }

    val PLASMID_INFUSER: RegistryObject<MenuType<PlasmidInfuserMenu>> =
        MENU_TYPE_REGISTRY.register("plasma_infuser_menu") {
            IForgeMenuType.create(IContainerFactory { id, inv, buf ->
                PlasmidInfuserMenu(id, inv, buf)
            })
        }

    val PLASMID_INJECTOR: RegistryObject<MenuType<PlasmidInjectorMenu>> =
        MENU_TYPE_REGISTRY.register("plasmid_injector_menu") {
            IForgeMenuType.create(IContainerFactory { id, inv, buf ->
                PlasmidInjectorMenu(id, inv, buf)
            })
        }

    val BLOOD_PURIFIER: RegistryObject<MenuType<BloodPurifierMenu>> =
        MENU_TYPE_REGISTRY.register("blood_purifier_menu") {
            IForgeMenuType.create(IContainerFactory { id, inv, buf ->
                BloodPurifierMenu(id, inv, buf)
            })
        }

    val INCUBATOR: RegistryObject<MenuType<IncubatorMenu>> =
        MENU_TYPE_REGISTRY.register("incubator") {
            IForgeMenuType.create(IContainerFactory { id, inv, buf ->
                IncubatorMenu(id, inv, buf)
            })
        }

    fun registerScreens() {
        MenuScreens.register(CELL_ANALYZER.get(), ::CellAnalyzerScreen)
        MenuScreens.register(COAL_GENERATOR.get(), ::CoalGeneratorScreen)
        MenuScreens.register(DNA_EXTRACTOR.get(), ::DnaExtractorScreen)
        MenuScreens.register(DNA_DECRYPTOR.get(), ::DnaDecryptorScreen)
        MenuScreens.register(PLASMID_INFUSER.get(), ::PlasmidInfuserScreen)
        MenuScreens.register(PLASMID_INJECTOR.get(), ::PlasmidInjectorScreen)
        MenuScreens.register(BLOOD_PURIFIER.get(), ::BloodPurifierScreen)
        MenuScreens.register(INCUBATOR.get(), ::IncubatorScreen)
    }

}