package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken

object ModCapabilities {

	val GENES: Capability<GenesData> = CapabilityManager.get(object : CapabilityToken<GenesData>() {})
	val TEMP_GENES: Capability<GenesData> = CapabilityManager.get(object : CapabilityToken<GenesData>() {})
	val KEPT_INVENTORY: Capability<KeptInventory> = CapabilityManager.get(object : CapabilityToken<KeptInventory>() {})

}