package dev.aaronhowser.mods.geneticsresequenced

import dev.aaronhowser.mods.geneticsresequenced.attributes.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.configs.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultMobGenes
import dev.aaronhowser.mods.geneticsresequenced.enchantments.ModEnchantments
import dev.aaronhowser.mods.geneticsresequenced.entities.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects.ModEffects
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
            registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "geneticsresequenced-client.toml")
        }

        val registries = listOf(
            ModEntityTypes.ENTITY_TYPE_REGISTRY,
            ModAttributes.ATTRIBUTE_REGISTRY,
            ModEffects.EFFECT_REGISTRY,
            ModBlocks.BLOCK_REGISTRY,
            ModBlockEntities.BLOCK_ENTITY_REGISTRY,
            ModItems.ITEM_REGISTRY,
            ModEnchantments.ENCHANTMENT_REGISTRY,
            ModPotions.POTION_REGISTRY,
            ModMenuTypes.MENU_TYPE_REGISTRY,
            ModRecipes.RECIPE_SERIALIZERS_REGISTRY,
            ModRecipes.RECIPE_TYPES_REGISTRY
        )

        for (registry in registries) {
            registry.register(MOD_BUS)
        }

        ModPacketHandler.setup()

        DefaultGenes.registerDefaultGenes()
        DefaultGenes.setGeneRequirements()
        DefaultMobGenes.registerDefaultGenes()
    }

}