package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.enchantments.DelicateTouchEnchantment
import dev.aaronhowser.mods.geneticsresequenced.items.ScraperItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ModEnchantments {

    val ENCHANTMENT_REGISTRY: DeferredRegister<Enchantment> =
        DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, GeneticsResequenced.ID)

    val ScraperEnchantmentCategory: EnchantmentCategory =
        EnchantmentCategory.create("scraper_enchantments") { item: Item -> item is ScraperItem }

    val DELICATE_TOUCH by ENCHANTMENT_REGISTRY.registerObject("delicate_touch") { DelicateTouchEnchantment }

}