package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.getting_started.GettingGenesEntry
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.getting_started.WhatAreGenesEntry
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems

class GettingStartedCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    override fun categoryId(): String {
        return "getting_started"
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf(
            "___",
            "___"
        )
    }

    override fun generateEntries() {
        this.add(WhatAreGenesEntry(this).generate('1'))
        this.add(GettingGenesEntry(this).generate('2'))
    }

    override fun categoryName(): String {
        return "Getting Started"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.DNA_HELIX)
    }
}