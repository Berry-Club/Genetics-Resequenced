package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

data class KeptInventory(
    val stacks: List<ItemStack>
) {

    constructor() : this(emptyList())

    companion object {

        val CODEC: Codec<KeptInventory> =
            ItemStack.CODEC.listOf().xmap(::KeptInventory, KeptInventory::stacks)

        fun Player.saveInventory(list: List<ItemStack>) {
            this.setData(ModAttachmentTypes.KEPT_INVENTORY, KeptInventory(list))
        }

        fun Player.getSavedInventory(): List<ItemStack> {
            return this.getData(ModAttachmentTypes.KEPT_INVENTORY).stacks
        }

        fun Player.clearSavedInventory() {
            this.setData(ModAttachmentTypes.KEPT_INVENTORY, KeptInventory(emptyList()))
        }

    }

}