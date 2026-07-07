package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronMenuTypesRegistry
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.menu.MachineScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.advanced_incubator.AdvancedIncubatorMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.advanced_incubator.AdvancedIncubatorScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.blood_purifier.BloodPurifierMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.cell_analyzer.CellAnalyzerMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.coal_generator.CoalGeneratorScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.dna_decryptor.DnaDecryptorMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.dna_extractor.DnaExtractorMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.incubator.IncubatorMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.incubator.IncubatorScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.plasmid_injector.PlasmidInjectorMenu
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModMenuTypes : AaronMenuTypesRegistry() {

	val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
		DeferredRegister.create(BuiltInRegistries.MENU, GeneticsResequenced.MOD_ID)

	override fun getMenuTypeRegistry(): DeferredRegister<MenuType<*>> = MENU_TYPE_REGISTRY

	val COAL_GENERATOR: DeferredHolder<MenuType<*>, MenuType<CoalGeneratorMenu>> =
		register("coal_generator", ::CoalGeneratorMenu)

	val CELL_ANALYZER: DeferredHolder<MenuType<*>, MenuType<CellAnalyzerMenu>> =
		register("cell_analyzer", ::CellAnalyzerMenu)

	val DNA_EXTRACTOR: DeferredHolder<MenuType<*>, MenuType<DnaExtractorMenu>> =
		register("dna_extractor", ::DnaExtractorMenu)

	val DNA_DECRYPTOR: DeferredHolder<MenuType<*>, MenuType<DnaDecryptorMenu>> =
		register("dna_decryptor", ::DnaDecryptorMenu)

	val PLASMID_INFUSER: DeferredHolder<MenuType<*>, MenuType<PlasmidInfuserMenu>> =
		register("plasmid_infuser", ::PlasmidInfuserMenu)

	val PLASMID_INJECTOR: DeferredHolder<MenuType<*>, MenuType<PlasmidInjectorMenu>> =
		register("plasmid_injector", ::PlasmidInjectorMenu)

	val BLOOD_PURIFIER: DeferredHolder<MenuType<*>, MenuType<BloodPurifierMenu>> =
		register("blood_purifier", ::BloodPurifierMenu)

	val INCUBATOR: DeferredHolder<MenuType<*>, MenuType<IncubatorMenu>> =
		register("incubator", ::IncubatorMenu)

	val ADVANCED_INCUBATOR: DeferredHolder<MenuType<*>, MenuType<AdvancedIncubatorMenu>> =
		register("advanced_incubator", ::AdvancedIncubatorMenu)

	override fun registerScreens(event: RegisterMenuScreensEvent) {
		event.register(COAL_GENERATOR.get(), ::CoalGeneratorScreen)
		event.register(CELL_ANALYZER.get()) { menu, playerInventory, title ->
			MachineScreen(menu, playerInventory, title, MachineScreen.CELL_ANALYZER_BACKGROUND)
		}
		event.register(DNA_EXTRACTOR.get()) { menu, playerInventory, title ->
			MachineScreen(menu, playerInventory, title, MachineScreen.DNA_EXTRACTOR_BACKGROUND)
		}
		event.register(DNA_DECRYPTOR.get()) { menu, playerInventory, title ->
			MachineScreen(menu, playerInventory, title, MachineScreen.DNA_DECRYPTOR_BACKGROUND)
		}
		event.register(PLASMID_INFUSER.get()) { menu, playerInventory, title ->
			MachineScreen(menu, playerInventory, title, MachineScreen.PLASMID_INFUSER_BACKGROUND)
		}
		event.register(PLASMID_INJECTOR.get()) { menu, playerInventory, title ->
			MachineScreen(menu, playerInventory, title, MachineScreen.PLASMID_INJECTOR_BACKGROUND)
		}
		event.register(BLOOD_PURIFIER.get()) { menu, playerInventory, title ->
			MachineScreen(menu, playerInventory, title, MachineScreen.BASIC_BACKGROUND)
		}
		event.register(INCUBATOR.get(), ::IncubatorScreen)
		event.register(ADVANCED_INCUBATOR.get(), ::AdvancedIncubatorScreen)
	}

}
