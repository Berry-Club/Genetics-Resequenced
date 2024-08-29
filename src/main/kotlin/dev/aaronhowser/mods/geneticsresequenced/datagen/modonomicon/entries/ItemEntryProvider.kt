package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import net.minecraft.world.item.ItemStack

abstract class ItemEntryProvider(
    parent: CategoryProviderBase?,
    val stack: ItemStack,
    fileName: String
) : BaseEntryProvider(parent, fileName) {

    override fun entryIcon(): BookIconModel {
        return BookIconModel.create(stack)
    }

    override fun entryDescription(): String {
        return ""
    }
}