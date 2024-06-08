package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AntiPlasmidItem : Item(Properties().tab(ModItems.MOD_TAB)) {

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        val gene = pStack.getGene()

        if (gene == null) {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.anti_plasmid.empty")
                    .withColor(ChatFormatting.GRAY)
            )
        } else {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.plasmid.gene", gene.nameComponent)
                    .withColor(ChatFormatting.GRAY)
            )
        }
    }

}