package dev.aaronhowser.mods.genetics_resequenced

import dev.aaronhowser.mods.genetics_resequenced.config.ClientConfig
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.registry.ModRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(GeneticsResequenced.MOD_ID)
class GeneticsResequenced {

	init {
		// KotlinForForge 4 requires a no-argument constructor, so the context cannot be injected.
		@Suppress("DEPRECATION")
		val modLoadingContext = ModLoadingContext.get()

		ModRegistries.register(MOD_BUS)

//		runWhenOn(Dist.CLIENT) {
//			val screenFactory = IConfigScreenFactory { container, screen -> ConfigurationScreen(container, screen) }
//			modContainer.registerExtensionPoint(IConfigScreenFactory::class.java, screenFactory)
//		}

		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC)
		modLoadingContext.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)
	}

	companion object {
		const val MOD_ID = "genetics_resequenced"
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		fun modResource(path: String): ResourceLocation =
			ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}

}