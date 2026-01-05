package dev.aaronhowser.mods.geneticsresequenced.datagen.lang

object ModRecipeLang {

	fun add(provider: ModLanguageProvider) {
		
		provider.apply {
			add(MOB, "Mob: %1\$s")
			add(GENE, "Gene: %1\$s")
			add(CHANCE, "Chance: %d%%")
			add(REQUIRES_POINTS, "%1\$s requires %2\$d points of DNA")
			add(BASIC_WORTH, "Basic Genes = 1 point")
			add(MATCHING_WORTH, "Matching Genes = 2 points")
			add(INJECTOR_GENES, "Syringes can have as many Genes as you want!")
			add(INJECTOR_ANTIGENES, "Syringes can have as many Genes (or Anti-Genes) as you want!")
			add(BLACK_DEATH, "Requires all negative Genes")
			add(SUBSTRATE, "Duplicates the Cell for each Organic Substrate used")

			add(BLOOD_PURIFIER, "Blood Purifier")
			add(CELL_ANALYZER, "Cell Analyzer")
			add(DNA_EXTRACTOR, "DNA Extractor")
			add(DNA_DECRYPTOR, "DNA Decryptor")
			add(PLASMID_INFUSER, "Plasmid Infuser")
			add(PLASMID_INJECTOR, "Plasmid Injector")
			add(INCUBATOR, "Incubator")
			add(GMO, "GMO Cell Incubating")
			add(SET_ENTITY, "Set Potion Entity")
			add(SUBSTRATE_DUPE, "Substrate Cell Duplication")
			add(VIRUS, "Virus Cultivation")

			add(EMI.SYRINGES, "Syringes")
			add(EMI.DELICATE_TOUCH_TAG, "Delicate Touch Enchantable")
			add(EMI.FIREBALL_TAG, "Usable for \"Shoot Fireballs\" Gene")
			add(EMI.MAGNET_BLACKLIST_TAG, "Blacklisted from \"Item Magnet\" Gene")
			add(EMI.BLOCKS_MOB_INTERACTION_TAG, "Prevents item interaction with entities that have the #geneticsresequenced:allows_preventing_interaction tag\nUsed for Villagers etc that already have their own interactions")

			add(EMI.BLOOD_PURIFIER, "Blood Purifier")
			add(EMI.CELL_ANALYZER, "Cell Analyzer")
			add(EMI.DNA_EXTRACTOR, "DNA Extractor")
			add(EMI.DNA_DECRYPTOR, "DNA Decryptor")
			add(EMI.PLASMID_INFUSER, "Plasmid Infuser")
			add(EMI.PLASMID_INJECTOR, "Plasmid Injector")
			add(EMI.INCUBATOR, "Incubator")
			add(EMI.CELL_DUPE, "Cell Duplication")
			add(EMI.SET_ENTITY, "Set Potion Entity")
			add(EMI.VIRUS, "Virus Cultivation")
			add(EMI.GMO, "GMO Cell Incubating")
		}

	}

	const val MOB = "recipe.geneticsresequenced.mob_gene.mob"
	const val GENE = "recipe.geneticsresequenced.mob_gene.gene"
	const val CHANCE = "recipe.geneticsresequenced.mob_gene.chance"
	const val REQUIRES_POINTS = "recipe.geneticsresequenced.plasmid_infuser.points_required"
	const val BASIC_WORTH = "recipe.geneticsresequenced.plasmid_infuser.basic"
	const val MATCHING_WORTH = "recipe.geneticsresequenced.plasmid_infuser.matching"
	const val INJECTOR_GENES = "recipe.geneticsresequenced.plasmid_injector.genes"
	const val INJECTOR_ANTIGENES = "recipe.geneticsresequenced.plasmid_injector.anti_genes"
	const val SUBSTRATE = "recipe.geneticsresequenced.substrate"
	const val BLACK_DEATH = "recipe.geneticsresequenced.black_death"

	const val BLOOD_PURIFIER = "recipe.category.geneticsresequenced.blood_purifier"
	const val CELL_ANALYZER = "recipe.category.geneticsresequenced.cell_analyzer"
	const val DNA_EXTRACTOR = "recipe.category.geneticsresequenced.dna_extractor"
	const val DNA_DECRYPTOR = "recipe.category.geneticsresequenced.dna_decryptor"
	const val PLASMID_INFUSER = "recipe.category.geneticsresequenced.plasmid_infuser"
	const val PLASMID_INJECTOR = "recipe.category.geneticsresequenced.plasmid_injector"
	const val INCUBATOR = "recipe.category.geneticsresequenced.incubator"

	const val GMO = "recipe.category.geneticsresequenced.gmo"
	const val SUBSTRATE_DUPE = "recipe.category.geneticsresequenced.cell_dupe"
	const val SET_ENTITY = "recipe.category.geneticsresequenced.set_entity"
	const val VIRUS = "recipe.category.geneticsresequenced.virus"

	object EMI {
		const val SYRINGES = "tag.item.geneticsresequenced.syringes"
		const val DELICATE_TOUCH_TAG = "tag.item.geneticsresequenced.enchantable.delicate_touch"
		const val FIREBALL_TAG = "tag.item.geneticsresequenced.activates_shoot_fireball_gene"
		const val MAGNET_BLACKLIST_TAG = "tag.item.geneticsresequenced.item_magnet_gene_blacklist"
		const val BLOCKS_MOB_INTERACTION_TAG = "tag.item.geneticsresequenced.prevents_some_mob_interaction"

		private fun emiCategory(name: String) = "emi.category.geneticsresequenced.$name"
		val BLOOD_PURIFIER = emiCategory("blood_purifier")
		val CELL_ANALYZER = emiCategory("cell_analyzer")
		val DNA_EXTRACTOR = emiCategory("dna_extractor")
		val DNA_DECRYPTOR = emiCategory("dna_decryptor")
		val PLASMID_INFUSER = emiCategory("plasmid_infuser")
		val PLASMID_INJECTOR = emiCategory("plasmid_injector")
		val INCUBATOR = emiCategory("incubator")
		val CELL_DUPE = emiCategory("cell_dupe")
		val SET_ENTITY = emiCategory("set_entity")
		val VIRUS = emiCategory("virus")
		val GMO = emiCategory("gmo")

	}

}