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

class NegativeGenesCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    val realThis = this

    override fun categoryId(): String {
        return "negative_genes"
    }

    override fun categoryName(): String {
        return "Negative Genes"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.ANTI_PLASMID)
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf("")
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

        addEntry(badOmen())
        addEntry(blindness())
        addEntry(cringe())
        addEntry(cursed())
        addEntry(flambe())
        addEntry(hunger())
    }

    private fun badOmen(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.BAD_OMEN.get(),
            "Bad Omen",
            mcLoc("textures/mob_effect/bad_omen.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Bad Omen",
                    "The ${major("Bad Omen")} gives entities the ${bad("Bad Omen")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun blindness(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.BLINDNESS.get(),
            "Blindness",
            mcLoc("textures/mob_effect/blindness.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Blindness",
                    "The ${major("Blindness")} gives entities the ${bad("Blindness")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun cringe(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.CRINGE.get(),
            "Cringe",
            Items.PLAYER_HEAD
        ) {
            override fun firstPages() {
                textPage(
                    "Cringe",
                    paragraphs(
                        "The ${major("Cringe")} gives ${minor("uwufies your chat messages")}, and sets your language to LOLCAT.",
                        "That second feature can be disabled in the client config."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun cursed(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.CURSED.get(),
            "Cursed",
            mcLoc("textures/mob_effect/unluck.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Cursed",
                    "The ${major("Cursed")} gives entities the ${bad("Bad Luck")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun flambe(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.FLAMBE.get(),
            "Flambé",
            Items.BLAZE_POWDER
        ) {
            override fun firstPages() {
                textPage(
                    "Flambe",
                    "The ${major("Flambé")} gene ${bad("constantly lights entities on fire")}."
                )
            }
        }

        return entry.generate()
    }

    private fun hunger(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.HUNGER.get(),
            "Hunger",
            mcLoc("textures/mob_effect/hunger.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Hunger",
                    "The ${major("Hunger")} gives entities the ${bad("Hunger")} effect."
                )
            }
        }

        return entry.generate()
    }

}