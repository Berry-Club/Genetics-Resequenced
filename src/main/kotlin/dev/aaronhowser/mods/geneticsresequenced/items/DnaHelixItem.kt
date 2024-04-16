package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.ClientHelper
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

object DnaHelixItem : EntityDnaItem() {

    private const val GENE_ID_NBT = "GeneId"
    private const val GENERIC_NBT = "IsGeneric"

    fun hasGene(itemStack: ItemStack): Boolean {
        return itemStack.tag?.contains(GENE_ID_NBT) ?: false
    }

    fun getGene(itemStack: ItemStack): Gene? {
        val string = itemStack.tag?.getString(GENE_ID_NBT)
        if (string.isNullOrBlank()) return null
        return Gene.fromId(string)
    }

    fun setGene(itemStack: ItemStack, gene: Gene): Boolean {
        val tag = itemStack.orCreateTag
        tag.putString(GENE_ID_NBT, gene.id)
        tag.remove(MOB_ID_NBT)
        return true
    }

    fun ItemStack.setGene(gene: Gene?): ItemStack? {
        if (gene == null) {
            setGeneric(this)
            return this
        }
        return if (setGene(this, gene)) {
            this
        } else {
            null
        }
    }

    fun setGeneric(itemStack: ItemStack): Boolean {
        val tag = itemStack.orCreateTag
        tag.remove(GENE_ID_NBT)
        tag.remove(MOB_ID_NBT)
        tag.putBoolean(GENERIC_NBT, true)
        return true
    }

    fun isGeneric(itemStack: ItemStack): Boolean {
        return itemStack.tag?.getBoolean(GENERIC_NBT) ?: false
    }

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {
        return if (hasGene(pStack))
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
        val gene = getGene(pStack)

        if (gene == null) {
            showNoGeneTooltips(pStack, pTooltipComponents, pLevel)
        } else {
            pTooltipComponents.add(Component.literal("Gene: ").append(gene.nameComponent)
                .withStyle { it.withColor(ChatFormatting.GRAY) })
        }
    }

    private fun showNoGeneTooltips(
        pStack: ItemStack,
        pTooltipComponents: MutableList<Component>,
        pLevel: Level?
    ) {

        if (isGeneric(pStack)) {
            pTooltipComponents.add(Component.literal("Gene: Basic Gene")
                .withStyle { it.withColor(ChatFormatting.GRAY) })
            return
        }

        pTooltipComponents.add(Component.literal("Gene: Unknown")
            .withStyle { it.withColor(ChatFormatting.GRAY) })

        val entity = getEntityType(pStack)
        if (entity != null) {
            pTooltipComponents.add(Component.literal("Entity: ").append(entity.description)
                .withStyle { it.withColor(ChatFormatting.GRAY) })
        }

        try {
            val isCreative = pLevel?.isClientSide == true && ClientHelper.playerIsCreative()
            if (isCreative) {
                val component =
                    Component
                        .translatable("tooltip.geneticsresequenced.dna_item.creative")
                        .withStyle { it.withColor(ChatFormatting.GRAY) }

                pTooltipComponents.add(component)
            }
        } catch (e: Exception) {
            GeneticsResequenced.LOGGER.error("EntityDnaItem isCreative check failed", e)
        }
    }

}