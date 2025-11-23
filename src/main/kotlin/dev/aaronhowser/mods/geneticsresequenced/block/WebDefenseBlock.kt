package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.BlockPos
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

class WebDefenseBlock : Block(Properties.ofFullCopy(Blocks.COBWEB)) {

	override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {

		var motionMultiplier = Vec3(0.25, 0.05, 0.25)

		if (entity is LivingEntity) {
			if (entity.hasEffect(MobEffects.WEAVING)) {
				motionMultiplier = Vec3(0.5, 0.25, 0.5)
			} else {
				val webWalker = ModGenes.WEB_WALKER.getHolderOrThrow(level.registryAccess())
				val webDefense = ModGenes.WEB_DEFENSE.getHolderOrThrow(level.registryAccess())

				if (entity.hasGene(webWalker) || entity.hasGene(webDefense)) {
					return
				}
			}
		}

		if (entity is LivingEntity && entity.hasEffect(MobEffects.WEAVING)) {
			motionMultiplier = Vec3(0.5, 0.25, 0.5)
		}

		entity.makeStuckInBlock(state, motionMultiplier)
	}

}