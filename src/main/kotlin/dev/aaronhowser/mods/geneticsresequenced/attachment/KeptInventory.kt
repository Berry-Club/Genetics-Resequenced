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

        val CODEC: Codec<KeptInventory> = ItemStack.CODEC.listOf().xmap(
            { list -> KeptInventory(list) },
            { inv -> inv.stacks }
        )

        val attachment by lazy { ModAttachmentTypes.KEPT_INVENTORY.get() }

        fun Player.keepInventory(list: List<ItemStack>) {
            this.setData(attachment, KeptInventory(list))
        }

        fun Player.getKeptInventory(): List<ItemStack> {
            return this.getData(attachment).stacks
        }

        fun Player.clearKeptInventory() {
            this.setData(attachment, KeptInventory(emptyList()))
        }

    }

}