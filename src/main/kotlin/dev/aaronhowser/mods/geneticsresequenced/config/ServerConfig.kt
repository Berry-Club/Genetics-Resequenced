package dev.aaronhowser.mods.geneticsresequenced.config

import net.minecraftforge.common.ForgeConfigSpec

object ServerConfig {
    val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    // General
    lateinit var keepGenesOnDeath: ForgeConfigSpec.BooleanValue
    lateinit var hardMode: ForgeConfigSpec.BooleanValue
    lateinit var showEffectIcons: ForgeConfigSpec.BooleanValue

    // Gene values
    lateinit var thornsChange: ForgeConfigSpec.DoubleValue
    lateinit var thornsDamage: ForgeConfigSpec.DoubleValue
    lateinit var thornsHunger: ForgeConfigSpec.DoubleValue
    lateinit var clawsChance: ForgeConfigSpec.DoubleValue
    lateinit var clawsDamage: ForgeConfigSpec.DoubleValue
    lateinit var bioluminescenceDuration: ForgeConfigSpec.IntValue
    lateinit var mobSightRadius: ForgeConfigSpec.DoubleValue
    lateinit var teleportDistance: ForgeConfigSpec.DoubleValue
    lateinit var wallClimbSpeed: ForgeConfigSpec.DoubleValue

    // Gene Cooldowns
    lateinit var woolyCooldown: ForgeConfigSpec.IntValue
    lateinit var meatyCooldown: ForgeConfigSpec.IntValue
    lateinit var milkyCooldown: ForgeConfigSpec.IntValue
    lateinit var bioluminescenceCooldown: ForgeConfigSpec.IntValue
    lateinit var meaty2Cooldown: ForgeConfigSpec.IntValue
    lateinit var eggCooldown: ForgeConfigSpec.IntValue
    lateinit var photosynthesisCooldown: ForgeConfigSpec.IntValue
    lateinit var mobSightCooldown: ForgeConfigSpec.IntValue
    lateinit var teleportCooldown: ForgeConfigSpec.IntValue

    init {
        generalConfigs()
        geneConfigs()

        SPEC = BUILDER.build()
    }

    private fun generalConfigs() {
        BUILDER.comment("General settings").push("general")

        hardMode = BUILDER
            .comment("Make earning traits harder, better balance when playing with Mods")
            .define("hardMode", false)

        keepGenesOnDeath = BUILDER
            .comment("Keep genes on death")
            .define("keepGenesOnDeath", true)

        showEffectIcons = BUILDER
            .comment("Show the icons for effects granted by genes")
            .define("showEffectIcons", true)

        BUILDER.pop()
    }

    private fun geneConfigs() {
        BUILDER.comment("Genes").push("genes")

        geneValueConfigs()
        geneCooldownConfigs()

        BUILDER.pop()
    }

    private fun geneValueConfigs() {
        BUILDER.comment("Values").push("values")

        thornsChange = BUILDER
            .comment("What probability should the Thorns gene have")
            .defineInRange("thornsChange", 0.15, 0.0, 1.0)

        thornsDamage = BUILDER
            .comment("How much damage to deal back to the attacker when they hit a target with the Thorns gene")
            .defineInRange("thornsDamage", 0.5, 0.0, Double.MAX_VALUE)

        thornsHunger = BUILDER
            .comment("How much hunger to drain when a player's Thorns gene deals damage to an attacker")
            .defineInRange("thornsHunger", 1.0, 0.0, Double.MAX_VALUE)

        clawsChance = BUILDER
            .comment("What probability should the Claws gene have (doubled if the player has Claws 2 gene)")
            .defineInRange("clawsOneChance", 0.33, 0.0, 1.0)

        clawsDamage = BUILDER
            .comment("How much damage should Bleeding deal")
            .defineInRange("clawsDamage", 8.0, 0.0, Double.MAX_VALUE)

        bioluminescenceDuration = BUILDER
            .comment("How long should the Bioluminescence gene last (in ticks)")
            .defineInRange("bioluminescenceDuration", 20 * 30, 1, Int.MAX_VALUE)

        mobSightRadius = BUILDER
            .comment("How far from the player should Mob Sight detect entities (in blocks)")
            .defineInRange("mobSightRadius", 32.0, 1.0, Double.MAX_VALUE)

        teleportDistance = BUILDER
            .comment("How far should the Teleport gene teleport the player, at maximum")
            .defineInRange("teleportDistance", 10.0, 1.0, Double.MAX_VALUE)

        wallClimbSpeed = BUILDER
            .comment("How fast should the player climb walls with the Wall Climbing gene")
            .defineInRange("wallClimbSpeed", 0.144, 0.0, Double.MAX_VALUE)

        BUILDER.pop()
    }

    private fun geneCooldownConfigs() {
        BUILDER.comment("Gene Cooldowns").push("cooldowns")

        woolyCooldown = BUILDER
            .comment("How many ticks to wait before someone with the Wooly gene can be sheared again (skipped if they eat grass)")
            .defineInRange("woolyCooldown", 20 * 60, 1, Int.MAX_VALUE)

        meatyCooldown = BUILDER
            .comment("How many ticks to wait before someone with the Meaty gene can be sheared again")
            .defineInRange("meatyCooldown", 20 * 60, 1, Int.MAX_VALUE)

        meaty2Cooldown = BUILDER
            .comment("How many ticks to wait before someone with the Meaty 2 gene drops meat again")
            .defineInRange("meaty2Cooldown", 20 * 60 * 5, 1, Int.MAX_VALUE)

        eggCooldown = BUILDER
            .comment("How many ticks to wait before someone with the Lay Egg gene lays an egg again")
            .defineInRange("eggCooldown", 20 * 60 * 5, 1, Int.MAX_VALUE)

        milkyCooldown = BUILDER
            .comment("How many ticks to wait before someone with the Milky gene can be milked again")
            .defineInRange("milkyCooldown", 1, 1, Int.MAX_VALUE)

        bioluminescenceCooldown = BUILDER
            .comment("How often entities with the Bioluminescence gene should emit light (in ticks)")
            .defineInRange("bioluminescenceCooldown", 10, 1, Int.MAX_VALUE)

        photosynthesisCooldown = BUILDER
            .comment("How often entities with the Photosynthesis gene should regain hunger (in ticks)")
            .defineInRange("photosynthesisCooldown", 20 * 30, 1, Int.MAX_VALUE)

        mobSightCooldown = BUILDER
            .comment("How often should the Mob Sight gene check for entities (in ticks)")
            .defineInRange("mobSightCooldown", 20 * 1, 1, Int.MAX_VALUE)

        teleportCooldown = BUILDER
            .comment("How many ticks to wait before someone with the Teleport gene can teleport again")
            .defineInRange("teleportCooldown", 20, 1, Int.MAX_VALUE)

        BUILDER.pop()
    }

}