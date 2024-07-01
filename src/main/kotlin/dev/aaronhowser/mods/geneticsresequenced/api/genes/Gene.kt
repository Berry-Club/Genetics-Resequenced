package dev.aaronhowser.mods.geneticsresequenced.api.genes

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.ChatFormatting
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance

class Gene(properties: GeneProperties) {


    val id: ResourceLocation = properties.id
    val isNegative: Boolean = properties.isNegative
    val isHidden: Boolean = properties.isHidden
    val canMobsHave: Boolean = properties.canMobsHave
    val dnaPointsRequired: Int = properties.dnaPointsRequired
    private val mutatesInto: Gene? = properties.mutatesInto
    private val potionDetails: GeneProperties.PotionDetails? = properties.potionDetails

    override fun toString(): String = "Gene($id)"

    companion object {

        val unknownGeneComponent: MutableComponent = ModLanguageProvider.Genes.UNKNOWN.toComponent()

        fun checkDeactivationConfig() {
            val disabledGenes = ServerConfig.disabledGenes.get()

            for (disabledGene in disabledGenes) {
                val gene = ModGenes.fromId(disabledGene)

                if (gene in requiredGenes) {
                    GeneticsResequenced.LOGGER.warn("Tried to disable gene $disabledGene, but it is required for the mod to function!")
                    continue
                }

                gene.deactivate()

            }
        }

        private val requiredGenes by lazy {
            setOf(
                ModGenes.BASIC.get()
            )
        }

        val CODEC: Codec<Gene> = GeneRegistry.GENE_REGISTRY.byNameCodec()

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Gene> =
            ByteBufCodecs.registry(GeneRegistry.GENE_REGISTRY_KEY)

    }

    private val requiredGenes: MutableSet<Gene> = mutableSetOf()

    val isMutation: Boolean
        get() = GeneRegistry.GENE_REGISTRY.any { it.mutatesInto == this }

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

            val component = translationKey
                .toComponent()
                .withStyle {
                    it
                        .withColor(color)
                        .withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ModLanguageProvider.Tooltips.COPY_GENE.toComponent(id.toString())
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
                    ModLanguageProvider.Genes.GENE_DISABLED.toComponent()
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

}