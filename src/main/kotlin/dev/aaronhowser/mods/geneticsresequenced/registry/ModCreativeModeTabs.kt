package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModItemLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModCreativeModeTabs {

	val TABS_REGISTRY: DeferredRegister<CreativeModeTab> =
		DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GeneticsResequenced.MOD_ID)

	val MOD_TAB: RegistryObject<CreativeModeTab> =
		TABS_REGISTRY.register("creative_tab", Supplier {
			CreativeModeTab.builder()
				.title(ModItemLang.CREATIVE_TAB.toComponent())
				.icon { ModItems.SYRINGE.getDefaultInstance() }
				.displayItems { displayContext: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
					val regularItems =
						ModItems.ITEM_REGISTRY.entries - ModItems.DNA_HELIX - ModItems.ORGANIC_MATTER - ModItems.CELL

					val itemsToDisplay = buildList {
						addAll(regularItems.map { it.getDefaultInstance() })

						add(EntityDnaItem.getOrganicStack(EntityType.PIG))
						add(EntityDnaItem.getCell(EntityType.PIG))
						addAll(DnaHelixItem.getAllHelices(displayContext.holders))
						addAll(PlasmidItem.getAllPlasmids(displayContext.holders))
					}

					output.acceptAll(itemsToDisplay)
				}
				.build()
		})

}