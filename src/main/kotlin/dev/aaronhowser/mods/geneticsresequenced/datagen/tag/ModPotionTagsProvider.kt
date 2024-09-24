package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModPotionTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : TagsProvider<Potion>(
    output,
    Registries.POTION,
    lookupProvider,
    GeneticsResequenced.ID,
    existingFileHelper
) {

    companion object {
        private fun create(id: String): TagKey<Potion> {
            return TagKey.create(Registries.POTION, OtherUtil.modResource(id))
        }

        val CAN_HAVE_ENTITY = create("can_have_entity")
    }

    override fun addTags(p0: HolderLookup.Provider) {
        this.tag(CAN_HAVE_ENTITY)
            .add(
                ModPotions.CELL_GROWTH.key!!,
                ModPotions.MUTATION.key!!
            )
    }
}