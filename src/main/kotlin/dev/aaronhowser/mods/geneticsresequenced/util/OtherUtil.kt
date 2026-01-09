package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.aaron.AaronExtensions.registryAccess
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraftforge.common.ForgeMod

object OtherUtil {

	fun getEntityType(resourceLocation: ResourceLocation): EntityType<*> {
		val entityType = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation)

		if (entityType === EntityType.PIG && resourceLocation != BuiltInRegistries.ENTITY_TYPE.defaultKey) {
			throw IllegalArgumentException("Unknown entity type: $resourceLocation")
		}

		return entityType
	}

	fun getLookedAtEntity(livingEntity: LivingEntity): Entity? {
		val reach = livingEntity.getAttributeValue(ForgeMod.ENTITY_REACH.get())

		val entityHitResult = ProjectileUtil.getEntityHitResult(
			livingEntity,
			livingEntity.eyePosition,
			livingEntity.eyePosition.add(livingEntity.lookAngle.scale(reach)),
			livingEntity.boundingBox.inflate(reach),
			{ true },
			reach
		)

		return entityHitResult?.entity
	}

	fun getEnchantmentRegistry(entity: Entity): Registry<Enchantment> {
		return entity.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
	}

	fun getEnchantHolder(entity: Entity, enchantment: ResourceKey<Enchantment>): Holder.Reference<Enchantment> {
		return getEnchantmentRegistry(entity).getHolderOrThrow(enchantment)
	}

	fun componentList(components: List<Component>): MutableComponent {
		val mutableComponent = Component.empty()

		for ((index, component) in components.withIndex()) {
			mutableComponent.append(component)
			if (index != components.size - 1) {
				mutableComponent.append("\n")
			}
		}

		return mutableComponent
	}

	fun getPotionStack(potion: Potion): ItemStack = PotionUtils.setPotion(Items.POTION.defaultInstance, potion)

}