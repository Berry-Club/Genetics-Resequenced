package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.data.EntityGenes
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
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

	fun getId(): Identifier {
		val entityString = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString().replace(':', '/')
		val geneString = geneHolder.key!!.identifier().toString().replace(':', '/')

		return GeneticsResequenced.modId("/dna_extractor/$entityString/to/$geneString")
	}

	companion object {
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
