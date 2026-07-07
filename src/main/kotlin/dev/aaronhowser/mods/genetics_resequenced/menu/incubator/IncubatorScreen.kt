package dev.aaronhowser.mods.genetics_resequenced.menu.incubator

import dev.aaronhowser.mods.genetics_resequenced.menu.MachineScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.components.Bubbles
import dev.aaronhowser.mods.genetics_resequenced.menu.components.ProgressArrow
import dev.aaronhowser.mods.genetics_resequenced.menu.components.TemperatureIndicator
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class IncubatorScreen(
	menu: IncubatorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<IncubatorMenu>(menu, playerInventory, title, INCUBATOR_BACKGROUND) {

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
			x = leftPos + TemperatureIndicator.X,
			y = topPos + TemperatureIndicator.Y,
			font = font,
			shouldRender = { menu.getCurrentEnergy() != 0 },
			shouldRenderTooltip = false,
			isHighTemperature = { true },
			onClickFunction = { _, _, _ -> }
		)

		bubbles = Bubbles(
			x = leftPos + Bubbles.X,
			y = topPos + Bubbles.Y,
			shouldRender = { menu.isCrafting() },
			highTemperature = { true }
		)

		addRenderableWidget(temperatureIndicator)
		addRenderableWidget(bubbles)
	}

	companion object {
		const val FAST_BUBBLE_SPEED = 12
	}

}
