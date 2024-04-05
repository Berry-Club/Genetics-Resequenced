package dev.aaronhowser.mods.geneticsresequenced.config

import net.minecraftforge.common.ForgeConfigSpec

object ServerConfig {
    val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    // General
    val keepGenesOnDeath: ForgeConfigSpec.BooleanValue
    val hardMode: ForgeConfigSpec.BooleanValue

    // Gene multipliers
    val thornsChange: ForgeConfigSpec.DoubleValue
    val thornsDamage: ForgeConfigSpec.DoubleValue
    val thornsHunger: ForgeConfigSpec.DoubleValue

    // Gene Cooldowns
    val woolyCooldown: ForgeConfigSpec.IntValue
    val meatyCooldown: ForgeConfigSpec.IntValue
    val milkyCooldown: ForgeConfigSpec.IntValue

    init {
        BUILDER.comment("General settings").push("general")

        hardMode = BUILDER
            .comment("Make earning traits harder, better balance when playing with Mods")
            .define("hardMode", false)

        keepGenesOnDeath = BUILDER
            .comment("Keep genes on death")
            .define("keepGenesOnDeath", true)

        BUILDER.pop()

        BUILDER.comment("Genes").push("genes")

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

        BUILDER.pop()

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

        BUILDER.pop()

        SPEC = BUILDER.build()
    }
}