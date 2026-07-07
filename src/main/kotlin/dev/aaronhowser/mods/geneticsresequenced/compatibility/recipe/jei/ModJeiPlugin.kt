package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.ModJeiInformationRecipes
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.CellToHelixJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.DecryptHelixJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.OrganicMatterToCellJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.PlasmidInfuserJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.PlasmidInjectorJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.PurifySyringeJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator.BasicIncubatorJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator.DupeCellJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator.GmoJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator.SetPotionEntityJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator.VirusJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IModInfoRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.fml.ModList

@JeiPlugin
class ModJeiPlugin : IModPlugin {

	override fun registerCategories(registration: IRecipeCategoryRegistration) {
		val guiHelper = registration.jeiHelpers.guiHelper

		registration.addRecipeCategories(
			PurifySyringeJeiRecipe.Category(guiHelper),
			OrganicMatterToCellJeiRecipe.Category(guiHelper),
			CellToHelixJeiRecipe.Category(guiHelper),
			DecryptHelixJeiRecipe.Category(guiHelper),
			PlasmidInfuserJeiRecipe.Category(guiHelper),
			PlasmidInjectorJeiRecipe.Category(guiHelper),
			BasicIncubatorJeiRecipe.Category(guiHelper),
			DupeCellJeiRecipe.Category(guiHelper),
			SetPotionEntityJeiRecipe.Category(guiHelper),
			VirusJeiRecipe.Category(guiHelper),
			GmoJeiRecipe.Category(guiHelper)
		)
	}

	override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
		registration.addRecipeCatalyst(ModBlocks.BLOOD_PURIFIER, PurifySyringeJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.CELL_ANALYZER, OrganicMatterToCellJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.DNA_EXTRACTOR, CellToHelixJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.DNA_DECRYPTOR, DecryptHelixJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.PLASMID_INFUSER, PlasmidInfuserJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.PLASMID_INJECTOR, PlasmidInjectorJeiRecipe.TYPE)

		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, BasicIncubatorJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, DupeCellJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, SetPotionEntityJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, VirusJeiRecipe.TYPE)

		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, BasicIncubatorJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, DupeCellJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, SetPotionEntityJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, VirusJeiRecipe.TYPE)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, GmoJeiRecipe.TYPE)
	}

	override fun registerItemSubtypes(registration: ISubtypeRegistration) {
		if (IS_EMI_INSTALLED) return

		fun justUseComponentsJeez(item: ItemLike) {
			registration.registerSubtypeInterpreter(
				item.asItem(),
				object : ISubtypeInterpreter<ItemStack> {
					@Suppress("OVERRIDE_DEPRECATION")
					override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String = ""
					override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? = ingredient.components
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

		val recipeManager = ClientUtil.localRegistryAccess
			?.let { net.minecraft.client.Minecraft.getInstance().level?.recipeManager }
			?: return

		registration.addRecipes(PurifySyringeJeiRecipe.TYPE, PurifySyringeJeiRecipe.getAllRecipes())
		registration.addRecipes(OrganicMatterToCellJeiRecipe.TYPE, OrganicMatterToCellJeiRecipe.getAllRecipes())
		registration.addRecipes(CellToHelixJeiRecipe.TYPE, CellToHelixJeiRecipe.getAllRecipes(recipeManager))
		registration.addRecipes(DecryptHelixJeiRecipe.TYPE, DecryptHelixJeiRecipe.getAllRecipes())
		registration.addRecipes(PlasmidInfuserJeiRecipe.TYPE, PlasmidInfuserJeiRecipe.getAllRecipes())
		registration.addRecipes(PlasmidInjectorJeiRecipe.TYPE, PlasmidInjectorJeiRecipe.getAllRecipes())
		registration.addRecipes(BasicIncubatorJeiRecipe.TYPE, BasicIncubatorJeiRecipe.getAllRecipes(recipeManager))
		registration.addRecipes(DupeCellJeiRecipe.TYPE, DupeCellJeiRecipe.getAllRecipes(recipeManager))
		registration.addRecipes(SetPotionEntityJeiRecipe.TYPE, SetPotionEntityJeiRecipe.getAllRecipes())
		registration.addRecipes(VirusJeiRecipe.TYPE, VirusJeiRecipe.getAllRecipes(recipeManager))
		registration.addRecipes(GmoJeiRecipe.TYPE, GmoJeiRecipe.getAllRecipes(recipeManager))
	}

	override fun getPluginUid(): ResourceLocation = PLUGIN_UID

	companion object {
		val PLUGIN_UID = GeneticsResequenced.modResource("jei_plugin")

		val IS_EMI_INSTALLED by lazy { ModList.get().isLoaded("emi") }
	}


}
