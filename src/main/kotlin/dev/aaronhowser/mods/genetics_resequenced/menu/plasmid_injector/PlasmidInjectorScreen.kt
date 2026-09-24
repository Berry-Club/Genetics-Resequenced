package dev.aaronhowser.mods.genetics_resequenced.menu.plasmid_injector

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.genetics_resequenced.menu.MachineScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class PlasmidInjectorScreen(
	menu: PlasmidInjectorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<PlasmidInjectorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = ScreenTextures.Backgrounds.PLASMID_INJECTOR

}