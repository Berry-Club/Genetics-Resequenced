package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModItemLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem
import dev.aaronhowser.mods.genetics_resequenced.item.PlasmidItem
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {

	val TABS_REGISTRY: DeferredRegister<CreativeModeTab> =
		DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, GeneticsResequenced.MOD_ID)

	val MOD_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> = TABS_REGISTRY.register("creative_tab", Supplier {
		CreativeModeTab.builder()
			.title(ModItemLang.CREATIVE_TAB.toComponent())
			.icon { ModItems.SYRINGE.toStack() }
			.displayItems { displayContext: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
				val regularItems =
					ModItems.ITEM_REGISTRY.entries - ModItems.DNA_HELIX - ModItems.ORGANIC_MATTER - ModItems.CELL

				val itemsToDisplay = buildList {
					addAll(regularItems.map { (it as DeferredItem).toStack() })

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