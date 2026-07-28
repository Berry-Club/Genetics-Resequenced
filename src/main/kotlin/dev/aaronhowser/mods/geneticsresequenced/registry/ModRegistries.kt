package dev.aaronhowser.mods.geneticsresequenced.registry

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object ModRegistries {

	private val REGISTRIES: List<DeferredRegister<*>> = listOf(
		ModDataComponents.DATA_COMPONENT_REGISTRY,
		ModItems.ITEM_REGISTRY,
		ModBlocks.BLOCK_REGISTRY,
		ModBlockEntityTypes.BLOCK_ENTITY_REGISTRY,
		ModCreativeModeTabs.TABS_REGISTRY,
		ModAttachmentTypes.ATTACHMENT_TYPES_REGISTRY,
		ModAttributes.ATTRIBUTE_REGISTRY,
		ModEntityTypes.ENTITY_TYPE_REGISTRY,
		ModMenuTypes.MENU_TYPE_REGISTRY,
		ModEffects.EFFECT_REGISTRY,
		ModPotions.POTION_REGISTRY,
		ModRecipeTypes.RECIPE_TYPES_REGISTRY,
		ModRecipeSerializers.RECIPE_SERIALIZERS_REGISTRY,
		ModItemSubPredicates.ITEM_SUB_PREDICATES
	)

	fun register(modBus: IEventBus) {
		REGISTRIES.forEach { it.register(modBus) }
	}

}