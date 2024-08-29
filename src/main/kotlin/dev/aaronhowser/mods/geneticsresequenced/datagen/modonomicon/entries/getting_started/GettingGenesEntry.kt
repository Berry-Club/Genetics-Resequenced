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

        spotlightPage(
            ModItems.SCRAPER,
            paragraphs(
                "First, you'll need the ${
                    1
//                    Companion.entryLink(
//                        "Scraper",
//                        "scraper"
//                    )
                }. This tool can be used on entities to collect ${
                    1
//                    Companion.entryLink(
//                        "Organic Matter",
//                        "organic_matter"
//                    )
                } from them.",
                "The Organic Matter will have the entity's type attached. This is what decides what Genes you can get from it."
            )
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