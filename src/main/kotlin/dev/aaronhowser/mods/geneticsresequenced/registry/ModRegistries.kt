package dev.aaronhowser.mods.geneticsresequenced.registry

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object ModRegistries {

    private val registries: List<DeferredRegister<out Any>> = listOf(
        ModDataComponents.DATA_COMPONENT_REGISTRY,
        ModItems.ITEM_REGISTRY,
        ModBlocks.BLOCK_REGISTRY,
        ModCreativeModeTabs.TABS_REGISTRY,
        ModAttachmentTypes.ATTACHMENT_TYPES
    )

    fun register(modBus: IEventBus) {
        registries.forEach { it.register(modBus) }
    }

}