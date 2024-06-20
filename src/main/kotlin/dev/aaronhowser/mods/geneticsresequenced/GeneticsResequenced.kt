package dev.aaronhowser.mods.geneticsresequenced

import dev.aaronhowser.mods.geneticsresequenced.block.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registries.ModRegistries
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in neoforge.mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(GeneticsResequenced.ID)
object GeneticsResequenced {
    const val ID = "geneticsresequenced"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        ModRegistries.register(MOD_BUS)
    }
}