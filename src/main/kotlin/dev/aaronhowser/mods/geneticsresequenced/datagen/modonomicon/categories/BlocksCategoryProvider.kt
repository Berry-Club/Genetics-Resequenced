package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.BaseEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.world.entity.EntityType

class BlocksCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    val realThis = this

    override fun categoryName(): String {
        return "Blocks"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModBlocks.BLOOD_PURIFIER)
    }

    override fun categoryId(): String {
        return "blocks"
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf(
            "g",
            "aed",
            "bpj",
            "iv",
            "f"
        )
    }

    override fun generateEntries() {
        var index = 0

        fun addEntry(entry: BookEntryModel) {
            this.add(entry.withSortNumber(index))
            index++
        }

        addEntry(coalGenerator('g'))
        addEntry(cellAnalyzer('a'))
        addEntry(dnaExtractor('e'))
        addEntry(dnaDecryptor('d'))
        addEntry(bloodPurifierBlock('b'))
        addEntry(plasmidInfuser('p'))
        addEntry(plasmidInjector('j'))
        addEntry(incubator('i'))
        addEntry(advancedIncubator('v'))
        addEntry(antiFieldBlock('f'))
    }

    private fun plasmidInjector(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Plasmid Injector",
            ModBlocks.PLASMID_INJECTOR,
            "plasmid_injector"
        ) {
            override fun generatePages() {
                textPage(
                    "Plasmid Injector",
                    paragraphs(
                        "The ${major("Plasmid Injector")} uses FE to inject completed ${
                            item("Plasmids", "plasmid")
                        } into a ${
                            item("Syringe", "syringe")
                        }.",
                        "You can inject as many Plasmids into a single Syringe as you want."
                    )
                )

                spotlightPage(
                    ModBlocks.PLASMID_INJECTOR,
                    paragraphs(
                        "The Syringe must be full of uncompromised blood.",
                        "Decontaminate blood in the ${block("Blood Purifier", "blood_purifier")}."
                    )
                )
            }
        }

        return entry.generate(location)
    }

    private fun plasmidInfuser(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Plasmid Infuser",
            ModBlocks.PLASMID_INFUSER,
            "plasmid_infuser"
        ) {
            override fun generatePages() {
                textPage(
                    "Plasmid Infuser",
                    paragraphs(
                        "The ${major("Plasmid Infuser")} uses FE to infuse ${
                            item("DNA Helices", "dna_helix")
                        } into a ${
                            item("Plasmid", "plasmid")
                        }.",
                        "Insert an empty Plasmid in the right slot, and a DNA Helix in the left slot.",
                        "The DNA Helix will be used up, and set the Plasmid's Gene to match the Helix's."
                    )
                )

                spotlightPage(
                    ModBlocks.PLASMID_INFUSER,
                    paragraphs(
                        "Each Gene requires a certain amount of ${minor("DNA Points")} for the Plasmid to be completed.",
                        "- Basic Genes are worth 1 point.\n- Genes of the Plasmid's type are worth 2 points.",
                        "A Basic Gene cannot be the first one infused into a Plasmid."
                    )
                )

            }
        }

        return entry.generate(location)
    }

    private fun advancedIncubator(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Advanced Incubator",
            ModBlocks.ADVANCED_INCUBATOR,
            "advanced_incubator"
        ) {
            override fun generatePages() {
                textPage(
                    "Advanced Incubator",
                    paragraphs(
                        "The ${major("Advanced Incubator")} is an upgrade to the ${
                            block("Incubator", "incubator")
                        }.",
                        "Like the Incubator, it functions as a Brewing Stand. However, the Advanced Incubator has a temperature mechanic.",
                        "The coil on the left of the GUI can be clicked to toggle between low and high temperatures."
                    )
                )

                spotlightPage(
                    ModBlocks.ADVANCED_INCUBATOR,
                    paragraphs(
                        "By default, it's ${bad("120 times slower")} at low-temperature. This means a single brew takes a full 20 minutes, excluding Overclockers.",
                        "The reason you'd *want* to use low temperature mode is that that's the only way you can get ${
                            item("Genetically Modified Cells", "potion_cell_growth@gmo_cell")
                        }"
                    )
                )

                val gmoStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(
                    gmoStack,
                    EntityType.IRON_GOLEM,
                    ModGenes.REGENERATION.getHolderOrThrow(registries())
                )

                spotlightPage(
                    gmoStack,
                    paragraphs(
                        "${
                            item("Genetically Modified Cells", "potion_cell_growth@gmo_cell")
                        } are ${minor("guaranteed to have a specific Gene")}, if crafted correctly.",
                        "Each GM Cell has its own recipe, with a ${bad("chance of failure")}.",
                        "The recipe for this GM Cell, for example, has a 30%% chance of success by default."
                    )
                )

                textPage(
                    paragraphs(
                        "Each Overclocker ${bad("decreases the chance by 10%%")}.",
                        "However, you can insert ${minor("Chorus Fruit")} into the top right slot to increase your odds!",
                        "By default, each Chorus Fruit increases the chance by 10%%. The Advanced Incubator will up as many Chorus Fruit as it takes to reach 100%."
                    )
                )

            }
        }

        return entry.generate(location)
    }

    private fun incubator(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Incubator",
            ModBlocks.INCUBATOR,
            "incubator"
        ) {
            override fun generatePages() {
                textPage(
                    "Incubator",
                    "The ${major("Incubator")} is effectively a faster Brewing Stand. It runs at twice the speed, but ${
                        bad("costs FE instead of Blaze Powder")
                    }."
                )

                spotlightPage(
                    ModBlocks.INCUBATOR,
                    "It speed can be increased with ${item("Overclockers", "overclocker")}."
                )

            }
        }

        return entry.generate(location)
    }

    private fun dnaDecryptor(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "DNA Decryptor",
            ModBlocks.DNA_DECRYPTOR,
            "dna_decryptor"
        ) {
            override fun generatePages() {
                textPage(
                    "DNA Decryptor",
                    paragraphs(
                        "The ${major("DNA Decryptor")} uses FE to decrypt encrypted ${
                            item("DNA Helices", "dna_helix")
                        }.",
                        "Every time DNA is decrypted, it will choose a ${
                            categoryLink("Gene", "genes")
                        }, based on the Entity the Helix came from.",
                        "This Gene is weighted, some Genes have a higher chance than others. The Gene is chosen at the start of the process."
                    )
                )

                spotlightPage(
                    ModBlocks.DNA_DECRYPTOR,
                    "If the Decryptor isn't working and there's a DNA Helix in the output slot, try taking it out. The next Gene may be of a different type, which means it can't stack with the one in the output slot."
                )

            }
        }

        return entry.generate(location)
    }

    private fun dnaExtractor(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "DNA Extractor",
            ModBlocks.DNA_EXTRACTOR,
            "dna_extractor"
        ) {
            override fun generatePages() {
                spotlightPage(
                    ModBlocks.DNA_EXTRACTOR.toStack(),
                    "DNA Extractor",
                    "The ${major("DNA Extractor")} uses FE to convert ${
                        item("Cells", "cell")
                    } into encrypted ${item("DNA Helices", "dna_helix")}."
                )
            }
        }

        return entry.generate(location)
    }

    private fun coalGenerator(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Coal Generator",
            ModBlocks.COAL_GENERATOR,
            "coal_generator"
        ) {
            override fun generatePages() {
                textPage(
                    "Coal Generator",
                    paragraphs(
                        "The ${major("Coal Generator")} burns furnace fuels to generate FE.",
                        "Furnace fuels that burn longer produce more FE.",
                        "The amount is configurable, but defaults to 6FE/t. With this, ${minor("1 Coal generates 9,600 FE")}."
                    )
                )

                spotlightPage(
                    ModBlocks.COAL_GENERATOR,
                    "If the Coal Generator fills up with fuel burning, it ${minor("pauses until it has room for more energy")}, preventing waste!"
                )
            }
        }

        return entry.generate(location)
    }

    private fun cellAnalyzer(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Cell Analyzer",
            ModBlocks.CELL_ANALYZER,
            "cell_analyzer"
        ) {
            override fun generatePages() {
                spotlightPage(
                    ModBlocks.CELL_ANALYZER.toStack(),
                    "Cell Analyzer",
                    "The ${major("Cell Analyzer")} uses FE to convert ${
                        item("Organic Matter", "organic_matter")
                    } into ${item("Cells", "cell")}."
                )
            }
        }

        return entry.generate(location)
    }

    private fun bloodPurifierBlock(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Blood Purifier",
            ModBlocks.BLOOD_PURIFIER,
            "blood_purifier"
        ) {
            override fun generatePages() {
                spotlightPage(
                    ModBlocks.BLOOD_PURIFIER.toStack(),
                    "Blood Purifier",
                    "The ${major("Blood Purifier")} uses FE to decontaminate ${item("Syringes", "syringe")}."
                )

            }
        }

        return entry.generate(location)
    }

    private fun antiFieldBlock(location: Char): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Anti-Field Block",
            ModBlocks.ANTI_FIELD_BLOCK,
            "anti_field_block"
        ) {
            override fun generatePages() {
                textPage(
                    "Anti-Field Block",
                    paragraphs(
                        "The ${major("Anti-Field Block")} allows you to ${minor("temporarily disable certain Genes")}.",
                        "Specifically, it disabled the ${
                            gene("Item Attraction Field", "item_magnet")
                        } and ${gene("XP Attraction Field", "xp_magnet")} when enabled.",
                        "The ${
                            item("Anti-Field Orb", "anti_field_orb")
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
        }

        return entry.generate(location)
    }
}