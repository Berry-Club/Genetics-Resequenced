package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs

import dev.aaronhowser.mods.geneticsresequenced.attachment.GeneCooldowns
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData.Companion.addTemporaryGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData.Companion.removeTemporaryGene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneAddedKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneCooldownKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneRemovedKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.ModifyEntityGenesKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.ModifyGeneRequirementsKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.TemporaryGeneAddedKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.TemporaryGeneRemovedKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.latvian.mods.kubejs.event.EventGroup
import dev.latvian.mods.kubejs.event.EventGroupRegistry
import dev.latvian.mods.kubejs.event.EventTargetType
import dev.latvian.mods.kubejs.event.TargetedEventHandler
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry
import dev.latvian.mods.kubejs.script.BindingRegistry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity

@Suppress("unused")
class GeneticsJS : KubeJSPlugin {

	override fun registerBindings(bindings: BindingRegistry) {
		bindings.add("GeneticsJS", GeneticsJS::class.java)
	}

	override fun registerServerRegistries(registry: ServerRegistryRegistry) {
		registry.register(ModGenes.GENE_REGISTRY_KEY, Gene.DIRECT_CODEC, Gene::class.java)
	}

	override fun registerEvents(registry: EventGroupRegistry) {
		registry.register(EVENT_GROUP)
	}

	companion object {
		private val EVENT_GROUP: EventGroup = EventGroup.of("GeneticsEvents")

		private val GENE_TARGET: EventTargetType<ResourceKey<Gene>> = EventTargetType.registryKey(ModGenes.GENE_REGISTRY_KEY, Gene::class.java)
		private val ENTITY_TYPE_TARGET: EventTargetType<ResourceKey<EntityType<*>>> = EventTargetType.registryKey(Registries.ENTITY_TYPE, EntityType::class.java)

		val GENE_ADDED_PRE: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("geneAddedPre") { GeneAddedKubeEvent.Pre::class.java }.hasResult().supportsTarget(GENE_TARGET)
		val GENE_ADDED_POST: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("geneAdded") { GeneAddedKubeEvent.Post::class.java }.supportsTarget(GENE_TARGET)
		val GENE_REMOVED_PRE: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("geneRemovedPre") { GeneRemovedKubeEvent.Pre::class.java }.hasResult().supportsTarget(GENE_TARGET)
		val GENE_REMOVED_POST: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("geneRemoved") { GeneRemovedKubeEvent.Post::class.java }.supportsTarget(GENE_TARGET)
		val TEMPORARY_GENE_ADDED_PRE: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("temporaryGeneAddedPre") { TemporaryGeneAddedKubeEvent.Pre::class.java }.hasResult().supportsTarget(GENE_TARGET)
		val TEMPORARY_GENE_ADDED_POST: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("temporaryGeneAdded") { TemporaryGeneAddedKubeEvent.Post::class.java }.supportsTarget(GENE_TARGET)
		val TEMPORARY_GENE_REMOVED_PRE: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("temporaryGeneRemovedPre") { TemporaryGeneRemovedKubeEvent.Pre::class.java }.hasResult().supportsTarget(GENE_TARGET)
		val TEMPORARY_GENE_REMOVED_POST: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("temporaryGeneRemoved") { TemporaryGeneRemovedKubeEvent.Post::class.java }.supportsTarget(GENE_TARGET)
		val GENE_COOLDOWN_ADDED: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("geneCooldownAdded") { GeneCooldownKubeEvent.Add::class.java }.hasResult().supportsTarget(GENE_TARGET)
		val GENE_COOLDOWN_REMOVED: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("geneCooldownRemoved") { GeneCooldownKubeEvent.Remove::class.java }.supportsTarget(GENE_TARGET)
		val MODIFY_GENE_WEIGHTS: TargetedEventHandler<ResourceKey<EntityType<*>>> = EVENT_GROUP.server("modifyGeneWeights") { ModifyEntityGenesKubeEvent::class.java }.supportsTarget(ENTITY_TYPE_TARGET)
		val MODIFY_GENE_REQUIREMENTS: TargetedEventHandler<ResourceKey<Gene>> = EVENT_GROUP.server("modifyGeneRequirements") { ModifyGeneRequirementsKubeEvent::class.java }.supportsTarget(GENE_TARGET)

		@JvmStatic
		fun hasGene(entity: LivingEntity, geneRk: ResourceKey<Gene>): Boolean {
			return entity.hasGene(geneRk)
		}

		@JvmStatic
		fun addGene(entity: LivingEntity, geneRk: ResourceKey<Gene>): Boolean {
			return entity.addGene(geneRk)
		}

		@JvmStatic
		fun addTemporaryGene(entity: LivingEntity, geneRk: ResourceKey<Gene>, tickDuration: Int): Boolean {
			return entity.addTemporaryGene(geneRk, tickDuration)
		}

		@JvmStatic
		fun removeGene(entity: LivingEntity, geneRk: ResourceKey<Gene>): Boolean {
			return entity.removeGene(geneRk)
		}

		@JvmStatic
		fun removeTemporaryGene(entity: LivingEntity, geneRk: ResourceKey<Gene>): Boolean {
			return entity.removeTemporaryGene(geneRk)
		}

		@JvmStatic
		fun addCooldown(entity: LivingEntity, geneRk: ResourceKey<Gene>, tickDuration: Int, notify: Boolean): Boolean {
			return GeneCooldowns.addCooldown(entity, geneRk, tickDuration, notify)
		}

		@JvmStatic
		fun removeCooldown(entity: LivingEntity, geneRk: ResourceKey<Gene>): Boolean {
			return GeneCooldowns.removeCooldown(entity, geneRk)
		}

	}

}
