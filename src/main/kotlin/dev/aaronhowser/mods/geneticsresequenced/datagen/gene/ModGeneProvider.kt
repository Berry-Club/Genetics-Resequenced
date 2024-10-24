package dev.aaronhowser.mods.geneticsresequenced.datagen.gene

import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.neoforge.common.NeoForgeMod
import java.util.*

class ModGeneProvider : RegistrySetBuilder() {

    companion object {
        fun bootstrap(context: BootstrapContext<Gene>) {

            val noEntities: HolderSet<EntityType<*>> = HolderSet.empty()
            val onlyPlayers = HolderSet.direct(EntityType.PLAYER.builtInRegistryHolder())

            context.register(
                ModGenes.BASIC,
                Gene(
                    allowedEntities = noEntities
                )
            )

            // Mutations

            context.register(
                ModGenes.CLAWS_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    requiresGeneRks = listOf(ModGenes.CLAWS)
                )
            )

            context.register(
                ModGenes.EFFICIENCY_FOUR,
                Gene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            ModAttributes.EFFICIENCY,
                            ModAttributes.efficiencyFourAttributeModifier
                        )
                    ),
                    allowedEntities = onlyPlayers,
                    requiresGeneRks = listOf(ModGenes.EFFICIENCY)
                )
            )

            context.register(
                ModGenes.FLIGHT,
                Gene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            NeoForgeMod.CREATIVE_FLIGHT,
                            ModAttributes.flightAttributeModifier
                        )
                    ),
                    allowedEntities = onlyPlayers,
                    requiresGeneRks = listOf(ModGenes.TELEPORT, ModGenes.STEP_ASSIST, ModGenes.NO_FALL_DAMAGE)
                )
            )

            context.register(
                ModGenes.HASTE_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DIG_SPEED,
                            level = 2
                        )
                    ),
                    requiresGeneRks = listOf(ModGenes.HASTE)
                )
            )

            context.register(
                ModGenes.MEATY_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    requiresGeneRks = listOf(ModGenes.MEATY)
                )
            )

            context.register(
                ModGenes.MORE_HEARTS_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            Attributes.MAX_HEALTH,
                            ModAttributes.moreHealthTwoAttributeModifier
                        )
                    ),
                    allowedEntities = onlyPlayers,
                    requiresGeneRks = listOf(ModGenes.MORE_HEARTS)
                )
            )

            context.register(
                ModGenes.PHOTOSYNTHESIS,
                Gene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers,
                    requiresGeneRks = listOf(ModGenes.EAT_GRASS, ModGenes.THORNS)
                )
            )

            context.register(
                ModGenes.REGENERATION_FOUR,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.REGENERATION,
                            level = 4
                        )
                    ),
                    requiresGeneRks = listOf(ModGenes.REGENERATION)
                )
            )

            context.register(
                ModGenes.RESISTANCE_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DAMAGE_RESISTANCE,
                            level = 2
                        )
                    ),
                    requiresGeneRks = listOf(ModGenes.RESISTANCE)
                )
            )

            context.register(
                ModGenes.SPEED_FOUR,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SPEED,
                            level = 4
                        )
                    ),
                    requiresGeneRks = listOf(ModGenes.SPEED_TWO)
                )
            )

            context.register(
                ModGenes.SPEED_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SPEED,
                            level = 2
                        )
                    ),
                    requiresGeneRks = listOf(ModGenes.SPEED)
                )
            )

            context.register(
                ModGenes.STRENGTH_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DAMAGE_BOOST,
                            level = 2
                        )
                    ),
                    requiresGeneRks = listOf(ModGenes.STRENGTH)
                )
            )

            //Standard list

            context.register(
                ModGenes.BIOLUMINESCENCE,
                Gene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                ModGenes.CHATTERBOX,
                Gene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                ModGenes.CHILLING,
                Gene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                ModGenes.CLAWS,
                Gene(
                    dnaPointsRequired = 20,
                )
            )

            context.register(
                ModGenes.DRAGON_BREATH,
                Gene(
                    dnaPointsRequired = 20,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.EAT_GRASS,
                Gene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                ModGenes.EFFICIENCY,
                Gene(
                    dnaPointsRequired = 30,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            ModAttributes.EFFICIENCY,
                            ModAttributes.efficiencyAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.EMERALD_HEART,
                Gene(
                    dnaPointsRequired = 30
                )
            )

            context.register(
                ModGenes.ENDER_DRAGON_HEALTH,
                Gene(
                    dnaPointsRequired = 60
                )
            )

            context.register(
                ModGenes.EXPLOSIVE_EXIT,
                Gene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                ModGenes.FIRE_PROOF,
                Gene(
                    dnaPointsRequired = 24
                )
            )

            context.register(
                ModGenes.HASTE,
                Gene(
                    dnaPointsRequired = 30,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DIG_SPEED
                        )
                    ),
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.INFINITY,
                Gene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.INVISIBLE,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.INVISIBILITY
                        )
                    )
                )
            )

            context.register(
                ModGenes.ITEM_MAGNET,
                Gene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.JUMP_BOOST,
                Gene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.JUMP
                        )
                    ),
                )
            )

            context.register(
                ModGenes.JOHNNY,
                Gene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                ModGenes.KEEP_INVENTORY,
                Gene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.KNOCKBACK,
                Gene(
                    dnaPointsRequired = 20,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            Attributes.ATTACK_KNOCKBACK,
                            ModAttributes.knockbackAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.LAY_EGG,
                Gene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                ModGenes.LUCK,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.LUCK
                        )
                    )
                )
            )

            context.register(
                ModGenes.MEATY,
                Gene(
                    dnaPointsRequired = 12,
                )
            )

            context.register(
                ModGenes.MILKY,
                Gene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                ModGenes.MOB_SIGHT,
                Gene(
                    dnaPointsRequired = 16,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.MORE_HEARTS,
                Gene(
                    dnaPointsRequired = 40,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            Attributes.MAX_HEALTH,
                            ModAttributes.moreHealthOneAttributeModifier
                        )
                    ),
                )
            )

            context.register(
                ModGenes.NIGHT_VISION,
                Gene(
                    dnaPointsRequired = 16,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.NIGHT_VISION
                        )
                    ),
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.NO_FALL_DAMAGE,
                Gene(
                    dnaPointsRequired = 30
                )
            )

            context.register(
                ModGenes.NO_HUNGER,
                Gene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.POISON_IMMUNITY,
                Gene(
                    dnaPointsRequired = 24
                )
            )

            context.register(
                ModGenes.REACHING,
                Gene(
                    dnaPointsRequired = 50,
                    allowedEntities = onlyPlayers,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            Attributes.ENTITY_INTERACTION_RANGE,
                            ModAttributes.reachingAttributeModifier
                        ),
                        Gene.AttributeEntry(
                            Attributes.BLOCK_INTERACTION_RANGE,
                            ModAttributes.reachingAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.REGENERATION,
                Gene(
                    dnaPointsRequired = 60,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.REGENERATION
                        )
                    ),
                )
            )

            context.register(
                ModGenes.RESISTANCE,
                Gene(
                    dnaPointsRequired = 30,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DAMAGE_RESISTANCE
                        )
                    ),
                )
            )

            context.register(
                ModGenes.SCARE_CREEPERS,
                Gene(
                    dnaPointsRequired = 20,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_CREEPER_GENE)
                )
            )

            context.register(
                ModGenes.SCARE_SKELETONS,
                Gene(
                    dnaPointsRequired = 20,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_SKELETON_GENE)
                )
            )

            context.register(
                ModGenes.SCARE_SPIDERS,
                Gene(
                    dnaPointsRequired = 50,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_SPIDER_GENE)
                )
            )

            context.register(
                ModGenes.SCARE_ZOMBIES,
                Gene(
                    dnaPointsRequired = 50,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_ZOMBIE_GENE)
                )
            )

            context.register(
                ModGenes.SHOOT_FIREBALLS,
                Gene(
                    dnaPointsRequired = 24,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.SLIMY_DEATH,
                Gene(
                    dnaPointsRequired = 60,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.SPEED,
                Gene(
                    dnaPointsRequired = 20,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SPEED
                        )
                    ),
                )
            )

            context.register(
                ModGenes.STEP_ASSIST,
                Gene(
                    dnaPointsRequired = 10,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            Attributes.STEP_HEIGHT,
                            ModAttributes.stepAssistAttributeModifier
                        )
                    )
                )
            )

            context.register(
                ModGenes.STRENGTH,
                Gene(
                    dnaPointsRequired = 20,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DAMAGE_BOOST
                        )
                    ),
                )
            )

            context.register(
                ModGenes.TELEPORT,
                Gene(
                    dnaPointsRequired = 24,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.THORNS,
                Gene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                ModGenes.WALL_CLIMBING,
                Gene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.WATER_BREATHING,
                Gene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                ModGenes.WITHER_HIT,
                Gene(
                    dnaPointsRequired = 20,
                )
            )

            context.register(
                ModGenes.WITHER_PROOF,
                Gene(
                    dnaPointsRequired = 40
                )
            )

            context.register(
                ModGenes.WOOLY,
                Gene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                ModGenes.XP_MAGNET,
                Gene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            //Negative effects

            //FIXME: This effect apparently has a sound on add now, probably want to remove that

            context.register(
                ModGenes.BAD_OMEN,
                Gene(
                    dnaPointsRequired = 20,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.BAD_OMEN
                        )
                    )
                )
            )

            context.register(
                ModGenes.BLINDNESS,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.BLINDNESS
                        )
                    )
                )
            )

            context.register(
                ModGenes.CRINGE,
                Gene(
                    dnaPointsRequired = 20,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                ModGenes.CURSED,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.UNLUCK
                        )
                    )
                )
            )

            context.register(
                ModGenes.FLAMBE,
                Gene(
                    dnaPointsRequired = 1
                )
            )

            context.register(
                ModGenes.HUNGER,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.HUNGER
                        )
                    )
                )
            )

            context.register(
                ModGenes.INFESTED,
                Gene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.INFESTED
                        )
                    )
                )
            )

            context.register(
                ModGenes.LEVITATION,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.LEVITATION
                        )
                    )
                )
            )

            context.register(
                ModGenes.MINING_FATIGUE,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DIG_SLOWDOWN
                        )
                    )
                )
            )

            context.register(
                ModGenes.NAUSEA,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.CONFUSION
                        )
                    )
                )
            )

            context.register(
                ModGenes.OOZING,
                Gene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.OOZING
                        )
                    )
                )
            )

            context.register(
                ModGenes.POISON,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.POISON
                        )
                    )
                )
            )

            context.register(
                ModGenes.POISON_FOUR,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.POISON,
                            level = 4
                        )
                    )
                )
            )

            context.register(
                ModGenes.SLOWNESS,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SLOWDOWN
                        )
                    )
                )
            )

            context.register(
                ModGenes.SLOWNESS_FOUR,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SLOWDOWN,
                            level = 4
                        )
                    )
                )
            )

            context.register(
                ModGenes.SLOWNESS_SIX,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SLOWDOWN,
                            level = 6
                        )
                    )
                )
            )

            context.register(
                ModGenes.WEAVING,
                Gene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.WEAVING
                        )
                    )
                )
            )

            context.register(
                ModGenes.WEAKNESS,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.WEAKNESS
                        )
                    )
                )
            )

            context.register(
                ModGenes.WIND_CHARGED,
                Gene(
                    dnaPointsRequired = 10,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.WIND_CHARGED
                        )
                    )
                )
            )

            context.register(
                ModGenes.WITHER,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.WITHER
                        )
                    )
                )
            )

            // Plagues

            context.register(
                ModGenes.BLACK_DEATH,
                Gene()
            )

            context.register(
                ModGenes.GREEN_DEATH,
                Gene()
            )

            context.register(
                ModGenes.WHITE_DEATH,
                Gene()
            )

            context.register(
                ModGenes.GRAY_DEATH,
                Gene()
            )

            context.register(
                ModGenes.UN_UNDEATH,
                Gene()
            )

        }
    }

}