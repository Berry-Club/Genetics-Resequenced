package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.ItemStack

class PurifySyringeJeiRecipe(
	val isMetal: Boolean = false
) {

	val contaminatedSyringe: ItemStack
	val decontaminatedSyringe: ItemStack

	init {
		val syringeStack = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

		val localPlayer = AaronClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")
		val entity = if (isMetal) Cow(EntityType.COW, localPlayer.level()) else localPlayer

		SyringeItem.setEntity(syringeStack, entity, setContaminated = true)
		contaminatedSyringe = syringeStack.copy()

		SyringeItem.setContaminated(syringeStack, false)
		decontaminatedSyringe = syringeStack.copy()
	}

	fun getId(): ResourceLocation {
		val type = if (isMetal) "metal" else "glass"
		return GeneticsResequenced.modResource("/purify_syringe/$type")
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<PurifySyringeJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModBlocks.BLOOD_PURIFIER)

		override fun getRecipeType(): RecipeType<PurifySyringeJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.BLOOD_PURIFIER.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 76
		override fun getHeight(): Int = 18

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: PurifySyringeJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.contaminatedSyringe)

			builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.decontaminatedSyringe)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: PurifySyringeJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(26, 1)
		}

		override fun getRegistryName(recipe: PurifySyringeJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<PurifySyringeJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "blood_purifier", PurifySyringeJeiRecipe::class.java)

		fun getAllRecipes(): List<PurifySyringeJeiRecipe> {
			return listOf(
				PurifySyringeJeiRecipe(isMetal = false),
				PurifySyringeJeiRecipe(isMetal = true)
			)
		}
	}
}
