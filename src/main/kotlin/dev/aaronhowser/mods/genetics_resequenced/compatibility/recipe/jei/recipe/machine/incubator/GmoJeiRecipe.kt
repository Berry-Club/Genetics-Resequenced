package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModPotions
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
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

	fun getId(): Identifier {
		val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')
		val geneString = idealResourceKey.identifier().toString().replace(':', '/')

		return GeneticsResequenced.modId("/gmo/$entityTypeString/$geneString")
	}

	companion object {
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
