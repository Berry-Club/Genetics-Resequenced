package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.item.SyringeItem
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.cow.Cow
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

	fun getId(): Identifier {
		val type = if (isMetal) "metal" else "glass"
		return GeneticsResequenced.modId("/purify_syringe/$type")
	}

	companion object {
		fun getAllRecipes(): List<PurifySyringeJeiRecipe> {
			return listOf(
				PurifySyringeJeiRecipe(isMetal = false),
				PurifySyringeJeiRecipe(isMetal = true)
			)
		}
	}
}
