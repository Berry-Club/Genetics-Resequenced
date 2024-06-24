package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import org.joml.Vector2i

object ScreenTextures {

    object Backgrounds {
        val AIR_DISPERSAL = OtherUtil.modResource("textures/gui/container/air_dispersal.png")
        val BASIC = OtherUtil.modResource("textures/gui/container/basic_machine_bg.png")
        val CELL_ANALYZER = OtherUtil.modResource("textures/gui/container/cell_analyzer.png")
        val COAL_GENERATOR = OtherUtil.modResource("textures/gui/container/coal_generator.png")
        val DNA_DECRYPTOR = OtherUtil.modResource("textures/gui/container/dna_decryptor.png")
        val DNA_EXTRACTOR = OtherUtil.modResource("textures/gui/container/dna_extractor.png")
        val INCUBATOR = OtherUtil.modResource("textures/gui/container/incubator.png")
        val INCUBATOR_ADVANCED = OtherUtil.modResource("textures/gui/container/incubator_advanced.png")
        val PLASMID_INFUSER = OtherUtil.modResource("textures/gui/container/plasmid_infuser.png")
        val PLASMID_INJECTOR = OtherUtil.modResource("textures/gui/container/plasmid_injector.png")

        val DIMENSIONS = Pair(166, 176)
    }

    object Elements {
        object ArrowDown {
            val TEXTURE = OtherUtil.modResource("arrow_down")
            val DIMENSIONS = Vector2i(28, 9)
        }

        object ArrowRight {
            val TEXTURE = OtherUtil.modResource("arrow_right")
            val DIMENSIONS = Vector2i(24, 17)
        }

        object Bubbles {
            val TEXTURE = OtherUtil.modResource("bubbles")
            val DIMENSIONS = Vector2i(12, 29)
        }

        object Burn {
            val TEXTURE = OtherUtil.modResource("burn")
            val DIMENSIONS = Vector2i(14, 14)
        }

        object Energy {
            val TEXTURE = OtherUtil.modResource("energy")
            val DIMENSIONS = Vector2i(14, 42)
        }

        object Heat {
            val HIGH = OtherUtil.modResource("heat_high")
            val LOW = OtherUtil.modResource("heat_low")

            val DIMENSIONS = Vector2i(18, 4)
        }

    }

}