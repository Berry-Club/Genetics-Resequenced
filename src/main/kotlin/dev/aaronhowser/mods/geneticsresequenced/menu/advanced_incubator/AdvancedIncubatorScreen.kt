package dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.menu.components.Bubbles
import dev.aaronhowser.mods.geneticsresequenced.menu.components.ProgressArrow
import dev.aaronhowser.mods.geneticsresequenced.menu.components.TemperatureIndicator
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class AdvancedIncubatorScreen(
	menu: AdvancedIncubatorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<AdvancedIncubatorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = ScreenTextures.Backgrounds.INCUBATOR_ADVANCED

	private lateinit var temperatureIndicator: TemperatureIndicator
	private lateinit var bubbles: Bubbles

	override val energyPosLeft: Int = 20
	override val energyPosTop: Int = 18

	override val arrowDirection: ProgressArrow.ArrowDirection = ProgressArrow.ArrowDirection.DOWN
	override val arrowPosLeft: Int = 101
	override val arrowPosTop: Int = 20

	override fun baseInit() {
		super.baseInit()

		temperatureIndicator = TemperatureIndicator(
			x = leftPos + ScreenTextures.Elements.Heat.Position.X,
			y = topPos + ScreenTextures.Elements.Heat.Position.Y,
			font = font,
			shouldRender = { menu.getCurrentEnergy() != 0 },
			shouldRenderTooltip = false,
			isHighTemperature = { true },
			onClickFunction = { _, _, _ ->

			}
		)

		bubbles = Bubbles(
			x = leftPos + ScreenTextures.Elements.Bubbles.Position.X,
			y = topPos + ScreenTextures.Elements.Bubbles.Position.Y,
			shouldRender = { menu.isCrafting() },
			highTemperature = { true }
		)

		addRenderableWidget(temperatureIndicator)
		addRenderableWidget(bubbles)
	}

}