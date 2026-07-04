package dev.aaronhowser.mods.geneticsresequenced.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.entity.goals.SupportSlimeAttackGoal
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.monster.Slime
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import java.util.*

class SupportSlime(
	entityType: EntityType<SupportSlime>,
	level: Level
) : Slime(entityType, level) {

	constructor(
		level: Level,
		ownerUuid: UUID
	) : this(ModEntityTypes.SUPPORT_SLIME.get(), level) {
		this.ownerUuid = ownerUuid
	}

	var ownerUuid: UUID?
		get() = entityData.get(OWNER)
			.takeIf(String::isNotEmpty)
			?.let(UUID::fromString)
		private set(value) {
			entityData.set(OWNER, value?.toString() ?: "")
		}

	private fun setOwner(entity: Entity) {
		this.ownerUuid = entity.uuid
	}

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		super.defineSynchedData(builder)
		builder.define(OWNER, "")
	}

	override fun onAddedToLevel() {
		if (isServerSide) {
			setOwnerIfNotSet()
		}

		super.onAddedToLevel()
	}

	private fun setOwnerIfNotSet() {
		if (ownerUuid != null) return

		val nearbyLivingEntities = level().getEntitiesOfClass(
			LivingEntity::class.java,
			this.boundingBox.inflate(10.0)
		).sortedByDescending { distanceToSqr(it) }

		val owner = nearbyLivingEntities.firstOrNull { it.hasGene(ModGenes.SLIMY_DEATH) }
		if (owner != null) {
			ownerUuid = owner.uuid
		} else {
			GeneticsResequenced.LOGGER.warn("Support Slime spawned without an owner!")
			this.remove(RemovalReason.DISCARDED)
		}
	}

	override fun tick() {
		super.tick()
		checkIfShouldDespawn()
	}

	private var ticksWithoutTarget = 0
	private var despawnAnimationPlaying = false

	private fun checkIfShouldDespawn() {
		if (this.isNoAi) return
		if (this.despawnAnimationPlaying) return

		if (this.tickCount % ServerConfig.CONFIG.slimyDeathDespawnCheckTimer.get() != 0) return

		val nearbyEntities = level().getEntities(
			this,
			this.boundingBox.inflate(16.0)
		)

		var nearEnemies = false
		var nearOwner = false

		for (entity in nearbyEntities) {
			if (nearEnemies && nearOwner) break

			if (entity.uuid == uuid) {
				nearOwner = true
			}

			if (entity is Mob && entity.target?.uuid == ownerUuid) {
				nearEnemies = true
			}
		}

		if (!nearOwner) {
			despawn()
		}

		if (nearEnemies) {
			this.ticksWithoutTarget = 0
		} else {
			this.ticksWithoutTarget += ServerConfig.CONFIG.slimyDeathDespawnCheckTimer.get()
			if (this.ticksWithoutTarget > ServerConfig.CONFIG.slimyDeathDespawnTime.get()) {
				despawn()
			}
		}
	}

	private fun despawn() {
		this.despawnAnimationPlaying = true

		if (this.size <= 1) {
			this.remove(RemovalReason.DISCARDED)
			return
		}

		setSize(this.size - 1, true)

		level().scheduleTaskInTicks(30) {
			despawn()
		}
	}

	override fun readAdditionalSaveData(input: ValueInput) {
		super.readAdditionalSaveData(input)

		this.ownerUuid = input.getString(OWNER_UUID_NBT_KEY)
			.map(UUID::fromString)
			.orElse(null)
	}

	override fun addAdditionalSaveData(output: ValueOutput) {
		super.addAdditionalSaveData(output)

		ownerUuid?.let { output.putString(OWNER_UUID_NBT_KEY, it.toString()) }
	}

	override fun setSize(size: Int, resetHealth: Boolean) {
		super.setSize(size, resetHealth)

		getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue = size * 3.0
		getAttribute(Attributes.MOVEMENT_SPEED)?.baseValue = 0.4 + 0.2 * size
	}

	override fun push(entity: Entity) {
		if (entity.uuid != ownerUuid) super.push(entity)
	}

	override fun playerTouch(entity: Player) {}

	private fun shouldSlimeAttackEntity(livingEntity: LivingEntity): Boolean {
		val mob = livingEntity as? Mob ?: return false

		val mobIsAttackingOwner = ownerUuid != null && mob.target?.uuid == ownerUuid
		return mobIsAttackingOwner
	}

	override fun registerGoals() {
		super.registerGoals()
		this.goalSelector.addGoal(1, SupportSlimeAttackGoal(this))

		this.targetSelector.removeAllGoals { true }
		this.targetSelector.addGoal(
			1,
			NearestAttackableTargetGoal(
				this,
				Mob::class.java,
				true,
				TargetingConditions.Selector { target, _ -> shouldSlimeAttackEntity(target) }
			)
		)

	}

	companion object {
		fun setAttributes(): AttributeSupplier {
			return createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 20.0)
				.build()
		}

		private const val OWNER_UUID_NBT_KEY = "OwnerUUID"
		private val OWNER: EntityDataAccessor<String> =
			SynchedEntityData.defineId(SupportSlime::class.java, EntityDataSerializers.STRING)

		fun spawnEggMessage(event: PlayerInteractEvent.RightClickBlock) {
			if (event.side.isClient) return

			val player = event.entity
			val item = event.itemStack

			if (!item.isItem(ModItems.FRIENDLY_SLIME_SPAWN_EGG.get())) return

			if (!player.hasGene(ModGenes.SLIMY_DEATH)) {
				player.sendSystemMessage(
					ModMessageLang.SUPPORT_SLIME_CREATIVE.toComponent(
						Gene.getNameComponent(
							ModGenes.SLIMY_DEATH,
							ClientUtil.localRegistryAccess!!
						)
					)
				)
			}

			if (player.level().difficulty == Difficulty.PEACEFUL) {
				player.sendSystemMessage(
					ModMessageLang.SUPPORT_SLIME_PEACEFUL.toComponent()
				)
			}

		}
	}

}
