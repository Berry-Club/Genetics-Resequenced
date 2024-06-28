package dev.aaronhowser.mods.geneticsresequenced.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.entity.goals.SupportSlimeAttackGoal
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.getUuidOrNull
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.Difficulty
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
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import java.util.*

class SupportSlime(
    pEntityType: EntityType<SupportSlime>,
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

        //TODO: Make this a DataAttachment
        private const val OWNER_UUID_NBT_KEY = "OwnerUUID"
        private val OWNER: EntityDataAccessor<Optional<UUID>> =
            SynchedEntityData.defineId(SupportSlime::class.java, EntityDataSerializers.OPTIONAL_UUID)

        fun spawnEggMessage(event: PlayerInteractEvent.RightClickBlock) {
            if (event.side.isClient) return

            val player = event.entity
            val item = event.itemStack

            if (!item.`is`(ModItems.FRIENDLY_SLIME_SPAWN_EGG.get())) return

            if (!player.hasGene(ModGenes.slimyDeath)) {
                player.sendSystemMessage(
                    ModLanguageProvider.Messages.SUPPORT_SLIME_CREATIVE.toComponent(
                        ModGenes.slimyDeath.nameComponent
                    )
                )
            }

            if (player.level().difficulty == Difficulty.PEACEFUL) {
                player.sendSystemMessage(
                    ModLanguageProvider.Messages.SUPPORT_SLIME_PEACEFUL.toComponent()
                )
            }

        }
    }

    override fun defineSynchedData(pBuilder: SynchedEntityData.Builder) {
        pBuilder.define(OWNER, Optional.empty())
        super.defineSynchedData(pBuilder)
    }

    override fun onAddedToWorld() {
        if (level().isClientSide) {
            return super.onAddedToWorld()
        }

        if (getOwnerUuid() == null) {
            setOwnerIfNotSet()
        }

        super.onAddedToWorld()
    }

    private fun setOwnerIfNotSet() {
        val nearbyLivingEntities = level().getEntitiesOfClass(
            LivingEntity::class.java,
            boundingBox.inflate(10.0)
        ).sortedByDescending { distanceToSqr(it) }

        val owner = nearbyLivingEntities.firstOrNull { it.hasGene(ModGenes.slimyDeath) }
        if (owner != null) {
            setOwner(owner.uuid)
        } else {
            GeneticsResequenced.LOGGER.warn("Support Slime spawned without an owner!")
            this.remove(RemovalReason.DISCARDED)
        }
    }

    fun getOwnerUuid(): UUID? {
        return try {
            entityData.get(OWNER).orElse(null)
        } catch (e: NullPointerException) {
            null
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
        if (isNoAi) return
        if (despawnAnimationPlaying) return

        if (tickCount % ServerConfig.slimyDeathDespawnCheckTimer.get() != 0) return

        val nearbyEntities = level().getEntities(
            this,
            boundingBox.inflate(16.0)
        )

        var nearEnemies = false
        var nearOwner = false

        for (entity in nearbyEntities) {
            if (nearEnemies && nearOwner) break

            if (entity.uuid == getOwnerUuid()) {
                nearOwner = true
            }

            if (entity is Mob && entity.target?.uuid == getOwnerUuid()) {
                nearEnemies = true
            }
        }

        if (!nearOwner) {
            despawn()
        }

        if (nearEnemies) {
            ticksWithoutTarget = 0
        } else {
            ticksWithoutTarget += ServerConfig.slimyDeathDespawnCheckTimer.get()
            if (ticksWithoutTarget > ServerConfig.slimyDeathDespawnTime.get()) {
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

    override fun readAdditionalSaveData(pCompound: CompoundTag) {
        super.readAdditionalSaveData(pCompound)

        val owner = pCompound.getUuidOrNull(OWNER_UUID_NBT_KEY) ?: return
        setOwner(owner)
    }

    override fun addAdditionalSaveData(pCompound: CompoundTag) {
        super.addAdditionalSaveData(pCompound)

        val owner = getOwnerUuid()
        if (owner != null) {
            pCompound.putUUID(OWNER_UUID_NBT_KEY, owner)
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

    private fun shouldSlimeAttackEntity(livingEntity: LivingEntity): Boolean {
        val owner: UUID = getOwnerUuid() ?: return false
        val mob: Mob = livingEntity as? Mob ?: return false

        val mobIsAttackingOwner = mob.target?.uuid == owner
        return mobIsAttackingOwner
    }

    override fun registerGoals() {
        super.registerGoals()
        goalSelector.addGoal(1, SupportSlimeAttackGoal(this))

        targetSelector.removeAllGoals { true }
        targetSelector.addGoal(
            1,
            NearestAttackableTargetGoal(
                this,
                Mob::class.java,
                true
            ) { shouldSlimeAttackEntity(it) }
        )

    }


}