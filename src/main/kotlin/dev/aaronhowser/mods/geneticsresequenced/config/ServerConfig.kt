package dev.aaronhowser.mods.geneticsresequenced.config

import net.minecraftforge.common.ForgeConfigSpec

object ServerConfig {
    val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

//    val playerGeneSharing: Boolean
//    val keepGenesOnDeath: Boolean
//    val allowGivingEntityGenes: Boolean

    val hardMode: ForgeConfigSpec.BooleanValue
//    val bioilluminanceLightLevel: Double
//    val thornsDamage:Double
//    val clawsDamage: Double
//    val clawsChange: Int


    init {
        BUILDER.comment("General settings").push("general")

        hardMode = BUILDER
            .comment("Make earning traits harder, better balance when playing with Mods")
            .define("hardMode", false)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}