package dev.aaronhowser.mods.geneticsresequenced.block.base

import net.neoforged.neoforge.energy.EnergyStorage

abstract class ModEnergyStorage(
    private val capacity: Int,
    private val maxTransfer: Int
) : EnergyStorage(capacity, maxTransfer) {

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        val extractedEnergy = super.extractEnergy(maxExtract, simulate)
        if (extractedEnergy != 0) {
            onEnergyChanged()
        }

        return extractedEnergy
    }

    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        val receivedEnergy = super.receiveEnergy(maxReceive, simulate)
        if (receivedEnergy != 0) {
            onEnergyChanged()
        }

        return receivedEnergy
    }

    fun setEnergy(energy: Int): Int {
        this.energy = energy
        return energy
    }

    abstract fun onEnergyChanged()
}