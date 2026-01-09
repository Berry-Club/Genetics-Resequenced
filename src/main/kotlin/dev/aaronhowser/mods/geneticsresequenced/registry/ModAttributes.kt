package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModAttributes {

	val ATTRIBUTE_REGISTRY: DeferredRegister<Attribute> =
		DeferredRegister.create(ForgeRegistries.ATTRIBUTES, GeneticsResequenced.MOD_ID)

	val EFFICIENCY: RegistryObject<RangedAttribute> =
		register("efficiency", 0.0, 0.0, 10000.0)

	@JvmField
	val BASE_LOOTING: RegistryObject<RangedAttribute> =
		register("base_looting", 0.0, 0.0, 10000.0)

	val XP_DROP_MULTIPLIER: RegistryObject<RangedAttribute> =
		register("xp_drop_multiplier", 1.0, 0.0, 1000.0)

	private fun register(
		name: String,
		default: Double,
		min: Double,
		max: Double
	): RegistryObject<RangedAttribute> {
		return ATTRIBUTE_REGISTRY.register(name, Supplier {
			RangedAttribute("geneticsresequenced.$name", default, min, max)
		})
	}

	object AttributeModifiers {
		//TODO: Maybe make this not an attribute modifier?
		val KNOCKBACK = AttributeModifier(
			GeneticsResequenced.modResource("knockback").toString(),
			2.0,
			AttributeModifier.Operation.ADDITION
		)

		val MORE_HEALTH_ONE = AttributeModifier(
			GeneticsResequenced.modResource("more_health_one").toString(),
			20.0,
			AttributeModifier.Operation.ADDITION
		)

		val MORE_HEALTH_TWO = AttributeModifier(
			GeneticsResequenced.modResource("more_health_two").toString(),
			20.0,
			AttributeModifier.Operation.ADDITION
		)

		val STEP_ASSIST = AttributeModifier(
			GeneticsResequenced.modResource("step_assist").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val EFFICIENCY = AttributeModifier(
			GeneticsResequenced.modResource("efficiency").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val EFFICIENCY_FOUR = AttributeModifier(
			GeneticsResequenced.modResource("efficiency_four").toString(),
			3.0, // Because you can't have this without the first level
			AttributeModifier.Operation.ADDITION
		)

		val REACHING = AttributeModifier(
			GeneticsResequenced.modResource("reaching").toString(),
			1.25,
			AttributeModifier.Operation.MULTIPLY_TOTAL
		)

		val BOUNTIFUL = AttributeModifier(
			GeneticsResequenced.modResource("bountiful").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val BOUNTIFUL_TWO = AttributeModifier(
			GeneticsResequenced.modResource("bountiful_two").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val EXPERIENCED = AttributeModifier(
			GeneticsResequenced.modResource("experienced").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)
	}

}