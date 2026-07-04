package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

object ModItems {

	val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(GeneticsResequenced.MOD_ID)

	val SCRAPER: DeferredItem<ScraperItem> =
		register("scraper", ::ScraperItem) { Item.Properties().durability(200).enchantable(5) }
	val SYRINGE: DeferredItem<SyringeItem> =
		register("syringe", ::SyringeItem) { Item.Properties().stacksTo(1) }
	val METAL_SYRINGE: DeferredItem<MetalSyringeItem> =
		register("metal_syringe", ::MetalSyringeItem)
	val GENE_CHECKER: DeferredItem<GeneCheckerItem> =
		register("gene_checker", ::GeneCheckerItem) { Item.Properties().stacksTo(1) }
	val ORGANIC_MATTER: DeferredItem<EntityDnaItem> =
		register("organic_matter", ::EntityDnaItem)
	val CELL: DeferredItem<EntityDnaItem> =
		register("cell", ::EntityDnaItem)
	val GMO_CELL: DeferredItem<GmoCell> =
		register("gmo_cell", ::GmoCell)
	val DNA_HELIX: DeferredItem<DnaHelixItem> =
		register("dna_helix", ::DnaHelixItem)
	val PLASMID: DeferredItem<PlasmidItem> =
		register("plasmid", ::PlasmidItem) { Item.Properties().stacksTo(1) }
	val ANTI_PLASMID: DeferredItem<AntiPlasmidItem> =
		register("anti_plasmid", ::AntiPlasmidItem) { Item.Properties().stacksTo(1) }
	val OVERCLOCKER: DeferredItem<Item> =
		register("overclocker", ::Item) { Item.Properties().stacksTo(8) }
	val ANTI_FIELD_ORB: DeferredItem<AntiFieldOrbItem> =
		register("anti_field_orb", ::AntiFieldOrbItem) { Item.Properties().stacksTo(1) }
	val DRAGON_HEALTH_CRYSTAL: DeferredItem<DragonHealthCrystal> =
		register("dragon_health_crystal", ::DragonHealthCrystal, DragonHealthCrystal.DEFAULT_PROPERTIES)
	val FRIENDLY_SLIME_SPAWN_EGG: DeferredItem<SpawnEggItem> =
		register(
			"support_slime_spawn_egg",
			::SpawnEggItem
		) { Item.Properties().spawnEgg(ModEntityTypes.SUPPORT_SLIME.get()) }

	private fun <T : Item> register(
		name: String,
		factory: (Item.Properties) -> T,
		properties: () -> Item.Properties = { Item.Properties() }
	): DeferredItem<T> {
		return ITEM_REGISTRY.registerItem(
			name,
			Function { itemProperties -> factory(itemProperties) },
			Supplier { properties() }
		)
	}

}
