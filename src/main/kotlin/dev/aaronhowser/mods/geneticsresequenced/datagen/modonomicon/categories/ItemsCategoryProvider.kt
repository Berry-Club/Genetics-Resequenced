package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.ItemEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems

class ItemsCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

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
        val realThis = this

        val antiFieldEntry = object : ItemEntryProvider(realThis, ModItems.ANTI_FIELD_ORB.toStack()) {

            override fun generatePages() {

                textPage(
                    "Anti-Field Orb",
                    paragraphs(
                        "The ${major("Anti-Field Orb")} allows you to ${minor("temporarily disable certain Genes")}.",
                        "Specifically, it disables the ${
                            entryLink(
                                "Item Attraction Field",
                                "getting_started/getting_genes"
                            )
                        } and ${
                            entryLink(
                                "XP Attraction Field",
                                "getting_started/getting_genes"
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
                return "The Anti-Field Orb is a powerful tool that can be used to remove the effects of a Field Orb."
            }
        }

        this.add(antiFieldEntry.generate('1'))

    }

    override fun categoryName(): String {
        return "Items"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.SYRINGE)
    }
}