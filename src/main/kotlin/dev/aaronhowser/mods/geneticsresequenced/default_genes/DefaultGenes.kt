package dev.aaronhowser.mods.geneticsresequenced.default_genes

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneBuilder
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.effect.MobEffects

@Suppress("unused", "MemberVisibilityCanBePrivate")
object DefaultGenes {

    fun registerDefaultGenes() {
        // Doesn't do anything, but it loads the object which initializes the genes
    }

    private fun registerGene(geneId: String): GeneBuilder =
        GeneBuilder(OtherUtil.modResource(geneId))

    val BASIC: Gene = registerGene("basic")
        .setDnaPointsRequired(0)
        .build()

    // Mutations (must be initialized first because they're used in arguments in ones below)

    val CLAWS_2: Gene = registerGene("claws_2")
        .setDnaPointsRequired(50)
        .allowMobs()
        .build()
    val EFFICIENCY_4: Gene = registerGene("efficiency_4")
        .setDnaPointsRequired(50)
        .build()
    val FLIGHT: Gene = registerGene("flight")
        .setDnaPointsRequired(50)
        .build()
    val HASTE_2: Gene = registerGene("haste_2")
        .setDnaPointsRequired(50)
        .setPotion(MobEffects.DIG_SPEED, 2)
        .build()
    val MEATY_2: Gene = registerGene("meaty_2")
        .setDnaPointsRequired(50)
        .allowMobs()
        .build()
    val MORE_HEARTS_2: Gene = registerGene("more_hearts_2")
        .setDnaPointsRequired(50)
        .allowMobs()
        .build()
    val PHOTOSYNTHESIS: Gene = registerGene("photosynthesis")
        .setDnaPointsRequired(40)
        .build()
    val REGENERATION_4: Gene = registerGene("regeneration_4")
        .setDnaPointsRequired(50)
        .setPotion(MobEffects.REGENERATION, 4)
        .build()
    val RESISTANCE_2: Gene = registerGene("resistance_2")
        .setDnaPointsRequired(50)
        .setPotion(MobEffects.DAMAGE_RESISTANCE, 2)
        .build()
    val SPEED_4: Gene = registerGene("speed_4")
        .setDnaPointsRequired(50)
        .setPotion(MobEffects.MOVEMENT_SPEED, 4)
        .build()
    val SPEED_2: Gene = registerGene("speed_2")
        .setDnaPointsRequired(50)
        .setPotion(MobEffects.MOVEMENT_SPEED, 2)
        .setMutatesInto(SPEED_4)
        .build()
    val STRENGTH_2: Gene = registerGene("strength_2")
        .setDnaPointsRequired(50)
        .setPotion(MobEffects.DAMAGE_BOOST, 2)
        .build()
    val SCARE_ZOMBIES: Gene = registerGene("scare_zombies")
        .setDnaPointsRequired(50)
        .build()
    val SCARE_SPIDERS: Gene = registerGene("scare_spiders")
        .setDnaPointsRequired(50)
        .build()

