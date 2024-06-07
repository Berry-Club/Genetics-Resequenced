package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.entities.ModEntityTypes
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.ForgeSpawnEggItem
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModItems {

    val MOD_TAB: CreativeModeTab = object : CreativeModeTab(GeneticsResequenced.ID) {
        override fun makeIcon(): ItemStack {
            return ItemStack(SYRINGE.get())
        }
    }

    val ITEM_REGISTRY: DeferredRegister<Item> =
        DeferredRegister.create(ForgeRegistries.ITEMS, GeneticsResequenced.ID)

    val SCRAPER: RegistryObject<ScraperItem> =
        ITEM_REGISTRY.register("scraper") { ScraperItem() }
    val SYRINGE: RegistryObject<SyringeItem> =
        ITEM_REGISTRY.register("syringe") { SyringeItem() }
    val METAL_SYRINGE: RegistryObject<MetalSyringeItem> =
        ITEM_REGISTRY.register("metal_syringe") { MetalSyringeItem() }
    val ORGANIC_MATTER: RegistryObject<EntityDnaItem> =
        ITEM_REGISTRY.register("organic_matter") { EntityDnaItem() }
    val CELL: RegistryObject<EntityDnaItem> =
        ITEM_REGISTRY.register("cell") { EntityDnaItem() }
    val GMO_CELL: RegistryObject<GmoCell> =
        ITEM_REGISTRY.register("gmo_cell") { GmoCell() }
    val DNA_HELIX: RegistryObject<DnaHelixItem> =
        ITEM_REGISTRY.register("dna_helix") { DnaHelixItem() }
    val PLASMID: RegistryObject<PlasmidItem> =
        ITEM_REGISTRY.register("plasmid") { PlasmidItem() }
    val ANTI_PLASMID: RegistryObject<AntiPlasmidItem> =
        ITEM_REGISTRY.register("anti_plasmid") { AntiPlasmidItem() }
    val OVERCLOCKER: RegistryObject<Item> =
        ITEM_REGISTRY.register("overclocker") { Item(Item.Properties().tab(MOD_TAB).stacksTo(8)) }
    val ANTI_FIELD_ORB: RegistryObject<AntiFieldOrbItem> =
        ITEM_REGISTRY.register("anti_field_orb") { AntiFieldOrbItem() }
    val DRAGON_HEALTH_CRYSTAL: RegistryObject<DragonHealthCrystal> =
        ITEM_REGISTRY.register("dragon_health_crystal") { DragonHealthCrystal() }
    val FRIENDLY_SLIME_SPAWN_EGG: RegistryObject<ForgeSpawnEggItem> =
        ITEM_REGISTRY.register("support_slime_spawn_egg") {
            ForgeSpawnEggItem(
                ModEntityTypes.SUPPORT_SLIME,
                0x00FF00,
                0x0000FF,
                Item.Properties().tab(MOD_TAB)
            )
        }

}