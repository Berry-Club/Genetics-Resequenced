package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData.Companion.addTemporaryGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData.Companion.removeTemporaryGene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneChangeKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.latvian.mods.kubejs.event.EventGroup
import dev.latvian.mods.kubejs.event.EventHandler
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry
import dev.latvian.mods.kubejs.script.BindingRegistry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

@Suppress("unused")
class GeneticsJS : KubeJSPlugin {

	override fun registerBindings(bindings: BindingRegistry) {
		bindings.add("GeneticsJS", GeneticsJS::class.java)
	}

	override fun registerServerRegistries(registry: ServerRegistryRegistry) {
		registry.register(ModGenes.GENE_REGISTRY_KEY, Gene.DIRECT_CODEC, Gene::class.java)
	}

	companion object {
		private val EVENT_GROUP: EventGroup = EventGroup.of("GeneticsEvents")
		val GENE_CHANGED: EventHandler = EVENT_GROUP.server("geneChanged") { GeneChangeKubeEvent::class.java }.hasResult()

		@JvmStatic
		fun hasGene(entity: Entity, geneRk: ResourceKey<Gene>): Boolean {
			return entity.hasGene(geneRk)
		}

		@JvmStatic
		fun addGene(entity: Entity, geneRk: ResourceKey<Gene>): Boolean {
			if (entity !is LivingEntity) return false
			return entity.addGene(geneRk)
		}

		@JvmStatic
		fun addTemporaryGene(entity: Entity, geneRk: ResourceKey<Gene>, tickDuration: Int): Boolean {
			if (entity !is LivingEntity) return false
			return entity.addTemporaryGene(geneRk, tickDuration)
		}

		@JvmStatic
		fun removeGene(entity: Entity, geneRk: ResourceKey<Gene>): Boolean {
			if (entity !is LivingEntity) return false
			return entity.removeGene(geneRk)
		}

		@JvmStatic
		fun removeTemporaryGene(entity: Entity, geneRk: ResourceKey<Gene>): Boolean {
			if (entity !is LivingEntity) return false
			return entity.removeTemporaryGene(geneRk)
		}
	}

}