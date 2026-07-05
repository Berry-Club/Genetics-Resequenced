package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.advancement.HelixGenePredicate
import dev.aaronhowser.mods.genetics_resequenced.advancement.SyringeGenesPredicate
import net.minecraft.core.component.predicates.DataComponentPredicate
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModItemSubPredicates {

	val DATA_COMPONENT_PREDICATES: DeferredRegister<DataComponentPredicate.Type<*>> =
		DeferredRegister.create(Registries.DATA_COMPONENT_PREDICATE_TYPE, GeneticsResequenced.MOD_ID)

	val HELIX_GENE: DeferredHolder<DataComponentPredicate.Type<*>, DataComponentPredicate.Type<HelixGenePredicate>> =
		DATA_COMPONENT_PREDICATES.register("helix_gene", Supplier { HelixGenePredicate.TYPE })

	val SYRINGE_GENES: DeferredHolder<DataComponentPredicate.Type<*>, DataComponentPredicate.Type<SyringeGenesPredicate>> =
		DATA_COMPONENT_PREDICATES.register("syringe_genes", Supplier { SyringeGenesPredicate.TYPE })

}
