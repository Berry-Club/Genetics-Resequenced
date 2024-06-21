package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class EntityDnaItem : Item(Properties()) {

    companion object {

        fun hasEntity(itemStack: ItemStack): Boolean = itemStack.has(ModDataComponents.ENTITY_TYPE_COMPONENT.get())

        fun setMob(itemStack: ItemStack, entityType: EntityType<*>) {
            itemStack.set(ModDataComponents.ENTITY_TYPE_COMPONENT.get(), EntityTypeComponent(entityType))
        }

        fun ItemStack.setMob(entityType: EntityType<*>): ItemStack {
            setMob(this, entityType)
            return this
        }

        fun getEntityType(itemStack: ItemStack): EntityType<*>? {
            if (!hasEntity(itemStack)) return null

            return itemStack.get(ModDataComponents.ENTITY_TYPE_COMPONENT.get())?.entity
        }
    }

}