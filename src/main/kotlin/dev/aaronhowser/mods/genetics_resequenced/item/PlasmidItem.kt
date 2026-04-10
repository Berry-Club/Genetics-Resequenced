package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.item.components.PlasmidProgressItemComponent
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import java.util.function.Consumer

class PlasmidItem(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		itemStack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val geneHolder = getGene(itemStack)

		if (geneHolder == null) {
			builder.accept(
				ModTooltipLang.PLASMID_EMPTY
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
			return
		}

		builder.accept(
			ModTooltipLang.PLASMID_GENE
				.toComponent(geneHolder.getName())
				.withStyle(ChatFormatting.GRAY)
		)

		if (isComplete(itemStack)) {
			builder.accept(
				ModTooltipLang.PLASMID_COMPLETE
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			val amountNeeded = geneHolder.value().dnaPointsRequired
			val amount = getDnaPoints(itemStack)

			builder.accept(
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