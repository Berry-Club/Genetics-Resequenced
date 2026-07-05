package dev.aaronhowser.mods.genetics_resequenced.block_entity.base

import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.transaction.Transaction

class MachineEnergyStorage(
	capacity: Int,
	maxTransfer: Int,
	private val onChanged: () -> Unit
) : SimpleEnergyHandler(capacity, maxTransfer) {

	val energyStored: Int
		get() = amountAsInt

	val maxEnergyStored: Int
		get() = capacityAsInt

	fun receiveEnergy(amount: Int, simulate: Boolean): Int {
		Transaction.openRoot().use { transaction ->
			val inserted = insert(amount, transaction)
			if (!simulate) transaction.commit()
			return inserted
		}
	}

	fun extractEnergy(amount: Int, simulate: Boolean): Int {
		Transaction.openRoot().use { transaction ->
			val extracted = extract(amount, transaction)
			if (!simulate) transaction.commit()
			return extracted
		}
	}

	override fun onEnergyChanged(previousAmount: Int) {
		onChanged()
	}
}
