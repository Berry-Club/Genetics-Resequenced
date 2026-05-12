package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
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

class DnaHelixItem(properties: Properties) : EntityDnaItem(properties) {

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val geneHolder = getGeneHolder(stack)

		if (geneHolder == null) {
			showNoGeneTooltips(stack, tooltipComponents)
		} else {
			tooltipComponents.add(
				ModTooltipLang.GENE
					.toComponent(geneHolder.getName())
					.withStyle(ChatFormatting.GRAY)
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
			components: MutableList<Component>
		) {
			components.add(
				ModTooltipLang.GENE
					.toComponent(Gene.UNKNOWN_GENE_COMPONENT)
					.withStyle(ChatFormatting.GRAY)
			)

			val entity = getEntityType(stack)
			if (entity != null) {
				components.add(
					ModTooltipLang.HELIX_ENTITY
						.toComponent(entity.description)
						.withStyle(ChatFormatting.GRAY)
				)
			}

			if (ClientUtil.playerIsCreative()) {
				val component =
					ModTooltipLang.CELL_CREATIVE
						.toComponent()
						.withStyle(ChatFormatting.GRAY)

				components.add(component)
			}
		}
	}

}