package dev.aaronhowser.mods.geneticsresequenced.block.machine.cell_analyzer

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class CellAnalyzerScreen(
    pMenu: CellAnalyzerMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<CellAnalyzerMenu>(pMenu, pPlayerInventory, pTitle) {
    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.CELL_ANALYZER

    override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting
    override fun progressArrowWidth(): Int = menu.getScaledProgress()
    override fun progressArrowHeight(): Int = ScreenTextures.Elements.ArrowRight.Dimensions.HEIGHT
}