package dev.aaronhowser.mods.geneticsresequenced.block_old.machine.dna_decryptor

import dev.aaronhowser.mods.geneticsresequenced.block_old.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class DnaDecryptorScreen(
	pMenu: DnaDecryptorMenu,
	pPlayerInventory: Inventory,
	pTitle: Component
) : MachineScreen<DnaDecryptorMenu>(pMenu, pPlayerInventory, pTitle) {
	override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.DNA_DECRYPTOR

	override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting
}