package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

abstract class GeneEntryProvider : BaseEntryProvider {

    constructor(
        parent: CategoryProviderBase?,
        geneRk: ResourceKey<Gene>,
        name: String,
        iconRl: ResourceLocation
    ) : super(
        parent = parent,
        name = name,
        icon = iconRl,
        entryId = geneRk.location().toString().replace(":", "/")
    ) {
        this.geneHolder = geneRk.getHolderOrThrow(registries())

        this.plasmidStack = ModItems.PLASMID.toStack()
        PlasmidItem.setGene(this.plasmidStack, this.geneHolder)

    }

    constructor(
        parent: CategoryProviderBase?,
        geneRk: ResourceKey<Gene>,
        name: String,
        icon: ItemLike
    ) : super(
        parent = parent,
        name = name,
        icon = icon,
        entryId = geneRk.location().toString().replace(":", "/")
    ) {
        this.geneHolder = geneRk.getHolderOrThrow(registries())

        this.plasmidStack = ModItems.PLASMID.toStack()
        PlasmidItem.setGene(this.plasmidStack, this.geneHolder)
    }

    val geneHolder: Holder<Gene>
    val plasmidStack: ItemStack

    abstract fun firstPages()
    final override fun generatePages() {
        firstPages()

        val canMobsHaveText = if (geneHolder.value().allowedEntities.any { it.value() !== EntityType.PLAYER }) {
            "This Gene ${minor("can be given to mobs")}."
        } else {
            "This Gene ${bad("cannot be given to mobs")}."
        }

        val plasmid = ModItems.PLASMID.toStack()
        PlasmidItem.setGene(plasmid, geneHolder)
        PlasmidItem.setDnaPoints(plasmid, geneHolder.value().dnaPointsRequired)

        spotlightPage(
            plasmid,
            canMobsHaveText,
        )

    }

}