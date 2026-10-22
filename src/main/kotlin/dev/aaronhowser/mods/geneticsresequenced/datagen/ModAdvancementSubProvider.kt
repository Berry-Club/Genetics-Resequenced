package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.aaron.datagen.AaronAdvancementSubProvider
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.advancement.HelixGenePredicate
import dev.aaronhowser.mods.geneticsresequenced.advancement.ModAdvancements
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModAdvancementLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItemSubPredicates
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class ModAdvancementSubProvider(
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AaronAdvancementSubProvider(GeneticsResequenced.MOD_ID, lookupProvider) {

	override fun generate(
		registries: HolderLookup.Provider,
		saver: Consumer<AdvancementHolder>,
		existingFileHelper: ExistingFileHelper
	) {
		fun Advancement.Builder.save(id: ResourceLocation) = save(saver, id, existingFileHelper)

		val root = advancement()
			.display(
				ModItems.SCRAPER.get(),
				ModAdvancementLang.ROOT_TITLE.toComponent(),
				ModAdvancementLang.SCRAPER_DESC.toComponent(),
				GeneticsResequenced.modResource("textures/block/machine_bottom.png"),
				AdvancementType.TASK,
				true,
				true,
				false
			)
			.has(ModItems.SCRAPER)
			.save(ModAdvancements.ROOT)

		val cellAnalyzer = advancement()
			.parent(root)
			.display(
				ModBlocks.CELL_ANALYZER.get(),
				ModAdvancementLang.ANALYZER_TITLE.toComponent(),
				ModAdvancementLang.ANALYZER_DESC.toComponent()
			)
			.hasAny(ModBlocks.CELL_ANALYZER, ModItems.CELL)
			.save(ModAdvancements.CELL_ANALYZER)

		val dnaExtractor = advancement()
			.parent(cellAnalyzer)
			.display(
				ModBlocks.DNA_EXTRACTOR.get(),
				ModAdvancementLang.EXTRACTOR_TITLE.toComponent(),
				ModAdvancementLang.EXTRACTOR_DESC.toComponent(),
			)
			.hasAny(ModBlocks.DNA_EXTRACTOR, ModItems.DNA_HELIX)
			.save(ModAdvancements.DNA_EXTRACTOR)

		val dnaDecryptor = advancement()
			.parent(dnaExtractor)
			.display(
				ModBlocks.DNA_DECRYPTOR.get(),
				ModAdvancementLang.DECRYPTOR_TITLE.toComponent(),
				ModAdvancementLang.DECRYPTOR_DESC.toComponent(),
			)
			.has(ModBlocks.DNA_DECRYPTOR)
			.save(ModAdvancements.DNA_DECRYPTOR)

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
						.of(ModItems.DNA_HELIX.get())
						.withSubPredicate(
							ModItemSubPredicates.HELIX_GENE.get(),
							HelixGenePredicate.any()
						)
						.build()
				)
			)
			.save(ModAdvancements.DECRYPT_DNA)

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
						.of(ModItems.DNA_HELIX)
						.withSubPredicate(
							ModItemSubPredicates.HELIX_GENE.get(),
							HelixGenePredicate.blackDeath()
						)
						.build()
				)
			)
			.save(ModAdvancements.BLACK_DEATH)

		val plasmidInfuser = advancement()
			.parent(dnaExtractor)
			.display(
				ModBlocks.PLASMID_INFUSER.get(),
				ModAdvancementLang.INFUSER_TITLE.toComponent(),
				ModAdvancementLang.INFUSER_DESC.toComponent(),
			)
			.has(ModBlocks.PLASMID_INFUSER)
			.save(ModAdvancements.PLASMID_INFUSER)

		val plasmidInjector = advancement()
			.parent(plasmidInfuser)
			.display(
				ModBlocks.PLASMID_INJECTOR.get(),
				ModAdvancementLang.INJECTOR_TITLE.toComponent(),
				ModAdvancementLang.INJECTOR_DESC.toComponent(),
			)
			.has(ModBlocks.PLASMID_INJECTOR)
			.save(ModAdvancements.PLASMID_INJECTOR)

		val getGene = advancement()
			.parent(plasmidInjector)
			.display(
				ModItems.SYRINGE.withComponent(
					ModDataComponents.SPECIFIC_ENTITY.get(),
					SpecificEntityItemComponent(
						UUID.fromString("b0aa4edd-29e0-421f-b65c-be90055071b0"), //Arbitrary UUID
						Component.literal("A Fake Mob")
					)
				),
				ModAdvancementLang.GET_GENE_TITLE.toComponent(),
				ModAdvancementLang.GET_GENE_DESC.toComponent(),
				null,
				AdvancementType.GOAL,
				true, true, false
			)
			// Keep the legacy criterion name so completed progress survives upgrades.
			.addCriterion(
				"impossible",
				playerAction(ModAdvancements.GET_GENE)
			)
			.save(ModAdvancements.GET_GENE)

		advancement()
			.parent(getGene)
			.display(
				Items.ELYTRA,
				ModAdvancementLang.FLIGHT_TITLE.toComponent(),
				ModAdvancementLang.FLIGHT_DESC.toComponent(),
				AdvancementType.CHALLENGE,
			)
			.addCriterion(
				"impossible",
				playerAction(ModGenes.FLIGHT.location())
			)
			.save(ModAdvancements.GET_FLIGHT)

		advancement()
			.parent(getGene)
			.display(
				Items.JACK_O_LANTERN,
				ModAdvancementLang.SCARE_TITLE.toComponent(),
				ModAdvancementLang.SCARE_DESC.toComponent(),
				AdvancementType.CHALLENGE,
			)
			.addCriterion(
				"scare_spiders",
				playerAction(ModGenes.SCARE_SPIDERS.location())
			)
			.addCriterion(
				"scare_creepers",
				playerAction(ModGenes.SCARE_CREEPERS.location())
			)
			.addCriterion(
				"scare_skeletons",
				playerAction(ModGenes.SCARE_SKELETONS.location())
			)
			.addCriterion(
				"scare_zombies",
				playerAction(ModGenes.SCARE_ZOMBIES.location())
			)
			// Legacy completion satisfies every pair; new progress still requires all four genes.
			.addImpossibleCriterion()
			.requirements(
				AdvancementRequirements(
					listOf(
						listOf("impossible", "scare_spiders"),
						listOf("impossible", "scare_creepers"),
						listOf("impossible", "scare_skeletons"),
						listOf("impossible", "scare_zombies")
					)
				)
			)
			.save(ModAdvancements.GET_ALL_SCARE_GENES)

		advancement()
			.parent(getGene)
			.display(
				Items.SKELETON_SKULL,
				ModAdvancementLang.CRINGE_TITLE.toComponent(),
				ModAdvancementLang.CRINGE_DESC.toComponent(),
				AdvancementType.GOAL,
			)
			.addCriterion(
				"impossible",
				playerAction(ModGenes.CRINGE.location())
			)
			.save(ModAdvancements.GET_CRINGE)

		advancement()
			.parent(getGene)
			.display(
				Items.MILK_BUCKET,
				ModAdvancementLang.GET_MILKED_TITLE.toComponent(),
				ModAdvancementLang.GET_MILKED_DESC.toComponent(),
				AdvancementType.CHALLENGE,
			)
			.addCriterion(
				"impossible",
				playerAction(ModAdvancements.GET_MILKED)
			)
			.save(ModAdvancements.GET_MILKED)

		advancement()
			.parent(getGene)
			.display(
				Items.SLIME_BALL,
				ModAdvancementLang.SLIMY_TITLE.toComponent(),
				ModAdvancementLang.SLIMY_DESC.toComponent(),
				AdvancementType.CHALLENGE,
			)
			.addCriterion(
				"impossible",
				playerAction(ModAdvancements.TRIGGER_SLIMY_DEATH)
			)
			.save(ModAdvancements.TRIGGER_SLIMY_DEATH)

		val syringe = advancement()
			.parent(root)
			.display(
				ModItems.SYRINGE.get(),
				ModAdvancementLang.SYRINGE_TITLE.toComponent(),
				ModAdvancementLang.SYRINGE_DESC.toComponent(),
			)
			.hasAny(ModItems.SYRINGE, ModItems.METAL_SYRINGE)
			.save(ModAdvancements.SYRINGE)

		advancement()
			.parent(syringe)
			.display(
				ModBlocks.BLOOD_PURIFIER.get(),
				ModAdvancementLang.PURIFIER_TITLE.toComponent(),
				ModAdvancementLang.PURIFIER_DESC.toComponent(),
			)
			.has(ModBlocks.BLOOD_PURIFIER)
			.save(ModAdvancements.BLOOD_PURIFIER)

	}

}