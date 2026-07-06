package dev.aaronhowser.mods.genetics_resequenced.datagen.tag

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.registry.ModPotions
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.KeyTagProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.item.alchemy.Potion
import java.util.concurrent.CompletableFuture

class ModPotionTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : KeyTagProvider<Potion>(
	output,
	Registries.POTION,
	lookupProvider,
	GeneticsResequenced.MOD_ID
) {

	override fun addTags(p0: HolderLookup.Provider) {
		tag(CAN_HAVE_ENTITY)
			.add(
				ModPotions.CELL_GROWTH.key,
				ModPotions.MUTATION.key
			)
	}

	companion object {
		private fun create(id: String): TagKey<Potion> {
			return TagKey.create(Registries.POTION, GeneticsResequenced.modId(id))
		}

		val CAN_HAVE_ENTITY = create("can_have_entity")
	}
}
