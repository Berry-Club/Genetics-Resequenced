package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

abstract class GeneEntryProvider : BaseEntryProvider {

    constructor(
        parent: CategoryProviderBase?,
        gene: Gene,
        iconRl: ResourceLocation
    ) : super(
        parent = parent,
        icon = iconRl,
        entryId = gene.id.toString().replace(":", "/")
    )

    constructor(
        parent: CategoryProviderBase?,
        gene: Gene,
        icon: ItemLike
    ) : super(
        parent = parent,
        icon = icon,
        entryId = gene.id.toString().replace(":", "/")
    )
}