package dev.aaronhowser.mods.geneticsresequenced.gene

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey

object BaseModGenes {

    private fun resourceKey(geneName: String): ResourceKey<Gene> {
        return ResourceKey.create(ModGenes.GENE_REGISTRY_KEY, OtherUtil.modResource(geneName))
    }

    val BASIC = resourceKey("basic")

    // Mutations
    val CLAWS_TWO = resourceKey("claws_2")
    val EFFICIENCY_FOUR = resourceKey("efficiency_4")
    val FLIGHT = resourceKey("flight")
    val HASTE_TWO = resourceKey("haste_2")
    val MEATY_TWO = resourceKey("meaty_2")
    val MORE_HEARTS_TWO = resourceKey("more_hearts_2")
    val PHOTOSYNTHESIS = resourceKey("photosynthesis")
    val REGENERATION_FOUR = resourceKey("regeneration_4")
    val RESISTANCE_TWO = resourceKey("resistance_2")
    val SPEED_FOUR = resourceKey("speed_4")
    val SPEED_TWO = resourceKey("speed_2")
    val STRENGTH_TWO = resourceKey("strength_2")
    val SCARE_ZOMBIES = resourceKey("scare_zombies")
    val SCARE_SPIDERS = resourceKey("scare_spiders")

    //Standard list

    val BIOLUMINESCENCE = resourceKey("bioluminescence")
    val CHATTERBOX = resourceKey("chatterbox")
    val CHILLING = resourceKey("chilling")
    val CLAWS = resourceKey("claws")
    val DRAGON_BREATH = resourceKey("dragons_breath")
    val EAT_GRASS = resourceKey("eat_grass")
    val EFFICIENCY = resourceKey("efficiency")
    val EMERALD_HEART = resourceKey("emerald_heart")
    val ENDER_DRAGON_HEALTH = resourceKey("ender_dragon_health")
    val EXPLOSIVE_EXIT = resourceKey("explosive_exit")
    val FIRE_PROOF = resourceKey("fire_proof")
    val HASTE = resourceKey("haste")
    val INFINITY = resourceKey("infinity")
    val INVISIBLE = resourceKey("invisible")
    val ITEM_MAGNET = resourceKey("item_magnet")
    val JUMP_BOOST = resourceKey("jump_boost")
    val JOHNNY = resourceKey("johnny")
    val KEEP_INVENTORY = resourceKey("keep_inventory")
    val KNOCKBACK = resourceKey("knockback")
    val LAY_EGG = resourceKey("lay_egg")
    val LUCK = resourceKey("luck")
    val MEATY = resourceKey("meaty")
    val MILKY = resourceKey("milky")
    val MOB_SIGHT = resourceKey("mob_sight")
    val MORE_HEARTS = resourceKey("more_hearts")
    val NIGHT_VISION = resourceKey("night_vision")
    val NO_FALL_DAMAGE = resourceKey("no_fall_damage")
    val NO_HUNGER = resourceKey("no_hunger")
    val POISON_IMMUNITY = resourceKey("poison_immunity")
    val REGENERATION = resourceKey("regeneration")
    val REACHING = resourceKey("reaching")
    val RESISTANCE = resourceKey("resistance")
    val SCARE_CREEPERS = resourceKey("scare_creepers")
    val SCARE_SKELETONS = resourceKey("scare_skeletons")
    val SHOOT_FIREBALLS = resourceKey("shoot_fireballs")
    val SLIMY_DEATH = resourceKey("slimy_death")
    val SPEED = resourceKey("speed")
    val STEP_ASSIST = resourceKey("step_assist")
    val STRENGTH = resourceKey("strength")
    val TELEPORT = resourceKey("teleport")
    val THORNS = resourceKey("thorns")
    val WALL_CLIMBING = resourceKey("wall_climbing")
    val WATER_BREATHING = resourceKey("water_breathing")
    val WITHER_HIT = resourceKey("wither_hit")
    val WITHER_PROOF = resourceKey("wither_proof")
    val WOOLY = resourceKey("wooly")
    val XP_MAGNET = resourceKey("xp_magnet")

    //Negative effects

    //FIXME: This effect apparently has a sound on add now, probably want to remove that
    val BAD_OMEN = resourceKey("bad_omen")
    val BLINDNESS = resourceKey("blindness")
    val CRINGE = resourceKey("cringe")
    val CURSED = resourceKey("cursed")
    val FLAMBE = resourceKey("flambe")
    val HUNGER = resourceKey("hunger")
    val INFESTED = resourceKey("infested")
    val LEVITATION = resourceKey("levitation")
    val MINING_FATIGUE = resourceKey("mining_fatigue")
    val NAUSEA = resourceKey("nausea")
    val OOZING = resourceKey("oozing")
    val POISON = resourceKey("poison")
    val POISON_FOUR = resourceKey("poison_4")
    val SLOWNESS = resourceKey("slowness")
    val SLOWNESS_FOUR = resourceKey("slowness_4")
    val SLOWNESS_SIX = resourceKey("slowness_6")
    val WEAVING = resourceKey("weaving")
    val WEAKNESS = resourceKey("weakness")
    val WIND_CHARGED = resourceKey("wind_charged")
    val WITHER = resourceKey("wither")

    // Plagues

    val BLACK_DEATH = resourceKey("black_death")
    val GREEN_DEATH = resourceKey("green_death")
    val WHITE_DEATH = resourceKey("white_death")
    val GRAY_DEATH = resourceKey("gray_death")
    val UN_UNDEATH = resourceKey("un_undeath")


    fun ResourceKey<Gene>.getHolder(registries: HolderLookup.Provider): Holder.Reference<Gene>? =
        ModGenes.fromResourceKey(registries, this)
}