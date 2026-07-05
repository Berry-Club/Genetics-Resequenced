package dev.aaronhowser.mods.genetics_resequenced.datagen

import dev.aaronhowser.mods.aaron.datagen.AaronAdvancementSubProvider
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.advancement.HelixGenePredicate
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModAdvancementLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItemSubPredicates
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.criterion.DataComponentMatchers
import net.minecraft.advancements.criterion.InventoryChangeTrigger
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class ModAdvancementSubProvider(
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AaronAdvancementSubProvider(GeneticsResequenced.MOD_ID, lookupProvider) {

	override fun generate(
		registries: HolderLookup.Provider,
		saver: Consumer<AdvancementHolder>
	) {
		val itemLookup = registries.lookupOrThrow(Registries.ITEM)
		fun Advancement.Builder.save(id: Identifier) = save(saver, id)

		val root = advancement()
			.displayWithBackground(
				ModItems.SCRAPER.get(),
				Component.literal("Genetics: Resequenced"),
				ModAdvancementLang.SCRAPER_DESC.toComponent(),
				GeneticsResequenced.modResource("textures/block/machine_bottom.png"),
				AdvancementType.TASK,
				true,
				true,
				false
			)
			.has(ModItems.SCRAPER)
			.save(ROOT)

		val cellAnalyzer = advancement()
			.parent(root)
			.display(
				ModBlocks.CELL_ANALYZER.get(),
				ModAdvancementLang.ANALYZER_TITLE.toComponent(),
				ModAdvancementLang.ANALYZER_DESC.toComponent()
			)
			.hasAny(ModBlocks.CELL_ANALYZER, ModItems.CELL)
			.save(CELL_ANALYZER)

		val dnaExtractor = advancement()
			.parent(cellAnalyzer)
			.display(
				ModBlocks.DNA_EXTRACTOR.get(),
				ModAdvancementLang.EXTRACTOR_TITLE.toComponent(),
				ModAdvancementLang.EXTRACTOR_DESC.toComponent(),
			)
			.hasAny(ModBlocks.DNA_EXTRACTOR, ModItems.DNA_HELIX)
			.save(DNA_EXTRACTOR)

		val dnaDecryptor = advancement()
			.parent(dnaExtractor)
			.display(
				ModBlocks.DNA_DECRYPTOR.get(),
				ModAdvancementLang.DECRYPTOR_TITLE.toComponent(),
				ModAdvancementLang.DECRYPTOR_DESC.toComponent(),
			)
			.has(ModBlocks.DNA_DECRYPTOR)
			.save(DNA_DECRYPTOR)

		val decryptDna = advancement()
			.parent(dnaDecryptor)
			.display(
				ModItems.DNA_HELIX.get(),
				ModAdvancementLang.DECRYPT_TITLE.toComponent(),
				ModAdvancementLang.DECRYPT_DESC.toComponent(),
			)
			.addCriterion(
				"decrypted_dna",
				InventoryChangeTrigger.TriggerInstance.hasItems(
					ItemPredicate.Builder
						.item()
						.of(itemLookup, ModItems.DNA_HELIX.get())
						.withComponents(
							DataComponentMatchers.Builder.components()
								.partial(
									ModItemSubPredicates.HELIX_GENE.get(),
									HelixGenePredicate.any()
								)
								.build()
						)
						.build()
				)
			)
			.save(DECRYPT_DNA)

		Advancement.Builder.advancement()
			.parent(decryptDna)
			.display(
				Items.WITHER_ROSE,
				ModAdvancementLang.BLACK_DEATH_TITLE.toComponent(),
				ModAdvancementLang.BLACK_DEATH_DESC.toComponent(),
				null,
				AdvancementType.CHALLENGE,
				true, true, false
			)
			.addCriterion(
				"black_death_helix",
				InventoryChangeTrigger.TriggerInstance.hasItems(
					ItemPredicate.Builder
						.item()
						.of(itemLookup, ModItems.DNA_HELIX.get())
						.withComponents(
							DataComponentMatchers.Builder.components()
								.partial(
									ModItemSubPredicates.HELIX_GENE.get(),
									HelixGenePredicate.blackDeath()
								)
								.build()
						)
						.build()
				)
			)
			.save(BLACK_DEATH)

		val plasmidInfuser = advancement()
			.parent(dnaExtractor)
			.display(
				ModBlocks.PLASMID_INFUSER.get(),
				ModAdvancementLang.INFUSER_TITLE.toComponent(),
				ModAdvancementLang.INFUSER_DESC.toComponent(),
			)
			.has(ModBlocks.PLASMID_INFUSER)
			.save(PLASMID_INFUSER)

		val plasmidInjector = advancement()
			.parent(plasmidInfuser)
			.display(
				ModBlocks.PLASMID_INJECTOR.get(),
				ModAdvancementLang.INJECTOR_TITLE.toComponent(),
				ModAdvancementLang.INJECTOR_DESC.toComponent(),
			)
			.has(ModBlocks.PLASMID_INJECTOR)
			.save(PLASMID_INJECTOR)

		val fakeMobSyringe = ItemStackTemplate(
			ModItems.SYRINGE.get(),
			DataComponentPatch.builder()
				.set(
					ModDataComponents.SPECIFIC_ENTITY.get(),
					SpecificEntityItemComponent(
						UUID.fromString("b0aa4edd-29e0-421f-b65c-be90055071b0"), // Arbitrary UUID
						Component.literal("A Fake Mob")
					)
				)
				.build()
		)

		val getGene = advancement()
			.parent(plasmidInjector)
			.display(
				fakeMobSyringe,
				ModAdvancementLang.GET_GENE_TITLE.toComponent(),
				ModAdvancementLang.GET_GENE_DESC.toComponent(),
				null,
				AdvancementType.GOAL,
				true, true, false
			)
			.addImpossibleCriterion()
			.save(GET_GENE)

		advancement()
			.parent(getGene)
			.display(
				Items.ELYTRA,
				ModAdvancementLang.FLIGHT_TITLE.toComponent(),
				ModAdvancementLang.FLIGHT_DESC.toComponent(),
				AdvancementType.CHALLENGE,
			)
			.addImpossibleCriterion()
			.save(GET_FLIGHT)

		advancement()
			.parent(getGene)
			.display(
				Items.JACK_O_LANTERN,
				ModAdvancementLang.SCARE_TITLE.toComponent(),
				ModAdvancementLang.SCARE_DESC.toComponent(),
				AdvancementType.CHALLENGE,
			)
			.addImpossibleCriterion()
			.save(GET_ALL_SCARE_GENES)

		advancement()
			.parent(getGene)
			.display(
				Items.SKELETON_SKULL,
				ModAdvancementLang.CRINGE_TITLE.toComponent(),
				ModAdvancementLang.CRINGE_DESC.toComponent(),
				AdvancementType.GOAL,
			)
			.addImpossibleCriterion()
			.save(GET_CRINGE)

		advancement()
			.parent(getGene)
			.display(
				Items.MILK_BUCKET,
				ModAdvancementLang.GET_MILKED_TITLE.toComponent(),
				ModAdvancementLang.GET_MILKED_DESC.toComponent(),
				AdvancementType.CHALLENGE,
			)
			.addImpossibleCriterion()
			.save(GET_MILKED)

		advancement()
			.parent(getGene)
			.display(
				Items.SLIME_BALL,
				ModAdvancementLang.SLIMY_TITLE.toComponent(),
				ModAdvancementLang.SLIMY_DESC.toComponent(),
				AdvancementType.CHALLENGE,
			)
			.addImpossibleCriterion()
			.save(TRIGGER_SLIMY_DEATH)

		val syringe = advancement()
			.parent(root)
			.display(
				ModItems.SYRINGE.get(),
				ModAdvancementLang.SYRINGE_TITLE.toComponent(),
				ModAdvancementLang.SYRINGE_DESC.toComponent(),
			)
			.hasAny(ModItems.SYRINGE, ModItems.METAL_SYRINGE)
			.save(SYRINGE)

		advancement()
			.parent(syringe)
			.display(
				ModBlocks.BLOOD_PURIFIER.get(),
				ModAdvancementLang.PURIFIER_TITLE.toComponent(),
				ModAdvancementLang.PURIFIER_DESC.toComponent(),
			)
			.has(ModBlocks.BLOOD_PURIFIER)
			.save(BLOOD_PURIFIER)

	}

	companion object {
		private fun guide(string: String) = GeneticsResequenced.modResource("guide/$string")

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
