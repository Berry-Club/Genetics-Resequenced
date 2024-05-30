package dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.attributes.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class GeneChangedPacket(
    private val geneId: ResourceLocation,
    private val wasAdded: Boolean
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeResourceLocation(geneId)
        buffer.writeBoolean(wasAdded)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): GeneChangedPacket {
            return GeneChangedPacket(
                buffer.readResourceLocation(),
                buffer.readBoolean()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        if (context.get().direction != NetworkDirection.PLAY_TO_CLIENT) {
            throw IllegalStateException("Received SetWallClimbingPacket on wrong side!")
        }

        val player: LocalPlayer = Minecraft.getInstance().player
            ?: throw IllegalStateException("Received SetWallClimbingPacket without player!")

        val gene = Gene.fromId(geneId)
            ?: throw IllegalStateException("Received SetWallClimbingPacket with invalid gene id!")

        player.getGenes()?.apply {
            if (wasAdded) {
                addGene(gene)
            } else {
                removeGene(gene)
            }
        }

        handleAttributes(player, gene)

        context.get().packetHandled = true
    }

    private fun handleAttributes(player: LocalPlayer, gene: Gene) {
        val attributeInstance = when (gene) {
            DefaultGenes.EFFICIENCY, DefaultGenes.EFFICIENCY_4 -> player.attributes.getInstance(ModAttributes.EFFICIENCY)
            DefaultGenes.WALL_CLIMBING -> player.attributes.getInstance(ModAttributes.WALL_CLIMBING)
            else -> null
        } ?: return

        val newLevel = when (gene) {
            DefaultGenes.EFFICIENCY -> if (wasAdded) 1.0 else 0.0

            DefaultGenes.EFFICIENCY_4 -> {
                if (wasAdded) {
                    4.0
                } else {
                    if (player.getGenes()?.hasGene(DefaultGenes.EFFICIENCY) == true) {
                        1.0
                    } else {
                        0.0
                    }
                }
            }

            DefaultGenes.WALL_CLIMBING -> if (wasAdded) 1.0 else 0.0

            else -> throw IllegalStateException("Gene $gene went through the GeneChangedPacket but isn't handled!")
        }

        attributeInstance.baseValue = newLevel

    }
}