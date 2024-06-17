package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class DnaHelixItem : EntityDnaItem() {

    companion object {
        private const val GENE_ID_NBT = "GeneId"

        fun ItemStack.hasGene(): Boolean = this.tag?.contains(GENE_ID_NBT) ?: false

        fun ItemStack.getGene(): Gene? {
            val string = this.tag?.getString(GENE_ID_NBT)
            if (string.isNullOrBlank()) return null
            return Gene.fromId(string)
        }

        fun ItemStack.setGene(gene: Gene): ItemStack {
            val tag = this.orCreateTag
            tag.putString(GENE_ID_NBT, gene.id.toString())
            tag.remove(MOB_ID_NBT)
            return this
        }

        fun ItemStack.setBasic(): ItemStack {
            this.setGene(DefaultGenes.basic)
            return this
        }

        fun ItemStack.isBasic(): Boolean = this.getGene() == DefaultGenes.basic
    }

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {
        return if (pStack.hasGene())
            InteractionResult.PASS
        else
            super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand)
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        val gene = pStack.getGene()

        if (gene == null) {
            showNoGeneTooltips(pStack, pTooltipComponents, pLevel)
        } else {
            pTooltipComponents.add(
                Component.translatable("tooltip.geneticsresequenced.gene", gene.nameComponent)
                    .withColor(ChatFormatting.GRAY)
            )
        }
    }

    private fun showNoGeneTooltips(
        pStack: ItemStack,
        pTooltipComponents: MutableList<Component>,
        pLevel: Level?
    ) {
        pTooltipComponents.add(
            Component.translatable(
                "tooltip.geneticsresequenced.gene",
                Gene.unknownGeneComponent
            ).withColor(ChatFormatting.GRAY)
        )

        val entity = getEntityType(pStack)
        if (entity != null) {
            pTooltipComponents.add(
                Component.translatable(
                    "tooltip.geneticsresequenced.helix_entity",
                    entity.description
                )
                    .withColor(ChatFormatting.GRAY)
            )
        }

        try {
            val isCreative = pLevel?.isClientSide == true && ClientUtil.playerIsCreative()
            if (isCreative) {
                val component =
                    Component
                        .translatable("tooltip.geneticsresequenced.dna_item.creative")
                        .withColor(ChatFormatting.GRAY)

                pTooltipComponents.add(component)
            }
        } catch (e: Exception) {
            GeneticsResequenced.LOGGER.error("EntityDnaItem isCreative check failed", e)
        }
    }

}