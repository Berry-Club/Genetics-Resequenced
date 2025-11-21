package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.advancement.HelixGenePredicate
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModAdvancementLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItemSubPredicates
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class ModAdvancementSubProvider(
	val lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AdvancementProvider.AdvancementGenerator {

	private fun guide(string: String) = OtherUtil.modResource("guide/$string")

	override fun generate(
		registries: HolderLookup.Provider,
		saver: Consumer<AdvancementHolder>,
		existingFileHelper: ExistingFileHelper
	) {

		val root =
			Advancement.Builder.advancement()
				.display(
					ModItems.SCRAPER.get(),
					Component.literal("Genetics: Resequenced"),
					ModAdvancementLang.SCRAPER_DESC.toComponent(),
					OtherUtil.modResource("textures/block/machine_bottom.png"),
					AdvancementType.TASK,
					true,
					true,
					false
				)
				.addCriterion(
					"scraper",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SCRAPER.get())
				)
				.save(saver, guide("root"), existingFileHelper)

		val cellAnalyzer =
			Advancement.Builder.advancement()
				.parent(root)
				.display(
					ModBlocks.CELL_ANALYZER.get(),
					ModAdvancementLang.ANALYZER_TITLE.toComponent(),
					ModAdvancementLang.ANALYZER_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"cell_analyzer",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CELL_ANALYZER.get())
				)
				.addCriterion(
					"cell",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CELL.get())
				)
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(saver, guide("cell_analyzer"), existingFileHelper)

		val dnaExtractor =
			Advancement.Builder.advancement()
				.parent(cellAnalyzer)
				.display(
					ModBlocks.DNA_EXTRACTOR.get(),
					ModAdvancementLang.EXTRACTOR_TITLE.toComponent(),
					ModAdvancementLang.EXTRACTOR_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"dna_extractor",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.DNA_EXTRACTOR.get())
				)
				.addCriterion(
					"dna_helix",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DNA_HELIX.get())
				)
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(saver, guide("dna_extractor"), existingFileHelper)

		val dnaDecryptor =
			Advancement.Builder.advancement()
				.parent(dnaExtractor)
				.display(
					ModBlocks.DNA_DECRYPTOR.get(),
					ModAdvancementLang.DECRYPTOR_TITLE.toComponent(),
					ModAdvancementLang.DECRYPTOR_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"dna_decryptor",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.DNA_DECRYPTOR.get())
				)
				.save(saver, guide("dna_decryptor"), existingFileHelper)

		val decryptDna =
			Advancement.Builder.advancement()
				.parent(dnaDecryptor)
				.display(
					ModItems.DNA_HELIX.get(),
					ModAdvancementLang.DECRYPT_TITLE.toComponent(),
					ModAdvancementLang.DECRYPT_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"decrypted_dna",
					InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder
							.item()
							.of(ModItems.DNA_HELIX.get())
							.withSubPredicate(
								ModItemSubPredicates.HELIX_GENE.get(),
								HelixGenePredicate.any()
							)
							.build()
					)
				)
				.save(saver, guide("decrypt_dna"), existingFileHelper)

		Advancement.Builder.advancement()
			.parent(decryptDna)
			.display(
				Items.WITHER_ROSE,
				ModAdvancementLang.BLACK_DEATH_TITLE.toComponent(),
				ModAdvancementLang.BLACK_DEATH_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"black_death_helix",
				InventoryChangeTrigger.TriggerInstance.hasItems(
					ItemPredicate.Builder
						.item()
						.of(ModItems.DNA_HELIX)
						.withSubPredicate(
							ModItemSubPredicates.HELIX_GENE.get(),
							HelixGenePredicate.blackDeath()
						)
						.build()
				)
			)
			.save(saver, guide("black_death"), existingFileHelper)

		val plasmidInfuser =
			Advancement.Builder.advancement()
				.parent(dnaExtractor)
				.display(
					ModBlocks.PLASMID_INFUSER.get(),
					ModAdvancementLang.INFUSER_TITLE.toComponent(),
					ModAdvancementLang.INFUSER_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"plasmid_infuser",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.PLASMID_INFUSER.get())
				)
				.save(saver, guide("plasmid_infuser"), existingFileHelper)

		val plasmidInjector =
			Advancement.Builder.advancement()
				.parent(plasmidInfuser)
				.display(
					ModBlocks.PLASMID_INJECTOR.get(),
					ModAdvancementLang.INJECTOR_TITLE.toComponent(),
					ModAdvancementLang.INJECTOR_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"plasmid_injector",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.PLASMID_INJECTOR.get())
				)
				.save(saver, guide("plasmid_injector"), existingFileHelper)

		val getGene =
			Advancement.Builder.advancement()
				.parent(plasmidInjector)
				.display(
					ModItems.SYRINGE.toStack().apply {
						set(
							ModDataComponents.SPECIFIC_ENTITY,
							SpecificEntityItemComponent(
								UUID.fromString("b0aa4edd-29e0-421f-b65c-be90055071b0"), //Arbitrary UUID
								Component.literal("A Fake Mob")
							)
						)
					},
					ModAdvancementLang.GET_GENE_TITLE.toComponent(),
					ModAdvancementLang.GET_GENE_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"impossible",
					CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
				)
				.save(saver, guide("get_gene"), existingFileHelper)

		Advancement.Builder.advancement()
			.parent(getGene)
			.display(
				Items.ELYTRA,
				ModAdvancementLang.FLIGHT_TITLE.toComponent(),
				ModAdvancementLang.FLIGHT_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"impossible",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.save(saver, guide("get_flight"), existingFileHelper)

		Advancement.Builder.advancement()
			.parent(getGene)
			.display(
				Items.JACK_O_LANTERN,
				ModAdvancementLang.SCARE_TITLE.toComponent(),
				ModAdvancementLang.SCARE_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"impossible",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.save(saver, guide("get_all_scare_genes"), existingFileHelper)

		Advancement.Builder.advancement()
			.parent(getGene)
			.display(
				Items.SKELETON_SKULL,
				ModAdvancementLang.CRINGE_TITLE.toComponent(),
				ModAdvancementLang.CRINGE_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"impossible",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.save(saver, guide("get_cringe"), existingFileHelper)

		Advancement.Builder.advancement()
			.parent(getGene)
			.display(
				Items.MILK_BUCKET,
				ModAdvancementLang.GET_MILKED_TITLE.toComponent(),
				ModAdvancementLang.GET_MILKED_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"impossible",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.save(saver, guide("get_milked"), existingFileHelper)

		Advancement.Builder.advancement()
			.parent(getGene)
			.display(
				Items.SLIME_BALL,
				ModAdvancementLang.SLIMY_TITLE.toComponent(),
				ModAdvancementLang.SLIMY_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"impossible",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.save(saver, guide("trigger_slimy_death"), existingFileHelper)

		val syringe =
			Advancement.Builder.advancement()
				.parent(root)
				.display(
					ModItems.SYRINGE.get(),
					ModAdvancementLang.SYRINGE_TITLE.toComponent(),
					ModAdvancementLang.SYRINGE_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"syringe",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SYRINGE.get())
				)
				.addCriterion(
					"metal_syringe",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.METAL_SYRINGE.get())
				)
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(saver, guide("syringe"), existingFileHelper)

		Advancement.Builder.advancement()
			.parent(syringe)
			.display(
				ModBlocks.BLOOD_PURIFIER.get(),
				ModAdvancementLang.PURIFIER_TITLE.toComponent(),
				ModAdvancementLang.PURIFIER_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"blood_purifier",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.BLOOD_PURIFIER.get())
			)
			.save(saver, guide("blood_purifier"), existingFileHelper)

	}

}