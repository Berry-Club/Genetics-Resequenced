package dev.aaronhowser.mods.genetics_resequenced.recipe.base

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.genetics_resequenced.registry.ModIngredientTypes
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import java.util.stream.Stream

class PotionTagIngredient(
	val potionTag: TagKey<Potion>
) : ICustomIngredient {

	override fun test(stack: ItemStack): Boolean {
		if (stack.item != Items.POTION) return false

		val potion = OtherUtil.getPotion(stack) ?: return false
		return potion.isHolder(this.potionTag)
	}

	override fun items(): Stream<Holder<Item>> {
		return Stream.of(Items.POTION.builtInRegistryHolder())
	}

	override fun display(): SlotDisplay {
		val potionDisplays = BuiltInRegistries.POTION.getTagOrEmpty(this.potionTag)
			.asSequence()
			.map { OtherUtil.getPotionStack(it) }
			.map(ItemStackTemplate::fromNonEmptyStack)
			.map { SlotDisplay.ItemStackSlotDisplay(it) }
			.toList()

		return SlotDisplay.Composite(potionDisplays)
	}

	override fun isSimple(): Boolean {
		return false
	}

	override fun getType(): IngredientType<*> {
		return ModIngredientTypes.POTION_TAG.get()
	}

	companion object {
		val CODEC: MapCodec<PotionTagIngredient> =
			RecordCodecBuilder.mapCodec { instance ->
				instance.group(
					TagKey.codec(Registries.POTION)
						.fieldOf("potion_tag")
						.forGetter(PotionTagIngredient::potionTag)
				).apply(instance, ::PotionTagIngredient)
			}
	}

}
