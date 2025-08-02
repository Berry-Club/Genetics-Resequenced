package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class GeneCheckerItem(properties: Properties) : Item(properties) {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val usedStack = player.getItemInHand(usedHand)

		if (!level.isClientSide) {
			val targetEntity = OtherUtil.getLookedAtEntity(player) as? LivingEntity ?: player

			val targetGeneHolders = targetEntity.geneHolders

			val component = if (targetGeneHolders.isEmpty()) {
				if (targetEntity == player) {
					ModMessageLang.GENE_CHECKER_SELF_NO_GENES.toComponent()
				} else {
					ModMessageLang.GENE_CHECKER_TARGET_NO_GENES.toComponent(targetEntity.name)
				}
			} else {
				val genesComponent = OtherUtil.componentList(targetGeneHolders.map { Gene.getNameComponent(it) })

				if (targetEntity == player) {
					ModMessageLang.GENE_CHECKER_SELF_LIST.toComponent(genesComponent)
				} else {
					ModMessageLang.GENE_CHECKER_TARGET_LIST.toComponent(targetEntity.name, genesComponent)
				}
			}

			player.sendSystemMessage(component)
		}

		return InteractionResultHolder.success(usedStack)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}