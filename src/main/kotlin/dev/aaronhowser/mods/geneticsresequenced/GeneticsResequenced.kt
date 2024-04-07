package dev.aaronhowser.mods.geneticsresequenced

import dev.aaronhowser.mods.geneticsresequenced.attribute.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.block.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.blockentity.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.item.ModItems
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.potion.ModEffects
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(GeneticsResequenced.ID)
object GeneticsResequenced {
    const val ID = "geneticsresequenced"

    val LOGGER: Logger = LogManager.getLogger(ID)

    val MOD_TAB: CreativeModeTab = object : CreativeModeTab(ID) {
        override fun makeIcon(): ItemStack {
            return ItemStack(ModItems.DRAGON_HEALTH_CRYSTAL)
        }
    }

    init {
        ModLoadingContext.get().apply {
            registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, "geneticsresequenced-server.toml")
        }

        ModAttributes.REGISTRY.register(MOD_BUS)
        ModEffects.REGISTRY.register(MOD_BUS)
        ModBlocks.REGISTRY.register(MOD_BUS)
        ModBlockEntities.REGISTRY.register(MOD_BUS)
        ModItems.REGISTRY.register(MOD_BUS)

        ModPacketHandler.setup()
    }

}