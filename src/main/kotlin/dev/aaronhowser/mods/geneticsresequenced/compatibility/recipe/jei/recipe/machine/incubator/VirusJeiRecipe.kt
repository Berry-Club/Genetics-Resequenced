package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.VirusRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
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
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class VirusJeiRecipe(
	val id: ResourceLocation,
	val ingredient: Ingredient,
	val input: Ingredient,
	val output: ItemStack,
	val tooltips: List<Component> = emptyList()
) {

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<VirusJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemStack(BrewingRecipes.viralAgentsPotionStack)

		override fun getRecipeType(): RecipeType<VirusJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.VIRUS.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 65
		override fun getHeight(): Int = 61

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: VirusJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 28, 2).setStandardSlotBackground().addIngredients(recipe.ingredient)
			builder.addSlot(RecipeIngredientRole.INPUT, 5, 36).setStandardSlotBackground().addIngredients(recipe.input)
			builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 36).setStandardSlotBackground().addItemStack(recipe.output)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: VirusJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(23, 31)
		}

		override fun getTooltip(
			tooltip: ITooltipBuilder,
			recipe: VirusJeiRecipe,
			recipeSlotsView: IRecipeSlotsView,
			mouseX: Double,
			mouseY: Double
		) {
			if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) {
				tooltip.addAll(recipe.tooltips)
			}
		}

		override fun getRegistryName(recipe: VirusJeiRecipe): ResourceLocation = recipe.id
	}

	companion object {
		val TYPE: RecipeType<VirusJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "virus", VirusJeiRecipe::class.java)

		fun getAllRecipes(recipeManager: RecipeManager): List<VirusJeiRecipe> {
			val recipes = VirusRecipe.getVirusRecipes(recipeManager)
				.map {
					createVirusRecipe(it.value.inputDnaGene, it.value.outputGene)
				}
				.toMutableList()

			recipes.add(blackDeathRecipe(isMetal = false))
			recipes.add(blackDeathRecipe(isMetal = true))

			return recipes
		}

		private fun createVirusRecipe(
			inputDnaGeneRk: ResourceKey<Gene>,
			outputGeneRk: ResourceKey<Gene>
		): VirusJeiRecipe {
			val inputGeneString = inputDnaGeneRk.location().toString().replace(':', '/')
			val outputGeneString = outputGeneRk.location().toString().replace(':', '/')

			return VirusJeiRecipe(
				id = GeneticsResequenced.modResource("/virus/$inputGeneString/$outputGeneString"),
				ingredient = Ingredient.of(DnaHelixItem.getHelixStack(inputDnaGeneRk, ClientUtil.localRegistryAccess!!)),
				input = Ingredient.of(BrewingRecipes.viralAgentsPotionStack),
				output = DnaHelixItem.getHelixStack(outputGeneRk, ClientUtil.localRegistryAccess!!)
			)
		}

		private fun blackDeathRecipe(isMetal: Boolean): VirusJeiRecipe {
			val syringeStack = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

			val localPlayer = AaronClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")
			val entity = if (isMetal) Cow(EntityType.COW, localPlayer.level()) else localPlayer

			SyringeItem.setEntity(syringeStack, entity, setContaminated = false)

			for (gene in BlackDeathRecipe.getRequiredGenes(ClientUtil.localRegistryAccess!!)) {
				SyringeItem.addGene(syringeStack, gene)
			}

			val type = if (isMetal) "/metal" else ""

			return VirusJeiRecipe(
				id = GeneticsResequenced.modResource("/brewing/black_death$type"),
				ingredient = Ingredient.of(syringeStack),
				input = Ingredient.of(BrewingRecipes.viralAgentsPotionStack),
				output = DnaHelixItem.getHelixStack(ModGenes.BLACK_DEATH, ClientUtil.localRegistryAccess!!),
				tooltips = listOf(ModRecipeLang.BLACK_DEATH.toComponent().withStyle(ChatFormatting.GRAY))
			)
		}
	}
}
