package dev.aaronhowser.mods.genetics_resequenced.menu.advanced_incubator

import dev.aaronhowser.mods.aaron.menu.components.ChangingTextButton
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.menu.MachineScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.components.Bubbles
import dev.aaronhowser.mods.genetics_resequenced.menu.components.ProgressArrow
import dev.aaronhowser.mods.genetics_resequenced.menu.components.TemperatureIndicator
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class AdvancedIncubatorScreen(
	menu: AdvancedIncubatorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<AdvancedIncubatorMenu>(menu, playerInventory, title, ADVANCED_INCUBATOR_BACKGROUND) {

	private lateinit var temperatureIndicator: TemperatureIndicator
	private lateinit var bubbles: Bubbles
	private lateinit var changeTemperatureButton: Button

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
			isHighTemperature = { menu.isHighTemperature() },
			onClickFunction = { _, _, _ ->
				val packet = ClientClickedMenuButton(AdvancedIncubatorMenu.CYCLE_TEMPERATURE_BUTTON_ID)
				packet.messageServer()
			}
		)

		bubbles = Bubbles(
			x = leftPos + Bubbles.X,
			y = topPos + Bubbles.Y,
			shouldRender = { menu.isCrafting() },
			highTemperature = { true }
		)

		changeTemperatureButton = ChangingTextButton(
			x = rightPos - 60 - 5,
			y = topPos + 5,
			width = 60,
			height = 18,
			messageGetter = {
				if (menu.isHighTemperature()) {
					ModMessageLang.ADVANCED_INCUBATOR_HIGH_TEMP.toComponent()
				} else {
					ModMessageLang.ADVANCED_INCUBATOR_LOW_TEMP.toComponent()
				}
			},
			onPress = {
				val packet = ClientClickedMenuButton(AdvancedIncubatorMenu.CYCLE_TEMPERATURE_BUTTON_ID)
				packet.messageServer()
			}
		)

		addRenderableWidget(temperatureIndicator)
		addRenderableWidget(bubbles)
		addRenderableWidget(changeTemperatureButton)
	}

	companion object {
		const val SLOW_BUBBLE_SPEED = 12 * 3
	}

}
