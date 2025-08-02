package dev.aaronhowser.mods.geneticsresequenced.datagen.lang

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

}