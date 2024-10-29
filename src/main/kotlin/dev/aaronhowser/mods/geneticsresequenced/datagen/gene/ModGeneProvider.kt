package dev.aaronhowser.mods.geneticsresequenced.datagen.gene

import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.AttributeEntry
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.PotionDetails
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.neoforge.common.NeoForgeMod
import java.util.*

class ModGeneProvider : RegistrySetBuilder() {

    companion object {

        private fun makeGene(
            dnaPointsRequired: Int = 1,
            allowedEntities: HolderSet<EntityType<*>> = Gene.defaultAllowedEntities,
            potionDetails: Optional<PotionDetails> = Optional.empty(),
            attributeModifiers: List<AttributeEntry> = emptyList(),
            scaresEntitiesWithTag: Optional<TagKey<EntityType<*>>> = Optional.empty()
        ) = Gene(dnaPointsRequired, allowedEntities, potionDetails, attributeModifiers, scaresEntitiesWithTag)

        val noEntities: HolderSet<EntityType<*>> = HolderSet.empty()
        val onlyPlayers: HolderSet.Direct<EntityType<*>> = HolderSet.direct(EntityType.PLAYER.builtInRegistryHolder())

        fun bootstrap(context: BootstrapContext<Gene>) {

            context.register(
                ModGenes.BASIC,
                makeGene(
                    allowedEntities = noEntities
                )
            )

            // Mutations

            context.register(
                ModGenes.CLAWS_TWO,
                makeGene(
                    dnaPointsRequired = 50
                )
            )

            context.register(
                ModGenes.EFFICIENCY_FOUR,
                makeGene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        AttributeEntry(
                            ModAttributes.EFFICIENCY,
                            ModAttributes.efficiencyFourAttributeModifier
                        )
                    ),
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.FLIGHT,
                makeGene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        AttributeEntry(
                            NeoForgeMod.CREATIVE_FLIGHT,
                            ModAttributes.flightAttributeModifier
                        )
                    ),
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.HASTE_TWO,
                makeGene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.DIG_SPEED,
                            level = 2
                        )
                    )
                )
            )

            context.register(
                ModGenes.MEATY_TWO,
                makeGene(
                    dnaPointsRequired = 50
                )
            )

            context.register(
                ModGenes.MORE_HEARTS_TWO,
                makeGene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        AttributeEntry(
                            Attributes.MAX_HEALTH,
                            ModAttributes.moreHealthTwoAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.PHOTOSYNTHESIS,
                makeGene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.REGENERATION_FOUR,
                makeGene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.REGENERATION,
                            level = 4
                        )
                    )
                )
            )

            context.register(
                ModGenes.RESISTANCE_TWO,
                makeGene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.DAMAGE_RESISTANCE,
                            level = 2
                        )
                    )
                )
            )

            context.register(
                ModGenes.SPEED_FOUR,
                makeGene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.MOVEMENT_SPEED,
                            level = 4
                        )
                    )
                )
            )

            context.register(
                ModGenes.SPEED_TWO,
                makeGene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.MOVEMENT_SPEED,
                            level = 2
                        )
                    )
                )
            )

            context.register(
                ModGenes.STRENGTH_TWO,
                makeGene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.DAMAGE_BOOST,
                            level = 2
                        )
                    )
                )
            )

            //Standard list

            context.register(
                ModGenes.BIOLUMINESCENCE,
                makeGene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                ModGenes.CHATTERBOX,
                makeGene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                ModGenes.CHILLING,
                makeGene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                ModGenes.CLAWS,
                makeGene(
                    dnaPointsRequired = 20,
                )
            )

            context.register(
                ModGenes.DRAGON_BREATH,
                makeGene(
                    dnaPointsRequired = 20,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.EAT_GRASS,
                makeGene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                ModGenes.EFFICIENCY,
                makeGene(
                    dnaPointsRequired = 30,
                    attributeModifiers = listOf(
                        AttributeEntry(
                            ModAttributes.EFFICIENCY,
                            ModAttributes.efficiencyAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.EMERALD_HEART,
                makeGene(
                    dnaPointsRequired = 30
                )
            )

            context.register(
                ModGenes.ENDER_DRAGON_HEALTH,
                makeGene(
                    dnaPointsRequired = 60
                )
            )

            context.register(
                ModGenes.EXPLOSIVE_EXIT,
                makeGene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                ModGenes.FIRE_PROOF,
                makeGene(
                    dnaPointsRequired = 24
                )
            )

            context.register(
                ModGenes.HASTE,
                makeGene(
                    dnaPointsRequired = 30,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.DIG_SPEED
                        )
                    )
                )
            )

            context.register(
                ModGenes.INFINITY,
                makeGene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.INVISIBLE,
                makeGene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.INVISIBILITY
                        )
                    )
                )
            )

            context.register(
                ModGenes.ITEM_MAGNET,
                makeGene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.JUMP_BOOST,
                makeGene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.JUMP
                        )
                    ),
                )
            )

            context.register(
                ModGenes.JOHNNY,
                makeGene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                ModGenes.KEEP_INVENTORY,
                makeGene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.KNOCKBACK,
                makeGene(
                    dnaPointsRequired = 20,
                    attributeModifiers = listOf(
                        AttributeEntry(
                            Attributes.ATTACK_KNOCKBACK,
                            ModAttributes.knockbackAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.LAY_EGG,
                makeGene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                ModGenes.LUCK,
                makeGene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.LUCK
                        )
                    )
                )
            )

            context.register(
                ModGenes.MEATY,
                makeGene(
                    dnaPointsRequired = 12,
                )
            )

            context.register(
                ModGenes.MILKY,
                makeGene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                ModGenes.MOB_SIGHT,
                makeGene(
                    dnaPointsRequired = 16,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.MORE_HEARTS,
                makeGene(
                    dnaPointsRequired = 40,
                    attributeModifiers = listOf(
                        AttributeEntry(
                            Attributes.MAX_HEALTH,
                            ModAttributes.moreHealthOneAttributeModifier
                        )
                    ),
                )
            )

            context.register(
                ModGenes.NIGHT_VISION,
                makeGene(
                    dnaPointsRequired = 16,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.NIGHT_VISION
                        )
                    )
                )
            )

            context.register(
                ModGenes.NO_FALL_DAMAGE,
                makeGene(
                    dnaPointsRequired = 30
                )
            )

            context.register(
                ModGenes.NO_HUNGER,
                makeGene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.POISON_IMMUNITY,
                makeGene(
                    dnaPointsRequired = 24
                )
            )

            context.register(
                ModGenes.REACHING,
                makeGene(
                    dnaPointsRequired = 50,
                    allowedEntities = onlyPlayers,
                    attributeModifiers = listOf(
                        AttributeEntry(
                            Attributes.ENTITY_INTERACTION_RANGE,
                            ModAttributes.reachingAttributeModifier
                        ),
                        AttributeEntry(
                            Attributes.BLOCK_INTERACTION_RANGE,
                            ModAttributes.reachingAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.REGENERATION,
                makeGene(
                    dnaPointsRequired = 60,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.REGENERATION
                        )
                    ),
                )
            )

            context.register(
                ModGenes.RESISTANCE,
                makeGene(
                    dnaPointsRequired = 30,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.DAMAGE_RESISTANCE
                        )
                    ),
                )
            )

            context.register(
                ModGenes.SCARE_CREEPERS,
                makeGene(
                    dnaPointsRequired = 20,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_CREEPER_GENE)
                )
            )

            context.register(
                ModGenes.SCARE_SKELETONS,
                makeGene(
                    dnaPointsRequired = 20,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_SKELETON_GENE)
                )
            )

            context.register(
                ModGenes.SCARE_SPIDERS,
                makeGene(
                    dnaPointsRequired = 50,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_SPIDER_GENE)
                )
            )

            context.register(
                ModGenes.SCARE_ZOMBIES,
                makeGene(
                    dnaPointsRequired = 50,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_ZOMBIE_GENE)
                )
            )

            context.register(
                ModGenes.SHOOT_FIREBALLS,
                makeGene(
                    dnaPointsRequired = 24,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.SLIMY_DEATH,
                makeGene(
                    dnaPointsRequired = 60,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.SPEED,
                makeGene(
                    dnaPointsRequired = 20,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.MOVEMENT_SPEED
                        )
                    ),
                )
            )

            context.register(
                ModGenes.STEP_ASSIST,
                makeGene(
                    dnaPointsRequired = 10,
                    attributeModifiers = listOf(
                        AttributeEntry(
                            Attributes.STEP_HEIGHT,
                            ModAttributes.stepAssistAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.STRENGTH,
                makeGene(
                    dnaPointsRequired = 20,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.DAMAGE_BOOST
                        )
                    ),
                )
            )

            context.register(
                ModGenes.TELEPORT,
                makeGene(
                    dnaPointsRequired = 24,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.THORNS,
                makeGene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                ModGenes.WALL_CLIMBING,
                makeGene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.WATER_BREATHING,
                makeGene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                ModGenes.WITHER_HIT,
                makeGene(
                    dnaPointsRequired = 20,
                )
            )

            context.register(
                ModGenes.WITHER_PROOF,
                makeGene(
                    dnaPointsRequired = 40
                )
            )

            context.register(
                ModGenes.WOOLY,
                makeGene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                ModGenes.XP_MAGNET,
                makeGene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            //Negative effects

            //FIXME: This effect apparently has a sound on add now, probably want to remove that

            context.register(
                ModGenes.BAD_OMEN,
                makeGene(
                    dnaPointsRequired = 20,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.BAD_OMEN
                        )
                    )
                )
            )

            context.register(
                ModGenes.BLINDNESS,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.BLINDNESS
                        )
                    )
                )
            )

            context.register(
                ModGenes.CRINGE,
                makeGene(
                    dnaPointsRequired = 20,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.CURSED,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.UNLUCK
                        )
                    )
                )
            )

            context.register(
                ModGenes.FLAMBE,
                makeGene(
                    dnaPointsRequired = 1
                )
            )

            context.register(
                ModGenes.HUNGER,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.HUNGER
                        )
                    )
                )
            )

            context.register(
                ModGenes.INFESTED,
                makeGene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.INFESTED
                        )
                    )
                )
            )

            context.register(
                ModGenes.LEVITATION,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.LEVITATION
                        )
                    )
                )
            )

            context.register(
                ModGenes.MINING_FATIGUE,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.DIG_SLOWDOWN
                        )
                    )
                )
            )

            context.register(
                ModGenes.NAUSEA,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.CONFUSION
                        )
                    )
                )
            )

            context.register(
                ModGenes.OOZING,
                makeGene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.OOZING
                        )
                    )
                )
            )

            context.register(
                ModGenes.POISON,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.POISON
                        )
                    )
                )
            )

            context.register(
                ModGenes.POISON_FOUR,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.POISON,
                            level = 4
                        )
                    )
                )
            )

            context.register(
                ModGenes.SLOWNESS,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.MOVEMENT_SLOWDOWN
                        )
                    )
                )
            )

            context.register(
                ModGenes.SLOWNESS_FOUR,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.MOVEMENT_SLOWDOWN,
                            level = 4
                        )
                    )
                )
            )

            context.register(
                ModGenes.SLOWNESS_SIX,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.MOVEMENT_SLOWDOWN,
                            level = 6
                        )
                    )
                )
            )

            context.register(
                ModGenes.WEAVING,
                makeGene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.WEAVING
                        )
                    )
                )
            )

            context.register(
                ModGenes.WEAKNESS,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.WEAKNESS
                        )
                    )
                )
            )

            context.register(
                ModGenes.WIND_CHARGED,
                makeGene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.WIND_CHARGED
                        )
                    )
                )
            )

            context.register(
                ModGenes.WITHER,
                makeGene(
                    potionDetails = Optional.of(
                        PotionDetails(
                            effect = MobEffects.WITHER
                        )
                    )
                )
            )

            // Plagues

            context.register(
                ModGenes.BLACK_DEATH,
                makeGene()
            )

            context.register(
                ModGenes.GREEN_DEATH,
                makeGene()
            )

            context.register(
                ModGenes.WHITE_DEATH,
                makeGene()
            )

            context.register(
                ModGenes.GRAY_DEATH,
                makeGene()
            )

            context.register(
                ModGenes.UN_UNDEATH,
                makeGene()
            )

        }
    }

}