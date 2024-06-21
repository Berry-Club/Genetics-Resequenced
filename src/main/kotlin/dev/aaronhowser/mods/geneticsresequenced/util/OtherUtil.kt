package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object OtherUtil {

    fun modResource(path: String): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(GeneticsResequenced.ID, path)

    fun getEntityType(resourceLocation: ResourceLocation): EntityType<*> {

        val entityType = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation)

        if (entityType == EntityType.PIG && resourceLocation != getEntityResourceLocation(EntityType.PIG)) {
            throw IllegalArgumentException("Unknown entity type: $resourceLocation")
        }

        return entityType
    }

    fun getEntityResourceLocation(entityType: EntityType<*>): ResourceLocation {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
    }

    val Item.itemStack: ItemStack
        get() = this.defaultInstance
}