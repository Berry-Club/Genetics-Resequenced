package dev.aaronhowser.mods.geneticsresequenced.block_old.machine.cell_analyzer

import dev.aaronhowser.mods.geneticsresequenced.block_old.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block_old.base.menu.ScreenTextures
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
}