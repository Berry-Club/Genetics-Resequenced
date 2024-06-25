package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEffects.instance
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModPotions {

    val POTION_REGISTRY: DeferredRegister<Potion> =
        DeferredRegister.create(Registries.POTION, GeneticsResequenced.ID)

    val SUBSTRATE: DeferredHolder<Potion, Potion> =
        POTION_REGISTRY.register("substrate", Supplier {
            Potion("substrate".id, ModEffects.SUBSTRATE.instance)
        })
    val CELL_GROWTH: DeferredHolder<Potion, Potion> =
        POTION_REGISTRY.register("cell_growth", Supplier {
            Potion("cell_growth".id, ModEffects.CELL_GROWTH.instance)
        })
    val MUTATION: DeferredHolder<Potion, Potion> =
        POTION_REGISTRY.register("mutation", Supplier {
            Potion("mutation".id, ModEffects.MUTATION.instance)
        })
    val VIRAL_AGENTS: DeferredHolder<Potion, Potion> =
        POTION_REGISTRY.register("viral_agents", Supplier {
            Potion("viral_agents".id, ModEffects.VIRAL_AGENTS.instance)
        })

    val PANACEA: DeferredHolder<Potion, Potion> =
        POTION_REGISTRY.register("panacea", Supplier {
            Potion("panacea".id, ModEffects.PANACEA.instance)
        })
    val ZOMBIFY_VILLAGER: DeferredHolder<Potion, Potion> =
        POTION_REGISTRY.register("zombify_villager", Supplier {
            Potion("zombify_villager".id, ModEffects.ZOMBIFY_VILLAGER.instance)
        })

    // To prevent name conflicts if other mods happen to use the same potion names
    private val String.id
        get() = "${GeneticsResequenced.ID}.$this"

}