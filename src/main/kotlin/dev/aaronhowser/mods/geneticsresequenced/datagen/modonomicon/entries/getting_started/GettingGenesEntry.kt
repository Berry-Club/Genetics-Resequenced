package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.getting_started

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.BaseEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems

class GettingGenesEntry(
    parent: CategoryProviderBase
) : BaseEntryProvider(parent, "getting_genes") {
    override fun generatePages() {

        textPage(
            "Getting Genes",
            "Getting Genes is a rather involved process."
        )

    }

    override fun entryName(): String {
        return "Getting Genes"
    }

    override fun entryDescription(): String {
        return ""
    }

    override fun entryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.CELL)
    }
}