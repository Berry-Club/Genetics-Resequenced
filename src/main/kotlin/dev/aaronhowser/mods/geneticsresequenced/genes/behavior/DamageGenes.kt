package dev.aaronhowser.mods.geneticsresequenced.genes.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DragonHealthCrystal
import dev.aaronhowser.mods.geneticsresequenced.registries.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.IndirectEntityDamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraftforge.common.Tags
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import kotlin.random.Random

object DamageGenes {

    fun handleNoFallDamage(event: LivingDamageEvent) {
        if (!ModGenes.noFallDamage.isActive) return

        if (event.source != DamageSource.FALL) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.noFallDamage) && !entity.hasGene(ModGenes.flight)) return

        event.isCanceled = true
    }

    fun handleWitherProof(event: LivingDamageEvent) {
        if (!ModGenes.witherProof.isActive) return

        if (event.source != DamageSource.WITHER) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.witherProof)) return

        entity.removeEffect(MobEffects.WITHER)
        event.isCanceled = true
    }

    fun handleWitherHit(event: LivingHurtEvent) {
        if (!ModGenes.witherHit.isActive) return

        // Makes it not proc if it's an arrow or whatever
        if (event.source is IndirectEntityDamageSource) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.witherHit)) return

        val witherEffect = MobEffectInstance(
            MobEffects.WITHER,
            100
        )

        if (!event.entity.canBeAffected(witherEffect)) return

        event.entity.addEffect(witherEffect)
    }

    fun handleFireProof(event: LivingDamageEvent) {
        if (!ModGenes.fireProof.isActive) return

        val source = event.source

        if (!source.isFire) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.fireProof)) return

        entity.clearFire()
        event.isCanceled = true
    }

    fun handlePoisonProof(event: LivingDamageEvent) {
        if (!ModGenes.poisonImmunity.isActive) return

        val source = event.source

        if (!source.isMagic) return
        if (!event.entity.hasEffect(MobEffects.POISON)) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.poisonImmunity)) return

        entity.removeEffect(MobEffects.POISON)
        event.isCanceled = true
    }

    fun handleThorns(event: LivingHurtEvent) {
        if (!ModGenes.thorns.isActive) return

        val attacker = event.source.entity as? LivingEntity ?: return

        val target = event.entity as? Mob ?: event.entity as? Player ?: return
        if (target == attacker) return

        val chestPlate = target.getItemBySlot(EquipmentSlot.CHEST)
        val targetChestplateMissingOrLeather = chestPlate.isEmpty || chestPlate.`is`(Items.LEATHER_CHESTPLATE)
        if (!targetChestplateMissingOrLeather) return

        if (!target.hasGene(ModGenes.thorns)) return

        if (Random.nextDouble() > ServerConfig.thornsChance.get()) return

        attacker.hurt(DamageSource.thorns(target), ServerConfig.thornsDamage.get().toFloat())

        if (target is Player) {
            target.causeFoodExhaustion(ServerConfig.thornsHungerDrain.get().toFloat())
        }
    }

    fun handleClaws(event: LivingHurtEvent) {
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

        event.entity.addEffect(
            MobEffectInstance(
                ModEffects.BLEED,
                6000,
                0,
                false,
                true
            )
        )
    }

    fun handleDragonHealth(event: LivingDamageEvent) {
        if (!ModGenes.enderDragonHealth.isActive) return

        if (event.isCanceled) return
        val entity = event.entity

        if (entity.level.isClientSide) return

        if (!entity.hasGene(ModGenes.enderDragonHealth)) return

        val items = entity.handSlots.toMutableList()
        if (entity is Player) items += entity.inventory.items

        val healthCrystal = items.find { it.item == ModItems.DRAGON_HEALTH_CRYSTAL } ?: return

        val amountDamaged = event.amount.toInt()
        val crystalDurabilityRemaining = healthCrystal.maxDamage - healthCrystal.damageValue
        val amountToBlock = minOf(amountDamaged, crystalDurabilityRemaining)

        healthCrystal.hurtAndBreak(amountToBlock, entity) {
            DragonHealthCrystal.playBreakSound(it.level, it.x, it.y, it.z)
        }

        event.amount -= amountToBlock
        if (event.amount == 0f) event.isCanceled = true
    }

    fun handleJohnny(event: LivingHurtEvent) {
        if (!ModGenes.johnny.isActive) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.johnny)) return

        val weapon = attacker.mainHandItem
        if (!weapon.`is`(Tags.Items.TOOLS_AXES)) return

        event.amount *= ServerConfig.johnnyAttackMultiplier.get().toFloat()
    }

    fun handleChilling(event: LivingHurtEvent) {
        if (!ModGenes.chilling.isActive) return

        if (event.source is IndirectEntityDamageSource) return

        val attacker = event.source.entity as? LivingEntity ?: return
        if (!attacker.hasGene(ModGenes.chilling)) return

        if (Random.nextDouble() > ServerConfig.chillChance.get()) return

        val target = event.entity
        target.ticksFrozen = ServerConfig.chillDuration.get()
    }

}