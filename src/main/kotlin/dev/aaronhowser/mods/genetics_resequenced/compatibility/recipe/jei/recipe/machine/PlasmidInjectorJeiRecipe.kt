package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.item.PlasmidItem
import dev.aaronhowser.mods.genetics_resequenced.item.SyringeItem
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.cow.Cow
import net.minecraft.world.item.ItemStack

class PlasmidInjectorJeiRecipe(
	val geneHolder: Holder<Gene>,
	val isMetal: Boolean,
	val isAntiPlasmid: Boolean
) {

	val plasmid: ItemStack
	val syringeBefore: ItemStack
	val syringeAfter: ItemStack

	init {
		plasmid = if (isAntiPlasmid) ModItems.ANTI_PLASMID.toStack() else ModItems.PLASMID.toStack()
		PlasmidItem.setGene(plasmid, geneHolder, geneHolder.value().dnaPointsRequired)

		syringeBefore = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

		val localPlayer = AaronClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")
		val entity = if (isMetal) Cow(EntityType.COW, localPlayer.level()) else localPlayer

		SyringeItem.setEntity(syringeBefore, entity, setContaminated = false)

		syringeAfter = syringeBefore.copy()
		if (isAntiPlasmid) {
			SyringeItem.addAntigene(syringeAfter, geneHolder)
		} else {
			SyringeItem.addGene(syringeAfter, geneHolder)
		}
	}

	val tooltip: Component = if (isAntiPlasmid) {
		ModRecipeLang.INJECTOR_ANTIGENES
	} else {
		ModRecipeLang.INJECTOR_GENES
	}.toComponent()

	fun getId(): Identifier {
		val geneString = geneHolder.key!!.identifier().toString().replace(':', '/')
		val syringeString = if (isMetal) "/metal" else ""
		val plasmidString = if (isAntiPlasmid) "/anti" else ""

		return GeneticsResequenced.modId("/plasmid_injector/$geneString$syringeString$plasmidString")
	}

	companion object {
		fun getAllRecipes(): List<PlasmidInjectorJeiRecipe> {
			return ModGenes
				.getRegistrySorted(ClientUtil.localRegistryAccess!!, includeHelixOnly = false)
				.flatMap { geneHolder ->
					listOf(false, true).flatMap { isMetal ->
						listOf(false, true).map { isAntiPlasmid ->
							PlasmidInjectorJeiRecipe(geneHolder, isMetal, isAntiPlasmid)
						}
					}
				}
				.distinctBy(PlasmidInjectorJeiRecipe::getId)
		}
	}
}
