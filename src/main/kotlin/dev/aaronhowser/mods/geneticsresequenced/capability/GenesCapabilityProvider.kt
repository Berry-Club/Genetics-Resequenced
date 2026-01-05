package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional

class GenesCapabilityProvider : ICapabilitySerializable<CompoundTag> {

	private var genes: GenesData? = null
	private val optional: LazyOptional<GenesData> = LazyOptional.of(::lazilyGetGenes)

	private fun lazilyGetGenes(): GenesData {
		if (genes == null) {
			genes = GenesData()
		}
		return genes!!
	}

	override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
		return when (cap) {
			CAPABILITY -> optional.cast()
			else -> LazyOptional.empty()
		}
	}

	override fun serializeNBT(): CompoundTag? {
		TODO("Not yet implemented")
	}

	override fun deserializeNBT(nbt: CompoundTag?) {
		TODO("Not yet implemented")
	}

	companion object {
		val CAPABILITY: Capability<GenesData> = CapabilityManager.get(object : CapabilityToken<GenesData>() {})
	}

}