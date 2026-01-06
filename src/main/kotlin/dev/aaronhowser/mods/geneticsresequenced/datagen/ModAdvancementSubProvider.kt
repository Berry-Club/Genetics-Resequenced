package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.AaronExtensions.withComponent
import dev.aaronhowser.mods.aaron.datagen.AaronAdvancementSubProvider
import dev.aaronhowser.mods.geneticsresequenced.advancement.HelixGenePredicate
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModAdvancementLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.FrameType
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class ModAdvancementSubProvider(
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AaronAdvancementSubProvider(lookupProvider) {

	override fun generate(
		pRegistries: HolderLookup.Provider,
		pWriter: Consumer<Advancement>
	) {

		fun Advancement.save(): Advancement {
			pWriter.accept(this)
			return this
		}

		val root = advancement()
			.display(
				ModItems.SCRAPER.get(),
				Component.literal("Genetics: Resequenced"),
				ModAdvancementLang.SCRAPER_DESC.toComponent(),
				OtherUtil.modResource("textures/block/machine_bottom.png"),
				FrameType.TASK,
				true,
				true,
				false
			)
			.addCriterion(
				"scraper",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SCRAPER.get())
			)
			.build( ROOT)
			.save()

		val cellAnalyzer = advancement()
			.parent(root)
			.display(
				ModBlocks.CELL_ANALYZER.get(),
				ModAdvancementLang.ANALYZER_TITLE.toComponent(),
				ModAdvancementLang.ANALYZER_DESC.toComponent()
			)
			.addCriterion(
				"cell_analyzer",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CELL_ANALYZER.get())
			)
			.addCriterion(
				"cell",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CELL.get())
			)
			.requirements(RequirementsStrategy.OR)
			.build( CELL_ANALYZER)
			.save()

		val dnaExtractor = advancement()
			.parent(cellAnalyzer)
			.display(
				ModBlocks.DNA_EXTRACTOR.get(),
				ModAdvancementLang.EXTRACTOR_TITLE.toComponent(),
				ModAdvancementLang.EXTRACTOR_DESC.toComponent(),
			)
			.addCriterion(
				"dna_extractor",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.DNA_EXTRACTOR.get())
			)
			.addCriterion(
				"dna_helix",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DNA_HELIX.get())
			)
			.requirements(RequirementsStrategy.OR)
			.build( DNA_EXTRACTOR)
			.save()

		val dnaDecryptor = advancement()
			.parent(dnaExtractor)
			.display(
				ModBlocks.DNA_DECRYPTOR.get(),
				ModAdvancementLang.DECRYPTOR_TITLE.toComponent(),
				ModAdvancementLang.DECRYPTOR_DESC.toComponent(),
			)
			.addCriterion(
				"dna_decryptor",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.DNA_DECRYPTOR.get())
			)
			.build( DNA_DECRYPTOR)
			.save()

		val decryptDna = advancement()
			.parent(dnaDecryptor)
			.display(
				ModItems.DNA_HELIX.get(),
				ModAdvancementLang.DECRYPT_TITLE.toComponent(),
				ModAdvancementLang.DECRYPT_DESC.toComponent(),
			)
			.addCriterion(
				"decrypted_dna",
				InventoryChangeTrigger.TriggerInstance.hasItems(HelixGenePredicate.any())
			)
			.build( DECRYPT_DNA)
			.save()

		Advancement.Builder.advancement()
			.parent(decryptDna)
			.display(
				Items.WITHER_ROSE,
				ModAdvancementLang.BLACK_DEATH_TITLE.toComponent(),
				ModAdvancementLang.BLACK_DEATH_DESC.toComponent(),
				FrameType.CHALLENGE
			)
			.addCriterion(
				"black_death_helix",
				InventoryChangeTrigger.TriggerInstance.hasItems(HelixGenePredicate.blackDeath())
			)
			.build( BLACK_DEATH)
			.save()

		val plasmidInfuser = advancement()
			.parent(dnaExtractor)
			.display(
				ModBlocks.PLASMID_INFUSER.get(),
				ModAdvancementLang.INFUSER_TITLE.toComponent(),
				ModAdvancementLang.INFUSER_DESC.toComponent(),
			)
			.addCriterion(
				"plasmid_infuser",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.PLASMID_INFUSER.get())
			)
			.build( PLASMID_INFUSER)
			.save()

		val plasmidInjector = advancement()
			.parent(plasmidInfuser)
			.display(
				ModBlocks.PLASMID_INJECTOR.get(),
				ModAdvancementLang.INJECTOR_TITLE.toComponent(),
				ModAdvancementLang.INJECTOR_DESC.toComponent(),
			)
			.addCriterion(
				"plasmid_injector",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.PLASMID_INJECTOR.get())
			)
			.build( PLASMID_INJECTOR)
			.save()

		val getGene = advancement()
			.parent(plasmidInjector)
			.display(
				ModItems.SYRINGE
					.getDefaultInstance()
					.withComponent(
						SpecificEntityItemComponent(
							UUID.fromString("b0aa4edd-29e0-421f-b65c-be90055071b0"), //Arbitrary UUID
							Component.literal("A Fake Mob")
						)
					),
				ModAdvancementLang.GET_GENE_TITLE.toComponent(),
				ModAdvancementLang.GET_GENE_DESC.toComponent(),
				null,
				FrameType.GOAL,
				true, true, false
			)
			.addImpossibleCriterion()
			.build( GET_GENE)
			.save()

		advancement()
			.parent(getGene)
			.display(
				Items.ELYTRA,
				ModAdvancementLang.FLIGHT_TITLE.toComponent(),
				ModAdvancementLang.FLIGHT_DESC.toComponent(),
				FrameType.CHALLENGE,
			)
			.addImpossibleCriterion()
			.build( GET_FLIGHT)
			.save()

		advancement()
			.parent(getGene)
			.display(
				Items.JACK_O_LANTERN,
				ModAdvancementLang.SCARE_TITLE.toComponent(),
				ModAdvancementLang.SCARE_DESC.toComponent(),
				FrameType.CHALLENGE,
			)
			.addImpossibleCriterion()
			.build( GET_ALL_SCARE_GENES)
			.save()

		advancement()
			.parent(getGene)
			.display(
				Items.SKELETON_SKULL,
				ModAdvancementLang.CRINGE_TITLE.toComponent(),
				ModAdvancementLang.CRINGE_DESC.toComponent(),
				FrameType.GOAL,
			)
			.addImpossibleCriterion()
			.build( GET_CRINGE)
			.save()

		advancement()
			.parent(getGene)
			.display(
				Items.MILK_BUCKET,
				ModAdvancementLang.GET_MILKED_TITLE.toComponent(),
				ModAdvancementLang.GET_MILKED_DESC.toComponent(),
				FrameType.CHALLENGE,
			)
			.addImpossibleCriterion()
			.build( GET_MILKED)
			.save()

		advancement()
			.parent(getGene)
			.display(
				Items.SLIME_BALL,
				ModAdvancementLang.SLIMY_TITLE.toComponent(),
				ModAdvancementLang.SLIMY_DESC.toComponent(),
				FrameType.CHALLENGE,
			)
			.addImpossibleCriterion()
			.build( TRIGGER_SLIMY_DEATH)
			.save()

		val syringe = advancement()
			.parent(root)
			.display(
				ModItems.SYRINGE.get(),
				ModAdvancementLang.SYRINGE_TITLE.toComponent(),
				ModAdvancementLang.SYRINGE_DESC.toComponent(),
			)
			.addCriterion(
				"syringe",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SYRINGE.get())
			)
			.addCriterion(
				"metal_syringe",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.METAL_SYRINGE.get())
			)
			.requirements(RequirementsStrategy.OR)
			.build( SYRINGE)
			.save()

		advancement()
			.parent(syringe)
			.display(
				ModBlocks.BLOOD_PURIFIER.get(),
				ModAdvancementLang.PURIFIER_TITLE.toComponent(),
				ModAdvancementLang.PURIFIER_DESC.toComponent(),
			)
			.addCriterion(
				"blood_purifier",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.BLOOD_PURIFIER.get())
			)
			.build( BLOOD_PURIFIER)
			.save()

	}

	companion object {
		private fun guide(string: String) = OtherUtil.modResource("guide/$string")

		val ROOT = guide("root")
		val CELL_ANALYZER = guide("cell_analyzer")
		val DNA_EXTRACTOR = guide("dna_extractor")
		val DNA_DECRYPTOR = guide("dna_decryptor")
		val DECRYPT_DNA = guide("decrypt_dna")
		val BLACK_DEATH = guide("black_death")
		val PLASMID_INFUSER = guide("plasmid_infuser")
		val PLASMID_INJECTOR = guide("plasmid_injector")
		val GET_GENE = guide("get_gene")
		val GET_FLIGHT = guide("get_flight")
		val GET_ALL_SCARE_GENES = guide("get_all_scare_genes")
		val GET_CRINGE = guide("get_cringe")
		val GET_MILKED = guide("get_milked")
		val TRIGGER_SLIMY_DEATH = guide("trigger_slimy_death")
		val SYRINGE = guide("syringe")
		val BLOOD_PURIFIER = guide("blood_purifier")
	}

}