package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional

class KeptInventoryCapabilityProvider : ICapabilitySerializable<CompoundTag> {

	private var keptInventory: KeptInventoryCapability? = null
	private val optional: LazyOptional<KeptInventoryCapability> = LazyOptional.of(::lazilyGetGenes)

	private fun lazilyGetGenes(): KeptInventoryCapability {
		if (keptInventory == null) {
			keptInventory = KeptInventoryCapability()
		}
		return keptInventory!!
	}

	override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
		return when (cap) {
			CAPABILITY -> optional.cast()
			else -> LazyOptional.empty()
		}
	}

	override fun serializeNBT(): CompoundTag = lazilyGetGenes().toTag()
	override fun deserializeNBT(nbt: CompoundTag) = lazilyGetGenes().fromTag(nbt)

	companion object {
		val CAPABILITY_RL: ResourceLocation = OtherUtil.modResource("kept_inventory")
		val CAPABILITY: Capability<KeptInventoryCapability> = CapabilityManager.get(object : CapabilityToken<KeptInventoryCapability>() {})
	}

}