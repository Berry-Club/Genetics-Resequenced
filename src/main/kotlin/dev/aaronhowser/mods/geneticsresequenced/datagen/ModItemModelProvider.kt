package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraftforge.client.model.generators.ItemModelBuilder
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.RegistryObject

class ModItemModelProvider(
    generator: DataGenerator,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(
    generator,
    GeneticsResequenced.ID,
    existingFileHelper
) {
    override fun registerModels() {
        simpleItem(ModItems.SCRAPER)
        simpleItem(ModItems.ORGANIC_MATTER)
        simpleItem(ModItems.CELL)
        simpleItem(ModItems.DNA_HELIX)
        simpleItem(ModItems.PLASMID)
        simpleItem(ModItems.OVERCLOCKER)
        simpleItem(ModItems.ANTI_FIELD_ORB)
        simpleItem(ModItems.DRAGON_HEALTH_CRYSTAL)

        // Syringe and Spawn Egg purposefully excluded
    }

    private fun simpleItem(item: RegistryObject<out Item>): ItemModelBuilder {
        return withExistingParent(
            item.id.path,
            ResourceLocation("item/generated")
        ).texture(
            "layer0",
            OtherUtil.modResource("item/" + item.id.path)
        )
    }

}