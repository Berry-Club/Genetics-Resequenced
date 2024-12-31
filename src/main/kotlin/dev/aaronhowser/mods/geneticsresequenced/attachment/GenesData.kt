package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.event.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS


data class GenesData(
    val genes: HolderSet<Gene>
) {
    constructor() : this(HolderSet.empty())
    constructor(set: Set<Holder<Gene>>) : this(HolderSet.direct(set.toList()))

    companion object {

        val CODEC: Codec<GenesData> = Gene.CODEC.listOf().xmap(
            { GenesData(it.toSet()) },
            { it.genes.toList() }
        )

        fun syncPlayer(player: Player) {
            if (player !is ServerPlayer) return

            for (gene in player.geneHolders) {
                ModPacketHandler.messagePlayer(
                    player,
                    GeneChangedPacket(
                        player.id,
                        gene.key!!.location(),
                        true
                    )
                )
            }
        }

        var LivingEntity.geneHolders: Set<Holder<Gene>>
            get() = this.getData(ModAttachmentTypes.GENE_CONTAINER).genes.toSet()
            private set(value) {
                this.setData(ModAttachmentTypes.GENE_CONTAINER, GenesData(value))
            }

        fun LivingEntity.addGene(newGeneHolder: Holder<Gene>): Boolean {
            if (this.hasGene(newGeneHolder)) return false
            if (newGeneHolder.isHelixOnly) {
                GeneticsResequenced.LOGGER.debug("Cannot add gene $newGeneHolder to entities, as it has tag `#geneticsresequenced:helix_only`.")
                return false
            }

            if (
                this is Player
                && newGeneHolder.isNegative
                && ServerConfig.disableGivingPlayersNegativeGenes.get()
                && !newGeneHolder.`is`(ModGenes.CRINGE)
            ) {
                GeneticsResequenced.LOGGER.debug(
                    "Tried to give negative gene $newGeneHolder to player $this, but \"disableGivingPlayersNegativeGenes\" is true in the server config."
                )
                return false
            }

            if (this.type !in newGeneHolder.value().allowedEntities.map { it.value() }) {
                GeneticsResequenced.LOGGER.debug("Tried to give gene $newGeneHolder to mob $this, but mobs cannot have that gene!")
                return false
            }

            val eventPre = CustomEvents.GeneChangeEvent.Pre(this, newGeneHolder, true)
            val wasCanceled = FORGE_BUS.post(eventPre).isCanceled
            if (wasCanceled) {
                GeneticsResequenced.LOGGER.debug("Event was canceled: $eventPre")
                return false
            }

            this.geneHolders += newGeneHolder

            val eventPost = CustomEvents.GeneChangeEvent.Post(this, newGeneHolder, true)
            FORGE_BUS.post(eventPost)

            return true
        }

        fun LivingEntity.removeGene(removedGeneHolder: Holder<Gene>): Boolean {
            if (!this.hasGene(removedGeneHolder)) return false

            val eventPre = CustomEvents.GeneChangeEvent.Pre(this, removedGeneHolder, false)
            val wasCanceled = FORGE_BUS.post(eventPre).isCanceled
            if (wasCanceled) {
                GeneticsResequenced.LOGGER.debug("Event was canceled: $eventPre")
                return false
            }

            this.geneHolders -= removedGeneHolder

            val eventPost = CustomEvents.GeneChangeEvent.Post(this, removedGeneHolder, false)
            FORGE_BUS.post(eventPost)

            return true
        }

        fun LivingEntity.hasGene(gene: Holder<Gene>): Boolean {
            return gene in this.geneHolders
        }

        fun LivingEntity.hasGene(geneKey: ResourceKey<Gene>): Boolean {
            return this.geneHolders.any { it.isGene(geneKey) }
        }

        fun LivingEntity.removeAllGenes() {
            for (gene in this.geneHolders) {
                this.removeGene(gene)
            }
        }

        fun LivingEntity.addAlLGenes(registries: HolderLookup.Provider, includeNegative: Boolean = false) {
            val genesToAdd =
                ModGenes.getAllGeneHolders(registries).filter { includeNegative || !it.isNegative }

            for (gene in genesToAdd) {
                this.addGene(gene)
            }
        }

    }
}