package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.event.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS


data class GenesData(
    val genes: Set<Gene>
) {
    constructor() : this(HashSet())

    @Suppress("LoggingSimilarMessage")
    companion object {

        val CODEC: Codec<GenesData> = Gene.CODEC.listOf().xmap(
            { list: List<Gene> ->
                GenesData(
                    HashSet<Gene>(list)
                )
            },
            { genes: GenesData ->
                ArrayList<Gene>(
                    genes.genes
                )
            })

        var LivingEntity.genes: Set<Gene>
            get() = this.getData(ModAttachmentTypes.GENE_CONTAINER).genes
            private set(value) {
                this.setData(ModAttachmentTypes.GENE_CONTAINER, GenesData(value))
            }

        fun LivingEntity.addGene(newGene: Gene): Boolean {
            if (this.hasGene(newGene)) return false

            if (newGene.isHidden) {
                GeneticsResequenced.LOGGER.debug("Cannot add hidden gene $newGene to entity.")
                return false
            }

            if (this is Player && newGene.isNegative && ServerConfig.disableGivingPlayersNegativeGenes.get() && newGene != ModGenes.CRINGE.get()) {
                GeneticsResequenced.LOGGER.debug(
                    "Tried to give negative gene $newGene to player $this, but \"disableGivingPlayersNegativeGenes\" is true in the server config."
                )
                return false
            }

            if (this !is Player && !newGene.canMobsHave) {
                GeneticsResequenced.LOGGER.debug("Tried to give gene $newGene to mob $this, but mobs cannot have that gene!")
                return false
            }

            val eventPre = CustomEvents.GeneChangeEvent.Pre(this, newGene, true)
            val wasCanceled = FORGE_BUS.post(eventPre).isCanceled
            if (wasCanceled) {
                GeneticsResequenced.LOGGER.debug("Event was canceled: $eventPre")
                return false
            }

            this.genes += newGene

            val eventPost = CustomEvents.GeneChangeEvent.Post(this, newGene, true)
            FORGE_BUS.post(eventPost)

            return true
        }

        fun LivingEntity.removeGene(removedGene: Gene): Boolean {
            if (!this.hasGene(removedGene)) return false

            val eventPre = CustomEvents.GeneChangeEvent.Pre(this, removedGene, false)
            val wasCanceled = FORGE_BUS.post(eventPre).isCanceled
            if (wasCanceled) {
                GeneticsResequenced.LOGGER.debug("Event was canceled: $eventPre")
                return false
            }

            this.genes -= removedGene

            val eventPost = CustomEvents.GeneChangeEvent.Post(this, removedGene, false)
            FORGE_BUS.post(eventPost)

            return true
        }

        fun LivingEntity.hasGene(gene: Gene): Boolean {
            return gene in this.genes
        }

        fun LivingEntity.removeAllGenes() {
            for (gene in this.genes) {
                this.removeGene(gene)
            }
        }

        fun LivingEntity.addAlLGenes(includeNegative: Boolean = false) {
            val genesToAdd =
                GeneRegistry.GENE_REGISTRY.filter { includeNegative || !it.isNegative }

            for (gene in genesToAdd) {
                this.addGene(gene)
            }
        }

    }
}