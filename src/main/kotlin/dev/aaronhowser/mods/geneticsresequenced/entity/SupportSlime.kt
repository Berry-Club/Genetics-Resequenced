package dev.aaronhowser.mods.geneticsresequenced.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.monster.Slime
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

    override fun defineSynchedData(pBuilder: SynchedEntityData.Builder) {
        pBuilder.define(OWNER, Optional.empty())
        super.defineSynchedData(pBuilder)
    }

    override fun onAddedToWorld() {

        if (level().isClientSide) {
            return super.onAddedToWorld()
        }

    }

    private fun setOwner() {

        val nearbyLivingEntities = level().getEntitiesOfClass(
            LivingEntity::class.java,
            boundingBox.inflate(10.0)
        ).sortedByDescending { distanceToSqr(it) }

        for (livingEntity in nearbyLivingEntities) {
            if (livingEntity.hasGene(ModGenes.slimyDeath)) {
                setOwner(livingEntity.uuid)
                return
            }
        }

        if (getOwnerUuid() == null) {
            this.remove(RemovalReason.DISCARDED)
            GeneticsResequenced.LOGGER.warn("Support Slime spawned without an owner!")
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


}