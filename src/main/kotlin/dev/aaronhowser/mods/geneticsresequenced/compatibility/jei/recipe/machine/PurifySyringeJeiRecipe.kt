package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.recipe.machine

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
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

	companion object {
		fun getAllRecipes(): List<PurifySyringeJeiRecipe> {
			return listOf(
				PurifySyringeJeiRecipe(isMetal = false),
				PurifySyringeJeiRecipe(isMetal = true)
			)
		}
	}
}
