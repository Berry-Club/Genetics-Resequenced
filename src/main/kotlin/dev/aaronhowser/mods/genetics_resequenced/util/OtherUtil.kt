package dev.aaronhowser.mods.genetics_resequenced.util

import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import kotlin.jvm.optionals.getOrNull

object OtherUtil {

	val ItemLike.itemStack: ItemStack
		get() = this.asItem().defaultInstance

	fun getEntityType(resourceLocation: Identifier): EntityType<*> {
		return BuiltInRegistries.ENTITY_TYPE.getOptional(resourceLocation)
			.orElseThrow { IllegalArgumentException("Unknown entity type: $resourceLocation") }
	}

	fun getLookedAtEntity(livingEntity: LivingEntity): LivingEntity? {
		val reach = livingEntity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE)

		val entityHitResult = ProjectileUtil.getEntityHitResult(
			livingEntity,
			livingEntity.eyePosition,
			livingEntity.eyePosition.add(livingEntity.lookAngle.scale(reach)),
			livingEntity.boundingBox.inflate(reach),
			{ it is LivingEntity },
			reach
		)

		return entityHitResult?.entity as? LivingEntity
	}

	fun getPotionContents(itemStack: ItemStack): PotionContents? = itemStack.get(DataComponents.POTION_CONTENTS)
	fun getPotion(itemStack: ItemStack): Holder<Potion>? = getPotionContents(itemStack)?.potion?.getOrNull()

	fun getPotionStack(potion: Holder<Potion>): ItemStack {
		return PotionContents.createItemStack(Items.POTION, potion)
	}

	fun potionContents(potion: Holder<Potion>): PotionContents =
		PotionContents(potion)

	fun potionPatch(potion: Holder<Potion>): DataComponentPatch =
		DataComponentPatch.builder()
			.set(DataComponents.POTION_CONTENTS, potionContents(potion))
			.build()

	fun potionIngredient(potion: Holder<Potion>): Ingredient =
		DataComponentIngredient.of(false, potionPatch(potion), Items.POTION)

	fun potionStackTemplate(potion: Holder<Potion>): ItemStackTemplate =
		ItemStackTemplate(Items.POTION, potionPatch(potion))

	fun getEnchantmentRegistry(entity: Entity): Registry<Enchantment> {
		return entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
	}

	fun getEnchantHolder(entity: Entity, enchantment: ResourceKey<Enchantment>): Holder.Reference<Enchantment> {
		return getEnchantmentRegistry(entity).getOrThrow(enchantment)
	}

	fun componentList(components: List<Component>): MutableComponent {
		val mutableComponent = Component.empty()

		for (component in components) {
			mutableComponent.append("• ").append(component)
			if (component != components.last()) {
				mutableComponent.append("\n")
			}
		}

		return mutableComponent
	}

}
