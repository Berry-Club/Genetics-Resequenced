package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.ItemEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.entity.EntityType

class ItemsCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    val realThis = this

    override fun categoryId(): String {
        return "items"
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf(
            "___",
            "___"
        )
    }

    override fun generateEntries() {
        this.add(antiFieldOrb('o'))
        this.add(cell('c'))
        this.add(potionCellGrowth('g'))
        this.add(dnaHelix('d'))
        this.add(scraper('s'))
        this.add(organicMatter('m'))
    }

    fun dnaHelix(location: Char): BookEntryModel? {
        val dnaHelixEntry = object : ItemEntryProvider(
            realThis,
            ModItems.DNA_HELIX.toStack(),
            "dna_helix"
        ) {
            override fun generatePages() {
                textPage(
                    "DNA Helix",
                    "${major("DNA Helices")} contain genetic information. This can be either in the form of ${
                        categoryLinkDummy(
                            "Genes",
                            "genes"
                        )
                    } or an ${minor("entity type")}",
                    "Freshly crafted, a DNA Helix contains only the Entity that the DNA came from."
                )

                val cowHelix = ModItems.DNA_HELIX.toStack()
                EntityDnaItem.setEntityType(cowHelix, EntityType.COW)

                spotlightPage(
                    cowHelix,
                    paragraphs(
                        "This DNA Helix contains the genetic information of a ${minor("Cow")}, but the actual Gene it has is ${
                            bad(
                                "unknown"
                            )
                        }.",
                        "To decrypt it, you'll need to pass it through a ${
                            blockEntryLink(
                                "DNA Decryptor",
                                "blocks/dna_decryptor"
                            )
                        }."
                    )
                )

                val milkyHelix = ModItems.DNA_HELIX.toStack()
                DnaHelixItem.setGene(milkyHelix, ModGenes.MILKY.get())

                spotlightPage(
                    milkyHelix,
                    paragraphs(
                        "This DNA Helix has been decrypted, and now we can see it contains the ${
                            geneEntryLink(
                                "Milky",
                                "milky"
                            )
                        } Gene."
                    )
                )

                val basicHelix = ModItems.DNA_HELIX.toStack()
                DnaHelixItem.setBasic(basicHelix)

                spotlightPage(
                    basicHelix,
                    "DNA Helices have a chance of being ${minor("Basic")}/ This means that they ${bad("don't contain any Gene")}, but they can still contribute to ${
                        itemEntryLink(
                            "Plasmids",
                            "plasmid"
                        )
                    }."
                )

            }

            override fun entryName(): String {
                return "DNA Helix"
            }

        }

        return dnaHelixEntry.generate(location)
    }

    fun organicMatter(location: Char): BookEntryModel? {
        val organicMatterEntry = object : ItemEntryProvider(
            realThis,
            ModItems.ORGANIC_MATTER.toStack(),
            "organic_matter"
        ) {
            override fun generatePages() {
                textPage(
                    "Organic Matter",
                    paragraphs(
                        "Organic Matter is a recipe ingredient used in the creation of ${
                            itemEntryLink(
                                "Cells",
                                "cell"
                            )
                        }.",
                        "This is done in the ${blockEntryLink("Cell Analyzer", "blocks/cell_analyzer")}."
                    )
                )

                val cowMatter = ModItems.ORGANIC_MATTER.toStack()
                EntityDnaItem.setEntityType(cowMatter, EntityType.COW)

                spotlightPage(
                    cowMatter,
                    paragraphs(
                        "Each entity has its own Organic Matter, which can be processed into a ${
                            itemEntryLink(
                                "Cell",
                                "cell"
                            )
                        } of the entity's type.",
                        "You can see the Organic Matter's entity type in the item's tooltip."
                    )
                )
            }

            override fun entryName(): String {
                return "Organic Matter"
            }
        }

        return organicMatterEntry.generate(location)
    }

    fun scraper(location: Char): BookEntryModel? {
        val scraperEntry = object : ItemEntryProvider(
            realThis,
            ModItems.SCRAPER.toStack(),
            "scraper"
        ) {
            override fun generatePages() {
                textPage(
                    "Scraper",
                    paragraphs(
                        "The ${major("Scraper")} is used to get ${
                            itemEntryLink(
                                "Organic Matter",
                                "organic_matter"
                            )
                        } from mobs.",
                        "To use it, simply right-click the mob. This damages the mob, which counts as an attack and will anger neutral entities."
                    )
                )

                spotlightPage(
                    ModItems.SCRAPER.toStack(),
                    "The Scraper can be enchanted with ${minor("Delicate Touch")} to prevent damaging and angering entities."
                )
            }

            override fun entryName(): String {
                return "Scraper"
            }
        }

        return scraperEntry.generate(location)
    }

    fun antiFieldOrb(location: Char): BookEntryModel? {
        val antiFieldEntry = object : ItemEntryProvider(
            realThis,
            ModItems.ANTI_FIELD_ORB.toStack(),
            "anti_field_orb"
        ) {
            override fun generatePages() {
                textPage(
                    "Anti-Field Orb",
                    paragraphs(
                        "The ${major("Anti-Field Orb")} allows you to ${minor("temporarily disable certain Genes")}.",
                        "Specifically, it disables the ${
                            geneEntryLink(
                                "Item Attraction Field",
                                "item_attraction_field"
                            )
                        } and ${
                            geneEntryLink(
                                "XP Attraction Field",
                                "xp_attraction_field"
                            )
                        } when enabled.",
                    )
                )

                val activeOrb = ModItems.ANTI_FIELD_ORB.toStack()
                activeOrb.set(ModDataComponents.IS_ACTIVE_COMPONENT, BooleanItemComponent(true))

                spotlightPage(
                    activeOrb,
                    "The Anti-Field Orb can be toggled by right-clicking it while held."
                )

            }

            override fun entryName(): String {
                return "Anti-Field Orb"
            }

            override fun entryDescription(): String {
                return ""
            }
        }

        return antiFieldEntry.generate(location)
    }

    fun cell(location: Char): BookEntryModel? {
        val cellEntry = object : ItemEntryProvider(
            realThis,
            ModItems.CELL.toStack(),
            "cell"
        ) {
            override fun generatePages() {

                textPage(
                    "Cell",
                    paragraphs(
                        "${major("Cells")} are a recipe ingredient used in the creation of ${
                            itemEntryLink(
                                "DNA Helices",
                                "dna_helix"
                            )
                        }.",
                        "This is done in the ${blockEntryLink("DNA Extractor", "dna_extractor")}."
                    )
                )

                val cowCell = ModItems.CELL.toStack()
                EntityDnaItem.setEntityType(cowCell, EntityType.COW)

                spotlightPage(
                    cowCell,
                    paragraphs(
                        "Each entity has its own Cell, which can be processed into a ${
                            itemEntryLink(
                                "DNA Helix",
                                "dna_helix"
                            )
                        } of the entity's type.",
                        "You can see the Cell's entity type in the item's tooltip."
                    )
                )

            }

            override fun entryName(): String {
                return "Cell"
            }

        }

        return cellEntry.generate(location)
    }

    fun potionCellGrowth(location: Char): BookEntryModel? {
        val pcgStack = OtherUtil.getPotionStack(ModPotions.CELL_GROWTH)

        val pcgEntry = object : ItemEntryProvider(
            realThis,
            pcgStack,
            "potion_cell_growth"
        ) {

            override fun generatePages() {
                spotlightPage(
                    pcgStack,
                    paragraphs(
                        "${major("Potions of Cell Growth")}, like Organic Substrate, has no effect when imbibed. It's more of a crafting ingredient than a potion.",
                        "These recipes allow you to ${minor("improve your odds of getting certain rare Genes")}."
                    )
                )

                EntityDnaItem.setEntityType(pcgStack, EntityType.BLAZE)

                spotlightPage(
                    pcgStack,
                    "A ${
                        itemEntryLink(
                            "Cell",
                            "cell"
                        )
                    } can be brewed into the Potion of Cell Growth to ${minor("set the Potion to the Cell's entity type")}."
                )

                val gmoStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(gmoStack, EntityType.BLAZE, ModGenes.BIOLUMINESCENCE.get())

                spotlightPage(
                    gmoStack,
                    "From there, certain entity types have recipes to make ${
                        itemEntryLink(
                            "Genetically Modified Cells",
                            "gmo_cell"
                        )
                    }, which are guaranteed to give a specific Gene.",
                ).withAnchor("gmo_cell")

                textPage(
                    paragraphs(
                        "A low-temperature Advanced Incubator takes a very long time, but can be accelerated using Overclockers.",
                        "Unfortunately, Overclockers lower your chances of success. Chorus Fruits increase it back, though!",
                    )
                )

            }

            override fun entryName(): String {
                return "Potion of Cell Growth"
            }
        }

        return pcgEntry.generate(location)
    }

    override fun categoryName(): String {
        return "Items"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.SYRINGE)
    }
}