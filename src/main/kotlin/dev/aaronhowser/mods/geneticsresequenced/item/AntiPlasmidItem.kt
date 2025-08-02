package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class AntiPlasmidItem(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		pStack: ItemStack,
		pContext: TooltipContext,
		pTooltipComponents: MutableList<Component>,
		pTooltipFlag: TooltipFlag
	) {
		val geneHolder = PlasmidItem.getGene(pStack)

		if (geneHolder == null) {
			pTooltipComponents.add(
				ModTooltipLang.ANTI_PLASMID_EMPTY
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			pTooltipComponents.add(
				ModTooltipLang.PLASMID_GENE
					.toComponent(geneHolder.getName())
					.withStyle(ChatFormatting.GRAY)
			)
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}