package dev.aaronhowser.mods.geneticsresequenced

import dev.aaronhowser.mods.geneticsresequenced.attributes.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.blockentities.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.effects.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.enchantments.ModEnchantments
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.recipes.ModRecipes
import dev.aaronhowser.mods.geneticsresequenced.screens.ModMenuTypes
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

    init {
        ModLoadingContext.get().apply {
            registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, "geneticsresequenced-server.toml")
        }

        ModAttributes.REGISTRY.register(MOD_BUS)
        ModEffects.REGISTRY.register(MOD_BUS)
        ModBlocks.REGISTRY.register(MOD_BUS)
        ModBlockEntities.REGISTRY.register(MOD_BUS)
        ModItems.REGISTRY.register(MOD_BUS)
        ModRecipes.SERIALIZERS.register(MOD_BUS)
        ModEnchantments.REGISTRY.register(MOD_BUS)
        ModMenuTypes.REGISTRY.register(MOD_BUS)

        ModPacketHandler.setup()
    }

}