    //Standard list
    val BIOLUMINESCENCE: Gene = registerGene("bioluminescence")
        .setDnaPointsRequired(16)
        .build()
    val CLAWS: Gene = registerGene("claws")
        .setDnaPointsRequired(20)
        .setMutatesInto(CLAWS_2)
        .build()
    val DRAGONS_BREATH: Gene = registerGene("dragons_breath")
        .setDnaPointsRequired(20)
        .build()
    val EAT_GRASS: Gene = registerGene("eat_grass")
        .setDnaPointsRequired(16)
        .build()
    val EFFICIENCY: Gene = registerGene("efficiency")
        .setDnaPointsRequired(30)
        .setMutatesInto(EFFICIENCY_4)
        .build()
    val EMERALD_HEART: Gene = registerGene("emerald_heart")
        .setDnaPointsRequired(30)
        .allowMobs()
        .build()
    val ENDER_DRAGON_HEALTH: Gene = registerGene("ender_dragon_health")
        .setDnaPointsRequired(60)
        .build()
    val EXPLOSIVE_EXIT: Gene = registerGene("explosive_exit")
        .setDnaPointsRequired(20)
        .allowMobs()
        .build()
    val FIRE_PROOF: Gene = registerGene("fire_proof")
        .setDnaPointsRequired(24)
        .allowMobs()
        .build()
    val HASTE: Gene = registerGene("haste")
        .setDnaPointsRequired(30)
        .setPotion(MobEffects.DIG_SPEED, 1)
        .setMutatesInto(HASTE_2)
        .build()
    val INFINITY: Gene = registerGene("infinity")
        .setDnaPointsRequired(30)
        .build()
    val INVISIBLE: Gene = registerGene("invisible")
        .setDnaPointsRequired(50)
        .allowMobs()
        .setPotion(MobEffects.INVISIBILITY, 1)
        .build()
    val ITEM_MAGNET: Gene = registerGene("item_magnet")
        .setDnaPointsRequired(30)
        .build()
    val JUMP_BOOST: Gene = registerGene("jump_boost")
        .setDnaPointsRequired(10)
        .allowMobs()
        .setPotion(MobEffects.JUMP, 1)
        .setMutatesInto(FLIGHT)
        .build()
    val KEEP_INVENTORY: Gene = registerGene("keep_inventory")
        .setDnaPointsRequired(40)
        .build()
    val LAY_EGG: Gene = registerGene("lay_egg")
        .setDnaPointsRequired(12)
        .allowMobs()
        .build()
    val LUCK: Gene = registerGene("luck")
        .setDnaPointsRequired(50)
        .allowMobs()
        .setPotion(MobEffects.LUCK, 1)
        .build()
    val MEATY: Gene = registerGene("meaty")
        .setDnaPointsRequired(12)
        .allowMobs()
        .setMutatesInto(MEATY_2)
        .build()
    val MILKY: Gene = registerGene("milky")
        .setDnaPointsRequired(12)
        .allowMobs()
        .build()
    val MOB_SIGHT: Gene = registerGene("mob_sight")
        .setDnaPointsRequired(16)
        .build()
    val MORE_HEARTS: Gene = registerGene("more_hearts")
        .setDnaPointsRequired(40)
        .allowMobs()
        .setMutatesInto(MORE_HEARTS_2)
        .build()
    val NIGHT_VISION: Gene = registerGene("night_vision")
        .setDnaPointsRequired(16)
        .setPotion(MobEffects.NIGHT_VISION, 1)
        .build()
    val NO_FALL_DAMAGE: Gene = registerGene("no_fall_damage")
        .setDnaPointsRequired(30)
        .allowMobs()
        .build()
    val NO_HUNGER: Gene = registerGene("no_hunger")
        .setDnaPointsRequired(30)
        .build()
    val POISON_IMMUNITY: Gene = registerGene("poison_immunity")
        .setDnaPointsRequired(24)
        .allowMobs()
        .build()
    val REGENERATION: Gene = registerGene("regeneration")
        .setDnaPointsRequired(60)
        .allowMobs()
        .setPotion(MobEffects.REGENERATION, 1)
        .setMutatesInto(REGENERATION_4)
        .build()
    val RESISTANCE: Gene = registerGene("resistance")
        .setDnaPointsRequired(30)
        .allowMobs()
        .setPotion(MobEffects.DAMAGE_RESISTANCE, 1)
        .setMutatesInto(RESISTANCE_2)
        .build()
    val SCARE_CREEPERS: Gene = registerGene("scare_creepers")
        .setDnaPointsRequired(20)
        .setMutatesInto(SCARE_ZOMBIES)
        .build()
    val SCARE_SKELETONS: Gene = registerGene("scare_skeletons")
        .setDnaPointsRequired(20)
        .setMutatesInto(SCARE_SPIDERS)
        .build()
    val SHOOT_FIREBALLS: Gene = registerGene("shoot_fireballs")
        .setDnaPointsRequired(24)
        .build()
    val SLIMY_DEATH: Gene = registerGene("slimy_death")
        .setDnaPointsRequired(60)
        .build()
    val SPEED: Gene = registerGene("speed")
        .setDnaPointsRequired(20)
        .allowMobs()
        .setPotion(MobEffects.MOVEMENT_SPEED, 1)
        .setMutatesInto(SPEED_2)
        .build()
    val STEP_ASSIST: Gene = registerGene("step_assist")
        .setDnaPointsRequired(10)
        .build()
    val STRENGTH: Gene = registerGene("strength")
        .setDnaPointsRequired(20)
        .allowMobs()
        .setPotion(MobEffects.DAMAGE_BOOST, 1)
        .setMutatesInto(STRENGTH_2)
        .build()
    val TELEPORT: Gene = registerGene("teleport")
        .setDnaPointsRequired(24)
        .setMutatesInto(FLIGHT)
        .build()
    val THORNS: Gene = registerGene("thorns")
        .setDnaPointsRequired(12)
        .allowMobs()
        .setMutatesInto(PHOTOSYNTHESIS)
        .build()
    val WALL_CLIMBING: Gene = registerGene("wall_climbing")
        .setDnaPointsRequired(40)
        .build()
    val WATER_BREATHING: Gene = registerGene("water_breathing")
        .setDnaPointsRequired(16)
        .allowMobs()
        .build()
    val WITHER_HIT: Gene = registerGene("wither_hit")
        .setDnaPointsRequired(20)
        .allowMobs()
        .build()
    val WITHER_PROOF: Gene = registerGene("wither_proof")
        .setDnaPointsRequired(40)
        .allowMobs()
        .build()
    val WOOLY: Gene = registerGene("wooly")
        .setDnaPointsRequired(12)
        .allowMobs()
        .build()
    val XP_MAGNET: Gene = registerGene("xp_magnet")
        .setDnaPointsRequired(30)
        .build()

