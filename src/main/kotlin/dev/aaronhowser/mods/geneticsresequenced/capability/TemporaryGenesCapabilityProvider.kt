package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.capabilities.*
import net.minecraftforge.common.util.LazyOptional

@AutoRegisterCapability
class TemporaryGenesCapabilityProvider(
	private val registries: HolderLookup.Provider
) : ICapabilitySerializable<CompoundTag> {

	private var tempGenes: TemporaryGenesCapability? = null
	private val optional: LazyOptional<TemporaryGenesCapability> = LazyOptional.of(::lazilyGetGenes)

	private fun lazilyGetGenes(): TemporaryGenesCapability {
		if (tempGenes == null) {
			tempGenes = TemporaryGenesCapability()
		}
		return tempGenes!!
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
		val CAPABILITY_RL: ResourceLocation = OtherUtil.modResource("temporary_genes")
		val CAPABILITY: Capability<TemporaryGenesCapability> = CapabilityManager.get(object : CapabilityToken<TemporaryGenesCapability>() {})
	}

}