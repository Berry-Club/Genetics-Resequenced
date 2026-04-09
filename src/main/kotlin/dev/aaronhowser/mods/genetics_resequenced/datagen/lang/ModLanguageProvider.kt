package dev.aaronhowser.mods.genetics_resequenced.datagen.lang

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import dev.aaronhowser.mods.genetics_resequenced.registry.ModEffects
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
	output: PackOutput
) : LanguageProvider(output, GeneticsResequenced.MOD_ID, "en_us") {

	companion object {
		fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args)
	}

	override fun addTranslations() {

		ModItemLang.add(this)
		ModMessageLang.add(this)
		ModTooltipLang.add(this)
		ModConfigLang.add(this)
		ModInfoLang.add(this)
		ModGeneLang.add(this)
		ModAdvancementLang.add(this)
		ModRecipeLang.add(this)

		addEffect(ModEffects.BLEED, "Bleed")
		addEffect(ModEffects.SUBSTRATE, "Substrate")
		addEffect(ModEffects.CELL_GROWTH, "Cell Growth")
		addEffect(ModEffects.MUTATION, "Mutation")
		addEffect(ModEffects.VIRAL_AGENTS, "Viral Agents")
		addEffect(ModEffects.PANACEA, "Panacea")
		addEffect(ModEffects.ZOMBIFY_VILLAGER, "Zombify Villager")

		addBlock(ModBlocks.ANTI_FIELD_BLOCK, "Anti-Field Block")
		addBlock(ModBlocks.BIOLUMINESCENCE_BLOCK, "Bioluminescence Glow")
		addBlock(ModBlocks.WEB_DEFENSE_BLOCK, "Web Defense Cobweb")
		addBlock(ModBlocks.CELL_ANALYZER, "Cell Analyzer")
		addBlock(ModBlocks.COAL_GENERATOR, "Coal Generator")
		addBlock(ModBlocks.DNA_DECRYPTOR, "DNA Decryptor")
		addBlock(ModBlocks.DNA_EXTRACTOR, "DNA Extractor")
		addBlock(ModBlocks.BLOOD_PURIFIER, "Blood Purifier")
		addBlock(ModBlocks.INCUBATOR, "Incubator")
		addBlock(ModBlocks.ADVANCED_INCUBATOR, "Advanced Incubator")
		addBlock(ModBlocks.PLASMID_INFUSER, "Plasmid Infuser")
		addBlock(ModBlocks.PLASMID_INJECTOR, "Plasmid Injector")

		add(Commands.LIST_ALL_GENES, "Gene List:\n")
		add(Commands.NO_GENES, "No Genes found!")
		add(Commands.TARGET_GENE_LIST, "%1\$s's Genes:\n")
		add(Commands.TEMPORARY_GENE_LIST, "%1\$s's Temporary Genes:\n")
		add(Commands.TEMPORARY_GENE_WITH_DURATION, "%1\$s (%2\$d ticks)")

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

		add(Cooldown.STARTED, " triggered! Cooldown started: %s")
		add(Cooldown.ENDED, "%s has come off cooldown!")
		add(Cooldown.ON_COOLDOWN, "%s is on cooldown!")

		add(Keys.CATEGORY, "Genetics: Resequenced")
		add(Keys.DRAGONS_BREATH, "Dragon's Breath")
		add(Keys.TELEPORT, "Teleport")

		add(Other.DELICATE_TOUCH, "Delicate Touch")
		add(Other.SUPPORT_SLIME, "Support Slime")
		add(Other.DEATH_SCRAPER, "%s was scraped to the bone")
		add(Other.DEATH_SYRINGE, "%s died of blood loss")
		add(Other.DEATH_SYRINGE_PICKUP, "%s stepped on a loose Syringe")
		add(Other.BLEED_DEATH, "%s bled out!")
		add(Other.VIRUS_DEATH, "%s succumbed to a Virus!")
		add(Other.BOOK_LANDING_TEXT, "Welcome to Genetics: Resequenced! This guide book will help you get started with the mod.")
		add(Other.ORACLE_INDEX_TITLE, "Genetics: Resequenced")
	}

	object Commands {
		const val LIST_ALL_GENES = "command.genetics_resequenced.list_all_genes"
		const val NO_GENES = "command.genetics_resequenced.list.no_genes"
		const val TARGET_GENE_LIST = "command.genetics_resequenced.list.genes"
		const val TEMPORARY_GENE_LIST = "command.genetics_resequenced.list.temporary_genes"
		const val TEMPORARY_GENE_WITH_DURATION = "command.genetics_resequenced.list.temporary_genes_with_duration"
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

	object Cooldown {
		const val STARTED = "cooldown.genetics_resequenced.started"
		const val ENDED = "cooldown.genetics_resequenced.ended"
		const val ON_COOLDOWN = "cooldown.genetics_resequenced.on_cooldown"
	}

	object Other {
		const val DEATH_SCRAPER = "death.attack.gr_scraper"
		const val DEATH_SYRINGE = "death.attack.gr_syringe"
		const val DEATH_SYRINGE_PICKUP = "death.attack.gr_syringe_pickup"
		const val BLEED_DEATH = "death.attack.gr_bleed"
		const val VIRUS_DEATH = "death.attack.gr_virus"

		const val DELICATE_TOUCH = "enchantment.genetics_resequenced.delicate_touch"
		const val SUPPORT_SLIME = "entity.genetics_resequenced.support_slime"

		const val BOOK_LANDING_TEXT = "book.genetics_resequenced.landing_text"
		const val ORACLE_INDEX_TITLE = "oracle_index.title.genetics_resequenced"
	}

	object Keys {
		const val CATEGORY = "key.genetics_resequenced.category"
		const val DRAGONS_BREATH = "key.genetics_resequenced.dragons_breath"
		const val TELEPORT = "key.genetics_resequenced.teleport"
	}

}