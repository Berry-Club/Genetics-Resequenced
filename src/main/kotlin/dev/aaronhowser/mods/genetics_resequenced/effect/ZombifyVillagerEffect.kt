package dev.aaronhowser.mods.genetics_resequenced.effect

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.*
import net.minecraft.world.entity.monster.zombie.Zombie.ZombieGroupData
import net.minecraft.world.entity.npc.villager.Villager
import net.neoforged.neoforge.event.EventHooks

class ZombifyVillagerEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0x63873D
) {

	override fun isInstantenous(): Boolean = true

	/** @see [net.minecraft.world.entity.monster.Zombie.killedEntity] */
	override fun applyInstantenousEffect(
		serverLevel: ServerLevel,
		pSource: Entity?,
		pIndirectSource: Entity?,
		villager: LivingEntity,
		pAmplifier: Int,
		pHealth: Double
	) {
		if (villager !is Villager) return

		villager.convertTo(
			EntityType.ZOMBIE_VILLAGER,
			ConversionParams.single(villager, true, true)
		) { zombie ->
			zombie.finalizeSpawn(
				serverLevel,
				serverLevel.getCurrentDifficultyAt(zombie.blockPosition()),
				EntitySpawnReason.CONVERSION,
				ZombieGroupData(false, true)
			)

			zombie.villagerData = villager.villagerData
			zombie.setGossips(villager.gossips.copy())
			zombie.setTradeOffers(villager.offers.copy())
			zombie.villagerXp = villager.villagerXp

			EventHooks.onLivingConvert(villager, zombie)

			if (!villager.isSilent) {
				serverLevel.levelEvent(null, 1026, villager.blockPosition(), 0)
			}
		}
	}

}
