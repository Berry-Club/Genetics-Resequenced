package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.activeAntiFieldOrb
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.entityStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.geneStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.gmoCellStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.plasmidStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.minor
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBook
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookCategory
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookEntry
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.doubleSpacedLines
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.internalLink
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.italic
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType

object ItemsModonomiconCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: ModonomiconBookCategory
	lateinit var dnaHelix: ModonomiconBookEntry
	lateinit var organicMatter: ModonomiconBookEntry
	lateinit var cell: ModonomiconBookEntry
	lateinit var dragonHealthCrystal: ModonomiconBookEntry
	lateinit var overclocker: ModonomiconBookEntry
	lateinit var plasmid: ModonomiconBookEntry
	lateinit var syringe: ModonomiconBookEntry
	lateinit var scraper: ModonomiconBookEntry
	lateinit var antiFieldOrb: ModonomiconBookEntry
	lateinit var potionOfCellGrowth: ModonomiconBookEntry

	fun generate(book: ModonomiconBook, registries: HolderLookup.Provider) {
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

	private fun addEntries(book: ModonomiconBook) {
		scraper = bookCategory.entry(
			saveName = "scraper",
			name = "Scraper",
			icon = ModItems.SCRAPER.get()
		) {
			sortNumber = 1

			textPage(
				text = doubleSpacedLines(
					"The ${major("Scraper")} is used to get ${internalLink(organicMatter, "Organic Matter")} from mobs.",
					"Right-click a mob to scrape it, or sneak and right-click empty air to scrape yourself. Scraping damages and may anger the target."
				)
			)

			spotlightPage(ModItems.SCRAPER.get()) {
				text = "Scrapers can be enchanted with ${minor("Delicate Touch")}, which prevents damaging and angering entities."
			}
		}

		organicMatter = bookCategory.entry(
			saveName = "organic_matter",
			name = "Organic Matter",
			icon = ModItems.ORGANIC_MATTER.get()
		) {
			sortNumber = 2

			textPage(
				text = doubleSpacedLines(
					"${major("Organic Matter")} is a recipe ingredient used in the creation of ${internalLink(cell, "Cells")}.",
					"This is done in the ${internalLink(BlocksModonomiconCategory.cellAnalyzer, "Cell Analyzer")}."
				)
			)

			spotlightPage(entityStack(ModItems.ORGANIC_MATTER.get(), EntityType.COW)) {
				text = doubleSpacedLines(
					"Each entity has its own Organic Matter, which can be processed into a ${internalLink(cell, "Cell")} of the entity's type.",
					"You can see the Organic Matter's entity type in the item's tooltip."
				)
			}
		}

		cell = bookCategory.entry(
			saveName = "cell",
			name = "Cell",
			icon = ModItems.CELL.get()
		) {
			sortNumber = 3

			textPage(
				text = doubleSpacedLines(
					"${major("Cells")} are a recipe ingredient used in the creation of ${internalLink(dnaHelix, "DNA Helices")}.",
					"This is done in the ${internalLink(BlocksModonomiconCategory.dnaExtractor, "DNA Extractor")}."
				)
			)

			spotlightPage(entityStack(ModItems.CELL.get(), EntityType.COW)) {
				text = doubleSpacedLines(
					"Each entity has its own Cell, which can be processed into a ${internalLink(dnaHelix, "DNA Helix")} of the entity's type.",
					"You can see the Cell's entity type in the item's tooltip."
				)
			}
		}

		dnaHelix = bookCategory.entry(
			saveName = "dna_helix",
			name = "DNA Helix",
			icon = ModItems.DNA_HELIX.get()
		) {
			sortNumber = 4

			textPage(
				text = doubleSpacedLines(
					"${major("DNA Helices")} contain genetic information. This can be either in the form of a ${internalLink(GenesModonomiconCategory.bookCategory, "Gene")}, or an ${minor("Entity")}.",
					"Freshly crafted, a DNA Helix contains only the Entity that the DNA came from."
				)
			)

			spotlightPage(entityStack(ModItems.DNA_HELIX.get(), EntityType.COW)) {
				title = "Encrypted DNA Helix"
				text = "This DNA Helix contains the genetic information of a ${minor("Cow")}, but the actual Gene it has is ${bad("unknown")}. To decrypt it, you'll need to pass it through a ${internalLink(BlocksModonomiconCategory.dnaDecryptor, "DNA Decryptor")}."
			}

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.MILKY)) {
				title = "Decrypted DNA Helix"
				text = "This DNA Helix has been decrypted, and now we can see it contains the ${internalLink(GenesModonomiconCategory.milky, "Milky")} Gene."
			}

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.BASIC)) {
				title = "Basic Gene"
				text = "DNA Helices have a chance of being ${minor("Basic")}. This means that they ${bad("don't contain any Gene")}, but they can still contribute to ${internalLink(plasmid, "Plasmids")}."
			}
		}

		plasmid = bookCategory.entry(
			saveName = "plasmid",
			name = "Plasmid",
			icon = ModItems.PLASMID.get()
		) {
			sortNumber = 5

			textPage(
				text = doubleSpacedLines(
					"${major("Plasmids")} are the vehicle that carries ${internalLink(GenesModonomiconCategory.bookCategory, "Genes")}.",
					"Plasmids default as ${bad("empty")}, with no genetic information.",
					"To begin, use a ${internalLink(BlocksModonomiconCategory.plasmidInfuser, "Plasmid Infuser")} to infuse a decrypted DNA Helix into them.",
					"This will set the Plasmid's Gene."
				)
			)

			spotlightPage(ModItems.PLASMID.get()) {
				text = doubleSpacedLines(
					"Plasmids require a certain amount of ${minor("Gene Points")} to be complete.",
					"The amount of Gene Points required depends on the Plasmid's Gene. ${minor("More advanced Genes cost more Points.")}"
				)
			}

			spotlightPage(plasmidStack(registries, ModGenes.SCARE_CREEPERS, 1)) {
				text = doubleSpacedLines(
					"This Plasmid has been infused with the ${internalLink(GenesModonomiconCategory.scareCreepers, "Scare Creepers")} Gene.",
					"Notice, however, that it ${bad("only contains 1 Gene Point")}.",
					"Infusing more DNA Helices of the same Gene will add +2 Points each, while Basic Genes will add +1 each."
				)
			}

			spotlightPage(plasmidStack(registries, ModGenes.SCARE_CREEPERS, 20000)) {
				text = doubleSpacedLines(
					"Once it's reached its maximum Gene Points required, the Plasmid will be marked ${minor("complete")}!",
					"That means it's ready to be injected into a Syringe at the ${internalLink(BlocksModonomiconCategory.plasmidInjector, "Plasmid Injector")}."
				)
			}
		}

		syringe = bookCategory.entry(
			saveName = "syringe",
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
				text = doubleSpacedLines(
					"When you extract blood, it'll be ${bad("contaminated")}. You'll need to clean it up in the ${internalLink(BlocksModonomiconCategory.bloodPurifier, "Blood Purifier")} before it can be used.",
					"Plasmids are added in the ${internalLink(BlocksModonomiconCategory.plasmidInjector, "Plasmid Injector")}."
				)
			}

			spotlightPage(ModItems.METAL_SYRINGE.get()) {
				text = doubleSpacedLines(
					"You can also use the ${major("Metal Syringe")}, which targets others instead of yourself!",
					"This is how you would give Genes to entities."
				)
			}
		}

		bookCategory.entry(
			saveName = "metal_syringe",
			name = "Metal Syringe",
			icon = ModItems.METAL_SYRINGE.get()
		) {
			textPage(
				text = doubleSpacedLines(
					"A ${major("Metal Syringe")} extracts blood from the entity you target instead of yourself.",
					"Only that blood's owner can receive its Genes. While filled, the owner glows through walls."
				)
			)

			spotlightPage(ModItems.METAL_SYRINGE.get()) {
				text = "Purify contaminated blood, add completed Plasmids in a Plasmid Injector, then hold right-click on the blood's owner to inject it."
			}
		}

		bookCategory.entry(
			saveName = "gene_checker",
			name = "Gene Checker",
			icon = ModItems.GENE_CHECKER.get()
		) {
			sortNumber = 7

			textPage(
				text = "The ${major("Gene Checker")}, as the name implies, is used to ${minor("check what Genes an entity has")}."
			)

			spotlightPage(ModItems.GENE_CHECKER.get()) {
				text = "It shows both the target's current Genes and the Genes it can provide with their weights. If no entity is targeted, it checks you."
			}
		}

		overclocker = bookCategory.entry(
			saveName = "overclocker",
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
				text = "You can have ${minor("up to 8 Overclockers")} in a machine."
			}
		}

		antiFieldOrb = bookCategory.entry(
			saveName = "anti_field_orb",
			name = "Anti-Field Orb",
			icon = ModItems.ANTI_FIELD_ORB.get()
		) {
			sortNumber = 9

			textPage(
				text = doubleSpacedLines(
					"The ${major("Anti-Field Orb")} allows you to ${minor("temporarily disable certain Genes")}.",
					"Specifically, it disables the ${internalLink(GenesModonomiconCategory.itemAttractionField, "Item Attraction Field")} and ${internalLink(GenesModonomiconCategory.xpAttractionField, "XP Attraction Field")} when enabled.",
					"The ${internalLink(BlocksModonomiconCategory.antiFieldBlock, "Anti-Field Block")} functions similarly, but in block form."
				)
			)

			spotlightPage(activeAntiFieldOrb()) {
				text = "The Anti-Field Orb can be toggled by right-clicking it while held."
			}
		}

		dragonHealthCrystal = bookCategory.entry(
			saveName = "dragon_health_crystal",
			name = "Dragon Health Crystal",
			icon = ModItems.DRAGON_HEALTH_CRYSTAL.get()
		) {
			sortNumber = 10

			textPage(
				text = doubleSpacedLines(
					"The ${major("Dragon Health Crystal")} is part of the ${internalLink(GenesModonomiconCategory.enderDragonHealth, "Ender Dragon Health")} Gene.",
					"While you have that Gene, and while holding a Dragon Health Crystal, any incoming damage is negated and instead dealt to the Crystal."
				)
			)

			spotlightPage(ModItems.DRAGON_HEALTH_CRYSTAL.get()) {
				text = doubleSpacedLines(
					"A fresh Dragon Health Crystal has ${minor("1,000 durability")}.",
					"Each half-heart deals 1 point of durability damage.",
					"Only the first Crystal in your inventory is used. It can be repaired with Ghast Tears."
				)
			}
		}

		bookCategory.entry(
			saveName = "anti_plasmid",
			name = "Anti-Plasmid",
			icon = ModItems.ANTI_PLASMID.get()
		) {
			textPage(
				text = doubleSpacedLines(
					"An ${major("Anti-Plasmid")} removes one unwanted Gene from its host.",
					"Craft an empty Anti-Plasmid with a completed Plasmid to copy its Gene."
				)
			)

			spotlightPage(ModItems.ANTI_PLASMID.get()) {
				text = "Add it to purified blood in the Plasmid Injector, then inject that blood into its owner."
			}
		}

		bookCategory.entry(
			saveName = "gmo_cell",
			name = "GMO Cell",
			icon = ModItems.GMO_CELL.get()
		) {
			textPage(
				text = doubleSpacedLines(
					"A ${major("GMO Cell")} already has a known Gene, skipping random DNA Decryption.",
					"Make one from a typed Potion of Cell Growth or Mutation in a low-temperature Advanced Incubator. Each recipe has its own success chance."
				)
			)

			spotlightPage(ModItems.GMO_CELL.get()) {
				text = "Process it in a DNA Extractor to get a DNA Helix with its Gene already revealed. Recipe viewers show the available combinations."
			}
		}

		bookCategory.entry(
			saveName = "potion_organic_substrate",
			name = "Organic Substrate",
			icon = OtherUtil.getPotionStack(ModPotions.SUBSTRATE)
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
					"You may prefer to use the ${internalLink(BlocksModonomiconCategory.incubator, "Incubator")} to brew these recipes, rather than the Brewing Stand."
				)
			}
		}

		potionOfCellGrowth = bookCategory.entry(
			saveName = "potion_of_cell_growth",
			name = "Potion of Cell Growth",
			icon = OtherUtil.getPotionStack(ModPotions.CELL_GROWTH)
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
				text = doubleSpacedLines(
					"From there, certain entity types have recipes to make ${major("Genetically Modified Cells")}, which are guaranteed to give a specific Gene.",
					"However, there's a catch. They can ${italic("only")} be crafted in an ${internalLink(BlocksModonomiconCategory.incubatorAdvanced, "Advanced Incubator")} at low temperature, and there's ${bad("a chance of failure")}."
				)
			}

			textPage(
				text = doubleSpacedLines(
					"A low-temperature Advanced Incubator takes a very long time, but can be accelerated using Overclockers.",
					"Unfortunately, ${bad("Overclockers lower your chances")} of success. ${minor("Chorus Fruits")} increase it back, though!",
					"Process the GM Cell through a ${internalLink(BlocksModonomiconCategory.dnaExtractor, "DNA Extractor")} to get the set Helix."
				)
			)
		}

		bookCategory.entry(
			saveName = "potion_mutation",
			name = "Potion of Mutation",
			icon = OtherUtil.getPotionStack(ModPotions.MUTATION)
		) {
			sortNumber = 13

			textPage(
				text = doubleSpacedLines(
					"The ${major("Potion of Mutation")} is used to craft ${internalLink(potionOfCellGrowth, 2, "Genetically Modified Cells")} that are set to ${minor("mutation Genes")}.",
					"Mutation Genes are effectively just ${minor("more powerful")} Genes, and generally ${bad("require other Genes")}."
				)
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.MUTATION)) {
				text = ""
			}
		}

		bookCategory.entry(
			saveName = "potion_viral_agents",
			name = "Viral Agents",
			icon = OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)
		) {
			sortNumber = 14

			textPage(
				text = doubleSpacedLines(
					"${major("Viral Agents")} are the final crafting potion of the mod. They are used to craft ${internalLink(NegativeGenesModonomiconCategory.bookCategory, "negative Genes")}, which are generally just any Gene that's harmful.",
					"Negative Genes are ${minor("always lost on death")}, regardless of config setting. Additionally, there's a config to prevent players from even being able to ${italic("get")} negative Genes in the first place."
				)
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)) {
				text = "Virus Cultivation recipes can be crafted in the Brewing Stand or in either Incubator."
			}
		}

		bookCategory.entry(
			saveName = "potion_panacea",
			name = "Panacea",
			icon = OtherUtil.getPotionStack(ModPotions.PANACEA)
		) {
			sortNumber = 15

			textPage(
				text = "${major("Panacea")} is a simple potion: It cures all negative effects, and ${minor("removes all negative Genes")}."
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.PANACEA)) {
				text = "Sure, you could do the same with a Bucket of Milk and an Anti-Plasmid, but this is much more convenient, sometimes, maybe."
			}
		}

		bookCategory.entry(
			saveName = "potion_zombify_villager",
			name = "Potion of Zombify Villager",
			icon = OtherUtil.getPotionStack(ModPotions.ZOMBIFY_VILLAGER)
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