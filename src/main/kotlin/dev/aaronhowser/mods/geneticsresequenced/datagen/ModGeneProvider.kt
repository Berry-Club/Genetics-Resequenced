package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.neoforge.common.NeoForgeMod

class ModGeneProvider : RegistrySetBuilder() {

    override fun build(registryAccess: RegistryAccess): HolderLookup.Provider {

        ModGenes.BASIC = registerGene("basic") {
            GeneProperties(
                id = OtherUtil.modResource("basic"),
                isHidden = true
            )
        }

        // Mutations (must be initialized first because they're used in arguments in ones below)

        ModGenes.CLAWS_TWO = registerGene("claws_2") {
            GeneProperties(
                id = OtherUtil.modResource("claws_2"),
                dnaPointsRequired = 50,
                canMobsHave = true
            )
        }

        ModGenes.EFFICIENCY_FOUR = registerGene("efficiency_4") {
            GeneProperties(
                id = OtherUtil.modResource("efficiency_4"),
                dnaPointsRequired = 50,
                attributeModifiers = mapOf(ModAttributes.EFFICIENCY to listOf(ModAttributes.efficiencyFourAttributeModifier))
            )
        }

        ModGenes.FLIGHT = registerGene("flight") {
            GeneProperties(
                id = OtherUtil.modResource("flight"),
                dnaPointsRequired = 50,
                attributeModifiers = mapOf(NeoForgeMod.CREATIVE_FLIGHT to listOf(ModAttributes.flightAttributeModifier))
            )
        }

        ModGenes.HASTE_TWO = registerGene("haste_2") {
            GeneProperties(
                id = OtherUtil.modResource("haste_2"),
                dnaPointsRequired = 50,
                potionDetails = GeneProperties.PotionDetails(
                    effect = MobEffects.DIG_SPEED,
                    level = 2
                )
            )
        }

        ModGenes.MEATY_TWO = registerGene("meaty_2") {
            GeneProperties(
                id = OtherUtil.modResource("meaty_2"),
                dnaPointsRequired = 50,
                canMobsHave = true
            )
        }

        ModGenes.MORE_HEARTS_TWO = registerGene("more_hearts_2") {
            GeneProperties(
                id = OtherUtil.modResource("more_hearts_2"),
                dnaPointsRequired = 50,
                canMobsHave = true,
                attributeModifiers = mapOf(
                    Attributes.MAX_HEALTH to listOf(ModAttributes.moreHealthTwoAttributeModifier)
                )
            )
        }

        ModGenes.PHOTOSYNTHESIS = registerGene("photosynthesis") {
            GeneProperties(
                id = OtherUtil.modResource("photosynthesis"),
                dnaPointsRequired = 40
            )
        }

        ModGenes.REGENERATION_FOUR = registerGene("regeneration_4") {
            GeneProperties(
                id = OtherUtil.modResource("regeneration_4"),
                dnaPointsRequired = 50,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(
                    effect = MobEffects.REGENERATION,
                    level = 4
                )
            )
        }

        ModGenes.RESISTANCE_TWO = registerGene("resistance_2") {
            GeneProperties(
                id = OtherUtil.modResource("resistance_2"),
                dnaPointsRequired = 50,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(
                    effect = MobEffects.DAMAGE_RESISTANCE,
                    level = 2
                )
            )
        }

        ModGenes.SPEED_FOUR = registerGene("speed_4") {
            GeneProperties(
                id = OtherUtil.modResource("speed_4"),
                dnaPointsRequired = 50,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(
                    effect = MobEffects.MOVEMENT_SPEED,
                    level = 4
                )
            )
        }

        ModGenes.SPEED_TWO = registerGene("speed_2") {
            GeneProperties(
                id = OtherUtil.modResource("speed_2"),
                dnaPointsRequired = 50,
                canMobsHave = true,
                mutatesInto = SPEED_FOUR.get(),
                potionDetails = GeneProperties.PotionDetails(
                    effect = MobEffects.MOVEMENT_SPEED,
                    level = 2
                )
            )
        }

        ModGenes.STRENGTH_TWO = registerGene("strength_2") {
            GeneProperties(
                id = OtherUtil.modResource("strength_2"),
                dnaPointsRequired = 50,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(
                    effect = MobEffects.DAMAGE_BOOST,
                    level = 2
                )
            )
        }

        ModGenes.SCARE_ZOMBIES = registerGene("scare_zombies") {
            GeneProperties(
                id = OtherUtil.modResource("scare_zombies"),
                dnaPointsRequired = 50,
                canMobsHave = true
            )
        }

        ModGenes.SCARE_SPIDERS = registerGene("scare_spiders") {
            GeneProperties(
                id = OtherUtil.modResource("scare_spiders"),
                dnaPointsRequired = 50,
                canMobsHave = true
            )
        }

        //Standard list

        ModGenes.BIOLUMINESCENCE = registerGene("bioluminescence") {
            GeneProperties(
                id = OtherUtil.modResource("bioluminescence"),
                dnaPointsRequired = 16,
                canMobsHave = true
            )
        }

        ModGenes.CHATTERBOX = registerGene("chatterbox") {
            GeneProperties(
                id = OtherUtil.modResource("chatterbox"),
                dnaPointsRequired = 20
            )
        }

        ModGenes.CHILLING = registerGene("chilling") {
            GeneProperties(
                id = OtherUtil.modResource("chilling"),
                dnaPointsRequired = 20,
                canMobsHave = true
            )
        }

        ModGenes.CLAWS = registerGene("claws") {
            GeneProperties(
                id = OtherUtil.modResource("claws"),
                dnaPointsRequired = 20,
                canMobsHave = true,
                mutatesInto = CLAWS_TWO.get()
            )
        }

        ModGenes.DRAGON_BREATH = registerGene("dragons_breath") {
            GeneProperties(
                id = OtherUtil.modResource("dragons_breath"),
                dnaPointsRequired = 20
            )
        }

        ModGenes.EAT_GRASS = registerGene("eat_grass") {
            GeneProperties(
                id = OtherUtil.modResource("eat_grass"),
                dnaPointsRequired = 16,
            )
        }

        ModGenes.EFFICIENCY = registerGene("efficiency") {
            GeneProperties(
                id = OtherUtil.modResource("efficiency"),
                dnaPointsRequired = 30,
                mutatesInto = EFFICIENCY_FOUR.get(),
                attributeModifiers = mapOf(
                    ModAttributes.EFFICIENCY to listOf(ModAttributes.efficiencyAttributeModifier)
                )
            )
        }

        ModGenes.EMERALD_HEART = registerGene("emerald_heart") {
            GeneProperties(
                id = OtherUtil.modResource("emerald_heart"),
                dnaPointsRequired = 30,
                canMobsHave = true
            )
        }

        ModGenes.ENDER_DRAGON_HEALTH = registerGene("ender_dragon_health") {
            GeneProperties(
                id = OtherUtil.modResource("ender_dragon_health"),
                dnaPointsRequired = 60
            )
        }

        ModGenes.EXPLOSIVE_EXIT = registerGene("explosive_exit") {
            GeneProperties(
                id = OtherUtil.modResource("explosive_exit"),
                dnaPointsRequired = 20,
                canMobsHave = true
            )
        }

        ModGenes.FIRE_PROOF = registerGene("fire_proof") {
            GeneProperties(
                id = OtherUtil.modResource("fire_proof"),
                dnaPointsRequired = 24,
                canMobsHave = true
            )
        }

        ModGenes.HASTE = registerGene("haste") {
            GeneProperties(
                id = OtherUtil.modResource("haste"),
                dnaPointsRequired = 30,
                potionDetails = GeneProperties.PotionDetails(MobEffects.DIG_SPEED),
                mutatesInto = HASTE_TWO.get()
            )
        }

        ModGenes.INFINITY = registerGene("infinity") {
            GeneProperties(
                id = OtherUtil.modResource("infinity"),
                dnaPointsRequired = 30
            )
        }

        ModGenes.INVISIBLE = registerGene("invisible") {
            GeneProperties(
                id = OtherUtil.modResource("invisible"),
                dnaPointsRequired = 50,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.INVISIBILITY)
            )
        }

