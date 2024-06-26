package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import org.joml.Vector2i

object ScreenTextures {

    object Backgrounds {
        val BASIC = OtherUtil.modResource("textures/gui/container/basic_machine_bg.png")
        val CELL_ANALYZER = OtherUtil.modResource("textures/gui/container/cell_analyzer.png")
        val COAL_GENERATOR = OtherUtil.modResource("textures/gui/container/coal_generator.png")
        val DNA_DECRYPTOR = OtherUtil.modResource("textures/gui/container/dna_decryptor.png")
        val DNA_EXTRACTOR = OtherUtil.modResource("textures/gui/container/dna_extractor.png")
        val INCUBATOR = OtherUtil.modResource("textures/gui/container/incubator.png")
        val INCUBATOR_ADVANCED = OtherUtil.modResource("textures/gui/container/incubator_advanced.png")
        val PLASMID_INFUSER = OtherUtil.modResource("textures/gui/container/plasmid_infuser.png")
        val PLASMID_INJECTOR = OtherUtil.modResource("textures/gui/container/plasmid_injector.png")

        const val TEXTURE_SIZE = 256
    }

    object Elements {
        object ArrowDown {
            val TEXTURE = OtherUtil.modResource("arrow_down")
            const val TEXTURE_SIZE = 32
        }

        object ArrowRight {
            val TEXTURE = OtherUtil.modResource("arrow_right")
            const val TEXTURE_SIZE = 32
            val POSITION = Vector2i(73, 37)
        }

        object Bubbles {
            val TEXTURE = OtherUtil.modResource("bubbles")
            const val TEXTURE_SIZE = 32
        }

        object Burn {
            val TEXTURE = OtherUtil.modResource("burn")
            const val TEXTURE_SIZE = 16
            val POSITION = Vector2i(54, 54)
        }

        object Energy {
            val TEXTURE = OtherUtil.modResource("energy")
            const val TEXTURE_SIZE = 64
            val DIMENSIONS = Vector2i(18, 57)   // How much of the texture is actually used

            val LOCATION_DEFAULT = Vector2i(7, 8)
            val LOCATION_COAL_GEN = Vector2i(104, 12)
            val LOCATION_INCUBATOR = Vector2i(20, 12)
        }

        object Heat {
            val HIGH = OtherUtil.modResource("heat_high")
            val LOW = OtherUtil.modResource("heat_low")

            const val TEXTURE_SIZE = 32
        }

    }

}