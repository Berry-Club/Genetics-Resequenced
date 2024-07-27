package dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import net.minecraft.client.Minecraft
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
    override val backgroundSize: Int = ScreenTextures.Backgrounds.TEXTURE_SIZE

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY)

        renderProgressArrow(pGuiGraphics, leftPos, topPos)
        renderBurnProgress(pGuiGraphics, leftPos, topPos)
    }

    override val energyX: Int = ScreenTextures.Elements.Energy.Location.CoalGen.X
    override val energyY: Int = ScreenTextures.Elements.Energy.Location.CoalGen.Y

    private fun renderProgressArrow(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isBurning) return

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.ArrowRight.TEXTURE,
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE,
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.ArrowRight.Position.CoalGen.X,
            y + ScreenTextures.Elements.ArrowRight.Position.CoalGen.Y,
            menu.getScaledProgressArrow(),
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE
        )
    }

    private fun renderBurnProgress(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isBurning) return

        val fuelRemaining = menu.getScaledFuelRemaining()

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Burn.TEXTURE,
            ScreenTextures.Elements.Burn.TEXTURE_SIZE,
            ScreenTextures.Elements.Burn.TEXTURE_SIZE,
            0,
            14 - fuelRemaining,
            x + ScreenTextures.Elements.Burn.Position.X,
            y + ScreenTextures.Elements.Burn.Position.Y + ScreenTextures.Elements.Burn.HEIGHT - fuelRemaining,
            ScreenTextures.Elements.Burn.TEXTURE_SIZE,
            fuelRemaining
        )

    }

}