        ModGenes.ITEM_MAGNET = registerGene("item_magnet") {
            GeneProperties(
                id = OtherUtil.modResource("item_magnet"),
                dnaPointsRequired = 30
            )
        }

        ModGenes.JUMP_BOOST = registerGene("jump_boost") {
            GeneProperties(
                id = OtherUtil.modResource("jump_boost"),
                dnaPointsRequired = 10,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.JUMP),
                mutatesInto = FLIGHT.get()
            )
        }

        ModGenes.JOHNNY = registerGene("johnny") {
            GeneProperties(
                id = OtherUtil.modResource("johnny"),
                dnaPointsRequired = 20,
                canMobsHave = true
            )
        }

        ModGenes.KEEP_INVENTORY = registerGene("keep_inventory") {
            GeneProperties(
                id = OtherUtil.modResource("keep_inventory"),
                dnaPointsRequired = 40
            )
        }

        ModGenes.KNOCKBACK = registerGene("knockback") {
            GeneProperties(
                id = OtherUtil.modResource("knockback"),
                dnaPointsRequired = 20,
                canMobsHave = true,
                attributeModifiers = mapOf(
                    Attributes.ATTACK_KNOCKBACK to listOf(ModAttributes.knockbackAttributeModifier)
                )
            )
        }

        ModGenes.LAY_EGG = registerGene("lay_egg") {
            GeneProperties(
                id = OtherUtil.modResource("lay_egg"),
                dnaPointsRequired = 12,
                canMobsHave = true
            )
        }

        ModGenes.LUCK = registerGene("luck") {
            GeneProperties(
                id = OtherUtil.modResource("luck"),
                dnaPointsRequired = 50,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.LUCK)
            )
        }

        ModGenes.MEATY = registerGene("meaty") {
            GeneProperties(
                id = OtherUtil.modResource("meaty"),
                dnaPointsRequired = 12,
                canMobsHave = true,
                mutatesInto = MEATY_TWO.get()
            )
        }

        ModGenes.MILKY = registerGene("milky") {
            GeneProperties(
                id = OtherUtil.modResource("milky"),
                dnaPointsRequired = 12,
                canMobsHave = true
            )
        }

        ModGenes.MOB_SIGHT = registerGene("mob_sight") {
            GeneProperties(
                id = OtherUtil.modResource("mob_sight"),
                dnaPointsRequired = 16
            )
        }

        ModGenes.MORE_HEARTS = registerGene("more_hearts") {
            GeneProperties(
                id = OtherUtil.modResource("more_hearts"),
                dnaPointsRequired = 40,
                canMobsHave = true,
                mutatesInto = MORE_HEARTS_TWO.get(),
                attributeModifiers = mapOf(Attributes.MAX_HEALTH to listOf(ModAttributes.moreHealthOneAttributeModifier))
            )
        }

        ModGenes.NIGHT_VISION = registerGene("night_vision") {
            GeneProperties(
                id = OtherUtil.modResource("night_vision"),
                dnaPointsRequired = 16,
                potionDetails = GeneProperties.PotionDetails(MobEffects.NIGHT_VISION)
            )
        }

        ModGenes.NO_FALL_DAMAGE = registerGene("no_fall_damage") {
            GeneProperties(
                id = OtherUtil.modResource("no_fall_damage"),
                dnaPointsRequired = 30,
                canMobsHave = true
            )
        }

        ModGenes.NO_HUNGER = registerGene("no_hunger") {
            GeneProperties(
                id = OtherUtil.modResource("no_hunger"),
                dnaPointsRequired = 30
            )
        }

        ModGenes.POISON_IMMUNITY = registerGene("poison_immunity") {
            GeneProperties(
                id = OtherUtil.modResource("poison_immunity"),
                dnaPointsRequired = 24,
                canMobsHave = true
            )
        }

        ModGenes.REGENERATION = registerGene("regeneration") {
            GeneProperties(
                id = OtherUtil.modResource("regeneration"),
                dnaPointsRequired = 60,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.REGENERATION),
                mutatesInto = REGENERATION_FOUR.get()
            )
        }

        ModGenes.RESISTANCE = registerGene("resistance") {
            GeneProperties(
                id = OtherUtil.modResource("resistance"),
                dnaPointsRequired = 30,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.DAMAGE_RESISTANCE),
                mutatesInto = RESISTANCE_TWO.get()
            )
        }

        ModGenes.SCARE_CREEPERS = registerGene("scare_creepers") {
            GeneProperties(
                id = OtherUtil.modResource("scare_creepers"),
                dnaPointsRequired = 20,
                mutatesInto = SCARE_ZOMBIES.get(),
                canMobsHave = true
            )
        }

        ModGenes.SCARE_SKELETONS = registerGene("scare_skeletons") {
            GeneProperties(
                id = OtherUtil.modResource("scare_skeletons"),
                dnaPointsRequired = 20,
                mutatesInto = SCARE_SPIDERS.get(),
                canMobsHave = true
            )
        }

        ModGenes.SHOOT_FIREBALLS = registerGene("shoot_fireballs") {
            GeneProperties(
                id = OtherUtil.modResource("shoot_fireballs"),
                dnaPointsRequired = 24
            )
        }

        ModGenes.SLIMY_DEATH = registerGene("slimy_death") {
            GeneProperties(
                id = OtherUtil.modResource("slimy_death"),
                dnaPointsRequired = 60
            )
        }

        ModGenes.SPEED = registerGene("speed") {
            GeneProperties(
                id = OtherUtil.modResource("speed"),
                dnaPointsRequired = 20,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.MOVEMENT_SPEED),
                mutatesInto = SPEED_TWO.get()
            )
        }

        ModGenes.STEP_ASSIST = registerGene("step_assist") {
            GeneProperties(
                id = OtherUtil.modResource("step_assist"),
                dnaPointsRequired = 10,
                attributeModifiers = mapOf(
                    Attributes.STEP_HEIGHT to listOf(ModAttributes.stepAssistAttributeModifier)
                )
            )
        }

        ModGenes.STRENGTH = registerGene("strength") {
            GeneProperties(
                id = OtherUtil.modResource("strength"),
                dnaPointsRequired = 20,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.DAMAGE_BOOST),
                mutatesInto = STRENGTH_TWO.get()
            )
        }

        ModGenes.TELEPORT = registerGene("teleport") {
            GeneProperties(
                id = OtherUtil.modResource("teleport"),
                dnaPointsRequired = 24,
                mutatesInto = FLIGHT.get()
            )
        }

        ModGenes.THORNS = registerGene("thorns") {
            GeneProperties(
                id = OtherUtil.modResource("thorns"),
                dnaPointsRequired = 12,
                canMobsHave = true
            )
        }

        ModGenes.WALL_CLIMBING = registerGene("wall_climbing") {
            GeneProperties(
                id = OtherUtil.modResource("wall_climbing"),
                dnaPointsRequired = 40
            )
        }

        ModGenes.WATER_BREATHING = registerGene("water_breathing") {
            GeneProperties(
                id = OtherUtil.modResource("water_breathing"),
                dnaPointsRequired = 16,
                canMobsHave = true
            )
        }

        ModGenes.WITHER_HIT = registerGene("wither_hit") {
            GeneProperties(
                id = OtherUtil.modResource("wither_hit"),
                dnaPointsRequired = 20,
                canMobsHave = true
            )
        }

        ModGenes.WITHER_PROOF = registerGene("wither_proof") {
            GeneProperties(
                id = OtherUtil.modResource("wither_proof"),
                dnaPointsRequired = 40,
                canMobsHave = true
            )
        }

        ModGenes.WOOLY = registerGene("wooly") {
            GeneProperties(
                id = OtherUtil.modResource("wooly"),
                dnaPointsRequired = 12,
                canMobsHave = true
            )
        }

        ModGenes.XP_MAGNET = registerGene("xp_magnet") {
            GeneProperties(
                id = OtherUtil.modResource("xp_magnet"),
                dnaPointsRequired = 30
            )
        }

        //Negative effects

        //FIXME: This effect apparently has a sound on add now, probably want to remove that
        ModGenes.BAD_OMEN = registerGene("bad_omen") {
            GeneProperties(
                id = OtherUtil.modResource("bad_omen"),
                dnaPointsRequired = 20,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.BAD_OMEN)
            )
        }

        ModGenes.BLINDNESS = registerGene("blindness") {
            GeneProperties(
                id = OtherUtil.modResource("blindness"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.BLINDNESS)
            )
        }

        ModGenes.CRINGE = registerGene("cringe") {
            GeneProperties(
                id = OtherUtil.modResource("cringe"),
                dnaPointsRequired = 20,
                isNegative = true
            )
        }

        ModGenes.CURSED = registerGene("cursed") {
            GeneProperties(
                id = OtherUtil.modResource("cursed"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.UNLUCK)
            )
        }

        ModGenes.FLAMBE = registerGene("flambe") {
            GeneProperties(
                id = OtherUtil.modResource("flambe"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true
            )
        }

        ModGenes.HUNGER = registerGene("hunger") {
            GeneProperties(
                id = OtherUtil.modResource("hunger"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.HUNGER)
            )
        }

        ModGenes.INFESTED = registerGene("infested") {
            GeneProperties(
                id = OtherUtil.modResource("infested"),
                dnaPointsRequired = 10,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.INFESTED)
            )
        }

        ModGenes.LEVITATION = registerGene("levitation") {
            GeneProperties(
                id = OtherUtil.modResource("levitation"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.LEVITATION)
            )
        }

        ModGenes.MINING_FATIGUE = registerGene("mining_fatigue") {
            GeneProperties(
                id = OtherUtil.modResource("mining_fatigue"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.DIG_SLOWDOWN)
            )
        }

        ModGenes.NAUSEA = registerGene("nausea") {
            GeneProperties(
                id = OtherUtil.modResource("nausea"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.CONFUSION)
            )
        }

        ModGenes.OOZING = registerGene("oozing") {
            GeneProperties(
                id = OtherUtil.modResource("oozing"),
                dnaPointsRequired = 10,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.OOZING)
            )
        }

        ModGenes.POISON = registerGene("poison") {
            GeneProperties(
                id = OtherUtil.modResource("poison"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.POISON)
            )
        }

        ModGenes.POISON_FOUR = registerGene("poison_4") {
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

        ModGenes.SLOWNESS = registerGene("slowness") {
            GeneProperties(
                id = OtherUtil.modResource("slowness"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.MOVEMENT_SLOWDOWN)
            )
        }

        ModGenes.SLOWNESS_FOUR = registerGene("slowness_4") {
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

        ModGenes.SLOWNESS_SIX = registerGene("slowness_6") {
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

        ModGenes.WEAVING = registerGene("weaving") {
            GeneProperties(
                id = OtherUtil.modResource("weaving"),
                dnaPointsRequired = 10,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.WEAVING)
            )
        }

        ModGenes.WEAKNESS = registerGene("weakness") {
            GeneProperties(
                id = OtherUtil.modResource("weakness"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.WEAKNESS)
            )
        }

        ModGenes.WIND_CHARGED = registerGene("wind_charged") {
            GeneProperties(
                id = OtherUtil.modResource("wind_charged"),
                dnaPointsRequired = 10,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.WIND_CHARGED)
            )
        }

        ModGenes.WITHER = registerGene("wither") {
            GeneProperties(
                id = OtherUtil.modResource("wither"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true,
                potionDetails = GeneProperties.PotionDetails(MobEffects.WITHER)
            )
        }

        // Plagues

        ModGenes.BLACK_DEATH = registerGene("black_death") {
            GeneProperties(
                id = OtherUtil.modResource("black_death"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true
            )
        }

        ModGenes.GREEN_DEATH = registerGene("green_death") {
            GeneProperties(
                id = OtherUtil.modResource("green_death"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true
            )
        }

        ModGenes.WHITE_DEATH = registerGene("white_death") {
            GeneProperties(
                id = OtherUtil.modResource("white_death"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true
            )
        }

        ModGenes.GRAY_DEATH = registerGene("gray_death") {
            GeneProperties(
                id = OtherUtil.modResource("gray_death"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true
            )
        }

        ModGenes.UN_UNDEATH = registerGene("un_undeath") {
            GeneProperties(
                id = OtherUtil.modResource("un_undeath"),
                dnaPointsRequired = 1,
                isNegative = true,
                canMobsHave = true
            )
        }
    }

}