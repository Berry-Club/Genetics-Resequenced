package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityComponent.Companion.hasEntity
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class SyringeItem : Item(
    Properties().stacksTo(1)
) {

    companion object {

        fun Item.isSyringe(): Boolean {
            return this == ModItems.SYRINGE.get() || this == ModItems.METAL_SYRINGE.get()
        }

        fun ItemStack.isSyringe(): Boolean {
            return this.item.isSyringe()
        }

        fun isBeingUsed(syringeStack: ItemStack, entity: LivingEntity?): Boolean {
            if (entity == null) return false

            return entity.useItem == syringeStack
        }

        fun hasBlood(syringeStack: ItemStack): Boolean {
            return syringeStack.hasEntity()
        }

    }

}