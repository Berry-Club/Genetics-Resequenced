package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator.CoalGeneratorMenu
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredRegister

object ModMenuTypes {

    val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
        DeferredRegister.create(BuiltInRegistries.MENU, GeneticsResequenced.ID)

    val COAL_GENERATOR = MENU_TYPE_REGISTRY.register("coal_generator") {
        MenuType(::CoalGeneratorMenu, FeatureFlags.DEFAULT_FLAGS)
    }

}