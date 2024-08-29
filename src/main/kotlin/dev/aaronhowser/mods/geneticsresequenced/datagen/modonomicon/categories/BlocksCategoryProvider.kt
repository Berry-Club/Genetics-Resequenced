package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.BaseEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks

class BlocksCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    val realThis = this

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
        this.add(antiFieldBlock('f'))
        this.add(bloodPurifierBlock('b'))
        this.add(cellAnalyzer('a'))
//        this.add(coalGenerator('g'))
//        this.add(dnaExtractor('e'))
//        this.add(incubator('i'))
//        this.add(advancedIncubator('v'))
//        this.add(plasmidInfuser('p'))
//        this.add(plasmidInjector('j'))
    }

    private fun cellAnalyzer(location: Char): BookEntryModel? {
        val entry = object : BaseEntryProvider(
            realThis,
            ModBlocks.CELL_ANALYZER,
            "cell_analyzer"
        ) {
            override fun generatePages() {
                spotlightPage(
                    ModBlocks.CELL_ANALYZER.toStack(),
                    "Cell Analyzer",
                    "The ${major("Cell Analyzer")} uses FE to convert ${
                        itemEntryLink(
                            "Organic Matter",
                            "organic_matter"
                        )
                    } into ${itemEntryLink("Cells", "cell")}."
                )
            }

            override fun entryName(): String {
                return "Cell Analyzer"
            }
        }

        return entry.generate(location)
    }

    private fun bloodPurifierBlock(location: Char): BookEntryModel? {
        val entry = object : BaseEntryProvider(
            realThis,
            ModBlocks.BLOOD_PURIFIER,
            "blood_purifier"
        ) {
            override fun generatePages() {
                spotlightPage(
                    ModBlocks.BLOOD_PURIFIER.toStack(),
                    "Blood Purifier",
                    "The ${major("Blood Purifier")} uses FE to decontaminate ${itemEntryLink("Syringes", "syringe")}."
                )

            }

            override fun entryName(): String {
                return "Blood Purifier"
            }
        }

        return entry.generate(location)
    }

    private fun antiFieldBlock(location: Char): BookEntryModel? {
        val entry = object : BaseEntryProvider(
            realThis,
            ModBlocks.ANTI_FIELD_BLOCK,
            "anti_field_block"
        ) {
            override fun generatePages() {
                textPage(
                    "Anti-Field Block",
                    paragraphs(
                        "The ${major("Anti-Field Block")} allows you to ${minor("temporarily disable certain Genes")}.",
                        "Specifically, it disabled the ${
                            geneEntryLink(
                                "Item Attraction Field",
                                "item_attraction_field"
                            )
                        } and ${geneEntryLink("XP Attraction Field", "xp_attraction_field")} when enabled.",
                        "The ${
                            geneEntryLink(
                                "Anti-Field Orb",
                                "anti_field_orb"
                            )
                        } functions similarly, but in item form."
                    )
                )

                spotlightPage(
                    ModBlocks.ANTI_FIELD_BLOCK,
                    paragraphs(
                        "The Anti-Field Block is active by default, and can be disabled with a Redstone signal.",
                        "While active, it disables all Fields within a 25 block spherical radius. This amount is configurable."
                    )
                )

            }

            override fun entryName(): String {
                return "Anti-Field Block"
            }

        }

        return entry.generate(location)
    }

    override fun categoryName(): String {
        return "Blocks"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModBlocks.BLOOD_PURIFIER)
    }
}