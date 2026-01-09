package dev.aaronhowser.mods.geneticsresequenced.enchantment

import dev.aaronhowser.mods.geneticsresequenced.datagen.ModEnchantmentProvider
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.enchantment.Enchantment

object ModEnchantments {

	fun getDelicateTouchHolder(entity: Entity): Holder<Enchantment> {
		return OtherUtil.getEnchantHolder(entity, ModEnchantmentProvider.DELICATE_TOUCH)
	}

}