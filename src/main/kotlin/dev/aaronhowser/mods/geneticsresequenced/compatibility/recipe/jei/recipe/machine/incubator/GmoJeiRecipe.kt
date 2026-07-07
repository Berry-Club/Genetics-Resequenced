package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class GmoJeiRecipe(
	val entityType: EntityType<*>,
	val ingredient: Ingredient,
	val idealResourceKey: ResourceKey<Gene>,
	val geneChance: Float,
	isMutation: Boolean,
	val success: ItemStack,
	val failure: ItemStack
) {

	val input: ItemStack
	val tooltips: List<Component> = listOf(
		ModTooltipLang.GMO_TEMPERATURE_REQUIREMENT.toComponent().withStyle(ChatFormatting.GRAY),
		CommonComponents.EMPTY,
		ModTooltipLang.GMO_CHORUS.toComponent().withStyle(ChatFormatting.GRAY)
	)

	init {
		val requiredPotion = if (isMutation) ModPotions.MUTATION else ModPotions.CELL_GROWTH
		input = OtherUtil.getPotionStack(requiredPotion)
		EntityDnaItem.setEntityType(input, entityType)
	}

	fun getId(): ResourceLocation {
		val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')
		val geneString = idealResourceKey.location().toString().replace(':', '/')

		return GeneticsResequenced.modResource("/gmo/$entityTypeString/$geneString")
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<GmoJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModItems.GMO_CELL)

		override fun getRecipeType(): RecipeType<GmoJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.GMO.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 145
		override fun getHeight(): Int = 42

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: GmoJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 5, 12).setStandardSlotBackground().addItemStack(recipe.input)
			builder.addSlot(RecipeIngredientRole.INPUT, 28, 12).setStandardSlotBackground().addIngredients(recipe.ingredient)
			builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 2).setStandardSlotBackground().addItemStack(recipe.success)
			builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 22).setStandardSlotBackground().addItemStack(recipe.failure)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: GmoJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(52, 13)

			val successChance = (recipe.geneChance * 100).toInt()
			builder.addText(Component.literal("Success: $successChance%").withStyle(ChatFormatting.GREEN), 60, 9)
				.setPosition(99, 6)
				.setColor(0x000000)
				.setShadow(true)

			builder.addText(Component.literal("Failure: ${100 - successChance}%").withStyle(ChatFormatting.RED), 60, 9)
				.setPosition(99, 26)
				.setColor(0x000000)
				.setShadow(true)
		}

		override fun getTooltip(
			tooltip: ITooltipBuilder,
			recipe: GmoJeiRecipe,
			recipeSlotsView: IRecipeSlotsView,
			mouseX: Double,
			mouseY: Double
		) {
			if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) tooltip.addAll(recipe.tooltips)
		}

		override fun getRegistryName(recipe: GmoJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<GmoJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "gmo", GmoJeiRecipe::class.java)

		fun getAllRecipes(recipeManager: RecipeManager): List<GmoJeiRecipe> {
			return GmoRecipe.getGmoRecipes(recipeManager)
				.map {
					GmoJeiRecipe(
						it.value.entityType,
						it.value.topIngredient,
						it.value.idealGeneRk,
						it.value.geneChance,
						it.value.needsMutationPotion,
						it.value.getResultItem(ClientUtil.localRegistryAccess!!),
						it.value.getFailure(ClientUtil.localRegistryAccess!!)
					)
				}
		}
	}
}
