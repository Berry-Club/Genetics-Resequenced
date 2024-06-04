package dev.aaronhowser.mods.geneticsresequenced.default_genes

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneBuilder
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.effect.MobEffects

@Suppress("unused", "MemberVisibilityCanBePrivate")
object DefaultGenes {


    lateinit var basic: Gene

    // Regular Genes
    lateinit var bioluminescence: Gene
        private set
    lateinit var claws: Gene
        private set
    lateinit var clawsTwo: Gene
        private set
    lateinit var dragonsBreath: Gene
        private set
    lateinit var eatGrass: Gene
        private set
    lateinit var efficiency: Gene
        private set
    lateinit var efficiencyFour: Gene
        private set
    lateinit var emeraldHeart: Gene
        private set
    lateinit var enderDragonHealth: Gene
        private set
    lateinit var explosiveExit: Gene
        private set
    lateinit var fireProof: Gene
        private set
    lateinit var flight: Gene
        private set
    lateinit var haste: Gene
        private set
    lateinit var hasteTwo: Gene
        private set
    lateinit var infinity: Gene
        private set
    lateinit var invisible: Gene
        private set
    lateinit var itemMagnet: Gene
        private set
    lateinit var jumpBoost: Gene
        private set
    lateinit var keepInventory: Gene
        private set
    lateinit var layEgg: Gene
        private set
    lateinit var luck: Gene
        private set
    lateinit var meaty: Gene
        private set
    lateinit var meatyTwo: Gene
        private set
    lateinit var milky: Gene
        private set
    lateinit var mobSight: Gene
        private set
    lateinit var moreHearts: Gene
        private set
    lateinit var moreHeartsTwo: Gene
        private set
    lateinit var nightVision: Gene
        private set
    lateinit var noFallDamage: Gene
        private set
    lateinit var noHunger: Gene
        private set
    lateinit var photosynthesis: Gene
        private set
    lateinit var poisonImmunity: Gene
        private set
    lateinit var regeneration: Gene
        private set
    lateinit var regenerationFour: Gene
        private set
    lateinit var resistance: Gene
        private set
    lateinit var resistanceTwo: Gene
        private set
    lateinit var scareCreepers: Gene
        private set
    lateinit var scareSkeletons: Gene
        private set
    lateinit var scareSpiders: Gene
        private set
    lateinit var scareZombies: Gene
        private set
    lateinit var shootFireballs: Gene
        private set
    lateinit var slimyDeath: Gene
        private set
    lateinit var speed: Gene
        private set
    lateinit var speedFour: Gene
        private set
    lateinit var speedTwo: Gene
        private set
    lateinit var stepAssist: Gene
        private set
    lateinit var strength: Gene
        private set
    lateinit var strengthTwo: Gene
        private set
    lateinit var teleport: Gene
        private set
    lateinit var thorns: Gene
        private set
    lateinit var wallClimbing: Gene
        private set
    lateinit var waterBreathing: Gene
        private set
    lateinit var witherHit: Gene
        private set
    lateinit var witherProof: Gene
        private set
    lateinit var wooly: Gene
        private set
    lateinit var xpMagnet: Gene
        private set

    //Negative Genes
    lateinit var blindness: Gene
        private set
    lateinit var cursed: Gene
        private set
    lateinit var flambe: Gene
        private set
    lateinit var hunger: Gene
        private set
    lateinit var levitation: Gene
        private set
    lateinit var miningWeakness: Gene
        private set
    lateinit var nausea: Gene
        private set
    lateinit var poison: Gene
        private set
    lateinit var poisonFour: Gene
        private set
    lateinit var slowness: Gene
        private set
    lateinit var slownessFour: Gene
        private set
    lateinit var slownessSix: Gene
        private set
    lateinit var weakness: Gene
        private set
    lateinit var wither: Gene
        private set

    // Death Genes
    lateinit var blackDeath: Gene
        private set
    lateinit var greenDeath: Gene
        private set
    lateinit var whiteDeath: Gene
        private set
    lateinit var grayDeath: Gene
        private set
    lateinit var unUndeath: Gene
        private set


