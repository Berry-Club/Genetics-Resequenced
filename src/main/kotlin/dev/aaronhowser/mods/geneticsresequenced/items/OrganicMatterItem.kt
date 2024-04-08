package dev.aaronhowser.mods.geneticsresequenced.items

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

object OrganicMatterItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
) {

    private const val MOB_ID_NBT = "MobId"
    private const val MOB_TRANSLATION_KEY_NBT = "MobTranslationKey"

    fun setMobId(itemStack: ItemStack, mobId: String) {
        val tag = itemStack.orCreateTag
        tag.putString(MOB_ID_NBT, mobId)
    }

    private fun getMobId(itemStack: ItemStack): String {
        return itemStack.tag?.getString("MobId") ?: ""
    }

    fun setMobTranslationKey(itemStack: ItemStack, mobTranslationKey: String) {
        val tag = itemStack.orCreateTag
        tag.putString(MOB_TRANSLATION_KEY_NBT, mobTranslationKey)
    }

    private fun getMobTranslationKey(itemStack: ItemStack): String {
        return itemStack.tag?.getString("MobTranslationKey") ?: ""
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        val line1 = Component.literal("Mob ID: ${getMobId(pStack)}")
        val line2 = Component.literal("Mob: ").append(Component.translatable(getMobTranslationKey(pStack)))

        pTooltipComponents.add(line1)
        pTooltipComponents.add(line2)

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

}