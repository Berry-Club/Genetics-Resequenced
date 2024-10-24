package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.BaseModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
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
                BaseModGenes.BASIC,
                Gene(
                    allowedEntities = noEntities
                )
            )

            // Mutations

            context.register(
                BaseModGenes.CLAWS_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    requiresGeneRks = listOf(BaseModGenes.CLAWS)
                )
            )

            context.register(
                BaseModGenes.EFFICIENCY_FOUR,
                Gene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            ModAttributes.EFFICIENCY,
                            ModAttributes.efficiencyFourAttributeModifier
                        )
                    ),
                    allowedEntities = onlyPlayers,
                    requiresGeneRks = listOf(BaseModGenes.EFFICIENCY)
                )
            )

            context.register(
                BaseModGenes.FLIGHT,
                Gene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            NeoForgeMod.CREATIVE_FLIGHT,
                            ModAttributes.flightAttributeModifier
                        )
                    ),
                    allowedEntities = onlyPlayers,
                    requiresGeneRks = listOf(BaseModGenes.TELEPORT, BaseModGenes.STEP_ASSIST, BaseModGenes.NO_FALL_DAMAGE)
                )
            )

            context.register(
                BaseModGenes.HASTE_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DIG_SPEED,
                            level = 2
                        )
                    ),
                    requiresGeneRks = listOf(BaseModGenes.HASTE)
                )
            )

            context.register(
                BaseModGenes.MEATY_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    requiresGeneRks = listOf(BaseModGenes.MEATY)
                )
            )

            context.register(
                BaseModGenes.MORE_HEARTS_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    attributeModifiers = listOf(
                        Gene.AttributeEntry(
                            Attributes.MAX_HEALTH,
                            ModAttributes.moreHealthTwoAttributeModifier
                        )
                    ),
                    allowedEntities = onlyPlayers,
                    requiresGeneRks = listOf(BaseModGenes.MORE_HEARTS)
                )
            )

            context.register(
                BaseModGenes.PHOTOSYNTHESIS,
                Gene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers,
                    requiresGeneRks = listOf(BaseModGenes.EAT_GRASS, BaseModGenes.THORNS)
                )
            )

            context.register(
                BaseModGenes.REGENERATION_FOUR,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.REGENERATION,
                            level = 4
                        )
                    ),
                    requiresGeneRks = listOf(BaseModGenes.REGENERATION)
                )
            )

            context.register(
                BaseModGenes.RESISTANCE_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DAMAGE_RESISTANCE,
                            level = 2
                        )
                    ),
                    requiresGeneRks = listOf(BaseModGenes.RESISTANCE)
                )
            )

            context.register(
                BaseModGenes.SPEED_FOUR,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SPEED,
                            level = 4
                        )
                    ),
                    requiresGeneRks = listOf(BaseModGenes.SPEED_TWO)
                )
            )

            context.register(
                BaseModGenes.SPEED_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SPEED,
                            level = 2
                        )
                    ),
                    requiresGeneRks = listOf(BaseModGenes.SPEED)
                )
            )

            context.register(
                BaseModGenes.STRENGTH_TWO,
                Gene(
                    dnaPointsRequired = 50,
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DAMAGE_BOOST,
                            level = 2
                        )
                    ),
                    requiresGeneRks = listOf(BaseModGenes.STRENGTH)
                )
            )

            //Standard list

            context.register(
                BaseModGenes.BIOLUMINESCENCE,
                Gene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                BaseModGenes.CHATTERBOX,
                Gene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                BaseModGenes.CHILLING,
                Gene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                BaseModGenes.CLAWS,
                Gene(
                    dnaPointsRequired = 20,
                )
            )

            context.register(
                BaseModGenes.DRAGON_BREATH,
                Gene(
                    dnaPointsRequired = 20,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.EAT_GRASS,
                Gene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                BaseModGenes.EFFICIENCY,
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
                BaseModGenes.EMERALD_HEART,
                Gene(
                    dnaPointsRequired = 30
                )
            )

            context.register(
                BaseModGenes.ENDER_DRAGON_HEALTH,
                Gene(
                    dnaPointsRequired = 60
                )
            )

            context.register(
                BaseModGenes.EXPLOSIVE_EXIT,
                Gene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                BaseModGenes.FIRE_PROOF,
                Gene(
                    dnaPointsRequired = 24
                )
            )

            context.register(
                BaseModGenes.HASTE,
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
                BaseModGenes.INFINITY,
                Gene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.INVISIBLE,
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
                BaseModGenes.ITEM_MAGNET,
                Gene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.JUMP_BOOST,
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
                BaseModGenes.JOHNNY,
                Gene(
                    dnaPointsRequired = 20
                )
            )

            context.register(
                BaseModGenes.KEEP_INVENTORY,
                Gene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.KNOCKBACK,
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
                BaseModGenes.LAY_EGG,
                Gene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                BaseModGenes.LUCK,
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
                BaseModGenes.MEATY,
                Gene(
                    dnaPointsRequired = 12,
                )
            )

            context.register(
                BaseModGenes.MILKY,
                Gene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                BaseModGenes.MOB_SIGHT,
                Gene(
                    dnaPointsRequired = 16,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.MORE_HEARTS,
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
                BaseModGenes.NIGHT_VISION,
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
                BaseModGenes.NO_FALL_DAMAGE,
                Gene(
                    dnaPointsRequired = 30
                )
            )

            context.register(
                BaseModGenes.NO_HUNGER,
                Gene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.POISON_IMMUNITY,
                Gene(
                    dnaPointsRequired = 24
                )
            )

            context.register(
                BaseModGenes.REACHING,
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
                BaseModGenes.REGENERATION,
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
                BaseModGenes.RESISTANCE,
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
                BaseModGenes.SCARE_CREEPERS,
                Gene(
                    dnaPointsRequired = 20,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_CREEPER_GENE)
                )
            )

            context.register(
                BaseModGenes.SCARE_SKELETONS,
                Gene(
                    dnaPointsRequired = 20,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_SKELETON_GENE)
                )
            )

            context.register(
                BaseModGenes.SCARE_SPIDERS,
                Gene(
                    dnaPointsRequired = 50,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_SPIDER_GENE)
                )
            )

            context.register(
                BaseModGenes.SCARE_ZOMBIES,
                Gene(
                    dnaPointsRequired = 50,
                    scaresEntitiesWithTag = Optional.of(ModEntityTypeTagsProvider.AVOIDS_SCARE_ZOMBIE_GENE)
                )
            )

            context.register(
                BaseModGenes.SHOOT_FIREBALLS,
                Gene(
                    dnaPointsRequired = 24,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.SLIMY_DEATH,
                Gene(
                    dnaPointsRequired = 60,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.SPEED,
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
                BaseModGenes.STEP_ASSIST,
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
                BaseModGenes.STRENGTH,
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
                BaseModGenes.TELEPORT,
                Gene(
                    dnaPointsRequired = 24,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.THORNS,
                Gene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                BaseModGenes.WALL_CLIMBING,
                Gene(
                    dnaPointsRequired = 40,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.WATER_BREATHING,
                Gene(
                    dnaPointsRequired = 16
                )
            )

            context.register(
                BaseModGenes.WITHER_HIT,
                Gene(
                    dnaPointsRequired = 20,
                )
            )

            context.register(
                BaseModGenes.WITHER_PROOF,
                Gene(
                    dnaPointsRequired = 40
                )
            )

            context.register(
                BaseModGenes.WOOLY,
                Gene(
                    dnaPointsRequired = 12
                )
            )

            context.register(
                BaseModGenes.XP_MAGNET,
                Gene(
                    dnaPointsRequired = 30,
                    allowedEntities = onlyPlayers
                )
            )

            //Negative effects

            //FIXME: This effect apparently has a sound on add now, probably want to remove that

            context.register(
                BaseModGenes.BAD_OMEN,
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
                BaseModGenes.BLINDNESS,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.BLINDNESS
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.CRINGE,
                Gene(
                    dnaPointsRequired = 20,
                    allowedEntities = onlyPlayers
                )
            )

            context.register(
                BaseModGenes.CURSED,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.UNLUCK
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.FLAMBE,
                Gene(
                    dnaPointsRequired = 1
                )
            )

            context.register(
                BaseModGenes.HUNGER,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.HUNGER
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.INFESTED,
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
                BaseModGenes.LEVITATION,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.LEVITATION
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.MINING_FATIGUE,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.DIG_SLOWDOWN
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.NAUSEA,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.CONFUSION
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.OOZING,
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
                BaseModGenes.POISON,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.POISON
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.POISON_FOUR,
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
                BaseModGenes.SLOWNESS,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.MOVEMENT_SLOWDOWN
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.SLOWNESS_FOUR,
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
                BaseModGenes.SLOWNESS_SIX,
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
                BaseModGenes.WEAVING,
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
                BaseModGenes.WEAKNESS,
                Gene(
                    potionDetails = Optional.of(
                        Gene.PotionDetails(
                            effect = MobEffects.WEAKNESS
                        )
                    )
                )
            )

            context.register(
                BaseModGenes.WIND_CHARGED,
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
                BaseModGenes.WITHER,
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
                BaseModGenes.BLACK_DEATH,
                Gene()
            )

            context.register(
                BaseModGenes.GREEN_DEATH,
                Gene()
            )

            context.register(
                BaseModGenes.WHITE_DEATH,
                Gene()
            )

            context.register(
                BaseModGenes.GRAY_DEATH,
                Gene()
            )

            context.register(
                BaseModGenes.UN_UNDEATH,
                Gene()
            )

        }
    }

}