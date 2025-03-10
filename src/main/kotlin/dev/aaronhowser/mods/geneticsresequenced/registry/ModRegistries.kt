package dev.aaronhowser.mods.geneticsresequenced.registry

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object ModRegistries {

    private val registries: List<DeferredRegister<out Any>> = listOf(
        ModDataComponents.DATA_COMPONENT_REGISTRY,
        ModItems.ITEM_REGISTRY,
        ModBlocks.BLOCK_REGISTRY,
        ModBlockEntities.BLOCK_ENTITY_REGISTRY,
        ModCreativeModeTabs.TABS_REGISTRY,
        ModAttachmentTypes.ATTACHMENT_TYPES_REGISTRY,
        ModAttributes.ATTRIBUTE_REGISTRY,
        ModEntityTypes.ENTITY_TYPE_REGISTRY,
        ModMenuTypes.MENU_TYPE_REGISTRY,
        ModEffects.EFFECT_REGISTRY,
        ModPotions.POTION_REGISTRY,
        ModRecipeTypes.RECIPE_TYPES_REGISTRY,
        ModRecipeSerializers.RECIPE_SERIALIZERS_REGISTRY,
        ModIngredientTypes.INGREDIENT_TYPE_REGISTRY,
        ModItemSubPredicates.ITEM_SUB_PREDICATES
    )

    fun register(modBus: IEventBus) {
        registries.forEach { it.register(modBus) }
    }

}