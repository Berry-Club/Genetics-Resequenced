package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.BaseEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems

class GettingStartedCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    override fun categoryId(): String {
        return "getting_started"
    }

    override fun categoryName(): String {
        return "Getting Started"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.DNA_HELIX)
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf(
            "",
        )
    }

    override fun generateEntries() {
        var index = 0

        fun addEntry(entry: BookEntryModel) {
            this.add(entry.withSortNumber(index))
            index++
        }

        addEntry(whatAreGenes())
        addEntry(gettingGenes())
    }

    private fun whatAreGenes(): BookEntryModel {
        val entry = object : BaseEntryProvider(
            this@GettingStartedCategoryProvider,
            "What are Genes?",
            ModItems.DNA_HELIX,
            "what_are_genes",
        ) {

            override fun generatePages() {

                textPage(
                    "What are Genes?",
                    paragraphs(
                        "${major("Genes")} can be taken from mobs to ${minor("harness their abilities")}.",
                        "For example, Sheep have the ${gene("Wooly", "wooly")} Gene. If you inject this into yourself, you will be able to be sheared for wool!",
                        "To see a full list of Genes, ${categoryLink("see here", "genes")}."
                    )
                )

                textPage(
                    paragraphs(
                        "Each Gene page in the book will say what the Gene does. As for how to get it, the mod has [EMI](https://www.curseforge.com/minecraft/mc-mods/emi) integration.",
                        "Some Genes can even be given to mobs!"
                    )
                )

            }
        }

        return entry.generate()
    }

    private fun gettingGenes(): BookEntryModel {
        val entry = object : BaseEntryProvider(
            this@GettingStartedCategoryProvider,
            "Getting Genes",
            ModItems.CELL,
            "getting_genes"
        ) {
            override fun generatePages() {
                textPage(
                    "Getting Genes",
                    "Getting Genes is a rather involved process."
                )

                spotlightPage(
                    ModItems.SCRAPER,
                    paragraphs(
                        "First, you'll need the ${
                            item("Scraper", "scraper")
                        }. This tool can be used on entities to collect ${
                            item(
                                "Organic Matter",
                                "organic_matter"
                            )
                        } from them.",
                        "The Organic Matter will have the entity's type attached. This is what decides what Genes you can get from it."
                    )
                )

            }
        }

        return entry.generate()
    }
}