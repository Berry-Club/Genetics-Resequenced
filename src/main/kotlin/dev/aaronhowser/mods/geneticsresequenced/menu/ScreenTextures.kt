package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.menu.textures.ScreenSprite
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil

object ScreenTextures {

	object Backgrounds {
		private fun background(path: String, width: Int, height: Int): ScreenBackground =
			ScreenBackground(OtherUtil.modResource(path), width, height)

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
			ScreenSprite(OtherUtil.modResource(path), width, height)

		val ARROW_DOWN = sprite("textures/gui/sprite/arrow_down.png", 9, 28)
		val ARROW_RIGHT = sprite("textures/gui/sprite/arrow_right.png", 24, 17)
		val BUBBLES = sprite("textures/gui/sprite/bubbles.png", 11, 29)
		val BURN = sprite("textures/gui/sprite/burn.png", 14, 14)
		val ENERGY = sprite("textures/gui/sprite/energy.png", 18, 57)
		val HEAT_HIGH = sprite("textures/gui/sprite/heat_high.png", 18, 4)
		val HEAT_LOW = sprite("textures/gui/sprite/heat_low.png", 18, 4)
	}

	object Elements {
		object ArrowDown {
			val TEXTURE = OtherUtil.modResource("arrow_down")
			const val TEXTURE_SIZE = 32

			object Dimensions {
				const val WIDTH = 9
				const val HEIGHT = 28
			}

			object Position {
				const val X = 101
				const val Y = 20
			}
		}

		object ArrowRight {
			val TEXTURE = OtherUtil.modResource("arrow_right")
			const val TEXTURE_SIZE = 32

			object Dimensions {
				const val WIDTH = 24
				const val HEIGHT = 17
			}

			object Position {
				object Default {
					const val X = 83
					const val Y = 43
				}
			}

		}

		object Bubbles {
			val TEXTURE = OtherUtil.modResource("bubbles")
			const val TEXTURE_SIZE = 32

			object Position {
				const val X = 67
				const val Y = 18
			}

			object Dimensions {
				const val WIDTH = 11
				const val HEIGHT = 29
			}
		}

		object Heat {
			object Texture {
				val HIGH = OtherUtil.modResource("heat_high")
				val LOW = OtherUtil.modResource("heat_low")
			}

			const val TEXTURE_SIZE = 32

			object Dimensions {
				const val WIDTH = 18
				const val HEIGHT = 4
			}

			object Position {
				const val X = 64
				const val Y = 48
			}
		}

	}

}