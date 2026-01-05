package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional

class GenesCapabilityProvider(
	private val registries: HolderLookup.Provider
) : ICapabilitySerializable<CompoundTag> {

	private var genes: GenesCapability? = null
	private val optional: LazyOptional<GenesCapability> = LazyOptional.of(::lazilyGetGenes)

	private fun lazilyGetGenes(): GenesCapability {
		if (genes == null) {
			genes = GenesCapability()
		}
		return genes!!
	}

	override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
		return when (cap) {
			CAPABILITY -> optional.cast()
			else -> LazyOptional.empty()
		}
	}

	override fun serializeNBT(): CompoundTag = lazilyGetGenes().toTag(registries)
	override fun deserializeNBT(nbt: CompoundTag) = lazilyGetGenes().fromTag(registries, nbt)

	companion object {
		val CAPABILITY_RL: ResourceLocation = OtherUtil.modResource("genes")
		val CAPABILITY: Capability<GenesCapability> = CapabilityManager.get(object : CapabilityToken<GenesCapability>() {})
	}

}