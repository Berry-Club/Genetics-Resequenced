package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.getComponent
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.hasComponent
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.setComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.item.components.PlasmidProgressItemComponent
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
		pStack: ItemStack,
		pContext: TooltipContext,
		pTooltipComponents: MutableList<Component>,
		pTooltipFlag: TooltipFlag
	) {
		val geneHolder = getGene(pStack)

		if (geneHolder == null) {
			pTooltipComponents.add(
				ModTooltipLang.PLASMID_EMPTY
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
			return
		}

		pTooltipComponents.add(
			ModTooltipLang.PLASMID_GENE
				.toComponent(geneHolder.getName())
				.withStyle(ChatFormatting.GRAY)
		)

		if (isComplete(pStack)) {
			pTooltipComponents.add(
				ModTooltipLang.PLASMID_COMPLETE
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			val amountNeeded = geneHolder.value().dnaPointsRequired
			val amount = getDnaPoints(pStack)

			pTooltipComponents.add(
				ModTooltipLang.PLASMID_PROGRESS
					.toComponent(amount, amountNeeded)
					.withStyle(ChatFormatting.GRAY)
			)
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		fun hasGene(itemStack: ItemStack): Boolean = itemStack.hasComponent(PlasmidProgressItemComponent.Type)

		fun getGene(itemStack: ItemStack): Holder<Gene>? {
			return itemStack.getComponent(PlasmidProgressItemComponent.Type)?.geneHolder
		}

		fun setGene(itemStack: ItemStack, geneHolder: Holder<Gene>, amount: Int = 0) {
			val component = PlasmidProgressItemComponent(
				geneHolder,
				amount
			)

			itemStack.setComponent(component)
		}

		fun getDnaPoints(itemStack: ItemStack): Int {
			return itemStack.getComponent(PlasmidProgressItemComponent.Type)?.dnaPoints ?: 0
		}

		fun setDnaPoints(itemStack: ItemStack, amount: Int) {
			val component = PlasmidProgressItemComponent(
				getGene(itemStack) ?: return,
				amount
			)

			itemStack.setComponent(component)
		}

		fun increaseDnaPoints(itemStack: ItemStack, amount: Int = 1) {
			setDnaPoints(itemStack, getDnaPoints(itemStack) + amount)
		}

		fun isComplete(itemStack: ItemStack): Boolean {
			val geneHolder = getGene(itemStack) ?: return false
			return getDnaPoints(itemStack) >= geneHolder.value().dnaPointsRequired
		}

		fun getCompletedPlasmid(geneHolder: Holder<Gene>): ItemStack {
			val stack = ModItems.PLASMID.getDefaultInstance()
			setGene(stack, geneHolder, geneHolder.value().dnaPointsRequired)
			return stack
		}

		fun getAllPlasmids(registries: HolderLookup.Provider): List<ItemStack> {
			return ModGenes.getRegistrySorted(registries, includeHelixOnly = false).map(::getCompletedPlasmid)
		}

	}

}