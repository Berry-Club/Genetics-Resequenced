package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneDataComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class DnaHelixItem(properties: Properties) : EntityDnaItem(properties) {

	override fun appendHoverText(
		pStack: ItemStack,
		pLevel: Level?,
		pTooltipComponents: MutableList<Component>,
		pIsAdvanced: TooltipFlag
	) {
		val geneRk = GeneDataComponent.getGeneRk(pStack)

		if (geneRk == null) {
			showNoGeneTooltips(pStack, pTooltipComponents)
		} else {
			val registryAccess = pLevel?.registryAccess() ?: return
			val geneHolder = geneRk.getHolderOrThrow(registryAccess)

			pTooltipComponents.add(
				ModTooltipLang.GENE
					.toComponent(geneHolder.getName())
					.withStyle(ChatFormatting.GRAY)
			)
		}

	}

	private fun showNoGeneTooltips(
		pStack: ItemStack,
		pTooltipComponents: MutableList<Component>
	) {

		pTooltipComponents.add(
			ModTooltipLang.GENE
				.toComponent(Gene.UNKNOWN_GENE_COMPONENT)
				.withStyle(ChatFormatting.GRAY)
		)

		val entity = getEntityType(pStack)
		if (entity != null) {
			pTooltipComponents.add(
				ModTooltipLang.HELIX_ENTITY
					.toComponent(entity.description)
					.withStyle(ChatFormatting.GRAY)
			)
		}

		try {
			val isCreative = ClientUtil.playerIsCreative()

			if (isCreative) {
				val component =
					ModTooltipLang.CELL_CREATIVE
						.toComponent()
						.withStyle(ChatFormatting.GRAY)
				pTooltipComponents.add(component)
			}
		} catch (e: Exception) {
			GeneticsResequenced.LOGGER.error("DnaHelixItem isCreative check failed", e)
		}

	}

	companion object {

		fun getHelixStack(geneRk: ResourceKey<Gene>, registries: HolderLookup.Provider): ItemStack {
			return getHelixStack(geneRk.getHolderOrThrow(registries))
		}

		fun getHelixStack(geneHolder: Holder<Gene>): ItemStack {
			val itemStack = ModItems.DNA_HELIX.getDefaultInstance()
			GeneDataComponent.setGene(itemStack, geneHolder)
			return itemStack
		}

		fun getHelixStack(geneRk: ResourceKey<Gene>): ItemStack {
			val itemStack = ModItems.DNA_HELIX.getDefaultInstance()
			GeneDataComponent.setGene(itemStack, geneRk)
			return itemStack
		}

		fun getAllHelices(registries: HolderLookup.Provider): List<ItemStack> {
			return ModGenes.getRegistrySorted(registries, includeHelixOnly = true).map(::getHelixStack)
		}
	}

}