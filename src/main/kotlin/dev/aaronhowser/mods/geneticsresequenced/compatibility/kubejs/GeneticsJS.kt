package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin
import dev.latvian.mods.kubejs.script.BindingRegistry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity

@Suppress("unused")
class GeneticsJS : KubeJSPlugin {

	override fun registerBindings(bindings: BindingRegistry) {
		bindings.add("GeneticsJS", GeneticsJS::class.java)
	}

	companion object {
		@JvmStatic
		fun hasGene(entity: Entity, geneRk: ResourceKey<Gene>): Boolean {
			return entity.hasGene(geneRk)
		}
	}

}