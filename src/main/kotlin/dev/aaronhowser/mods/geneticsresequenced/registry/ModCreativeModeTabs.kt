package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {

    val TABS_REGISTRY: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, GeneticsResequenced.ID)

    val MOD_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> = TABS_REGISTRY.register("creative_tab", Supplier {
        CreativeModeTab.builder()
            .title(ModLanguageProvider.Items.CREATIVE_TAB.toComponent())
            .icon { ModItems.SYRINGE.toStack() }
            .displayItems { displayContext: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                val regularItems =
                    ModItems.ITEM_REGISTRY.entries - ModItems.DNA_HELIX - ModItems.PLASMID

                val itemsToDisplay = buildList {
                    addAll(regularItems.map { (it as DeferredItem).toStack() })
                    addAll(DnaHelixItem.getAllHelices(displayContext.holders))
                    addAll(PlasmidItem.getAllPlasmids(displayContext.holders))
                }

                output.acceptAll(itemsToDisplay)
            }
            .build()
    })

}