    //Negative effects
    val BLINDNESS: Gene = registerGene("blindness")
        .setPotion(MobEffects.BLINDNESS, 1)
        .allowMobs()
        .setNegative()
        .build()
    val CURSED: Gene = registerGene("cursed")
        .setPotion(MobEffects.UNLUCK, 1)
        .allowMobs()
        .setNegative()
        .build()
    val DEAD_ALL: Gene = registerGene("black_death")
        .setNegative()
        .allowMobs()
        .build()
    val DEAD_CREEPERS: Gene = registerGene("green_death")
        .setNegative()
        .allowMobs()
        .build()
    val DEAD_HOSTILE: Gene = registerGene("white_death")
        .setNegative()
        .allowMobs()
        .build()
    val DEAD_OLD_AGE: Gene = registerGene("gray_death")
        .setNegative()
        .allowMobs()
        .build()
    val DEAD_UNDEAD: Gene = registerGene("undeath")
        .setNegative()
        .allowMobs()
        .build()
    val FLAMBE: Gene = registerGene("flambe")
        .setNegative()
        .allowMobs()
        .build()
    val HUNGER: Gene = registerGene("hunger")
        .setPotion(MobEffects.HUNGER, 1)
        .setNegative()
        .allowMobs()
        .build()
    val LEVITATION: Gene = registerGene("levitation")
        .setPotion(MobEffects.LEVITATION, 1)
        .allowMobs()
        .setNegative()
        .build()
    val MINING_WEAKNESS: Gene = registerGene("mining_weakness")
        .setPotion(MobEffects.DIG_SLOWDOWN, 1)
        .allowMobs()
        .setNegative()
        .build()
    val NAUSEA: Gene = registerGene("nausea")
        .setPotion(MobEffects.CONFUSION, 1)
        .allowMobs()
        .setNegative()
        .build()
    val POISON: Gene = registerGene("poison")
        .setPotion(MobEffects.POISON, 1)
        .allowMobs()
        .setNegative()
        .build()
    val POISON_4: Gene = registerGene("poison_4")
        .setPotion(MobEffects.POISON, 4)
        .allowMobs()
        .setNegative()
        .build()
    val REALLY_DEAD_ALL: Gene = registerGene("void_death")
        .setNegative()
        .allowMobs()
        .build()
    val SLOWNESS: Gene = registerGene("slowness")
        .setPotion(MobEffects.MOVEMENT_SLOWDOWN, 1)
        .allowMobs()
        .setNegative()
        .build()
    val SLOWNESS_4: Gene = registerGene("slowness_4")
        .setPotion(MobEffects.MOVEMENT_SLOWDOWN, 4)
        .allowMobs()
        .setNegative()
        .build()
    val SLOWNESS_6: Gene = registerGene("slowness_6")
        .setPotion(MobEffects.MOVEMENT_SLOWDOWN, 6)
        .allowMobs()
        .setNegative()
        .build()
    val WEAKNESS: Gene = registerGene("weakness")
        .setPotion(MobEffects.WEAKNESS, 1)
        .allowMobs()
        .setNegative()
        .build()
    val WITHER: Gene = registerGene("wither")
        .setPotion(MobEffects.WITHER, 1)
        .allowMobs()
        .setNegative()
        .build()

    val defaultGeneRequirements: MutableMap<Gene, Set<Gene>> = mutableMapOf(
        CLAWS_2 to setOf(CLAWS),
        EFFICIENCY_4 to setOf(EFFICIENCY),
        FLIGHT to setOf(TELEPORT, JUMP_BOOST, NO_FALL_DAMAGE),
        HASTE_2 to setOf(HASTE),
        MEATY_2 to setOf(MEATY),
        MORE_HEARTS_2 to setOf(MORE_HEARTS),
        REGENERATION_4 to setOf(REGENERATION),
        RESISTANCE_2 to setOf(RESISTANCE),
        SPEED_2 to setOf(SPEED),
        SPEED_4 to setOf(SPEED_2),
        STRENGTH_2 to setOf(STRENGTH),
        SCARE_ZOMBIES to setOf(SCARE_CREEPERS),
        SCARE_SPIDERS to setOf(SCARE_SKELETONS),
        PHOTOSYNTHESIS to setOf(THORNS)
    )

    fun setGeneRequirements() {
        for ((gene: Gene, requiredGenes: Set<Gene>) in defaultGeneRequirements) {
            gene.addRequiredGenes(requiredGenes)
        }
    }

}