package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon

import com.klikli_dev.modonomicon.api.datagen.SingleBookSubProvider
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories.BlocksCategoryProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories.GenesCategoryProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories.GettingStartedCategoryProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories.ItemsCategoryProvider
import java.util.function.BiConsumer

class ModModonomiconProvider(
    defaultLang: BiConsumer<String, String>
) : SingleBookSubProvider("guide", GeneticsResequenced.ID, defaultLang) {

    override fun generateCategories() {
        this.add(GettingStartedCategoryProvider(this).generate())
        this.add(ItemsCategoryProvider(this).generate())
        this.add(BlocksCategoryProvider(this).generate())
        this.add(GenesCategoryProvider(this).generate())
    }

    override fun bookName(): String {
        return "Big Book of Genetics"
    }

    override fun bookTooltip(): String {
        return "A guide to all things genetic"
    }

    override fun registerDefaultMacros() {
        // Nothing
    }

}
