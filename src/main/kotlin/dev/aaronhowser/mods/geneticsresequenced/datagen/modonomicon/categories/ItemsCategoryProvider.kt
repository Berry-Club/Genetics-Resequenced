package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.ItemEntryProvider
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
        this.add(antiFieldOrb('1'))
        this.add(cell('2'))
        this.add(potionCellGrowth('3'))
    }

    fun antiFieldOrb(location: Char): BookEntryModel? {
        val antiFieldEntry = object : ItemEntryProvider(realThis, ModItems.ANTI_FIELD_ORB.toStack()) {
            override fun generatePages() {
                textPage(
                    "Anti-Field Orb",
                    paragraphs(
                        "The ${major("Anti-Field Orb")} allows you to ${minor("temporarily disable certain Genes")}.",
                        "Specifically, it disables the ${
                            entryLink(
                                "Item Attraction Field",
                                "genes/item_attraction_field"
                            )
                        } and ${
                            entryLink(
                                "XP Attraction Field",
                                "genes/xp_attraction_field"
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
        val cellEntry = object : ItemEntryProvider(realThis, ModItems.CELL.toStack()) {
            override fun generatePages() {

                textPage(
                    "Cell",
                    paragraphs(
                        "${major("Cells")} are a recipe ingredient used in the creation of ${
                            entryLink(
                                "DNA Helices",
                                "items/dna_helix"
                            )
                        }.",
                        "This is done in the ${entryLink("DNA Extractor", "blocks/dna_extractor")}."
                    )
                )

                val cowCell = ModItems.CELL.toStack()
                EntityDnaItem.setEntityType(cowCell, EntityType.COW)

                spotlightPage(
                    cowCell,
                    paragraphs(
                        "Each entity has its own Cell, which can be processed into a ${
                            entryLink(
                                "DNA Helix",
                                "items/dna_helix"
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

        val pcgEntry = object : ItemEntryProvider(realThis, pcgStack) {
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
                        Companion.entryLink(
                            "Cell",
                            "items/cell"
                        )
                    } can be brewed into the Potion of Cell Growth to ${minor("set the Potion to the Cell's entity type")}."
                )

                val gmoStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(gmoStack, EntityType.BLAZE, ModGenes.BIOLUMINESCENCE.get())

                spotlightPage(
                    gmoStack,
                    "From there, certain entity types have recipes to make ${
                        entryLink(
                            "Genetically Modified Cells",
                            "items/gmo_cell"
                        )
                    }, which are guaranteed to give a specific Gene.",
                )

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