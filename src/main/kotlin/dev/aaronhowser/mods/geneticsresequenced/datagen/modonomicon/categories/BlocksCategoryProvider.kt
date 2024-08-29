package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks

class BlocksCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {
    override fun categoryId(): String {
        return "blocks"
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf(
            "___",
            "___"
        )
    }

    override fun generateEntries() {

    }

    override fun categoryName(): String {
        return "Blocks"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModBlocks.BLOOD_PURIFIER)
    }
}