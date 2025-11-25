package dev.aaronhowser.mods.geneticsresequenced.config

import net.minecraftforge.common.ForgeConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ClientConfig(
	private val builder: ForgeConfigSpec.Builder
) {

	lateinit var woolyRemovesCape: ForgeConfigSpec.BooleanValue
	lateinit var disableParrotNarrator: ForgeConfigSpec.BooleanValue
	lateinit var disableCringeLangChange: ForgeConfigSpec.BooleanValue
	lateinit var supportSlimeRenderDebug: ForgeConfigSpec.BooleanValue
	lateinit var itemMagnetBlacklistTooltip: ForgeConfigSpec.BooleanValue

	init {
		generalConfigs()

		builder.build()
	}

	private fun generalConfigs() {
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
	}

	companion object {
		private val configPair: Pair<ClientConfig, ForgeConfigSpec> = ForgeConfigSpec.Builder().configure(::ClientConfig)

		val CONFIG: ClientConfig = configPair.left
		val CONFIG_SPEC: ForgeConfigSpec = configPair.right
	}

}