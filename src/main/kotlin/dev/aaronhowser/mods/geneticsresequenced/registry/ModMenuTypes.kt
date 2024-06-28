package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.machine.blood_purifier.BloodPurifierMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.blood_purifier.BloodPurifierScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.cell_analyzer.CellAnalyzerMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.cell_analyzer.CellAnalyzerScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator.CoalGeneratorScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_decryptor.DnaDecryptorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_decryptor.DnaDecryptorScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_extractor.DnaExtractorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_extractor.DnaExtractorScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_extractor.PlasmidInfuserScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_injector.PlasmidInjectorScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator.IncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator.IncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_injector.PlasmidInjectorMenu
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModMenuTypes {

    val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
        DeferredRegister.create(BuiltInRegistries.MENU, GeneticsResequenced.ID)

    val COAL_GENERATOR: DeferredHolder<MenuType<*>, MenuType<CoalGeneratorMenu>> =
        MENU_TYPE_REGISTRY.register("coal_generator", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                CoalGeneratorMenu(id, inv, buf)
            }
        })

    val CELL_ANALYZER: DeferredHolder<MenuType<*>, MenuType<CellAnalyzerMenu>> =
        MENU_TYPE_REGISTRY.register("cell_analyzer", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                CellAnalyzerMenu(id, inv, buf)
            }
        })

    val DNA_EXTRACTOR: DeferredHolder<MenuType<*>, MenuType<DnaExtractorMenu>> =
        MENU_TYPE_REGISTRY.register("dna_extractor", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                DnaExtractorMenu(id, inv, buf)
            }
        })

    val DNA_DECRYPTOR: DeferredHolder<MenuType<*>, MenuType<DnaDecryptorMenu>> =
        MENU_TYPE_REGISTRY.register("dna_decryptor", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                DnaDecryptorMenu(id, inv, buf)
            }
        })

    val PLASMID_INFUSER: DeferredHolder<MenuType<*>, MenuType<PlasmidInfuserMenu>> =
        MENU_TYPE_REGISTRY.register("plasmid_infuser", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                PlasmidInfuserMenu(id, inv, buf)
            }
        })

    val PLASMID_INJECTOR: DeferredHolder<MenuType<*>, MenuType<PlasmidInjectorMenu>> =
        MENU_TYPE_REGISTRY.register("plasmid_injector", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                PlasmidInjectorMenu(id, inv, buf)
            }
        })

    val BLOOD_PURIFIER: DeferredHolder<MenuType<*>, MenuType<BloodPurifierMenu>> =
        MENU_TYPE_REGISTRY.register("blood_purifier", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                BloodPurifierMenu(id, inv, buf)
            }
        })

    val INCUBATOR: DeferredHolder<MenuType<*>, MenuType<IncubatorMenu>> =
        MENU_TYPE_REGISTRY.register("incubator", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                IncubatorMenu(id, inv, buf)
            }
        })

    val ADVANCED_INCUBATOR: DeferredHolder<MenuType<*>, MenuType<AdvancedIncubatorMenu>> =
        MENU_TYPE_REGISTRY.register("advanced_incubator", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                AdvancedIncubatorMenu(id, inv, buf)
            }
        })

    fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(COAL_GENERATOR.get(), ::CoalGeneratorScreen)
        event.register(CELL_ANALYZER.get(), ::CellAnalyzerScreen)
        event.register(DNA_EXTRACTOR.get(), ::DnaExtractorScreen)
        event.register(DNA_DECRYPTOR.get(), ::DnaDecryptorScreen)
        event.register(PLASMID_INFUSER.get(), ::PlasmidInfuserScreen)
        event.register(PLASMID_INJECTOR.get(), ::PlasmidInjectorScreen)
        event.register(BLOOD_PURIFIER.get(), ::BloodPurifierScreen)
        event.register(INCUBATOR.get(), ::IncubatorScreen)
        event.register(ADVANCED_INCUBATOR.get(), ::AdvancedIncubatorScreen)
    }

}