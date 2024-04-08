package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ModItems {

    val REGISTRY: DeferredRegister<Item> =
        DeferredRegister.create(ForgeRegistries.ITEMS, GeneticsResequenced.ID)

    val DRAGON_HEALTH_CRYSTAL by REGISTRY.registerObject("dragon_health_crystal") {
        DragonHealthCrystal
    }

}