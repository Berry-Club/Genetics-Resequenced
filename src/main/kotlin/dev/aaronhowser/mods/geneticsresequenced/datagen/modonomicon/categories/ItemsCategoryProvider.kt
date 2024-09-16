package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries.BaseEntryProvider
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanItemComponent
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.entity.EntityType

class ItemsCategoryProvider(
    parent: ModonomiconProviderBase?
) : CategoryProvider(parent) {

    val realThis = this

    override fun categoryId(): String {
        return "items"
    }

    override fun generateEntryMap(): Array<String> {
        return arrayOf(
            "smcdiy",
            "l   oh",
            "gupvz"
        )
    }

    override fun generateEntries() {
        var index = 0

        fun addEntry(entry: BookEntryModel) {
            this.add(entry.withSortNumber(index))
            index++
        }

        addEntry(scraper('z'))
        addEntry(organicMatter('m'))
        addEntry(cell('c'))
        addEntry(dnaHelix('d'))
        addEntry(plasmid('i'))
        addEntry(syringe('y'))
        addEntry(overclocker('l'))
        addEntry(antiFieldOrb('o'))
        addEntry(dragonHealthCrystal('h'))
        addEntry(potionCellGrowth('g'))
        addEntry(potionMutation('u'))
        addEntry(panacea('p'))
        addEntry(viralAgents('v'))
        addEntry(zombifyVillager('z'))
    }

    fun zombifyVillager(location: Char): BookEntryModel {
        val zombifyStack = OtherUtil.getPotionStack(ModPotions.ZOMBIFY_VILLAGER)

        val zombifyEntry = object : BaseEntryProvider(
            realThis,
            "Potion of Zombify Villager",
            zombifyStack,
            "zombify_villager"
        ) {
            override fun generatePages() {
                textPage(
                    "Potion of Zombify Villager",
                    "A ${major("Potion of Zombify Villager")} does right what it says on the tin: it turns Villagers into Zombie Villagers."
                )

                spotlightPage(
                    zombifyStack,
                    "Obviously, it's best utilized in Splash form."
                )
            }
        }

        return zombifyEntry.generate(location)
    }

    fun viralAgents(location: Char): BookEntryModel {
        val viralStack = BrewingRecipes.viralAgentsPotionStack

        val viralEntry = object : BaseEntryProvider(
            realThis,
            "Viral Agents",
            viralStack,
            "viral_agents"
        ) {
            override fun generatePages() {
                textPage(
                    "Viral Agents",
                    paragraphs(
                        "${major("Viral Agents")} are the final crafting potion of the mod. They are used to craft ${
                            categoryLink("negative Genes", "genes/negative")
                        }, which are generally just any gene that's harmful.",
                        "Negative Genes are always lost on death, regardless of config setting. Additionally, there's a config to prevent players from even being able to get negative Genes in the first place."
                    )
                )

                spotlightPage(
                    viralStack,
                    "Virus Cultivation recipes can be crafted in the Brewing Stand or in either Incubator."
                )
            }
        }

        return viralEntry.generate(location)
    }

    fun syringe(location: Char): BookEntryModel {
        val syringeEntry = object : BaseEntryProvider(
            realThis,
            "Syringe",
            ModItems.SYRINGE,
            "syringe"
        ) {
            override fun generatePages() {
                textPage(
                    "Syringe",
                    paragraphs(
                        "The ${major("Syringe")} is used to ${minor("extract and inject blood")}. Simply hold right-click, and the process will end automatically.",
                        "The reason you'd *want* to extract blood is that that's what carries completed ${
                            item("Plasmids", "plasmid")
                        } back into your body."
                    )
                )

                spotlightPage(
                    ModItems.SYRINGE.toStack(),
                    paragraphs(
                        "When you extract blood, it'll be ${bad("contaminated")}. You'll need to clean it in the ${
                            block("Blood Purifier", "blood_purifier")
                        } before it can be used.",
                        "Plasmids are added in the ${
                            block("Plasmid Injector", "plasmid_injector")
                        }."
                    )
                )

                spotlightPage(
                    ModItems.METAL_SYRINGE.toStack(),
                    paragraphs(
                        "You can also use the ${major("Metal Syringe")}, which targets others instead of yourself!",
                        "This is how you would give Genes to entities."
                    )
                )
            }
        }

        return syringeEntry.generate(location)
    }

    fun plasmid(location: Char): BookEntryModel {
        val plasmidEntry = object : BaseEntryProvider(
            realThis,
            "Plasmid",
            ModItems.PLASMID,
            "plasmid"
        ) {
            override fun generatePages() {
                textPage(
                    "Plasmid",
                    paragraphs(
                        "${major("Plasmids")} are the vehicle that carries ${categoryLink("Genes", "genes")}.",
                        "Plasmids default as ${bad("empty")}, with no genetic information.",
                        "To begin, use a ${
                            block("Plasmid Infuser", "plasmid_infuser")
                        } to infuse a decrypted DNA Helix into them.",
                        "This will set the Plasmid's Gene."
                    )
                )

                val plasmidEmpty = ModItems.PLASMID.toStack()
                spotlightPage(
                    plasmidEmpty,
                    paragraphs(
                        "Plasmids require a certain amount of ${minor("Gene Points")} to be complete.",
                        "The amount of Gene Points required depends on the Plasmid's Gene. More advanced Genes cost more Points."
                    )
                )

                val plasmidOne = ModItems.PLASMID.toStack()

                PlasmidItem.setGene(plasmidOne, ModGenes.SCARE_CREEPERS.get())
                PlasmidItem.setDnaPoints(plasmidOne, 1)

                spotlightPage(
                    plasmidOne,
                    paragraphs(
                        "This Plasmid has been infused with the ${
                            gene("Scare Creepers", "scare_creepers")
                        } Gene.",
                        "Notice, however, that it ${bad("only contains 1 Gene Point")}.",
                        "Infusing more DNA Helices of the same Gene will add +2 Points each, while Basic Genes will add +1 each."
                    )
                )

                val plasmidFull = ModItems.PLASMID.toStack()
                PlasmidItem.setGene(plasmidFull, ModGenes.SCARE_CREEPERS.get())
                PlasmidItem.setDnaPoints(plasmidFull, 99999999)
                spotlightPage(
                    plasmidFull,
                    paragraphs(
                        "Once it's reached its maximum Gene Points required, the Plasmid will be marked ${minor("complete")}!",
                        "That means it's ready to be injected into a Syringe at the ${
                            block("Plasmid Injector", "plasmid_injector")
                        }."
                    )
                )

            }
        }

        return plasmidEntry.generate(location)
    }

    fun panacea(location: Char): BookEntryModel {
        val panaceaStack = BrewingRecipes.panaceaPotionStack

        val panaceaEntry = object : BaseEntryProvider(
            realThis,
            "Panacea",
            panaceaStack,
            "panacea"
        ) {
            override fun generatePages() {
                textPage(
                    "Panacea",
                    "${major("Panacea")} is a simple potion: It cures all negative effects, and ${minor("removes all negative Genes")}."
                )

                spotlightPage(
                    panaceaStack,
                    "Sure, you could do the same with a Bucket of Milk and an Anti-Plasmid, but this is much more convenient, sometimes, maybe."
                )
            }
        }

        return panaceaEntry.generate(location)
    }

    fun overclocker(location: Char): BookEntryModel {
        val overclockerEntry = object : BaseEntryProvider(
            realThis,
            "Overclocker",
            ModItems.OVERCLOCKER,
            "overclocker"
        ) {
            override fun generatePages() {
                textPage(
                    "Overclocker",
                    paragraphs(
                        "The ${major("Overclocker")} can be inserted into most of the mod's machines to ${minor("massively increase their speed")}.",
                        "Each Overclocker doubles the speed, but increases the power draw by a quarter."
                    )
                )

                spotlightPage(
                    ModItems.OVERCLOCKER.toStack(),
                    "You can have ${minor("up to 8 Overclockers")} in a machine."
                )
            }
        }

        return overclockerEntry.generate(location)
    }

    fun potionMutation(location: Char): BookEntryModel {
        val mutationStack = BrewingRecipes.mutationPotionStack

        val mutationEntry = object : BaseEntryProvider(
            realThis,
            "Potion of Mutation",
            mutationStack,
            "potion_mutation"
        ) {
            override fun generatePages() {
                spotlightPage(
                    mutationStack,
                    "Potion of Mutation",
                    paragraphs(
                        "The ${major("Potion of Mutation")} is used to craft ${
                            item("Genetically Modified Cells", "potion_cell_growth@gmo_cell")
                        } that are set to ${minor("mutation Genes")}.",
                        "Mutation Genes are effectively just ${minor("more powerful")} Genes, and generally ${bad("require other Genes")}."
                    )
                )
            }
        }

        return mutationEntry.generate(location)
    }

    fun dragonHealthCrystal(location: Char): BookEntryModel {
        val dragonCrystalEntry = object : BaseEntryProvider(
            realThis,
            "Dragon Health Crystal",
            ModItems.DRAGON_HEALTH_CRYSTAL,
            "dragon_health_crystal"
        ) {
            override fun generatePages() {
                textPage(
                    "Dragon Health Crystal",
                    paragraphs(
                        "The ${major("Dragon Health Crystal")} is part of the ${
                            gene("Ender Dragon Health", "ender_dragon_health")
                        } Gene.",
                        "While you have that Gene, and while holding a Dragon Health Crystal, any incoming damage is negated and instead dealt to the Crystal."
                    )
                )

                spotlightPage(
                    ModItems.DRAGON_HEALTH_CRYSTAL.toStack(),
                    "A fresh Dragon health Crystal has ${minor("1,000 durability")}. Each half-heart deals 1 point of durability damage. It can be repaired with End Crystals."
                )
            }
        }

        return dragonCrystalEntry.generate(location)
    }

    fun dnaHelix(location: Char): BookEntryModel {
        val dnaHelixEntry = object : BaseEntryProvider(
            realThis,
            "DNA Helix",
            ModItems.DNA_HELIX,
            "dna_helix"
        ) {
            override fun generatePages() {
                textPage(
                    "DNA Helix",
                    paragraphs(
                        "${major("DNA Helices")} contain genetic information. This can be either in the form of ${
                            categoryLink("Genes", "genes")
                        } or an ${minor("entity type")}",
                        "Freshly crafted, a DNA Helix contains only the Entity that the DNA came from."
                    )
                )

                val cowHelix = ModItems.DNA_HELIX.toStack()
                EntityDnaItem.setEntityType(cowHelix, EntityType.COW)

                spotlightPage(
                    cowHelix,
                    "Encrypted Helix",
                    paragraphs(
                        "This DNA Helix contains the genetic information of a ${minor("Cow")}, but the actual Gene it has is ${
                            bad("unknown")
                        }.",
                        "To decrypt it, you'll need to pass it through a ${block("DNA Decryptor", "dna_decryptor")}."
                    )
                )

                val milkyHelix = ModItems.DNA_HELIX.toStack()
                DnaHelixItem.setGene(milkyHelix, ModGenes.MILKY.get())

                spotlightPage(
                    milkyHelix,
                    "Decrypted Helix",
                    paragraphs(
                        "This DNA Helix has been decrypted, and now we can see it contains the ${
                            gene("Milky", "milky")
                        } Gene."
                    )
                )

                val basicHelix = ModItems.DNA_HELIX.toStack()
                DnaHelixItem.setBasic(basicHelix)

                spotlightPage(
                    basicHelix,
                    "Basic Gene",
                    "DNA Helices have a chance of being ${minor("Basic")}. This means that they ${bad("don't contain any Gene")}, but they can still contribute to ${
                        item("Plasmids", "plasmid")
                    }."
                )

            }
        }

        return dnaHelixEntry.generate(location)
    }

    fun organicMatter(location: Char): BookEntryModel {
        val organicMatterEntry = object : BaseEntryProvider(
            realThis,
            "Organic Matter",
            ModItems.ORGANIC_MATTER,
            "organic_matter"
        ) {
            override fun generatePages() {
                textPage(
                    "Organic Matter",
                    paragraphs(
                        "Organic Matter is a recipe ingredient used in the creation of ${
                            item("Cells", "cell")
                        }.",
                        "This is done in the ${block("Cell Analyzer", "cell_analyzer")}."
                    )
                )

                val cowMatter = ModItems.ORGANIC_MATTER.toStack()
                EntityDnaItem.setEntityType(cowMatter, EntityType.COW)

                spotlightPage(
                    cowMatter,
                    paragraphs(
                        "Each entity has its own Organic Matter, which can be processed into a ${
                            item("Cell", "cell")
                        } of the entity's type.",
                        "You can see the Organic Matter's entity type in the item's tooltip."
                    )
                )
            }
        }

        return organicMatterEntry.generate(location)
    }

    fun scraper(location: Char): BookEntryModel {
        val scraperEntry = object : BaseEntryProvider(
            realThis,
            "Scraper",
            ModItems.SCRAPER,
            "scraper"
        ) {
            override fun generatePages() {
                textPage(
                    "Scraper",
                    paragraphs(
                        "The ${major("Scraper")} is used to get ${
                            item("Organic Matter", "organic_matter")
                        } from mobs.",
                        "To use it, simply right-click the mob. This damages the mob, which counts as an attack and will anger neutral entities."
                    )
                )

                spotlightPage(
                    ModItems.SCRAPER.toStack(),
                    "The Scraper can be enchanted with ${minor("Delicate Touch")} to prevent damaging and angering entities."
                )
            }
        }

        return scraperEntry.generate(location)
    }

    fun antiFieldOrb(location: Char): BookEntryModel {
        val antiFieldEntry = object : BaseEntryProvider(
            realThis,
            "Anti-Field Orb",
            ModItems.ANTI_FIELD_ORB,
            "anti_field_orb"
        ) {
            override fun generatePages() {
                textPage(
                    "Anti-Field Orb",
                    paragraphs(
                        "The ${major("Anti-Field Orb")} allows you to ${minor("temporarily disable certain Genes")}.",
                        "Specifically, it disables the ${
                            gene("Item Attraction Field", "item_magnet")
                        } and ${
                            gene("XP Attraction Field", "xp_magnet")
                        } when enabled.",
                    )
                )

                val activeOrb = ModItems.ANTI_FIELD_ORB.toStack()
                activeOrb.set(ModDataComponents.IS_ACTIVE_COMPONENT, BooleanItemComponent(true))

                spotlightPage(
                    activeOrb,
                    "The Anti-Field Orb can be toggled by right-clicking it while held."
                )

            }
        }

        return antiFieldEntry.generate(location)
    }

    fun cell(location: Char): BookEntryModel {
        val cellEntry = object : BaseEntryProvider(
            realThis,
            "Cell",
            ModItems.CELL,
            "cell"
        ) {
            override fun generatePages() {

                textPage(
                    "Cell",
                    paragraphs(
                        "${major("Cells")} are a recipe ingredient used in the creation of ${
                            item("DNA Helices", "dna_helix")
                        }.",
                        "This is done in the ${block("DNA Extractor", "dna_extractor")}."
                    )
                )

                val cowCell = ModItems.CELL.toStack()
                EntityDnaItem.setEntityType(cowCell, EntityType.COW)

                spotlightPage(
                    cowCell,
                    paragraphs(
                        "Each entity has its own Cell, which can be processed into a ${
                            item(
                                "DNA Helix",
                                "dna_helix"
                            )
                        } of the entity's type.",
                        "You can see the Cell's entity type in the item's tooltip."
                    )
                )
            }
        }

        return cellEntry.generate(location)
    }

    fun potionCellGrowth(location: Char): BookEntryModel {
        val pcgStack = BrewingRecipes.cellGrowthPotionStack

        val pcgEntry = object : BaseEntryProvider(
            realThis,
            "Potion of Cell Growth",
            pcgStack,
            "potion_cell_growth"
        ) {

            override fun generatePages() {
                spotlightPage(
                    pcgStack,
                    "Potion of Cell Growth",
                    paragraphs(
                        "${major("Potions of Cell Growth")}, like Organic Substrate, has no effect when imbibed. It's more of a crafting ingredient than a potion.",
                        "These recipes allow you to ${minor("improve your odds of getting certain rare Genes")}."
                    )
                )

                EntityDnaItem.setEntityType(pcgStack, EntityType.BLAZE)

                spotlightPage(
                    pcgStack,
                    "Entity Set",
                    "A ${
                        item("Cell", "cell")
                    } can be brewed into the Potion of Cell Growth to ${minor("set the Potion to the Cell's entity type")}."
                )

                val gmoStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(gmoStack, EntityType.BLAZE, ModGenes.BIOLUMINESCENCE.get())

                spotlightPage(
                    gmoStack,
                    "Genetically Modified Cell",
                    "From there, certain entity types have recipes to make ${
                        major("Genetically Modified Cells")
                    }, which are guaranteed to give a specific Gene.",
                ).withAnchor("gmo_cell")

                textPage(
                    paragraphs(
                        "A low-temperature Advanced Incubator takes a very long time, but can be accelerated using Overclockers.",
                        "Unfortunately, Overclockers lower your chances of success. Chorus Fruits increase it back, though!",
                    )
                )

            }
        }

        return pcgEntry.generate(location)
    }

    override fun categoryName(): String {
        return "Items"
    }

    override fun categoryIcon(): BookIconModel {
        return BookIconModel.create(ModItems.SYRINGE)
    }
}