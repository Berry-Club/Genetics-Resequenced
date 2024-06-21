package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class EntityDnaItem : Item(Properties()) {

    companion object {

        fun hasEntity(itemStack: ItemStack): Boolean = itemStack.has(DataComponents)

        fun setMob(itemStack: ItemStack, entityType: EntityType<*>): Boolean {
            val mobRL = OtherUtil.getEntityResourceLocation(entityType)

            return setMob(itemStack, mobRL)
        }

        fun setMob(itemStack: ItemStack, entityRL: ResourceLocation): Boolean {
            val tag = itemStack.orCreateTag
            tag.putString(MOB_ID_NBT, entityRL.toString())
            return true
        }

        fun ItemStack.setMob(entityType: EntityType<*>): ItemStack? {
            return if (setMob(this, entityType)) {
                this
            } else {
                null
            }
        }

        fun ItemStack.setMob(entityRL: ResourceLocation): ItemStack? {
            return if (setMob(this, entityRL)) {
                this
            } else {
                null
            }
        }

        fun getEntityType(itemStack: ItemStack): EntityType<*>? {
            if (!hasEntity(itemStack)) return null
            val string = itemStack.tag?.getString(MOB_ID_NBT) ?: return null
            val resourceLocation = ResourceLocation.tryParse(string) ?: return null

            return OtherUtil.getEntityType(resourceLocation)
        }
    }

}