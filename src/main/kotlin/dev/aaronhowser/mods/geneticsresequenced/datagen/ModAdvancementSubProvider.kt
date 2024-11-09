package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.advancement.ItemGenePredicate
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
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
import java.util.function.Consumer

class ModAdvancementSubProvider : AdvancementProvider.AdvancementGenerator {

    private lateinit var registries: HolderLookup.Provider
    private lateinit var saver: Consumer<AdvancementHolder>
    private lateinit var existingFileHelper: ExistingFileHelper

    private fun guide(string: String) = OtherUtil.modResource("guide/$string")

    override fun generate(
        registries: HolderLookup.Provider,
        saver: Consumer<AdvancementHolder>,
        existingFileHelper: ExistingFileHelper
    ) {

        this.registries = registries
        this.saver = saver
        this.existingFileHelper = existingFileHelper

        val root =
            Advancement.Builder.advancement()
                .display(
                    ModItems.SCRAPER.get(),
                    Component.literal("Genetics: Resequenced"),
                    ModLanguageProvider.Advancements.SCRAPER_DESC.toComponent(),
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
                .build(guide("root"))
                .add()

        val cellAnalyzer =
            Advancement.Builder.advancement()
                .parent(root)
                .display(
                    ModBlocks.CELL_ANALYZER.get(),
                    ModLanguageProvider.Advancements.ANALYZER_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.ANALYZER_DESC.toComponent(),
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
                .build(guide("cell_analyzer"))
                .add()

        val dnaExtractor =
            Advancement.Builder.advancement()
                .parent(cellAnalyzer)
                .display(
                    ModBlocks.DNA_EXTRACTOR.get(),
                    ModLanguageProvider.Advancements.EXTRACTOR_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.EXTRACTOR_DESC.toComponent(),
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
                .build(guide("dna_extractor"))
                .add()

        val dnaDecryptor =
            Advancement.Builder.advancement()
                .parent(dnaExtractor)
                .display(
                    ModBlocks.DNA_DECRYPTOR.get(),
                    ModLanguageProvider.Advancements.DECRYPTOR_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.DECRYPTOR_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "dna_decryptor",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.DNA_DECRYPTOR.get())
                )
                .build(guide("dna_decryptor"))
                .add()

        val decryptDna =
            Advancement.Builder.advancement()
                .parent(dnaDecryptor)
                .display(
                    ModItems.DNA_HELIX.get(),
                    ModLanguageProvider.Advancements.DECRYPT_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.DECRYPT_DESC.toComponent(),
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
                            .withSubPredicate(ModItemSubPredicates.ITEM_GENE.get(), ItemGenePredicate.any())
                            .build()
                    )
                )
                .build(guide("decrypt_dna"))
                .add()

        val blackDeath =
            Advancement.Builder.advancement()
                .parent(decryptDna)
                .display(
                    Items.WITHER_ROSE,
                    ModLanguageProvider.Advancements.BLACK_DEATH_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.BLACK_DEATH_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "impossible",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("black_death"))
                .add()

        val plasmidInfuser =
            Advancement.Builder.advancement()
                .parent(dnaExtractor)
                .display(
                    ModBlocks.PLASMID_INFUSER.get(),
                    ModLanguageProvider.Advancements.INFUSER_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.INFUSER_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "plasmid_infuser",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.PLASMID_INFUSER.get())
                )
                .build(guide("plasmid_infuser"))
                .add()

        val plasmidInjector =
            Advancement.Builder.advancement()
                .parent(plasmidInfuser)
                .display(
                    ModBlocks.PLASMID_INJECTOR.get(),
                    ModLanguageProvider.Advancements.INJECTOR_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.INJECTOR_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "plasmid_injector",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.PLASMID_INJECTOR.get())
                )
                .build(guide("plasmid_injector"))
                .add()

        val getGene =
            Advancement.Builder.advancement()
                .parent(plasmidInjector)
                .display(
                    ModItems.SYRINGE.toStack().apply {
                        set(
                            ModDataComponents.SPECIFIC_ENTITY_COMPONENT,
                            SpecificEntityItemComponent(
                                UUID.fromString("b0aa4edd-29e0-421f-b65c-be90055071b0"), //Arbitrary UUID
                                Component.literal("A Fake Mob")
                            )
                        )
                    },
                    ModLanguageProvider.Advancements.GET_GENE_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.GET_GENE_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "impossible",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("get_gene"))
                .add()

        val getFlight =
            Advancement.Builder.advancement()
                .parent(getGene)
                .display(
                    Items.ELYTRA,
                    ModLanguageProvider.Advancements.FLIGHT_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.FLIGHT_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "impossible",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("get_flight"))
                .add()

        val getAllScares =
            Advancement.Builder.advancement()
                .parent(getGene)
                .display(
                    Items.JACK_O_LANTERN,
                    ModLanguageProvider.Advancements.SCARE_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.SCARE_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "impossible",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("get_all_scare_genes"))
                .add()

        val getCringe =
            Advancement.Builder.advancement()
                .parent(getGene)
                .display(
                    Items.SKELETON_SKULL,
                    ModLanguageProvider.Advancements.CRINGE_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.CRINGE_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "impossible",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("get_cringe"))
                .add()

        val getMilked =
            Advancement.Builder.advancement()
                .parent(getGene)
                .display(
                    Items.MILK_BUCKET,
                    ModLanguageProvider.Advancements.GET_MILKED_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.GET_MILKED_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "impossible",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("get_milked"))
                .add()

        val triggerSlimyDeath =
            Advancement.Builder.advancement()
                .parent(getGene)
                .display(
                    Items.SLIME_BALL,
                    ModLanguageProvider.Advancements.SLIMY_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.SLIMY_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "impossible",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("trigger_slimy_death"))
                .add()

        val syringe =
            Advancement.Builder.advancement()
                .parent(root)
                .display(
                    ModItems.SYRINGE.get(),
                    ModLanguageProvider.Advancements.SYRINGE_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.SYRINGE_DESC.toComponent(),
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
                .build(guide("syringe"))
                .add()

        val bloodPurifier =
            Advancement.Builder.advancement()
                .parent(syringe)
                .display(
                    ModBlocks.BLOOD_PURIFIER.get(),
                    ModLanguageProvider.Advancements.PURIFIER_TITLE.toComponent(),
                    ModLanguageProvider.Advancements.PURIFIER_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "blood_purifier",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.BLOOD_PURIFIER.get())
                )
                .build(guide("blood_purifier"))
                .add()

    }

    private fun AdvancementHolder.add(): AdvancementHolder {
        this@ModAdvancementSubProvider.saver.accept(this)
        return this
    }

}