package dev.aaronhowser.mods.genetics_resequenced.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ClientConfig(
	private val builder: ModConfigSpec.Builder
) {

	lateinit var woolyRemovesCape: ModConfigSpec.BooleanValue
	lateinit var disableParrotNarrator: ModConfigSpec.BooleanValue
	lateinit var supportSlimeRenderDebug: ModConfigSpec.BooleanValue
	lateinit var itemMagnetBlacklistTooltip: ModConfigSpec.BooleanValue

	lateinit var disableCringeLangChange: ModConfigSpec.BooleanValue
	lateinit var doesCringeShake: ModConfigSpec.BooleanValue
	lateinit var cringeShakeAmplitude: ModConfigSpec.DoubleValue
	lateinit var cringeShakeSpeed: ModConfigSpec.DoubleValue

	init {
		generalConfigs()
		cringeConfigs()

		builder.build()
	}

	private fun generalConfigs() {
		woolyRemovesCape = builder
			.comment("When a player with the Wooly gene is sheared, their outer skin layers are removed. Enable this to also remove the cape.")
			.define("woolyRemovesCape", true)

		disableParrotNarrator = builder
			.comment("Disables the narrator when a player with the Parrot gene speaks.")
			.define("disableParrotNarrator", false)

		supportSlimeRenderDebug = builder
			.comment("Enable to render the base Slime model for Support Slime entities.")
			.define("supportSlimeRenderDebug", false)

		itemMagnetBlacklistTooltip = builder
			.comment("Show that an item is in the Item Magnet's blacklist in its tooltip.")
			.define("itemMagnetBlacklistTooltip", true)
	}

	private fun cringeConfigs() {
		builder.push("cringe")

		disableCringeLangChange = builder
			.comment("Disables the change to LOLCAT language when you get the Cringe gene. This comes with a resource-reload, which may cause lag.")
			.define("disableCringeLangChange", false)

		doesCringeShake = builder
			.comment("Enable or disable the shaking effect caused by the Cringe gene.")
			.define("doesCringeShake", true)

		cringeShakeAmplitude = builder
			.comment("Sets the amplitude of the shaking effect caused by the Cringe gene.")
			.defineInRange("cringeShakeAmplitude", 0.03, 0.0, Double.MAX_VALUE)

		cringeShakeSpeed = builder
			.comment("Sets the speed of the shaking effect caused by the Cringe gene.")
			.defineInRange("cringeShakeSpeed", 5.0, 0.0, Double.MAX_VALUE)

		builder.pop()
	}

	companion object {
		private val configPair: Pair<ClientConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ClientConfig)

		val CONFIG: ClientConfig = configPair.left
		val CONFIG_SPEC: ModConfigSpec = configPair.right
	}

}