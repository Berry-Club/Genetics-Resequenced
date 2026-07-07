package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
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
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class PlasmidInfuserJeiRecipe(
	val geneHolder: Holder<Gene>,
	val basic: Boolean
) {

	val helix: ItemStack
	val plasmid: ItemStack

	init {
		helix = DnaHelixItem.getHelixStack(
			if (basic) ModGenes.BASIC.getHolderOrThrow(ClientUtil.localRegistryAccess!!) else geneHolder
		)

		plasmid = ModItems.PLASMID.toStack()
		PlasmidItem.setGene(plasmid, geneHolder, geneHolder.value().dnaPointsRequired)
	}

	val tooltips: List<Component> = listOf(
		ModRecipeLang.REQUIRES_POINTS
			.toComponent(
				Gene.getNameComponent(geneHolder).withStyle(ChatFormatting.GRAY),
				geneHolder.value().dnaPointsRequired
			)
			.withStyle(ChatFormatting.GRAY),
		ModRecipeLang.BASIC_WORTH.toComponent().withStyle(ChatFormatting.GRAY),
		ModRecipeLang.MATCHING_WORTH.toComponent().withStyle(ChatFormatting.GRAY)
	)

	fun getId(): ResourceLocation {
		val geneString = geneHolder.key!!.location().toString().replace(':', '/')
		val basicString = if (basic) "/basic" else ""

		return GeneticsResequenced.modResource("/plasmid_infuser/$geneString$basicString")
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<PlasmidInfuserJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModBlocks.PLASMID_INFUSER)

		override fun getRecipeType(): RecipeType<PlasmidInfuserJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.PLASMID_INFUSER.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 76
		override fun getHeight(): Int = 18

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: PlasmidInfuserJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.helix)

			builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.plasmid)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: PlasmidInfuserJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(26, 1)
		}

		override fun getTooltip(
			tooltip: ITooltipBuilder,
			recipe: PlasmidInfuserJeiRecipe,
			recipeSlotsView: IRecipeSlotsView,
			mouseX: Double,
			mouseY: Double
		) {
			if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) {
				tooltip.addAll(recipe.tooltips)
			}
		}

		override fun getRegistryName(recipe: PlasmidInfuserJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<PlasmidInfuserJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "plasmid_infuser", PlasmidInfuserJeiRecipe::class.java)

		fun getAllRecipes(): List<PlasmidInfuserJeiRecipe> {
			return ModGenes
				.getRegistrySorted(ClientUtil.localRegistryAccess!!, includeHelixOnly = false)
				.flatMap {
					listOf(
						PlasmidInfuserJeiRecipe(it, basic = true),
						PlasmidInfuserJeiRecipe(it, basic = false)
					)
				}
				.distinctBy(PlasmidInfuserJeiRecipe::getId)
		}
	}
}
