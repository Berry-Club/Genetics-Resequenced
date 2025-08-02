package dev.aaronhowser.mods.geneticsresequenced.block.base

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.MapColor

abstract class MachineBlock(
	properties: Properties = DEFAULT_PROPERTIES
) : Block(properties), EntityBlock {

	companion object {
		val H_FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

		val DEFAULT_PROPERTIES: Properties = Properties.of()
			.mapColor(MapColor.METAL)
			.requiresCorrectToolForDrops()
			.strength(5f, 6f)
			.sound(SoundType.METAL)
	}

}