package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {

    val TABS_REGISTRY: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, GeneticsResequenced.ID)

    //TODO: This sucks?? Can it be better??
    val MOD_TAB = TABS_REGISTRY.register("example", Supplier {
        CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.literal("Test"))
            .icon { ItemStack(Items.STICK) }
            .displayItems { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->

                for (item: DeferredHolder<Item, out Item> in ModItems.ITEM_REGISTRY.entries) {
                    output.accept(item.get())
                }

            }
            .build()
    })

}