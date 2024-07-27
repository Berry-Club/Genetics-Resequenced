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

    override val energyX: Int = ScreenTextures.Elements.Energy.Location.Default.X
    override val energyY: Int = ScreenTextures.Elements.Energy.Location.Default.Y

    override val arrowX = ScreenTextures.Elements.ArrowRight.Position.Blood.X
    override val arrowY = ScreenTextures.Elements.ArrowRight.Position.Blood.Y
    override fun shouldRenderProgressArrow() = menu.isCrafting
    override fun progressArrowX() = menu.getScaledProgress()
    override fun progressArrowY() = ScreenTextures.Elements.ArrowRight.Dimensions.HEIGHT

}