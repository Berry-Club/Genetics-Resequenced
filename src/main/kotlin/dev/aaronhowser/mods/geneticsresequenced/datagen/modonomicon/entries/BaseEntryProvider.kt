package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.EntryBackground
import com.klikli_dev.modonomicon.api.datagen.EntryProvider
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.ChatFormatting
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

abstract class BaseEntryProvider : EntryProvider {

    constructor(
        parent: CategoryProviderBase?,
        icon: ResourceLocation,
        entryId: String
    ) : super(parent) {
        this.entryId = entryId
        this.icon = Either.left(icon)
    }

    constructor(
        parent: CategoryProviderBase?,
        icon: ItemStack,
        entryId: String
    ) : super(parent) {
        this.entryId = entryId
        this.icon = Either.right(icon)
    }

    constructor(
        parent: CategoryProviderBase?,
        icon: ItemLike,
        entryId: String
    ) : this(parent, icon.itemStack, entryId)

    val entryId: String
    val icon: Either<ResourceLocation, ItemStack>

    override fun entryId(): String {
        return this.entryId
    }

    override fun entryBackground(): Pair<Int, Int> {
        return EntryBackground.DEFAULT
    }

    override fun entryIcon(): BookIconModel {
        return if (this.icon.left().isPresent) {
            BookIconModel.create(this.icon.left().get())
        } else {
            BookIconModel.create(this.icon.right().get())
        }
    }

    override fun entryDescription(): String {
        return ""
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

    fun gene(text: String, entryId: String): String {
        return "[${text}](entry://genes/${entryId})"
    }

    fun block(text: String, entryId: String): String {
        return "[${text}](entry://blocks/${entryId})"
    }

    fun item(text: String, entryId: String): String {
        return "[${text}](entry://items/${entryId})"
    }

    private var pageIndex = 0

    fun textPage(
        text: String
    ): BookTextPageModel {
        return textPage(
            "page_${this.pageIndex++}",
            "",
            text
        )
    }

    fun textPage(
        title: String,
        text: String
    ): BookTextPageModel {
        return textPage(
            "page_${this.pageIndex++}",
            title,
            text
        )
    }

    fun textPage(
        pageId: String,
        title: String,
        text: String
    ): BookTextPageModel {
        val page = this.page(pageId) {
            BookTextPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
        }

        this.pageTitle(title)
        this.pageText(text)

        return page
    }

    fun spotlightPage(
        itemLike: ItemLike,
        text: String
    ): BookSpotlightPageModel {
        return this.spotlightPage(itemLike.itemStack, "", text)
    }

    fun spotlightPage(
        itemStack: ItemStack,
        text: String
    ): BookSpotlightPageModel {
        return this.spotlightPage(itemStack, "", text)
    }

    fun spotlightPage(
        itemStack: ItemStack,
        title: String,
        text: String,
    ): BookSpotlightPageModel {

        val page = this.page("page_${this.pageIndex++}") {
            BookSpotlightPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withItem(itemStack)
        }

        this.pageTitle(title)
        this.pageText(text)

        return page
    }

}