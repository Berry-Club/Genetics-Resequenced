package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import java.util.*
import kotlin.jvm.optionals.getOrNull

object OtherUtil {

    fun modResource(path: String): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(GeneticsResequenced.ID, path)

    val ItemLike.itemStack: ItemStack
        get() = this.asItem().defaultInstance

    fun MutableComponent.withColor(color: ChatFormatting): MutableComponent = this.withStyle { it.withColor(color) }

    private val entityUuidMap: MutableMap<UUID, LivingEntity> = mutableMapOf()
    fun getNearbyEntityFromUuid(uuid: UUID, searchAroundEntity: LivingEntity): LivingEntity? {
        val mappedValue = entityUuidMap[uuid]
        if (mappedValue != null) return mappedValue

        val nearbyEntities = searchAroundEntity.level().getNearbyEntities(
            LivingEntity::class.java,
            TargetingConditions.DEFAULT,
            searchAroundEntity,
            searchAroundEntity.boundingBox.inflate(50.0)
        )

        for (entity in nearbyEntities) {
            if (entity.uuid == uuid) {
                entityUuidMap[uuid] = entity
                return entity
            }
        }

        return null
    }

    fun CompoundTag.getUuidOrNull(key: String): UUID? {
        if (!this.hasUUID(key)) return null
        return this.getUUID(key)
    }

    fun getEntityType(resourceLocation: ResourceLocation): EntityType<*> {
        val entityType = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation)

        if (entityType === EntityType.PIG && resourceLocation != BuiltInRegistries.ENTITY_TYPE.defaultKey) {
            throw IllegalArgumentException("Unknown entity type: $resourceLocation")
        }

        return entityType
    }

    fun getLookedAtEntity(livingEntity: LivingEntity, range: Double = 4.0): Entity? {
        val entityHitResult = ProjectileUtil.getEntityHitResult(
            livingEntity,
            livingEntity.eyePosition,
            livingEntity.eyePosition.add(livingEntity.lookAngle.scale(range)),
            livingEntity.boundingBox.inflate(range),
            { true },
            range
        )

        return entityHitResult?.entity
    }

    fun getPotionContents(itemStack: ItemStack): PotionContents? = itemStack.get(DataComponents.POTION_CONTENTS)
    fun getPotion(itemStack: ItemStack): Holder<Potion>? = getPotionContents(itemStack)?.potion?.getOrNull()

    fun getPotionStack(potion: Holder<Potion>): ItemStack {
        return PotionContents.createItemStack(Items.POTION, potion)
    }

    fun getEnchantmentRegistry(entity: Entity): Registry<Enchantment> {
        return entity.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
    }

    fun getEnchantHolder(entity: Entity, enchantment: ResourceKey<Enchantment>): Holder.Reference<Enchantment> {
        return getEnchantmentRegistry(entity).getHolderOrThrow(enchantment)
    }

    fun componentList(components: List<Component>, separator: String = "\n• "): MutableComponent {
        val mutableComponent = Component.empty()

        for (component in components) {
            mutableComponent.append(Component.literal(separator).append(component))
        }

        return mutableComponent
    }

    fun <T> tagKeyStreamCodec(registry: ResourceKey<out Registry<T>>): StreamCodec<ByteBuf, TagKey<T>> {
        return ResourceLocation.STREAM_CODEC.map(
            { TagKey.create(registry, it) },
            { it.location() }
        )
    }
}