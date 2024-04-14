package dev.aaronhowser.mods.geneticsresequenced.screens

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModMenuTypes {

    val MENU_TYPE_RECIPE: DeferredRegister<MenuType<*>> =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, GeneticsResequenced.ID)


    val CELL_ANALYZER: RegistryObject<MenuType<CellAnalyzerMenu>> =
        MENU_TYPE_RECIPE.register("cell_analyzer_menu") {
            IForgeMenuType.create(IContainerFactory { id, inv, buf ->
                CellAnalyzerMenu(id, inv, buf)
            })
        }

    val COAL_GENERATOR: RegistryObject<MenuType<CoalGeneratorMenu>> = MENU_TYPE_RECIPE.register("coal_generator_menu") {
        IForgeMenuType.create(IContainerFactory { id, inv, buf ->
            CoalGeneratorMenu(id, inv, buf)
        })
    }

    val DNA_DECRYPTOR: RegistryObject<MenuType<DnaDecryptorMenu>> = MENU_TYPE_RECIPE.register("dna_decryptor_menu") {
        IForgeMenuType.create(IContainerFactory { id, inv, buf ->
            DnaDecryptorMenu(id, inv, buf)
        })
    }
}