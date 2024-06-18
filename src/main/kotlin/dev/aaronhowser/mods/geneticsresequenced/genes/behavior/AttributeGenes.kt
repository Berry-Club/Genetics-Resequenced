package dev.aaronhowser.mods.geneticsresequenced.genes.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registries.ModAttributes
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.event.entity.player.PlayerEvent

object AttributeGenes {

    fun setEfficiency(player: Player, newLevel: Int) {
        val attributes = player.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
        attributes.baseValue = newLevel.toDouble()
    }

    fun handleEfficiency(event: PlayerEvent.BreakSpeed) {
        if (!ModGenes.efficiency.isActive) return

        val efficiencyAttribute = event.entity.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
        if (efficiencyAttribute.baseValue <= 0.0) return

        event.newSpeed += (1 + efficiencyAttribute.baseValue * efficiencyAttribute.baseValue).toFloat()
    }

    private val stepAssistAttributeModifier = AttributeModifier(
        "Genetics Resequenced: Step Assist",
        1.0,
        AttributeModifier.Operation.ADDITION
    )

    fun setStepAssist(player: Player, adding: Boolean) {
        if (!ModGenes.stepAssist.isActive && adding) return

        val stepHeightAttribute = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()) ?: return

        if (adding) {
            stepHeightAttribute.addPermanentModifier(stepAssistAttributeModifier)
            return
        } else {
            stepHeightAttribute.removeModifier(stepAssistAttributeModifier)
        }
    }

    private val moreHealthOneModifier = AttributeModifier(
        "Genetics Resequenced: More Hearts 1",
        20.0,
        AttributeModifier.Operation.ADDITION
    )

    private val moreHealthTwoModifier = AttributeModifier(
        "Genetics Resequenced: More Hearts 2",
        20.0,
        AttributeModifier.Operation.ADDITION
    )

    fun setMoreHearts(player: Player, level: Int, adding: Boolean = true) {
        val maxHealthAttribute = player.getAttribute(Attributes.MAX_HEALTH) ?: return

        val hasLevelOne = maxHealthAttribute.hasModifier(moreHealthOneModifier)
        val hasLevelTwo = maxHealthAttribute.hasModifier(moreHealthTwoModifier)

        when (level) {
            1 -> if (!ModGenes.moreHearts.isActive) return
            2 -> if (!ModGenes.moreHeartsTwo.isActive) return
        }

        when (level) {
            1 -> if (adding) {
                if (hasLevelOne) return
                maxHealthAttribute.addPermanentModifier(moreHealthOneModifier)
            } else {
                if (!hasLevelOne) return
                maxHealthAttribute.removeModifier(moreHealthOneModifier)
            }

            2 -> {
                if (adding) {
                    if (hasLevelTwo) return
                    maxHealthAttribute.addPermanentModifier(moreHealthTwoModifier)
                } else {
                    if (!hasLevelTwo) return
                    maxHealthAttribute.removeModifier(moreHealthTwoModifier)
                }
            }

            else -> throw IllegalArgumentException("Invalid level for more hearts: $level")
        }
    }

    private val knockbackAttributeModifier = AttributeModifier(
        "Genetics Resequenced: Extra Knock Back",
        ServerConfig.knockbackStrength.get(),
        AttributeModifier.Operation.ADDITION
    )

    fun setKnockback(player: Player, adding: Boolean) {
        if (!ModGenes.knockback.isActive && adding) return

        val attackKnockBackAttribute = player.getAttribute(Attributes.ATTACK_KNOCKBACK) ?: return

        if (adding) {
            attackKnockBackAttribute.addPermanentModifier(knockbackAttributeModifier)
        } else {
            attackKnockBackAttribute.removeModifier(knockbackAttributeModifier)
        }
    }

    fun setWallClimbing(player: Player, value: Boolean) {
        val wallClimbAttribute = player.attributes.getInstance(ModAttributes.WALL_CLIMBING) ?: return
        wallClimbAttribute.baseValue = if (value) 1.0 else 0.0
    }

    fun handleWallClimbing(player: Player) {
        if (!ModGenes.wallClimbing.isActive) return

        val wallClimbingAttribute = player.attributes.getInstance(ModAttributes.WALL_CLIMBING) ?: return
        if (wallClimbingAttribute.baseValue <= 0.0) return

        if (!player.hasGene(ModGenes.wallClimbing)) return

        // ONLY HAPPENS ON CLIENT?????
        if (player.horizontalCollision || player.minorHorizontalCollision) {
            player.setDeltaMovement(
                player.deltaMovement.x,
                if (player.isCrouching) 0.0 else ServerConfig.wallClimbSpeed.get(),
                player.deltaMovement.z
            )
            player.fallDistance = 0.0f
        }

    }

}