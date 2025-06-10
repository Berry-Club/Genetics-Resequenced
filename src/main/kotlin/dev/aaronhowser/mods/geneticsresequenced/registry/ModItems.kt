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

	val SCRAPER: DeferredItem<ScraperItem> = registerNoArg("scraper", ::ScraperItem)
	val SYRINGE: DeferredItem<SyringeItem> = registerNoArg("syringe", ::SyringeItem)
	val METAL_SYRINGE: DeferredItem<MetalSyringeItem> = registerNoArg("metal_syringe", ::MetalSyringeItem)
	val GENE_CHECKER: DeferredItem<GeneCheckerItem> = registerNoArg("gene_checker", ::GeneCheckerItem)
	val ORGANIC_MATTER: DeferredItem<EntityDnaItem> = registerNoArg("organic_matter", ::EntityDnaItem)
	val CELL: DeferredItem<EntityDnaItem> = registerNoArg("cell", ::EntityDnaItem)
	val GMO_CELL: DeferredItem<GmoCell> = registerNoArg("gmo_cell", ::GmoCell)
	val DNA_HELIX: DeferredItem<DnaHelixItem> = registerNoArg("dna_helix", ::DnaHelixItem)
	val PLASMID: DeferredItem<PlasmidItem> = registerNoArg("plasmid", ::PlasmidItem)
	val ANTI_PLASMID: DeferredItem<AntiPlasmidItem> = registerNoArg("anti_plasmid", ::AntiPlasmidItem)
	val OVERCLOCKER: DeferredItem<Item> =
		ITEM_REGISTRY.registerSimpleItem("overclocker", Item.Properties().stacksTo(8))
	val ANTI_FIELD_ORB: DeferredItem<AntiFieldOrbItem> = registerNoArg("anti_field_orb", ::AntiFieldOrbItem)
	val DRAGON_HEALTH_CRYSTAL: DeferredItem<DragonHealthCrystal> = registerNoArg("dragon_health_crystal", ::DragonHealthCrystal)
	val FRIENDLY_SLIME_SPAWN_EGG: DeferredItem<SpawnEggItem> =
		ITEM_REGISTRY.registerItem("support_slime_spawn_egg") {
			DeferredSpawnEggItem(
				ModEntityTypes.SUPPORT_SLIME,
				0x00FF00,
				0x0000FF,
				Item.Properties()
			)
		}

	private fun <T : Item> registerNoArg(name: String, item: () -> T): DeferredItem<T> {
		return ITEM_REGISTRY.registerItem(name) { item() }
	}

}