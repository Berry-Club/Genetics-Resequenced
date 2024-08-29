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
            "1_2",
        )
    }

    override fun generateEntries() {
        this.add(whatAreGenes('1'))
        this.add(gettingGenes('2'))
    }

    private fun whatAreGenes(location: Char): BookEntryModel? {
        val entry = object : BaseEntryProvider(
            this@GettingStartedCategoryProvider,
            ModItems.DNA_HELIX,
            "what_are_genes",
        ) {

            override fun generatePages() {

                textPage(
                    "What are Genes?",
                    paragraphs(
                        "${major("Genes")} can be taken from mobs to ${minor("harness their abilities")}.",
                        "For example, Sheep have the ${minor("Wooly")} Gene. If you inject this into yourself, you will be able to be sheared for wool!",
                        "To see a full list of Genes, ${minor("see here")}."
                    )
                )

                textPage(
                    paragraphs(
                        "Each Gene page in the book will say what the Gene does, and how to obtain it.",
                        "Some Genes can even be given to mobs!"
                    )
                )

            }

            override fun entryName(): String {
                return "What are Genes?"
            }
        }

        return entry.generate(location)
    }

    private fun gettingGenes(location: Char): BookEntryModel? {
        val entry = object : BaseEntryProvider(
            this@GettingStartedCategoryProvider,
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
                            itemEntryLink(
                                "Scraper",
                                "scraper"
                            )
                        }. This tool can be used on entities to collect ${
                            itemEntryLink(
                                "Organic Matter",
                                "organic_matter"
                            )
                        } from them.",
                        "The Organic Matter will have the entity's type attached. This is what decides what Genes you can get from it."
                    )
                )

            }

            override fun entryName(): String {
                return "Getting Genes"
            }
        }

        return entry.generate(location)
    }
}