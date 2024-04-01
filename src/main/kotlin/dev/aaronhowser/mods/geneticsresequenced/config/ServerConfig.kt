package dev.aaronhowser.mods.geneticsresequenced.config

import net.minecraftforge.common.ForgeConfigSpec

object ServerConfig {
    val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

//    val playerGeneSharing: ForgeConfigSpec.BooleanValue
    val keepGenesOnDeath: ForgeConfigSpec.BooleanValue
//    val allowGivingEntityGenes: ForgeConfigSpec.BooleanValue

    val hardMode: ForgeConfigSpec.BooleanValue
//    val bioluminescenceLightLevel: Double
//    val thornsDamage:Double
//    val clawsDamage: Double
//    val clawsChange: Int


    init {
        BUILDER.comment("General settings").push("general")

        hardMode = BUILDER
            .comment("Make earning traits harder, better balance when playing with Mods")
            .define("hardMode", false)

        keepGenesOnDeath = BUILDER
            .comment("Keep genes on death")
            .define("keepGenesOnDeath", true)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}