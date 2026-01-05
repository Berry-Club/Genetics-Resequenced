package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken

object ModCapabilities {

	val GENES: Capability<GenesData> = CapabilityManager.get(object : CapabilityToken<GenesData>() {})
	val TEMP_GENES: Capability<GenesData> = CapabilityManager.get(object : CapabilityToken<GenesData>() {})
	val KEPT_INVENTORY: Capability<KeptInventory> = CapabilityManager.get(object : CapabilityToken<KeptInventory>() {})

	val GENES_NAME = OtherUtil.modResource("genes")
	val TEMP_GENES_NAME = OtherUtil.modResource("temporary_genes")
	val KEPT_INVENTORY_NAME = OtherUtil.modResource("kept_inventory")

}