package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, GeneticsResequenced.ID, existingFileHelper) {

    override fun registerModels() {

        syringe()
        metalSyringe()

        basicItem(ModItems.SCRAPER.get())
        basicItem(ModItems.ORGANIC_MATTER.get())
        basicItem(ModItems.CELL.get())
        basicItem(ModItems.GMO_CELL.get())
        basicItem(ModItems.DNA_HELIX.get())
        basicItem(ModItems.PLASMID.get())
        basicItem(ModItems.ANTI_PLASMID.get())
        basicItem(ModItems.OVERCLOCKER.get())
        basicItem(ModItems.ANTI_FIELD_ORB.get())
        basicItem(ModItems.DRAGON_HEALTH_CRYSTAL.get())

        this.withExistingParent(
            ModItems.FRIENDLY_SLIME_SPAWN_EGG.id.path,
            mcLoc("item/template_spawn_egg")
        )

    }

    private fun syringe() {

    }

    private fun metalSyringe() {

    }
}
