package dev.aaronhowser.mods.geneticsresequenced.registries

import net.minecraftforge.eventbus.api.IEventBus

object ModRegistries {

    private val registries = listOf(
        ModEntityTypes.ENTITY_TYPE_REGISTRY,
        ModAttributes.ATTRIBUTE_REGISTRY,
        ModEffects.EFFECT_REGISTRY,
        ModBlocks.BLOCK_REGISTRY,
        ModBlockEntities.BLOCK_ENTITY_REGISTRY,
        ModItems.ITEM_REGISTRY,
        ModEnchantments.ENCHANTMENT_REGISTRY,
        ModPotions.POTION_REGISTRY,
        ModMenuTypes.MENU_TYPE_REGISTRY,
        ModRecipeSerializers.RECIPE_SERIALIZERS_REGISTRY,
        ModRecipeTypes.RECIPE_TYPES_REGISTRY,
        GeneRegistry.GENES_REGISTRY
    )

    fun register(modBus: IEventBus) {
        registries.forEach {
            it.register(modBus)
        }
    }

}