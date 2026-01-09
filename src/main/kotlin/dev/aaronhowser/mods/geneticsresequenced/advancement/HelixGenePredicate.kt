package dev.aaronhowser.mods.geneticsresequenced.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronExtensions.isTrue
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneDataComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.ItemStack

//TODO: Move away from something as hardcoded as this
data class HelixGenePredicate(
	val helixType: HelixType
) : ItemPredicate() {

	enum class HelixType : StringRepresentable {
		ANY,
		BLACK_DEATH

		;

		override fun getSerializedName(): String {
			return name
		}
	}

	override fun matches(stack: ItemStack): Boolean {
		return when (helixType) {
			HelixType.ANY -> GeneDataComponent.hasGene(stack)
			HelixType.BLACK_DEATH -> GeneDataComponent.getGeneRk(stack)?.isGene(ModGenes.BLACK_DEATH).isTrue()
		}
	}

	companion object {
		fun any() = HelixGenePredicate(HelixType.ANY)

		fun blackDeath(): HelixGenePredicate {
			return HelixGenePredicate(HelixType.BLACK_DEATH)
		}

		val CODEC: Codec<HelixGenePredicate> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					StringRepresentable.fromEnum { HelixType.entries.toTypedArray() }
						.fieldOf("helix_type")
						.forGetter(HelixGenePredicate::helixType),
				).apply(instance, ::HelixGenePredicate)
			}
	}

}