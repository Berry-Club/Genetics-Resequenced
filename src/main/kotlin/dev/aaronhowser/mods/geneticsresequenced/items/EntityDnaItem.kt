package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

open class EntityDnaItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
) {

    companion object {
        const val MOB_ID_NBT = "MobId"

        fun hasMob(itemStack: ItemStack): Boolean = itemStack.tag?.contains(MOB_ID_NBT) ?: false

        fun setMob(itemStack: ItemStack, entityType: EntityType<*>): Boolean {
            if (itemStack.item !is EntityDnaItem) {
                return false
            }
            val mobRL = ForgeRegistries.ENTITY_TYPES.getKey(entityType)
            if (mobRL == null) {
                GeneticsResequenced.LOGGER.error("Failed to get mob id for $entityType")
                return false
            }

            return setMob(itemStack, mobRL)
        }

        fun setMob(itemStack: ItemStack, entityRL: ResourceLocation): Boolean {
            val tag = itemStack.orCreateTag
            tag.putString(MOB_ID_NBT, entityRL.toString())
            return true
        }

        fun ItemStack.setMob(entityType: EntityType<*>): ItemStack? {
            return if (setMob(this, entityType)) {
                this
            } else {
                null
            }
        }

        fun ItemStack.setMob(entityRL: ResourceLocation): ItemStack? {
            return if (setMob(this, entityRL)) {
                this
            } else {
                null
            }
        }

        fun getEntityType(itemStack: ItemStack): EntityType<*>? {
            if (!hasMob(itemStack)) return null
            val string = itemStack.tag?.getString(MOB_ID_NBT) ?: return null
            val resourceLocation = ResourceLocation.tryParse(string) ?: return null

            return ForgeRegistries.ENTITY_TYPES.getValue(resourceLocation)
        }
    }

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {

        if (pPlayer.isCreative) {

            val newStack = pStack.copy().setMob(pInteractionTarget.type)
            return if (newStack != null) {
                pPlayer.setItemInHand(pUsedHand, newStack)
                InteractionResult.SUCCESS
            } else {
                InteractionResult.FAIL
            }

        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand)
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {

        val entityType = getEntityType(pStack)
        if (entityType != null) {
            val component =
                Component
                    .translatable("tooltip.geneticsresequenced.dna_item.filled")
                    .append(entityType.getDescription())
                    .withColor(ChatFormatting.GRAY)

            pTooltipComponents.add(component)
        } else {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.dna_item.empty")
                    .withColor(ChatFormatting.GRAY)
            )
        }

        try {
            val isCreative = pLevel?.isClientSide == true && ClientUtil.playerIsCreative()
            if (isCreative) {
                val component =
                    Component
                        .translatable("tooltip.geneticsresequenced.dna_item.creative")
                        .withColor(ChatFormatting.GRAY)

                pTooltipComponents.add(component)
            }
        } catch (e: Exception) {
            GeneticsResequenced.LOGGER.error("EntityDnaItem isCreative check failed", e)
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

}