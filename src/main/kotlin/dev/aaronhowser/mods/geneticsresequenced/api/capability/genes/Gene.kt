package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance

@Suppress("unused")
class Gene(
    val id: ResourceLocation,
    val isNegative: Boolean = false,
    val dnaPointsRequired: Int,
    val mutatesInto: Gene?,
    private val potionDetails: GeneBuilder.PotionDetails? = null
) {

    override fun toString(): String = "Gene($id)"

    companion object {
        private val GENE_REGISTRY: MutableSet<Gene> = mutableSetOf()
        fun getRegistry(): List<Gene> = GENE_REGISTRY.sortedBy { it.id }

        fun fromId(searchedId: ResourceLocation): Gene? {
            return GENE_REGISTRY.find { it.id == searchedId }
        }

        fun fromId(searchedId: String): Gene? {
            return GENE_REGISTRY.find { it.id.toString() == searchedId }
        }

        val basicGeneComponent: MutableComponent = Component.translatable("gene.geneticsresequenced.basic")
        val unknownGeneComponent: MutableComponent = Component.translatable("gene.geneticsresequenced.unknown")

        fun register(
            id: ResourceLocation,
            isNegative: Boolean,
            dnaPointsRequired: Int,
            mutatesInto: Gene?,
            potionDetails: GeneBuilder.PotionDetails?
        ): Gene {
            val gene = Gene(
                id = id,
                isNegative = isNegative,
                dnaPointsRequired = dnaPointsRequired,
                mutatesInto = mutatesInto,
                potionDetails = potionDetails
            )

            GENE_REGISTRY.add(gene)
            return gene
        }

        fun checkDeactivationConfig() {
            val disabledGenes = ServerConfig.disabledGenes.get()

            for (disabledGene in disabledGenes) {
                val gene = fromId(disabledGene)
                if (gene == null) {
                    GeneticsResequenced.LOGGER.warn("Tried to disable gene $disabledGene, but it does not exist!")
                    continue
                }

                if (gene in requiredGenes) {
                    GeneticsResequenced.LOGGER.warn("Tried to disable gene $disabledGene, but it is required for the mod to function!")
                    continue
                }

                gene.deactivate()

            }
        }

        private val requiredGenes = setOf(
            DefaultGenes.BASIC
        )
    }

    private val requiredGenes: MutableSet<Gene> = mutableSetOf()

    val mutatesFrom: Set<Gene>
        get() = GENE_REGISTRY.filter { it.mutatesInto == this }.toSet()

    val isMutation: Boolean
        get() = mutatesFrom.isNotEmpty()

    fun addRequiredGenes(genes: Collection<Gene>) {
        requiredGenes.addAll(genes)
    }

    fun removeRequiredGenes(genes: Collection<Gene>) {
        requiredGenes.removeAll(genes.toSet())
    }

    fun getRequiredGenes(): Set<Gene> {
        return requiredGenes.toSet()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    val translationKey: String = "gene.${id.namespace}.${id.path}"

    val nameComponent: Component
        get() {
            val color = if (isActive) {
                if (isNegative) {
                    ChatFormatting.RED
                } else if (isMutation) {
                    ChatFormatting.DARK_PURPLE
                } else {
                    ChatFormatting.GRAY
                }
            } else {
                ChatFormatting.DARK_RED
            }

            val component = Component
                .translatable(translationKey)
                .withStyle {
                    it
                        .withColor(color)
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

            if (!isActive) {
                component.append(Component.translatable("gene.geneticsresequenced.gene_disabled"))
            }

            return component
        }

    //TODO: Implement this in the genes etc
    var isActive: Boolean = true
        private set

    private fun deactivate() {
        isActive = false
        GeneticsResequenced.LOGGER.info("Deactivated gene $id")
    }

    fun getPotion(): MobEffectInstance? {
        if (potionDetails == null) return null

        val effect = potionDetails.effect
        val duration = potionDetails.duration
        val amplifier = potionDetails.level - 1

        return MobEffectInstance(
            effect,
            duration,
            amplifier,
            true,
            false,
            ServerConfig.showEffectIcons.get()
        )
    }

    fun canBeAdded(targetGenes: GenesCapability): Boolean {
        if (targetGenes.hasGene(this)) return false
        if (!isMutation) return true

        return requiredGenes.all { targetGenes.hasGene(it) }
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