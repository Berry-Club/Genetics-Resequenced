package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModAttributes {

	val ATTRIBUTE_REGISTRY: DeferredRegister<Attribute> =
		DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, GeneticsResequenced.MOD_ID)

	val EFFICIENCY: DeferredHolder<Attribute, RangedAttribute> =
		register("efficiency", 0.0, 0.0, 10000.0)

	@JvmField
	val BASE_LOOTING: DeferredHolder<Attribute, RangedAttribute> =
		register("base_looting", 0.0, 0.0, 10000.0)

	val XP_DROP_MULTIPLIER: DeferredHolder<Attribute, RangedAttribute> =
		register("xp_drop_multiplier", 1.0, 0.0, 1000.0)

	private fun register(
		name: String,
		default: Double,
		min: Double,
		max: Double
	): DeferredHolder<Attribute, RangedAttribute> {
		return ATTRIBUTE_REGISTRY.register(name, Supplier {
			RangedAttribute("genetics_resequenced.$name", default, min, max)
		})
	}

	object AttributeModifiers {
		//TODO: Maybe make this not an attribute modifier?
		val KNOCKBACK = AttributeModifier(
			GeneticsResequenced.modResource("knockback"),
			2.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val FLIGHT = AttributeModifier(
			GeneticsResequenced.modResource("flight"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val MORE_HEALTH_ONE = AttributeModifier(
			GeneticsResequenced.modResource("more_health_one"),
			20.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val MORE_HEALTH_TWO = AttributeModifier(
			GeneticsResequenced.modResource("more_health_two"),
			20.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val STEP_ASSIST = AttributeModifier(
			GeneticsResequenced.modResource("step_assist"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val EFFICIENCY = AttributeModifier(
			GeneticsResequenced.modResource("efficiency"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val EFFICIENCY_FOUR = AttributeModifier(
			GeneticsResequenced.modResource("efficiency_four"),
			3.0, // Because you can't have this without the first level
			AttributeModifier.Operation.ADD_VALUE
		)

		val REACHING = AttributeModifier(
			GeneticsResequenced.modResource("reaching"),
			1.25,
			AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
		)

		val BOUNTIFUL = AttributeModifier(
			GeneticsResequenced.modResource("bountiful"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val BOUNTIFUL_TWO = AttributeModifier(
			GeneticsResequenced.modResource("bountiful_two"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)

		val EXPERIENCED = AttributeModifier(
			GeneticsResequenced.modResource("experienced"),
			1.0,
			AttributeModifier.Operation.ADD_VALUE
		)
	}

}