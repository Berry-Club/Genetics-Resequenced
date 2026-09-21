package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.activeAntiFieldOrb
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.entityStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.geneStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.gmoCellStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.minor
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.plasmidStack
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBook
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBookCategory
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBookEntry
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.italic
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.internalLink
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items

object ItemsPatchouliCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: PatchouliBookCategory
		private set

	lateinit var dnaHelix: PatchouliBookEntry
		private set

	lateinit var organicMatter: PatchouliBookEntry
		private set

	lateinit var cell: PatchouliBookEntry
		private set

	lateinit var dragonHealthCrystal: PatchouliBookEntry
		private set

	lateinit var overclocker: PatchouliBookEntry
		private set

	lateinit var plasmid: PatchouliBookEntry
		private set

	lateinit var syringe: PatchouliBookEntry
		private set

	lateinit var scraper: PatchouliBookEntry
		private set

	lateinit var antiFieldOrb: PatchouliBookEntry
		private set

	lateinit var potionOfCellGrowth: PatchouliBookEntry
		private set

	fun generate(book: PatchouliBook, registries: HolderLookup.Provider) {
		this.registries = registries
		bookCategory = book.category(
			saveName = "items",
			name = "Items",
			description = "All the items in the mod",
			icon = ModItems.SYRINGE.get()
		) {
			sortNumber = 2
		}


		addEntries(book)
	}

	private fun addEntries(book: PatchouliBook) {
		scraper = book.entry(
			saveName = "scraper",
			category = bookCategory,
			name = "Scraper",
			icon = ModItems.SCRAPER.get()
		) {
			sortNumber = 1

			textPage(
				text = doubleSpacedLines(
					"The ${major("Scraper")} is used to get ${internalLink(organicMatter, "Organic Matter")} from mobs.",
					"To use it, simply right-click the mob. This damages the mob, which counts as an attack and will anger neutral entities."
				)
			)

			spotlightPage(ModItems.SCRAPER.get()) {
				linkRecipe = true
				text = "Scrapers can be enchanted with ${minor("Delicate Touch")}, which prevents damaging and angering entities."
			}
		}

		organicMatter = book.entry(
			saveName = "organic_matter",
			category = bookCategory,
			name = "Organic Matter",
			icon = ModItems.ORGANIC_MATTER.get()
		) {
			sortNumber = 2

			textPage(
				text = doubleSpacedLines(
					"${major("Organic Matter")} is a recipe ingredient used in the creation of ${internalLink(cell, "Cells")}.",
					"This is done in the ${internalLink(BlocksPatchouliCategory.cellAnalyzer, "Cell Analyzer")}."
				)
			)

			spotlightPage(entityStack(ModItems.ORGANIC_MATTER.get(), EntityType.COW)) {
				linkRecipe = true
				text = doubleSpacedLines(
					"Each entity has its own Organic Matter, which can be processed into a ${internalLink(cell, "Cell")} of the entity's type.",
					"You can see the Organic Matter's entity type in the item's tooltip."
				)
			}
		}

		cell = book.entry(
			saveName = "cell",
			category = bookCategory,
			name = "Cell",
			icon = ModItems.CELL.get()
		) {
			sortNumber = 3

			textPage(
				text = doubleSpacedLines(
					"${major("Cells")} are a recipe ingredient used in the creation of ${internalLink(dnaHelix, "DNA Helices")}.",
					"This is done in the ${internalLink(BlocksPatchouliCategory.dnaExtractor, "DNA Extractor")}."
				)
			)

			spotlightPage(entityStack(ModItems.CELL.get(), EntityType.COW)) {
				linkRecipe = true
				text = doubleSpacedLines(
					"Each entity has its own Cell, which can be processed into a ${internalLink(dnaHelix, "DNA Helix")} of the entity's type.",
					"You can see the Cell's entity type in the item's tooltip."
				)
			}
		}

		dnaHelix = book.entry(
			saveName = "dna_helix",
			category = bookCategory,
			name = "DNA Helix",
			icon = ModItems.DNA_HELIX.get()
		) {
			sortNumber = 4

			textPage(
				text = doubleSpacedLines(
					"${major("DNA Helices")} contain genetic information. This can be either in the form of a ${internalLink(GenesPatchouliCategory.bookCategory, "Gene")}, or an ${minor("Entity")}.",
					"Freshly crafted, a DNA Helix contains only the Entity that the DNA came from."
				)
			)

			spotlightPage(entityStack(ModItems.DNA_HELIX.get(), EntityType.COW)) {
				title = "Encrypted DNA Helix"
				linkRecipe = true
				text = "This DNA Helix contains the genetic information of a ${minor("Cow")}, but the actual Gene it has is ${bad("unknown")}. To decrypt it, you'll need to pass it through a ${internalLink(BlocksPatchouliCategory.dnaDecryptor, "DNA Decryptor")}."
			}

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.MILKY)) {
				title = "Decrypted DNA Helix"
				text = "This DNA Helix has been decrypted, and now we can see it contains the ${internalLink(GenesPatchouliCategory.milky, "Milky")} Gene."
			}

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.BASIC)) {
				title = "Basic Gene"
				text = "DNA Helices have a chance of being ${minor("Basic")}. This means that they ${bad("don't contain any Gene")}, but they can still contribute to ${internalLink(plasmid, "Plasmids")}."
			}
		}

		plasmid = book.entry(
			saveName = "plasmid",
			category = bookCategory,
			name = "Plasmid",
			icon = ModItems.PLASMID.get()
		) {
			sortNumber = 5

			textPage(
				text = doubleSpacedLines(
					"${major("Plasmids")} are the vehicle that carries ${internalLink(GenesPatchouliCategory.bookCategory, "Genes")}.",
					"Plasmids default as ${bad("empty")}, with no genetic information.",
					"To begin, use a ${internalLink(BlocksPatchouliCategory.plasmidInfuser, "Plasmid Infuser")} to infuse a decrypted DNA Helix into them.",
					"This will set the Plasmid's Gene."
				)
			)

			spotlightPage(ModItems.PLASMID.get()) {
				linkRecipe = true
				text = doubleSpacedLines(
					"Plasmids require a certain amount of ${minor("Gene Points")} to be complete.",
					"The amount of Gene Points required depends on the Plasmid's Gene. ${minor("More advanced Genes cost more Points.")}"
				)
			}

			spotlightPage(plasmidStack(registries, ModGenes.SCARE_CREEPERS, 1)) {
				text = doubleSpacedLines(
					"This Plasmid has been infused with the ${internalLink(GenesPatchouliCategory.scareCreepers, "Scare Creepers")} Gene.",
					"Notice, however, that it ${bad("only contains 1 Gene Point")}.",
					"Infusing more DNA Helices of the same Gene will add +2 Points each, while Basic Genes will add +1 each."
				)
			}

			spotlightPage(plasmidStack(registries, ModGenes.SCARE_CREEPERS, 20000)) {
				text = doubleSpacedLines(
					"Once it's reached its maximum Gene Points required, the Plasmid will be marked ${minor("complete")}!",
					"That means it's ready to be injected into a Syringe at the ${internalLink(BlocksPatchouliCategory.plasmidInjector, "Plasmid Injector")}."
				)
			}
		}

		syringe = book.entry(
			saveName = "syringe",
			category = bookCategory,
			name = "Syringe",
			icon = ModItems.SYRINGE.get()
		) {
			sortNumber = 6

			textPage(
				text = doubleSpacedLines(
					"The ${major("Syringe")} is used to ${minor("extract and inject blood")}. Simply hold right-click, and the process will end automatically.",
					"The reason you'd ${italic("want")} to extract blood is that that's what carries completed ${internalLink(plasmid, "Plasmids")} back into your body."
				)
			)

			spotlightPage(ModItems.SYRINGE.get()) {
				linkRecipe = true
				text = doubleSpacedLines(
					"When you extract blood, it'll be ${bad("contaminated")}. You'll need to clean it up in the ${internalLink(BlocksPatchouliCategory.bloodPurifier, "Blood Purifier")} before it can be used.",
					"Plasmids are added in the ${internalLink(BlocksPatchouliCategory.plasmidInjector, "Plasmid Injector")}."
				)
			}

			spotlightPage(ModItems.METAL_SYRINGE.get()) {
				linkRecipe = true
				text = doubleSpacedLines(
					"You can also use the ${major("Metal Syringe")}, which targets others instead of yourself!",
					"This is how you would give Genes to entities."
				)
			}
		}

		book.entry(
			saveName = "gene_checker",
			category = bookCategory,
			name = "Gene Checker",
			icon = ModItems.GENE_CHECKER.get()
		) {
			sortNumber = 7

			textPage(
				text = "The ${major("Gene Checker")}, as the name implies, is used to ${minor("check what Genes an entity has")}."
			)

			spotlightPage(ModItems.GENE_CHECKER.get()) {
				linkRecipe = true
				text = "If you're looking at an entity, its Genes will be listed. Otherwise, it'll list your own Genes instead."
			}
		}

		overclocker = book.entry(
			saveName = "overclocker",
			category = bookCategory,
			name = "Overclocker",
			icon = ModItems.OVERCLOCKER.get()
		) {
			sortNumber = 8

			textPage(
				text = doubleSpacedLines(
					"The ${major("Overclocker")} can be inserted into most of the mod's machines to ${minor("massively increase their speed")}.",
					"Each Overclocker doubles the speed, but increases the power draw by a quarter."
				)
			)

			spotlightPage(ModItems.OVERCLOCKER.get()) {
				linkRecipe = true
				text = "You can have ${minor("up to 8 Overclockers")} in a machine."
			}
		}

		antiFieldOrb = book.entry(
			saveName = "anti_field_orb",
			category = bookCategory,
			name = "Anti-Field Orb",
			icon = ModItems.ANTI_FIELD_ORB.get()
		) {
			sortNumber = 9

			textPage(
				text = doubleSpacedLines(
					"The ${major("Anti-Field Orb")} allows you to ${minor("temporarily disable certain Genes")}.",
					"Specifically, it disables the ${internalLink(GenesPatchouliCategory.itemAttractionField, "Item Attraction Field")} and ${internalLink(GenesPatchouliCategory.xpAttractionField, "XP Attraction Field")} when enabled.",
					"The ${internalLink(BlocksPatchouliCategory.antiFieldBlock, "Anti-Field Block")} functions similarly, but in block form."
				)
			)

			spotlightPage(activeAntiFieldOrb()) {
				linkRecipe = true
				text = "The Anti-Field Orb can be toggled by right-clicking it while held."
			}
		}

		dragonHealthCrystal = book.entry(
			saveName = "dragon_health_crystal",
			category = bookCategory,
			name = "Dragon Health Crystal",
			icon = ModItems.DRAGON_HEALTH_CRYSTAL.get()
		) {
			sortNumber = 10

			textPage(
				text = doubleSpacedLines(
					"The ${major("Dragon Health Crystal")} is part of the ${internalLink(GenesPatchouliCategory.enderDragonHealth, "Ender Dragon Health")} Gene.",
					"While you have that Gene, and while holding a Dragon Health Crystal, any incoming damage is negated and instead dealt to the Crystal."
				)
			)

			spotlightPage(ModItems.DRAGON_HEALTH_CRYSTAL.get()) {
				linkRecipe = true
				text = doubleSpacedLines(
					"A fresh Dragon Health Crystal has ${minor("1,000 durability")}.",
					"Each half-heart deals 1 point of durability damage.",
					"It can be repaired with End Crystals."
				)
			}
		}

		book.entry(
			saveName = "potion_organic_substrate",
			category = bookCategory,
			name = "Organic Substrate",
			icon = Items.POTION
		) {
			sortNumber = 11

			textPage(
				text = doubleSpacedLines(
					"${major("Organic Substrate")} is the first of the mod's potions.",
					"Organic Substrate has no effect when drunk, but is used in some recipes.",
					"If a ${internalLink(cell, "Cell")} is brewed into Organic Substrate, the Substrate is replaced with a copy of the Cell. Given that you can have 3 Potions in a single brew, this allows you to triple Cells, if needed."
				)
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.SUBSTRATE)) {
				text = doubleSpacedLines(
					"Additionally, Organic Substrate is used to brew ${internalLink(potionOfCellGrowth, "Potions of Cell Growth")}.",
					"You may prefer to use the ${internalLink(BlocksPatchouliCategory.incubator, "Incubator")} to brew these recipes, rather than the Brewing Stand."
				)
			}
		}

		potionOfCellGrowth = book.entry(
			saveName = "potion_of_cell_growth",
			category = bookCategory,
			name = "Potion of Cell Growth",
			icon = Items.POTION
		) {
			sortNumber = 12

			spotlightPage(OtherUtil.getPotionStack(ModPotions.CELL_GROWTH)) {
				text = doubleSpacedLines(
					"${major("Potions of Cell Growth")}, like Organic Substrate, has no effect when imbibed. It's more of a crafting ingredient than a potion.",
					"These recipes allow you to ${minor("improve your odds of getting certain rare Genes")}."
				)
			}

			spotlightPage(entityStack(OtherUtil.getPotionStack(ModPotions.CELL_GROWTH), EntityType.BLAZE)) {
				text = "A ${internalLink(cell, "Cell")} can be brewed into the Potion of Cell Growth to ${minor("set the Potion to the Cell's entity type")}."
			}

			spotlightPage(gmoCellStack(registries, ModGenes.BIOLUMINESCENCE, EntityType.BLAZE)) {
				linkRecipe = true
				text = doubleSpacedLines(
					"From there, certain entity types have recipes to make ${major("Genetically Modified Cells")}, which are guaranteed to give a specific Gene.",
					"However, there's a catch. They can ${italic("only")} be crafted in an ${internalLink(BlocksPatchouliCategory.incubatorAdvanced, "Advanced Incubator")} at low temperature, and there's ${bad("a chance of failure")}."
				)
			}

			textPage(
				text = doubleSpacedLines(
					"A low-temperature Advanced Incubator takes a very long time, but can be accelerated using Overclockers.",
					"Unfortunately, ${bad("Overclockers lower your chances")} of success. ${minor("Chorus Fruits")} increase it back, though!",
					"Process the GM Cell through a ${internalLink(BlocksPatchouliCategory.dnaExtractor, "DNA Extractor")} to get the set Helix."
				)
			)
		}

		book.entry(
			saveName = "potion_mutation",
			category = bookCategory,
			name = "Potion of Mutation",
			icon = Items.POTION
		) {
			sortNumber = 13

			textPage(
				text = doubleSpacedLines(
					"The ${major("Potion of Mutation")} is used to craft ${internalLink(potionOfCellGrowth, "gmo_cell", "Genetically Modified Cells")} that are set to ${minor("mutation Genes")}.",
					"Mutation Genes are effectively just ${minor("more powerful")} Genes, and generally ${bad("require other Genes")}."
				)
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.MUTATION)) {
				text = ""
			}
		}

		book.entry(
			saveName = "potion_viral_agents",
			category = bookCategory,
			name = "Viral Agents",
			icon = Items.POTION
		) {
			sortNumber = 14

			textPage(
				text = doubleSpacedLines(
					"${major("Viral Agents")} are the final crafting potion of the mod. They are used to craft ${internalLink(NegativeGenesPatchouliCategory.bookCategory, "negative Genes")}, which are generally just any Gene that's harmful.",
					"Negative Genes are ${minor("always lost on death")}, regardless of config setting. Additionally, there's a config to prevent players from even being able to ${italic("get")} negative Genes in the first place."
				)
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)) {
				text = "Virus Cultivation recipes can be crafted in the Brewing Stand or in either Incubator."
			}
		}

		book.entry(
			saveName = "potion_panacea",
			category = bookCategory,
			name = "Panacea",
			icon = Items.POTION
		) {
			sortNumber = 15

			textPage(
				text = "${major("Panacea")} is a simple potion: It cures all negative effects, and ${minor("removes all negative Genes")}."
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.PANACEA)) {
				text = "Sure, you could do the same with a Bucket of Milk and an Anti-Plasmid, but this is much more convenient, sometimes, maybe."
			}
		}

		book.entry(
			saveName = "potion_zombify_villager",
			category = bookCategory,
			name = "Potion of Zombify Villager",
			icon = Items.POTION
		) {
			sortNumber = 16

			textPage(
				text = "A ${major("Potion of Zombify Villager")} does right what it says on the tin: it turns Villagers into Zombie Villagers."
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.ZOMBIFY_VILLAGER)) {
				text = "Obviously, it's best utilized in Splash form."
			}
		}


	}

}