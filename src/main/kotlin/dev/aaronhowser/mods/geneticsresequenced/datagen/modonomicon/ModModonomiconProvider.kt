package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon

import com.klikli_dev.modonomicon.api.datagen.SingleBookSubProvider
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories.GettingStartedCategoryProvider
import java.util.function.BiConsumer

class ModModonomiconProvider(
    defaultLang: BiConsumer<String, String>
) : SingleBookSubProvider("guide", GeneticsResequenced.ID, defaultLang) {

    private val gettingStartedCategoryProvider = GettingStartedCategoryProvider(this)

    override fun generateCategories() {
        this.add(gettingStartedCategoryProvider.generate())
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
