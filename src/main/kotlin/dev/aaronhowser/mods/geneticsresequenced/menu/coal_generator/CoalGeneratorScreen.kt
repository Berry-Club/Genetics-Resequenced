package dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.menu.components.GeneratorBurn
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class CoalGeneratorScreen(
	menu: CoalGeneratorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<CoalGeneratorMenu>(menu, playerInventory, title, ScreenTextures.Backgrounds.COAL_GENERATOR) {

	private lateinit var generatorBurn: GeneratorBurn

	override val energyPosLeft: Int = 104
	override val energyPosTop: Int = 18

	override val arrowPosLeft: Int = 75
	override val arrowPosTop: Int = 41

	override fun shouldRenderProgressArrow(): Boolean = menu.isBurning()

	override fun baseInit() {
		super.baseInit()

		generatorBurn = GeneratorBurn(
			x = leftPos + GeneratorBurn.X,
			y = topPos + GeneratorBurn.Y,
			shouldRender = { menu.isBurning() },
			percentDone = { menu.getPercentDone() }
		)

		addRenderableWidget(generatorBurn)
	}

}
