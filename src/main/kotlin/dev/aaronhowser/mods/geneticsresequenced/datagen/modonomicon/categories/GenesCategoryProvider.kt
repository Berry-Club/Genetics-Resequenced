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
        addEntry(flight())
        addEntry(haste())
        addEntry(infinity())
        addEntry(invisible())
        addEntry(itemAttractionField())
        addEntry(johnny())
        addEntry(jumpBoost())
        addEntry(keepInventory())
        addEntry(knockback())
        addEntry(layEgg())
        addEntry(luck())
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

    private fun flight(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.FLIGHT.get(),
            "Flight",
            Items.ELYTRA
        ) {
            override fun firstPages() {

                val teleport = gene("Teleport", "teleport")
                val jumpBoost = gene("Jump Boost", "jump_boost")
                val noFallDamage = gene("No Fall Damage", "no_fall_damage")

                textPage(
                    "Flight",
                    paragraphs(
                        "The ${major("Flight")} Gene allows players to ${minor("fly")} by double-tapping jump.",
                        "By default, this Gene requires the following other Genes:\n- $teleport\n- $jumpBoost\n- $noFallDamage"
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun haste(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.HASTE.get(),
            "Haste",
            mcLoc("textures/mob_effect/haste.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Haste",
                    paragraphs(
                        "The ${major("Haste")} Gene gives entities the ${minor("Haste")} effect.",
                        "Can be mutated into ${(major("Haste II"))}."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun infinity(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.INFINITY.get(),
            "Infinite",
            Items.BOW
        ) {
            override fun firstPages() {
                textPage(
                    "Infinity",
                    "The ${major("Infinity")} Gene allows players to ${minor("fire Arrows when none are in their inventory")}."
                )
            }
        }

        return entry.generate()
    }

    private fun invisible(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.INVISIBLE.get(),
            "Invisible",
            Items.POTION
        ) {
            override fun firstPages() {
                textPage(
                    "Invisible",
                    "The ${major("Invisible")} Gene gives entities the ${minor("Invisibility")} effect."
                )
            }
        }

        return entry.generate()
    }

    private fun itemAttractionField(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.ITEM_MAGNET.get(),
            "Item Attraction Field",
            Items.IRON_INGOT
        ) {
            override fun firstPages() {
                val orb = item("Anti-Field Orb", "anti_field_orb")
                val block = block("Anti-Field Block", "anti_field_block")

                textPage(
                    "Item Attraction Field",
                    paragraphs(
                        "The ${major("Item Attraction Field")} Gene causes players to ${minor("grab items from much larger distances")}.",
                        "This Gene is disabled when the player has an active $orb, is near an active $block, or when sneaking."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun johnny(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.JOHNNY.get(),
            "Johnny",
            Items.IRON_AXE
        ) {
            override fun firstPages() {
                textPage(
                    "Johnny",
                    "The ${major("Johnny")} Gene ${minor("increases your attack damage when using Axes")}."
                )
            }
        }

        return entry.generate()
    }

    private fun jumpBoost(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.JUMP_BOOST.get(),
            "Jump Boost",
            mcLoc("textures/mob_effect/jump_boost.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Jump Boost",
                    "The ${major("Jump Boost")} Gene gives entities the ${minor("Jump Boost")} effect.",
                )
            }
        }

        return entry.generate()
    }

    private fun keepInventory(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.KEEP_INVENTORY.get(),
            "Keep Inventory",
            Items.SKELETON_SKULL
        ) {
            override fun firstPages() {
                textPage(
                    "Keep Inventory",
                    "The ${major("Keep Inventory")} Gene causes players to ${minor("keep their inventory upon death")}."
                )
            }
        }

        return entry.generate()
    }

    private fun knockback(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.KNOCKBACK.get(),
            "Knockback",
            Items.PISTON
        ) {
            override fun firstPages() {
                textPage(
                    "Knockback",
                    "The ${major("Knockback")} Gene ${minor("increases your attack knockback")}."
                )
            }
        }

        return entry.generate()
    }

    private fun layEgg(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.LAY_EGG.get(),
            "Lay Egg",
            Items.EGG
        ) {
            override fun firstPages() {
                textPage(
                    "Lay Egg",
                    paragraphs(
                        "The ${major("Lay Egg")} Gene causes entities to ${minor("spawn an Egg")} every so often.",
                        "This time is configurable, but defaults to once every 5 minutes."
                    )
                )
            }
        }

        return entry.generate()
    }

    private fun luck(): BookEntryModel {
        val entry = object : GeneEntryProvider(
            realThis,
            ModGenes.LUCK.get(),
            "Luck",
            mcLoc("textures/mob_effect/luck.png")
        ) {
            override fun firstPages() {
                textPage(
                    "Luck",
                    "The ${major("Luck")} Gene gives entities the ${minor("Luck")} effect."
                )
            }
        }

        return entry.generate()
    }

}