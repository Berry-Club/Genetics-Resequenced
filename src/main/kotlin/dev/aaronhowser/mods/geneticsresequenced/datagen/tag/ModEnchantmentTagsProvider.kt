package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.enchantment.ModEnchantments
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EnchantmentTagsProvider
import net.minecraft.tags.EnchantmentTags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEnchantmentTagsProvider(
    pOutput: PackOutput,
    pProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : EnchantmentTagsProvider(pOutput, pProvider, GeneticsResequenced.ID, existingFileHelper) {

    override fun addTags(p0: HolderLookup.Provider) {
        this.tag(EnchantmentTags.NON_TREASURE)
            .add(ModEnchantments.DELICATE_TOUCH)
    }


}