package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModItemLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder
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
				val regularItems = mutableListOf<Item>()
				val blockItems = mutableListOf<Item>()

				for (deferred in ModItems.ITEM_REGISTRY.entries) {
					val item = deferred.get()

					if (item is BlockItem) {
						blockItems.add(item)
					} else {
						regularItems.add(item)
					}
				}

				for (item in regularItems) {
					when (item) {
						ModItems.ORGANIC_MATTER.get() -> {
							output.accept(EntityDnaItem.getOrganicStack(EntityType.PIG))
							continue
						}

						ModItems.CELL.get() -> {
							output.accept(EntityDnaItem.getCell(EntityType.PIG))
							continue
						}

						ModItems.DNA_HELIX.get() -> {
							continue
						}
					}

					output.accept(item)
				}

				for (blockItem in blockItems) {
					output.accept(blockItem)
				}

				output.acceptAll(DnaHelixItem.getAllHelices(displayContext.holders))
			}
			.build()
	})

}