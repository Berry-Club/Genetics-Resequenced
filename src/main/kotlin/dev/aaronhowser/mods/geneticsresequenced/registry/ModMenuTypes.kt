package dev.aaronhowser.mods.geneticsresequenced.registry

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
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModMenuTypes {

	val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
		DeferredRegister.create(BuiltInRegistries.MENU, GeneticsResequenced.ID)

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

	fun registerScreens(event: RegisterMenuScreensEvent) {
		event.register(COAL_GENERATOR.get(), ::CoalGeneratorScreen)
		event.register(CELL_ANALYZER.get(), ::CellAnalyzerScreen)
		event.register(DNA_EXTRACTOR.get(), ::DnaExtractorScreen)
		event.register(DNA_DECRYPTOR.get(), ::DnaDecryptorScreen)
		event.register(PLASMID_INFUSER.get(), ::PlasmidInfuserScreen)
		event.register(PLASMID_INJECTOR.get(), ::PlasmidInjectorScreen)
		event.register(BLOOD_PURIFIER.get(), ::BloodPurifierScreen)
		event.register(INCUBATOR.get(), ::IncubatorScreen)
		event.register(ADVANCED_INCUBATOR.get(), ::AdvancedIncubatorScreen)
	}

	fun <T : AbstractContainerMenu> register(name: String, constructor: MenuType.MenuSupplier<T>): DeferredHolder<MenuType<*>, MenuType<T>> {
		return MENU_TYPE_REGISTRY.register(name, Supplier { MenuType(constructor, FeatureFlags.DEFAULT_FLAGS) })
	}

}