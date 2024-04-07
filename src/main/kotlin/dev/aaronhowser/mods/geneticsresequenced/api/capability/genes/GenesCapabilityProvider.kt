package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.common.util.LazyOptional

class GenesCapabilityProvider : ICapabilityProvider, INBTSerializable<CompoundTag> {


    companion object {
        val GENE_CAPABILITY: Capability<GenesCapability> = CapabilityManager.get(object : CapabilityToken<GenesCapability>() {})
    }

    private var genes: GenesCapability? = null
    private final val optional: LazyOptional<GenesCapability> = LazyOptional.of(this::createGenes)

    private fun createGenes(): GenesCapability {
        if (this.genes == null) {
            this.genes = GenesCapability()
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