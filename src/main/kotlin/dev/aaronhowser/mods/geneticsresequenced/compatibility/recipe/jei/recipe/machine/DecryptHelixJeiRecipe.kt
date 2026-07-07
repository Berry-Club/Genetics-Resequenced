package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack

class DecryptHelixJeiRecipe(
	val entityType: EntityType<*>,
	val geneHolder: Holder<Gene>,
	val chance: Float
) {

	val encryptedHelix: ItemStack
	val decryptedHelix: ItemStack = DnaHelixItem.getHelixStack(geneHolder)

	init {
		encryptedHelix = ModItems.DNA_HELIX.toStack()
		EntityDnaItem.setEntityType(encryptedHelix, entityType)
	}

	fun getId(): ResourceLocation {
		val entityString = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString().replace(':', '/')
		val geneString = geneHolder.key!!.location().toString().replace(':', '/')

		return GeneticsResequenced.modResource("/dna_extractor/$entityString/to/$geneString")
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<DecryptHelixJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModBlocks.DNA_DECRYPTOR)

		override fun getRecipeType(): RecipeType<DecryptHelixJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.DNA_DECRYPTOR.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 116
		override fun getHeight(): Int = 18

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: DecryptHelixJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 40, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.encryptedHelix)

			builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.decryptedHelix)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: DecryptHelixJeiRecipe, focuses: IFocusGroup) {
			builder.addText(Component.literal(String.format("%.2f%%", recipe.chance * 100)), 38, 9)
				.setPosition(0, 4)
				.setColor(0x3E3E3E)

			builder.addRecipeArrow().setPosition(66, 1)
		}

		override fun getRegistryName(recipe: DecryptHelixJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<DecryptHelixJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "dna_decryptor", DecryptHelixJeiRecipe::class.java)

		fun getAllRecipes(): List<DecryptHelixJeiRecipe> {
			val recipes = mutableListOf<DecryptHelixJeiRecipe>()

			for ((entityType, map) in EntityGenes.getAllWeights(ClientUtil.localRegistryAccess!!)) {
				val totalWeight = map.values.sum()

				for ((geneHolder, weight) in map) {
					recipes.add(DecryptHelixJeiRecipe(entityType, geneHolder, weight.toFloat() / totalWeight))
				}
			}

			return recipes.distinctBy(DecryptHelixJeiRecipe::getId)
		}
	}
}
