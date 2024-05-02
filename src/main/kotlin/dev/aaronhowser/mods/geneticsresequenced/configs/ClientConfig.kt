package dev.aaronhowser.mods.geneticsresequenced.configs

import net.minecraftforge.common.ForgeConfigSpec

object ClientConfig {
    private val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    lateinit var woolyRemovesCape: ForgeConfigSpec.BooleanValue

    init {
        generalConfigs()

        SPEC = BUILDER.build()
    }

    private fun generalConfigs() {
        BUILDER.push("general")

        woolyRemovesCape = BUILDER.comment("When a player with the Wooly gene is sheared, their outer skin layers are removed. Enable this to also remove the cape.")
            .define("woolyRemovesCape", true)

        BUILDER.pop()
    }

}