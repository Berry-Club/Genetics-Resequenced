package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack

class OrganicMatterToCellJeiRecipe(
	val entityType: EntityType<*>
) {

	val organicMatter: ItemStack
	val cell: ItemStack

	init {
		organicMatter = ModItems.ORGANIC_MATTER.toStack()
		EntityDnaItem.setEntityType(organicMatter, entityType)

		cell = ModItems.CELL.toStack()
		EntityDnaItem.setEntityType(cell, entityType)
	}

	fun getId(): Identifier {
		val entityString = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString().replace(':', '/')
		return GeneticsResequenced.modId("/cell_analyzer/$entityString")
	}

	companion object {
		fun getAllRecipes(): List<OrganicMatterToCellJeiRecipe> {
			return EntityDnaItem.VALID_ENTITY_TYPES
				.map(::OrganicMatterToCellJeiRecipe)
				.distinctBy(OrganicMatterToCellJeiRecipe::getId)
		}
	}
}
