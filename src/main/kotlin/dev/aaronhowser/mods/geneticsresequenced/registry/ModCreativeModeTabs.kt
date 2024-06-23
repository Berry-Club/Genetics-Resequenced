package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {

    val TABS_REGISTRY: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, GeneticsResequenced.ID)

    val MOD_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> = TABS_REGISTRY.register("example", Supplier {
        CreativeModeTab.builder()
            .title(Component.translatable(ModLanguageProvider.Items.CREATIVE_TAB))
            .icon { ItemStack(Items.STICK) }
            .displayItems { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->

                ModItems.ITEM_REGISTRY.entries.forEach { item ->
                    output.accept(item.get())
                }

            }
            .build()
    })

}