package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.ClickGenes
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.EventHooks
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID
)
object ClickEvents {

	@SubscribeEvent
	fun onInteractEntity(event: PlayerInteractEvent.EntityInteract) {
		ClickGenes.handleWoolyOther(event)
		ClickGenes.handleMilkyOther(event)
		ClickGenes.handleMeatyOther(event)

		val interactionResult = getItemInteractionOverrideResult(event, event.target) ?: return
		event.cancellationResult = interactionResult
		event.isCanceled = true
	}

	@SubscribeEvent
	fun onInteractEntitySpecific(event: PlayerInteractEvent.EntityInteractSpecific) {
		val interactionResult = getItemInteractionOverrideResult(event, event.target) ?: return
		event.cancellationResult = interactionResult
		event.isCanceled = true
	}

	private fun getItemInteractionOverrideResult(
		event: PlayerInteractEvent,
		interactionTarget: Entity
	): InteractionResult? {
		val livingTarget = interactionTarget as? LivingEntity ?: return null
		val originalItemStack = event.itemStack
		if (!originalItemStack.isItem(ModItemTagsProvider.OVERRIDES_ENTITY_INTERACTION)) return null

		val player = event.entity
		val itemStackCopy = originalItemStack.copy()
		val interactionStack = if (player.hasInfiniteMaterials()) itemStackCopy else originalItemStack
		val interactionResult = interactionStack.interactLivingEntity(player, livingTarget, event.hand)
		if (interactionResult == InteractionResult.PASS) return null

		if (interactionResult.consumesAction()) {
			player.level().gameEvent(
				GameEvent.ENTITY_INTERACT,
				livingTarget.position(),
				GameEvent.Context.of(player)
			)

			if (interactionStack.isEmpty && !player.hasInfiniteMaterials()) {
				EventHooks.onPlayerDestroyItem(player, itemStackCopy, event.hand)
				player.setItemInHand(event.hand, ItemStack.EMPTY)
			}
		}

		return interactionResult
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