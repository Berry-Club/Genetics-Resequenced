package dev.aaronhowser.mods.geneticsresequenced.datagen.lang

object ModTooltipLang {

	fun add(provider: ModLanguageProvider) {
		provider.add(GENE, "Gene: %1\$s")
		provider.add(HELIX_ENTITY, "Entity: %1\$s")
		provider.add(COPY_GENE, "Click to copy ID:\n%s")
		provider.add(ACTIVE, "Active")
		provider.add(INACTIVE, "Inactive")
		provider.add(CELL_MOB, "Cell type: %s")
		provider.add(CELL_NO_MOB, "Empty")
		provider.add(CELL_CREATIVE, "Creative: Right-click mob to set.")
		provider.add(PLASMID_EMPTY, "Empty Plasmid")
		provider.add(ANTI_PLASMID_EMPTY, "Empty Anti-Plasmid")
		provider.add(PLASMID_GENE, "Contains Gene: %s")
		provider.add(PLASMID_COMPLETE, "Plasmid is complete!")
		provider.add(PLASMID_PROGRESS, "DNA Points: %d/%d")
		provider.add(SYRINGE_CONTAMINATED, "Contaminated Blood")
		provider.add(SYRINGE_OWNER, "Blood: %1\$s")
		provider.add(INFUSER_BASIC, "+1 DNA Point")
		provider.add(INFUSER_MATCHING, "+2 DNA Points")
		provider.add(INFUSER_MISMATCH, "Not applicable")
		provider.add(INJECTOR_CONTAMINATED, "You can't inject Genes into Contaminated Blood!")
		provider.add(INFUSER_ANTI_PLASMID_1, "Anti-Plasmids are not set in the Plasmid Infuser!")
		provider.add(INFUSER_ANTI_PLASMID_2, "Craft it together with a completed Plasmid to set it.")
		provider.add(COAL_GEN_TOTAL_FE, "Stack total: %d FE")
		provider.add(IGNORE_POTION, "Don't craft this! It does nothing!")
		provider.add(GMO_CHANCE, "Chance: %d%%")
		provider.add(SUBSTRATE_RECIPE, "Each Organic Substrate is replaced with a copy of the Cell.")
		provider.add(INCUBATOR_SET_HIGH, "Set temperature to high")
		provider.add(INCUBATOR_SET_LOW, "Set temperature to low")
		provider.add(BLACK_DEATH_RECIPE, "Requires a Syringe that has every negative Gene.")
		provider.add(SYRINGE_ADDING_GENES, "Adding Genes:")
		provider.add(SYRINGE_REMOVING_GENES, "Removing:")
		provider.add(GMO_TEMPERATURE_REQUIREMENT, "Advanced Incubator §lmust be low temperature§r")
		provider.add(GMO_CHORUS, "Overclockers lower your chance, but Chorus Fruits increase it!")
		provider.add(ITEM_MAGNET_BLACKLIST, "Blacklisted from Item Magnet Gene")
		provider.add(FE, "%1\$s/%2\$s FE")
		provider.add(GMO_BASE_CHANCE, "%1\$s Gene base chance: %2\$d%%")
		provider.add(GMO_OVERCLOCKER_CHANCE, "%1\$d Overclockers lower chance to %2\$d%%")
		provider.add(GMO_CHORUS_CHANCE, "%1\$d Chorus Fruit increase chance to %2\$d%%")
		provider.add(GMO_SUCCESS, "Result on success")
		provider.add(GMO_FAILURE, "Result on failure")
	}

	const val GENE = "tooltip.geneticsresequenced.gene"
	const val HELIX_ENTITY = "tooltip.geneticsresequenced.helix_entity"
	const val COPY_GENE = "tooltip.geneticsresequenced.copy_gene_id"
	const val ACTIVE = "tooltip.geneticsresequenced.antifield_active"
	const val INACTIVE = "tooltip.geneticsresequenced.antifield_inactive"
	const val CELL_MOB = "tooltip.geneticsresequenced.dna_item.filled"
	const val CELL_NO_MOB = "tooltip.geneticsresequenced.dna_item.empty"
	const val CELL_CREATIVE = "tooltip.geneticsresequenced.dna_item.creative"
	const val PLASMID_EMPTY = "tooltip.geneticsresequenced.plasmid.empty"
	const val ANTI_PLASMID_EMPTY = "tooltip.geneticsresequenced.anti_plasmid.empty"
	const val PLASMID_GENE = "tooltip.geneticsresequenced.plasmid.gene"
	const val PLASMID_COMPLETE = "tooltip.geneticsresequenced.plasmid.complete"
	const val PLASMID_PROGRESS = "tooltip.geneticsresequenced.plasmid.amount"
	const val SYRINGE_CONTAMINATED = "tooltip.geneticsresequenced.syringe.contaminated"
	const val SYRINGE_OWNER = "tooltip.geneticsresequenced.syringe.blood_owner"
	const val INFUSER_BASIC = "tooltip.geneticsresequenced.plasmid_infuser.basic_gene"
	const val INFUSER_MATCHING = "tooltip.geneticsresequenced.plasmid_infuser.matching_gene"
	const val INFUSER_MISMATCH = "tooltip.geneticsresequenced.plasmid_infuser.different_gene"
	const val INFUSER_ANTI_PLASMID_1 = "tooltip.geneticsresequenced.plasmid_infuser.anti_plasmid_1"
	const val INFUSER_ANTI_PLASMID_2 = "tooltip.geneticsresequenced.plasmid_infuser.anti_plasmid_2"
	const val INJECTOR_CONTAMINATED = "tooltip.geneticsresequenced.plasmid_injector.contaminated"
	const val COAL_GEN_TOTAL_FE = "tooltip.geneticsresequenced.coal_generator.total_fe"
	const val IGNORE_POTION = "tooltip.geneticsresequenced.potion.ignore"
	const val GMO_CHANCE = "tooltip.geneticsresequenced.gmo_cell.chance"
	const val SUBSTRATE_RECIPE = "tooltip.geneticsresequenced.substrate_recipe"
	const val INCUBATOR_SET_HIGH = "tooltip.geneticsresequenced.advanced_incubator.set_high"
	const val INCUBATOR_SET_LOW = "tooltip.geneticsresequenced.advanced_incubator.set_low"
	const val BLACK_DEATH_RECIPE = "tooltip.geneticsresequenced.black_death_recipe"
	const val SYRINGE_ADDING_GENES = "tooltip.geneticsresequenced.syringe.adding"
	const val SYRINGE_REMOVING_GENES = "tooltip.geneticsresequenced.syringe.removing"
	const val GMO_TEMPERATURE_REQUIREMENT = "tooltip.geneticsresequenced.gmo_temperature_requirement"
	const val GMO_CHORUS = "tooltip.geneticsresequenced.gmo_recipe.line3"
	const val GMO_SUCCESS = "tooltip.geneticsresequenced.gmo_recipe.success"
	const val GMO_FAILURE = "tooltip.geneticsresequenced.gmo_recipe.failure"
	const val ITEM_MAGNET_BLACKLIST = "tooltip.geneticsresequenced.item_magnet_blacklist"
	const val FE = "tooltip.geneticsresequenced.fe"
	const val GMO_BASE_CHANCE = "tooltip.geneticsresequenced.gmo.chance"
	const val GMO_OVERCLOCKER_CHANCE = "tooltip.geneticsresequenced.gmo.overclocker"
	const val GMO_CHORUS_CHANCE = "tooltip.geneticsresequenced.gmo.chorus"

}