package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneContainer.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.MushroomCow
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import kotlin.random.Random

object ClickGenes {

    val recentlySheered = GeneCooldown(
        ModGenes.wooly,
        ServerConfig.woolyCooldown.get()
    )

    fun handleWooly(event: PlayerInteractEvent.EntityInteract) {
        if (!ModGenes.wooly.isActive) return

        val target = event.target as? LivingEntity ?: return
        val clicker = event.entity

        if (target.level().isClientSide) return

        when (target) {
            is Sheep, is MushroomCow -> return
        }

        if (!target.hasGene(ModGenes.wooly)) return

        val clickedWithShears = event.itemStack.`is`(ModTags.WOOLY_ITEM_TAG)
        if (!clickedWithShears) return

        val newlySheared = recentlySheered.add(target)

        if (!newlySheared) {
            clicker.sendSystemMessage(Component.translatable("message.geneticsresequenced.wooly.recent"))
            return
        }

        val woolItemStack = ItemStack(Blocks.WHITE_WOOL)

        val woolEntity = ItemEntity(
            event.level,
            target.eyePosition.x,
            target.eyePosition.y,
            target.eyePosition.z,
            woolItemStack
        )
        event.level.addFreshEntity(woolEntity)
        woolEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
        )

        event.itemStack.hurtAndBreak(1, clicker, clicker.getEquipmentSlotForItem(event.itemStack))

        event.level.playSound(
            null,
            target,
            SoundEvents.SHEEP_SHEAR,
            target.soundSource,
            1.0f,
            1.0f
        )

        //TODO
//        if (target is ServerPlayer) {
//            ModPacketHandler.messagePlayer(target, ShearedPacket(removingSkin = true))
//        }

    }

}