package dev.aaronhowser.mods.genetics_resequenced

import dev.aaronhowser.mods.genetics_resequenced.config.ClientConfig
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.compatibility.kubejs.GeneticsJsEventHandler
import dev.aaronhowser.mods.genetics_resequenced.registry.ModRegistries
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.common.NeoForge
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runWhenOn

@Mod(GeneticsResequenced.MOD_ID)
class GeneticsResequenced(
	modContainer: ModContainer
) {

	init {
		ModRegistries.register(MOD_BUS)

		if (ModList.get().isLoaded("kubejs")) {
			NeoForge.EVENT_BUS.register(GeneticsJsEventHandler)
		}

		runWhenOn(Dist.CLIENT) {
			val screenFactory = IConfigScreenFactory { container, screen -> ConfigurationScreen(container, screen) }
			modContainer.registerExtensionPoint(IConfigScreenFactory::class.java, screenFactory)
		}

		modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC)
		modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)
	}

	companion object {
		const val MOD_ID = "genetics_resequenced"
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		fun modId(path: String): Identifier =
			Identifier.fromNamespaceAndPath(MOD_ID, path)
	}
}