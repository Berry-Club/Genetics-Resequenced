package dev.aaronhowser.mods.geneticsresequenced.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {
        private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

        val CONFIG: ServerConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var keepGenesOnDeath: ModConfigSpec.BooleanValue
        lateinit var minimumCooldownForNotification: ModConfigSpec.IntValue
        lateinit var antifieldBlockRadius: ModConfigSpec.IntValue

        lateinit var coalGeneratorEnergyCapacity: ModConfigSpec.IntValue
        lateinit var coalGeneratorEnergyTransferRate: ModConfigSpec.IntValue
        lateinit var coalGeneratorEnergyPerTick: ModConfigSpec.IntValue
        lateinit var incubatorTicksPerBrew: ModConfigSpec.IntValue
        lateinit var incubatorLowTempTickFactor: ModConfigSpec.IntValue

        lateinit var disabledGenes: ModConfigSpec.ConfigValue<List<String>>
        lateinit var disableGivingPlayersNegativeGenes: ModConfigSpec.BooleanValue

        lateinit var bioluminescenceCooldown: ModConfigSpec.IntValue
        lateinit var bioluminescenceDuration: ModConfigSpec.IntValue
        lateinit var clawsChance: ModConfigSpec.DoubleValue
        lateinit var clawsDamage: ModConfigSpec.DoubleValue
        lateinit var chillChance: ModConfigSpec.DoubleValue
        lateinit var chillDuration: ModConfigSpec.IntValue
        lateinit var eggCooldown: ModConfigSpec.IntValue
        lateinit var emeraldHeartCooldown: ModConfigSpec.IntValue
        lateinit var emeraldHeartChatChance: ModConfigSpec.DoubleValue
        lateinit var dragonsBreathCooldown: ModConfigSpec.IntValue
        lateinit var itemMagnetCooldown: ModConfigSpec.IntValue
        lateinit var itemMagnetRadius: ModConfigSpec.DoubleValue
        lateinit var johnnyAttackMultiplier: ModConfigSpec.DoubleValue
        lateinit var knockbackStrength: ModConfigSpec.DoubleValue
        lateinit var meatyCooldown: ModConfigSpec.IntValue
        lateinit var meaty2Cooldown: ModConfigSpec.IntValue
        lateinit var milkyCooldown: ModConfigSpec.IntValue
        lateinit var mobSightCooldown: ModConfigSpec.IntValue
        lateinit var mobSightRadius: ModConfigSpec.DoubleValue
        lateinit var noHungerCooldown: ModConfigSpec.IntValue
        lateinit var noHungerMinimum: ModConfigSpec.IntValue
        lateinit var passivesCheckCooldown: ModConfigSpec.IntValue
        lateinit var photosynthesisCooldown: ModConfigSpec.IntValue
        lateinit var photoSynthesisHungerAmount: ModConfigSpec.IntValue
        lateinit var photoSynthesisSaturationAmount: ModConfigSpec.DoubleValue
        lateinit var slimyDeathCooldown: ModConfigSpec.IntValue
        lateinit var slimyDeathHealthMultiplier: ModConfigSpec.DoubleValue
        lateinit var teleportCooldown: ModConfigSpec.IntValue
        lateinit var teleportDistance: ModConfigSpec.DoubleValue
        lateinit var thornsChance: ModConfigSpec.DoubleValue
        lateinit var thornsDamage: ModConfigSpec.DoubleValue
        lateinit var thornsHungerDrain: ModConfigSpec.DoubleValue
        lateinit var wallClimbSpeed: ModConfigSpec.DoubleValue
        lateinit var woolyCooldown: ModConfigSpec.IntValue
        lateinit var xpMagnetCooldown: ModConfigSpec.IntValue
        lateinit var xpMagnetRadius: ModConfigSpec.DoubleValue
    }

    init {
        generalConfigs()
        machineConfigs()
        geneConfigs()

        builder.build()
    }

    private fun generalConfigs() {
        builder.push("general")

        keepGenesOnDeath = builder
            .comment("Keep genes on death")
            .define("keepGenesOnDeath", true)

        minimumCooldownForNotification = builder
            .comment("When a cooldown ends, it notifies the player, unless the cooldown is shorter than this value (in ticks)")
            .defineInRange("minimumCooldownForNotification", 20 * 60, 1, Int.MAX_VALUE)

        antifieldBlockRadius = builder
            .comment("How far should the Antifield Block prevent Item/XP Magnet Genes from working (in blocks)")
            .defineInRange("antifieldBlockRadius", 25, 1, Int.MAX_VALUE)

        builder.pop()
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

        // most other machines hold 20_000
        // but dispersal is 1_000


        builder.pop()
    }

    private fun geneConfigs() {
        builder.push("genes")

        disabledGenes = builder
            .comment("List of genes to disable.\nExample: [\"geneticsresequenced:wooly\",\"geneticsresequenced:lay_egg\"]")
            .defineList("disabledGenes", listOf()) { it is String }

        disableGivingPlayersNegativeGenes = builder
            .comment("Set true to prevent players from being given negative genes")
            .define("disableGivingPlayersNegativeGenes", false)

        bioluminescenceDuration = builder
            .comment("How long should light sources from the Bioluminescence gene last (in ticks)")
            .defineInRange("bioluminescenceDuration", 20 * 30, 1, Int.MAX_VALUE)
        bioluminescenceCooldown = builder
            .comment("How often entities with the Bioluminescence gene should emit light (in ticks)")
            .defineInRange("bioluminescenceCooldown", 10, 1, Int.MAX_VALUE)

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
            .defineInRange("dragonsBreathCooldown", 20 * 15, 1, Int.MAX_VALUE)

        itemMagnetCooldown = builder
            .comment("How often should the Item Magnet gene attract items (in ticks)")
            .defineInRange("itemMagnetCooldown", 10, 1, Int.MAX_VALUE)
        itemMagnetRadius = builder
            .comment("How far should the Item Magnet gene attract items (in blocks)")
            .defineInRange("itemMagnetRadius", 8.0, 1.0, Double.MAX_VALUE)

        johnnyAttackMultiplier = builder
            .comment("How much should the Johnny gene multiply damage by, when using an Axe?")
            .defineInRange("johnnyAttackMultiplier", 1.25, 0.0, Double.MAX_VALUE)

        knockbackStrength = builder
            .comment("How strong should the Knock Back gene be")
            .defineInRange("knockbackStrength", 1.0, 0.0, Double.MAX_VALUE)

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
        mobSightCooldown = builder
            .comment("How often should the Mob Sight gene check for entities (in ticks)")
            .defineInRange("mobSightCooldown", 20 * 1, 1, Int.MAX_VALUE)

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
        photoSynthesisHungerAmount = builder
            .comment("How much hunger to regain when the Photosynthesis gene procs")
            .defineInRange("photoSynthesisHungerAmount", 1, 0, Int.MAX_VALUE)
        photoSynthesisSaturationAmount = builder
            .comment("How much saturation to regain when the Photosynthesis gene procs")
            .defineInRange("photoSynthesisSaturationAmount", 0.5, 0.0, Double.MAX_VALUE)

        slimyDeathCooldown = builder
            .comment("How many ticks to wait before someone with the Slimy Death gene can spawn slimes again")
            .defineInRange("slimyDeathCooldown", 20 * 60 * 10, 1, Int.MAX_VALUE)
        slimyDeathHealthMultiplier = builder
            .comment("How much health should entities who survive via Slimy Death regain upon death, as a percentage of their max health")
            .defineInRange("slimyDeathHealthMultiplier", 0.5, 0.0, 1.0)

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

}