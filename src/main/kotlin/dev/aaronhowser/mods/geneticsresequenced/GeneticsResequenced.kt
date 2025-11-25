package dev.aaronhowser.mods.geneticsresequenced

import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRegistries
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(GeneticsResequenced.MOD_ID)
class GeneticsResequenced {

	companion object {
		const val MOD_ID = "geneticsresequenced"
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)
	}

	init {
		ModRegistries.register(MOD_BUS)

//		runWhenOn(Dist.CLIENT) {
//			val screenFactory = IConfigScreenFactory { container, screen -> ConfigurationScreen(container, screen) }
//			modContainer.registerExtensionPoint(IConfigScreenFactory::class.java, screenFactory)
//		}

		val ctx = ModLoadingContext.get()

		ctx.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC)
		ctx.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)
	}
}