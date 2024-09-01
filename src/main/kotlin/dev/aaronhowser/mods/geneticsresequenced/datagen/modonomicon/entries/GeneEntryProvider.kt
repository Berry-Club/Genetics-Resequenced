package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

abstract class GeneEntryProvider : BaseEntryProvider {

    constructor(
        parent: CategoryProviderBase?,
        gene: Gene,
        name: String,
        iconRl: ResourceLocation
    ) : super(
        parent = parent,
        name = name,
        icon = iconRl,
        entryId = gene.id.toString().replace(":", "/")
    ) {
        this.gene = gene

        this.plasmidStack = ModItems.PLASMID.toStack()
        PlasmidItem.setGene(this.plasmidStack, gene)
        PlasmidItem.setDnaPoints(this.plasmidStack, gene.dnaPointsRequired)

    }

    constructor(
        parent: CategoryProviderBase?,
        gene: Gene,
        name: String,
        icon: ItemLike
    ) : super(
        parent = parent,
        name = name,
        icon = icon,
        entryId = gene.id.toString().replace(":", "/")
    ) {
        this.gene = gene

        this.plasmidStack = ModItems.PLASMID.toStack()
        PlasmidItem.setGene(this.plasmidStack, gene)
        PlasmidItem.setDnaPoints(this.plasmidStack, gene.dnaPointsRequired)
    }

    val gene: Gene
    val plasmidStack: ItemStack

    abstract fun firstPages()
    final override fun generatePages() {
        firstPages()

        val canMobsHaveText = if (gene.canMobsHave) {
            "This Gene ${minor("can be given to mobs")}."
        } else {
            "This Gene ${bad("cannot be given to mobs")}."
        }

        val plasmid = ModItems.PLASMID.toStack()
        PlasmidItem.setGene(plasmid, gene)
        PlasmidItem.setDnaPoints(plasmid, gene.dnaPointsRequired)

        spotlightPage(
            plasmid,
            canMobsHaveText,
        )

    }

}