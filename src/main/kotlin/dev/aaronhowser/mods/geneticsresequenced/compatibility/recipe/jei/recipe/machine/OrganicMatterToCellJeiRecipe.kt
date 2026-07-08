package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
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

	fun getId(): ResourceLocation {
		val entityString = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString().replace(':', '/')
		return GeneticsResequenced.modResource("/cell_analyzer/$entityString")
	}

	companion object {
		fun getAllRecipes(): List<OrganicMatterToCellJeiRecipe> {
			return EntityDnaItem.VALID_ENTITY_TYPES
				.map(::OrganicMatterToCellJeiRecipe)
				.distinctBy(OrganicMatterToCellJeiRecipe::getId)
		}
	}
}
