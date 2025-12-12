package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModAttributes {

	val ATTRIBUTE_REGISTRY: DeferredRegister<Attribute> =
		DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, GeneticsResequenced.ID)

	val EFFICIENCY: DeferredHolder<Attribute, RangedAttribute> =
		register("efficiency", 0.0, 0.0, 10000.0)

	val BOUNTIFUL: DeferredHolder<Attribute, RangedAttribute> =
		register("bountiful", 0.0, 0.0, 10000.0)

	private fun register(name: String, default: Double, min: Double, max: Double): DeferredHolder<Attribute, RangedAttribute> {
		return ATTRIBUTE_REGISTRY.register(name, Supplier {
			RangedAttribute("geneticsresequenced.$name", default, min, max)
		})
	}

	object AttributeModifiers {
		//TODO: Maybe make this not an attribute modifier?
		val KNOCKBACK = AttributeModifier(
			OtherUtil.modResource("knockback"),
			2.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val FLIGHT = AttributeModifier(
			OtherUtil.modResource("flight"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val MORE_HEALTH_ONE = AttributeModifier(
			OtherUtil.modResource("more_health_one"),
			20.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val MORE_HEALTH_TWO = AttributeModifier(
			OtherUtil.modResource("more_health_two"),
			20.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val STEP_ASSIST = AttributeModifier(
			OtherUtil.modResource("step_assist"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val EFFICIENCY = AttributeModifier(
			OtherUtil.modResource("efficiency"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val EFFICIENCY_FOUR = AttributeModifier(
			OtherUtil.modResource("efficiency_four"),
			3.0, // Because you can't have this without the first level
			AttributeModifier.Operation.ADD_VALUE
		)

		val REACHING = AttributeModifier(
			OtherUtil.modResource("reaching"),
			1.25,
			AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
		)

		val BOUNTIFUL = AttributeModifier(
			OtherUtil.modResource("bountiful"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)
	}

}