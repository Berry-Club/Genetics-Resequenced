package dev.aaronhowser.mods.geneticsresequenced.config

import net.minecraftforge.common.ForgeConfigSpec

object ServerConfig {
    val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    // General
    val keepGenesOnDeath: ForgeConfigSpec.BooleanValue
    val hardMode: ForgeConfigSpec.BooleanValue

    // Genes
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

        SPEC = BUILDER.build()
    }
}