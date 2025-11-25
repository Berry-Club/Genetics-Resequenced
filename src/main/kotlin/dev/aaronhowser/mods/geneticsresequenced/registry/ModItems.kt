package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronItemRegistry
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModItems : AaronItemRegistry() {

	val ITEM_REGISTRY: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, GeneticsResequenced.MOD_ID)
	override fun getItemRegistry(): DeferredRegister<Item> = ITEM_REGISTRY

	val SCRAPER: RegistryObject<ScraperItem> =
		register("scraper", ::ScraperItem, ScraperItem.DEFAULT_PROPERTIES)
	val SYRINGE: RegistryObject<SyringeItem> =
		register("syringe", ::SyringeItem, SyringeItem.DEFAULT_PROPERTIES)
	val METAL_SYRINGE: RegistryObject<MetalSyringeItem> =
		register("metal_syringe", ::MetalSyringeItem, MetalSyringeItem.DEFAULT_PROPERTIES)
	val GENE_CHECKER: RegistryObject<GeneCheckerItem> =
		register("gene_checker", ::GeneCheckerItem, GeneCheckerItem.DEFAULT_PROPERTIES)
	val ORGANIC_MATTER: RegistryObject<EntityDnaItem> =
		register("organic_matter", ::EntityDnaItem)
	val CELL: RegistryObject<EntityDnaItem> =
		register("cell", ::EntityDnaItem)
	val GMO_CELL: RegistryObject<GmoCell> =
		register("gmo_cell", ::GmoCell)
	val DNA_HELIX: RegistryObject<DnaHelixItem> =
		register("dna_helix", ::DnaHelixItem)
	val PLASMID: RegistryObject<PlasmidItem> =
		register("plasmid", ::PlasmidItem, PlasmidItem.DEFAULT_PROPERTIES)
	val ANTI_PLASMID: RegistryObject<AntiPlasmidItem> =
		register("anti_plasmid", ::AntiPlasmidItem, AntiPlasmidItem.DEFAULT_PROPERTIES)
	val OVERCLOCKER: RegistryObject<Item> =
		register("overclocker", ::Item, Item.Properties().stacksTo(8))
	val ANTI_FIELD_ORB: RegistryObject<AntiFieldOrbItem> =
		register("anti_field_orb", ::AntiFieldOrbItem, AntiFieldOrbItem.DEFAULT_PROPERTIES)
	val DRAGON_HEALTH_CRYSTAL: RegistryObject<DragonHealthCrystal> =
		register("dragon_health_crystal", ::DragonHealthCrystal, DragonHealthCrystal.DEFAULT_PROPERTIES)
	val FRIENDLY_SLIME_SPAWN_EGG: RegistryObject<SpawnEggItem> =
		registerSpawnEgg(
			"support_slime_spawn_egg",
			ModEntityTypes.SUPPORT_SLIME::get,
			0x00FF00,
			0x0000FF
		)

}