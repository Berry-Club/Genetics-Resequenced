package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import com.mojang.datafixers.util.Either
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.GeneEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.PageWriter.Companion.textPage
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder

class GenesCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    val realThis = this

    override fun categoryId(): String {
        return "genes"
    }

    override fun categoryName(): String {
        return "Genes"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.PLASMID)
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf(
            "br",
        )
    }

    fun mcLoc(path: String): ResourceLocation {
        return ResourceLocation.withDefaultNamespace(path)
    }

    override fun generateEntries() {

        entry(
            ModGenes.BIOLUMINESCENCE,
            "Bioluminescence",
            Either.right(Items.GLOWSTONE),
        ) {
            val t = textPage {
                title("Bioluminescence")

                paragraph("Bioluminescence is the ability to produce light. This gene allows the organism to produce light.")
            }

        }

    }

    var index = 0

    fun entry(
        geneHolder: DeferredHolder<Gene, Gene>,
        entryName: String,
        icon: Either<ResourceLocation, ItemLike>,
        geneEntryProvider: (GeneEntryProvider) -> Unit
    ): BookEntryModel? {
        return entry(geneHolder.get(), entryName, icon, geneEntryProvider)
    }

    fun entry(
        gene: Gene,
        entryName: String,
        icon: Either<ResourceLocation, ItemLike>,
        geneEntryProvider: (GeneEntryProvider) -> Unit
    ): BookEntryModel? {
        val entry = object : GeneEntryProvider(
            realThis,
            gene,
            icon
        ) {
            override fun generatePages() {
                geneEntryProvider(this)
            }

            override fun entryName(): String = entryName
        }

        this.add(entry.generate()).withSortNumber(++index)

        return null
    }


}