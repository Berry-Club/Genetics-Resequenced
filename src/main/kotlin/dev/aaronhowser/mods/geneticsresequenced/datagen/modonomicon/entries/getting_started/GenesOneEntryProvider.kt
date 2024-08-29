package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.getting_started

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.EntryBackground
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import com.mojang.datafixers.util.Pair
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.BaseEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems

class GenesOneEntryProvider(
    parent: CategoryProviderBase?
) : BaseEntryProvider(parent, "genes_one") {

    override fun generatePages() {

        page(
            "page_1",
            "What are Genes?",
            paragraphs(
                "${major("Genes")} can be taken from mobs to ${minor("harness their abilities")}.",
                "For example, Sheep have the ${minor("Wooly")} Gene. If you inject this into yourself, you will be able to be sheared for wool!",
                "To see a full list of Genes, ${minor("see here")}."
            )
        )

        page(
            "page_2",
            paragraphs(
                "Each Gene page in the book will say what the Gene does, and how to obtain it.",
                "Some Genes can even be given to mobs!"
            )
        )

    }

    override fun entryName(): String {
        return "What are Genes?"
    }

    override fun entryDescription(): String {
        return ""
    }

    override fun entryBackground(): Pair<Int, Int> {
        return EntryBackground.DEFAULT
    }

    override fun entryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.DNA_HELIX)
    }
}