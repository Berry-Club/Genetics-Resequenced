package dev.aaronhowser.mods.geneticsresequenced.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ClientConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {
        private val configPair: Pair<ClientConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ClientConfig)

        val CONFIG: ClientConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var woolyRemovesCape: ModConfigSpec.BooleanValue
        lateinit var disableParrotNarrator: ModConfigSpec.BooleanValue
        lateinit var disableCringeLangChange: ModConfigSpec.BooleanValue
        lateinit var supportSlimeRenderDebug: ModConfigSpec.BooleanValue
        lateinit var itemMagnetBlacklistTooltip: ModConfigSpec.BooleanValue
    }

    init {
        generalConfigs()

        builder.build()
    }

    private fun generalConfigs() {
        builder.push("general")

        woolyRemovesCape = builder
            .comment("When a player with the Wooly gene is sheared, their outer skin layers are removed. Enable this to also remove the cape.")
            .define("woolyRemovesCape", true)

        disableParrotNarrator = builder
            .comment("Disables the narrator when a player with the Parrot gene speaks.")
            .define("disableParrotNarrator", false)

        disableCringeLangChange = builder
            .comment("Disables the change to LOLCAT language when you get the Cringe gene. This comes with a resource-reload, which may cause lag.")
            .define("disableCringeLangChange", false)

        supportSlimeRenderDebug = builder
            .comment("Enable to render the base Slime model for Support Slime entities.")
            .define("supportSlimeRenderDebug", false)

        itemMagnetBlacklistTooltip = builder
            .comment("Show that an item is in the Item Magnet's blacklist in its tooltip.")
            .define("itemMagnetBlacklistTooltip", true)

        builder.pop()
    }

}