package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import java.util.function.Consumer

class AntiPlasmidItem(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipDisplay: TooltipDisplay,
		tooltipComponents: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val geneHolder = PlasmidItem.getGene(stack)

		if (geneHolder == null) {
			tooltipComponents.accept(
				ModTooltipLang.ANTI_PLASMID_EMPTY
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			tooltipComponents.accept(
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
