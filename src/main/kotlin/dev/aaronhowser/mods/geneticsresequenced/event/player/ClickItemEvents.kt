package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.projectile.SmallFireball
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import kotlin.random.Random

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object ClickItemEvents {

    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
        if (event.side.isClient) return
        val player = event.entity
        val genes = player.getGenes() ?: return

        if (genes.hasGene(EnumGenes.MILKY)) milkyItem(event)
        if (genes.hasGene(EnumGenes.SHOOT_FIREBALLS)) shootFireball(event)
    }

    private fun shootFireball(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity

        if (!player.isCrouching) return
        if (!event.itemStack.`is`(ModTags.FIREBALL_TAG)) return

        val lookVec = player.lookAngle

        val fireball = SmallFireball(
            event.level,
            player,
            lookVec.x,
            lookVec.y,
            lookVec.z
        ).apply {
            setPos(x, player.eyeY, z)
        }

        event.level.addFreshEntity(fireball)

        event.level.playSound(
            null,
            player,
            SoundEvents.BLAZE_SHOOT,
            SoundSource.PLAYERS,
            1.0f,
            1.0f
        )

        event.itemStack.shrink(1)
    }

    private fun milkyItem(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity

        if (!player.isCrouching) return

        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        event.itemStack.shrink(1)
        player.addItem(ItemStack(Items.MILK_BUCKET))

        val sound = if (Random.nextFloat() < 0.05f) {
            SoundEvents.GOAT_SCREAMING_MILK
        } else {
            if (Random.nextBoolean()) SoundEvents.COW_MILK else SoundEvents.GOAT_MILK
        }

        event.level.playSound(
            null,
            player,
            sound,
            SoundSource.PLAYERS,
            1.0f,
            1.0f
        )
    }

}