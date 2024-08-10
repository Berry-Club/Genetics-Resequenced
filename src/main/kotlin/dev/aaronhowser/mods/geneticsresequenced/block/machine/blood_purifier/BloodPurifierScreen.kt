package dev.aaronhowser.mods.geneticsresequenced.block.machine.blood_purifier

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class BloodPurifierScreen(
    pMenu: BloodPurifierMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<BloodPurifierMenu>(pMenu, pPlayerInventory, pTitle) {

    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.BASIC

    override fun progressArrowAmountToRender(): Int = menu.getScaledProgress()
    override fun shouldRenderProgressArrow() = menu.isCrafting

}