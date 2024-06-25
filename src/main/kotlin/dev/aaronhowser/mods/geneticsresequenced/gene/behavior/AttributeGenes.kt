package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.entity.player.PlayerEvent

object AttributeGenes {

    private val efficiencyRL = OtherUtil.modResource("efficiency")
    private val efficiencyAttributeModifier = AttributeModifier(
        efficiencyRL,
        1.0,
        AttributeModifier.Operation.ADD_VALUE
    )

    private val efficiencyFourRl = OtherUtil.modResource("efficiency_four")
    private val efficiencyFourAttributeModifier = AttributeModifier(
        efficiencyFourRl,
        3.0, // Because you can't have this without the first level
        AttributeModifier.Operation.ADD_VALUE
    )

    fun setEfficiency(player: Player, level: Int, adding: Boolean) {
        val attribute = player.getAttribute(ModAttributes.EFFICIENCY) ?: return

        val modifier = when (level) {
            1 -> efficiencyAttributeModifier
            4 -> efficiencyFourAttributeModifier
            else -> throw IllegalArgumentException("Invalid efficiency level: $level")
        }

        val resourceLocation = modifier.id

        if (adding) {
            attribute.addPermanentModifier(modifier)
        } else if (attribute.hasModifier(resourceLocation)) {
            attribute.removeModifier(modifier)
        }
    }

    fun handleEfficiency(event: PlayerEvent.BreakSpeed) {
        if (!ModGenes.efficiency.isActive) return

        val efficiencyAttribute = event.entity.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
        if (efficiencyAttribute.value <= 0.0) return

        event.newSpeed += (1 + efficiencyAttribute.value * efficiencyAttribute.value).toFloat()
    }

    private val stepAssistAttributeModifier = AttributeModifier(
        OtherUtil.modResource("step_assist"),
        1.0,
        AttributeModifier.Operation.ADD_VALUE
    )

    fun setStepAssist(player: Player, adding: Boolean) {
        if (!ModGenes.stepAssist.isActive && adding) return

        val stepHeightAttribute = player.getAttribute(Attributes.STEP_HEIGHT) ?: return

        if (adding) {
            stepHeightAttribute.addPermanentModifier(stepAssistAttributeModifier)
            return
        } else {
            stepHeightAttribute.removeModifier(stepAssistAttributeModifier)
        }
    }

    private val moreHealthOneRl = OtherUtil.modResource("more_health_one")
    private val moreHealthOneAttributeModifier = AttributeModifier(
        moreHealthOneRl,
        20.0,
        AttributeModifier.Operation.ADD_VALUE
    )

    private val moreHealthTwoRl = OtherUtil.modResource("more_health_two")
    private val moreHealthTwoAttributeModifier = AttributeModifier(
        moreHealthTwoRl,
        20.0,
        AttributeModifier.Operation.ADD_VALUE
    )

    //TODO: Mark as entity-allowed
    fun setMoreHearts(entity: LivingEntity, level: Int, adding: Boolean) {
        val maxHealthAttribute = entity.getAttribute(Attributes.MAX_HEALTH) ?: return

        when (level) {
            1 -> if (!ModGenes.moreHearts.isActive) return
            2 -> if (!ModGenes.moreHeartsTwo.isActive) return
            else -> throw IllegalArgumentException("Invalid more hearts level: $level")
        }

        val hasLevelOne = maxHealthAttribute.hasModifier(moreHealthOneRl)
        val hasLevelTwo = maxHealthAttribute.hasModifier(moreHealthTwoRl)

        when (level) {

            1 -> if (adding) {
                if (hasLevelOne) return
                maxHealthAttribute.addPermanentModifier(moreHealthOneAttributeModifier)
            } else {
                if (!hasLevelOne) return
                maxHealthAttribute.removeModifier(moreHealthOneAttributeModifier)
            }

            2 -> if (adding) {
                if (hasLevelTwo) return
                maxHealthAttribute.addPermanentModifier(moreHealthTwoAttributeModifier)
            } else {
                if (!hasLevelTwo) return
                maxHealthAttribute.removeModifier(moreHealthTwoAttributeModifier)
            }

        }
    }


    private val knockbackRl = OtherUtil.modResource("knockback")
    private val knockbackAttributeModifier = AttributeModifier(
        knockbackRl,
        ServerConfig.knockbackStrength.get(),
        AttributeModifier.Operation.ADD_VALUE
    )

    fun setKnockback(entity: LivingEntity, adding: Boolean) {
        //TODO: Maybe make this not attribute-based, so this check can be on attack instead?
        if (!ModGenes.knockback.isActive && adding) return

        val attackKnockBackAttribute = entity.getAttribute(Attributes.ATTACK_KNOCKBACK) ?: return

        if (adding) {
            attackKnockBackAttribute.addPermanentModifier(knockbackAttributeModifier)
        } else {
            attackKnockBackAttribute.removeModifier(knockbackAttributeModifier)
        }
    }


    private val wallClimbingRl = OtherUtil.modResource("wall_climbing")
    private val wallClimbingAttributeModifier = AttributeModifier(
        wallClimbingRl,
        1.0,
        AttributeModifier.Operation.ADD_VALUE
    )

    fun setWallClimbing(player: Player, adding: Boolean) {
        val wallClimbingAttribute = player.getAttribute(ModAttributes.WALL_CLIMBING) ?: return

        if (adding) {
            wallClimbingAttribute.addPermanentModifier(wallClimbingAttributeModifier)
        } else {
            wallClimbingAttribute.removeModifier(wallClimbingAttributeModifier)
        }
    }

    fun handleWallClimbing(player: Player) {
        if (!ModGenes.wallClimbing.isActive) return

        val walkClimbingAttributeValue = player.getAttributeValue(ModAttributes.WALL_CLIMBING)
        if (walkClimbingAttributeValue <= 0.0) return

        if (!player.hasGene(ModGenes.wallClimbing)) return

        if (player.horizontalCollision || player.minorHorizontalCollision) {
            player.setDeltaMovement(
                player.deltaMovement.x,
                if (player.isCrouching) 0.0 else ServerConfig.wallClimbSpeed.get(),
                player.deltaMovement.z
            )

            player.fallDistance = 0.0f
        }
    }

    private val flightRl = OtherUtil.modResource("flight")
    private val flightAttributeModifier = AttributeModifier(
        flightRl,
        1.0,
        AttributeModifier.Operation.ADD_VALUE
    )

    fun setFlight(player: Player, adding: Boolean) {
        if (!ModGenes.flight.isActive) return
        if (player.level().isClientSide) return

        val attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT) ?: return

        if (adding) {
            attribute.addPermanentModifier(flightAttributeModifier)
        } else {
            attribute.removeModifier(flightAttributeModifier)
        }
    }

}