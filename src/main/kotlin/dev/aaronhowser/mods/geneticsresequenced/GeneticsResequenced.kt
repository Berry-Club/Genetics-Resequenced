package dev.aaronhowser.mods.geneticsresequenced

import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRegistries
import net.minecraft.core.RegistryAccess
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runWhenOn

@Mod(GeneticsResequenced.ID)
class GeneticsResequenced(
    modContainer: ModContainer
) {

    companion object {
        const val ID = "geneticsresequenced"
        val LOGGER: Logger = LogManager.getLogger(ID)

        //FIXME: This is DISGUSTING I HATE IT
        lateinit var registryAccess: RegistryAccess
    }

    init {
        ModRegistries.register(MOD_BUS)

        runWhenOn(Dist.CLIENT) {
            val screenFactory = IConfigScreenFactory { container, screen -> ConfigurationScreen(container, screen) }
            modContainer.registerExtensionPoint(IConfigScreenFactory::class.java, screenFactory)
        }

        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC)
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)
    }
}