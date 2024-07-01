package dev.aaronhowser.mods.geneticsresequenced.enchantment

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level

object ModEnchantments {

    val delicateTouchResourceKey: ResourceKey<Enchantment> =
        ResourceKey.create(Registries.ENCHANTMENT, OtherUtil.modResource("delicate_touch"))

    fun getDelicateTouchHolder(level: Level): Holder<Enchantment> = level.registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(delicateTouchResourceKey)

}