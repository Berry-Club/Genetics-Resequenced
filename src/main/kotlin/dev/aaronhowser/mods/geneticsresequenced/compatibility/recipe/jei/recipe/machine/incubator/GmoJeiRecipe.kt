package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
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
