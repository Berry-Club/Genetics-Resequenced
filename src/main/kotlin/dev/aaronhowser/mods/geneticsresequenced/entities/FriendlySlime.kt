package dev.aaronhowser.mods.geneticsresequenced.entities

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.monster.Slime
import net.minecraft.world.level.Level
import java.util.*

class FriendlySlime(
    pEntityType: EntityType<out FriendlySlime>,
    pLevel: Level
) : Slime(pEntityType, pLevel) {

    companion object {
        fun setAttributes(): AttributeSupplier {
            return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .build()
        }

        const val OWNER_UUID_NBT = "OwnerUUID"
        val OWNER: EntityDataAccessor<Optional<UUID>> =
            SynchedEntityData.defineId(FriendlySlime::class.java, EntityDataSerializers.OPTIONAL_UUID)
    }

    override fun defineSynchedData() {
        this.entityData.define(OWNER, Optional.empty())
        super.defineSynchedData()
    }

    override fun onAddedToWorld() {

        if (level.isClientSide) {
            return super.onAddedToWorld()
        }

        val nearbyEntities: List<LivingEntity> = level.getEntitiesOfClass(
            LivingEntity::class.java,
            boundingBox.inflate(16.0)
        )

        for (entity in nearbyEntities) {
            val genes: GenesCapability = entity.getGenes() ?: continue
            if (genes.hasGene(DefaultGenes.SLIMY_DEATH)) {
                setOwner(entity.uuid)
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

    override fun registerGoals() {
        super.registerGoals()

        targetSelector.removeAllGoals()

        targetSelector.addGoal(
            1,
            NearestAttackableTargetGoal(
                this,
                LivingEntity::class.java,
                true
            ) { true }
        )

    }

}