package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneProperties
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.effect.MobEffects
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModGenes {

    val GENE_REGISTRY: DeferredRegister<Gene> =
        DeferredRegister.create(GeneRegistry.GENE_REGISTRY, GeneticsResequenced.ID)

    private fun registerGene(name: String, geneProperties: () -> GeneProperties): DeferredHolder<Gene, Gene> {
        return GENE_REGISTRY.register(name, Supplier {
            Gene(geneProperties())
        })
    }

    val BASIC = registerGene("basic") {
        GeneProperties(
            id = OtherUtil.modResource("basic"),
            isHidden = true
        )
    }

    // Mutations (must be initialized first because they're used in arguments in ones below)

    val CLAWS_TWO = registerGene("claws_2") {
        GeneProperties(
            id = OtherUtil.modResource("claws_2"),
            dnaPointsRequired = 50,
            canMobsHave = true
        )
    }

    val EFFICIENCY_FOUR = registerGene("efficiency_4") {
        GeneProperties(
            id = OtherUtil.modResource("efficiency_4"),
            dnaPointsRequired = 50
        )
    }

    val FLIGHT = registerGene("flight") {
        GeneProperties(
            id = OtherUtil.modResource("flight"),
            dnaPointsRequired = 50
        )
    }

    val HASTE_TWO = registerGene("haste_2") {
        GeneProperties(
            id = OtherUtil.modResource("haste_2"),
            dnaPointsRequired = 50
        )
    }

    val MEATY_TWO = registerGene("meaty_2") {
        GeneProperties(
            id = OtherUtil.modResource("meaty_2"),
            dnaPointsRequired = 50,
            canMobsHave = true
        )
    }

    val MORE_HEARTS_TWO = registerGene("more_hearts_2") {
        GeneProperties(
            id = OtherUtil.modResource("more_hearts_2"),
            dnaPointsRequired = 50,
            canMobsHave = true
        )
    }

    val PHOTOSYNTHESIS = registerGene("photosynthesis") {
        GeneProperties(
            id = OtherUtil.modResource("photosynthesis"),
            dnaPointsRequired = 40
        )
    }

    val REGENERATION_FOUR = registerGene("regeneration_4") {
        GeneProperties(
            id = OtherUtil.modResource("regeneration_4"),
            dnaPointsRequired = 50
        )
    }

    val RESISTANCE_TWO = registerGene("resistance_2") {
        GeneProperties(
            id = OtherUtil.modResource("resistance_2"),
            dnaPointsRequired = 50
        )
    }

    val SPEED_FOUR = registerGene("speed_4") {
        GeneProperties(
            id = OtherUtil.modResource("speed_4"),
            dnaPointsRequired = 50
        )
    }

    val SPEED_TWO = registerGene("speed_2") {
        GeneProperties(
            id = OtherUtil.modResource("speed_2"),
            dnaPointsRequired = 50
        )
    }

    val STRENGTH_TWO = registerGene("strength_2") {
        GeneProperties(
            id = OtherUtil.modResource("strength_2"),
            dnaPointsRequired = 50
        )
    }

    val SCARE_ZOMBIES = registerGene("scare_zombies") {
        GeneProperties(
            id = OtherUtil.modResource("scare_zombies"),
            dnaPointsRequired = 50
        )
    }

    val SCARE_SPIDERS = registerGene("scare_spiders") {
        GeneProperties(
            id = OtherUtil.modResource("scare_spiders"),
            dnaPointsRequired = 50
        )
    }

    //Standard list

    val BIO_LUMINESCENCE = registerGene("bioluminescence") {
        GeneProperties(
            id = OtherUtil.modResource("bioluminescence"),
            dnaPointsRequired = 16,
            canMobsHave = true
        )
    }

    val CHATTERBOX = registerGene("chatterbox") {
        GeneProperties(
            id = OtherUtil.modResource("chatterbox"),
            dnaPointsRequired = 20
        )
    }

    val CHILLING = registerGene("chilling") {
        GeneProperties(
            id = OtherUtil.modResource("chilling"),
            dnaPointsRequired = 20,
            canMobsHave = true
        )
    }

    val CLAWS = registerGene("claws") {
        GeneProperties(
            id = OtherUtil.modResource("claws"),
            dnaPointsRequired = 20,
            canMobsHave = true,
            mutatesInto = CLAWS_TWO.get()
        )
    }

    val DRAGON_BREATH = registerGene("dragons_breath") {
        GeneProperties(
            id = OtherUtil.modResource("dragons_breath"),
            dnaPointsRequired = 20
        )
    }

    val EAT_GRASS = registerGene("eat_grass") {
        GeneProperties(
            id = OtherUtil.modResource("eat_grass"),
            dnaPointsRequired = 16,
        )
    }

    val EFFICIENCY = registerGene("efficiency") {
        GeneProperties(
            id = OtherUtil.modResource("efficiency"),
            dnaPointsRequired = 30,
            mutatesInto = EFFICIENCY_FOUR.get()
        )
    }

    val EMERALD_HEART = registerGene("emerald_heart") {
        GeneProperties(
            id = OtherUtil.modResource("emerald_heart"),
            dnaPointsRequired = 30,
            canMobsHave = true
        )
    }

    val ENDER_DRAGON_HEALTH = registerGene("ender_dragon_health") {
        GeneProperties(
            id = OtherUtil.modResource("ender_dragon_health"),
            dnaPointsRequired = 60
        )
    }

    val EXPLOSIVE_EXIT = registerGene("explosive_exit") {
        GeneProperties(
            id = OtherUtil.modResource("explosive_exit"),
            dnaPointsRequired = 20,
            canMobsHave = true
        )
    }

    val FIRE_PROOF = registerGene("fire_proof") {
        GeneProperties(
            id = OtherUtil.modResource("fire_proof"),
            dnaPointsRequired = 24,
            canMobsHave = true
        )
    }

    val HASTE = registerGene("haste") {
        GeneProperties(
            id = OtherUtil.modResource("haste"),
            dnaPointsRequired = 30,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.DIG_SPEED,
                level = 1
            ),
            mutatesInto = HASTE_TWO.get()
        )
    }

    val INFINITY = registerGene("infinity") {
        GeneProperties(
            id = OtherUtil.modResource("infinity"),
            dnaPointsRequired = 30
        )
    }

    val INVISIBLE = registerGene("invisible") {
        GeneProperties(
            id = OtherUtil.modResource("invisible"),
            dnaPointsRequired = 50,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.INVISIBILITY,
                level = 1
            )
        )
    }

    val ITEM_MAGNET = registerGene("item_magnet") {
        GeneProperties(
            id = OtherUtil.modResource("item_magnet"),
            dnaPointsRequired = 30
        )
    }

    val JUMP_BOOST = registerGene("jump_boost") {
        GeneProperties(
            id = OtherUtil.modResource("jump_boost"),
            dnaPointsRequired = 10,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.JUMP,
                level = 1
            ),
            mutatesInto = FLIGHT.get()
        )
    }

    val JOHNNY = registerGene("johnny") {
        GeneProperties(
            id = OtherUtil.modResource("johnny"),
            dnaPointsRequired = 20,
            canMobsHave = true
        )
    }

    val KEEP_INVENTORY = registerGene("keep_inventory") {
        GeneProperties(
            id = OtherUtil.modResource("keep_inventory"),
            dnaPointsRequired = 40
        )
    }

    val KNOCKBACK = registerGene("knockback") {
        GeneProperties(
            id = OtherUtil.modResource("knockback"),
            dnaPointsRequired = 20,
            canMobsHave = true
        )
    }

    val LAY_EGG = registerGene("lay_egg") {
        GeneProperties(
            id = OtherUtil.modResource("lay_egg"),
            dnaPointsRequired = 12,
            canMobsHave = true
        )
    }

    val LUCK = registerGene("luck") {
        GeneProperties(
            id = OtherUtil.modResource("luck"),
            dnaPointsRequired = 50,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.LUCK,
                level = 1
            )
        )
    }

    val MEATY = registerGene("meaty") {
        GeneProperties(
            id = OtherUtil.modResource("meaty"),
            dnaPointsRequired = 12,
            canMobsHave = true,
            mutatesInto = MEATY_TWO.get()
        )
    }

    val MILKY = registerGene("milky") {
        GeneProperties(
            id = OtherUtil.modResource("milky"),
            dnaPointsRequired = 12,
            canMobsHave = true
        )
    }

    val MOB_SIGHT = registerGene("mob_sight") {
        GeneProperties(
            id = OtherUtil.modResource("mob_sight"),
            dnaPointsRequired = 16
        )
    }

    val MORE_HEARTS = registerGene("more_hearts") {
        GeneProperties(
            id = OtherUtil.modResource("more_hearts"),
            dnaPointsRequired = 40,
            canMobsHave = true,
            mutatesInto = MORE_HEARTS_TWO.get()
        )
    }

    val NIGHT_VISION = registerGene("night_vision") {
        GeneProperties(
            id = OtherUtil.modResource("night_vision"),
            dnaPointsRequired = 16,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.NIGHT_VISION,
                level = 1
            )
        )
    }

    val NO_FALL_DAMAGE = registerGene("no_fall_damage") {
        GeneProperties(
            id = OtherUtil.modResource("no_fall_damage"),
            dnaPointsRequired = 30,
            canMobsHave = true
        )
    }

    val NO_HUNGER = registerGene("no_hunger") {
        GeneProperties(
            id = OtherUtil.modResource("no_hunger"),
            dnaPointsRequired = 30
        )
    }

    val POISON_IMMUNITY = registerGene("poison_immunity") {
        GeneProperties(
            id = OtherUtil.modResource("poison_immunity"),
            dnaPointsRequired = 24,
            canMobsHave = true
        )
    }

    val REGENERATION = registerGene("regeneration") {
        GeneProperties(
            id = OtherUtil.modResource("regeneration"),
            dnaPointsRequired = 60,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.REGENERATION,
                level = 1
            ),
            mutatesInto = REGENERATION_FOUR.get()
        )
    }

    val RESISTANCE = registerGene("resistance") {
        GeneProperties(
            id = OtherUtil.modResource("resistance"),
            dnaPointsRequired = 30,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.DAMAGE_RESISTANCE,
                level = 1
            ),
            mutatesInto = RESISTANCE_TWO.get()
        )
    }

    val SCARE_CREEPERS = registerGene("scare_creepers") {
        GeneProperties(
            id = OtherUtil.modResource("scare_creepers"),
            dnaPointsRequired = 20,
            mutatesInto = SCARE_ZOMBIES.get()
        )
    }

    val SCARE_SKELETONS = registerGene("scare_skeletons") {
        GeneProperties(
            id = OtherUtil.modResource("scare_skeletons"),
            dnaPointsRequired = 20,
            mutatesInto = SCARE_SPIDERS.get()
        )
    }

    val SHOOT_FIREBALLS = registerGene("shoot_fireballs") {
        GeneProperties(
            id = OtherUtil.modResource("shoot_fireballs"),
            dnaPointsRequired = 24
        )
    }

    val SLIMY_DEATH = registerGene("slimy_death") {
        GeneProperties(
            id = OtherUtil.modResource("slimy_death"),
            dnaPointsRequired = 60
        )
    }

    val SPEED = registerGene("speed") {
        GeneProperties(
            id = OtherUtil.modResource("speed"),
            dnaPointsRequired = 20,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.MOVEMENT_SPEED,
                level = 1
            ),
            mutatesInto = SPEED_TWO.get()
        )
    }

    val STEP_ASSIST = registerGene("step_assist") {
        GeneProperties(
            id = OtherUtil.modResource("step_assist"),
            dnaPointsRequired = 10
        )
    }

    val STRENGTH = registerGene("strength") {
        GeneProperties(
            id = OtherUtil.modResource("strength"),
            dnaPointsRequired = 20,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.DAMAGE_BOOST,
                level = 1
            ),
            mutatesInto = STRENGTH_TWO.get()
        )
    }

    val TELEPORT = registerGene("teleport") {
        GeneProperties(
            id = OtherUtil.modResource("teleport"),
            dnaPointsRequired = 24,
            mutatesInto = FLIGHT.get()
        )
    }

    val THORNS = registerGene("thorns") {
        GeneProperties(
            id = OtherUtil.modResource("thorns"),
            dnaPointsRequired = 12,
            canMobsHave = true
        )
    }

    val WALL_CLIMBING = registerGene("wall_climbing") {
        GeneProperties(
            id = OtherUtil.modResource("wall_climbing"),
            dnaPointsRequired = 40
        )
    }

    val WATER_BREATHING = registerGene("water_breathing") {
        GeneProperties(
            id = OtherUtil.modResource("water_breathing"),
            dnaPointsRequired = 16,
            canMobsHave = true
        )
    }

    val WITHER_HIT = registerGene("wither_hit") {
        GeneProperties(
            id = OtherUtil.modResource("wither_hit"),
            dnaPointsRequired = 20,
            canMobsHave = true
        )
    }

    val WITHER_PROOF = registerGene("wither_proof") {
        GeneProperties(
            id = OtherUtil.modResource("wither_proof"),
            dnaPointsRequired = 40,
            canMobsHave = true
        )
    }

    val WOOLY = registerGene("wooly") {
        GeneProperties(
            id = OtherUtil.modResource("wooly"),
            dnaPointsRequired = 12,
            canMobsHave = true
        )
    }

    val XP_MAGNET = registerGene("xp_magnet") {
        GeneProperties(
            id = OtherUtil.modResource("xp_magnet"),
            dnaPointsRequired = 30
        )
    }

    //Negative effects

    //FIXME: This effect apparently has a sound on add now, probably want to remove that
    val BAD_OMEN = registerGene("bad_omen") {
        GeneProperties(
            id = OtherUtil.modResource("bad_omen"),
            dnaPointsRequired = 20,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.BAD_OMEN,
                level = 1
            )
        )
    }

    val BLINDNESS = registerGene("blindness") {
        GeneProperties(
            id = OtherUtil.modResource("blindness"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.BLINDNESS,
                level = 1
            )
        )
    }

    val CRINGE = registerGene("cringe") {
        GeneProperties(
            id = OtherUtil.modResource("cringe"),
            dnaPointsRequired = 20,
            isNegative = true
        )
    }

    val CURSED = registerGene("cursed") {
        GeneProperties(
            id = OtherUtil.modResource("cursed"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.UNLUCK,
                level = 1
            )
        )
    }

    val FLAMBE = registerGene("flambe") {
        GeneProperties(
            id = OtherUtil.modResource("flambe"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true
        )
    }

    val HUNGER = registerGene("hunger") {
        GeneProperties(
            id = OtherUtil.modResource("hunger"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.HUNGER,
                level = 1
            )
        )
    }

    val LEVITATION = registerGene("levitation") {
        GeneProperties(
            id = OtherUtil.modResource("levitation"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.LEVITATION,
                level = 1
            )
        )
    }

    val MINING_FATIGUE = registerGene("mining_fatigue") {
        GeneProperties(
            id = OtherUtil.modResource("mining_fatigue"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.DIG_SLOWDOWN,
                level = 1
            )
        )
    }

    val NAUSEA = registerGene("nausea") {
        GeneProperties(
            id = OtherUtil.modResource("nausea"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.CONFUSION,
                level = 1
            )
        )
    }

    val POISON = registerGene("poison") {
        GeneProperties(
            id = OtherUtil.modResource("poison"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.POISON,
                level = 1
            )
        )
    }

    val POISON_FOUR = registerGene("poison_4") {
        GeneProperties(
            id = OtherUtil.modResource("poison_4"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.POISON,
                level = 4
            )
        )
    }

    val SLOWNESS = registerGene("slowness") {
        GeneProperties(
            id = OtherUtil.modResource("slowness"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.MOVEMENT_SLOWDOWN,
                level = 1
            )
        )
    }

    val SLOWNESS_FOUR = registerGene("slowness_4") {
        GeneProperties(
            id = OtherUtil.modResource("slowness_4"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.MOVEMENT_SLOWDOWN,
                level = 4
            )
        )
    }

    val SLOWNESS_SIX = registerGene("slowness_6") {
        GeneProperties(
            id = OtherUtil.modResource("slowness_6"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.MOVEMENT_SLOWDOWN,
                level = 6
            )
        )
    }

    val WEAKNESS = registerGene("weakness") {
        GeneProperties(
            id = OtherUtil.modResource("weakness"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.WEAKNESS,
                level = 1
            )
        )
    }

    val WITHER = registerGene("wither") {
        GeneProperties(
            id = OtherUtil.modResource("wither"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true,
            potionDetails = GeneProperties.PotionDetails(
                effect = MobEffects.WITHER,
                level = 1
            )
        )
    }

    val BLACK_DEATH = registerGene("black_death") {
        GeneProperties(
            id = OtherUtil.modResource("black_death"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true
        )
    }

    val GREEN_DEATH = registerGene("green_death") {
        GeneProperties(
            id = OtherUtil.modResource("green_death"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true
        )
    }

    val WHITE_DEATH = registerGene("white_death") {
        GeneProperties(
            id = OtherUtil.modResource("white_death"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true
        )
    }

    val GRAY_DEATH = registerGene("gray_death") {
        GeneProperties(
            id = OtherUtil.modResource("gray_death"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true
        )
    }

    val UN_UNDEATH = registerGene("un_undeath") {
        GeneProperties(
            id = OtherUtil.modResource("un_undeath"),
            dnaPointsRequired = 1,
            isNegative = true,
            canMobsHave = true
        )
    }


}