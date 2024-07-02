package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import kotlin.random.Random

@Suppress("UnstableApiUsage")
object DamageGenes {

    // Canceling

    fun handleNoFallDamage(event: LivingIncomingDamageEvent) {
        if (!ModGenes.NO_FALL_DAMAGE.get().isActive) return

        if (!event.source.`is`(DamageTypes.FALL)) return

        val entity = event.entity
        if (entity.hasGene(ModGenes.NO_FALL_DAMAGE.get())) {
            event.amount = 0f
        }
    }

    fun handleWitherProof(event: LivingIncomingDamageEvent) {
        if (!ModGenes.WITHER_PROOF.get().isActive) return

        if (!event.source.`is`(DamageTypes.WITHER)) return

        val entity = event.entity

        if (entity.hasGene(ModGenes.WITHER_PROOF.get())) {
            entity.removeEffect(MobEffects.WITHER)
            event.amount = 0f
        }
    }

    fun handleFireProof(event: LivingIncomingDamageEvent) {
        if (!ModGenes.FIRE_PROOF.get().isActive) return

        if (!event.source.`is`(DamageTypes.IN_FIRE) && !event.source.`is`(DamageTypes.ON_FIRE)) return

        val entity = event.entity

        if (entity.hasGene(ModGenes.FIRE_PROOF.get())) {
            entity.clearFire()
            event.amount = 0f
        }
    }

    fun handlePoisonProof(event: LivingIncomingDamageEvent) {
        if (!ModGenes.POISON_IMMUNITY.get().isActive) return

        if (!event.source.`is`(NeoForgeMod.POISON_DAMAGE)) return

        val entity = event.entity
        if (entity.hasGene(ModGenes.POISON_IMMUNITY.get())) {
            entity.removeEffect(MobEffects.POISON)
            event.amount = 0f
        }
    }

    // Changing amount (not just canceling)

    fun handleDragonHealth(event: LivingDamageEvent.Pre) {
        if (!ModGenes.ENDER_DRAGON_HEALTH.get().isActive) return

        if (event.container.newDamage == 0f) return
        val entity = event.entity

        if (entity.level().isClientSide) return

        if (!entity.hasGene(ModGenes.ENDER_DRAGON_HEALTH.get())) return

        val items = entity.handSlots.toMutableList()
        if (entity is Player) items += entity.inventory.items

        val healthCrystal = items.find { it.item == ModItems.DRAGON_HEALTH_CRYSTAL } ?: return

        val amountDamaged = Mth.ceil(event.container.newDamage)
        val crystalDurabilityRemaining = healthCrystal.maxDamage - healthCrystal.damageValue
        val amountToBlock = minOf(amountDamaged, crystalDurabilityRemaining)

        healthCrystal.hurtAndBreak(amountToBlock, entity, entity.getEquipmentSlotForItem(healthCrystal))

        event.container.newDamage -= amountToBlock
        if (event.container.newDamage < 0f) event.container.newDamage = 0f
    }

    fun handleJohnny(event: LivingDamageEvent.Pre) {
        if (!ModGenes.JOHNNY.get().isActive) return

        val attacker = event.container.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.JOHNNY.get())) return

        val weaponIsAxe = attacker.mainHandItem.item is AxeItem //Is there a better way of doing this?
        if (!weaponIsAxe) return

        event.container.newDamage *= ServerConfig.johnnyAttackMultiplier.get().toFloat()
    }

    // Triggers

    fun handleWitherHit(event: LivingDamageEvent.Post) {
        if (!ModGenes.WITHER_HIT.get().isActive) return

        // Makes it not proc if it's an arrow or whatever
        if (!event.source.isDirect) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.WITHER_HIT.get())) return

        val witherEffect = MobEffectInstance(
            MobEffects.WITHER,
            100
        )

        event.entity.addEffect(witherEffect)
    }

    fun handleThorns(event: LivingDamageEvent.Post) {
        if (!ModGenes.THORNS.get().isActive) return

        val attacker = event.source.entity as? LivingEntity ?: return

        val target = event.entity as? Mob ?: event.entity as? Player ?: return
        if (target == attacker) return

        val chestPlate = target.getItemBySlot(EquipmentSlot.CHEST)
        val targetChestplateMissingOrLeather = chestPlate.isEmpty || chestPlate.`is`(Items.LEATHER_CHESTPLATE)
        if (!targetChestplateMissingOrLeather) return

        if (!target.hasGene(ModGenes.THORNS.get())) return

        if (Random.nextDouble() > ServerConfig.thornsChance.get()) return

        val thornsDamageSource = target.level().damageSources().thorns(target)
        attacker.hurt(thornsDamageSource, ServerConfig.thornsDamage.get().toFloat())

        if (target is Player) {
            target.causeFoodExhaustion(ServerConfig.thornsHungerDrain.get().toFloat())
        }
    }

    fun handleClaws(event: LivingDamageEvent.Post) {
        if (!ModGenes.CLAWS.get().isActive) return

        val attacker = event.source.entity as? LivingEntity ?: return

        if (!attacker.mainHandItem.isEmpty) return

        val clawsLevel: Int = if (ModGenes.CLAWS_TWO.get().isActive && attacker.hasGene(ModGenes.CLAWS_TWO.get())) {
            2
        } else if (attacker.hasGene(ModGenes.CLAWS.get())) {
            1
        } else {
            return
        }

        val chanceOfHappening = ServerConfig.clawsChance.get() * clawsLevel

        if (Random.nextDouble() > chanceOfHappening) return

        event.entity.addEffect(
            MobEffectInstance(
                ModEffects.BLEED,
                20 * 5,
                0,
                false,
                true,
                true
            ),
            attacker
        )
    }

    fun handleChilling(event: LivingDamageEvent.Post) {
        if (!ModGenes.CHILLING.get().isActive) return

        if (!event.source.isDirect) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.CHILLING.get())) return

        if (Random.nextDouble() > ServerConfig.chillChance.get()) return

        val target = event.entity
        target.ticksFrozen = ServerConfig.chillDuration.get()
    }

}