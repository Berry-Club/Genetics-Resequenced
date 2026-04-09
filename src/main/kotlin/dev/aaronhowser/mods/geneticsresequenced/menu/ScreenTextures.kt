package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.menu.textures.ScreenSprite
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil

object ScreenTextures {

	object Backgrounds {
		private fun background(path: String, width: Int, height: Int): ScreenBackground =
			ScreenBackground(GeneticsResequenced.modResource(path), width, height)

		val BASIC = background("textures/gui/container/basic_machine_bg.png", 176, 172)
		val CELL_ANALYZER = background("textures/gui/container/cell_analyzer.png", 176, 172)
		val COAL_GENERATOR = background("textures/gui/container/coal_generator.png", 176, 172)
		val DNA_DECRYPTOR = background("textures/gui/container/dna_decryptor.png", 176, 172)
		val DNA_EXTRACTOR = background("textures/gui/container/dna_extractor.png", 176, 172)
		val INCUBATOR = background("textures/gui/container/incubator.png", 176, 172)
		val INCUBATOR_ADVANCED = background("textures/gui/container/incubator_advanced.png", 176, 172)
		val PLASMID_INFUSER = background("textures/gui/container/plasmid_infuser.png", 176, 172)
		val PLASMID_INJECTOR = background("textures/gui/container/plasmid_injector.png", 176, 172)
	}

	object Sprites {
		fun sprite(path: String, width: Int, height: Int): ScreenSprite =
			ScreenSprite(GeneticsResequenced.modResource(path), width, height)

		val ARROW_DOWN = sprite("textures/gui/sprite/arrow_down.png", 9, 28)
		val ARROW_RIGHT = sprite("textures/gui/sprite/arrow_right.png", 24, 17)
		val BUBBLES = sprite("textures/gui/sprite/bubbles.png", 11, 29)
		val BURN = sprite("textures/gui/sprite/burn.png", 14, 14)
		val ENERGY = sprite("textures/gui/sprite/energy.png", 18, 57)
		val HEAT_HIGH = sprite("textures/gui/sprite/heat_high.png", 18, 4)
		val HEAT_LOW = sprite("textures/gui/sprite/heat_low.png", 18, 4)
	}

}