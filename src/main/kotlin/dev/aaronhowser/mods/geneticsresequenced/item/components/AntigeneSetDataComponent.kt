package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.resources.ResourceKey

data class AntigeneSetDataComponent(
	val antigenes: List<ResourceKey<Gene>>
) : PseudoDataComponent<AntigeneSetDataComponent, AntigeneSetDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<AntigeneSetDataComponent>(OtherUtil.modResource("antigenes")) {
		val CODEC: Codec<AntigeneSetDataComponent> =
			ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
				.listOf()
				.xmap(::AntigeneSetDataComponent, AntigeneSetDataComponent::antigenes)

		override fun getCodec(): Codec<AntigeneSetDataComponent> = CODEC
	}

	override val type: Type = Type
}