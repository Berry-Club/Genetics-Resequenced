package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

@Suppress("unused")
class Gene(
    val id: String
) {

    var isNegative: Boolean = false
        private set

    private fun setNegative(): Gene {
        this.isNegative = true
        return this
    }

    private var mutatesInto: Gene? = null
    private fun setMutatesInto(mutatesInto: Gene): Gene {
        this.mutatesInto = mutatesInto
        return this
    }

    private val isMutation: Boolean
        get() = REGISTRY.any { it.mutatesInto == this }

    lateinit var description: String
        private set

    private fun setDescription(description: String): Gene {
        this.description = description
        return this
    }

    private fun build(): Gene {
        REGISTRY.add(this)
        return this
    }

    companion object {

        val REGISTRY: MutableSet<Gene> = mutableSetOf()

        fun valueOf(name: String): Gene {
            return REGISTRY.first { it.id == name }
        }

        //Mutations, must be first
        val HASTE_2 = Gene("HASTE_2").setDescription("Haste II").build()
        val EFFICIENCY_4 = Gene("EFFICIENCY_4").setDescription("Efficiency IV").build()
        val REGENERATION_4 = Gene("REGENERATION_4").setDescription("Regeneration IV").build()
        val SPEED_4 = Gene("SPEED_4").setDescription("Speed IV").build()
        val SPEED_2 = Gene("SPEED_2").setDescription("Speed II").setMutatesInto(SPEED_4).build()
        val RESISTANCE_2 = Gene("RESISTANCE_2").setDescription("Resistance II").build()
        val STRENGTH_2 = Gene("STRENGTH_2").setDescription("Strength II").build()
        val MEATY_2 = Gene("MEATY_2").setDescription("Meaty II").build()
        val MORE_HEARTS_2 = Gene("MORE_HEARTS_2").setDescription("More Hearts II").build()
        val INVISIBLE = Gene("INVISIBLE").setDescription("Invisibility").build()
        val FLIGHT = Gene("FLIGHT").setDescription("Flight").build()
        val LUCK = Gene("LUCK").setDescription("Luck").build()
        val SCARE_ZOMBIES = Gene("SCARE_ZOMBIES").setDescription("Scare Zombies").build()
        val SCARE_SPIDERS = Gene("SCARE_SPIDERS").setDescription("Scare Spiders").build()
        val THORNS = Gene("THORNS").setDescription("Thorns").build()
        val CLAWS_2 = Gene("CLAWS_2").setDescription("Claws II").build()

        //Standard list
        val DRAGONS_BREATH = Gene("DRAGONS_BREATH").setDescription("Dragon's Breath").build()
        val EAT_GRASS = Gene("EAT_GRASS").setDescription("Eat Grass").build()
        val EMERALD_HEART = Gene("EMERALD_HEART").setDescription("Emerald Heart").build()
        val ENDER_DRAGON_HEALTH = Gene("ENDER_DRAGON_HEALTH").setDescription("Ender Dragon Health").build()
        val EXPLOSIVE_EXIT = Gene("EXPLOSIVE_EXIT").setDescription("Explosive Exit").build()
        val FIRE_PROOF = Gene("FIRE_PROOF").setDescription("Fire Proof").build()
        val ITEM_MAGNET = Gene("ITEM_MAGNET").setDescription("Item Magnet").build()
        val JUMP_BOOST = Gene("JUMP_BOOST").setDescription("Jump Boost").setMutatesInto(FLIGHT).build()
        val MILKY = Gene("MILKY").setDescription("Milky").build()
        val MORE_HEARTS = Gene("MORE_HEARTS").setDescription("More Hearts").setMutatesInto(MORE_HEARTS_2).build()
        val NIGHT_VISION = Gene("NIGHT_VISION").setDescription("Night Vision").build()
        val NO_FALL_DAMAGE = Gene("NO_FALL_DAMAGE").setDescription("No Fall Damage").build()
        val PHOTOSYNTHESIS = Gene("PHOTOSYNTHESIS").setDescription("Photosynthesis").setMutatesInto(THORNS).build()
        val POISON_IMMUNITY = Gene("POISON_IMMUNITY").setDescription("Poison Immunity").build()
        val RESISTANCE = Gene("RESISTANCE").setDescription("Resistance").setMutatesInto(RESISTANCE_2)
        val KEEP_INVENTORY = Gene("KEEP_INVENTORY").setDescription("Keep Inventory").build()
        val SCARE_CREEPERS =
            Gene("SCARE_CREEPERS").setDescription("Scare Creepers").setMutatesInto(SCARE_ZOMBIES).build()
        val SCARE_SKELETONS =
            Gene("SCARE_SKELETONS").setDescription("Scare Skeletons").setMutatesInto(SCARE_SPIDERS).build()
        val SHOOT_FIREBALLS = Gene("SHOOT_FIREBALLS").setDescription("Shoot Fireballs").build()
        val SLIMY_DEATH = Gene("SLIMY_DEATH").setDescription("Slimy Death").build()
        val SPEED = Gene("SPEED").setDescription("Speed").setMutatesInto(SPEED_2).build()
        val STRENGTH = Gene("STRENGTH").setDescription("Strength").setMutatesInto(STRENGTH_2).build()
        val TELEPORT = Gene("TELEPORT").setDescription("Teleport").setMutatesInto(FLIGHT).build()
        val WATER_BREATHING = Gene("WATER_BREATHING").setDescription("Water Breathing").build()
        val WOOLY = Gene("WOOLY").setDescription("Wooly").build()
        val WITHER_HIT = Gene("WITHER_HIT").setDescription("Wither Hit").build()
        val WITHER_PROOF = Gene("WITHER_PROOF").setDescription("Wither Proof").build()
        val XP_MAGNET = Gene("XP_MAGNET").setDescription("XP Magnet").build()
        val STEP_ASSIST = Gene("STEP_ASSIST").setDescription("Step Assist").build()
        val INFINITY = Gene("INFINITY").setDescription("Infinity").build()
        val BIOLUMINESCENCE = Gene("BIOLUMINESCENCE").setDescription("Bioluminescence").build()
        val CYBERNETIC = Gene("CYBERNETIC").setDescription("Cybernetic").build()
        val LAY_EGG = Gene("LAY_EGG").setDescription("Lay Eggs").build()
        val MEATY = Gene("MEATY").setDescription("Meaty").setMutatesInto(MEATY_2).build()
        val NO_HUNGER = Gene("NO_HUNGER").setDescription("No Hunger").build()
        val CLAWS = Gene("CLAWS").setDescription("Claws").setMutatesInto(CLAWS_2).build()
        val HASTE = Gene("HASTE").setDescription("Haste").setMutatesInto(HASTE_2).build()
        val EFFICIENCY = Gene("EFFICIENCY").setDescription("Efficiency").setMutatesInto(EFFICIENCY_4).build()
        val WALL_CLIMBING = Gene("WALL_CLIMBING").setDescription("Climb Walls").build()
        val MOB_SIGHT = Gene("MOB_SIGHT").setDescription("Mob Sight").build()
        val REGENERATION = Gene("REGENERATION").setDescription("Regeneration").setMutatesInto(REGENERATION_4).build()

        //Negative effects
        val POISON = Gene("POISON").setDescription("Poison II").setNegative().build()
        val POISON_4 = Gene("POISON_4").setDescription("Poison IV").setNegative().build()
        val WITHER = Gene("WITHER").setDescription("Wither II").setNegative().build()
        val WEAKNESS = Gene("WEAKNESS").setDescription("Weakness").setNegative().build()
        val BLINDNESS = Gene("BLINDNESS").setDescription("Blindness").setNegative().build()
        val SLOWNESS = Gene("SLOWNESS").setDescription("Slowness").setNegative().build()
        val SLOWNESS_4 = Gene("SLOWNESS_4").setDescription("Slowness IV").setNegative().build()
        val SLOWNESS_6 = Gene("SLOWNESS_6").setDescription("Slowness VI").setNegative().build()
        val NAUSEA = Gene("NAUSEA").setDescription("Nausea").setNegative().build()
        val HUNGER = Gene("HUNGER").setDescription("Hunger").setNegative().build()
        val FLAME = Gene("FLAME").setDescription("Flambe").setNegative().build()
        val CURSED = Gene("CURSED").setDescription("Cursed").setNegative().build()
        val LEVITATION = Gene("LEVITATION").setDescription("Levitation").setNegative().build()
        val MINING_WEAKNESS = Gene("MINING_WEAKNESS").setDescription("Mining Weakness").setNegative().build()
        val DEAD_CREEPERS = Gene("DEAD_CREEPERS").setDescription("Green Death").setNegative().build()
        val DEAD_UNDEAD = Gene("DEAD_UNDEAD").setDescription("Un-Death").setNegative().build()
        val DEAD_OLD_AGE = Gene("DEAD_OLD_AGE").setDescription("Gray Death").setNegative().build()
        val DEAD_HOSTILE = Gene("DEAD_HOSTILE").setDescription("White Death").setNegative().build()
        val DEAD_ALL = Gene("DEAD_ALL").setDescription("Black Death").setNegative().build()
        val REALLY_DEAD_ALL = Gene("REALLY_DEAD_ALL").setDescription("Void Death").setNegative().build()
    }

    var isActive: Boolean = true

    fun getPotion(): MobEffectInstance? {

        val effect = when (this) {
            HASTE, HASTE_2 -> MobEffects.DIG_SPEED
            REGENERATION, REGENERATION_4 -> MobEffects.REGENERATION
            SPEED, SPEED_2, SPEED_4 -> MobEffects.MOVEMENT_SPEED
            RESISTANCE, RESISTANCE_2 -> MobEffects.DAMAGE_RESISTANCE
            STRENGTH, STRENGTH_2 -> MobEffects.DAMAGE_BOOST
            INVISIBLE -> MobEffects.INVISIBILITY
            LUCK -> MobEffects.LUCK
            NIGHT_VISION -> MobEffects.NIGHT_VISION
            JUMP_BOOST -> MobEffects.JUMP
            POISON, POISON_4 -> MobEffects.POISON
            WEAKNESS -> MobEffects.WEAKNESS
            BLINDNESS -> MobEffects.BLINDNESS
            SLOWNESS, SLOWNESS_4, SLOWNESS_6 -> MobEffects.MOVEMENT_SLOWDOWN
            NAUSEA -> MobEffects.CONFUSION
            HUNGER -> MobEffects.HUNGER
            CURSED -> MobEffects.UNLUCK
            LEVITATION -> MobEffects.LEVITATION
            MINING_WEAKNESS -> MobEffects.DIG_SLOWDOWN
            WITHER -> MobEffects.WITHER
            else -> return null
        }

        val level = when (this) {
            HASTE_2, REGENERATION_4, SPEED_2, RESISTANCE_2, STRENGTH_2 -> 1
            SPEED_4, POISON_4, SLOWNESS_4 -> 3
            SLOWNESS_6 -> 5
            else -> 0
        }

        return MobEffectInstance(effect, 300, level, true, false, ServerConfig.showEffectIcons.get())
    }

    fun canAddMutation(genes: GenesCapability, syringeGenes: GenesCapability): Boolean {
        return when (this) {
            HASTE_2 -> genes.hasGene(HASTE) || syringeGenes.hasGene(HASTE)
            EFFICIENCY_4 -> genes.hasGene(EFFICIENCY) || syringeGenes.hasGene(EFFICIENCY)
            REGENERATION_4 -> genes.hasGene(REGENERATION) || syringeGenes.hasGene(REGENERATION)
            SPEED_4 -> genes.hasGene(SPEED_2) || syringeGenes.hasGene(SPEED_2)
            SPEED_2 -> genes.hasGene(SPEED) || syringeGenes.hasGene(SPEED)
            RESISTANCE_2 -> genes.hasGene(RESISTANCE) || syringeGenes.hasGene(RESISTANCE)
            STRENGTH_2 -> genes.hasGene(STRENGTH) || syringeGenes.hasGene(STRENGTH)
            MEATY_2 -> genes.hasGene(MEATY) || syringeGenes.hasGene(MEATY)
            MORE_HEARTS_2 -> genes.hasGene(MORE_HEARTS) || syringeGenes.hasGene(MORE_HEARTS)
            INVISIBLE -> true
            FLIGHT -> genes.hasGene(JUMP_BOOST) || genes.hasGene(TELEPORT) || genes.hasGene(NO_FALL_DAMAGE) ||
                    syringeGenes.hasGene(JUMP_BOOST) || syringeGenes.hasGene(TELEPORT) || syringeGenes.hasGene(
                NO_FALL_DAMAGE
            )

            LUCK -> true
            SCARE_ZOMBIES -> genes.hasGene(SCARE_CREEPERS) || syringeGenes.hasGene(SCARE_CREEPERS)
            SCARE_SPIDERS -> genes.hasGene(SCARE_SKELETONS) || syringeGenes.hasGene(SCARE_SKELETONS)
            THORNS -> genes.hasGene(PHOTOSYNTHESIS) || syringeGenes.hasGene(PHOTOSYNTHESIS)
            CLAWS_2 -> genes.hasGene(CLAWS) || syringeGenes.hasGene(CLAWS)
            else -> true
        }
    }

    fun getNumberNeeded(gene: Gene): Int {
        if (gene.isMutation) return 50
        return if (ServerConfig.hardMode.get()) 24 else when (gene) {
            STEP_ASSIST, JUMP_BOOST -> 10
            MILKY, WOOLY, MEATY, LAY_EGG, THORNS -> 12
            EAT_GRASS, NIGHT_VISION, MOB_SIGHT, WATER_BREATHING, BIOLUMINESCENCE -> 16
            DRAGONS_BREATH, SCARE_CREEPERS, SCARE_SKELETONS, WITHER_HIT, SPEED, CLAWS, STRENGTH, EXPLOSIVE_EXIT -> 20
            FIRE_PROOF, POISON_IMMUNITY, SHOOT_FIREBALLS, TELEPORT -> 24
            EMERALD_HEART, NO_FALL_DAMAGE, NO_HUNGER, RESISTANCE, XP_MAGNET, ITEM_MAGNET, INFINITY, CYBERNETIC -> 30
            WITHER_PROOF, MORE_HEARTS, WALL_CLIMBING, KEEP_INVENTORY, PHOTOSYNTHESIS -> 40
            REGENERATION, ENDER_DRAGON_HEALTH, SLIMY_DEATH -> 60
            else -> 22
        }
    }
}