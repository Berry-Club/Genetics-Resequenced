package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.common.util.LazyOptional

object GenesCapabilityProvider : ICapabilityProvider, INBTSerializable<CompoundTag> {

    val GENE_CAPABILITY: Capability<Genes> = CapabilityManager.get(object : CapabilityToken<Genes>() {})

    private var genes: Genes? = null
    private final val optional: LazyOptional<Genes> = LazyOptional.of(this::createGenes)

    private fun createGenes(): Genes {
        if (this.genes == null) {
            this.genes = Genes()
        }
        return this.genes!!
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return if (cap == GENE_CAPABILITY) {
            optional.cast()
        } else {
            LazyOptional.empty()
        }
    }

    override fun serializeNBT(): CompoundTag {
        val compoundTag = CompoundTag()
        createGenes().saveNbt(compoundTag)
        return compoundTag
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        createGenes().loadNbt(nbt)
    }
}