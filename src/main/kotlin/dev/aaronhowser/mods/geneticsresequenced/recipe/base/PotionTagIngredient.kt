package dev.aaronhowser.mods.geneticsresequenced.recipe.base

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.registry.ModIngredientTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import java.util.stream.Stream

class PotionTagIngredient(
    val potionTag: TagKey<Potion>
) : ICustomIngredient {

    override fun test(stack: ItemStack): Boolean {
        if (stack.item != Items.POTION) return false

        val potion = OtherUtil.getPotion(stack) ?: return false
        return potion.`is`(potionTag)
    }

    override fun getItems(): Stream<ItemStack> {
        val allPotions = BuiltInRegistries.POTION.holders()
        val validPotions = allPotions.filter { test(OtherUtil.getPotionStack(it)) }

        return validPotions.map { OtherUtil.getPotionStack(it) }
    }

    override fun isSimple(): Boolean {
        return false
    }

    override fun getType(): IngredientType<*> {
        return ModIngredientTypes.POTION_TAG.get()
    }

    companion object {
        val CODEC: MapCodec<PotionTagIngredient> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    TagKey.codec(Registries.POTION)
                        .fieldOf("potion_tag")
                        .forGetter(PotionTagIngredient::potionTag)
                ).apply(instance, ::PotionTagIngredient)
            }
    }

}