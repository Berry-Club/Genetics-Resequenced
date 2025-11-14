package dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.menu.components.GeneratorBurn
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class CoalGeneratorScreen(
	menu: CoalGeneratorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<CoalGeneratorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = ScreenTextures.Backgrounds.COAL_GENERATOR

	private lateinit var generatorBurn: GeneratorBurn

	override fun baseInit() {
		super.baseInit()

		generatorBurn = GeneratorBurn(
			x = guiLeft + ScreenTextures.Elements.Burn.Position.X,
			y = guiTop + ScreenTextures.Elements.Burn.Position.Y,
			shouldRender = { menu.isBurning() },
			percentDone = { menu.getPercentDone() }
		)

		addRenderableWidget(generatorBurn)
	}

}