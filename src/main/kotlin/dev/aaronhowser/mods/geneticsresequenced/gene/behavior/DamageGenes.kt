package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEffects
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
        val noFallDamage = ModGenes.NO_FALL_DAMAGE.getHolder(event.entity.registryAccess()) ?: return
        if (noFallDamage.isDisabled) return

        if (!event.source.`is`(DamageTypes.FALL)) return

        val entity = event.entity
        if (entity.hasGene(ModGenes.NO_FALL_DAMAGE)) {
            event.amount = 0f
        }
    }

    fun handleWitherProof(event: LivingIncomingDamageEvent) {
        val witherProof = ModGenes.WITHER_PROOF.getHolder(event.entity.registryAccess()) ?: return
        if (witherProof.isDisabled) return

        if (!event.source.`is`(DamageTypes.WITHER)) return

        val entity = event.entity

        if (entity.hasGene(ModGenes.WITHER_PROOF)) {
            entity.removeEffect(MobEffects.WITHER)
            event.amount = 0f
        }
    }

    fun handleFireProof(event: LivingIncomingDamageEvent) {
        val fireProof = ModGenes.FIRE_PROOF.getHolder(event.entity.registryAccess()) ?: return
        if (fireProof.isDisabled) return

        if (!event.source.`is`(DamageTypes.IN_FIRE) && !event.source.`is`(DamageTypes.ON_FIRE)) return

        val entity = event.entity

        if (entity.hasGene(ModGenes.FIRE_PROOF)) {
            entity.clearFire()
            event.amount = 0f
        }
    }

    fun handlePoisonProof(event: LivingIncomingDamageEvent) {
        val poisonImmunity = ModGenes.POISON_IMMUNITY.getHolder(event.entity.registryAccess()) ?: return
        if (poisonImmunity.isDisabled) return

        if (!event.source.`is`(NeoForgeMod.POISON_DAMAGE)) return

        val entity = event.entity
        if (entity.hasGene(ModGenes.POISON_IMMUNITY)) {
            entity.removeEffect(MobEffects.POISON)
            event.amount = 0f
        }
    }

    // Changing amount (not just canceling)

    fun handleDragonHealth(event: LivingDamageEvent.Pre) {
        val enderDragonHealth = ModGenes.ENDER_DRAGON_HEALTH.getHolder(event.entity.registryAccess()) ?: return
        if (enderDragonHealth.isDisabled) return

        if (event.container.newDamage == 0f) return
        val entity = event.entity

        if (entity.level().isClientSide) return

        if (!entity.hasGene(ModGenes.ENDER_DRAGON_HEALTH)) return

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
        val johnny = ModGenes.JOHNNY.getHolder(event.entity.registryAccess()) ?: return
        if (johnny.isDisabled) return

        val attacker = event.container.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.JOHNNY)) return

        val weaponIsAxe = attacker.mainHandItem.item is AxeItem //Is there a better way of doing this?
        if (!weaponIsAxe) return

        event.container.newDamage *= ServerConfig.johnnyAttackMultiplier.get().toFloat()
    }

    // Triggers

    fun handleWitherHit(event: LivingDamageEvent.Post) {
        val witherHit = ModGenes.WITHER_HIT.getHolder(event.entity.registryAccess()) ?: return
        if (witherHit.isDisabled) return

        // Makes it not proc if it's an arrow or whatever
        if (!event.source.isDirect) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.WITHER_HIT)) return

        val witherEffect = MobEffectInstance(
            MobEffects.WITHER,
            100
        )

        event.entity.addEffect(witherEffect)
    }

    fun handleThorns(event: LivingDamageEvent.Post) {
        val thorns = ModGenes.THORNS.getHolder(event.entity.registryAccess()) ?: return
        if (thorns.isDisabled) return

        val attacker = event.source.entity as? LivingEntity ?: return

        val target = event.entity as? Mob ?: event.entity as? Player ?: return
        if (target == attacker) return

        val chestPlate = target.getItemBySlot(EquipmentSlot.CHEST)
        val targetChestplateMissingOrLeather = chestPlate.isEmpty || chestPlate.`is`(Items.LEATHER_CHESTPLATE)
        if (!targetChestplateMissingOrLeather) return

        if (!target.hasGene(ModGenes.THORNS)) return

        if (Random.nextDouble() > ServerConfig.thornsChance.get()) return

        val thornsDamageSource = target.level().damageSources().thorns(target)
        attacker.hurt(thornsDamageSource, ServerConfig.thornsDamage.get().toFloat())

        if (target is Player) {
            target.causeFoodExhaustion(ServerConfig.thornsHungerDrain.get().toFloat())
        }
    }

    fun handleClaws(event: LivingDamageEvent.Post) {
        val claws = ModGenes.CLAWS.getHolder(event.entity.registryAccess()) ?: return
        if (claws.isDisabled) return

        val attacker = event.source.entity as? LivingEntity ?: return

        if (!attacker.mainHandItem.isEmpty) return

        val clawsTwo = ModGenes.CLAWS_TWO.getHolder(event.entity.registryAccess()) ?: return
        val clawsLevel: Int = if (!clawsTwo.isDisabled && attacker.hasGene(ModGenes.CLAWS_TWO)) {
            2
        } else if (attacker.hasGene(ModGenes.CLAWS)) {
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
        val chilling = ModGenes.CHILLING.getHolder(event.entity.registryAccess()) ?: return
        if (chilling.isDisabled) return

        if (!event.source.isDirect) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.CHILLING)) return

        if (Random.nextDouble() > ServerConfig.chillChance.get()) return

        val target = event.entity
        target.ticksFrozen = ServerConfig.chillDuration.get()
    }

}