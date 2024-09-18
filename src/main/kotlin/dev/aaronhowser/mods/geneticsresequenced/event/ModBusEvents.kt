package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.block.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import net.minecraft.core.Direction
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.config.ModConfigEvent
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.registries.DataPackRegistryEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    bus = EventBusSubscriber.Bus.MOD
)
object ModBusEvents {

    @SubscribeEvent
    fun onNewDataPackRegistry(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(
            GeneRegistry.GENE_REGISTRY_KEY,
            Gene.DIRECT_CODEC,
            Gene.DIRECT_CODEC
        )
    }

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        if (event is ModConfigEvent.Unloading) return

        val config = event.config

        // Comparing spec didn't work for some reason
        if (config.modId == GeneticsResequenced.ID && config.type == ModConfig.Type.SERVER) {
            Gene.checkDeactivationConfig()
        }
    }

    @SubscribeEvent
    fun onEntityAttributeCreation(event: EntityAttributeCreationEvent) {
        event.put(ModEntityTypes.SUPPORT_SLIME.get(), SupportSlime.setAttributes())
    }

    @SubscribeEvent
    fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {

        fun getItemCap(blockEntity: BlockEntity, direction: Direction): IItemHandler? {
            if (blockEntity is InventoryEnergyBlockEntity) {
                return blockEntity.getItemHandler(direction)
            }
            return null
        }

        fun getEnergyCap(blockEntity: BlockEntity, direction: Direction): EnergyStorage? {
            if (blockEntity is InventoryEnergyBlockEntity) {
                return blockEntity.getEnergyCapability(direction)
            }
            return null
        }

        for (blockEntityType: BlockEntityType<*> in ModBlockEntities.BLOCK_ENTITY_REGISTRY.entries.map { it.get() }) {

            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                blockEntityType
            ) { blockEntity, direction -> getItemCap(blockEntity, direction) }

            event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                blockEntityType
            ) { blockEntity, direction -> getEnergyCap(blockEntity, direction) }

        }

    }

    @SubscribeEvent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        ModPacketHandler.registerPayloads(event)
    }

    @SubscribeEvent
    fun onEntityAttributeModification(event: EntityAttributeModificationEvent) {
        if (!event.has(EntityType.PLAYER, ModAttributes.EFFICIENCY)) {
            event.add(EntityType.PLAYER, ModAttributes.EFFICIENCY)
        }
    }

}