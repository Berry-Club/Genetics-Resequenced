package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.ingredient

import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModPotionTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.PotionTagIngredient
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.ingredients.IIngredientHelper
import mezz.jei.api.ingredients.IIngredientRenderer
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.registration.IModIngredientRegistration
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.crafting.Ingredient
import java.util.Optional
import java.util.stream.Stream

object PotionTagJeiIngredient {

	val TYPE: IIngredientType<PotionTagIngredient> =
		IIngredientType { PotionTagIngredient::class.java }

	private val ALL_INGREDIENTS = listOf(
		PotionTagIngredient(ModPotionTagsProvider.CAN_HAVE_ENTITY)
	)

	fun register(registration: IModIngredientRegistration) {
		registration.register(
			TYPE,
			ALL_INGREDIENTS,
			Helper,
			Renderer,
			PotionTagIngredient.CODEC.codec()
		)
	}

	fun addToSlot(slot: IRecipeSlotBuilder, ingredient: Ingredient): IRecipeSlotBuilder {
		val customIngredient = ingredient.customIngredient
		return if (customIngredient is PotionTagIngredient) {
			slot.addIngredient(TYPE, customIngredient)
		} else {
			slot.addIngredients(ingredient)
		}
	}

	private object Helper : IIngredientHelper<PotionTagIngredient> {
		override fun getIngredientType(): IIngredientType<PotionTagIngredient> = TYPE

		override fun getDisplayName(ingredient: PotionTagIngredient): String {
			return "#${ingredient.potionTag.location()}"
		}

		@Suppress("OVERRIDE_DEPRECATION", "removal")
		override fun getUniqueId(ingredient: PotionTagIngredient, context: UidContext): String {
			return getUid(ingredient, context).toString()
		}

		override fun getUid(ingredient: PotionTagIngredient, context: UidContext): Any {
			return ingredient.potionTag.location()
		}

		override fun getResourceLocation(ingredient: PotionTagIngredient): ResourceLocation {
			return ingredient.potionTag.location()
		}

		override fun getCheatItemStack(ingredient: PotionTagIngredient): ItemStack {
			return ingredient.getItems().findFirst().map(ItemStack::copy).orElse(ItemStack.EMPTY)
		}

		override fun copyIngredient(ingredient: PotionTagIngredient): PotionTagIngredient {
			return PotionTagIngredient(ingredient.potionTag)
		}

		override fun getTagStream(ingredient: PotionTagIngredient): Stream<ResourceLocation> {
			return Stream.of(ingredient.potionTag.location())
		}

		override fun getTagKeyEquivalent(ingredients: Collection<PotionTagIngredient>): Optional<TagKey<*>> {
			val tag = ingredients.firstOrNull()?.potionTag ?: return Optional.empty()

			return if (ingredients.all { it.potionTag == tag }) {
				Optional.of(tag)
			} else {
				Optional.empty()
			}
		}

		override fun getErrorInfo(ingredient: PotionTagIngredient?): String {
			return ingredient?.potionTag?.location()?.toString() ?: "null"
		}
	}

	private object Renderer : IIngredientRenderer<PotionTagIngredient> {
		override fun render(guiGraphics: GuiGraphics, ingredient: PotionTagIngredient) {
			val stacks = ingredient.getItems().toList()
			if (stacks.isEmpty()) return

			val index = ((System.currentTimeMillis() / 1000) % stacks.size).toInt()
			guiGraphics.renderItem(stacks[index], 0, 0)
		}

		override fun getTooltip(ingredient: PotionTagIngredient, tooltipFlag: TooltipFlag): List<Component> {
			val stack = ingredient.getItems().findFirst().orElse(ItemStack.EMPTY)
			val tooltip = if (stack.isEmpty) {
				mutableListOf<Component>(Component.literal("#${ingredient.potionTag.location()}"))
			} else {
				stack.getTooltipLines(
					Item.TooltipContext.of(Minecraft.getInstance().level),
					Minecraft.getInstance().player,
					tooltipFlag
				).toMutableList()
			}

			tooltip.add(Component.literal("#${ingredient.potionTag.location()}"))
			return tooltip
		}

		override fun getFontRenderer(minecraft: Minecraft, ingredient: PotionTagIngredient): Font {
			return minecraft.font
		}
	}
}
