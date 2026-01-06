package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.getComponent
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.hasComponent
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.setComponent
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
		val geneHolder = getGeneHolder(pStack)

		if (geneHolder == null) {
			showNoGeneTooltips(pStack, pTooltipComponents)
		} else {
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
		fun hasGene(itemStack: ItemStack): Boolean = itemStack.hasComponent(GeneDataComponent.Type)
		fun getGeneHolder(itemStack: ItemStack): Holder<Gene>? = itemStack.getComponent(GeneDataComponent.Type)?.geneHolder

		fun setGeneHolder(itemStack: ItemStack, geneHolder: Holder<Gene>): ItemStack {
			itemStack.setComponent(GeneDataComponent(geneHolder))
			return itemStack
		}

		fun getHelixStack(geneRk: ResourceKey<Gene>, registries: HolderLookup.Provider): ItemStack {
			return getHelixStack(geneRk.getHolderOrThrow(registries))
		}

		fun getHelixStack(geneHolder: Holder<Gene>): ItemStack {
			val itemStack = ModItems.DNA_HELIX.getDefaultInstance()
			setGeneHolder(itemStack, geneHolder)
			return itemStack
		}

		fun getAllHelices(registries: HolderLookup.Provider): List<ItemStack> {
			return ModGenes.getRegistrySorted(registries, includeHelixOnly = true)
				.map { geneHolder -> getHelixStack(geneHolder) }
		}
	}

}