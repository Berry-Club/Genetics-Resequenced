package dev.aaronhowser.mods.geneticsresequenced.screens

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModMenuTypes {

    val REGISTRY: DeferredRegister<MenuType<*>> =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, GeneticsResequenced.ID)


    val CELL_ANALYZER: RegistryObject<MenuType<CellAnalyzerMenu>> =
        REGISTRY.register("cell_analyzer_menu") {
            IForgeMenuType.create(IContainerFactory { id, inv, buf ->
                CellAnalyzerMenu(id, inv, buf)
            })
        }
}