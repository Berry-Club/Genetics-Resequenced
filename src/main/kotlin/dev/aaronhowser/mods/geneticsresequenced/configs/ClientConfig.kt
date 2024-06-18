package dev.aaronhowser.mods.geneticsresequenced.configs

import net.minecraftforge.common.ForgeConfigSpec

object ClientConfig {
    private val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    lateinit var woolyRemovesCape: ForgeConfigSpec.BooleanValue
    lateinit var disableParrotNarrator: ForgeConfigSpec.BooleanValue
    lateinit var disableCringeLangChange: ForgeConfigSpec.BooleanValue
    lateinit var supportSlimeRenderDebug: ForgeConfigSpec.BooleanValue

    init {
        generalConfigs()

        SPEC = BUILDER.build()
    }

    private fun generalConfigs() {
        BUILDER.push("general")

        woolyRemovesCape = BUILDER
            .comment("When a player with the Wooly gene is sheared, their outer skin layers are removed. Enable this to also remove the cape.")
            .define("woolyRemovesCape", true)

        disableParrotNarrator = BUILDER
            .comment("Disables the narrator when a player with the Parrot gene speaks.")
            .define("disableParrotNarrator", false)

        disableCringeLangChange = BUILDER
            .comment("Disables the change to LOLCAT language when you get the Cringe gene. This comes with a resource-reload, which may cause lag.")
            .define("disableCringeLangChange", false)

        supportSlimeRenderDebug = BUILDER
            .comment("Enable to render the base Slime model for Support Slime entities.")
            .define("supportSlimeRenderDebug", false)

        BUILDER.pop()
    }

}