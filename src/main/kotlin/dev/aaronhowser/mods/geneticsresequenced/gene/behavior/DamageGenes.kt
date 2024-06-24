package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
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
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import kotlin.random.Random

@Suppress("UnstableApiUsage")
object DamageGenes {

    // Canceling

    fun handleNoFallDamage(event: LivingIncomingDamageEvent) {
        if (!ModGenes.noFallDamage.isActive) return

        if (event.source == DamageTypes.FALL) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.noFallDamage) && !entity.hasGene(ModGenes.flight)) return

        event.amount = 0f
    }

    fun handleWitherProof(event: LivingIncomingDamageEvent) {
        if (!ModGenes.witherProof.isActive) return

        if (event.source != DamageTypes.WITHER) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.witherProof)) return

        entity.removeEffect(MobEffects.WITHER)
        event.amount = 0f
    }

    fun handleFireProof(event: LivingIncomingDamageEvent) {
        if (!ModGenes.fireProof.isActive) return

        if (event.source != DamageTypes.IN_FIRE && event.source != DamageTypes.ON_FIRE) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.fireProof)) return

        entity.clearFire()
        event.amount = 0f
    }

    fun handlePoisonProof(event: LivingIncomingDamageEvent) {
        if (!ModGenes.poisonImmunity.isActive) return

        if (event.source != DamageTypes.MAGIC && event.source != DamageTypes.INDIRECT_MAGIC) return

        if (!event.entity.hasEffect(MobEffects.POISON)) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.poisonImmunity)) return

        entity.removeEffect(MobEffects.POISON)
        event.amount = 0f
    }

    // Changing amount (not just canceling)

    fun handleDragonHealth(event: LivingDamageEvent.Pre) {
        if (!ModGenes.enderDragonHealth.isActive) return

        if (event.container.newDamage == 0f) return
        val entity = event.entity

        if (entity.level().isClientSide) return

        if (!entity.hasGene(ModGenes.enderDragonHealth)) return

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
        if (!ModGenes.johnny.isActive) return

        val attacker = event.container.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.johnny)) return

        val weaponIsAxe = attacker.mainHandItem.item is AxeItem //Is there a better way of doing this?
        if (weaponIsAxe) return

        event.container.newDamage *= ServerConfig.johnnyAttackMultiplier.get().toFloat()
    }

    // Triggers

    fun handleWitherHit(event: LivingDamageEvent.Post) {
        if (!ModGenes.witherHit.isActive) return

        // Makes it not proc if it's an arrow or whatever
        if (!event.source.isDirect) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.witherHit)) return

        val witherEffect = MobEffectInstance(
            MobEffects.WITHER,
            100
        )


        if (!CommonHooks.canMobEffectBeApplied(event.entity, witherEffect)) return

        event.entity.addEffect(witherEffect)
    }

    fun handleThorns(event: LivingDamageEvent.Post) {
        if (!ModGenes.thorns.isActive) return

        val attacker = event.source.entity as? LivingEntity ?: return

        val target = event.entity as? Mob ?: event.entity as? Player ?: return
        if (target == attacker) return

        val chestPlate = target.getItemBySlot(EquipmentSlot.CHEST)
        val targetChestplateMissingOrLeather = chestPlate.isEmpty || chestPlate.`is`(Items.LEATHER_CHESTPLATE)
        if (!targetChestplateMissingOrLeather) return

        if (!target.hasGene(ModGenes.thorns)) return

        if (Random.nextDouble() > ServerConfig.thornsChance.get()) return

        val thornsDamageSource = target.level().damageSources().thorns(target)
        attacker.hurt(thornsDamageSource, ServerConfig.thornsDamage.get().toFloat())

        if (target is Player) {
            target.causeFoodExhaustion(ServerConfig.thornsHungerDrain.get().toFloat())
        }
    }

    fun handleClaws(event: LivingDamageEvent.Post) {
        if (!ModGenes.claws.isActive) return

        val attacker = event.source.entity as? LivingEntity ?: return

        if (!attacker.mainHandItem.isEmpty) return

        val clawsLevel: Int = if (ModGenes.clawsTwo.isActive && attacker.hasGene(ModGenes.clawsTwo)) {
            2
        } else if (attacker.hasGene(ModGenes.claws)) {
            1
        } else {
            return
        }


        val chanceOfHappening = ServerConfig.clawsChance.get() * clawsLevel

        if (Random.nextDouble() > chanceOfHappening) return

        //TODO
//        event.entity.addEffect(
//            MobEffectInstance(
//                ModEffects.BLEED,
//                6000,
//                0,
//                false,
//                true
//            )
//        )
    }

    fun handleChilling(event: LivingDamageEvent.Post) {
        if (!ModGenes.chilling.isActive) return

        if (!event.source.isDirect) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.chilling)) return

        if (Random.nextDouble() > ServerConfig.chillChance.get()) return

        val target = event.entity
        target.ticksFrozen = ServerConfig.chillDuration.get()
    }

}