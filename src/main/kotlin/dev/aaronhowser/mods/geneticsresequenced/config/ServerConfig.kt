package dev.aaronhowser.mods.geneticsresequenced.config

import net.minecraftforge.common.ForgeConfigSpec

object ServerConfig {
    val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    // General
    lateinit var keepGenesOnDeath: ForgeConfigSpec.BooleanValue
    lateinit var hardMode: ForgeConfigSpec.BooleanValue

    // Gene values
    lateinit var thornsChange: ForgeConfigSpec.DoubleValue
    lateinit var thornsDamage: ForgeConfigSpec.DoubleValue
    lateinit var thornsHunger: ForgeConfigSpec.DoubleValue
    lateinit var clawsChance: ForgeConfigSpec.DoubleValue
    lateinit var clawsDamage: ForgeConfigSpec.DoubleValue

    // Gene Cooldowns
    lateinit var woolyCooldown: ForgeConfigSpec.IntValue
    lateinit var meatyCooldown: ForgeConfigSpec.IntValue
    lateinit var milkyCooldown: ForgeConfigSpec.IntValue

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

        milkyCooldown = BUILDER
            .comment("How many ticks to wait before someone with the Milky gene can be milked again")
            .defineInRange("milkyCooldown", 1, 1, Int.MAX_VALUE)

        BUILDER.pop()
    }

}