package dev.aaronhowser.mods.geneticsresequenced.datagen.datapack

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType

object ModDamageTypeProvider {

	private fun createRk(name: String): ResourceKey<DamageType> =
		ResourceKey.create(Registries.DAMAGE_TYPE, GeneticsResequenced.modResource(name))

	val STEP_ON_SYRINGE: ResourceKey<DamageType> = createRk("step_on_syringe")
	val USE_SYRINGE: ResourceKey<DamageType> = createRk("use_syringe")
	val USE_SCRAPER: ResourceKey<DamageType> = createRk("use_scraper")

	fun bootstrap(context: BootstrapContext<DamageType>) {
		context.register(
			STEP_ON_SYRINGE,
			DamageType(
				ModMessageLang.DEATH_SYRINGE_PICKUP,
				1f
			)
		)

		context.register(
			USE_SYRINGE,
			DamageType(
				ModMessageLang.DEATH_SYRINGE,
				1f
			)
		)

		context.register(
			USE_SCRAPER,
			DamageType(
				ModMessageLang.DEATH_SCRAPER,
				0.1f
			)
		)
	}

}