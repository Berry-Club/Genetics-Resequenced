package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.api.capability.CapabilityHandler
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.commands.ModCommands
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.entity.animal.MushroomCow
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.entity.animal.goat.Goat
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import kotlin.random.Random

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object ModEvents {

    @SubscribeEvent
    fun onAttachCapability(event: AttachCapabilitiesEvent<Entity>) {
        val entity = event.`object`

        if (entity !is LivingEntity) return

        event.addCapability(CapabilityHandler.GENE_CAPABILITY_RL, GenesCapabilityProvider)
    }

    @SubscribeEvent
    fun onPlayerCloned(event: PlayerEvent.Clone) {
        if (!event.isWasDeath) return

        event.original.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { oldGenes ->
            event.original.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { newGenes ->
                newGenes.setGeneList(oldGenes.getGeneList())
            }
        }
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        handleKeepGenesOnDeath(event)
    }

    private fun handleKeepGenesOnDeath(event: PlayerRespawnEvent) {
        if (ServerConfig.keepGenesOnDeath.get()) return

        val player = event.entity
        val playerGenes = player.getGenes() ?: return

        if (playerGenes.getGeneList().isEmpty()) return

        val component = Component
            .literal("Genetics Resequenced")
            .withStyle {
                it
                    .withColor(ChatFormatting.DARK_PURPLE)
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, Component
                                .literal(playerGenes.getGeneList().joinToString(", "))
                        )
                    )
            }
            .append(
                Component
                    .literal(": Your genes have been removed on death.")
                    .withStyle {
                        it
                            .withColor(ChatFormatting.GRAY)
                    }
            )
        player.sendSystemMessage(component)

        playerGenes.removeAllGenes()
    }

    @SubscribeEvent
    fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
        ModCommands.register(event.dispatcher)
    }

    @SubscribeEvent
    fun onInteractEntity(event: PlayerInteractEvent.EntityInteract) {

        if (event.side.isClient) return

        val target = event.target
        if (target !is LivingEntity) return

        val targetGenes = target.getGenes() ?: return

        if (targetGenes.hasGene(EnumGenes.WOOLY)) wooly(event)
        if (targetGenes.hasGene(EnumGenes.MILKY)) milky(event)
        if (targetGenes.hasGene(EnumGenes.MEATY)) meaty(event)
    }

    //TODO: Make these into recipes? Would be nice for packs or whatever
    private fun wooly(event: PlayerInteractEvent.EntityInteract) {

        when (event.target) {
            is Sheep, is MushroomCow -> return
        }

        val clickedWithShears = event.itemStack.`is`(ModTags.SHEARS_TAG)
        if (!clickedWithShears) return

        val woolItemStack = ItemStack(Blocks.WHITE_WOOL)

        val woolEntity = ItemEntity(
            event.level,
            event.target.eyePosition.x,
            event.target.eyePosition.y,
            event.target.eyePosition.z,
            woolItemStack
        )
        event.level.addFreshEntity(woolEntity)
        woolEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
        )

        event.itemStack.hurtAndBreak(1, event.entity) { }

        event.level.playSound(
            null,
            event.target,
            SoundEvents.SHEEP_SHEAR,
            event.target.soundSource,
            1.0f,
            1.0f
        )
    }

    private fun meaty(event: PlayerInteractEvent.EntityInteract) {
        val clickedWithShears = event.itemStack.`is`(ModTags.SHEARS_TAG)
        if (!clickedWithShears) return

        val porkItemStack = ItemStack(Items.PORKCHOP)

        val porkEntity = ItemEntity(
            event.level,
            event.target.eyePosition.x,
            event.target.eyePosition.y,
            event.target.eyePosition.z,
            porkItemStack
        )
        event.level.addFreshEntity(porkEntity)
        porkEntity.setDeltaMovement(
            Random.nextDouble(-0.05, 0.05),
            Random.nextDouble(0.05, 0.1),
            Random.nextDouble(-0.05, 0.05)
        )

        event.itemStack.hurtAndBreak(1, event.entity) { }

        event.level.playSound(
            null,
            event.target,
            SoundEvents.SHEEP_SHEAR,
            event.target.soundSource,
            1.0f,
            1.0f
        )
    }

    //TODO: Let this also be done by sneak right-clicking with a bucket if you have the gene
    private fun milky(event: PlayerInteractEvent.EntityInteract) {

        when (event.target) {
            is Cow, is Goat -> return
        }

        val clickedWithBucket = event.itemStack.`is`(Items.BUCKET)
        if (!clickedWithBucket) return

        event.itemStack.shrink(1)
        event.entity.addItem(ItemStack(Items.MILK_BUCKET))

        val sound = if (event.target is Player && Random.nextFloat() < 0.05f) {
            SoundEvents.GOAT_SCREAMING_MILK
        } else {
            if (Random.nextBoolean()) SoundEvents.COW_MILK else SoundEvents.GOAT_MILK
        }

        event.level.playSound(
            null,
            event.target,
            sound,
            event.target.soundSource,
            1.0f,
            1.0f
        )
    }

    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
        if (event.side.isClient) return
        val player = event.entity
        val genes = player.getGenes() ?: return

        if (genes.hasGene(EnumGenes.MILKY)) milkyItem(event)

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

    @SubscribeEvent
    fun onInteractWithBlock(event: PlayerInteractEvent.RightClickBlock) {
        if (event.side.isClient) return
        val player = event.entity

        val genes = player.getGenes() ?: return

        if (genes.hasGene(EnumGenes.EAT_GRASS)) eatGrass(event)
    }

    private fun eatGrass(event: PlayerInteractEvent.RightClickBlock) {

        if (!event.itemStack.isEmpty) return

        val player = event.entity

        val isHungry = player.foodData.foodLevel < 20
        if (!isHungry) return

        val block = event.level.getBlockState(event.pos).block

        val blockAfter = when (block) {
            Blocks.GRASS_BLOCK, Blocks.MYCELIUM -> Blocks.DIRT
            Blocks.WARPED_NYLIUM, Blocks.CRIMSON_NYLIUM -> Blocks.NETHERRACK
            else -> return
        }

        event.level.setBlockAndUpdate(event.pos, blockAfter.defaultBlockState())
        player.foodData.eat(1, 0.1f)

        event.level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.PLAYER_BURP,
            SoundSource.PLAYERS,
            1.0f,
            1.0f
        )

        event.level.playSound(
            null,
            event.pos,
            SoundEvents.GRASS_BREAK,
            SoundSource.BLOCKS,
            1.0f,
            1.0f
        )

    }

}