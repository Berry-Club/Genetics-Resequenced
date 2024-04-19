package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance

@Suppress("unused")
class Gene(
    val id: ResourceLocation
) {

    companion object {
        private val GENE_REGISTRY: MutableSet<Gene> = mutableSetOf()
        fun getRegistry(): List<Gene> = GENE_REGISTRY.sortedBy { it.id }

        fun fromId(searchedId: ResourceLocation): Gene? {
            return GENE_REGISTRY.find { it.id == searchedId }
        }

        fun fromId(searchedId: String): Gene? {
            return GENE_REGISTRY.find { it.id.toString() == searchedId }
        }
    }

    var isNegative: Boolean = false
        private set

    fun setNegative(): Gene {
        this.isNegative = true
        return this
    }

    private var mutatesInto: Gene? = null
    fun setMutatesInto(mutatesInto: Gene): Gene {
        this.mutatesInto = mutatesInto
        return this
    }

    val isMutation: Boolean
        get() = GENE_REGISTRY.any { it.mutatesInto == this }

    val translationKey: String = "gene.${id.namespace}.${id.path}"

    var amountNeeded: Int = -1
        private set

    fun setAmountNeeded(amountNeeded: Int): Gene {
        this.amountNeeded = amountNeeded
        return this
    }

    val nameComponent: Component
        get() = Component
            .translatable(translationKey)
            .withStyle {
                it
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Component.translatable("tooltip.geneticsresequenced.copy_gene_id", id)
                        )
                    )
                    .withClickEvent(
                        ClickEvent(
                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                            id.toString()
                        )
                    )
            }

    fun build(): Gene {

        if (amountNeeded == -1 || translationKey.isBlank()) {
            throw IllegalStateException("Gene $id is missing required fields")
        }

        GENE_REGISTRY.add(this)
        return this
    }

    var isActive: Boolean = true

    private var potionEffect: MobEffect? = null
    private var potionLevel: Int = 1
    private var potionDuration: Int = 300

    fun getPotion(): MobEffectInstance? {
        if (potionEffect == null) return null

        return MobEffectInstance(
            potionEffect!!,
            potionDuration,
            potionLevel - 1,
            true,
            false,
            ServerConfig.showEffectIcons.get()
        )
    }

    fun setPotion(
        effect: MobEffect,
        level: Int,
        duration: Int = 300
    ): Gene {
        this.potionEffect = effect
        this.potionLevel = level
        this.potionDuration = duration
        return this
    }

//    fun canAddMutation(genes: GenesCapability, syringeGenes: GenesCapability): Boolean {
//        return when (this) {
//            HASTE_2 -> genes.hasGene(HASTE) || syringeGenes.hasGene(HASTE)
//            EFFICIENCY_4 -> genes.hasGene(EFFICIENCY) || syringeGenes.hasGene(EFFICIENCY)
//            REGENERATION_4 -> genes.hasGene(REGENERATION) || syringeGenes.hasGene(REGENERATION)
//            SPEED_4 -> genes.hasGene(SPEED_2) || syringeGenes.hasGene(SPEED_2)
//            SPEED_2 -> genes.hasGene(SPEED) || syringeGenes.hasGene(SPEED)
//            RESISTANCE_2 -> genes.hasGene(RESISTANCE) || syringeGenes.hasGene(RESISTANCE)
//            STRENGTH_2 -> genes.hasGene(STRENGTH) || syringeGenes.hasGene(STRENGTH)
//            MEATY_2 -> genes.hasGene(MEATY) || syringeGenes.hasGene(MEATY)
//            MORE_HEARTS_2 -> genes.hasGene(MORE_HEARTS) || syringeGenes.hasGene(MORE_HEARTS)
//            INVISIBLE -> true
//            FLIGHT -> genes.hasGene(JUMP_BOOST) || genes.hasGene(TELEPORT) || genes.hasGene(NO_FALL_DAMAGE) ||
//                    syringeGenes.hasGene(JUMP_BOOST) || syringeGenes.hasGene(TELEPORT) || syringeGenes.hasGene(
//                NO_FALL_DAMAGE
//            )
//
//            LUCK -> true
//            SCARE_ZOMBIES -> genes.hasGene(SCARE_CREEPERS) || syringeGenes.hasGene(SCARE_CREEPERS)
//            SCARE_SPIDERS -> genes.hasGene(SCARE_SKELETONS) || syringeGenes.hasGene(SCARE_SKELETONS)
//            THORNS -> genes.hasGene(PHOTOSYNTHESIS) || syringeGenes.hasGene(PHOTOSYNTHESIS)
//            CLAWS_2 -> genes.hasGene(CLAWS) || syringeGenes.hasGene(CLAWS)
//            else -> true
//        }
//    }
}