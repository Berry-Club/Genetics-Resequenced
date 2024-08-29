package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.EntryProvider
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel
import net.minecraft.ChatFormatting

abstract class BaseEntryProvider(
    parent: CategoryProviderBase?,
    val entryId: String
) : EntryProvider(parent) {

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

    fun page(
        pageId: String,
        text: String
    ) {
        page(pageId, "", text)
    }

    fun page(
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

    override fun entryId(): String {
        return this.entryId
    }

}