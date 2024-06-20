package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.block.ModBlocks
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object ModRegistries {

    private val registries: List<DeferredRegister<out Any>> = listOf(
        ModCreativeModeTabs.TABS_REGISTRY,
        ModItems.ITEM_REGISTRY,
        ModBlocks.BLOCK_REGISTRY
    )

    fun register(modBus: IEventBus) {
        registries.forEach { it.register(modBus) }
    }

}