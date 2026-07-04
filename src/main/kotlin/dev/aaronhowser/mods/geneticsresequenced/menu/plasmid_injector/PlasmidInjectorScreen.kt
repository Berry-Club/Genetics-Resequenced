package dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class PlasmidInjectorScreen(
	menu: PlasmidInjectorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<PlasmidInjectorMenu>(menu, playerInventory, title, ScreenTextures.Backgrounds.PLASMID_INJECTOR)
