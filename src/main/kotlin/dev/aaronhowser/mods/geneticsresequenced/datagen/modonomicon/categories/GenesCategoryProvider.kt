package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.GeneEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
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
        var index = 0

        fun addEntry(entry: BookEntryModel) {
            this.add(entry.withSortNumber(index))
            index++
        }

        addEntry(bioluminescence())
        addEntry(chatterbox())
        addEntry(chilling())
        addEntry(claws())
    }

    private fun bioluminescence(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.BIOLUMINESCENCE.get(),
            "Bioluminescence",
            Blocks.GLOWSTONE
        ) {
            override fun firstPages() {
                textPage(
                    "Bioluminescence",
                    "The ${major("Bioluminescence")} Gene causes entities to ${minor("spawn light sources")} when in the dark."
                )
            }
        }

        return entry.generate()
    }

    private fun chatterbox(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.CHATTERBOX.get(),
            "Chatterbox",
            Blocks.NOTE_BLOCK
        ) {
            override fun firstPages() {
                textPage(
                    "Chatterbox",
                    paragraphs(
                        "The ${major("Chatterbox")} Gene causes your chat messages to be ${minor("read by the game's narrator")}.",
                        "This only applies to players within 64 blocks of you, and can be disabled in the client config."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun chilling(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.CHILLING.get(),
            "Chilling",
            Items.POWDER_SNOW_BUCKET
        ) {
            override fun firstPages() {
                textPage(
                    "Chilling",
                    "The ${major("Chilling")} Gene makes your melee attacks have a chance of ${minor("inflicting a buildup of Freezing")}."
                )
            }
        }

        return entry.generate()
    }

    private fun claws(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.CLAWS.get(),
            "Claws",
            Items.STONE_SWORD
        ) {
            override fun firstPages() {
                textPage(
                    "Claws",
                    paragraphs(
                        "The ${major("Claws")} Gene gives entities a chance to ${minor("inflict Bleeding")} on empty-handed melee attacks.",
                        "When mutated into Claws II, the chance is doubled."
                    )
                    //TODO: Add a link to the mutation page?
                )
            }
        }

        return entry.generate()
    }

}