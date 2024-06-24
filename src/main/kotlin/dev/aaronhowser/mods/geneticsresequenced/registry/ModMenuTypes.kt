package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.machines.blood_purifier.BloodPurifierMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machines.blood_purifier.BloodPurifierScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator.CoalGeneratorScreen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModMenuTypes {

    val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
        DeferredRegister.create(BuiltInRegistries.MENU, GeneticsResequenced.ID)

    val COAL_GENERATOR: DeferredHolder<MenuType<*>, MenuType<CoalGeneratorMenu>> =
        MENU_TYPE_REGISTRY.register("coal_generator", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                CoalGeneratorMenu(id, inv, buf)
            }
        })

    val BLOOD_PURIFIER: DeferredHolder<MenuType<*>, MenuType<BloodPurifierMenu>> =
        MENU_TYPE_REGISTRY.register("blood_purifier", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                BloodPurifierMenu(id, inv, buf)
            }
        })

    fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(COAL_GENERATOR.get(), ::CoalGeneratorScreen)
        event.register(BLOOD_PURIFIER.get(), ::BloodPurifierScreen)
    }

}