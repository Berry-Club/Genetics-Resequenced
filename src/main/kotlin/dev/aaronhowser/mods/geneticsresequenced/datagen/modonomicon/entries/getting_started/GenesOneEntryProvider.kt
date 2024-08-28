package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.getting_started

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.EntryBackground
import com.klikli_dev.modonomicon.api.datagen.EntryProvider
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel
import com.mojang.datafixers.util.Pair
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems

class GenesOneEntryProvider(parent: CategoryProviderBase?) : EntryProvider(parent) {

    override fun generatePages() {
        this.page("Genes 1") {
            BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        }

        this.pageTitle("Genes 1")

        this.pageText("The first step in understanding the genetic code.")

    }

    override fun entryName(): String {
        return "Genes 1"
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

    override fun entryId(): String {
        return "genes_one"
    }
}