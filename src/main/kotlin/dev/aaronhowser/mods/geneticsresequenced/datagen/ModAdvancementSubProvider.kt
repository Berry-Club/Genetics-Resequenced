package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
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
                    ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/adventure.png"),
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


    }

    private fun AdvancementHolder.add(): AdvancementHolder {
        this@ModAdvancementSubProvider.saver.accept(this)
        return this
    }

}