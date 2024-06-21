package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.AntiFieldOrbItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.ScraperItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(GeneticsResequenced.ID)

    val SCRAPER: DeferredItem<ScraperItem> =
        ITEM_REGISTRY.registerItem("scraper") { ScraperItem() }
    val SYRINGE: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("syringe")
    val METAL_SYRINGE: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("metal_syringe")
    val ORGANIC_MATTER: DeferredItem<EntityDnaItem> =
        ITEM_REGISTRY.registerItem("organic_matter") { EntityDnaItem() }
    val CELL: DeferredItem<EntityDnaItem> =
        ITEM_REGISTRY.registerItem("cell") { EntityDnaItem() }
    val GMO_CELL: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("gmo_cell")
    val DNA_HELIX: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("dna_helix")
    val PLASMID: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("plasmid")
    val ANTI_PLASMID: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("anti_plasmid")
    val OVERCLOCKER: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("overclocker")
    val ANTI_FIELD_ORB: DeferredItem<Item> =
        ITEM_REGISTRY.registerItem("anti_field_orb") { AntiFieldOrbItem() }
    val DRAGON_HEALTH_CRYSTAL: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("dragon_health_crystal")
    val FRIENDLY_SLIME_SPAWN_EGG: DeferredItem<Item> =
        ITEM_REGISTRY.registerSimpleItem("support_slime_spawn_egg")

}