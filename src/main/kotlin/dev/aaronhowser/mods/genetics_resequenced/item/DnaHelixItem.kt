package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toGrayComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import java.util.function.Consumer

class DnaHelixItem(properties: Properties) : EntityDnaItem(properties) {

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipDisplay: TooltipDisplay,
		tooltipComponents: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val geneHolder = getGeneHolder(stack)

		if (geneHolder == null) {
			showNoGeneTooltips(stack, tooltipComponents)
		} else {
			tooltipComponents.accept(
				ModTooltipLang.GENE
					.toGrayComponent(geneHolder.getName())
			)
		}

	}

	companion object {
		fun hasGene(itemStack: ItemStack): Boolean = itemStack.has(ModDataComponents.GENE)
		fun getGeneHolder(itemStack: ItemStack): Holder<Gene>? = itemStack.get(ModDataComponents.GENE)

		fun setGeneHolder(itemStack: ItemStack, geneHolder: Holder<Gene>): ItemStack {
			itemStack.set(ModDataComponents.GENE, geneHolder)
			return itemStack
		}

		fun getHelixStack(geneRk: ResourceKey<Gene>, registries: HolderLookup.Provider): ItemStack {
			return getHelixStack(geneRk.getHolderOrThrow(registries))
		}

		fun getHelixStack(geneHolder: Holder<Gene>): ItemStack {
			val itemStack = ModItems.DNA_HELIX.toStack()
			setGeneHolder(itemStack, geneHolder)
			return itemStack
		}

		fun getAllHelices(registries: HolderLookup.Provider): List<ItemStack> {
			return ModGenes.getRegistrySorted(registries, includeHelixOnly = true)
				.map { geneHolder -> getHelixStack(geneHolder) }
		}

		private fun showNoGeneTooltips(
			stack: ItemStack,
			components: Consumer<Component>
		) {
			components.accept(
				ModTooltipLang.GENE
					.toGrayComponent(Gene.UNKNOWN_GENE_COMPONENT)
			)

			val entity = getEntityType(stack)
			if (entity != null) {
				components.accept(
					ModTooltipLang.HELIX_ENTITY
						.toGrayComponent(entity.description)
				)
			}

			if (ClientUtil.playerIsCreative()) {
				val component =
					ModTooltipLang.CELL_CREATIVE.toGrayComponent()

				components.accept(component)
			}
		}
	}

}
