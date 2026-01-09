package dev.aaronhowser.mods.geneticsresequenced.datagen.lang

object ModMessageLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(SCRAPER_CANT_SCRAPE, "%s cannot be scraped.")
			add(CANT_SET_ENTITY, "Cannot set to this entity.")
			add(DEATH_GENE_REMOVAL, "Death has reset your Genes!")
			add(DEATH_NEGATIVE_GENE_REMOVAL, "Death has remove your negative Genes!")
			add(RECENT_WOOLY, "This entity has already been sheared recently!")
			add(RECENT_MEATY, "This entity has already been meated recently!")
			add(RECENT_MILKY, "This entity has already been milked recently!")
			add(MILK_MILKED, "You have been milked!")
			add(SYRINGE_INJECTED, "You have gained the %1\$s Gene!")
			add(SYRINGE_FAILED, "You have failed to gain the %1\$s Gene!")
			add(SYRINGE_CONTAMINATED, "You can't inject yourself with contaminated blood!")
			add(METAL_SYRINGE_MISMATCH, "This Syringe is for a different entity!")
			add(METAL_SYRINGE_CONTAMINATED, "You can't inject entities with contaminated blood!")
			add(METAL_SYRINGE_NO_MOBS, "Mobs cannot have the %1\$s Gene!")
			add(SUPPORT_SLIME_CREATIVE, "Support Slimes despawn with no owner! Give yourself the %1\$s Gene to stop them from despawning!")
			add(SUPPORT_SLIME_PEACEFUL, "Support slimes are technically Slimes, which means they can't exist in Peaceful mode!")
			add(SYRINGE_REMOVE_GENES_SUCCESS, "You have removed the %s Gene!")
			add(SYRINGE_REMOVE_GENES_FAIL, "The %s Gene was not removed as you did not have it!")
			add(MISSING_GENE_REQUIREMENTS_1, "You feel the %s Gene fade away...")
			add(MISSING_GENE_REQUIREMENTS_2, "It seems you §c[do not meet the requirements]§r for it.")
			add(MISSING_GENE_REQUIREMENTS_LIST, "Required Genes:\n")
			add(CRINGE_GRASS, "With the touch of Grass, you feel the cringe leave your body.")
			add(CRINGE_ADDED, "You feel the cringe entering your body, taking over. Your perception of the world changes in %d...")
			add(CRINGE_REMOVED, "You feel yourself become more based, the cringe leaving the world in %d...")
			add(CRINGE_CONFIG, "You can disable this in the client config!\n\nYour language is reset to normal when you leave the game.")
			add(CRINGE_RELOADING, "Reloading resources now!")
			add(SLIME_SPAM, "%s's Slime %d")
			add(GENE_CHECKER_SELF_NO_GENES, "You have no Genes!")
			add(GENE_CHECKER_SELF_LIST, "You have the following Genes:\n%s")
			add(GENE_CHECKER_TARGET_NO_GENES, "%s has no Genes!")
			add(GENE_CHECKER_TARGET_LIST, "%s has the following Genes:\n%s")
			add(GENE_CHECKER_POSSIBLE_GENES, "%s can provide the following Genes:\n%s")
			add(GENE_CHECKER_NO_POSSIBLE_GENES, "%s cannot provide any Genes")
			add(GENE_CHECKER_SELF_TEMPORARY_LIST, "You have the following temporary Genes:\n%s")
			add(GENE_CHECKER_TARGET_TEMPORARY_LIST, "%s has the following temporary Genes:\n%s")
			add(GENE_WEIGHT, "%s with weight %d")
			add(ADVANCED_INCUBATOR_HIGH_TEMP, "Temp: HIGH")
			add(ADVANCED_INCUBATOR_LOW_TEMP, "Temp: LOW")
		}
	}

	const val SCRAPER_CANT_SCRAPE = "message.geneticsresequenced.scraper.cant_scrape"
	const val CANT_SET_ENTITY = "message.geneticsresequenced.cant_set_entity"
	const val DEATH_GENE_REMOVAL = "message.geneticsresequenced.death_gene_removal"
	const val DEATH_NEGATIVE_GENE_REMOVAL = "message.geneticsresequenced.death_negative_gene_removal"
	const val RECENT_WOOLY = "message.geneticsresequenced.wooly.recent"
	const val RECENT_MEATY = "message.geneticsresequenced.meaty.recent"
	const val RECENT_MILKY = "message.geneticsresequenced.milk.recent"
	const val MILK_MILKED = "message.geneticsresequenced.milk.milked"
	const val SYRINGE_INJECTED = "message.geneticsresequenced.syringe.injected"
	const val SYRINGE_FAILED = "message.geneticsresequenced.syringe.failed"
	const val SYRINGE_CONTAMINATED = "message.geneticsresequenced.syringe.contaminated"
	const val METAL_SYRINGE_MISMATCH = "message.geneticsresequenced.metal_syringe.mismatch"
	const val METAL_SYRINGE_CONTAMINATED = "message.geneticsresequenced.metal_syringe.contaminated"
	const val METAL_SYRINGE_NO_MOBS = "message.geneticsresequenced.metal_syringe.no_mobs"
	const val SUPPORT_SLIME_CREATIVE = "message.geneticsresequenced.support_slime_creative"
	const val SUPPORT_SLIME_PEACEFUL = "message.geneticsresequenced.support_slime_peaceful"
	const val SYRINGE_REMOVE_GENES_SUCCESS = "message.geneticsresequenced.syringe.anti_gene.success"
	const val SYRINGE_REMOVE_GENES_FAIL = "message.geneticsresequenced.syringe.anti_gene.fail"
	const val MISSING_GENE_REQUIREMENTS_1 = "message.geneticsresequenced.gene_missing_requirements.1"
	const val MISSING_GENE_REQUIREMENTS_2 = "message.geneticsresequenced.gene_missing_requirements.2"
	const val MISSING_GENE_REQUIREMENTS_LIST = "message.geneticsresequenced.gene_missing_requirements.list"
	const val CRINGE_GRASS = "message.geneticsresequenced.cringe.cured"
	const val CRINGE_ADDED = "message.geneticsresequenced.cringe.resources.add"
	const val CRINGE_REMOVED = "message.geneticsresequenced.cringe.resources.remove"
	const val CRINGE_CONFIG = "message.geneticsresequenced.cringe.resources.tooltip"
	const val CRINGE_RELOADING = "message.geneticsresequenced.cringe.resources.reloading"
	const val SLIME_SPAM = "message.geneticsresequenced.slimy_spam"
	const val GENE_CHECKER_SELF_NO_GENES = "message.geneticsresequenced.gene_checker.no_genes"
	const val GENE_CHECKER_SELF_LIST = "message.geneticsresequenced.gene_checker.list"
	const val GENE_CHECKER_TARGET_NO_GENES = "message.geneticsresequenced.gene_checker.target.no_genes"
	const val GENE_CHECKER_TARGET_LIST = "message.geneticsresequenced.gene_checker.target.list"
	const val GENE_CHECKER_SELF_NO_TEMPORARY_GENES = "message.geneticsresequenced.gene_checker.no_temporary_genes"
	const val GENE_CHECKER_SELF_TEMPORARY_LIST = "message.geneticsresequenced.gene_checker.temporary_list"
	const val GENE_CHECKER_TARGET_NO_TEMPORARY_GENES = "message.geneticsresequenced.gene_checker.target.no_temporary_genes"
	const val GENE_CHECKER_TARGET_TEMPORARY_LIST = "message.geneticsresequenced.gene_checker.target.temporary_list"
	const val GENE_CHECKER_POSSIBLE_GENES = "message.geneticsresequenced.gene_checker.target.possible_genes"
	const val GENE_CHECKER_NO_POSSIBLE_GENES = "message.geneticsresequenced.gene_checker.target.no_possible_genes"
	const val GENE_WEIGHT = "message.geneticsresequenced.gene_weight"
	const val ADVANCED_INCUBATOR_HIGH_TEMP = "message.geneticsresequenced.advanced_incubator.high_temperature"
	const val ADVANCED_INCUBATOR_LOW_TEMP = "message.geneticsresequenced.advanced_incubator.low_temperature"

}