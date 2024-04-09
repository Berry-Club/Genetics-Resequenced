package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.network.chat.Component
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

    private lateinit var description: String

    private fun setDescription(description: String): Gene {
        this.description = description
        return this
    }

    fun getDescription(): Component {
        return Component.translatable(description)
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
        val HASTE_2 = Gene("HASTE_2")
            .setDescription("gene.geneticsresequenced.haste_2").build()
        val EFFICIENCY_4 = Gene("EFFICIENCY_4")
            .setDescription("gene.geneticsresequenced.efficiency_4").build()
        val REGENERATION_4 = Gene("REGENERATION_4")
            .setDescription("gene.geneticsresequenced.regeneration_4").build()
        val SPEED_4 = Gene("SPEED_4")
            .setDescription("gene.geneticsresequenced.speed_4").build()
        val SPEED_2 = Gene("SPEED_2")
            .setDescription("gene.geneticsresequenced.speed_2").setMutatesInto(SPEED_4).build()
        val RESISTANCE_2 = Gene("RESISTANCE_2")
            .setDescription("gene.geneticsresequenced.resistance_2").build()
        val STRENGTH_2 = Gene("STRENGTH_2")
            .setDescription("gene.geneticsresequenced.strength_2").build()
        val MEATY_2 = Gene("MEATY_2")
            .setDescription("gene.geneticsresequenced.meaty_2").build()
        val MORE_HEARTS_2 = Gene("MORE_HEARTS_2")
            .setDescription("gene.geneticsresequenced.more_hearts_2").build()
        val INVISIBLE = Gene("INVISIBLE")
            .setDescription("gene.geneticsresequenced.invisibility").build()
        val FLIGHT = Gene("FLIGHT")
            .setDescription("gene.geneticsresequenced.flight").build()
        val LUCK = Gene("LUCK")
            .setDescription("gene.geneticsresequenced.luck").build()
        val SCARE_ZOMBIES = Gene("SCARE_ZOMBIES")
            .setDescription("gene.geneticsresequenced.scare_zombies").build()
        val SCARE_SPIDERS = Gene("SCARE_SPIDERS")
            .setDescription("gene.geneticsresequenced.scare_spiders").build()
        val THORNS = Gene("THORNS")
            .setDescription("gene.geneticsresequenced.thorns").build()
        val CLAWS_2 = Gene("CLAWS_2")
            .setDescription("gene.geneticsresequenced.claws_2").build()

        //Standard list
        val DRAGONS_BREATH = Gene("DRAGONS_BREATH")
            .setDescription("gene.geneticsresequenced.dragons_breath").build()
        val EAT_GRASS = Gene("EAT_GRASS")
            .setDescription("gene.geneticsresequenced.eat_grass").build()
        val EMERALD_HEART = Gene("EMERALD_HEART")
            .setDescription("gene.geneticsresequenced.emerald_heart").build()
        val ENDER_DRAGON_HEALTH = Gene("ENDER_DRAGON_HEALTH")
            .setDescription("gene.geneticsresequenced.ender_dragon_health").build()
        val EXPLOSIVE_EXIT = Gene("EXPLOSIVE_EXIT")
            .setDescription("gene.geneticsresequenced.explosive_exit").build()
        val FIRE_PROOF = Gene("FIRE_PROOF")
            .setDescription("gene.geneticsresequenced.fire_proof").build()
        val ITEM_MAGNET = Gene("ITEM_MAGNET")
            .setDescription("gene.geneticsresequenced.item_magnet").build()
        val JUMP_BOOST = Gene("JUMP_BOOST")
            .setDescription("gene.geneticsresequenced.jump_boost").setMutatesInto(FLIGHT).build()
        val MILKY = Gene("MILKY")
            .setDescription("gene.geneticsresequenced.milky").build()
        val MORE_HEARTS = Gene("MORE_HEARTS")
            .setDescription("gene.geneticsresequenced.more_hearts").setMutatesInto(MORE_HEARTS_2).build()
        val NIGHT_VISION = Gene("NIGHT_VISION")
            .setDescription("gene.geneticsresequenced.night_vision").build()
        val NO_FALL_DAMAGE = Gene("NO_FALL_DAMAGE")
            .setDescription("gene.geneticsresequenced.no_fall_damage").build()
        val PHOTOSYNTHESIS = Gene("PHOTOSYNTHESIS")
            .setDescription("gene.geneticsresequenced.photosynthesis").setMutatesInto(THORNS).build()
        val POISON_IMMUNITY = Gene("POISON_IMMUNITY")
            .setDescription("gene.geneticsresequenced.poison_immunity").build()
        val RESISTANCE = Gene("RESISTANCE")
            .setDescription("gene.geneticsresequenced.resistance").setMutatesInto(RESISTANCE_2)
        val KEEP_INVENTORY = Gene("KEEP_INVENTORY")
            .setDescription("gene.geneticsresequenced.keep_inventory").build()
        val SCARE_CREEPERS = Gene("SCARE_CREEPERS")
            .setDescription("gene.geneticsresequenced.scare_creepers").setMutatesInto(SCARE_ZOMBIES).build()
        val SCARE_SKELETONS = Gene("SCARE_SKELETONS")
            .setDescription("gene.geneticsresequenced.scare_skeletons").setMutatesInto(SCARE_SPIDERS).build()
        val SHOOT_FIREBALLS = Gene("SHOOT_FIREBALLS")
            .setDescription("gene.geneticsresequenced.shoot_fireballs").build()
        val SLIMY_DEATH = Gene("SLIMY_DEATH")
            .setDescription("gene.geneticsresequenced.slimy_death").build()
        val SPEED = Gene("SPEED")
            .setDescription("gene.geneticsresequenced").setMutatesInto(SPEED_2).build()
        val STRENGTH = Gene("STRENGTH")
            .setDescription("gene.geneticsresequenced.strength").setMutatesInto(STRENGTH_2).build()
        val TELEPORT = Gene("TELEPORT")
            .setDescription("gene.geneticsresequenced.teleport").setMutatesInto(FLIGHT).build()
        val WATER_BREATHING = Gene("WATER_BREATHING")
            .setDescription("gene.geneticsresequenced.water_breathing").build()
        val WOOLY = Gene("WOOLY")
            .setDescription("gene.geneticsresequenced.wooly").build()
        val WITHER_HIT = Gene("WITHER_HIT")
            .setDescription("gene.geneticsresequenced.wither_hit").build()
        val WITHER_PROOF = Gene("WITHER_PROOF")
            .setDescription("gene.geneticsresequenced.wither_proof").build()
        val XP_MAGNET = Gene("XP_MAGNET")
            .setDescription("gene.geneticsresequenced.xp_magnet").build()
        val STEP_ASSIST = Gene("STEP_ASSIST")
            .setDescription("gene.geneticsresequenced.step_assist").build()
        val INFINITY = Gene("INFINITY")
            .setDescription("gene.geneticsresequenced.infinity").build()
        val BIOLUMINESCENCE = Gene("BIOLUMINESCENCE")
            .setDescription("gene.geneticsresequenced.bioluminescence").build()
        val CYBERNETIC = Gene("CYBERNETIC")
            .setDescription("gene.geneticsresequenced.cybernetics").build()
        val LAY_EGG = Gene("LAY_EGG")
            .setDescription("gene.geneticsresequenced.lay_eggs").build()
        val MEATY = Gene("MEATY")
            .setDescription("gene.geneticsresequenced.meaty").setMutatesInto(MEATY_2).build()
        val NO_HUNGER = Gene("NO_HUNGER")
            .setDescription("gene.geneticsresequenced.no_hunger").build()
        val CLAWS = Gene("CLAWS")
            .setDescription("gene.geneticsresequenced.claws").setMutatesInto(CLAWS_2).build()
        val HASTE = Gene("HASTE")
            .setDescription("gene.geneticsresequenced.haste").setMutatesInto(HASTE_2).build()
        val EFFICIENCY = Gene("EFFICIENCY")
            .setDescription("gene.geneticsresequenced.efficiency").setMutatesInto(EFFICIENCY_4).build()
        val WALL_CLIMBING = Gene("WALL_CLIMBING")
            .setDescription("gene.geneticsresequenced.climb_walls").build()
        val MOB_SIGHT = Gene("MOB_SIGHT")
            .setDescription("gene.geneticsresequenced.mob_sight").build()
        val REGENERATION = Gene("REGENERATION")
            .setDescription("gene.geneticsresequenced.regeneration").setMutatesInto(REGENERATION_4).build()

        //Negative effects
        val POISON = Gene("POISON")
            .setDescription("gene.geneticsresequenced.poison_2").setNegative().build()
        val POISON_4 = Gene("POISON_4")
            .setDescription("gene.geneticsresequenced.poison_4").setNegative().build()
        val WITHER = Gene("WITHER")
            .setDescription("gene.geneticsresequenced.wither_2").setNegative().build()
        val WEAKNESS = Gene("WEAKNESS")
            .setDescription("gene.geneticsresequenced.weakness").setNegative().build()
        val BLINDNESS = Gene("BLINDNESS")
            .setDescription("gene.geneticsresequenced.blindness").setNegative().build()
        val SLOWNESS = Gene("SLOWNESS")
            .setDescription("gene.geneticsresequenced.slowness").setNegative().build()
        val SLOWNESS_4 = Gene("SLOWNESS_4")
            .setDescription("gene.geneticsresequenced.slowness_4").setNegative().build()
        val SLOWNESS_6 = Gene("SLOWNESS_6")
            .setDescription("gene.geneticsresequenced.slowness_6").setNegative().build()
        val NAUSEA = Gene("NAUSEA")
            .setDescription("gene.geneticsresequenced.nausea").setNegative().build()
        val HUNGER = Gene("HUNGER")
            .setDescription("gene.geneticsresequenced.hunger").setNegative().build()
        val FLAME = Gene("FLAME")
            .setDescription("gene.geneticsresequenced.flambe").setNegative().build()
        val CURSED = Gene("CURSED")
            .setDescription("gene.geneticsresequenced.cursed").setNegative().build()
        val LEVITATION = Gene("LEVITATION")
            .setDescription("gene.geneticsresequenced.levitation").setNegative().build()
        val MINING_WEAKNESS = Gene("MINING_WEAKNESS")
            .setDescription("gene.geneticsresequenced.mining_weakness").setNegative().build()
        val DEAD_CREEPERS = Gene("DEAD_CREEPERS")
            .setDescription("gene.geneticsresequenced.green_death").setNegative().build()
        val DEAD_UNDEAD = Gene("DEAD_UNDEAD")
            .setDescription("gene.geneticsresequenced.undeath").setNegative().build()
        val DEAD_OLD_AGE = Gene("DEAD_OLD_AGE")
            .setDescription("gene.geneticsresequenced.gray_death").setNegative().build()
        val DEAD_HOSTILE = Gene("DEAD_HOSTILE")
            .setDescription("gene.geneticsresequenced.white_death").setNegative().build()
        val DEAD_ALL = Gene("DEAD_ALL")
            .setDescription("gene.geneticsresequenced.black_death").setNegative().build()
        val REALLY_DEAD_ALL = Gene("REALLY_DEAD_ALL")
            .setDescription("gene.geneticsresequenced.void_death").setNegative().build()
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