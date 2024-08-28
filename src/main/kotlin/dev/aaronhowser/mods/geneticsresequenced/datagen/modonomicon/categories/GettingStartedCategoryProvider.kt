package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.getting_started.GenesOneEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems

class GettingStartedCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    override fun categoryId(): String {
        return "getting_started"
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf(
            "_1_",
            "___"
        )
    }

    override fun generateEntries() {
        this.add(GenesOneEntryProvider(this).generate('1'))
    }

    override fun categoryName(): String {
        return "Getting Started"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.DNA_HELIX)
    }
}