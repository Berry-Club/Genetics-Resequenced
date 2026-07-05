package dev.aaronhowser.mods.genetics_resequenced.datagen.lang

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
			add(DEATH_SCRAPER, "%s was scraped to the bone")
			add(DEATH_SYRINGE, "%s died of blood loss")
			add(DEATH_SYRINGE_PICKUP, "%s stepped on a loose Syringe")
			add(BLEED_DEATH, "%s bled out!")
			add(VIRUS_DEATH, "%s succumbed to a Virus!")
			add(COOLDOWN_STARTED, "%s went on cooldown for %s seconds")
			add(COOLDOWN_ENDED, "%s has come off cooldown!")
			add(ALREADY_ON_COOLDOWN, "%s will be on cooldown for %s")

			add(Commands.LIST_ALL_GENES, "Gene List:\n")
			add(Commands.NO_GENES, "No Genes found!")
			add(Commands.TARGET_GENE_LIST, "%1\$s's Genes:\n")
			add(Commands.TEMPORARY_GENE_LIST, "%1\$s's Temporary Genes:\n")
			add(TEMPORARY_GENE_WITH_DURATION, "%1\$s (%2\$d ticks)")

			add(Commands.REMOVED_LIGHTS, "Removed %d nearby Bioluminescence Gene light sources.")
			add(Commands.REMOVED_LIGHTS_RANGE_TOO_HIGH, "Range too high! Must be between 1 and 100.")

			add(Commands.ADD_SINGLE_SUCCESS, "Added %1\$s to %2\$s!")
			add(Commands.ADD_SINGLE_FAIL, "Failed to add %1\$s to %2\$s!")
			add(Commands.ADD_MULTIPLE_SUCCESS, "Added %1\$s to %2\$d entities!")
			add(Commands.ADD_MULTIPLE_FAIL, "Failed to add %1\$s to %2\$d entities!")
			add(Commands.ADD_ALL_SINGLE, "Added all positive Genes to %s!")
			add(Commands.ADD_ALL_MULTIPLE, "Added all positive Genes to %d entities!")

			add(Commands.TEMPORARY_ADD_SINGLE_SUCCESS, "Temporarily added %1\$s to %2\$s for %3\$s ticks!")
			add(Commands.TEMPORARY_ADD_SINGLE_FAIL, "Failed to temporarily add %1\$s to %2\$s ticks.")
			add(Commands.TEMPORARY_ADD_MULTIPLE_SUCCESS, "Temporarily added %1\$s to %2\$d entities for %3\$s ticks!")
			add(Commands.TEMPORARY_ADD_MULTIPLE_FAIL, "Failed to temporarily add %1\$s to %2\$d entities for %3\$s ticks.")

			add(Commands.REMOVE_MULTIPLE_SUCCESS, "Removed %1\$s from %2\$d entities!")
			add(Commands.REMOVE_MULTIPLE_FAIL, "Failed to remove %1\$s from %2\$d entities!")
			add(Commands.REMOVE_SINGLE_SUCCESS, "Removed %1\$s from %2\$s!")
			add(Commands.REMOVE_SINGLE_FAIL, "Failed to remove %1\$s from %2\$s!")
			add(Commands.REMOVE_ALL_SINGLE, "Removed all Genes from %s!")
			add(Commands.REMOVE_ALL_MULTIPLE, "Removed all Genes from %d entities!")
		}
	}

	const val DEATH_SCRAPER = "death.attack.gr_scraper"
	const val DEATH_SYRINGE = "death.attack.gr_syringe"
	const val DEATH_SYRINGE_PICKUP = "death.attack.gr_syringe_pickup"
	const val BLEED_DEATH = "death.attack.gr_bleed"
	const val VIRUS_DEATH = "death.attack.gr_virus"
	const val SCRAPER_CANT_SCRAPE = "message.genetics_resequenced.scraper.cant_scrape"
	const val CANT_SET_ENTITY = "message.genetics_resequenced.cant_set_entity"
	const val DEATH_GENE_REMOVAL = "message.genetics_resequenced.death_gene_removal"
	const val DEATH_NEGATIVE_GENE_REMOVAL = "message.genetics_resequenced.death_negative_gene_removal"
	const val RECENT_WOOLY = "message.genetics_resequenced.wooly.recent"
	const val RECENT_MEATY = "message.genetics_resequenced.meaty.recent"
	const val RECENT_MILKY = "message.genetics_resequenced.milk.recent"
	const val MILK_MILKED = "message.genetics_resequenced.milk.milked"
	const val SYRINGE_INJECTED = "message.genetics_resequenced.syringe.injected"
	const val SYRINGE_FAILED = "message.genetics_resequenced.syringe.failed"
	const val SYRINGE_CONTAMINATED = "message.genetics_resequenced.syringe.contaminated"
	const val METAL_SYRINGE_MISMATCH = "message.genetics_resequenced.metal_syringe.mismatch"
	const val METAL_SYRINGE_CONTAMINATED = "message.genetics_resequenced.metal_syringe.contaminated"
	const val METAL_SYRINGE_NO_MOBS = "message.genetics_resequenced.metal_syringe.no_mobs"
	const val SUPPORT_SLIME_CREATIVE = "message.genetics_resequenced.support_slime_creative"
	const val SUPPORT_SLIME_PEACEFUL = "message.genetics_resequenced.support_slime_peaceful"
	const val SYRINGE_REMOVE_GENES_SUCCESS = "message.genetics_resequenced.syringe.anti_gene.success"
	const val SYRINGE_REMOVE_GENES_FAIL = "message.genetics_resequenced.syringe.anti_gene.fail"
	const val MISSING_GENE_REQUIREMENTS_1 = "message.genetics_resequenced.gene_missing_requirements.1"
	const val MISSING_GENE_REQUIREMENTS_2 = "message.genetics_resequenced.gene_missing_requirements.2"
	const val MISSING_GENE_REQUIREMENTS_LIST = "message.genetics_resequenced.gene_missing_requirements.list"
	const val CRINGE_GRASS = "message.genetics_resequenced.cringe.cured"
	const val CRINGE_ADDED = "message.genetics_resequenced.cringe.resources.add"
	const val CRINGE_REMOVED = "message.genetics_resequenced.cringe.resources.remove"
	const val CRINGE_CONFIG = "message.genetics_resequenced.cringe.resources.tooltip"
	const val CRINGE_RELOADING = "message.genetics_resequenced.cringe.resources.reloading"
	const val SLIME_SPAM = "message.genetics_resequenced.slimy_spam"
	const val GENE_CHECKER_SELF_NO_GENES = "message.genetics_resequenced.gene_checker.no_genes"
	const val GENE_CHECKER_SELF_LIST = "message.genetics_resequenced.gene_checker.list"
	const val GENE_CHECKER_TARGET_NO_GENES = "message.genetics_resequenced.gene_checker.target.no_genes"
	const val GENE_CHECKER_TARGET_LIST = "message.genetics_resequenced.gene_checker.target.list"
	const val GENE_CHECKER_SELF_NO_TEMPORARY_GENES = "message.genetics_resequenced.gene_checker.no_temporary_genes"
	const val GENE_CHECKER_SELF_TEMPORARY_LIST = "message.genetics_resequenced.gene_checker.temporary_list"
	const val GENE_CHECKER_TARGET_NO_TEMPORARY_GENES = "message.genetics_resequenced.gene_checker.target.no_temporary_genes"
	const val GENE_CHECKER_TARGET_TEMPORARY_LIST = "message.genetics_resequenced.gene_checker.target.temporary_list"
	const val GENE_CHECKER_POSSIBLE_GENES = "message.genetics_resequenced.gene_checker.target.possible_genes"
	const val GENE_CHECKER_NO_POSSIBLE_GENES = "message.genetics_resequenced.gene_checker.target.no_possible_genes"
	const val GENE_WEIGHT = "message.genetics_resequenced.gene_weight"
	const val ADVANCED_INCUBATOR_HIGH_TEMP = "message.genetics_resequenced.advanced_incubator.high_temperature"
	const val ADVANCED_INCUBATOR_LOW_TEMP = "message.genetics_resequenced.advanced_incubator.low_temperature"
	const val COOLDOWN_STARTED = "message.genetics_resequenced.cooldown_started"
	const val COOLDOWN_ENDED = "message.genetics_resequenced.cooldown_ended"
	const val ALREADY_ON_COOLDOWN = "message.genetics_resequenced.already_on_cooldown"
	const val TEMPORARY_GENE_WITH_DURATION = "command.genetics_resequenced.list.temporary_genes_with_duration"

	object Commands {
		const val LIST_ALL_GENES = "command.genetics_resequenced.list_all_genes"
		const val NO_GENES = "command.genetics_resequenced.list.no_genes"
		const val TARGET_GENE_LIST = "command.genetics_resequenced.list.genes"
		const val TEMPORARY_GENE_LIST = "command.genetics_resequenced.list.temporary_genes"
		const val REMOVED_LIGHTS = "command.genetics_resequenced.remove_nearby_lights.success"
		const val REMOVED_LIGHTS_RANGE_TOO_HIGH = "command.genetics_resequenced.remove_nearby_lights.range_too_high"
		const val ADD_SINGLE_SUCCESS = "command.genetics_resequenced.add_gene.single_target.success"
		const val ADD_SINGLE_FAIL = "command.genetics_resequenced.add_gene.single_target.fail"
		const val ADD_MULTIPLE_SUCCESS = "command.genetics_resequenced.add_gene.multiple_targets.success"
		const val ADD_MULTIPLE_FAIL = "command.genetics_resequenced.add_gene.multiple_targets.fail"
		const val TEMPORARY_ADD_SINGLE_SUCCESS = "command.genetics_resequenced.temporary_add_gene.single_target.success"
		const val TEMPORARY_ADD_SINGLE_FAIL = "command.genetics_resequenced.temporary_add_gene.single_target.fail"
		const val TEMPORARY_ADD_MULTIPLE_SUCCESS = "command.genetics_resequenced.temporary_add_gene.multiple_targets.success"
		const val TEMPORARY_ADD_MULTIPLE_FAIL = "command.genetics_resequenced.temporary_add_gene.multiple_targets.fail"
		const val ADD_ALL_SINGLE = "command.genetics_resequenced.add_all.single_target"
		const val ADD_ALL_MULTIPLE = "command.genetics_resequenced.add_all.multiple_targets"
		const val REMOVE_MULTIPLE_SUCCESS = "command.genetics_resequenced.remove_gene.multiple_targets.success"
		const val REMOVE_MULTIPLE_FAIL = "command.genetics_resequenced.remove_gene.multiple_targets.fail"
		const val REMOVE_SINGLE_SUCCESS = "command.genetics_resequenced.remove_gene.single_target.success"
		const val REMOVE_SINGLE_FAIL = "command.genetics_resequenced.remove_gene.single_target.fail"
		const val REMOVE_ALL_SINGLE = "command.genetics_resequenced.remove_all.single_target"
		const val REMOVE_ALL_MULTIPLE = "command.genetics_resequenced.remove_all.multiple_targets"
	}

}