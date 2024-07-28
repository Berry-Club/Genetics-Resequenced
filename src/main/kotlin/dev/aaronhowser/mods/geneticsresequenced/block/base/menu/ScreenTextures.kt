package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil

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
                object CoalGen {
                    const val X = 75
                    const val Y = 41
                }

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

        object Burn {
            val TEXTURE = OtherUtil.modResource("burn")
            const val TEXTURE_SIZE = 16

            object Position {
                const val X = 52
                const val Y = 59
            }

            object Dimensions {
                const val WIDTH = 14
                const val HEIGHT = 14
            }
        }

        object Energy {
            val TEXTURE = OtherUtil.modResource("energy")
            const val TEXTURE_SIZE = 64

            // How much of the texture is actually used
            object Dimensions {
                const val WIDTH = 18
                const val HEIGHT = 57
            }

            object Location {
                object Default {
                    const val X = 7
                    const val Y = 14
                }

                object CoalGen {
                    const val X = 104
                    const val Y = 18
                }

                object Incubator {
                    const val X = 20
                    const val Y = 18
                }
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