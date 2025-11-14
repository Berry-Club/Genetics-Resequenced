package dev.aaronhowser.mods.geneticsresequenced.block_old.machine.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.block_old.base.CraftingMachineBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class PlasmidInjectorBlock : CraftingMachineBlock(
	PlasmidInjectorBlockEntity::class.java
) {

	override fun <T : BlockEntity> getTicker(
		pLevel: Level,
		pState: BlockState,
		pBlockEntityType: BlockEntityType<T>
	): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			pBlockEntityType,
			ModBlockEntities.PLASMID_INJECTOR.get(),
			PlasmidInjectorBlockEntity.Companion::tick
		)
	}

}