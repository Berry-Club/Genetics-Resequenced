package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.subtypes.CellSubtypeInterpreter
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.subtypes.DnaHelixSubtypeInterpreter
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
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
					override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? = ingredient.components
					@Deprecated("Deprecated in Java", ReplaceWith("\"\""))
					override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String = ""
				}
			)
		}

		justUseComponentsJeez(ModItems.ORGANIC_MATTER)
		justUseComponentsJeez(ModItems.CELL)
		justUseComponentsJeez(ModItems.GMO_CELL)
		justUseComponentsJeez(ModItems.DNA_HELIX)
	}

	override fun getPluginUid(): ResourceLocation = PLUGIN_UID

	companion object {
		val PLUGIN_UID = OtherUtil.modResource("jei_plugin")

		val IS_EMI_INSTALLED by lazy { ModList.get().isLoaded("emi") }
	}


}