package dev.aaronhowser.mods.geneticsresequenced.datagen.lang

object ModMessageLang {

	fun add(provider: ModLanguageProvider) {
		provider.add(SCRAPER_CANT_SCRAPE, "%s cannot be scraped.")
		provider.add(CANT_SET_ENTITY, "Cannot set to this entity.")
		provider.add(DEATH_GENE_REMOVAL, "Death has reset your Genes!")
		provider.add(DEATH_NEGATIVE_GENE_REMOVAL, "Death has remove your negative Genes!")
		provider.add(RECENT_WOOLY, "This entity has already been sheared recently!")
		provider.add(RECENT_MEATY, "This entity has already been meated recently!")
		provider.add(RECENT_MILKY, "This entity has already been milked recently!")
		provider.add(MILK_MILKED, "You have been milked!")
		provider.add(SYRINGE_INJECTED, "You have gained the %1\$s Gene!")
		provider.add(SYRINGE_FAILED, "You have failed to gain the %1\$s Gene!")
		provider.add(SYRINGE_CONTAMINATED, "You can't inject yourself with contaminated blood!")
		provider.add(METAL_SYRINGE_MISMATCH, "This Syringe is for a different entity!")
		provider.add(METAL_SYRINGE_CONTAMINATED, "You can't inject entities with contaminated blood!")
		provider.add(METAL_SYRINGE_NO_MOBS, "Mobs cannot have the %1\$s Gene!")
		provider.add(SUPPORT_SLIME_CREATIVE, "Support Slimes despawn with no owner! Give yourself the %1\$s Gene to stop them from despawning!")
		provider.add(SUPPORT_SLIME_PEACEFUL, "Support slimes are technically Slimes, which means they can't exist in Peaceful mode!")
		provider.add(SYRINGE_REMOVE_GENES_SUCCESS, "You have removed the %s Gene!")
		provider.add(SYRINGE_REMOVE_GENES_FAIL, "The %s Gene was not removed as you did not have it!")
		provider.add(MISSING_GENE_REQUIREMENTS, "You feel the %s Gene fade away...\nIt seems you §cdo not meet the requirements§r for it.")
		provider.add(MISSING_GENE_REQUIREMENTS_LIST, "Required Genes:\n")
		provider.add(CRINGE_GRASS, "With the touch of Grass, you feel the cringe leave your body.")
		provider.add(CRINGE_ADDED, "You feel the cringe entering your body, taking over. Your perception of the world changes in %d...")
		provider.add(CRINGE_REMOVED, "You feel yourself become more based, the cringe leaving the world in %d...")
		provider.add(CRINGE_CONFIG, "You can disable this in the client config!\n\nYour language is reset to normal when you leave the game.")
		provider.add(CRINGE_RELOADING, "Reloading resources now!")
		provider.add(SLIME_SPAM, "%s's Slime %d")
		provider.add(GENE_CHECKER_SELF_NO_GENES, "You have no Genes!")
		provider.add(GENE_CHECKER_SELF_LIST, "You have the following Genes:\n%s")
		provider.add(GENE_CHECKER_TARGET_NO_GENES, "%s has no Genes!")
		provider.add(GENE_CHECKER_TARGET_LIST, "%s has the following Genes:\n%s")
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
	const val MISSING_GENE_REQUIREMENTS = "message.geneticsresequenced.gene_missing_requirements"
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

}