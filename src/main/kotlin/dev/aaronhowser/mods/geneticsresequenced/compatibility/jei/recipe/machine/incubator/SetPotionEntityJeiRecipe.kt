package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class SetPotionEntityJeiRecipe(
	val entityType: EntityType<*>,
	val isMutation: Boolean
) {

	val ingredient: Ingredient
	val input: Ingredient
	val output: ItemStack

	init {
		val cellStack = ModItems.CELL.toStack()
		EntityDnaItem.setEntityType(cellStack, entityType)
		ingredient = Ingredient.of(cellStack)

		val potionStack = if (isMutation) BrewingRecipes.mutationPotionStack else BrewingRecipes.cellGrowthPotionStack
		input = Ingredient.of(potionStack)

		val outputStack = potionStack.copy()
		EntityDnaItem.setEntityType(outputStack, entityType)
		output = outputStack
	}

	fun getId(): ResourceLocation {
		val potionString = if (isMutation) "mutation" else "pcg"
		val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')

		return GeneticsResequenced.modResource("/set_potion_entity/$potionString/$entityTypeString")
	}

	companion object {
		fun getAllRecipes(): List<SetPotionEntityJeiRecipe> {
			return EntityDnaItem.VALID_ENTITY_TYPES.flatMap {
				listOf(
					SetPotionEntityJeiRecipe(it, isMutation = false),
					SetPotionEntityJeiRecipe(it, isMutation = true)
				)
			}
		}
	}
}
