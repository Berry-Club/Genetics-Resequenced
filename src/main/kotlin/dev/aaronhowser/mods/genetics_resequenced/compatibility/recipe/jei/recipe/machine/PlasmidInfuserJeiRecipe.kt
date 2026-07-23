package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.item.PlasmidItem
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

class PlasmidInfuserJeiRecipe(
	val geneHolder: Holder<Gene>,
	val basic: Boolean
) {

	val helix: ItemStack
	val plasmid: ItemStack

	init {
		helix = DnaHelixItem.getHelixStack(
			if (basic) ModGenes.BASIC.getHolderOrThrow(ClientUtil.localRegistryAccess!!) else geneHolder
		)

		plasmid = ModItems.PLASMID.toStack()
		PlasmidItem.setGene(plasmid, geneHolder, geneHolder.value().dnaPointsRequired)
	}

	val tooltips: List<Component> = listOf(
		ModRecipeLang.REQUIRES_POINTS
			.toComponent(
				Gene.getNameComponent(geneHolder).withStyle(ChatFormatting.GRAY),
				geneHolder.value().dnaPointsRequired
			)
			.withStyle(ChatFormatting.GRAY),
		ModRecipeLang.BASIC_WORTH.toComponent().withStyle(ChatFormatting.GRAY),
		ModRecipeLang.MATCHING_WORTH.toComponent().withStyle(ChatFormatting.GRAY)
	)

	fun getId(): Identifier {
		val geneString = geneHolder.key!!.identifier().toString().replace(':', '/')
		val basicString = if (basic) "/basic" else ""

		return GeneticsResequenced.modId("/plasmid_infuser/$geneString$basicString")
	}

	companion object {
		fun getAllRecipes(): List<PlasmidInfuserJeiRecipe> {
			return ModGenes
				.getRegistrySorted(ClientUtil.localRegistryAccess!!, includeHelixOnly = false)
				.flatMap {
					listOf(
						PlasmidInfuserJeiRecipe(it, basic = true),
						PlasmidInfuserJeiRecipe(it, basic = false)
					)
				}
				.distinctBy(PlasmidInfuserJeiRecipe::getId)
		}
	}
}
