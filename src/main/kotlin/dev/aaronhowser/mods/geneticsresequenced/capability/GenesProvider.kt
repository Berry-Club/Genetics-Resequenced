package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional

class GenesProvider(
	private var registries: HolderLookup.Provider
) : ICapabilitySerializable<CompoundTag> {

	private val instance = GenesData()

	override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
		return if (cap == ModCapabilities.GENES) {
			LazyOptional.of { instance }.cast()
		} else {
			LazyOptional.empty()
		}
	}

	override fun serializeNBT(): CompoundTag = instance.toTag(registries)
	override fun deserializeNBT(nbt: CompoundTag) = instance.fromTag(registries, nbt)
}