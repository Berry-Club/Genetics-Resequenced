package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
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

        val root = this.add(
            Advancement.Builder.advancement()
                .display(
                    ModItems.SYRINGE.get(),
                    Component.literal("Genetics: Resequenced"),
                    Component.translatable(ModLanguageProvider.Advancements.SCRAPER_DESC),
                    null,
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
        )

    }

    private fun add(advancement: AdvancementHolder): AdvancementHolder {
        this.saver.accept(advancement)
        return advancement
    }

}