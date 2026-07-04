package dev.aaronhowser.mods.geneticsresequenced.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.advancements.criterion.SingleComponentItemPredicate
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.predicates.DataComponentPredicate
import net.minecraft.util.StringRepresentable

//TODO: Move away from something as hardcoded as this
data class HelixGenePredicate(
	val helixType: HelixType
) : SingleComponentItemPredicate<Holder<Gene>> {

	enum class HelixType : StringRepresentable {
		ANY, BLACK_DEATH;

		override fun getSerializedName(): String {
			return name
		}
	}

	override fun componentType(): DataComponentType<Holder<Gene>> {
		return ModDataComponents.GENE.get()
	}

	override fun matches(geneHolder: Holder<Gene>): Boolean {
		return when (helixType) {
			HelixType.ANY -> true
			HelixType.BLACK_DEATH -> geneHolder.isGene(ModGenes.BLACK_DEATH)
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

		val TYPE: DataComponentPredicate.Type<HelixGenePredicate> = DataComponentPredicate.ConcreteType(CODEC)
	}

}