    fun registerDefaultGenes() {
        basic = registerGene("basic")
            .setDnaPointsRequired(0)
            .build().also { it.canBeAdded = false }

        bioluminescence = registerGene("bioluminescence")
            .allowMobs()
            .setDnaPointsRequired(16)
            .build()
        claws = registerGene("claws")
            .setDnaPointsRequired(20)
            .allowMobs()
            .setMutatesInto(clawsTwo)
            .build()
        clawsTwo = registerGene("claws_2")
            .setDnaPointsRequired(50)
            .allowMobs()
            .build()
        dragonsBreath = registerGene("dragons_breath")
            .setDnaPointsRequired(20)
            .build()
        eatGrass = registerGene("eat_grass")
            .setDnaPointsRequired(16)
            .build()
        efficiency = registerGene("efficiency")
            .setDnaPointsRequired(30)
            .setMutatesInto(efficiencyFour)
            .build()
        efficiencyFour = registerGene("efficiency_4")
            .setDnaPointsRequired(50)
            .build()
        emeraldHeart = registerGene("emerald_heart")
            .setDnaPointsRequired(30)
            .allowMobs()
            .build()
        enderDragonHealth = registerGene("ender_dragon_health")
            .setDnaPointsRequired(60)
            .build()
        explosiveExit = registerGene("explosive_exit")
            .setDnaPointsRequired(20)
            .allowMobs()
            .build()
        fireProof = registerGene("fire_proof")
            .setDnaPointsRequired(24)
            .allowMobs()
            .build()
        flight = registerGene("flight")
            .setDnaPointsRequired(50)
            .build()
        haste = registerGene("haste")
            .setDnaPointsRequired(30)
            .setPotion(MobEffects.DIG_SPEED, 1)
            .setMutatesInto(hasteTwo)
            .build()
        hasteTwo = registerGene("haste_2")
            .setDnaPointsRequired(50)
            .setPotion(MobEffects.DIG_SPEED, 2)
            .build()
        infinity = registerGene("infinity")
            .setDnaPointsRequired(30)
            .build()
        invisible = registerGene("invisible")
            .setDnaPointsRequired(50)
            .allowMobs()
            .setPotion(MobEffects.INVISIBILITY, 1)
            .build()
        itemMagnet = registerGene("item_magnet")
            .setDnaPointsRequired(30)
            .build()
        jumpBoost = registerGene("jump_boost")
            .setDnaPointsRequired(10)
            .allowMobs()
            .setPotion(MobEffects.JUMP, 1)
            .setMutatesInto(flight)
            .build()
        keepInventory = registerGene("keep_inventory")
            .setDnaPointsRequired(40)
            .build()
        layEgg = registerGene("lay_egg")
            .setDnaPointsRequired(12)
            .allowMobs()
            .build()
        luck = registerGene("luck")
            .setDnaPointsRequired(50)
            .allowMobs()
            .setPotion(MobEffects.LUCK, 1)
            .build()
        meaty = registerGene("meaty")
            .setDnaPointsRequired(12)
            .allowMobs()
            .setMutatesInto(meatyTwo)
            .build()
        meatyTwo = registerGene("meaty_2")
            .setDnaPointsRequired(50)
            .allowMobs()
            .build()
        milky = registerGene("milky")
            .setDnaPointsRequired(12)
            .allowMobs()
            .build()
        mobSight = registerGene("mob_sight")
            .setDnaPointsRequired(16)
            .build()
        moreHearts = registerGene("more_hearts")
            .setDnaPointsRequired(40)
            .allowMobs()
            .setMutatesInto(moreHeartsTwo)
            .build()
        moreHeartsTwo = registerGene("more_hearts_2")
            .setDnaPointsRequired(50)
            .allowMobs()
            .build()
        nightVision = registerGene("night_vision")
            .setDnaPointsRequired(16)
            .setPotion(MobEffects.NIGHT_VISION, 1)
            .build()
        noFallDamage = registerGene("no_fall_damage")
            .setDnaPointsRequired(30)
            .allowMobs()
            .build()
        noHunger = registerGene("no_hunger")
            .setDnaPointsRequired(30)
            .build()
        photosynthesis = registerGene("photosynthesis")
            .setDnaPointsRequired(40)
            .build()
        poisonImmunity = registerGene("poison_immunity")
            .setDnaPointsRequired(24)
            .allowMobs()
            .build()
        regeneration = registerGene("regeneration")
            .setDnaPointsRequired(60)
            .allowMobs()
            .setPotion(MobEffects.REGENERATION, 1)
            .setMutatesInto(regenerationFour)
            .build()
        regenerationFour = registerGene("regeneration_4")
            .setDnaPointsRequired(50)
            .setPotion(MobEffects.REGENERATION, 4)
            .build()
        resistance = registerGene("resistance")
            .setDnaPointsRequired(30)
            .allowMobs()
            .setPotion(MobEffects.DAMAGE_RESISTANCE, 1)
            .setMutatesInto(resistanceTwo)
            .build()
        resistanceTwo = registerGene("resistance_2")
            .setDnaPointsRequired(50)
            .setPotion(MobEffects.DAMAGE_RESISTANCE, 2)
            .build()
        scareCreepers = registerGene("scare_creepers")
            .setDnaPointsRequired(20)
            .setMutatesInto(scareZombies)
            .build()
        scareSkeletons = registerGene("scare_skeletons")
            .setDnaPointsRequired(20)
            .setMutatesInto(scareSpiders)
            .build()
        scareSpiders = registerGene("scare_spiders")
            .setDnaPointsRequired(50)
            .build()
        scareZombies = registerGene("scare_zombies")
            .setDnaPointsRequired(50)
            .build()
        shootFireballs = registerGene("shoot_fireballs")
            .setDnaPointsRequired(24)
            .build()
        slimyDeath = registerGene("slimy_death")
            .setDnaPointsRequired(60)
            .build()
        speed = registerGene("speed")
            .setDnaPointsRequired(20)
            .allowMobs()
            .setPotion(MobEffects.MOVEMENT_SPEED, 1)
            .setMutatesInto(speedTwo)
            .build()
        speedFour = registerGene("speed_4")
            .setDnaPointsRequired(50)
            .setPotion(MobEffects.MOVEMENT_SPEED, 4)
            .build()
        speedTwo = registerGene("speed_2")
            .setDnaPointsRequired(50)
            .setPotion(MobEffects.MOVEMENT_SPEED, 2)
            .setMutatesInto(speedFour)
            .build()
        stepAssist = registerGene("step_assist")
            .setDnaPointsRequired(10)
            .build()
        strength = registerGene("strength")
            .setDnaPointsRequired(20)
            .allowMobs()
            .setPotion(MobEffects.DAMAGE_BOOST, 1)
            .setMutatesInto(strengthTwo)
            .build()
        strengthTwo = registerGene("strength_2")
            .setDnaPointsRequired(50)
            .setPotion(MobEffects.DAMAGE_BOOST, 2)
            .build()
        teleport = registerGene("teleport")
            .setDnaPointsRequired(24)
            .setMutatesInto(flight)
            .build()
        thorns = registerGene("thorns")
            .setDnaPointsRequired(12)
            .allowMobs()
            .setMutatesInto(photosynthesis)
            .build()
        wallClimbing = registerGene("wall_climbing")
            .setDnaPointsRequired(40)
            .build()
        waterBreathing = registerGene("water_breathing")
            .setDnaPointsRequired(16)
            .allowMobs()
            .build()
        witherHit = registerGene("wither_hit")
            .setDnaPointsRequired(20)
            .allowMobs()
            .build()
        witherProof = registerGene("wither_proof")
            .setDnaPointsRequired(40)
            .allowMobs()
            .build()
        wooly = registerGene("wooly")
            .setDnaPointsRequired(12)
            .allowMobs()
            .build()
        xpMagnet = registerGene("xp_magnet")
            .setDnaPointsRequired(30)
            .build()

        //Negative effects
        blindness = registerGene("blindness")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.BLINDNESS, 1)
            .allowMobs()
            .setNegative()
            .build()
        cursed = registerGene("cursed")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.UNLUCK, 1)
            .allowMobs()
            .setNegative()
            .build()
        flambe = registerGene("flambe")
            .setDnaPointsRequired(1)
            .setNegative()
            .allowMobs()
            .build()
        hunger = registerGene("hunger")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.HUNGER, 1)
            .setNegative()
            .allowMobs()
            .build()
        levitation = registerGene("levitation")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.LEVITATION, 1)
            .allowMobs()
            .setNegative()
            .build()
        miningWeakness = registerGene("mining_weakness")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.DIG_SLOWDOWN, 1)
            .allowMobs()
            .setNegative()
            .build()
        nausea = registerGene("nausea")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.CONFUSION, 1)
            .allowMobs()
            .setNegative()
            .build()
        poison = registerGene("poison")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.POISON, 1)
            .allowMobs()
            .setNegative()
            .build()
        poisonFour = registerGene("poison_4")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.POISON, 4)
            .allowMobs()
            .setNegative()
            .build()
        slowness = registerGene("slowness")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.MOVEMENT_SLOWDOWN, 1)
            .allowMobs()
            .setNegative()
            .build()
        slownessFour = registerGene("slowness_4")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.MOVEMENT_SLOWDOWN, 4)
            .allowMobs()
            .setNegative()
            .build()
        slownessSix = registerGene("slowness_6")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.MOVEMENT_SLOWDOWN, 6)
            .allowMobs()
            .setNegative()
            .build()
        weakness = registerGene("weakness")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.WEAKNESS, 1)
            .allowMobs()
            .setNegative()
            .build()
        wither = registerGene("wither")
            .setDnaPointsRequired(1)
            .setPotion(MobEffects.WITHER, 1)
            .allowMobs()
            .setNegative()
            .build()

        blackDeath = registerGene("black_death")
            .setDnaPointsRequired(1)
            .setNegative()
            .allowMobs()
            .build()
        greenDeath = registerGene("green_death")
            .setDnaPointsRequired(1)
            .setNegative()
            .allowMobs()
            .build()
        whiteDeath = registerGene("white_death")
            .setDnaPointsRequired(1)
            .setNegative()
            .allowMobs()
            .build()
        grayDeath = registerGene("gray_death")
            .setDnaPointsRequired(1)
            .setNegative()
            .allowMobs()
            .build()
        unUndeath = registerGene("un_undeath")
            .setDnaPointsRequired(1)
            .setNegative()
            .allowMobs()
            .build()
    }

    private fun registerGene(geneId: String): GeneBuilder =
        GeneBuilder(OtherUtil.modResource(geneId))

    val defaultGeneRequirements: MutableMap<Gene, Set<Gene>> = mutableMapOf(
        clawsTwo to setOf(claws),
        efficiencyFour to setOf(efficiency),
        flight to setOf(teleport, jumpBoost, noFallDamage),
        hasteTwo to setOf(haste),
        meatyTwo to setOf(meaty),
        moreHeartsTwo to setOf(moreHearts),
        regenerationFour to setOf(regeneration),
        resistanceTwo to setOf(resistance),
        speedTwo to setOf(speed),
        speedFour to setOf(speedTwo),
        strengthTwo to setOf(strength),
        scareZombies to setOf(scareCreepers),
        scareSpiders to setOf(scareSkeletons),
        photosynthesis to setOf(thorns)
    )

    fun setGeneRequirements() {
        for ((gene: Gene, requiredGenes: Set<Gene>) in defaultGeneRequirements) {
            gene.addRequiredGenes(requiredGenes)
        }
    }

}