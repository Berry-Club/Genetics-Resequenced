package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.Bubbles
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.ProgressArrow
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.TemperatureIndicator
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class IncubatorScreen(
    pMenu: IncubatorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<IncubatorMenu>(pMenu, pPlayerInventory, pTitle) {

    companion object {
        const val FAST_BUBBLE_SPEED = 12
    }

    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.INCUBATOR

    private lateinit var temperatureIndicator: TemperatureIndicator
    private lateinit var bubbles: Bubbles

    override fun init() {
        super.init()

        temperatureIndicator = TemperatureIndicator(
            x = leftPos + ScreenTextures.Elements.Heat.Position.X,
            y = topPos + ScreenTextures.Elements.Heat.Position.Y,
            font = font,
            shouldRender = { menu.blockEntity.energyStorage.energyStored != 0 },
            shouldRenderTooltip = false,
            isHighTemperature = { true },
            onClickFunction = { _, _, _ -> }
        )

        bubbles = Bubbles(
            x = leftPos + ScreenTextures.Elements.Bubbles.Position.X,
            y = topPos + ScreenTextures.Elements.Bubbles.Position.Y,
            shouldRender = { menu.isCrafting }
        )

        addRenderableWidget(temperatureIndicator)
        addRenderableWidget(bubbles)
    }

    override val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.Incubator.X
    override val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.Incubator.Y

    override val arrowDirection: ProgressArrow.ArrowDirection = ProgressArrow.ArrowDirection.DOWN
    override val arrowLeftPos: Int = ScreenTextures.Elements.ArrowDown.Position.X
    override val arrowTopPos: Int = ScreenTextures.Elements.ArrowDown.Position.Y

    override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting

}