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
        addEntry(climbWalls())
        addEntry(dragonsBreath())
        addEntry(eatGrass())
        addEntry(efficiency())
        addEntry(emeraldHeart())
        addEntry(enderDragonHealth())
        addEntry(explosiveExit())
        addEntry(fireProof())
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

    private fun climbWalls(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.WALL_CLIMBING.get(),
            "Climb Walls",
            Items.STRING
        ) {
            override fun firstPages() {
                textPage(
                    "Climb Walls",
                    paragraphs(
                        "The ${major("Climb Walls")} Gene allows players to ${minor("climb up walls")}.",
                        "You can hold sneak to stop climbing."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun dragonsBreath(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.DRAGON_BREATH.get(),
            "Dragon's Breath",
            Items.DRAGON_BREATH
        ) {
            override fun firstPages() {
                textPage(
                    "Dragon's Breath",
                    paragraphs(
                        "The ${major("Dragon's Breath")} Gene allows players to ${minor("shoot a dragon fireball")} when the \"Dragon's Breath\" keybind is pressed.",
                        "This has a configurable cooldown, with the default being 15 seconds."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun eatGrass(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.EAT_GRASS.get(),
            "Eat Grass",
            Items.GRASS_BLOCK
        ) {
            override fun firstPages() {
                textPage(
                    "Eat Grass",
                    "The ${major("Eat Grass")} Gene allows players to ${minor("right-click Grass Blocks to regain hunger")}."
                )
            }
        }

        return entry.generate()
    }

    private fun efficiency(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.EFFICIENCY.get(),
            "Efficiency",
            Items.DIAMOND_PICKAXE
        ) {
            override fun firstPages() {
                textPage(
                    "Efficiency",
                    paragraphs(
                        "The ${major("Efficiency")} Gene ${minor("makes players mine faster")}. This stacks with enchantments and the Haste effect.",
                        "Can be mutated into ${major("Efficiency IV")}."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun emeraldHeart(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.EMERALD_HEART.get(),
            "Emerald Heart",
            Items.EMERALD
        ) {
            override fun firstPages() {
                textPage(
                    "Emerald Heart",
                    paragraphs(
                        "The ${major("Emerald Heart")} Gene causes entities to ${minor("drop an Emerald upon death")}.",
                        "For players, this has a configurable cooldown, with the default being 60 seconds."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun enderDragonHealth(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.ENDER_DRAGON_HEALTH.get(),
            "Ender Dragon Health",
            ModItems.DRAGON_HEALTH_CRYSTAL
        ) {
            override fun firstPages() {
                textPage(
                    "Ender Dragon Health",
                    paragraphs(
                        "The ${major("Ender Dragon Health")} Gene allows players to ${minor("completely negate damage")} while a ${
                            item(
                                "Dragon Health Crystal",
                                "dragon_health_crystal"
                            )
                        } is in their inventory.",
                        "Any damage they would take is instead dealt to the Crystal's durability."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun explosiveExit(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.EXPLOSIVE_EXIT.get(),
            "Explosive Exit",
            Items.TNT
        ) {
            override fun firstPages() {
                textPage(
                    "Explosive Exit",
                    paragraphs(
                        "The ${major("Explosive Exit")} Gene causes entities to ${minor("explode upon death")}.",
                        "If a player has this Gene, they only explode if they have 5 Gunpowder in their inventory, which is removed upon detonation."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun fireProof(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.FIRE_PROOF.get(),
            "Fire Proof",
            Items.FLINT_AND_STEEL
        ) {
            override fun firstPages() {
                textPage(
                    "Fire Proof",
                    "The ${major("Fire Proof")} Gene makes entities ${minor(" completely immune to fire damage")}.",
                )
            }
        }

        return entry.generate()
    }

}