package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronMenuTypesRegistry
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator.AdvancedIncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator.AdvancedIncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.blood_purifier.BloodPurifierMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.blood_purifier.BloodPurifierScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.cell_analyzer.CellAnalyzerMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.cell_analyzer.CellAnalyzerScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator.CoalGeneratorScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_decryptor.DnaDecryptorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_decryptor.DnaDecryptorScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_extractor.DnaExtractorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_extractor.DnaExtractorScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.incubator.IncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.incubator.IncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser.PlasmidInfuserScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_injector.PlasmidInjectorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_injector.PlasmidInjectorScreen
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModMenuTypes : AaronMenuTypesRegistry() {

	val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
		DeferredRegister.create(ForgeRegistries.MENU_TYPES, GeneticsResequenced.ID)

	override fun getMenuTypeRegistry(): DeferredRegister<MenuType<*>> = MENU_TYPE_REGISTRY

	val COAL_GENERATOR: RegistryObject<MenuType<CoalGeneratorMenu>> =
		register("coal_generator", ::CoalGeneratorMenu)

	val CELL_ANALYZER: RegistryObject<MenuType<CellAnalyzerMenu>> =
		register("cell_analyzer", ::CellAnalyzerMenu)

	val DNA_EXTRACTOR: RegistryObject<MenuType<DnaExtractorMenu>> =
		register("dna_extractor", ::DnaExtractorMenu)

	val DNA_DECRYPTOR: RegistryObject<MenuType<DnaDecryptorMenu>> =
		register("dna_decryptor", ::DnaDecryptorMenu)

	val PLASMID_INFUSER: RegistryObject<MenuType<PlasmidInfuserMenu>> =
		register("plasmid_infuser", ::PlasmidInfuserMenu)

	val PLASMID_INJECTOR: RegistryObject<MenuType<PlasmidInjectorMenu>> =
		register("plasmid_injector", ::PlasmidInjectorMenu)

	val BLOOD_PURIFIER: RegistryObject<MenuType<BloodPurifierMenu>> =
		register("blood_purifier", ::BloodPurifierMenu)

	val INCUBATOR: RegistryObject<MenuType<IncubatorMenu>> =
		register("incubator", ::IncubatorMenu)

	val ADVANCED_INCUBATOR: RegistryObject<MenuType<AdvancedIncubatorMenu>> =
		register("advanced_incubator", ::AdvancedIncubatorMenu)

	override fun registerScreens(event: FMLClientSetupEvent) {
		event.enqueueWork {
			MenuScreens.register(COAL_GENERATOR.get(), ::CoalGeneratorScreen)
			MenuScreens.register(COAL_GENERATOR.get(), ::CoalGeneratorScreen)
			MenuScreens.register(CELL_ANALYZER.get(), ::CellAnalyzerScreen)
			MenuScreens.register(DNA_EXTRACTOR.get(), ::DnaExtractorScreen)
			MenuScreens.register(DNA_DECRYPTOR.get(), ::DnaDecryptorScreen)
			MenuScreens.register(PLASMID_INFUSER.get(), ::PlasmidInfuserScreen)
			MenuScreens.register(PLASMID_INJECTOR.get(), ::PlasmidInjectorScreen)
			MenuScreens.register(BLOOD_PURIFIER.get(), ::BloodPurifierScreen)
			MenuScreens.register(INCUBATOR.get(), ::IncubatorScreen)
			MenuScreens.register(ADVANCED_INCUBATOR.get(), ::AdvancedIncubatorScreen)
		}
	}

}