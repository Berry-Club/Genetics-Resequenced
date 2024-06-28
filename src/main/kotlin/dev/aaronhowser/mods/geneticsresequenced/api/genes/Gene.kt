package dev.aaronhowser.mods.geneticsresequenced.api.genes

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.component
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance
import kotlin.properties.Delegates

class Gene(
    val id: ResourceLocation
) {

    var isNegative: Boolean by Delegates.notNull()
        private set
    var isHidden: Boolean by Delegates.notNull()
        private set
    var canMobsHave: Boolean by Delegates.notNull()
        private set
    var dnaPointsRequired: Int by Delegates.notNull()
        private set
    var mutatesInto: Gene? = null
        private set
    private var potionDetails: GeneBuilder.PotionDetails? = null

    fun setDetails(
        isNegative: Boolean,
        canMobsHave: Boolean,
        dnaPointsRequired: Int,
        mutatesInto: Gene?,
        potionDetails: GeneBuilder.PotionDetails?,
        isHidden: Boolean
    ) {
        this.isNegative = isNegative
        this.canMobsHave = canMobsHave
        this.dnaPointsRequired = dnaPointsRequired
        this.mutatesInto = mutatesInto
        this.potionDetails = potionDetails
        this.isHidden = isHidden
    }

    override fun toString(): String = "Gene($id)"

    object Registry {
        //TODO: Make this an actual registry
        private val GENE_REGISTRY: MutableSet<Gene> = mutableSetOf()

        fun getRegistryUnsorted(): List<Gene> {
            return GENE_REGISTRY.toList()
        }

        fun getRegistry(): List<Gene> {
            val sortedGenes = GENE_REGISTRY.sortedBy { it.id }

            val positiveGenes: MutableList<Gene> = mutableListOf()
            val mutations: MutableList<Gene> = mutableListOf()
            val negativeGenes: MutableList<Gene> = mutableListOf()

            for (gene in sortedGenes) {
                when {
                    !gene.isMutation && !gene.isNegative -> positiveGenes.add(gene)
                    gene.isMutation -> mutations.add(gene)
                    gene.isNegative -> negativeGenes.add(gene)
                }
            }

            return positiveGenes + mutations + negativeGenes
        }

        val noCustomGenes = getRegistry().all { it.id.namespace == GeneticsResequenced.ID }

        fun register(
            id: ResourceLocation,
            isNegative: Boolean,
            canMobsHave: Boolean,
            dnaPointsRequired: Int,
            mutatesInto: Gene?,
            potionDetails: GeneBuilder.PotionDetails?,
            hidden: Boolean,
        ): Gene {
            val gene = Gene(id = id)

            gene.setDetails(
                isNegative = isNegative,
                canMobsHave = canMobsHave,
                dnaPointsRequired = dnaPointsRequired,
                mutatesInto = mutatesInto,
                potionDetails = potionDetails,
                isHidden = hidden
            )

            GENE_REGISTRY.add(gene)

            return gene
        }

        fun fromId(searchedId: ResourceLocation): Gene? {
            return GENE_REGISTRY.find { it.id == searchedId }
        }

        fun fromId(searchedString: String): Gene? {
            return fromId(ResourceLocation.parse(searchedString))
        }

    }

    companion object {

        val unknownGeneComponent: MutableComponent = ModLanguageProvider.Genes.UNKNOWN.component

        fun checkDeactivationConfig() {
            val disabledGenes = ServerConfig.disabledGenes.get()

            for (disabledGene in disabledGenes) {
                val gene = Registry.fromId(disabledGene)
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
            ModGenes.basic
        )

        val CODEC: Codec<Gene> = ResourceLocation.CODEC.xmap(
            { id: ResourceLocation -> Registry.fromId(id) ?: throw IllegalArgumentException("Unknown gene $id") },
            { gene: Gene -> gene.id }
        )

        val STREAM_CODEC: StreamCodec<ByteBuf, Gene> = ResourceLocation.STREAM_CODEC.map(
            { id: ResourceLocation -> Registry.fromId(id) ?: throw IllegalArgumentException("Unknown gene $id") },
            { gene: Gene -> gene.id }
        )

    }

    private val requiredGenes: MutableSet<Gene> = mutableSetOf()

    val isMutation: Boolean
        get() = Registry.getRegistryUnsorted().any { it.mutatesInto == this }

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

            val component = (translationKey
                .component
                .withStyle {
                    it
                        .withColor(color)
                        .withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ModLanguageProvider.Tooltips.COPY_GENE.component(id.toString())
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
                component.append(
                    ModLanguageProvider.Genes.GENE_DISABLED.component
                )
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
        val potionDetails = potionDetails ?: return null

        return MobEffectInstance(
            potionDetails.effect,
            potionDetails.duration,
            potionDetails.level - 1,
            true,
            false,
            potionDetails.showIcon
        )
    }

//    fun canBeAdded(targetGenes: GenesCapability): Boolean {
//        if (targetGenes.hasGene(this)) return false
//        if (!isMutation) return true
//
//        return requiredGenes.all { targetGenes.hasGene(it) }
//    }

}