package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.EntryBackground
import com.klikli_dev.modonomicon.api.datagen.EntryProvider
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel
import com.mojang.datafixers.util.Pair
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.ChatFormatting
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

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

        fun entryLink(text: String, entryId: String): String {
            return "[$text](entry://$entryId)"
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

    fun spotlightPage(
        itemLike: ItemLike,
        text: String
    ) {
        this.spotlightPage(itemLike.itemStack, "", text)
    }

    fun spotlightPage(
        itemStack: ItemStack,
        text: String
    ) {
        this.spotlightPage(itemStack, "", text)
    }

    fun spotlightPage(
        itemStack: ItemStack,
        title: String,
        text: String,
    ) {

        val hasNonDefaultComponents = !itemStack.componentsPatch.isEmpty
        val ingredient = if (hasNonDefaultComponents) {
            DataComponentIngredient.of(false, itemStack)
        } else {
            Ingredient.of(itemStack)
        }

        this.page("page_${this.pageIndex++}") {
            BookSpotlightPageModel.create()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .withItem(ingredient)
        }

        this.pageTitle(title)
        this.pageText(text)
    }

}