package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlock
import dev.aaronhowser.mods.geneticsresequenced.block.block_entity.PlasmidInfuserBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class PlasmidInfuserBlock : MachineBlock() {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return PlasmidInfuserBlockEntity(pos, state)
	}

}