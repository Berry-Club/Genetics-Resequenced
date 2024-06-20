package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {

    val TABS_REGISTRY: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, GeneticsResequenced.ID)

    val MOD_TAB = TABS_REGISTRY.register("example", Supplier {
        CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.literal("Test"))
            .icon { ItemStack(Items.STICK) }
            .build()
    })

}