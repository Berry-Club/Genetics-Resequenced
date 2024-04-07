package dev.aaronhowser.mods.geneticsresequenced.packet

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.attribute.ModAttributes
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class GeneChangedPacket(
    private val geneId: String,
    private val wasAdded: Boolean
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeUtf(geneId)
        buffer.writeBoolean(wasAdded)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): GeneChangedPacket {
            return GeneChangedPacket(
                buffer.readUtf(),
                buffer.readBoolean()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        if (context.get().direction != NetworkDirection.PLAY_TO_CLIENT) {
            throw IllegalStateException("Received SetWallClimbingPacket on wrong side!")
        }

        val player = Minecraft.getInstance().player
            ?: throw IllegalStateException("Received SetWallClimbingPacket without player!")

        val gene = Gene.valueOf(geneId)

        player.getGenes()?.apply {
            if (wasAdded) {
                addGene(gene)
            } else {
                removeGene(gene)
            }
        }

        handleAttributes(player, gene)
    }

    private fun handleAttributes(player: LocalPlayer, gene: Gene) {
        val attributeInstance = when (gene) {
            Gene.EFFICIENCY, Gene.EFFICIENCY_4 -> player.attributes.getInstance(ModAttributes.EFFICIENCY)
            Gene.WALL_CLIMBING -> player.attributes.getInstance(ModAttributes.WALL_CLIMBING)
            else -> null
        } ?: return

        val newLevel = when (gene) {
            Gene.EFFICIENCY -> if (wasAdded) 1.0 else 0.0

            Gene.EFFICIENCY_4 -> {
                if (wasAdded) {
                    4.0
                } else {
                    if (player.getGenes()?.hasGene(Gene.EFFICIENCY) == true) {
                        1.0
                    } else {
                        0.0
                    }
                }
            }

            Gene.WALL_CLIMBING -> if (wasAdded) 1.0 else 0.0

            else -> throw IllegalStateException("Gene $gene went through the GeneChangedPacket but isn't handled!")
        }

        attributeInstance.baseValue = newLevel

    }
}