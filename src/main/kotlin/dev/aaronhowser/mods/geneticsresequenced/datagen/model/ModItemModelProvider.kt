package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
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

    companion object {
        val syringeFilled = OtherUtil.modResource("filled")
        val syringeUsing = OtherUtil.modResource("using")
    }

    private fun syringe() {

        val flipped = getBuilder("syringe_flipped")
            .texture("layer0", OtherUtil.modResource("item/glass_syringe_flipped"))

        val full = getBuilder("syringe_full")
            .texture("layer0", OtherUtil.modResource("item/glass_syringe_full"))

        val flippedInjecting = getBuilder("syringe_full_flipped")
            .texture("layer0", OtherUtil.modResource("item/glass_syringe_full_flipped"))

        val syringe = getBuilder("syringe")
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", OtherUtil.modResource("item/glass_syringe"))

            .override()
            .predicate(modLoc("full"), 0f)
            .predicate(modLoc("injecting"), 1f)
            .model(flipped)
            .end()

            .override()
            .predicate(modLoc("full"), 1f)
            .predicate(modLoc("injecting"), 0f)
            .model(full)
            .end()

            .override()
            .predicate(modLoc("full"), 1f)
            .predicate(modLoc("injecting"), 1f)
            .model(flippedInjecting)
            .end()

    }

    private fun metalSyringe() {

    }
}
