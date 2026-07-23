package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.cast
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category.*
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.ModJeiInformationRecipes
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.*
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator.DupeCellJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator.GmoJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator.SetPotionEntityJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator.VirusJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype.EntityDnaSubtypeInterpreter
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype.GeneSubtypeInterpreter
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype.GmoCellSubtypeInterpreter
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype.PlasmidSubtypeInterpreter
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.BasicIncubatorRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.*
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.neoforged.fml.ModList

@JeiPlugin
class ModJeiPlugin : IModPlugin {

	override fun registerCategories(registration: IRecipeCategoryRegistration) {
		val guiHelper = registration.jeiHelpers.guiHelper

		registration.addRecipeCategories(
			PurifySyringeJeiCategory(BLOOD_PURIFIER, guiHelper),
			OrganicMatterToCellJeiCategory(CELL_ANALYZER, guiHelper),
			CellToHelixJeiCategory(DNA_EXTRACTOR, guiHelper),
			DecryptHelixJeiCategory(DNA_DECRYPTOR, guiHelper),
			PlasmidInfuserJeiCategory(PLASMID_INFUSER, guiHelper),
			PlasmidInjectorJeiCategory(PLASMID_INJECTOR, guiHelper),
			BasicIncubatorJeiCategory(INCUBATOR, guiHelper),
			DupeCellJeiCategory(CELL_DUPE, guiHelper),
			SetPotionEntityJeiCategory(SET_ENTITY, guiHelper),
			VirusJeiCategory(VIRUS, guiHelper),
			GmoJeiCategory(GMO, guiHelper)
		)
	}

	override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
		registration.addRecipeCatalyst(ModBlocks.BLOOD_PURIFIER, BLOOD_PURIFIER)
		registration.addRecipeCatalyst(ModBlocks.CELL_ANALYZER, CELL_ANALYZER)
		registration.addRecipeCatalyst(ModBlocks.DNA_EXTRACTOR, DNA_EXTRACTOR)
		registration.addRecipeCatalyst(ModBlocks.DNA_DECRYPTOR, DNA_DECRYPTOR)
		registration.addRecipeCatalyst(ModBlocks.PLASMID_INFUSER, PLASMID_INFUSER)
		registration.addRecipeCatalyst(ModBlocks.PLASMID_INJECTOR, PLASMID_INJECTOR)

		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, INCUBATOR)
		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, RecipeTypes.BREWING)
		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, CELL_DUPE)
		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, SET_ENTITY)
		registration.addRecipeCatalyst(ModBlocks.INCUBATOR, VIRUS)

		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, INCUBATOR)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, RecipeTypes.BREWING)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, CELL_DUPE)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, SET_ENTITY)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, VIRUS)
		registration.addRecipeCatalyst(ModBlocks.ADVANCED_INCUBATOR, GMO)
	}

	override fun registerItemSubtypes(registration: ISubtypeRegistration) {
		if (IS_EMI_INSTALLED) return

		registration.registerSubtypeInterpreter(ModItems.CELL.get(), EntityDnaSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.ORGANIC_MATTER.get(), EntityDnaSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.DNA_HELIX.get(), GeneSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.GMO_CELL.get(), GmoCellSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.PLASMID.get(), PlasmidSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.ANTI_PLASMID.get(), PlasmidSubtypeInterpreter)
	}

	override fun registerModInfo(modAliasRegistration: IModInfoRegistration) {
		modAliasRegistration.addModAliases(GeneticsResequenced.MOD_ID, "gene", "genetics", "gr")
	}

	override fun registerRecipes(registration: IRecipeRegistration) {

		ModJeiInformationRecipes.addInformationRecipes(registration)

		val recipeManager = Minecraft.getInstance().singleplayerServer?.recipeManager ?: return

		registration.addRecipes(BLOOD_PURIFIER, PurifySyringeJeiRecipe.getAllRecipes())
		registration.addRecipes(CELL_ANALYZER, OrganicMatterToCellJeiRecipe.getAllRecipes())
		registration.addRecipes(DNA_EXTRACTOR, CellToHelixJeiRecipe.getAllRecipes(recipeManager))
		registration.addRecipes(DNA_DECRYPTOR, DecryptHelixJeiRecipe.getAllRecipes())
		registration.addRecipes(PLASMID_INFUSER, PlasmidInfuserJeiRecipe.getAllRecipes())
		registration.addRecipes(PLASMID_INJECTOR, PlasmidInjectorJeiRecipe.getAllRecipes())
		registration.addRecipes(INCUBATOR, BasicIncubatorRecipe.getBasicRecipes(recipeManager))
		registration.addRecipes(CELL_DUPE, DupeCellJeiRecipe.getAllRecipes(recipeManager))
		registration.addRecipes(SET_ENTITY, SetPotionEntityJeiRecipe.getAllRecipes())
		registration.addRecipes(VIRUS, VirusJeiRecipe.getAllRecipes(recipeManager))
		registration.addRecipes(GMO, GmoJeiRecipe.getAllRecipes(recipeManager))
	}

	override fun getPluginUid(): Identifier = PLUGIN_UID

	companion object {
		val PLUGIN_UID = GeneticsResequenced.modId("jei_plugin")

		val BLOOD_PURIFIER: RecipeType<PurifySyringeJeiRecipe> = makeRecipeType("blood_purifier", PurifySyringeJeiRecipe::class.java)
		val CELL_ANALYZER: RecipeType<OrganicMatterToCellJeiRecipe> = makeRecipeType("cell_analyzer", OrganicMatterToCellJeiRecipe::class.java)
		val DNA_EXTRACTOR: RecipeType<CellToHelixJeiRecipe> = makeRecipeType("dna_extractor", CellToHelixJeiRecipe::class.java)
		val DNA_DECRYPTOR: RecipeType<DecryptHelixJeiRecipe> = makeRecipeType("dna_decryptor", DecryptHelixJeiRecipe::class.java)
		val PLASMID_INFUSER: RecipeType<PlasmidInfuserJeiRecipe> = makeRecipeType("plasmid_infuser", PlasmidInfuserJeiRecipe::class.java)
		val PLASMID_INJECTOR: RecipeType<PlasmidInjectorJeiRecipe> = makeRecipeType("plasmid_injector", PlasmidInjectorJeiRecipe::class.java)

		val INCUBATOR: RecipeType<RecipeHolder<BasicIncubatorRecipe>> = makeRecipeHolderType("incubator")
		val CELL_DUPE: RecipeType<DupeCellJeiRecipe> = makeRecipeType("cell_dupe", DupeCellJeiRecipe::class.java)
		val SET_ENTITY: RecipeType<SetPotionEntityJeiRecipe> = makeRecipeType("set_entity", SetPotionEntityJeiRecipe::class.java)
		val VIRUS: RecipeType<VirusJeiRecipe> = makeRecipeType("virus", VirusJeiRecipe::class.java)
		val GMO: RecipeType<GmoJeiRecipe> = makeRecipeType("gmo", GmoJeiRecipe::class.java)

		val IS_EMI_INSTALLED by lazy { ModList.get().isLoaded("emi") }

		private fun <T : Any> makeRecipeType(id: String, recipeClass: Class<T>): RecipeType<T> {
			return RecipeType.create(GeneticsResequenced.MOD_ID, id, recipeClass)
		}

		private fun <T : Recipe<*>> makeRecipeHolderType(id: String): RecipeType<RecipeHolder<T>> {
			return RecipeType.create(GeneticsResequenced.MOD_ID, id, RecipeHolder::class.java).cast()
		}
	}


}
