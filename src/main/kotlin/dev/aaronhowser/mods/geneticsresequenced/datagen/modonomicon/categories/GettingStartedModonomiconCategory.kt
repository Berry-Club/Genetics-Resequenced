package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.entityStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.geneStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.plasmidStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.minor
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBook
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookCategory
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.doubleSpacedLines
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.internalLink
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType

object GettingStartedModonomiconCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: ModonomiconBookCategory

	fun generate(book: ModonomiconBook, registries: HolderLookup.Provider) {
		this.registries = registries
		bookCategory = book.category(
			saveName = "intro",
			name = "Getting Started",
			description = "An introduction to the mod, and instructions on how to get started.",
			icon = ModItems.DNA_HELIX.get()
		) {
			sortNumber = 0
		}

		addEntries(book)
	}

	private fun addEntries(book: ModonomiconBook) {
		bookCategory.entry(
			saveName = "genes_1",
			name = "What are Genes?",
			icon = ModItems.DNA_HELIX.get()
		) {
			sortNumber = 0

			textPage(
				text = doubleSpacedLines(
					"${major("Genes")} can be taken from mobs to ${minor("harness their abilities")}.",
					"For example, Sheep have the ${internalLink(GenesModonomiconCategory.wooly, "Wooly")} Gene. If you inject this into yourself, you will be able to be sheared for wool!",
					"To see a full list of Genes, ${internalLink(GenesModonomiconCategory.bookCategory, "see here")}."
				)
			)

			textPage(
				text = doubleSpacedLines(
					"Each Gene page in the book will say what the Gene does. As for how to get it, the mod has [EMI](https://www.curseforge.com/minecraft/mc-mods/emi) integration.",
					"Some Genes can even be given to mobs!"
				)
			)
		}

		bookCategory.entry(
			saveName = "genes_2",
			name = "Getting Genes",
			icon = ModItems.CELL.get()
		) {
			sortNumber = 1

			textPage(
				text = "Getting Genes is a rather involved process."
			)

			spotlightPage(ModItems.SCRAPER.get()) {
				text = doubleSpacedLines(
					"First, you'll need the ${internalLink(ItemsModonomiconCategory.scraper, "Scraper")}. This tool can be used on entities to collect ${internalLink(ItemsModonomiconCategory.organicMatter, "Organic Matter")} from them.",
					"The Organic Matter will have the entity's type attached. This is what decides what Genes you can get from it."
				)
			}

			spotlightPage(entityStack(ModItems.CELL.get(), EntityType.SHEEP)) {
				text = doubleSpacedLines(
					"Craft a ${internalLink(BlocksModonomiconCategory.cellAnalyzer, "Cell Analyzer")}, and process the Organic Matter through it. This will create a ${internalLink(ItemsModonomiconCategory.cell, "Cell")}.",
					"Like the Organic Matter, this has the entity's type attached."
				)
			}

			spotlightPage(entityStack(ModItems.DNA_HELIX.get(), EntityType.SHEEP)) {
				text = doubleSpacedLines(
					"Craft a ${internalLink(BlocksModonomiconCategory.dnaExtractor, "DNA Extractor")}, and process the Cell through it. This will create a ${internalLink(ItemsModonomiconCategory.dnaHelix, "DNA Helix")}, which carries an unknown Gene.",
					"To find out what Gene it is, you'll need to process it through a ${internalLink(BlocksModonomiconCategory.dnaDecryptor, "DNA Decryptor")}."
				)
			}

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.EAT_GRASS)) {
				text = doubleSpacedLines(
					"Some entities have ${minor("several Genes")} that they can give. This is a weighted choice, with some Genes being more common than others.",
					"You can see the chance of each in JEI.",
					"All DNA Helices have a chance of being ${bad("Basic")}, which contains no Gene.",
					"This does not make it useless, however."
				)
			}

			spotlightPage(plasmidStack(registries, ModGenes.EAT_GRASS, 300)) {
				text = doubleSpacedLines(
					"Now that you have some decrypted DNA Helices, you have to inject them into a ${internalLink(ItemsModonomiconCategory.plasmid, "Plasmid")}.",
					"This is done at the ${internalLink(BlocksModonomiconCategory.plasmidInfuser, "Plasmid Infuser")}.",
					"For more information on that, see the Plasmid's page, linked above."
				)
			}

			spotlightPage(ModItems.SYRINGE.get()) {
				text = doubleSpacedLines(
					"Craft a ${internalLink(ItemsModonomiconCategory.syringe, "Syringe")}, and use it to extract some of your own blood.",
					"Pass it through a ${internalLink(BlocksModonomiconCategory.bloodPurifier, "Blood Purifier")} to decontaminate it.",
					"Then, you can combine it and the completed Plasmid in a ${internalLink(BlocksModonomiconCategory.plasmidInjector, "Plasmid Injector")} to add the Gene to the Syringe."
				)
			}

			textPage(
				text = doubleSpacedLines(
					"You can add as many Genes to the Syringe as you like. When you're ready, ${minor("inject the blood back into yourself")} to gain all the Genes!",
					"Be warned, some Genes may ${bad("require other Genes")} to be able to be injected. Trying to inject them without the requisite Genes will result in dire consequences.",
					"You can see what Genes are required in the Gene's Plasmid's JEI info page."
				)
			)
		}


	}

}