package dev.aaronhowser.mods.geneticsresequenced.block_old.machine.plasmid_infuser

import dev.aaronhowser.mods.geneticsresequenced.block_old.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block_old.base.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class PlasmidInfuserScreen(
	pMenu: PlasmidInfuserMenu,
	pPlayerInventory: Inventory,
	pTitle: Component
) : MachineScreen<PlasmidInfuserMenu>(pMenu, pPlayerInventory, pTitle) {
	override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.PLASMID_INFUSER

	override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting
}