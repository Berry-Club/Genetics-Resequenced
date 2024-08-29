package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.EntryBackground
import com.klikli_dev.modonomicon.api.datagen.EntryProvider
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel
import com.mojang.datafixers.util.Pair
import net.minecraft.ChatFormatting

abstract class BaseEntryProvider(
    parent: CategoryProviderBase?,
    val entryId: String
) : EntryProvider(parent) {

    override fun entryId(): String {
        return this.entryId
    }

    override fun entryBackground(): Pair<Int, Int> {
        return EntryBackground.DEFAULT
    }

    companion object {
        fun coloredText(color: ChatFormatting, text: String): String {
            val colorHexInt = color.color ?: error("Invalid color $color")
            val colorString = String.format("%06X", 0xFFFFFF and colorHexInt)

            return "[#](${colorString})$text[#]()"
        }

        fun major(text: String): String = coloredText(ChatFormatting.DARK_PURPLE, text)
        fun minor(text: String): String = coloredText(ChatFormatting.DARK_AQUA, text)
        fun bad(text: String): String = coloredText(ChatFormatting.RED, text)

        fun paragraphs(vararg paragraphs: String): String {
            return paragraphs.joinToString(separator = " \\\n  \\\n")
        }
    }

    private var pageIndex = 0

    fun textPage(
        text: String
    ) {
        textPage(
            "page_${this.pageIndex++}",
            "",
            text
        )
    }

    fun textPage(
        title: String,
        text: String
    ) {
        textPage(
            "page_${this.pageIndex++}",
            title,
            text
        )
    }

    fun textPage(
        pageId: String,
        title: String,
        text: String
    ) {
        this.page(pageId) {
            BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        }

        this.pageTitle(title)

        this.pageText(text)
    }

}