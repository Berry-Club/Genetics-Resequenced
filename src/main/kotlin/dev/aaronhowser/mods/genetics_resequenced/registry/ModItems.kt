package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronItemRegistry
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.item.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems : AaronItemRegistry() {

	val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(GeneticsResequenced.MOD_ID)
	override fun getItemRegistry(): DeferredRegister.Items = ITEM_REGISTRY

	val SCRAPER: DeferredItem<ScraperItem> =
		register("scraper", ::ScraperItem, ScraperItem.DEFAULT_PROPERTIES)
	val SYRINGE: DeferredItem<SyringeItem> =
		register("syringe", ::SyringeItem, SyringeItem.DEFAULT_PROPERTIES)
	val METAL_SYRINGE: DeferredItem<MetalSyringeItem> =
		register("metal_syringe", ::MetalSyringeItem, MetalSyringeItem.DEFAULT_PROPERTIES)
	val GENE_CHECKER: DeferredItem<GeneCheckerItem> =
		register("gene_checker", ::GeneCheckerItem, GeneCheckerItem.DEFAULT_PROPERTIES)
	val ORGANIC_MATTER: DeferredItem<EntityDnaItem> =
		register("organic_matter", ::EntityDnaItem)
	val CELL: DeferredItem<EntityDnaItem> =
		register("cell", ::EntityDnaItem)
	val GMO_CELL: DeferredItem<GmoCell> =
		register("gmo_cell", ::GmoCell)
	val DNA_HELIX: DeferredItem<DnaHelixItem> =
		register("dna_helix", ::DnaHelixItem)
	val PLASMID: DeferredItem<PlasmidItem> =
		register("plasmid", ::PlasmidItem, PlasmidItem.DEFAULT_PROPERTIES)
	val ANTI_PLASMID: DeferredItem<AntiPlasmidItem> =
		register("anti_plasmid", ::AntiPlasmidItem, AntiPlasmidItem.DEFAULT_PROPERTIES)
	val OVERCLOCKER: DeferredItem<Item> =
		register("overclocker", ::Item, Item.Properties().stacksTo(8))
	val ANTI_FIELD_ORB: DeferredItem<AntiFieldOrbItem> =
		register("anti_field_orb", ::AntiFieldOrbItem, AntiFieldOrbItem.DEFAULT_PROPERTIES)
	val DRAGON_HEALTH_CRYSTAL: DeferredItem<DragonHealthCrystal> =
		register("dragon_health_crystal", ::DragonHealthCrystal, DragonHealthCrystal.DEFAULT_PROPERTIES)
	val FRIENDLY_SLIME_SPAWN_EGG: DeferredItem<SpawnEggItem> =
		registerSpawnEgg(
			"support_slime_spawn_egg",
			ModEntityTypes.SUPPORT_SLIME::get,
			0x00FF00,
			0x0000FF
		)

}