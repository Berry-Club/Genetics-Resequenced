package dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser

import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class PlasmidInfuserScreen(
	menu: PlasmidInfuserMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<PlasmidInfuserMenu>(menu, playerInventory, title, ScreenTextures.Backgrounds.PLASMID_INFUSER)
