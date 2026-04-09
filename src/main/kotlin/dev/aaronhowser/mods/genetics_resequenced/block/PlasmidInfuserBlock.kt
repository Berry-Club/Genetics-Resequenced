package dev.aaronhowser.mods.genetics_resequenced.block

import dev.aaronhowser.mods.genetics_resequenced.block.base.MachineBlock
import dev.aaronhowser.mods.genetics_resequenced.block.block_entity.PlasmidInfuserBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class PlasmidInfuserBlock : MachineBlock() {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return PlasmidInfuserBlockEntity(pos, state)
	}

}