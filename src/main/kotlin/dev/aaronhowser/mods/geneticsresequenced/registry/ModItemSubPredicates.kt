package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.advancement.HelixGenePredicate
import net.minecraft.advancements.critereon.ItemSubPredicate
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModItemSubPredicates {

    val ITEM_SUB_PREDICATES: DeferredRegister<ItemSubPredicate.Type<*>> =
        DeferredRegister.create(Registries.ITEM_SUB_PREDICATE_TYPE, GeneticsResequenced.ID)

    val HELIX_GENE: DeferredHolder<ItemSubPredicate.Type<*>, ItemSubPredicate.Type<HelixGenePredicate>> =
        ITEM_SUB_PREDICATES.register("helix_gene", Supplier { HelixGenePredicate.TYPE })

}