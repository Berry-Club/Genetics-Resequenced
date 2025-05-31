package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.neoforged.neoforge.common.DeferredSpawnEggItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

	val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(GeneticsResequenced.ID)

	val SCRAPER: DeferredItem<ScraperItem> = registerItem("scraper", ScraperItem())
	val SYRINGE: DeferredItem<SyringeItem> = registerItem("syringe", SyringeItem())
	val METAL_SYRINGE: DeferredItem<MetalSyringeItem> = registerItem("metal_syringe", MetalSyringeItem())
	val GENE_CHECKER: DeferredItem<GeneCheckerItem> = registerItem("gene_checker", GeneCheckerItem())
	val ORGANIC_MATTER: DeferredItem<EntityDnaItem> = registerItem("organic_matter", EntityDnaItem())
	val CELL: DeferredItem<EntityDnaItem> = registerItem("cell", EntityDnaItem())
	val GMO_CELL: DeferredItem<GmoCell> = registerItem("gmo_cell", GmoCell())
	val DNA_HELIX: DeferredItem<DnaHelixItem> = registerItem("dna_helix", DnaHelixItem())
	val PLASMID: DeferredItem<PlasmidItem> = registerItem("plasmid", PlasmidItem())
	val ANTI_PLASMID: DeferredItem<AntiPlasmidItem> = registerItem("anti_plasmid", AntiPlasmidItem())
	val OVERCLOCKER: DeferredItem<Item> = ITEM_REGISTRY.registerSimpleItem("overclocker", Item.Properties().stacksTo(8))
	val ANTI_FIELD_ORB: DeferredItem<AntiFieldOrbItem> = registerItem("anti_field_orb", AntiFieldOrbItem())
	val DRAGON_HEALTH_CRYSTAL: DeferredItem<DragonHealthCrystal> = registerItem("dragon_health_crystal", DragonHealthCrystal())
	val FRIENDLY_SLIME_SPAWN_EGG: DeferredItem<SpawnEggItem> =
		ITEM_REGISTRY.registerItem("support_slime_spawn_egg") {
			DeferredSpawnEggItem(
				ModEntityTypes.SUPPORT_SLIME,
				0x00FF00,
				0x0000FF,
				Item.Properties()
			)
		}

	private fun <T : Item> registerItem(name: String, item: T): DeferredItem<T> {
		return ITEM_REGISTRY.registerItem(name) { item }
	}

}