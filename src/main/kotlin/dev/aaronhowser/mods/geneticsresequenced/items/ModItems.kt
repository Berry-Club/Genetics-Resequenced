package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ModItems {

    val MOD_TAB: CreativeModeTab = object : CreativeModeTab(GeneticsResequenced.ID) {
        override fun makeIcon(): ItemStack {
            return ItemStack(DRAGON_HEALTH_CRYSTAL)
        }
    }

    val ITEM_REGISTRY: DeferredRegister<Item> =
        DeferredRegister.create(ForgeRegistries.ITEMS, GeneticsResequenced.ID)

    val DRAGON_HEALTH_CRYSTAL by ITEM_REGISTRY.registerObject("dragon_health_crystal") { DragonHealthCrystal }
    val ANTI_FIELD_ORB by ITEM_REGISTRY.registerObject("anti_field_orb") { AntiFieldOrbItem }
    val SCRAPER by ITEM_REGISTRY.registerObject("scraper") { ScraperItem }
    val SYRINGE by ITEM_REGISTRY.registerObject("syringe") { SyringeItem }
    val ORGANIC_MATTER by ITEM_REGISTRY.registerObject("organic_matter") { EntityDnaItem() }
    val CELL by ITEM_REGISTRY.registerObject("cell") { EntityDnaItem() }
    val DNA_HELIX by ITEM_REGISTRY.registerObject("dna_helix") { DnaHelixItem }
    val OVERCLOCKER by ITEM_REGISTRY.registerObject("overclocker") { Item(Item.Properties().tab(MOD_TAB).stacksTo(8)) }

}