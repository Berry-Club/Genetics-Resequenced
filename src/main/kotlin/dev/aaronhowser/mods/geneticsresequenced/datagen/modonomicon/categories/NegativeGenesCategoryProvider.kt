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
        addEntry(infested())
        addEntry(levitation())
        addEntry(miningFatigue())
        addEntry(nausea())
        addEntry(oozing())
        addEntry(poison())
        addEntry(slowness())
        addEntry(weakness())
        addEntry(weaving())
        addEntry(windCharged())
        addEntry(wither())

        addEntry(grayDeath())
        addEntry(greenDeath())
        addEntry(unUndeath())
        addEntry(whiteDeath())
        addEntry(blackDeath())
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

    private fun infested(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.INFESTED.get(),
            "Infested",
            mcLoc("textures/mob_effect/infested.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Infested",
                    "The ${major("Infested")} gives entities the ${bad("Infested")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun levitation(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.LEVITATION.get(),
            "Levitation",
            mcLoc("textures/mob_effect/levitation.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Levitation",
                    "The ${major("Levitation")} gives entities the ${bad("Levitation")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun miningFatigue(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.MINING_FATIGUE.get(),
            "Mining Fatigue",
            mcLoc("textures/mob_effect/mining_fatigue.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Mining Fatigue",
                    "The ${major("Mining Fatigue")} gives entities the ${bad("Mining Fatigue")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun nausea(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.NAUSEA.get(),
            "Nausea",
            mcLoc("textures/mob_effect/nausea.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Nausea",
                    "The ${major("Nausea")} gives entities the ${bad("Nausea")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun oozing(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.OOZING.get(),
            "Oozing",
            mcLoc("textures/mob_effect/oozing.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Oozing",
                    "The ${major("Oozing")} gives entities the ${bad("Oozing")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun poison(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.POISON.get(),
            "Poison",
            mcLoc("textures/mob_effect/poison.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Poison",
                    "The ${major("Poison")} gives entities the ${bad("Poison")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun slowness(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.SLOWNESS.get(),
            "Slowness",
            mcLoc("textures/mob_effect/slowness.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Slowness",
                    "The ${major("Slowness")} gives entities the ${bad("Slowness")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun weakness(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.WEAKNESS.get(),
            "Weakness",
            mcLoc("textures/mob_effect/weakness.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Weakness",
                    "The ${major("Weakness")} gives entities the ${bad("Weakness")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun weaving(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.WEAVING.get(),
            "Weaving",
            mcLoc("textures/mob_effect/weaving.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Weaving",
                    "The ${major("Weaving")} gives entities the ${bad("Weaving")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun windCharged(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.WIND_CHARGED.get(),
            "Wind Charged",
            mcLoc("textures/mob_effect/wind_charged.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Wind Charged",
                    "The ${major("Wind Charged")} gives entities the ${bad("Wind Charged")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun wither(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.WITHER.get(),
            "Wither",
            mcLoc("textures/mob_effect/wither.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Wither",
                    "The ${major("Wither")} gives entities the ${bad("Wither")} effect."
                )
            }
        }

        return entry.generate()
    }


    private fun grayDeath(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.GRAY_DEATH.get(),
            "Gray Death",
            Items.POISONOUS_POTATO
        ) {
            override fun firstPages() {
                textPage(
                    "Gray Death",
                    "The ${major("Gray Death")} Plague Gene ${bad("kills any entity that can age")}",
                )
            }
        }

        return entry.generate()
    }

    private fun greenDeath(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.GREEN_DEATH.get(),
            "Green Death",
            Items.CREEPER_HEAD
        ) {
            override fun firstPages() {
                textPage(
                    "Green Death",
                    "The ${major("Green Death")} Plague Gene ${bad("kills Creepers")}.",
                )
            }
        }

        return entry.generate()
    }

    private fun unUndeath(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.UN_UNDEATH.get(),
            "Un-Undeath",
            Items.ZOMBIE_HEAD
        ) {
            override fun firstPages() {
                textPage(
                    "Un-Undeath",
                    "The ${major("Un-Undeath")} Plague Gene ${bad("kills the undead")}.",
                )
            }
        }

        return entry.generate()
    }

    private fun whiteDeath(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.WHITE_DEATH.get(),
            "White Death",
            Items.BONE
        ) {
            override fun firstPages() {
                textPage(
                    "White Death",
                    "The ${major("White Death")} Plague Gene ${bad("kills monsters")}.",
                )
            }
        }

        return entry.generate()
    }

    private fun blackDeath(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.BLACK_DEATH.get(),
            "Black Death",
            Items.WITHER_ROSE
        ) {
            override fun firstPages() {
                textPage(
                    "Black Death",
                    paragraphs(
                        "The ${major("Black Death")} Plague Gene ${bad("instantly kills whoever has it")}. Given its strength, this Gene is much harder to acquire than others.",
                        "To craft its DNA Helix, you'll need to get a ${minor("Syringe with **every other negative gene** in the game")}. This includes other Plagues, but not disabled Genes.",
                        "From there, simply brew that Syringe into a Potion of Viral Agents."
                    )
                )
            }
        }

        return entry.generate()
    }

}