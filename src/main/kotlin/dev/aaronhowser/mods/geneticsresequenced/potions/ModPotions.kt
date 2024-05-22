package dev.aaronhowser.mods.geneticsresequenced.potions

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setBasic
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects.ModEffects
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.brewing.BrewingRecipe
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

object ModPotions {

    val POTION_REGISTRY: DeferredRegister<Potion> =
        DeferredRegister.create(ForgeRegistries.POTIONS, GeneticsResequenced.ID)

    private fun addId(name: String): String {
        return GeneticsResequenced.ID + "." + name
    }

    //TODO: Remove splash, lingering, and arrow potions (apparently needs mixins >:( ) Maybe just tooltip them instead? Add a recipe back to regular potion?
    //TODO: Recipes
    val SUBSTRATE
            by register("substrate") { Potion(addId("substrate"), MobEffectInstance(ModEffects.SUBSTRATE)) }
    val CELL_GROWTH
            by register("cell_growth") { Potion(addId("cell_growth"), MobEffectInstance(ModEffects.CELL_GROWTH)) }
    val MUTATION
            by register("mutation") { Potion(addId("mutation"), MobEffectInstance(ModEffects.MUTATION)) }
    val VIRAL_AGENTS
            by register("viral_agents") { Potion(addId("viral_agents"), MobEffectInstance(ModEffects.VIRAL_AGENTS)) }
    val THE_CURE
            by register("the_cure") { Potion(addId("the_cure"), MobEffectInstance(ModEffects.THE_CURE)) }

    private fun register(
        name: String,
        supplier: () -> Potion
    ): ObjectHolderDelegate<Potion> {
        val potion = POTION_REGISTRY.registerObject(name, supplier)
        return potion
    }

    private val modPotions: List<Potion> by lazy {
        POTION_REGISTRY.entries.map { it.get() }
    }

    fun tooltip(event: ItemTooltipEvent) {
        val stack = event.itemStack

        val itemPotion = PotionUtils.getPotion(stack)
        if (itemPotion == Potions.EMPTY) return

        if (itemPotion !in modPotions) return

        if (stack.`is`(Items.POTION)) return

        event.toolTip.add(
            Component.translatable("tooltip.geneticsresequenced.potion.ignore")
                .withStyle { it.withColor(ChatFormatting.RED) }
        )
    }

    fun addRecipes() {

        fun potionStack(potion: Potion): ItemStack {
            return PotionUtils.setPotion(ItemStack(Items.POTION), potion)
        }

        val mundanePotion = potionStack(Potions.MUNDANE)
        val substratePotion = potionStack(SUBSTRATE)
        val cellGrowthPotion = potionStack(CELL_GROWTH)
        val mutationPotion = potionStack(MUTATION)
        val viralAgentsPotion = potionStack(VIRAL_AGENTS)

        val basicHelix = ItemStack(ModItems.DNA_HELIX.get()).setBasic()

        val substrateRecipe = BrewingRecipe(
            mundanePotion.ingredient,
            ModItems.ORGANIC_MATTER.get().ingredient,
            substratePotion
        )
        val cellGrowthRecipe = BrewingRecipe(
            substratePotion.ingredient,
            basicHelix.ingredient,
            cellGrowthPotion
        )
        val mutationRecipe = BrewingRecipe(
            cellGrowthPotion.ingredient,
            Items.FERMENTED_SPIDER_EYE.ingredient,
            mutationPotion
        )
        val viralRecipe = BrewingRecipe(
            mutationPotion.ingredient,
            Items.CHORUS_FRUIT.ingredient,
            viralAgentsPotion
        )

        BrewingRecipeRegistry.addRecipe(substrateRecipe)
        BrewingRecipeRegistry.addRecipe(cellGrowthRecipe)
        BrewingRecipeRegistry.addRecipe(mutationRecipe)
        BrewingRecipeRegistry.addRecipe(viralRecipe)

    }

    private val ItemStack.ingredient: Ingredient
        get() = Ingredient.of(this)
    private val Item.ingredient: Ingredient
        get() = Ingredient.of(this)

}