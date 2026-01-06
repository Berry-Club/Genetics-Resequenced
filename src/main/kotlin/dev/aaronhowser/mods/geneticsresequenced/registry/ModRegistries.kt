package dev.aaronhowser.mods.geneticsresequenced.registry

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

object ModRegistries {

	private val REGISTRIES: List<DeferredRegister<*>> = listOf(
		ModItems.ITEM_REGISTRY,
		ModBlocks.BLOCK_REGISTRY,
		ModBlockEntityTypes.BLOCK_ENTITY_REGISTRY,
		ModCreativeModeTabs.TABS_REGISTRY,
		ModAttributes.ATTRIBUTE_REGISTRY,
		ModEntityTypes.ENTITY_TYPE_REGISTRY,
		ModMenuTypes.MENU_TYPE_REGISTRY,
		ModEffects.EFFECT_REGISTRY,
		ModPotions.POTION_REGISTRY,
		ModRecipeTypes.RECIPE_TYPES_REGISTRY,
		ModRecipeSerializers.RECIPE_SERIALIZERS_REGISTRY
	)

	fun register(modBus: IEventBus) {
		REGISTRIES.forEach { it.register(modBus) }
	}

}