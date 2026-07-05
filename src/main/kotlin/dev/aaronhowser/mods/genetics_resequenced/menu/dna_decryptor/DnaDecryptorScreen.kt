package dev.aaronhowser.mods.genetics_resequenced.menu.dna_decryptor

import dev.aaronhowser.mods.genetics_resequenced.menu.MachineScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class DnaDecryptorScreen(
	menu: DnaDecryptorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<DnaDecryptorMenu>(menu, playerInventory, title, ScreenTextures.Backgrounds.DNA_DECRYPTOR)
