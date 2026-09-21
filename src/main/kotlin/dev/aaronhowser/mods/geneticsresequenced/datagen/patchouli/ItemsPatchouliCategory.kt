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

class ItemsPatchouliCategory(
	private val registries: HolderLookup.Provider
) {

	private lateinit var category: PatchouliBookCategory

	fun generate(book: PatchouliBook) {
		category = book.category(
			saveName = "items",
			name = "Items",
			description = "All the items in the mod",
			icon = ModItems.SYRINGE.get()
		) {
			sortNumber = 2
		}

		BOOK_CATEGORY = category

		book.addEntries()
	}

	private fun PatchouliBook.addEntries() {
		entry(
			saveName = ANTI_FIELD_ORB.localSaveName,
			category = category,
			name = "Anti-Field Orb",
			icon = ModItems.ANTI_FIELD_ORB.get()
		) {
			sortNumber = 9

			textPage(
				text = "The ${major("Anti-Field Orb")} allows you to ${minor("temporarily disable certain Genes")}.\$(br2)Specifically, it disables the ${pageLink(GenesPatchouliCategory.ITEM_ATTRACTION_FIELD, "Item Attraction Field")} and ${pageLink(GenesPatchouliCategory.XP_ATTRACTION_FIELD, "XP Attraction Field")} when enabled.\$(br2)The ${pageLink(BlocksPatchouliCategory.ANTI_FIELD_BLOCK, "Anti-Field Block")} functions similarly, but in block form."
			)

			spotlightPage(activeAntiFieldOrb()) {
				linkRecipe = true
				text = "The Anti-Field Orb can be toggled by right-clicking it while held."
			}
		}

		entry(
			saveName = CELL.localSaveName,
			category = category,
			name = "Cell",
			icon = ModItems.CELL.get()
		) {
			sortNumber = 3

			textPage(
				text = "${major("Cells")} are a recipe ingredient used in the creation of ${pageLink(ItemsPatchouliCategory.DNA_HELIX, "DNA Helices")}.\$(br2)This is done in the ${pageLink(BlocksPatchouliCategory.DNA_EXTRACTOR, "DNA Extractor")}."
			)

			spotlightPage(entityStack(ModItems.CELL.get(), EntityType.COW)) {
				linkRecipe = true
				text = "Each entity has its own Cell, which can be processed into a ${pageLink(ItemsPatchouliCategory.DNA_HELIX, "DNA Helix")} of the entity's type.\$(br2)You can see the Cell's entity type in the item's tooltip."
			}
		}

		entry(
			saveName = DNA_HELIX.localSaveName,
			category = category,
			name = "DNA Helix",
			icon = ModItems.DNA_HELIX.get()
		) {
			sortNumber = 4

			textPage(
				text = "${major("DNA Helices")} contain genetic information. This can be either in the form of a ${pageLink(GenesPatchouliCategory.CATEGORY, "Gene")}, or an ${minor("Entity")}.\$(br2)Freshly crafted, a DNA Helix contains only the Entity that the DNA came from."
			)

			spotlightPage(entityStack(ModItems.DNA_HELIX.get(), EntityType.COW)) {
				title = "Encrypted DNA Helix"
				linkRecipe = true
				text = "This DNA Helix contains the genetic information of a ${minor("Cow")}, but the actual Gene it has is ${bad("unknown")}. To decrypt it, you'll need to pass it through a ${pageLink(BlocksPatchouliCategory.DNA_DECRYPTOR, "DNA Decryptor")}."
			}

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.MILKY)) {
				title = "Decrypted DNA Helix"
				text = "This DNA Helix has been decrypted, and now we can see it contains the ${pageLink(GenesPatchouliCategory.MILKY, "Milky")} Gene."
			}

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.BASIC)) {
				title = "Basic Gene"
				text = "DNA Helices have a chance of being ${minor("Basic")}. This means that they ${bad("don't contain any Gene")}, but they can still contribute to ${pageLink(ItemsPatchouliCategory.PLASMID, "Plasmids")}."
			}
		}

		entry(
			saveName = DRAGON_HEALTH_CRYSTAL.localSaveName,
			category = category,
			name = "Dragon Health Crystal",
			icon = ModItems.DRAGON_HEALTH_CRYSTAL.get()
		) {
			sortNumber = 10

			textPage(
				text = "The ${major("Dragon Health Crystal")} is part of the ${pageLink(GenesPatchouliCategory.ENDER_DRAGON_HEALTH, "Ender Dragon Health")} Gene.\$(br2)While you have that Gene, and while holding a Dragon Health Crystal, any incoming damage is negated and instead dealt to the Crystal."
			)

			spotlightPage(ModItems.DRAGON_HEALTH_CRYSTAL.get()) {
				linkRecipe = true
				text = "A fresh Dragon Health Crystal has ${minor("1,000 durability")}.\$(br2)Each half-heart deals 1 point of durability damage.\$(br2)It can be repaired with End Crystals."
			}
		}

		entry(
			saveName = GENE_CHECKER.localSaveName,
			category = category,
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

		entry(
			saveName = ORGANIC_MATTER.localSaveName,
			category = category,
			name = "Organic Matter",
			icon = ModItems.ORGANIC_MATTER.get()
		) {
			sortNumber = 2

			textPage(
				text = "${major("Organic Matter")} is a recipe ingredient used in the creation of ${pageLink(ItemsPatchouliCategory.CELL, "Cells")}.\$(br2)This is done in the ${pageLink(BlocksPatchouliCategory.CELL_ANALYZER, "Cell Analyzer")}."
			)

			spotlightPage(entityStack(ModItems.ORGANIC_MATTER.get(), EntityType.COW)) {
				linkRecipe = true
				text = "Each entity has its own Organic Matter, which can be processed into a ${pageLink(ItemsPatchouliCategory.CELL, "Cell")} of the entity's type.\$(br2)You can see the Organic Matter's entity type in the item's tooltip."
			}
		}

		entry(
			saveName = OVERCLOCKER.localSaveName,
			category = category,
			name = "Overclocker",
			icon = ModItems.OVERCLOCKER.get()
		) {
			sortNumber = 8

			textPage(
				text = "The ${major("Overclocker")} can be inserted into most of the mod's machines to ${minor("massively increase their speed")}.\$(br2)Each Overclocker doubles the speed, but increases the power draw by a quarter."
			)

			spotlightPage(ModItems.OVERCLOCKER.get()) {
				linkRecipe = true
				text = "You can have ${minor("up to 8 Overclockers")} in a machine."
			}
		}

		entry(
			saveName = PLASMID.localSaveName,
			category = category,
			name = "Plasmid",
			icon = ModItems.PLASMID.get()
		) {
			sortNumber = 5

			textPage(
				text = "${major("Plasmids")} are the vehicle that carries ${pageLink(GenesPatchouliCategory.CATEGORY, "Genes")}.\$(br2)Plasmids default as ${bad("empty")}, with no genetic information.\$(br2)To begin, use a ${pageLink(BlocksPatchouliCategory.PLASMID_INFUSER, "Plasmid Infuser")} to infuse a decrypted DNA Helix into them.\$(br2)This will set the Plasmid's Gene."
			)

			spotlightPage(ModItems.PLASMID.get()) {
				linkRecipe = true
				text = "Plasmids require a certain amount of ${minor("Gene Points")} to be complete.\$(br2)The amount of Gene Points required depends on the Plasmid's Gene. ${minor("More advanced Genes cost more Points.")}"
			}

			spotlightPage(plasmidStack(registries, ModGenes.SCARE_CREEPERS, 1)) {
				text = "This Plasmid has been infused with the ${pageLink(GenesPatchouliCategory.SCARE_CREEPERS, "Scare Creepers")} Gene.\$(br2)Notice, however, that it ${bad("only contains 1 Gene Point")}.\$(br2)Infusing more DNA Helices of the same Gene will add +2 Points each, while Basic Genes will add +1 each."
			}

			spotlightPage(plasmidStack(registries, ModGenes.SCARE_CREEPERS, 20000)) {
				text = "Once it's reached its maximum Gene Points required, the Plasmid will be marked ${minor("complete")}!\$(br2)That means it's ready to be injected into a Syringe at the ${pageLink(BlocksPatchouliCategory.PLASMID_INJECTOR, "Plasmid Injector")}."
			}
		}

		entry(
			saveName = POTION_MUTATION.localSaveName,
			category = category,
			name = "Potion of Mutation",
			icon = Items.POTION
		) {
			sortNumber = 13

			textPage(
				text = "The ${major("Potion of Mutation")} is used to craft \$(l:geneticsresequenced:items/cell_growth#gmo_cell)Genetically Modified Cells/\$ that are set to ${minor("mutation Genes")}.\$(br2)Mutation Genes are effectively just ${minor("more powerful")} Genes, and generally ${bad("require other Genes")}."
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.MUTATION)) {
				text = ""
			}
		}

		entry(
			saveName = POTION_OF_CELL_GROWTH.localSaveName,
			category = category,
			name = "Potion of Cell Growth",
			icon = Items.POTION
		) {
			sortNumber = 12

			spotlightPage(OtherUtil.getPotionStack(ModPotions.CELL_GROWTH)) {
				text = "${major("Potions of Cell Growth")}, like Organic Substrate, has no effect when imbibed. It's more of a crafting ingredient than a potion.\$(br2)These recipes allow you to ${minor("improve your odds of getting certain rare Genes")}."
			}

			spotlightPage(entityStack(OtherUtil.getPotionStack(ModPotions.CELL_GROWTH), EntityType.BLAZE)) {
				text = "A ${pageLink(ItemsPatchouliCategory.CELL, "Cell")} can be brewed into the Potion of Cell Growth to ${minor("set the Potion to the Cell's entity type")}."
			}

			spotlightPage(gmoCellStack(registries, ModGenes.BIOLUMINESCENCE, EntityType.BLAZE)) {
				linkRecipe = true
				text = "From there, certain entity types have recipes to make ${major("Genetically Modified Cells")}, which are guaranteed to give a specific Gene.\$(br2)However, there's a catch. They can \$(o)only/\$ be crafted in an ${pageLink(BlocksPatchouliCategory.INCUBATOR_ADVANCED, "Advanced Incubator")} at low temperature, and there's ${bad("a chance of failure")}."
			}

			textPage(
				text = "A low-temperature Advanced Incubator takes a very long time, but can be accelerated using Overclockers.\$(br2)Unfortunately, ${bad("Overclockers lower your chances")} of success. ${minor("Chorus Fruits")} increase it back, though!\$(br2)Process the GM Cell through a ${pageLink(BlocksPatchouliCategory.DNA_EXTRACTOR, "DNA Extractor")} to get the set Helix."
			)
		}

		entry(
			saveName = POTION_ORGANIC_SUBSTRATE.localSaveName,
			category = category,
			name = "Organic Substrate",
			icon = Items.POTION
		) {
			sortNumber = 11

			textPage(
				text = "${major("Organic Substrate")} is the first of the mod's potions.\$(br2)Organic Substrate has no effect when drunk, but is used in some recipes.\$(br2)If a ${pageLink(ItemsPatchouliCategory.CELL, "Cell")} is brewed into Organic Substrate, the Substrate is replaced with a copy of the Cell. Given that you can have 3 Potions in a single brew, this allows you to triple Cells, if needed."
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.SUBSTRATE)) {
				text = "Additionally, Organic Substrate is used to brew \$(l:geneticsresequenced:items/cell_growth)Potions of Cell Growth/\$.\$(br2)You may prefer to use the ${pageLink(BlocksPatchouliCategory.INCUBATOR, "Incubator")} to brew these recipes, rather than the Brewing Stand."
			}
		}

		entry(
			saveName = POTION_PANACEA.localSaveName,
			category = category,
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

		entry(
			saveName = POTION_VIRAL_AGENTS.localSaveName,
			category = category,
			name = "Viral Agents",
			icon = Items.POTION
		) {
			sortNumber = 14

			textPage(
				text = "${major("Viral Agents")} are the final crafting potion of the mod. They are used to craft ${pageLink(NegativeGenesPatchouliCategory.CATEGORY, "negative Genes")}, which are generally just any Gene that's harmful.\$(br2)Negative Genes are ${minor("always lost on death")}, regardless of config setting. Additionally, there's a config to prevent players from even being able to \$(o)get/\$ negative Genes in the first place."
			)

			spotlightPage(OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)) {
				text = "Virus Cultivation recipes can be crafted in the Brewing Stand or in either Incubator."
			}
		}

		entry(
			saveName = POTION_ZOMBIFY_VILLAGER.localSaveName,
			category = category,
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

		entry(
			saveName = SCRAPER.localSaveName,
			category = category,
			name = "Scraper",
			icon = ModItems.SCRAPER.get()
		) {
			sortNumber = 1

			textPage(
				text = "The ${major("Scraper")} is used to get ${pageLink(ItemsPatchouliCategory.ORGANIC_MATTER, "Organic Matter")} from mobs.\$(br2)To use it, simply right-click the mob. This damages the mob, which counts as an attack and will anger neutral entities."
			)

			spotlightPage(ModItems.SCRAPER.get()) {
				linkRecipe = true
				text = "Scrapers can be enchanted with ${minor("Delicate Touch")}, which prevents damaging and angering entities."
			}
		}

		entry(
			saveName = SYRINGE.localSaveName,
			category = category,
			name = "Syringe",
			icon = ModItems.SYRINGE.get()
		) {
			sortNumber = 6

			textPage(
				text = "The ${major("Syringe")} is used to ${minor("extract and inject blood")}. Simply hold right-click, and the process will end automatically.\$(br2)The reason you'd \$(o)want/\$ to extract blood is that that's what carries completed ${pageLink(ItemsPatchouliCategory.PLASMID, "Plasmids")} back into your body."
			)

			spotlightPage(ModItems.SYRINGE.get()) {
				linkRecipe = true
				text = "When you extract blood, it'll be ${bad("contaminated")}. You'll need to clean it up in the ${pageLink(BlocksPatchouliCategory.BLOOD_PURIFIER, "Blood Purifier")} before it can be used.\$(br2)Plasmids are added in the ${pageLink(BlocksPatchouliCategory.PLASMID_INJECTOR, "Plasmid Injector")}."
			}

			spotlightPage(ModItems.METAL_SYRINGE.get()) {
				linkRecipe = true
				text = "You can also use the ${major("Metal Syringe")}, which targets others instead of yourself!\$(br2)This is how you would give Genes to entities."
			}
		}
	}

	companion object {

		lateinit var BOOK_CATEGORY: PatchouliBookCategory
			private set

		val CATEGORY = PatchouliBookReference("items")
		val ANTI_FIELD_ORB = PatchouliBookReference("items/anti_field_orb")
		val CELL = PatchouliBookReference("items/cell")
		val DNA_HELIX = PatchouliBookReference("items/dna_helix")
		val DRAGON_HEALTH_CRYSTAL = PatchouliBookReference("items/dragon_health_crystal")
		val GENE_CHECKER = PatchouliBookReference("items/gene_checker")
		val ORGANIC_MATTER = PatchouliBookReference("items/organic_matter")
		val OVERCLOCKER = PatchouliBookReference("items/overclocker")
		val PLASMID = PatchouliBookReference("items/plasmid")
		val POTION_MUTATION = PatchouliBookReference("items/potion_mutation")
		val POTION_OF_CELL_GROWTH = PatchouliBookReference("items/potion_of_cell_growth")
		val POTION_ORGANIC_SUBSTRATE = PatchouliBookReference("items/potion_organic_substrate")
		val POTION_PANACEA = PatchouliBookReference("items/potion_panacea")
		val POTION_VIRAL_AGENTS = PatchouliBookReference("items/potion_viral_agents")
		val POTION_ZOMBIFY_VILLAGER = PatchouliBookReference("items/potion_zombify_villager")
		val SCRAPER = PatchouliBookReference("items/scraper")
		val SYRINGE = PatchouliBookReference("items/syringe")
	}
}