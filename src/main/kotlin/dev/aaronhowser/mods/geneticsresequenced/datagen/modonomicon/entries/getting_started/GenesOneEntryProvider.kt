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

    override fun customPages() {
        this.pageTitle("What are Genes?")

        this.pageText(
            """
            ${major("Genes")} cam ne taken from mobs to ${minor("harness their abilities")}.
            
            For example, Sheep have the ${minor("Wooly")} Gene. If you inject this into yourself, you will be able to be sheared for wool!

            To see a full list of Genes, ${minor("see here")}.
        """.trimIndent()
        )
    }

    override fun entryName(): String {
        return "What are Genes?"
    }

    override fun entryDescription(): String {
        return "The first step in understanding the genetic code."
    }

    override fun entryBackground(): Pair<Int, Int> {
        return EntryBackground.DEFAULT
    }

    override fun entryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.DNA_HELIX)
    }
}