package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneDataComponent
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class GmoCell(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		pStack: ItemStack,
		pLevel: Level?,
		pTooltipComponents: MutableList<Component>,
		pIsAdvanced: TooltipFlag
	) {
		val entityType = EntityDnaItem.getEntityType(pStack)
		if (entityType != null) {
			val entityComponent =
				ModTooltipLang.CELL_MOB
					.toComponent(entityType.description)
					.withStyle(ChatFormatting.GRAY)
			pTooltipComponents.add(entityComponent)
		} else {
			val noEntityComponent =
				ModTooltipLang.CELL_NO_MOB
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			pTooltipComponents.add(noEntityComponent)
		}

		val geneRk = GeneDataComponent.getGeneRk(pStack)
		if (geneRk != null) {
			val geneComponent =
				ModTooltipLang.GENE
					.toComponent(geneRk.location())
					.withStyle(ChatFormatting.GRAY)
			pTooltipComponents.add(geneComponent)
		} else {
			val noGeneComponent =
				ModTooltipLang.GENE
					.toComponent(Gene.UNKNOWN_GENE_COMPONENT)
					.withStyle(ChatFormatting.GRAY)
			pTooltipComponents.add(noGeneComponent)
		}
	}

	companion object {
		fun setDetails(
			itemStack: ItemStack,
			entityType: EntityType<*>,
			geneHolder: Holder<Gene>,
		) {
			EntityDnaItem.setEntityType(itemStack, entityType)
			GeneDataComponent.setGene(itemStack, geneHolder)
		}
	}

}