package dev.aaronhowser.mods.geneticsresequenced.entities

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.monster.Slime
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import java.util.*
import kotlin.math.max

class SupportSlime(
    pEntityType: EntityType<out SupportSlime>,
    pLevel: Level
) : Slime(pEntityType, pLevel) {

    companion object {
        fun setAttributes(): AttributeSupplier {
            return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 20.0)
                .build()
        }

        private const val OWNER_UUID_NBT = "OwnerUUID"
        private val OWNER: EntityDataAccessor<Optional<UUID>> =
            SynchedEntityData.defineId(SupportSlime::class.java, EntityDataSerializers.OPTIONAL_UUID)

    }

    override fun defineSynchedData() {
        this.entityData.define(OWNER, Optional.empty())
        super.defineSynchedData()
    }

    override fun onAddedToWorld() {

        if (level.isClientSide) {
            return super.onAddedToWorld()
        }

        val nearbyLivingEntities: List<LivingEntity> = level.getEntitiesOfClass(
            LivingEntity::class.java,
            boundingBox.inflate(16.0)
        ).sortedBy { it.distanceToSqr(this) }

        for (livingEntity in nearbyLivingEntities) {
            val genes: GenesCapability = livingEntity.getGenes() ?: continue
            if (genes.hasGene(DefaultGenes.SLIMY_DEATH)) {
                setOwner(livingEntity.uuid)
                break
            }
        }

        if (getOwner() == null) {
            this.remove(RemovalReason.DISCARDED)
            GeneticsResequenced.LOGGER.warn("Friendly Slime spawned without an owner!")
        }

        super.onAddedToWorld()
    }

    private fun setOwner(ownerUuid: UUID) {
        entityData.set(OWNER, Optional.of(ownerUuid))
    }

    private fun getOwner(): UUID? {
        return try {
            entityData.get(OWNER).orElse(null)
        } catch (e: NullPointerException) {
            null
        }
    }

    override fun readAdditionalSaveData(pCompound: CompoundTag) {
        super.readAdditionalSaveData(pCompound)
        if (pCompound.hasUUID(OWNER_UUID_NBT)) {
            this.setOwner(pCompound.getUUID(OWNER_UUID_NBT))
        }
    }

    override fun addAdditionalSaveData(pCompound: CompoundTag) {
        super.addAdditionalSaveData(pCompound)
        val owner = this.getOwner()
        if (owner != null) {
            pCompound.putUUID(OWNER_UUID_NBT, owner)
        }
    }

    override fun setSize(pSize: Int, pResetHealth: Boolean) {
        super.setSize(pSize, pResetHealth)
        getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue = pSize * 3.0
        getAttribute(Attributes.MOVEMENT_SPEED)?.baseValue = 0.4 + 0.2 * pSize
    }


    override fun isDealsDamage(): Boolean = isEffectiveAi

    private fun shouldSlimeAttackEntity(livingEntity: LivingEntity): Boolean {
        val owner: UUID = getOwner() ?: return false
        val mob: Mob = livingEntity as? Mob ?: return false

        val mobIsAttackingOwner = mob.target?.uuid == owner
        return mobIsAttackingOwner
    }

    override fun registerGoals() {
        super.registerGoals()

        goalSelector.addGoal(1, SupportSlimeAttackGoal(this))

        targetSelector.removeAllGoals()
        targetSelector.addGoal(
            1,
            NearestAttackableTargetGoal(
                this,
                Mob::class.java,
                true
            ) { livingEntity -> shouldSlimeAttackEntity(livingEntity) }
        )

    }

    class SupportSlimeAttackGoal(
        private val mob: SupportSlime
    ) : Goal() {
        private var ticksUntilNextAttack = 0

        override fun canUse(): Boolean = true

        override fun start() {
            val target = mob.target as? Entity ?: return
            mob.isAggressive = true
            mob.navigation.moveTo(target, 1.0)
            ticksUntilNextAttack = 0
        }

        override fun stop() {
            mob.isAggressive = false
            mob.target = null
            mob.navigation.stop()
        }

        override fun tick() {
            val target = mob.target ?: return
            mob.getLookControl().setLookAt(target, 30.0f, 30.0f)
            val distanceSqr = mob.distanceToSqr(target.x, target.y, target.z)

            this.ticksUntilNextAttack = max(
                ticksUntilNextAttack - 1,
                0
            )
            this.checkAndPerformAttack(target, distanceSqr)
        }


        private fun checkAndPerformAttack(pEnemy: LivingEntity, pDistToEnemySqr: Double) {
            val d0: Double = this.getAttackReachSqr(pEnemy)
            if (pDistToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown()
                this.mob.doHurtTarget(pEnemy)
            }
        }

        private fun resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20)
        }

        private fun getAttackReachSqr(pAttackTarget: LivingEntity): Double {
            return (mob.bbWidth * 2.0f * mob.bbWidth * 2.0f + pAttackTarget.bbWidth).toDouble()
        }
    }

}