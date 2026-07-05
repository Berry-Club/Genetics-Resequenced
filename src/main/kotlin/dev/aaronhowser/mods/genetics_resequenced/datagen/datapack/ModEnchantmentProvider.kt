package dev.aaronhowser.mods.genetics_resequenced.datagen.datapack

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.tag.ModItemTagsProvider
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment

object ModEnchantmentProvider {

	val DELICATE_TOUCH = createRk("delicate_touch")

	fun bootstrap(context: BootstrapContext<Enchantment>) {
		val itemGetter = context.lookup(Registries.ITEM)

		context.register(
			DELICATE_TOUCH,
			Enchantment.enchantment(
				Enchantment.definition(
					itemGetter.getOrThrow(ModItemTagsProvider.ENCHANTABLE_DELICATE_TOUCH),
					10,
					1,
					Enchantment.dynamicCost(4, 0),
					Enchantment.dynamicCost(150, 0),
					1,
					EquipmentSlotGroup.HAND
				)
			).build(DELICATE_TOUCH.identifier())
		)
	}

	private fun createRk(name: String): ResourceKey<Enchantment> =
		ResourceKey.create(Registries.ENCHANTMENT, GeneticsResequenced.modId(name))

}