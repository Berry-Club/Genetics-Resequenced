package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.ModJeiInformationRecipes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.registration.IModInfoRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.fml.ModList

@JeiPlugin
class ModJeiPlugin : IModPlugin {

	override fun registerItemSubtypes(registration: ISubtypeRegistration) {
		if (IS_EMI_INSTALLED) return

		fun justUseComponentsJeez(item: ItemLike) {
			registration.registerSubtypeInterpreter(
				item.asItem(),
				object : ISubtypeInterpreter<ItemStack> {
					@Suppress("OVERRIDE_DEPRECATION")
					override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String = ""
					override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any = ingredient.components
				}
			)
		}

		justUseComponentsJeez(ModItems.CELL)
		justUseComponentsJeez(ModItems.GMO_CELL)
		justUseComponentsJeez(ModItems.DNA_HELIX)
		justUseComponentsJeez(ModItems.ORGANIC_MATTER)
		justUseComponentsJeez(ModItems.PLASMID)
		justUseComponentsJeez(ModItems.ANTI_PLASMID)
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