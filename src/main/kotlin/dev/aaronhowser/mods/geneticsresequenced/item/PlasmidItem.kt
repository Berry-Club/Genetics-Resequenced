package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.item.components.PlasmidProgressItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class PlasmidItem(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		components: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val geneHolder = getGene(stack)

		if (geneHolder == null) {
			components.add(
				ModTooltipLang.PLASMID_EMPTY
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
			return
		}

		components.add(
			ModTooltipLang.PLASMID_GENE
				.toComponent(geneHolder.getName())
				.withStyle(ChatFormatting.GRAY)
		)

		if (isComplete(stack)) {
			components.add(
				ModTooltipLang.PLASMID_COMPLETE
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			val amountNeeded = geneHolder.value().dnaPointsRequired
			val amount = getDnaPoints(stack)

			components.add(
				ModTooltipLang.PLASMID_PROGRESS
					.toComponent(amount, amountNeeded)
					.withStyle(ChatFormatting.GRAY)
			)
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		fun hasGene(itemStack: ItemStack): Boolean = itemStack.has(ModDataComponents.PLASMID_PROGRESS)

		fun getGene(itemStack: ItemStack): Holder<Gene>? {
			return itemStack.get(ModDataComponents.PLASMID_PROGRESS)?.geneHolder
		}

		fun setGene(itemStack: ItemStack, geneHolder: Holder<Gene>, amount: Int = 0) {
			val component = PlasmidProgressItemComponent(
				geneHolder,
				amount
			)
			itemStack.set(ModDataComponents.PLASMID_PROGRESS, component)
		}

		fun getDnaPoints(itemStack: ItemStack): Int {
			return itemStack.get(ModDataComponents.PLASMID_PROGRESS)?.dnaPoints ?: 0
		}

		fun setDnaPoints(itemStack: ItemStack, amount: Int) {
			val component = PlasmidProgressItemComponent(
				getGene(itemStack) ?: return,
				amount
			)
			itemStack.set(ModDataComponents.PLASMID_PROGRESS, component)
		}

		fun increaseDnaPoints(itemStack: ItemStack, amount: Int = 1) {
			setDnaPoints(itemStack, getDnaPoints(itemStack) + amount)
		}

		fun isComplete(itemStack: ItemStack): Boolean {
			val geneHolder = getGene(itemStack) ?: return false
			return getDnaPoints(itemStack) >= geneHolder.value().dnaPointsRequired
		}

		fun getCompletedPlasmid(geneHolder: Holder<Gene>): ItemStack {
			val stack = ModItems.PLASMID.toStack()
			setGene(stack, geneHolder, geneHolder.value().dnaPointsRequired)
			return stack
		}

		fun getAllPlasmids(registries: HolderLookup.Provider): List<ItemStack> {
			return ModGenes.getRegistrySorted(registries, includeHelixOnly = false).map(::getCompletedPlasmid)
		}

	}

}