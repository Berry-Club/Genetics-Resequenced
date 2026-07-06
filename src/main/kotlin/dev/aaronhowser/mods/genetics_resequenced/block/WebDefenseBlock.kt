package dev.aaronhowser.mods.genetics_resequenced.block

import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.InsideBlockEffectApplier
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

class WebDefenseBlock(properties: Properties) : Block(properties) {

	override fun onPlace(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		oldState: BlockState,
		movedByPiston: Boolean
	) {
		level.scheduleTick(pos, this, ServerConfig.CONFIG.webDefenseDuration.get())
	}

	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		level.removeBlock(pos, false)
	}

	override fun entityInside(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		entity: Entity,
		effectApplier: InsideBlockEffectApplier,
		isPrecise: Boolean
	) {
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

	companion object {
		fun properties(): Properties = Properties.ofFullCopy(Blocks.COBWEB)
	}

}
