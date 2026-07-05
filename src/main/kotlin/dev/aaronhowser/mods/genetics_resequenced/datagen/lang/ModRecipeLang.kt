package dev.aaronhowser.mods.genetics_resequenced.datagen.lang

object ModRecipeLang {

	fun add(provider: ModLanguageProvider) {
		provider.add(MOB, "Mob: %1\$s")
		provider.add(GENE, "Gene: %1\$s")
		provider.add(CHANCE, "Chance: %d%%")
		provider.add(REQUIRES_POINTS, "%1\$s requires %2\$d points of DNA")
		provider.add(BASIC_WORTH, "Basic Genes = 1 point")
		provider.add(MATCHING_WORTH, "Matching Genes = 2 points")
		provider.add(INJECTOR_GENES, "Syringes can have as many Genes as you want!")
		provider.add(INJECTOR_ANTIGENES, "Syringes can have as many Genes (or Anti-Genes) as you want!")
		provider.add(BLACK_DEATH, "Requires all negative Genes")
		provider.add(SUBSTRATE, "Duplicates the Cell for each Organic Substrate used")

		provider.add(BLOOD_PURIFIER, "Blood Purifier")
		provider.add(CELL_ANALYZER, "Cell Analyzer")
		provider.add(DNA_EXTRACTOR, "DNA Extractor")
		provider.add(DNA_DECRYPTOR, "DNA Decryptor")
		provider.add(PLASMID_INFUSER, "Plasmid Infuser")
		provider.add(PLASMID_INJECTOR, "Plasmid Injector")
		provider.add(INCUBATOR, "Incubator")
		provider.add(GMO, "GMO Cell Incubating")
		provider.add(SET_ENTITY, "Set Potion Entity")
		provider.add(SUBSTRATE_DUPE, "Substrate Cell Duplication")
		provider.add(VIRUS, "Virus Cultivation")

		provider.add(EMI.SYRINGES, "Syringes")
		provider.add(EMI.DELICATE_TOUCH_TAG, "Delicate Touch Enchantable")
		provider.add(EMI.FIREBALL_TAG, "Usable for \"Shoot Fireballs\" Gene")
		provider.add(EMI.MAGNET_BLACKLIST_TAG, "Blacklisted from \"Item Magnet\" Gene")
		provider.add(EMI.BLOCKS_MOB_INTERACTION_TAG, "Prevents item interaction with entities that have the #genetics_resequenced:allows_preventing_interaction tag\nUsed for Villagers etc that already have their own interactions")

		provider.add(EMI.BLOOD_PURIFIER, "Blood Purifier")
		provider.add(EMI.CELL_ANALYZER, "Cell Analyzer")
		provider.add(EMI.DNA_EXTRACTOR, "DNA Extractor")
		provider.add(EMI.DNA_DECRYPTOR, "DNA Decryptor")
		provider.add(EMI.PLASMID_INFUSER, "Plasmid Infuser")
		provider.add(EMI.PLASMID_INJECTOR, "Plasmid Injector")
		provider.add(EMI.INCUBATOR, "Incubator")
		provider.add(EMI.CELL_DUPE, "Cell Duplication")
		provider.add(EMI.SET_ENTITY, "Set Potion Entity")
		provider.add(EMI.VIRUS, "Virus Cultivation")
		provider.add(EMI.GMO, "GMO Cell Incubating")
	}

	const val MOB = "recipe.genetics_resequenced.mob_gene.mob"
	const val GENE = "recipe.genetics_resequenced.mob_gene.gene"
	const val CHANCE = "recipe.genetics_resequenced.mob_gene.chance"
	const val REQUIRES_POINTS = "recipe.genetics_resequenced.plasmid_infuser.points_required"
	const val BASIC_WORTH = "recipe.genetics_resequenced.plasmid_infuser.basic"
	const val MATCHING_WORTH = "recipe.genetics_resequenced.plasmid_infuser.matching"
	const val INJECTOR_GENES = "recipe.genetics_resequenced.plasmid_injector.genes"
	const val INJECTOR_ANTIGENES = "recipe.genetics_resequenced.plasmid_injector.anti_genes"
	const val SUBSTRATE = "recipe.genetics_resequenced.substrate"
	const val BLACK_DEATH = "recipe.genetics_resequenced.black_death"

	const val BLOOD_PURIFIER = "recipe.category.genetics_resequenced.blood_purifier"
	const val CELL_ANALYZER = "recipe.category.genetics_resequenced.cell_analyzer"
	const val DNA_EXTRACTOR = "recipe.category.genetics_resequenced.dna_extractor"
	const val DNA_DECRYPTOR = "recipe.category.genetics_resequenced.dna_decryptor"
	const val PLASMID_INFUSER = "recipe.category.genetics_resequenced.plasmid_infuser"
	const val PLASMID_INJECTOR = "recipe.category.genetics_resequenced.plasmid_injector"
	const val INCUBATOR = "recipe.category.genetics_resequenced.incubator"

	const val GMO = "recipe.category.genetics_resequenced.gmo"
	const val SUBSTRATE_DUPE = "recipe.category.genetics_resequenced.cell_dupe"
	const val SET_ENTITY = "recipe.category.genetics_resequenced.set_entity"
	const val VIRUS = "recipe.category.genetics_resequenced.virus"

	object EMI {
		const val SYRINGES = "tag.item.genetics_resequenced.syringes"
		const val DELICATE_TOUCH_TAG = "tag.item.genetics_resequenced.enchantable.delicate_touch"
		const val FIREBALL_TAG = "tag.item.genetics_resequenced.activates_shoot_fireball_gene"
		const val MAGNET_BLACKLIST_TAG = "tag.item.genetics_resequenced.item_magnet_gene_blacklist"
		const val BLOCKS_MOB_INTERACTION_TAG = "tag.item.genetics_resequenced.prevents_some_mob_interaction"

		private fun emiCategory(name: String) = "emi.category.genetics_resequenced.$name"
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