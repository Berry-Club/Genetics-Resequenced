package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModAttributes {

	val ATTRIBUTE_REGISTRY: DeferredRegister<Attribute> =
		DeferredRegister.create(ForgeRegistries.ATTRIBUTES, GeneticsResequenced.MOD_ID)

	private const val EFFICIENCY_ATTRIBUTE_NAME = "geneticsresequenced.efficiency"
	val EFFICIENCY: RegistryObject<RangedAttribute> =
		ATTRIBUTE_REGISTRY.register("efficiency") {
			RangedAttribute(
				EFFICIENCY_ATTRIBUTE_NAME,
				0.0,
				0.0, Double.MAX_VALUE
			)
		}

	object AttributeModifiers {
		//TODO: Maybe make this not an attribute modifier?
		val KNOCKBACK = AttributeModifier(
			OtherUtil.modResource("knockback").toString(),
			2.0,
			AttributeModifier.Operation.ADDITION
		)

		val FLIGHT = AttributeModifier(
			OtherUtil.modResource("flight").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val MORE_HEALTH_ONE = AttributeModifier(
			OtherUtil.modResource("more_health_one").toString(),
			20.0,
			AttributeModifier.Operation.ADDITION
		)

		val MORE_HEALTH_TWO = AttributeModifier(
			OtherUtil.modResource("more_health_two").toString(),
			20.0,
			AttributeModifier.Operation.ADDITION
		)

		val STEP_ASSIST = AttributeModifier(
			OtherUtil.modResource("step_assist").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val EFFICIENCY = AttributeModifier(
			OtherUtil.modResource("efficiency").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val EFFICIENCY_FOUR = AttributeModifier(
			OtherUtil.modResource("efficiency_four").toString(),
			3.0, // Because you can't have this without the first level
			AttributeModifier.Operation.ADDITION
		)

		val REACHING = AttributeModifier(
			OtherUtil.modResource("reaching").toString(),
			1.25,
			AttributeModifier.Operation.MULTIPLY_BASE
		)
	}

}