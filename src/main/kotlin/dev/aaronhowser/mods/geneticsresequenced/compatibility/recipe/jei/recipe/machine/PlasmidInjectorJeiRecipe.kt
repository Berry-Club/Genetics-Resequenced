package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
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
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.ItemStack

class PlasmidInjectorJeiRecipe(
	val geneHolder: Holder<Gene>,
	val isMetal: Boolean,
	val isAntiPlasmid: Boolean
) {

	val plasmid: ItemStack
	val syringeBefore: ItemStack
	val syringeAfter: ItemStack

	init {
		plasmid = if (isAntiPlasmid) ModItems.ANTI_PLASMID.toStack() else ModItems.PLASMID.toStack()
		PlasmidItem.setGene(plasmid, geneHolder, geneHolder.value().dnaPointsRequired)

		syringeBefore = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

		val localPlayer = AaronClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")
		val entity = if (isMetal) Cow(EntityType.COW, localPlayer.level()) else localPlayer

		SyringeItem.setEntity(syringeBefore, entity, setContaminated = false)

		syringeAfter = syringeBefore.copy()
		if (isAntiPlasmid) {
			SyringeItem.addAntigene(syringeAfter, geneHolder)
		} else {
			SyringeItem.addGene(syringeAfter, geneHolder)
		}
	}

	val tooltip: Component = if (isAntiPlasmid) {
		ModRecipeLang.INJECTOR_ANTIGENES
	} else {
		ModRecipeLang.INJECTOR_GENES
	}.toComponent()

	fun getId(): ResourceLocation {
		val geneString = geneHolder.key!!.location().toString().replace(':', '/')
		val syringeString = if (isMetal) "/metal" else ""
		val plasmidString = if (isAntiPlasmid) "/anti" else ""

		return GeneticsResequenced.modResource("/plasmid_injector/$geneString$syringeString$plasmidString")
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<PlasmidInjectorJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModBlocks.PLASMID_INJECTOR)

		override fun getRecipeType(): RecipeType<PlasmidInjectorJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.PLASMID_INJECTOR.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 102
		override fun getHeight(): Int = 18

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: PlasmidInjectorJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.plasmid)

			builder.addSlot(RecipeIngredientRole.INPUT, 24, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.syringeBefore)

			builder.addSlot(RecipeIngredientRole.OUTPUT, 78, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.syringeAfter)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: PlasmidInjectorJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(48, 1)
		}

		override fun getTooltip(
			tooltip: ITooltipBuilder,
			recipe: PlasmidInjectorJeiRecipe,
			recipeSlotsView: IRecipeSlotsView,
			mouseX: Double,
			mouseY: Double
		) {
			if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) {
				tooltip.add(recipe.tooltip)
			}
		}

		override fun getRegistryName(recipe: PlasmidInjectorJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<PlasmidInjectorJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "plasmid_injector", PlasmidInjectorJeiRecipe::class.java)

		fun getAllRecipes(): List<PlasmidInjectorJeiRecipe> {
			return ModGenes
				.getRegistrySorted(ClientUtil.localRegistryAccess!!, includeHelixOnly = false)
				.flatMap { geneHolder ->
					listOf(false, true).flatMap { isMetal ->
						listOf(false, true).map { isAntiPlasmid ->
							PlasmidInjectorJeiRecipe(geneHolder, isMetal, isAntiPlasmid)
						}
					}
				}
				.distinctBy(PlasmidInjectorJeiRecipe::getId)
		}
	}
}
