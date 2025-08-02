package dev.aaronhowser.mods.geneticsresequenced.datagen.lang

import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems

object ModItemLang {

	fun add(provider: ModLanguageProvider) {

		provider.add(CREATIVE_TAB, "Genetics: Resequenced")
		provider.add(GUIDE_BOOK, "Big Book of Genetics")
		provider.add(SYRINGE_EMPTY, "Empty Syringe")
		provider.add(SYRINGE_FULL, "Full Syringe")
		provider.add(METAL_SYRINGE_EMPTY, "Empty Metal Syringe")
		provider.add(METAL_SYRINGE_FULL, "Full Metal Syringe")

		provider.addItem(ModItems.DRAGON_HEALTH_CRYSTAL, "Dragon Health Crystal")
		provider.addItem(ModItems.ORGANIC_MATTER, "Organic Matter")
		provider.addItem(ModItems.CELL, "Cell")
		provider.addItem(ModItems.GMO_CELL, "Genetically Modified Cell")
		provider.addItem(ModItems.ANTI_FIELD_ORB, "Anti-Field Orb")
		provider.addItem(ModItems.SCRAPER, "Scraper")
		provider.addItem(ModItems.DNA_HELIX, "DNA Helix")
		provider.addItem(ModItems.PLASMID, "Plasmid")
		provider.addItem(ModItems.OVERCLOCKER, "Overclocker")
		provider.addItem(ModItems.FRIENDLY_SLIME_SPAWN_EGG, "Support Slime Spawn Egg")
		provider.addItem(ModItems.ANTI_PLASMID, "Anti-Plasmid")
		provider.addItem(ModItems.GENE_CHECKER, "Gene Checker")

		provider.add(POTION_SUBSTRATE, "Organic Substrate")
		provider.add(POTION_CELL_GROWTH, "Potion of Cell Growth")
		provider.add(POTION_MUTATION, "Potion of Mutation")
		provider.add(POTION_VIRAL_AGENTS, "Potion of Viral Agents")
		provider.add(POTION_PANACEA, "Potion of Panacea")
		provider.add(POTION_ZOMBIFY_VILLAGER, "Potion of Zombify Villager")

		provider.add(SPLASH_POTION_SUBSTRATE, "Splash Potion of Organic Substrate")
		provider.add(SPLASH_POTION_CELL_GROWTH, "Splash Potion of Cell Growth")
		provider.add(SPLASH_POTION_MUTATION, "Splash Potion of Mutation")
		provider.add(SPLASH_POTION_VIRAL_AGENTS, "Splash Potion of Viral Agents")
		provider.add(SPLASH_POTION_PANACEA, "Splash Potion of Panacea")
		provider.add(SPLASH_POTION_ZOMBIFY_VILLAGER, "Splash Potion of Zombify Villager")
		provider.add(LINGERING_POTION_SUBSTRATE, "Lingering Potion of Organic Substrate")
		provider.add(LINGERING_POTION_CELL_GROWTH, "Lingering Potion of Cell Growth")
		provider.add(LINGERING_POTION_MUTATION, "Lingering Potion of Mutation")
		provider.add(LINGERING_POTION_VIRAL_AGENTS, "Lingering Potion of Viral Agents")
		provider.add(LINGERING_POTION_PANACEA, "Lingering Potion of Panacea")
		provider.add(LINGERING_POTION_ZOMBIFY_VILLAGER, "Lingering Potion of Zombify Villager")
		provider.add(TIPPED_ARROW_SUBSTRATE, "Arrow of Organic Substrate")
		provider.add(TIPPED_ARROW_CELL_GROWTH, "Arrow of Cell Growth")
		provider.add(TIPPED_ARROW_MUTATION, "Arrow of Mutation")
		provider.add(TIPPED_ARROW_VIRAL_AGENTS, "Arrow of Viral Agents")
		provider.add(TIPPED_ARROW_PANACEA, "Arrow of Panacea")
		provider.add(TIPPED_ARROW_ZOMBIFY_VILLAGER, "Arrow of Zombify Villager")
	}

