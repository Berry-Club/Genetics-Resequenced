package dev.aaronhowser.mods.geneticsresequenced.entities

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.entities.goals.SupportSlimeAttackGoal
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registries.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.getUuidOrNull
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.monster.Slime
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import java.util.*

class SupportSlime(
    pEntityType: EntityType<out SupportSlime>,
    pLevel: Level
) : Slime(pEntityType, pLevel) {

    constructor(
        level: Level,
        ownerUuid: UUID
    ) : this(ModEntityTypes.SUPPORT_SLIME.get(), level) {
        setOwner(ownerUuid)
    }

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

        fun spawnEggMessage(event: PlayerInteractEvent.RightClickBlock) {
            if (event.side.isClient) return

            val player = event.entity
            val item = event.itemStack

            if (!item.`is`(ModItems.FRIENDLY_SLIME_SPAWN_EGG.get())) return

            if (player.hasGene(ModGenes.slimyDeath)) return

            player.sendSystemMessage(
                Component.translatable(
                    "message.geneticsresequenced.support_slime_creative",
                    ModGenes.slimyDeath.nameComponent
                )
            )
        }
    }

    override fun defineSynchedData() {
        this.entityData.define(OWNER, Optional.empty())
        super.defineSynchedData()
    }

    override fun onAddedToWorld() {

        if (level.isClientSide) {
            return super.onAddedToWorld()
        }

        if (getOwnerUuid() == null) setOwnerIfNotSet()

        super.onAddedToWorld()
    }

    private fun setOwnerIfNotSet() {
        val nearbyLivingEntities: List<LivingEntity> = level.getEntitiesOfClass(
            LivingEntity::class.java,
            boundingBox.inflate(16.0)
        ).sortedBy { it.distanceToSqr(this) }

        for (livingEntity in nearbyLivingEntities) {
            if (livingEntity.hasGene(ModGenes.slimyDeath)) {
                setOwner(livingEntity.uuid)
                break
            }
        }

        if (getOwnerUuid() == null) {
            this.remove(RemovalReason.DISCARDED)
            GeneticsResequenced.LOGGER.warn("Friendly Slime spawned without an owner!")
        }
    }

    private fun setOwner(ownerUuid: UUID) {
        entityData.set(OWNER, Optional.of(ownerUuid))
    }

    override fun tick() {
        super.tick()
        checkIfShouldDespawn()
    }

    private var ticksWithoutTarget = 0
    private var despawnAnimationPlaying = false

    private fun checkIfShouldDespawn() {
        if (despawnAnimationPlaying) return

        val nearbyEntities = level.getEntities(
            this,
            boundingBox.inflate(16.0)
        )

        var areThereNearbyHostiles = false
        var isOwnerNearby = false

        for (entity in nearbyEntities) {
            if (areThereNearbyHostiles && isOwnerNearby) break

            if (entity.uuid == getOwnerUuid()) {
                isOwnerNearby = true
            }

            if (entity is Mob && entity.target?.uuid == getOwnerUuid()) {
                areThereNearbyHostiles = true
            }
        }

        if (!isOwnerNearby) {
            despawn()
        }

        if (areThereNearbyHostiles) {
            ticksWithoutTarget = 0
        } else {
            ticksWithoutTarget++
            if (ticksWithoutTarget > 20 * 10) {
                despawn()
            }
        }
    }

    private fun despawn() {
        despawnAnimationPlaying = true

        if (size <= 1) {
            this.remove(RemovalReason.DISCARDED)
            return
        }
        setSize(size - 1, true)
        ModScheduler.scheduleTaskInTicks(30) {
            despawn()
        }
    }

    fun getOwnerUuid(): UUID? {
        return try {
            entityData.get(OWNER).orElse(null)
        } catch (e: NullPointerException) {
            null
        }
    }

    override fun readAdditionalSaveData(pCompound: CompoundTag) {
        super.readAdditionalSaveData(pCompound)

        val owner = pCompound.getUuidOrNull(OWNER_UUID_NBT) ?: return
        setOwner(owner)
    }

    override fun addAdditionalSaveData(pCompound: CompoundTag) {
        super.addAdditionalSaveData(pCompound)
        val owner = this.getOwnerUuid()
        if (owner != null) {
            pCompound.putUUID(OWNER_UUID_NBT, owner)
        }
    }

    override fun setSize(pSize: Int, pResetHealth: Boolean) {
        super.setSize(pSize, pResetHealth)
        getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue = pSize * 3.0
        getAttribute(Attributes.MOVEMENT_SPEED)?.baseValue = 0.4 + 0.2 * pSize
    }

    override fun push(pEntity: Entity) {
        if (pEntity.uuid != getOwnerUuid()) super.push(pEntity)
    }

    override fun playerTouch(pEntity: Player) {}

    override fun isDealsDamage(): Boolean = isEffectiveAi

    private fun shouldSlimeAttackEntity(livingEntity: LivingEntity): Boolean {
        val owner: UUID = getOwnerUuid() ?: return false
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

}