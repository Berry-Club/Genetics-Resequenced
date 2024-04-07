package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import net.minecraft.world.entity.LivingEntity

fun LivingEntity.getGenes(): GenesCapability? {

    if (!this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).isPresent) return null

    return this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).orElseThrow {
        IllegalStateException("Genes capability not present")
    }

}