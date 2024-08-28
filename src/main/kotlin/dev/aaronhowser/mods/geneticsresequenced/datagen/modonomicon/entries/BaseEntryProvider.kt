package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.EntryProvider
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel
import net.minecraft.ChatFormatting

abstract class BaseEntryProvider(
    parent: CategoryProviderBase?,
    val pageId: String
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
    }

    override fun generatePages() {
        this.page(this.pageId) {
            BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        }

        customPages()
    }

    abstract fun customPages()

    override fun entryId(): String {
        return this.pageId
    }

    fun paragraphs(vararg paragraphs: String): String {
        return paragraphs.joinToString(separator = "\n\n")
    }

}