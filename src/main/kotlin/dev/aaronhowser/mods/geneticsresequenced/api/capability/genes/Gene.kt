package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance
import kotlin.properties.Delegates

class Gene(
    val id: ResourceLocation
) {

    override fun equals(other: Any?) = other is Gene && other.id == id

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

    companion object {
        //TODO: Make this an actual registry
        private val GENE_REGISTRY: MutableSet<Gene> = mutableSetOf()

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

//        fun checkDeactivationConfig() {
//            val disabledGenes = ServerConfig.disabledGenes.get()
//
//            for (disabledGene in disabledGenes) {
//                val gene = fromId(disabledGene)
//                if (gene == null) {
//                    GeneticsResequenced.LOGGER.warn("Tried to disable gene $disabledGene, but it does not exist!")
//                    continue
//                }
//
//                if (gene in requiredGenes) {
//                    GeneticsResequenced.LOGGER.warn("Tried to disable gene $disabledGene, but it is required for the mod to function!")
//                    continue
//                }
//
//                gene.deactivate()
//
//            }
//        }

//        private val requiredGenes = setOf(
//            ModGenes.basic
//        )

        fun fromId(searchedId: ResourceLocation): Gene? {
            return GENE_REGISTRY.find { it.id == searchedId }
        }

        val CODEC: Codec<Gene> = ResourceLocation.CODEC.xmap(
            { id: ResourceLocation -> fromId(id) ?: throw IllegalArgumentException("Unknown gene $id") },
            { gene: Gene -> gene.id }
        )

        val CODEC_NEW = object : Codec<Gene> {
            override fun <T : Any?> encode(input: Gene?, ops: DynamicOps<T>?, prefix: T): DataResult<T> {
                TODO("Not yet implemented")
            }

            override fun <T : Any?> decode(ops: DynamicOps<T>?, input: T): DataResult<Pair<Gene, T>> {
                TODO("Not yet implemented")
            }

        }
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
                                Component.translatable("tooltip.geneticsresequenced.copy_gene_id", id.toString())
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

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (mutatesInto?.hashCode() ?: 0)
        result = 31 * result + (potionDetails?.hashCode() ?: 0)
        result = 31 * result + requiredGenes.hashCode()
        result = 31 * result + translationKey.hashCode()
        result = 31 * result + isActive.hashCode()
        return result
    }

//    fun canBeAdded(targetGenes: GenesCapability): Boolean {
//        if (targetGenes.hasGene(this)) return false
//        if (!isMutation) return true
//
//        return requiredGenes.all { targetGenes.hasGene(it) }
//    }

}