	const val CREATIVE_TAB = "itemGroup.geneticsresequenced"

	const val GUIDE_BOOK = "item.geneticsresequenced.guide_book"
	const val SYRINGE_EMPTY = "item.geneticsresequenced.syringe.empty"
	const val SYRINGE_FULL = "item.geneticsresequenced.syringe.full"
	const val METAL_SYRINGE_EMPTY = "item.geneticsresequenced.metal_syringe.empty"
	const val METAL_SYRINGE_FULL = "item.geneticsresequenced.metal_syringe.full"

	const val POTION_SUBSTRATE = "item.minecraft.potion.effect.geneticsresequenced.substrate"
	const val POTION_CELL_GROWTH = "item.minecraft.potion.effect.geneticsresequenced.cell_growth"
	const val POTION_MUTATION = "item.minecraft.potion.effect.geneticsresequenced.mutation"
	const val POTION_VIRAL_AGENTS = "item.minecraft.potion.effect.geneticsresequenced.viral_agents"
	const val POTION_PANACEA = "item.minecraft.potion.effect.geneticsresequenced.panacea"
	const val POTION_ZOMBIFY_VILLAGER = "item.minecraft.potion.effect.geneticsresequenced.zombify_villager"
	const val SPLASH_POTION_SUBSTRATE = "item.minecraft.splash_potion.effect.geneticsresequenced.substrate"
	const val SPLASH_POTION_CELL_GROWTH = "item.minecraft.splash_potion.effect.geneticsresequenced.cell_growth"
	const val SPLASH_POTION_MUTATION = "item.minecraft.splash_potion.effect.geneticsresequenced.mutation"
	const val SPLASH_POTION_VIRAL_AGENTS = "item.minecraft.splash_potion.effect.geneticsresequenced.viral_agents"
	const val SPLASH_POTION_PANACEA = "item.minecraft.splash_potion.effect.geneticsresequenced.panacea"
	const val SPLASH_POTION_ZOMBIFY_VILLAGER = "item.minecraft.splash_potion.effect.geneticsresequenced.zombify_villager"
	const val LINGERING_POTION_SUBSTRATE = "item.minecraft.lingering_potion.effect.geneticsresequenced.substrate"
	const val LINGERING_POTION_CELL_GROWTH = "item.minecraft.lingering_potion.effect.geneticsresequenced.cell_growth"
	const val LINGERING_POTION_MUTATION = "item.minecraft.lingering_potion.effect.geneticsresequenced.mutation"
	const val LINGERING_POTION_VIRAL_AGENTS = "item.minecraft.lingering_potion.effect.geneticsresequenced.viral_agents"
	const val LINGERING_POTION_PANACEA = "item.minecraft.lingering_potion.effect.geneticsresequenced.panacea"
	const val LINGERING_POTION_ZOMBIFY_VILLAGER = "item.minecraft.lingering_potion.effect.geneticsresequenced.zombify_villager"
	const val TIPPED_ARROW_SUBSTRATE = "item.minecraft.tipped_arrow.effect.geneticsresequenced.substrate"
	const val TIPPED_ARROW_CELL_GROWTH = "item.minecraft.tipped_arrow.effect.geneticsresequenced.cell_growth"
	const val TIPPED_ARROW_MUTATION = "item.minecraft.tipped_arrow.effect.geneticsresequenced.mutation"
	const val TIPPED_ARROW_VIRAL_AGENTS = "item.minecraft.tipped_arrow.effect.geneticsresequenced.viral_agents"
	const val TIPPED_ARROW_PANACEA = "item.minecraft.tipped_arrow.effect.geneticsresequenced.panacea"
	const val TIPPED_ARROW_ZOMBIFY_VILLAGER = "item.minecraft.tipped_arrow.effect.geneticsresequenced.zombify_villager"


}