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
    val isNegative: Boolean,
    val canMobsHave: Boolean,
    val dnaPointsRequired: Int,
    val mutatesInto: Gene?,
    private val potionDetails: GeneBuilder.PotionDetails? = null
) {

    var hidden: Boolean = false

    override fun toString(): String = "Gene($id)"

    companion object {
        private val GENE_REGISTRY: MutableSet<Gene> = mutableSetOf()
        fun getRegistry(): List<Gene> {
            val sortedGenes = GENE_REGISTRY.sortedBy { it.id }

            val positiveGenes = sortedGenes.filter { !it.isNegative && !it.isMutation }
            val mutations = sortedGenes.filter { it.isMutation }
            val negativeGenes = sortedGenes.filter { it.isNegative }

            return positiveGenes + mutations + negativeGenes
        }

        fun fromId(searchedId: ResourceLocation): Gene? {
            return GENE_REGISTRY.find { it.id == searchedId }
        }

        fun fromId(searchedId: String): Gene? {
            return GENE_REGISTRY.find { it.id.toString() == searchedId }
        }

        val unknownGeneComponent: MutableComponent = Component.translatable("gene.geneticsresequenced.unknown")

        fun register(
            id: ResourceLocation,
            isNegative: Boolean,
            canMobsHave: Boolean,
            dnaPointsRequired: Int,
            mutatesInto: Gene?,
            potionDetails: GeneBuilder.PotionDetails?,
            hidden: Boolean,
        ): Gene {
            val gene = Gene(
                id = id,
                isNegative = isNegative,
                canMobsHave = canMobsHave,
                dnaPointsRequired = dnaPointsRequired,
                mutatesInto = mutatesInto,
                potionDetails = potionDetails
            )

            GENE_REGISTRY.add(gene)

            gene.hidden = hidden

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
            DefaultGenes.basic
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

}