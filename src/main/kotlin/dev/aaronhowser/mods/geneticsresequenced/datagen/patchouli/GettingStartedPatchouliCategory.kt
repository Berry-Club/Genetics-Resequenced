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

class GettingStartedPatchouliCategory(
	private val registries: HolderLookup.Provider
) {

	private lateinit var category: PatchouliBookCategory

	fun generate(book: PatchouliBook) {
		category = book.category(
			saveName = "intro",
			name = "Getting Started",
			description = "An introduction to the mod, and instructions on how to get started.",
			icon = ModItems.DNA_HELIX.get()
		) {
			sortNumber = 0
		}

		BOOK_CATEGORY = category

		book.addEntries()
	}

	private fun PatchouliBook.addEntries() {
		entry(
			saveName = GENES_1.localSaveName,
			category = category,
			name = "What are Genes?",
			icon = ModItems.DNA_HELIX.get()
		) {
			sortNumber = 0

			textPage(
				text = "${major("Genes")} can be taken from mobs to ${minor("harness their abilities")}.\$(br2)For example, Sheep have the ${pageLink(GenesPatchouliCategory.WOOLY, "Wooly")} Gene. If you inject this into yourself, you will be able to be sheared for wool!\$(br2)To see a full list of Genes, ${pageLink(GenesPatchouliCategory.CATEGORY, "see here")}."
			)

			textPage(
				text = "Each Gene page in the book will say what the Gene does, As for how to get it, the mod has \$(l:https://www.curseforge.com/minecraft/mc-mods/emi)EMI/\$ integration.\$(br2)Some Genes can even be given to mobs!"
			)
		}

		entry(
			saveName = GENES_2.localSaveName,
			category = category,
			name = "Getting Genes",
			icon = ModItems.CELL.get()
		) {
			sortNumber = 1

			textPage(
				text = "Getting Genes is a rather involved process."
			)

			spotlightPage(ModItems.SCRAPER.get()) {
				text = "First, you'll need the ${pageLink(ItemsPatchouliCategory.SCRAPER, "Scraper")}. This tool can be used on entities to collect ${pageLink(ItemsPatchouliCategory.ORGANIC_MATTER, "Organic Matter")} from them.\$(br2)The Organic Matter will have the entity's type attached. This is what decides what Genes you can get from it."
			}

			spotlightPage(entityStack(ModItems.CELL.get(), EntityType.SHEEP)) {
				text = "Craft a ${pageLink(BlocksPatchouliCategory.CELL_ANALYZER, "Cell Analyzer")}, and process the Organic Matter through it. This will create a ${pageLink(ItemsPatchouliCategory.CELL, "Cell")}.\$(br2)Like the Organic Matter, this has the entity's type attached."
			}

			spotlightPage(entityStack(ModItems.DNA_HELIX.get(), EntityType.SHEEP)) {
				text = "Craft a ${pageLink(BlocksPatchouliCategory.DNA_EXTRACTOR, "DNA Extractor")}, and process the Cell through it. This will create a ${pageLink(ItemsPatchouliCategory.DNA_HELIX, "DNA Helix")}, which carries an unknown Gene.\$(br2)To find out what Gene it is, you'll need to process it through a ${pageLink(BlocksPatchouliCategory.DNA_DECRYPTOR, "DNA Decryptor")}."
			}

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.EAT_GRASS)) {
				text = "Some entities have ${minor("several Genes")} that they can give. This is a weighted choice, with some Genes being more common than others.\$(br2)You can see the chance of each in JEI.\$(br2)All DNA Helices have a chance of being ${bad("Basic")}, which contains no Gene.\$(br2)This does not make it useless, however."
			}

			spotlightPage(plasmidStack(registries, ModGenes.EAT_GRASS, 300)) {
				text = "Now that you have some decrypted DNA Helices, you have to inject them into a ${pageLink(ItemsPatchouliCategory.PLASMID, "Plasmid")}.\$(br2)This is done at the ${pageLink(BlocksPatchouliCategory.PLASMID_INFUSER, "Plasmid Infuser")}.\$(br2)For more information on that, see the Plasmid's page, linked above."
			}

			spotlightPage(ModItems.SYRINGE.get()) {
				text = "Craft a ${pageLink(ItemsPatchouliCategory.SYRINGE, "Syringe")}, and use it to extract some of your own blood.\$(br2)Pass it through a ${pageLink(BlocksPatchouliCategory.BLOOD_PURIFIER, "Blood Purifier")} to decontaminate it.\$(br2)Then, you can combine it and the completed Plasmid in a ${pageLink(BlocksPatchouliCategory.PLASMID_INJECTOR, "Plasmid Injector")} to add the Gene to the Syringe."
			}

			textPage(
				text = "You can add as many Genes to the Syringe as you like. When you're ready, ${minor("inject the blood back into yourself")} to gain all the Genes!\$(br2)Be warned, some Genes may ${bad("require other Genes")} to be able to be injected. Trying to inject them without the requisite Genes will result in dire consequences.\$(br2)You can see what Genes are required in the Gene's Plasmid's JEI info page."
			)
		}
	}

	companion object {

		lateinit var BOOK_CATEGORY: PatchouliBookCategory
			private set

		val CATEGORY = PatchouliBookReference("intro")
		val GENES_1 = PatchouliBookReference("intro/genes_1")
		val GENES_2 = PatchouliBookReference("intro/genes_2")
	}
}