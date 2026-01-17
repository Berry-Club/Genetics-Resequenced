package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.*
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
			UUID.fromString("6eb382cf-8600-465c-863e-98a744e13ca0"),
			GeneticsResequenced.modResource("knockback").toString(),
			2.0,
			AttributeModifier.Operation.ADDITION
		)

		val MORE_HEALTH_ONE = AttributeModifier(
			UUID.fromString("29aa1ce2-dfde-4903-8773-8e829d8d1418"),
			GeneticsResequenced.modResource("more_health_one").toString(),
			20.0,
			AttributeModifier.Operation.ADDITION
		)

		val MORE_HEALTH_TWO = AttributeModifier(
			UUID.fromString("061aab68-8432-45f6-b585-c93418b2cda5"),
			GeneticsResequenced.modResource("more_health_two").toString(),
			20.0,
			AttributeModifier.Operation.ADDITION
		)

		val STEP_ASSIST = AttributeModifier(
			UUID.fromString("2a73add8-ca9b-4100-94e5-b16effc58e92"),
			GeneticsResequenced.modResource("step_assist").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val EFFICIENCY = AttributeModifier(
			UUID.fromString("4a2dcd73-a0e5-47f2-9034-b23cc880d4c6"),
			GeneticsResequenced.modResource("efficiency").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val EFFICIENCY_FOUR = AttributeModifier(
			UUID.fromString("b51a491b-acd2-4cb7-abe4-66d65240bf95"),
			GeneticsResequenced.modResource("efficiency_four").toString(),
			3.0, // Because you can't have this without the first level
			AttributeModifier.Operation.ADDITION
		)

		val REACHING = AttributeModifier(
			UUID.fromString("57991785-80e0-4c10-8c8b-2c53a08fdbf1"),
			GeneticsResequenced.modResource("reaching").toString(),
			1.25,
			AttributeModifier.Operation.MULTIPLY_TOTAL
		)

		val BOUNTIFUL = AttributeModifier(
			UUID.fromString("708bf329-fd4c-4c4e-87c0-1971cc749fcb"),
			GeneticsResequenced.modResource("bountiful").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val BOUNTIFUL_TWO = AttributeModifier(
			UUID.fromString("1cc517ad-8022-4045-a3e4-53f5bef7a9e1"),
			GeneticsResequenced.modResource("bountiful_two").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)

		val EXPERIENCED = AttributeModifier(
			UUID.fromString("95644827-8a6d-43db-9869-e5db4c970d71"),
			GeneticsResequenced.modResource("experienced").toString(),
			1.0,
			AttributeModifier.Operation.ADDITION
		)
	}

}