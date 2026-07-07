package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toGrayComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
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
					.toGrayComponent(entityType.description)
			)
		} else {
			components.accept(
				ModTooltipLang.CELL_NO_MOB.toGrayComponent()
			)
		}

		val geneHolder = DnaHelixItem.getGeneHolder(stack)
		if (geneHolder != null) {
			components.accept(
				ModTooltipLang.GENE
					.toGrayComponent(geneHolder.getName())
			)
		} else {
			components.accept(
				ModTooltipLang.GENE
					.toGrayComponent(Gene.UNKNOWN_GENE_COMPONENT)
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
