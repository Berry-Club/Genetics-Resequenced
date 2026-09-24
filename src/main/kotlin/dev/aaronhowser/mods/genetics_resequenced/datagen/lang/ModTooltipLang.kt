package dev.aaronhowser.mods.genetics_resequenced.datagen.lang

object ModTooltipLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(GENE, "Gene: %1\$s")
			add(HELIX_ENTITY, "Entity: %1\$s")
			add(COPY_GENE, "Click to copy ID:\n%s")
			add(ACTIVE, "Active")
			add(INACTIVE, "Inactive")
			add(CELL_MOB, "Cell type: %s")
			add(CELL_NO_MOB, "Empty")
			add(CELL_CREATIVE, "Creative: Right-click mob to set.")
			add(PLASMID_EMPTY, "Empty Plasmid")
			add(ANTI_PLASMID_EMPTY, "Empty Anti-Plasmid")
			add(PLASMID_GENE, "Contains Gene: %s")
			add(PLASMID_COMPLETE, "Plasmid is complete!")
			add(PLASMID_PROGRESS, "DNA Points: %d/%d")
			add(SYRINGE_CONTAMINATED, "Contaminated Blood")
			add(SYRINGE_OWNER, "Blood: %1\$s")
			add(INFUSER_BASIC, "+1 DNA Point")
			add(INFUSER_MATCHING, "+2 DNA Points")
			add(INFUSER_MISMATCH, "Not applicable")
			add(INJECTOR_CONTAMINATED, "You can't inject Genes into Contaminated Blood!")
			add(INFUSER_ANTI_PLASMID_1, "Anti-Plasmids are not set in the Plasmid Infuser!")
			add(INFUSER_ANTI_PLASMID_2, "Craft it together with a completed Plasmid to set it.")
			add(COAL_GEN_TOTAL_FE, "Stack total: %d FE")
			add(IGNORE_POTION, "Don't craft this! It does nothing!")
			add(GMO_CHANCE, "Chance: %d%%")
			add(SUBSTRATE_RECIPE, "Each Organic Substrate is replaced with a copy of the Cell.")
			add(INCUBATOR_SET_HIGH, "Set temperature to high")
			add(INCUBATOR_SET_LOW, "Set temperature to low")
			add(BLACK_DEATH_RECIPE, "Requires a Syringe that has every negative Gene.")
			add(SYRINGE_ADDING_GENES, "Adding Genes:")
			add(SYRINGE_REMOVING_GENES, "Removing:")
			add(GMO_TEMPERATURE_REQUIREMENT, "Advanced Incubator §lmust be low temperature§r")
			add(GMO_CHORUS, "Overclockers lower your chance, but Chorus Fruits increase it!")
			add(ITEM_MAGNET_BLACKLIST, "Blacklisted from Item Magnet Gene")
			add(FE, "%1\$s/%2\$s FE")
			add(GMO_BASE_CHANCE, "%1\$s Gene base chance: %2\$d%%")
			add(GMO_OVERCLOCKER_CHANCE, "%1\$d Overclockers lower chance to %2\$d%%")
			add(GMO_CHORUS_CHANCE, "%1\$d Chorus Fruit increase chance to %2\$d%%")
			add(GMO_SUCCESS, "Result on success")
			add(GMO_FAILURE, "Result on failure")
		}
	}

	const val GENE = "tooltip.genetics_resequenced.gene"
	const val HELIX_ENTITY = "tooltip.genetics_resequenced.helix_entity"
	const val COPY_GENE = "tooltip.genetics_resequenced.copy_gene_id"
	const val ACTIVE = "tooltip.genetics_resequenced.antifield_active"
	const val INACTIVE = "tooltip.genetics_resequenced.antifield_inactive"
	const val CELL_MOB = "tooltip.genetics_resequenced.dna_item.filled"
	const val CELL_NO_MOB = "tooltip.genetics_resequenced.dna_item.empty"
	const val CELL_CREATIVE = "tooltip.genetics_resequenced.dna_item.creative"
	const val PLASMID_EMPTY = "tooltip.genetics_resequenced.plasmid.empty"
	const val ANTI_PLASMID_EMPTY = "tooltip.genetics_resequenced.anti_plasmid.empty"
	const val PLASMID_GENE = "tooltip.genetics_resequenced.plasmid.gene"
	const val PLASMID_COMPLETE = "tooltip.genetics_resequenced.plasmid.complete"
	const val PLASMID_PROGRESS = "tooltip.genetics_resequenced.plasmid.amount"
	const val SYRINGE_CONTAMINATED = "tooltip.genetics_resequenced.syringe.contaminated"
	const val SYRINGE_OWNER = "tooltip.genetics_resequenced.syringe.blood_owner"
	const val INFUSER_BASIC = "tooltip.genetics_resequenced.plasmid_infuser.basic_gene"
	const val INFUSER_MATCHING = "tooltip.genetics_resequenced.plasmid_infuser.matching_gene"
	const val INFUSER_MISMATCH = "tooltip.genetics_resequenced.plasmid_infuser.different_gene"
	const val INFUSER_ANTI_PLASMID_1 = "tooltip.genetics_resequenced.plasmid_infuser.anti_plasmid_1"
	const val INFUSER_ANTI_PLASMID_2 = "tooltip.genetics_resequenced.plasmid_infuser.anti_plasmid_2"
	const val INJECTOR_CONTAMINATED = "tooltip.genetics_resequenced.plasmid_injector.contaminated"
	const val COAL_GEN_TOTAL_FE = "tooltip.genetics_resequenced.coal_generator.total_fe"
	const val IGNORE_POTION = "tooltip.genetics_resequenced.potion.ignore"
	const val GMO_CHANCE = "tooltip.genetics_resequenced.gmo_cell.chance"
	const val SUBSTRATE_RECIPE = "tooltip.genetics_resequenced.substrate_recipe"
	const val INCUBATOR_SET_HIGH = "tooltip.genetics_resequenced.advanced_incubator.set_high"
	const val INCUBATOR_SET_LOW = "tooltip.genetics_resequenced.advanced_incubator.set_low"
	const val BLACK_DEATH_RECIPE = "tooltip.genetics_resequenced.black_death_recipe"
	const val SYRINGE_ADDING_GENES = "tooltip.genetics_resequenced.syringe.adding"
	const val SYRINGE_REMOVING_GENES = "tooltip.genetics_resequenced.syringe.removing"
	const val GMO_TEMPERATURE_REQUIREMENT = "tooltip.genetics_resequenced.gmo_temperature_requirement"
	const val GMO_CHORUS = "tooltip.genetics_resequenced.gmo_recipe.line3"
	const val GMO_SUCCESS = "tooltip.genetics_resequenced.gmo_recipe.success"
	const val GMO_FAILURE = "tooltip.genetics_resequenced.gmo_recipe.failure"
	const val ITEM_MAGNET_BLACKLIST = "tooltip.genetics_resequenced.item_magnet_blacklist"
	const val FE = "tooltip.genetics_resequenced.fe"
	const val GMO_BASE_CHANCE = "tooltip.genetics_resequenced.gmo.chance"
	const val GMO_OVERCLOCKER_CHANCE = "tooltip.genetics_resequenced.gmo.overclocker"
	const val GMO_CHORUS_CHANCE = "tooltip.genetics_resequenced.gmo.chorus"

}