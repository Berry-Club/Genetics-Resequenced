package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import java.util.function.Consumer

class GmoCell(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		itemStack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val entityType = EntityDnaItem.getEntityType(itemStack)

		if (entityType != null) {
			builder.accept(
				ModTooltipLang.CELL_MOB
					.toComponent(entityType.description)
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			builder.accept(
				ModTooltipLang.CELL_NO_MOB
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		}

		val geneHolder = DnaHelixItem.getGeneHolder(itemStack)
		if (geneHolder != null) {
			builder.accept(
				ModTooltipLang.GENE
					.toComponent(geneHolder.getName())
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			builder.accept(
				ModTooltipLang.GENE
					.toComponent(Gene.UNKNOWN_GENE_COMPONENT)
					.withStyle(ChatFormatting.GRAY)
			)
		}
	}

	companion object {
		fun setDetails(
			itemStack: ItemStack,
			entityType: EntityType<*>,
			geneHolder: Holder<Gene>,
		) {
			EntityDnaItem.setEntityType(itemStack, entityType)
			DnaHelixItem.setGeneHolder(itemStack, geneHolder)
		}
	}

}