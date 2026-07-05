package dev.aaronhowser.mods.genetics_resequenced.menu.cell_analyzer

import dev.aaronhowser.mods.genetics_resequenced.menu.MachineScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class CellAnalyzerScreen(
	menu: CellAnalyzerMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<CellAnalyzerMenu>(menu, playerInventory, title, ScreenTextures.Backgrounds.CELL_ANALYZER) {

	init {
		topPos -= 8
	}

}
