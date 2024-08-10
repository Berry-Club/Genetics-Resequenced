package dev.aaronhowser.mods.geneticsresequenced.compatibility

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin

object ModCompatibilityHandler {

    fun openEmuMenu(screen: MachineScreen<*>) {
        ModEmiPlugin.displayRecipeCategory(screen)
    }

}