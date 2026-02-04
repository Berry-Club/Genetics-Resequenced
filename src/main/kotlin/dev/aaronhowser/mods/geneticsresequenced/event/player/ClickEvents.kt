package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.ClickGenes
import net.minecraft.world.InteractionHand
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(
	modid = GeneticsResequenced.ID
)
object ClickEvents {

	@SubscribeEvent
	fun onInteractEntity(event: PlayerInteractEvent.EntityInteract) {
		ClickGenes.handleWoolyOther(event)
		ClickGenes.handleMilkyOther(event)
		ClickGenes.handleMeatyOther(event)

		checkShouldCancel(event)
	}

	private fun checkShouldCancel(event: PlayerInteractEvent.EntityInteract) {
		val entity = event.target
		if (!entity.isEntity(ModEntityTypeTagsProvider.ALLOWS_PREVENTING_INTERACTION)) return

		val mainHandStack = event.entity.getItemInHand(InteractionHand.MAIN_HAND)
		val offHandStack = event.entity.getItemInHand(InteractionHand.OFF_HAND)

		if (
			mainHandStack.isItem(ModItemTagsProvider.PREVENTS_SOME_MOB_INTERACTION)
			|| offHandStack.isItem(ModItemTagsProvider.PREVENTS_SOME_MOB_INTERACTION)
		) {
			event.isCanceled = true
		}
	}

	@SubscribeEvent
	fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
		ClickGenes.handleWoolySelf(event)
		ClickGenes.handleMilkySelf(event)
		ClickGenes.handleMeatySelf(event)
		ClickGenes.shootFireball(event)
	}

	@SubscribeEvent
	fun onDigSpeed(event: PlayerEvent.BreakSpeed) {
		AttributeGenes.handleEfficiency(event)
	}

	@SubscribeEvent
	fun onClickBlock(event: PlayerInteractEvent.RightClickBlock) {
		SupportSlime.spawnEggMessage(event)
	}

	@SubscribeEvent
	fun onInteractWithBlock(event: PlayerInteractEvent.RightClickBlock) {
		ClickGenes.eatGrass(event)
		ClickGenes.cureCringe(event)
	}

	@SubscribeEvent
	fun onGetProjectile(event: LivingGetProjectileEvent) {
		ClickGenes.handleInfinityGetProjectile(event)
	}

	@SubscribeEvent
	fun onProjectileAdded(event: EntityJoinLevelEvent) {
		ClickGenes.handleInfinityArrow(event)
	}

}