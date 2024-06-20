package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(GeneticsResequenced.ID)

    val RANDOM_ITEM = ITEM_REGISTRY.registerSimpleItem("random_item")
    val ANOTHER = ITEM_REGISTRY.registerSimpleItem("another")

}