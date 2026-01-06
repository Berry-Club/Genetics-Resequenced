package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import java.util.concurrent.CompletableFuture

class ModDamageTypeProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DatapackBuiltinEntriesProvider(
	output,
	lookupProvider,
	BUILDER,
	setOf(GeneticsResequenced.MOD_ID)
) {

	companion object {
		val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, ::bootstrap)

		val VIRUS: ResourceKey<DamageType> = rk("virus")

		private fun rk(name: String): ResourceKey<DamageType> = ResourceKey.create(
			Registries.DAMAGE_TYPE,
			OtherUtil.modResource(name)
		)

		fun bootstrap(context: BootstapContext<DamageType>) {
			context.register(
				VIRUS,
				DamageType(
					"virus",
					DamageScaling.NEVER,
					0f,
					DamageEffects.HURT,
				)
			)
		}
	}

}