package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.machines.blood_purifier.BloodPurifierMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machines.blood_purifier.BloodPurifierScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machines.cell_analyzer.CellAnalyzerMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machines.cell_analyzer.CellAnalyzerScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator.CoalGeneratorScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machines.dna_decryptor.DnaDecryptorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machines.dna_decryptor.DnaDecryptorScreen
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

    val DNA_DECRYPTOR: DeferredHolder<MenuType<*>, MenuType<DnaDecryptorMenu>> =
        MENU_TYPE_REGISTRY.register("dna_decryptor", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                DnaDecryptorMenu(id, inv, buf)
            }
        })

    val BLOOD_PURIFIER: DeferredHolder<MenuType<*>, MenuType<BloodPurifierMenu>> =
        MENU_TYPE_REGISTRY.register("blood_purifier", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                BloodPurifierMenu(id, inv, buf)
            }
        })

    fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(COAL_GENERATOR.get(), ::CoalGeneratorScreen)
        event.register(CELL_ANALYZER.get(), ::CellAnalyzerScreen)
        event.register(DNA_DECRYPTOR.get(), ::DnaDecryptorScreen)
        event.register(BLOOD_PURIFIER.get(), ::BloodPurifierScreen)
    }

}