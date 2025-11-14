package dev.aaronhowser.mods.geneticsresequenced.block.base

import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.energy.EnergyStorage

class BetterEnergyStorage(
	val blockEntity: BlockEntity,
	capacity: Int,
	transferRate: Int
) : EnergyStorage(capacity, transferRate) {

	override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
		val extractedEnergy = super.extractEnergy(maxExtract, simulate)
		if (extractedEnergy != 0) {
			blockEntity.setChanged()
		}

		return extractedEnergy
	}

	override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
		val receivedEnergy = super.receiveEnergy(toReceive, simulate)
		if (receivedEnergy != 0) {
			blockEntity.setChanged()
		}

		return receivedEnergy
	}

	fun setEnergy(energy: Int) {
		this.energy = energy
		blockEntity.setChanged()
	}

}