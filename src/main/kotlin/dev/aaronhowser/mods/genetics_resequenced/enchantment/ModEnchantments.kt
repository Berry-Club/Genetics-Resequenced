package dev.aaronhowser.mods.genetics_resequenced.enchantment

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.enchantment.Enchantment

object ModEnchantments {

	val DELICATE_TOUCH: ResourceKey<Enchantment> =
		ResourceKey.create(Registries.ENCHANTMENT, GeneticsResequenced.modResource("delicate_touch"))
	val SURGICAL_PRECISION: ResourceKey<Enchantment> =
		ResourceKey.create(Registries.ENCHANTMENT, GeneticsResequenced.modResource("surgical_precision"))

	fun getDelicateTouchHolder(entity: Entity): Holder<Enchantment> {
		return OtherUtil.getEnchantHolder(entity, DELICATE_TOUCH)
	}

	fun getSurgicalPrecisionHolder(entity: Entity): Holder<Enchantment> {
		return OtherUtil.getEnchantHolder(entity, SURGICAL_PRECISION)
	}

}