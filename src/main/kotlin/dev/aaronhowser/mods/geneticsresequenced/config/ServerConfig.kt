package dev.aaronhowser.mods.geneticsresequenced.config

import net.minecraftforge.common.ForgeConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
	private val builder: ForgeConfigSpec.Builder
) {

	lateinit var keepGenesOnDeath: ForgeConfigSpec.BooleanValue
	lateinit var minimumCooldownForNotification: ForgeConfigSpec.IntValue
	lateinit var antifieldBlockRadius: ForgeConfigSpec.IntValue

	lateinit var coalGeneratorEnergyCapacity: ForgeConfigSpec.IntValue
	lateinit var coalGeneratorEnergyTransferRate: ForgeConfigSpec.IntValue
	lateinit var coalGeneratorEnergyPerTick: ForgeConfigSpec.IntValue
	lateinit var incubatorTicksPerBrew: ForgeConfigSpec.IntValue
	lateinit var incubatorLowTempTickFactor: ForgeConfigSpec.IntValue
	lateinit var incubatorOverclockerChanceDecrease: ForgeConfigSpec.DoubleValue
	lateinit var incubatorChorusFruitChanceIncrease: ForgeConfigSpec.DoubleValue

	lateinit var disableGivingPlayersNegativeGenes: ForgeConfigSpec.BooleanValue

	lateinit var bioluminescenceCooldown: ForgeConfigSpec.IntValue
	lateinit var bioluminescenceDuration: ForgeConfigSpec.IntValue
	lateinit var clawsChance: ForgeConfigSpec.DoubleValue
	lateinit var clawsDamage: ForgeConfigSpec.DoubleValue
	lateinit var chillChance: ForgeConfigSpec.DoubleValue
	lateinit var chillDuration: ForgeConfigSpec.IntValue
	lateinit var eggCooldown: ForgeConfigSpec.IntValue
	lateinit var emeraldHeartCooldown: ForgeConfigSpec.IntValue
	lateinit var emeraldHeartChatChance: ForgeConfigSpec.DoubleValue
	lateinit var dragonsBreathCooldown: ForgeConfigSpec.IntValue
	lateinit var itemMagnetCooldown: ForgeConfigSpec.IntValue
	lateinit var itemMagnetRadius: ForgeConfigSpec.DoubleValue
	lateinit var johnnyAttackMultiplier: ForgeConfigSpec.DoubleValue
	lateinit var meatyCooldown: ForgeConfigSpec.IntValue
	lateinit var meaty2Cooldown: ForgeConfigSpec.IntValue
	lateinit var milkyCooldown: ForgeConfigSpec.IntValue
	lateinit var mobSightRadius: ForgeConfigSpec.DoubleValue
	lateinit var noHungerCooldown: ForgeConfigSpec.IntValue
	lateinit var noHungerMinimum: ForgeConfigSpec.IntValue
	lateinit var passivesCheckCooldown: ForgeConfigSpec.IntValue
	lateinit var photosynthesisCooldown: ForgeConfigSpec.IntValue
	lateinit var photosynthesisHungerAmount: ForgeConfigSpec.IntValue
	lateinit var photosynthesisSaturationAmount: ForgeConfigSpec.DoubleValue
	lateinit var slimyDeathCooldown: ForgeConfigSpec.IntValue
	lateinit var slimyDeathHealthMultiplier: ForgeConfigSpec.DoubleValue
	lateinit var slimyDeathDespawnCheckTimer: ForgeConfigSpec.IntValue
	lateinit var slimyDeathDespawnTime: ForgeConfigSpec.IntValue
	lateinit var teleportCooldown: ForgeConfigSpec.IntValue
	lateinit var teleportDistance: ForgeConfigSpec.DoubleValue
	lateinit var thornsChance: ForgeConfigSpec.DoubleValue
	lateinit var thornsDamage: ForgeConfigSpec.DoubleValue
	lateinit var thornsHungerDrain: ForgeConfigSpec.DoubleValue
	lateinit var wallClimbSpeed: ForgeConfigSpec.DoubleValue
	lateinit var webDefenseChance: ForgeConfigSpec.DoubleValue
	lateinit var webDefenseDuration: ForgeConfigSpec.IntValue
	lateinit var woolyCooldown: ForgeConfigSpec.IntValue
	lateinit var xpMagnetCooldown: ForgeConfigSpec.IntValue
	lateinit var xpMagnetRadius: ForgeConfigSpec.DoubleValue

	init {
		generalConfigs()
		machineConfigs()
		geneConfigs()

		builder.build()
	}

	private fun generalConfigs() {
		keepGenesOnDeath = builder
			.comment("Keep genes on death")
			.define("keepGenesOnDeath", true)

		minimumCooldownForNotification = builder
			.comment("When a cooldown ends, it notifies the player, unless the cooldown is shorter than this value (in ticks)")
			.defineInRange("minimumCooldownForNotification", 20 * 60, 1, Int.MAX_VALUE)

		antifieldBlockRadius = builder
			.comment("How far should the Antifield Block prevent Item/XP Magnet Genes from working (in blocks)")
			.defineInRange("antifieldBlockRadius", 25, 1, Int.MAX_VALUE)
	}

	private fun machineConfigs() {
		builder.push("machines")

		coalGeneratorEnergyCapacity = builder
			.comment("How much energy should the Coal Generator be able to store")
			.defineInRange("coalGeneratorEnergyCapacity", 100_000, 1, Int.MAX_VALUE)
		coalGeneratorEnergyTransferRate = builder
			.comment("How much energy should the Coal Generator be able to transfer per tick")
			.defineInRange("coalGeneratorEnergyTransferRate", 256, 1, Int.MAX_VALUE)
		coalGeneratorEnergyPerTick = builder
			.comment("How much energy should the Coal Generator generate per tick (1 item takes 200 ticks to burn in a Furnace)")
			.defineInRange("coalGeneratorEnergyPerTick", 6, 1, Int.MAX_VALUE)

		incubatorTicksPerBrew = builder
			.comment("How many ticks should the Incubator take to brew a potion? A vanilla brewing stand takes 400 ticks")
			.defineInRange("incubatorTicksPerBrew", 200, 1, Int.MAX_VALUE)
		incubatorLowTempTickFactor = builder
			.comment("How many times slower should the Advanced Incubator be when it's at low temperature? Default is 120, which makes it take a full Minecraft day (excluding Overclockers)")
			.defineInRange("incubatorLowTempTickFactor", 120, 1, Int.MAX_VALUE)

		incubatorOverclockerChanceDecrease = builder
			.comment("How much should each Overclocker decrease the success rate of GMO recipes? Equation is `decreasedChance = geneChance * (1 - (overclockerCount * incubatorOverclockerChanceDecrease))`")
			.defineInRange("incubatorOverclockerChanceDecrease", 0.1, 0.0, 1.0)

		incubatorChorusFruitChanceIncrease = builder
			.comment("How much should each Chorus Fruit increase the success rate of GMO recipes? Equation is `finalChance = decreasedChance ")
			.defineInRange("incubatorChorusFruitChanceIncrease", 0.1, 0.0, 1.0)

		// most other machines hold 20_000
		// but dispersal is 1_000


		builder.pop()
	}

	private fun geneConfigs() {
		builder.push("genes")

		disableGivingPlayersNegativeGenes = builder
			.comment("Set true to prevent players from being given negative genes")
			.define("disableGivingPlayersNegativeGenes", false)

		bioluminescenceDuration = builder
			.comment("How long should light sources from the Bioluminescence gene last (in ticks)")
			.defineInRange("bioluminescenceDuration", 20 * 30, 1, Int.MAX_VALUE)
		bioluminescenceCooldown = builder
			.comment("How often entities with the Bioluminescence gene should emit light (in ticks)")
			.defineInRange("bioluminescenceCooldown", 10, 1, Int.MAX_VALUE)

		webDefenseDuration = builder
			.comment("How long should webs placed by the Web Defense gene last (in ticks)")
			.defineInRange("webDefenseDuration", 20 * 4, 1, Int.MAX_VALUE)
		webDefenseChance = builder
			.comment("What probability should the Web Defense gene have")
			.defineInRange("webDefenseChance", 0.05, 0.0, 1.0)

		eggCooldown = builder
			.comment("How many ticks to wait before someone with the Lay Egg gene lays an egg again")
			.defineInRange("eggCooldown", 20 * 60 * 5, 1, Int.MAX_VALUE)

		clawsChance = builder
			.comment("What probability should the Claws gene have (doubled if the player has Claws 2 gene)")
			.defineInRange("clawsBaseChance", 0.33, 0.0, 1.0)
		clawsDamage = builder
			.comment("How much damage to deal when the Claws gene procs")
			.defineInRange("clawsDamage", 8.0, 0.0, Double.MAX_VALUE)

		chillChance = builder
			.comment("What probability should the Chill gene have")
			.defineInRange("chillChance", 0.75, 0.0, 1.0)
		chillDuration = builder
			.comment("How long should the Chill gene slow entities down for (in ticks)")
			.defineInRange("chillDuration", 20 * 15, 1, Int.MAX_VALUE)

		emeraldHeartCooldown = builder
			.comment("How many ticks to wait before the Emerald Heart gene can proc again")
			.defineInRange("emeraldHeartCooldown", 20 * 60, 1, Int.MAX_VALUE)
		emeraldHeartChatChance = builder
			.comment("What probability should there be of making a Villager sound when you send a chat message with Emerald Heart?")
			.defineInRange("emeraldHeartChatChance", 0.4, 0.0, 1.0)

		dragonsBreathCooldown = builder
			.comment("How many ticks to wait before someone with the Dragon's Breath gene can breathe fire again")
			.defineInRange("dragonsBreathCooldown", 20 * 5, 1, Int.MAX_VALUE)

		itemMagnetCooldown = builder
			.comment("How often should the Item Magnet gene attract items (in ticks)")
			.defineInRange("itemMagnetCooldown", 10, 1, Int.MAX_VALUE)
		itemMagnetRadius = builder
			.comment("How far should the Item Magnet gene attract items (in blocks)")
			.defineInRange("itemMagnetRadius", 8.0, 1.0, Double.MAX_VALUE)

		johnnyAttackMultiplier = builder
			.comment("How much should the Johnny gene multiply damage by, when using an Axe?")
			.defineInRange("johnnyAttackMultiplier", 1.25, 0.0, Double.MAX_VALUE)

		meatyCooldown = builder
			.comment("How many ticks to wait before someone with the Meaty gene can be sheared again")
			.defineInRange("meatyCooldown", 20 * 60, 1, Int.MAX_VALUE)
		meaty2Cooldown = builder
			.comment("How many ticks to wait before someone with the Meaty 2 gene drops meat again")
			.defineInRange("meaty2Cooldown", 20 * 60 * 5, 1, Int.MAX_VALUE)

		milkyCooldown = builder
			.comment("How many ticks to wait before someone with the Milky gene can be milked again")
			.defineInRange("milkyCooldown", 1, 1, Int.MAX_VALUE)

		mobSightRadius = builder
			.comment("How far from the player should Mob Sight detect entities (in blocks)")
			.defineInRange("mobSightRadius", 32.0, 1.0, Double.MAX_VALUE)
		noHungerCooldown = builder
			.comment("How often should the No Hunger gene check to reset hunger (in ticks)")
			.defineInRange("noHungerCooldown", 20 * 4, 1, Int.MAX_VALUE)
		noHungerMinimum = builder
			.comment("What's the minimum hunger level to reset to")
			.defineInRange("noHungerMinimum", 10, 1, 20)

		passivesCheckCooldown = builder
			.comment("How often should passive genes (potions, Flame, Lay Egg, Meaty 2) be checked for effects (in ticks)")
			.defineInRange("passivesCheckCooldown", 20 * 2, 1, Int.MAX_VALUE)

		photosynthesisCooldown = builder
			.comment("How often entities with the Photosynthesis gene should regain hunger (in ticks)")
			.defineInRange("photosynthesisCooldown", 20 * 30, 1, Int.MAX_VALUE)
		photosynthesisHungerAmount = builder
			.comment("How much hunger to regain when the Photosynthesis gene procs")
			.defineInRange("photosynthesisHungerAmount", 1, 0, Int.MAX_VALUE)
		photosynthesisSaturationAmount = builder
			.comment("How much saturation to regain when the Photosynthesis gene procs")
			.defineInRange("photosynthesisSaturationAmount", 0.5, 0.0, Double.MAX_VALUE)

		slimyDeathCooldown = builder
			.comment("How many ticks to wait before someone with the Slimy Death gene can spawn slimes again")
			.defineInRange("slimyDeathCooldown", 20 * 60 * 10, 1, Int.MAX_VALUE)
		slimyDeathHealthMultiplier = builder
			.comment("How much health should entities who survive via Slimy Death regain upon death, as a percentage of their max health")
			.defineInRange("slimyDeathHealthMultiplier", 0.5, 0.0, 1.0)
		slimyDeathDespawnCheckTimer = builder
			.comment("How often should Support Slimes check if they should despawn? They despawn if there's nothing to attack, or if they aren't near their owner.")
			.defineInRange("slimyDeathDespawnCheckTimer", 20, 1, Int.MAX_VALUE)
		slimyDeathDespawnTime = builder
			.comment("How long should Support Slimes have to remain in valid despawning conditions before they actually despawn?")
			.defineInRange("slimyDeathDespawnTime", 20 * 10, 1, Int.MAX_VALUE)

		teleportCooldown = builder
			.comment("How many ticks to wait before someone with the Teleport gene can teleport again")
			.defineInRange("teleportCooldown", 20, 1, Int.MAX_VALUE)
		teleportDistance = builder
			.comment("How far should the Teleport gene teleport the player, at maximum")
			.defineInRange("teleportDistance", 10.0, 1.0, Double.MAX_VALUE)

		thornsChance = builder
			.comment("What probability should the Thorns gene have")
			.defineInRange("thornsChance", 0.15, 0.0, 1.0)
		thornsDamage = builder
			.comment("How much damage to deal back to the attacker when they hit a target with the Thorns gene")
			.defineInRange("thornsDamage", 1.5, 0.0, Double.MAX_VALUE)
		thornsHungerDrain = builder
			.comment("How much hunger to drain when the Thorns gene deals damage")
			.defineInRange("thornsHungerDrain", 1.0, 0.0, Double.MAX_VALUE)

		wallClimbSpeed = builder
			.comment("How fast should the player climb walls with the Wall Climbing gene")
			.defineInRange("wallClimbSpeed", 0.144, 0.0, Double.MAX_VALUE)

		woolyCooldown = builder
			.comment("How many ticks to wait before someone with the Wooly gene can be sheared again (skipped if they eat grass)")
			.defineInRange("woolyCooldown", 20 * 60, 1, Int.MAX_VALUE)

		xpMagnetCooldown = builder
			.comment("How often should the XP Magnet gene attract XP orbs (in ticks)")
			.defineInRange("xpMagnetCooldown", 10, 1, Int.MAX_VALUE)
		xpMagnetRadius = builder
			.comment("How far should the XP Magnet gene attract XP orbs (in blocks)")
			.defineInRange("xpMagnetRadius", 8.0, 1.0, Double.MAX_VALUE)

		builder.pop()
	}

	companion object {
		private val configPair: Pair<ServerConfig, ForgeConfigSpec> = ForgeConfigSpec.Builder().configure(::ServerConfig)

		val CONFIG: ServerConfig = configPair.left
		val CONFIG_SPEC: ForgeConfigSpec = configPair.right
	}

}