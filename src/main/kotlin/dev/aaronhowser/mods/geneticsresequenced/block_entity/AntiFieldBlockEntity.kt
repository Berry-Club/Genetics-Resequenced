package dev.aaronhowser.mods.geneticsresequenced.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toBlockPos
import dev.aaronhowser.mods.geneticsresequenced.AntiFieldCarrier
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class AntiFieldBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.ANTI_FIELD_BLOCK.get(), pos, blockState) {

	private fun addToList() {
		val level = level as? AntiFieldCarrier ?: return
		level.getAntiFieldBlocks().add(worldPosition.asLong())
	}

	private fun removeFromList() {
		val level = level as? AntiFieldCarrier ?: return
		level.getAntiFieldBlocks().remove(worldPosition.asLong())
	}

	override fun setRemoved() {
		super.setRemoved()
		removeFromList()
	}

	companion object {
		fun AntiFieldCarrier.getAntiFieldBlocks(): LongOpenHashSet = this.`geneticsresequenced$getAntiFieldPositions`()

		fun isNearActiveAntiField(entity: Entity): Boolean {
			return isNearActiveAntiField(entity.level(), entity.blockPosition())
		}

		fun isNearActiveAntiField(
			level: Level,
			location: BlockPos
		): Boolean {
			if (level !is AntiFieldCarrier) return false

			val antiFieldBlocks = level.getAntiFieldBlocks()
			if (antiFieldBlocks.isEmpty()) return false

			val radius = ServerConfig.CONFIG.antifieldBlockRadius.get()

			for (posLong in antiFieldBlocks) {
				val pos = posLong.toBlockPos()
				if (pos.closerThan(location, radius)) return true
			}

			return false
		}

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: AntiFieldBlockEntity
		) {
			val isDisabled = blockState.getValue(AntiFieldBlock.POWERED)

			if (isDisabled) {
				blockEntity.removeFromList()
			} else {
				blockEntity.addToList()
			}
		}

	}

}