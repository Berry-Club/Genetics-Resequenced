package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, GeneticsResequenced.ID, existingFileHelper) {

    override fun registerModels() {

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
        basicItem(ModItems.GENE_CHECKER.get())

        spawnEggItem(ModItems.FRIENDLY_SLIME_SPAWN_EGG.get())

        syringe()
        metalSyringe()

    }

    private fun syringe() {
        val item = ModItems.SYRINGE.get()

        val emptyFlipped = getBuilder(OtherUtil.modResource("syringe_flipped_empty").toString())
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", OtherUtil.modResource("item/glass_syringe_flipped"))

        val full = getBuilder(OtherUtil.modResource("syringe_full").toString())
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", OtherUtil.modResource("item/glass_syringe_full"))

        val fullFlipped = getBuilder(OtherUtil.modResource("syringe_full_flipped").toString())
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", OtherUtil.modResource("item/glass_syringe_full_flipped"))

        getBuilder(BuiltInRegistries.ITEM.getKey(item).toString())
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", OtherUtil.modResource("item/glass_syringe_empty"))

            .override()
            .predicate(OtherUtil.modResource("injecting"), 1f)
            .predicate(OtherUtil.modResource("full"), 0f)
            .model(emptyFlipped)
            .end()

            .override()
            .predicate(OtherUtil.modResource("injecting"), 0f)
            .predicate(OtherUtil.modResource("full"), 1f)
            .model(full)
            .end()

            .override()
            .predicate(OtherUtil.modResource("injecting"), 1f)
            .predicate(OtherUtil.modResource("full"), 1f)
            .model(fullFlipped)
            .end()
    }

    private fun metalSyringe() {
        val item = ModItems.METAL_SYRINGE.get()

        val full = getBuilder(OtherUtil.modResource("metal_syringe_full").toString())
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", OtherUtil.modResource("item/metal_syringe_full"))

        getBuilder(BuiltInRegistries.ITEM.getKey(item).toString())
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", OtherUtil.modResource("item/metal_syringe_empty"))

            .override()
            .predicate(OtherUtil.modResource("full"), 1f)
            .model(full)
            .end()
    }

}
