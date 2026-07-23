package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack

class SetPotionEntityJeiRecipe(
	val entityType: EntityType<*>,
	val isMutation: Boolean
) {

	val ingredient: ItemStack
	val input: ItemStack
	val output: ItemStack

	init {
		val cellStack = ModItems.CELL.toStack()
		EntityDnaItem.setEntityType(cellStack, entityType)
		ingredient = cellStack

		val potionStack = if (isMutation) BrewingRecipes.mutationPotionStack else BrewingRecipes.cellGrowthPotionStack
		input = potionStack

		val outputStack = potionStack.copy()
		EntityDnaItem.setEntityType(outputStack, entityType)
		output = outputStack
	}

	fun getId(): Identifier {
		val potionString = if (isMutation) "mutation" else "pcg"
		val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')

		return GeneticsResequenced.modId("/set_potion_entity/$potionString/$entityTypeString")
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
