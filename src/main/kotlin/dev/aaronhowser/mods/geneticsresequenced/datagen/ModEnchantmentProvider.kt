package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import java.util.*

object ModEnchantmentProvider : RegistrySetBuilder() {

	private fun createRk(name: String): ResourceKey<Enchantment> = ResourceKey.create(Registries.ENCHANTMENT, OtherUtil.modResource(name))

	val DELICATE_TOUCH = createRk("delicate_toucha")

	fun bootstrap(context: BootstrapContext<Enchantment>) {

		context.register(
			DELICATE_TOUCH,
			Enchantment(
				Component.literal(""),
				Enchantment.EnchantmentDefinition(
					BuiltInRegistries.ITEM.getTag(ModItemTagsProvider.ENCHANTABLE_DELICATE_TOUCH).get(),
					Optional.empty(),
					10,
					1,
					Enchantment.Cost(4, 0),
					Enchantment.Cost(150, 0),
					1,
					listOf()
				),
				HolderSet.empty(),
				DataComponentMap.builder().build()
			)
		)

	}

}