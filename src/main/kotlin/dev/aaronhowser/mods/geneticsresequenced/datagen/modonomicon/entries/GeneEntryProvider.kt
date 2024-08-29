package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.EntryBackground
import com.klikli_dev.modonomicon.api.datagen.EntryProvider
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import com.mojang.datafixers.util.Pair
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

abstract class GeneEntryProvider : EntryProvider {

    constructor(
        parent: CategoryProviderBase?,
        gene: Gene,
        iconRl: ResourceLocation
    ) : super(parent) {
        this.icon = iconRl
        this.gene = gene
    }

    constructor(
        parent: CategoryProviderBase?,
        gene: Gene,
        icon: ItemLike
    ) : super(parent) {
        this.icon = icon
        this.gene = gene
    }

    val gene: Gene
    val icon: Any

    override fun entryDescription(): String {
        return ""
    }

    override fun entryBackground(): Pair<Int, Int> {
        return EntryBackground.DEFAULT
    }

    override fun entryIcon(): BookIconModel {
        if (icon is ResourceLocation) {
            return BookIconModel.create(icon)
        }

        if (icon is ItemLike) {
            return BookIconModel.create(icon)
        }

        error("Invalid icon type: $icon")

    }

    override fun entryId(): String {
        return gene.id.toString().replace(":", "/")
    }
}