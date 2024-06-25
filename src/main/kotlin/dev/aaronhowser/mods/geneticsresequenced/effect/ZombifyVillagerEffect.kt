package dev.aaronhowser.mods.geneticsresequenced.effect

import net.minecraft.nbt.NbtOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.monster.Zombie.ZombieGroupData
import net.minecraft.world.entity.npc.Villager
import net.neoforged.neoforge.event.EventHooks

class ZombifyVillagerEffect : MobEffect(
    MobEffectCategory.HARMFUL,
    0x63873D
) {

    override fun isInstantenous(): Boolean = true

    /**
     * See [net.minecraft.world.entity.monster.Zombie.killedEntity]
     */
    override fun applyInstantenousEffect(
        pSource: Entity?,
        pIndirectSource: Entity?,
        villager: LivingEntity,
        pAmplifier: Int,
        pHealth: Double
    ) {
        if (villager.level().isClientSide) return
        if (villager !is Villager) return

        val serverLevel = villager.level() as? ServerLevel ?: return

        val zombieVillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false) ?: return
        zombieVillager.apply {

            finalizeSpawn(
                serverLevel,
                serverLevel.getCurrentDifficultyAt(zombieVillager.blockPosition()),
                MobSpawnType.CONVERSION,
                ZombieGroupData(false, true),
            )

            villagerData = villager.villagerData
            setGossips(villager.gossips.store(NbtOps.INSTANCE))
            setTradeOffers(villager.offers.copy())
            villagerXp = villager.villagerXp
        }

        EventHooks.onLivingConvert(villager, zombieVillager)

        if (!villager.isSilent) {
            serverLevel.levelEvent(null, 1026, villager.blockPosition(), 0)
        }
    }

}