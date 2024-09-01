package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon

import com.klikli_dev.modonomicon.api.datagen.EntryProvider

class PageWriter {

    companion object {
        fun textPage(
            entryProvider: EntryProvider,
            block: PageWriter.() -> Unit
        ) {
            val writer = PageWriter()
            writer.block()

            entryProvider.page("test")

            val bodyText = writer.paragraphs.joinToString(separator = " \\\n  \\\n")

        }
    }

    private var title = ""
    private val paragraphs = mutableListOf<String>()

    fun title(title: String) {
        this.title = title
    }

    fun paragraph(text: String) {
        this.paragraphs.add(text)
    }

}