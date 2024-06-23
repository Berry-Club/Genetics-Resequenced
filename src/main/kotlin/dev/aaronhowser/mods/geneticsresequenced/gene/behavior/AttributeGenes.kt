package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.event.entity.player.PlayerEvent

object AttributeGenes {

    private val efficiencyModifierRL = OtherUtil.modResource("efficiency")
    private val efficiencyAttributeModifier = AttributeModifier(
        efficiencyModifierRL,
        1.0,
        AttributeModifier.Operation.ADD_VALUE
    )

    private val efficiencyFourModifierRL = OtherUtil.modResource("efficiency_four")
    private val efficiencyFourAttributeModifier = AttributeModifier(
        efficiencyFourModifierRL,
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


}