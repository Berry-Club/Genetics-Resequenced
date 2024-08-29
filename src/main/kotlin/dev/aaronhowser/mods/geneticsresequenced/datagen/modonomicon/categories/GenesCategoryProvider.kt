package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.GeneEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks

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
        this.add(bioluminescence('b'))
        this.add(regeneration('r'))
    }

    private fun regeneration(location: Char): BookEntryModel? {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.REGENERATION.get(),
            mcLoc("textures/mob_effect/regeneration.png")
        ) {
            override fun generatePages() {
            }

            override fun entryName(): String {
                return "Regeneration"
            }
        }

        return entry.generate(location)
    }

    private fun bioluminescence(location: Char): BookEntryModel? {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.BIOLUMINESCENCE.get(),
            Blocks.GLOWSTONE
        ) {
            override fun generatePages() {
            }

            override fun entryName(): String {
                return "Bioluminescence"
            }
        }

        return entry.generate(location)
    }

}