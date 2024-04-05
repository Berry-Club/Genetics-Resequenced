package dev.aaronhowser.mods.geneticsresequenced.blockentity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState


class BioluminescenceBlockEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(ModBlockEntities.BIOLUMINESCENCE.get(), pos, state) {

    private var timeRemaining = 20 * 10

    fun tick() {
        level?.apply {
            if (isClientSide) return

            println("Time remaining: $timeRemaining")

            if (timeRemaining-- <= 0) {
                removeBlockEntity(this@BioluminescenceBlockEntity.blockPos)
                removeBlock(this@BioluminescenceBlockEntity.blockPos, false)
            }
        }
    }

    companion object {
        const val NBT_NAME = "timeRemaining"
    }

    override fun saveAdditional(pTag: CompoundTag) {
        super.saveAdditional(pTag)
        pTag.putInt(NBT_NAME, this.timeRemaining)
    }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)
        this.timeRemaining = pTag.getInt(NBT_NAME)
    }

}
