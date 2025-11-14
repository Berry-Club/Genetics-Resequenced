package dev.aaronhowser.mods.geneticsresequenced.block.base

import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.MapColor

abstract class MachineBlock(
	properties: Properties = DEFAULT_PROPERTIES
) : Block(properties), EntityBlock {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(H_FACING, Direction.NORTH)
		)
	}

	override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T> {
		return BlockEntityTicker { l, p, s, be ->
			if (be is MachineBlockEntity) {
				MachineBlockEntity.tick(l, p, s, be)
			}
		}
	}

	companion object {
		val H_FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

		val DEFAULT_PROPERTIES: Properties = Properties.of()
			.mapColor(MapColor.METAL)
			.requiresCorrectToolForDrops()
			.strength(5f, 6f)
			.sound(SoundType.METAL)
	}

}