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
//        this.add(bloodPurifierBlock('b'))
//        this.add(cellAnalyzer('a'))
//        this.add(coalGenerator('g'))
//        this.add(dnaExtractor('e'))
//        this.add(incubator('i'))
//        this.add(advancedIncubator('v'))
//        this.add(plasmidInfuser('p'))
//        this.add(plasmidInjector('j'))
    }

    private fun antiFieldBlock(location: Char): BookEntryModel? {
        val entry = object : BaseEntryProvider(
            realThis,
            ModBlocks.ANTI_FIELD_BLOCK,
            "anti_field_block"
        ) {
            override fun generatePages() {

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