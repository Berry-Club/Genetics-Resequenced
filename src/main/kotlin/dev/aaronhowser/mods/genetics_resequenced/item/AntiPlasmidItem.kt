package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AntiPlasmidItem(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		pStack: ItemStack,
		pLevel: Level?,
		pTooltipComponents: MutableList<Component>,
		pIsAdvanced: TooltipFlag
	) {
		val registryAccess = pLevel?.registryAccess() ?: return

		val geneRk = PlasmidItem.getGeneRk(pStack)

		if (geneRk == null) {
			pTooltipComponents.add(
				ModTooltipLang.ANTI_PLASMID_EMPTY
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			pTooltipComponents.add(
				ModTooltipLang.PLASMID_GENE
					.toComponent(geneRk.getHolderOrThrow(registryAccess).getName())
					.withStyle(ChatFormatting.GRAY)
			)
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}