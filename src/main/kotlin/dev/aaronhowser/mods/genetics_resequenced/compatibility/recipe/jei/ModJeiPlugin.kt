package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.ModJeiInformationRecipes
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype.EntityDnaSubtypeInterpreter
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype.GeneSubtypeInterpreter
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype.GmoCellSubtypeInterpreter
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype.PlasmidSubtypeInterpreter
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IModInfoRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.resources.Identifier
import net.neoforged.fml.ModList

@JeiPlugin
class ModJeiPlugin : IModPlugin {

	override fun registerItemSubtypes(registration: ISubtypeRegistration) {
		if (IS_EMI_INSTALLED) return

		registration.registerSubtypeInterpreter(ModItems.CELL.get(), EntityDnaSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.ORGANIC_MATTER.get(), EntityDnaSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.DNA_HELIX.get(), GeneSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.GMO_CELL.get(), GmoCellSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.PLASMID.get(), PlasmidSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.ANTI_PLASMID.get(), PlasmidSubtypeInterpreter)
	}

	// TODO
//	override fun registerIngredients(registration: IModIngredientRegistration) {
//
//		val subtypeManager = registration.subtypeManager
//		val colorHelper = registration.colorHelper
//
//		val stackHelper =
//
//			registration.register(
//				ModIngredientTypes.POTION_TAG.get(),
//
//				)
//
//	}

	override fun registerModInfo(modAliasRegistration: IModInfoRegistration) {
		modAliasRegistration.addModAliases(GeneticsResequenced.MOD_ID, "gene", "genetics", "gr")
	}

	override fun registerRecipes(registration: IRecipeRegistration) {

		ModJeiInformationRecipes.addInformationRecipes(registration)

	}

	override fun getPluginUid(): Identifier = PLUGIN_UID

	companion object {
		val PLUGIN_UID = GeneticsResequenced.modId("jei_plugin")

		val IS_EMI_INSTALLED by lazy { ModList.get().isLoaded("emi") }
	}


}
