package dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class CoalGeneratorScreen(
    pMenu: CoalGeneratorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<CoalGeneratorMenu>(pMenu, pPlayerInventory, pTitle) {

    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.COAL_GENERATOR

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY)

        renderBurnProgress(pGuiGraphics, leftPos, topPos)
    }

    override val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.CoalGen.X
    override val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.CoalGen.Y

    override val arrowLeftPos = ScreenTextures.Elements.ArrowRight.Position.CoalGen.X
    override val arrowTopPos = ScreenTextures.Elements.ArrowRight.Position.CoalGen.Y

    override fun shouldRenderProgressArrow(): Boolean = menu.isBurning

    private fun renderBurnProgress(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isBurning) return

        val fuelRemaining = menu.getScaledFuelRemaining()

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Burn.TEXTURE,
            ScreenTextures.Elements.Burn.TEXTURE_SIZE,
            ScreenTextures.Elements.Burn.TEXTURE_SIZE,
            0,
            ScreenTextures.Elements.Burn.Dimensions.HEIGHT - fuelRemaining,
            x + ScreenTextures.Elements.Burn.Position.X,
            y + ScreenTextures.Elements.Burn.Position.Y + ScreenTextures.Elements.Burn.Dimensions.HEIGHT - fuelRemaining,
            ScreenTextures.Elements.Burn.TEXTURE_SIZE,
            fuelRemaining
        )

    }

}