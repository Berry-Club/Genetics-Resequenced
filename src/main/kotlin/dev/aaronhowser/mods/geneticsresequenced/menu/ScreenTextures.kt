package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced

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
		class SpriteSheetFragment(
			val uStart: Int,
			val vStart: Int,
			val width: Int,
			val height: Int
		) {
			val texture = GeneticsResequenced.modResource("textures/gui/sprites.png")
		}

		const val SPRITE_SHEET_SIZE = 64

		val ARROW_DOWN = SpriteSheetFragment(1, 1, 9, 28)
		val ARROW_RIGHT = SpriteSheetFragment(1, 31, 24, 17)
		val BUBBLES = SpriteSheetFragment(11, 1, 12, 29)
		val BURN = SpriteSheetFragment(25, 11, 14, 14)
		val ENERGY = SpriteSheetFragment(45, 1, 18, 57)
		val HEAT_HIGH = SpriteSheetFragment(24, 1, 18, 4)
		val HEAT_LOW = SpriteSheetFragment(24, 6, 18, 4)
	}

}