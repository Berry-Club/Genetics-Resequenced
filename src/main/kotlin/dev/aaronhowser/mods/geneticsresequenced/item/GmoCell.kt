package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
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
		stack: ItemStack,
		context: TooltipContext,
		tooltipDisplay: TooltipDisplay,
		components: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val entityType = EntityDnaItem.getEntityType(stack)
		if (entityType != null) {
			components.accept(
				ModTooltipLang.CELL_MOB
					.toComponent(entityType.description)
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			components.accept(
				ModTooltipLang.CELL_NO_MOB
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		}

		val geneHolder = DnaHelixItem.getGeneHolder(stack)
		if (geneHolder != null) {
			components.accept(
				ModTooltipLang.GENE
					.toComponent(geneHolder.getName())
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			components.accept(
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
