package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.activeAntiFieldOrb
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.entityStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.geneStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.gmoCellStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.minor
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.pageLink
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.plasmidStack
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBook
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBookCategory
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items

class BlocksPatchouliCategory(
	private val registries: HolderLookup.Provider
) {

	private lateinit var category: PatchouliBookCategory

	fun generate(book: PatchouliBook) {
		category = book.category(
			saveName = "blocks",
			name = "Blocks",
			description = "All the blocks in the mod",
			icon = ModBlocks.CELL_ANALYZER.get()
		) {
			sortNumber = 1
		}

		BOOK_CATEGORY = category

		book.addEntries()
	}

	private fun PatchouliBook.addEntries() {
		entry(
			saveName = ANTI_FIELD_BLOCK.localSaveName,
			category = category,
			name = "Anti-Field Block",
			icon = ModBlocks.ANTI_FIELD_BLOCK.get()
		) {
			sortNumber = 10

			textPage(
				text = "The ${major("Anti-Field Block")} allows you to ${minor("temporarily disable certain Genes")}.\$(br2)Specifically, it disables the ${pageLink(GenesPatchouliCategory.ITEM_ATTRACTION_FIELD, "Item Attraction Field")} and ${pageLink(GenesPatchouliCategory.XP_ATTRACTION_FIELD, "XP Attraction Field")} when enabled.\$(br2)The ${pageLink(ItemsPatchouliCategory.ANTI_FIELD_ORB, "Anti-Field Orb")} functions similarly, but in item form."
			)

			spotlightPage(ModBlocks.ANTI_FIELD_BLOCK.get()) {
				linkRecipe = true
				text = "The Anti-Field Block is active by default, and can be disabled with a Redstone signal.\$(br2)While active, it disables all Fields within a 25 block spherical radius. This amount is configurable."
			}
		}

		entry(
			saveName = BLOOD_PURIFIER.localSaveName,
			category = category,
			name = "Blood Purifier",
			icon = ModBlocks.BLOOD_PURIFIER.get()
		) {
			sortNumber = 7

			spotlightPage(ModBlocks.BLOOD_PURIFIER.get()) {
				linkRecipe = true
				text = "The Blood Purifier uses FE to decontaminate ${pageLink(ItemsPatchouliCategory.SYRINGE, "Syringes")}."
			}
		}

		entry(
			saveName = CELL_ANALYZER.localSaveName,
			category = category,
			name = "Cell Analyzer",
			icon = ModBlocks.CELL_ANALYZER.get()
		) {
			sortNumber = 2

			textPage(
				text = "The ${major("Cell Analyzer")} uses FE to convert ${pageLink(ItemsPatchouliCategory.ORGANIC_MATTER, "Organic Matter")} into ${pageLink(ItemsPatchouliCategory.CELL, "Cells")}."
			)

			spotlightPage(ModBlocks.CELL_ANALYZER.get()) {
				linkRecipe = true
				text = ""
			}
		}

		entry(
			saveName = COAL_GENERATOR.localSaveName,
			category = category,
			name = "Coal Generator",
			icon = ModBlocks.COAL_GENERATOR.get()
		) {
			sortNumber = 1

			textPage(
				text = "The ${major("Coal Generator")} ${minor("burns furnace fuels to generate FE")}.\$(br2)Furnace fuels that burn longer produce more FE.\$(br2)The amount is configurable, but defaults to 6FE/t. With this, ${minor("1 Coal generates 9,600 FE")}."
			)

			spotlightPage(ModBlocks.COAL_GENERATOR.get()) {
				linkRecipe = true
				text = "If the Coal Generator fills up with fuel burning, it ${minor("pauses until it has room for more energy")}, preventing waste!"
			}
		}

		entry(
			saveName = DNA_DECRYPTOR.localSaveName,
			category = category,
			name = "DNA Decryptor",
			icon = ModBlocks.DNA_DECRYPTOR.get()
		) {
			sortNumber = 4

			textPage(
				text = "The ${major("DNA Decryptor")} uses FE to decrypt ${pageLink(ItemsPatchouliCategory.DNA_HELIX, "DNA Helices")}.\$(br2)Every time DNA is decrypted, it will choose a ${pageLink(GenesPatchouliCategory.CATEGORY, "Gene")}, based on the Entity the Helix came from.\$(br2)This Gene is weighted, some Genes have a higher chance than others. The Gene is chosen at the start of the process."
			)

			spotlightPage(ModBlocks.DNA_DECRYPTOR.get()) {
				linkRecipe = true
				text = "If the Decryptor isn't working and there's a DNA Helix in the output slot, try taking it out. The next Gene may be of a different type, which means it can't stack with the one in the output slot."
			}
		}

		entry(
			saveName = DNA_EXTRACTOR.localSaveName,
			category = category,
			name = "DNA Extractor",
			icon = ModBlocks.DNA_EXTRACTOR.get()
		) {
			sortNumber = 3

			textPage(
				text = "The ${major("DNA Extractor")} uses FE to convert ${pageLink(ItemsPatchouliCategory.CELL, "Cells")} into encrypted ${pageLink(ItemsPatchouliCategory.DNA_HELIX, "DNA Helices")}."
			)

			spotlightPage(ModBlocks.DNA_EXTRACTOR.get()) {
				linkRecipe = true
				text = ""
			}
		}

		entry(
			saveName = INCUBATOR_ADVANCED.localSaveName,
			category = category,
			name = "Advanced Incubator",
			icon = ModBlocks.ADVANCED_INCUBATOR.get()
		) {
			sortNumber = 9

			textPage(
				text = "The ${major("Advanced Incubator")} is an upgrade to the ${pageLink(BlocksPatchouliCategory.INCUBATOR, "Incubator")}.\$(br2)Like the Incubator, it functions as a Brewing Stand. However, the Advanced Incubator has a ${minor("temperature")} mechanic.\$(br2)The coil on the left of the GUI can be clicked to toggle between low and high temperatures."
			)

			spotlightPage(ModBlocks.ADVANCED_INCUBATOR.get()) {
				linkRecipe = true
				text = "By default, it's ${bad("120 times slower")} at low-temperature. This means a single brew takes a full 20 minutes, excluding Overclockers.\$(br2)The reason you'd want to use low temperature mode is that is the only way you can get \$(l:geneticsresequenced:items/cell_growth#gmo_cell)Genetically Modified Cells/\$."
			}

			spotlightPage(gmoCellStack(registries, ModGenes.REGENERATION, EntityType.IRON_GOLEM)) {
				text = "\$(l:geneticsresequenced:items/cell_growth#gmo_cell)Genetically Modified Cells/\$ are ${minor("guaranteed to have a specific Gene")}, if crafted correctly.\$(br2)Each GM Cell has its own recipe, with a ${bad("chance of failure")}.\$(br2)The recipe for this GM Cell, for example, has a 30% chance of success, by default."
			}

			textPage(
				text = "Each Overclocker ${bad("decreases the chance by 10%")}.\$(br2)However, you can insert ${minor("Chorus Fruit")} into the top right slot to increase your odds!\$(br2)By default, each Chorus Fruit increases the chance by 10%. The Advanced Incubator will up as many Chorus Fruit as it takes to reach 100%."
			)
		}

		entry(
			saveName = INCUBATOR.localSaveName,
			category = category,
			name = "Incubator",
			icon = ModBlocks.INCUBATOR.get()
		) {
			sortNumber = 8

			textPage(
				text = "The ${major("Incubator")} is effectively a ${minor("faster Brewing Stand")}. It runs at ${minor("twice the speed")}, but ${bad("costs FE")} instead of Blaze Powder."
			)

			spotlightPage(ModBlocks.INCUBATOR.get()) {
				linkRecipe = true
				text = "It speed can be increased with ${pageLink(ItemsPatchouliCategory.OVERCLOCKER, "Overclockers")}."
			}
		}

		entry(
			saveName = PLASMID_INFUSER.localSaveName,
			category = category,
			name = "Plasmid Infuser",
			icon = ModBlocks.PLASMID_INFUSER.get()
		) {
			sortNumber = 5

			textPage(
				text = "The ${major("Plasmid Infuser")} uses FE to infuse ${pageLink(ItemsPatchouliCategory.DNA_HELIX, "DNA Helices")} into a ${pageLink(ItemsPatchouliCategory.PLASMID, "Plasmid")}.\$(br2)Insert an empty Plasmid in the right slot, and a DNA Helix in the left slot.\$(br2)The DNA Helix will be used up, and set the Plasmid's Gene to match the Helix's."
			)

			spotlightPage(ModBlocks.PLASMID_INFUSER.get()) {
				linkRecipe = true
				text = "Each Gene requires a certain amount of ${minor("DNA Points")} for the Plasmid to be completed.\$(br)\$(li)Basic Genes are worth 1 point.\$(li)Genes of the Plasmid's type are worth 2 points.\$(br2)A Basic Gene cannot be the first one infused into a Plasmid."
			}
		}

		entry(
			saveName = PLASMID_INJECTOR.localSaveName,
			category = category,
			name = "Plasmid Injector",
			icon = ModBlocks.PLASMID_INJECTOR.get()
		) {
			sortNumber = 6

			textPage(
				text = "The ${major("Plasmid Injector")} uses FE to inject completed ${pageLink(ItemsPatchouliCategory.PLASMID, "Plasmids")} into a ${pageLink(ItemsPatchouliCategory.SYRINGE, "Syringe")}.\$(br2)You can inject as many Plasmids into a single Syringe as you want."
			)

			spotlightPage(ModBlocks.PLASMID_INJECTOR.get()) {
				linkRecipe = true
				text = "The Syringe must be full of uncontaminated blood.\$(br2)Decontaminate blood in the ${pageLink(BlocksPatchouliCategory.BLOOD_PURIFIER, "Blood Purifier")}."
			}
		}
	}

	companion object {

		lateinit var BOOK_CATEGORY: PatchouliBookCategory
			private set

		val CATEGORY = PatchouliBookReference("blocks")
		val ANTI_FIELD_BLOCK = PatchouliBookReference("blocks/anti_field_block")
		val BLOOD_PURIFIER = PatchouliBookReference("blocks/blood_purifier")
		val CELL_ANALYZER = PatchouliBookReference("blocks/cell_analyzer")
		val COAL_GENERATOR = PatchouliBookReference("blocks/coal_generator")
		val DNA_DECRYPTOR = PatchouliBookReference("blocks/dna_decryptor")
		val DNA_EXTRACTOR = PatchouliBookReference("blocks/dna_extractor")
		val INCUBATOR_ADVANCED = PatchouliBookReference("blocks/incubator_advanced")
		val INCUBATOR = PatchouliBookReference("blocks/incubator")
		val PLASMID_INFUSER = PatchouliBookReference("blocks/plasmid_infuser")
		val PLASMID_INJECTOR = PatchouliBookReference("blocks/plasmid_injector")
	}
}