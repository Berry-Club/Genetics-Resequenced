package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.gmoCellStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.minor
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBook
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookCategory
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookEntry
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.doubleSpacedLines
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.internalLink
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.lines
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.list
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType

object BlocksModonomiconCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: ModonomiconBookCategory
	lateinit var bloodPurifier: ModonomiconBookEntry
	lateinit var plasmidInfuser: ModonomiconBookEntry
	lateinit var incubatorAdvanced: ModonomiconBookEntry
	lateinit var antiFieldBlock: ModonomiconBookEntry
	lateinit var dnaExtractor: ModonomiconBookEntry
	lateinit var incubator: ModonomiconBookEntry
	lateinit var cellAnalyzer: ModonomiconBookEntry
	lateinit var dnaDecryptor: ModonomiconBookEntry
	lateinit var plasmidInjector: ModonomiconBookEntry

	fun generate(book: ModonomiconBook, registries: HolderLookup.Provider) {
		this.registries = registries
		bookCategory = book.category(
			saveName = "blocks",
			name = "Blocks",
			description = "All the blocks in the mod",
			icon = ModBlocks.CELL_ANALYZER.get()
		) {
			sortNumber = 1
		}


		addEntries(book)
	}

	private fun addEntries(book: ModonomiconBook) {
		bookCategory.entry(
			saveName = "coal_generator",
			name = "Coal Generator",
			icon = ModBlocks.COAL_GENERATOR.get()
		) {
			sortNumber = 1

			textPage(
				text = doubleSpacedLines(
					"The ${major("Coal Generator")} ${minor("burns furnace fuels to generate FE")}.",
					"Furnace fuels that burn longer produce more FE.",
					"The amount is configurable, but defaults to 6FE/t. With this, ${minor("1 Coal generates 9,600 FE")}."
				)
			)

			spotlightPage(ModBlocks.COAL_GENERATOR.get()) {
				text = "If the Coal Generator fills up with fuel burning, it ${minor("pauses until it has room for more energy")}, preventing waste!"
			}
		}

		cellAnalyzer = bookCategory.entry(
			saveName = "cell_analyzer",
			name = "Cell Analyzer",
			icon = ModBlocks.CELL_ANALYZER.get()
		) {
			sortNumber = 2

			textPage(
				text = "The ${major("Cell Analyzer")} uses FE to convert ${internalLink(ItemsModonomiconCategory.organicMatter, "Organic Matter")} into ${internalLink(ItemsModonomiconCategory.cell, "Cells")}."
			)

			spotlightPage(ModBlocks.CELL_ANALYZER.get()) {
				text = ""
			}
		}

		dnaExtractor = bookCategory.entry(
			saveName = "dna_extractor",
			name = "DNA Extractor",
			icon = ModBlocks.DNA_EXTRACTOR.get()
		) {
			sortNumber = 3

			textPage(
				text = "The ${major("DNA Extractor")} converts Cells and GMO Cells into DNA Helices. Regular Cells keep their entity type but have an unknown Gene; GMO Cells keep their known Gene."
			)

			spotlightPage(ModBlocks.DNA_EXTRACTOR.get()) {
				text = ""
			}
		}

		dnaDecryptor = bookCategory.entry(
			saveName = "dna_decryptor",
			name = "DNA Decryptor",
			icon = ModBlocks.DNA_DECRYPTOR.get()
		) {
			sortNumber = 4

			textPage(
				text = doubleSpacedLines(
					"The ${major("DNA Decryptor")} uses FE to decrypt ${internalLink(ItemsModonomiconCategory.dnaHelix, "DNA Helices")}.",
					"Every time DNA is decrypted, it will choose a ${internalLink(GenesModonomiconCategory.bookCategory, "Gene")}, based on the Entity the Helix came from.",
					"This Gene is weighted, some Genes have a higher chance than others. The Gene is chosen at the start of the process."
				)
			)

			spotlightPage(ModBlocks.DNA_DECRYPTOR.get()) {
				text = "If the Decryptor isn't working and there's a DNA Helix in the output slot, try taking it out. The next Gene may be of a different type, which means it can't stack with the one in the output slot."
			}
		}

		plasmidInfuser = bookCategory.entry(
			saveName = "plasmid_infuser",
			name = "Plasmid Infuser",
			icon = ModBlocks.PLASMID_INFUSER.get()
		) {
			sortNumber = 5

			textPage(
				text = doubleSpacedLines(
					"The ${major("Plasmid Infuser")} uses FE to infuse ${internalLink(ItemsModonomiconCategory.dnaHelix, "DNA Helices")} into a ${internalLink(ItemsModonomiconCategory.plasmid, "Plasmid")}.",
					"Insert an empty Plasmid in the right slot, and a DNA Helix in the left slot.",
					"The DNA Helix will be used up, and set the Plasmid's Gene to match the Helix's."
				)
			)

			spotlightPage(ModBlocks.PLASMID_INFUSER.get()) {
				text = doubleSpacedLines(
					lines(
						"Each Gene requires a certain amount of ${minor("DNA Points")} for the Plasmid to be completed.",
						list(
							"Basic Genes are worth 1 point.",
							"Genes of the Plasmid's type are worth 2 points."
						)
					),
					"A Basic Gene cannot be the first one infused into a Plasmid."
				)
			}
		}

		plasmidInjector = bookCategory.entry(
			saveName = "plasmid_injector",
			name = "Plasmid Injector",
			icon = ModBlocks.PLASMID_INJECTOR.get()
		) {
			sortNumber = 6

			textPage(
				text = doubleSpacedLines(
					"The ${major("Plasmid Injector")} uses FE to inject completed ${internalLink(ItemsModonomiconCategory.plasmid, "Plasmids")} into a ${internalLink(ItemsModonomiconCategory.syringe, "Syringe")}.",
					"You can inject as many Plasmids into a single Syringe as you want."
				)
			)

			spotlightPage(ModBlocks.PLASMID_INJECTOR.get()) {
				text = doubleSpacedLines(
					"The Syringe must be full of uncontaminated blood.",
					"Decontaminate blood in the ${internalLink(bloodPurifier, "Blood Purifier")}."
				)
			}
		}

		bloodPurifier = bookCategory.entry(
			saveName = "blood_purifier",
			name = "Blood Purifier",
			icon = ModBlocks.BLOOD_PURIFIER.get()
		) {
			sortNumber = 7

			spotlightPage(ModBlocks.BLOOD_PURIFIER.get()) {
				text = "The Blood Purifier uses FE to decontaminate ${internalLink(ItemsModonomiconCategory.syringe, "Syringes")}."
			}
		}

		incubator = bookCategory.entry(
			saveName = "incubator",
			name = "Incubator",
			icon = ModBlocks.INCUBATOR.get()
		) {
			sortNumber = 8

			textPage(
				text = "The ${major("Incubator")} uses FE for brewing and special genetic recipes. Its top ingredient is shared across three bottom slots, allowing up to three items per operation."
			)

			spotlightPage(ModBlocks.INCUBATOR.get()) {
				text = "It also accepts normal Brewing Stand recipes. Its speed can be increased with ${internalLink(ItemsModonomiconCategory.overclocker, "Overclockers")}."
			}
		}

		incubatorAdvanced = bookCategory.entry(
			saveName = "incubator_advanced",
			name = "Advanced Incubator",
			icon = ModBlocks.ADVANCED_INCUBATOR.get()
		) {
			sortNumber = 9

			textPage(
				text = doubleSpacedLines(
					"The ${major("Advanced Incubator")} is an upgrade to the ${internalLink(incubator, "Incubator")}.",
					"Like the Incubator, it functions as a Brewing Stand. However, the Advanced Incubator has a ${minor("temperature")} mechanic.",
					"The coil on the left of the GUI can be clicked to toggle between low and high temperatures."
				)
			)

			spotlightPage(ModBlocks.ADVANCED_INCUBATOR.get()) {
				text = doubleSpacedLines(
					"By default, it's ${bad("120 times slower")} at low-temperature. This means a single brew takes a full 20 minutes, excluding Overclockers.",
					"The reason you'd want to use low temperature mode is that is the only way you can get ${internalLink(ItemsModonomiconCategory.potionOfCellGrowth, 2, "Genetically Modified Cells")}."
				)
			}

			spotlightPage(gmoCellStack(registries, ModGenes.REGENERATION, EntityType.IRON_GOLEM)) {
				text = doubleSpacedLines(
					"${internalLink(ItemsModonomiconCategory.potionOfCellGrowth, 2, "Genetically Modified Cells")} are ${minor("guaranteed to have a specific Gene")}, if crafted correctly.",
					"Each GM Cell has its own recipe, with a ${bad("chance of failure")}.",
					"The recipe for this GM Cell, for example, has a 30% chance of success, by default."
				)
			}

			textPage(
				text = doubleSpacedLines(
					"Each Overclocker ${bad("decreases the chance by 10%")}.",
					"However, you can insert ${minor("Chorus Fruit")} into the top right slot to increase your odds!",
					"By default, each Chorus Fruit increases the chance by 10%. The Advanced Incubator will up as many Chorus Fruit as it takes to reach 100%."
				)
			)
		}

		antiFieldBlock = bookCategory.entry(
			saveName = "anti_field_block",
			name = "Anti-Field Block",
			icon = ModBlocks.ANTI_FIELD_BLOCK.get()
		) {
			sortNumber = 10

			textPage(
				text = doubleSpacedLines(
					"The ${major("Anti-Field Block")} allows you to ${minor("temporarily disable certain Genes")}.",
					"Specifically, it disables the ${internalLink(GenesModonomiconCategory.itemAttractionField, "Item Attraction Field")} and ${internalLink(GenesModonomiconCategory.xpAttractionField, "XP Attraction Field")} when enabled.",
					"The ${internalLink(ItemsModonomiconCategory.antiFieldOrb, "Anti-Field Orb")} functions similarly, but in item form."
				)
			)

			spotlightPage(ModBlocks.ANTI_FIELD_BLOCK.get()) {
				text = doubleSpacedLines(
					"The Anti-Field Block is active by default, and can be disabled with a Redstone signal.",
					"While active, it disables affected Genes within a 25-block radius by default. This radius is configurable."
				)
			}
		}


	}

}