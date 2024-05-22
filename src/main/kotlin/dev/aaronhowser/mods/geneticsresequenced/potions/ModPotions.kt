package dev.aaronhowser.mods.geneticsresequenced.potions

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.recipes.ComplexBrewingRecipe
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
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


        // Basic recipes
        val substratePotion = potionStack(SUBSTRATE)
        val cellGrowthPotion = potionStack(CELL_GROWTH)
        val mutationPotion = potionStack(MUTATION)
        val viralAgentsPotion = potionStack(VIRAL_AGENTS)

        val substrateRecipe = ComplexBrewingRecipe(
            Potions.MUNDANE,
            ModItems.ORGANIC_MATTER.get(),
            null,
            cellGrowthPotion
        )
        val cellGrowthRecipe = ComplexBrewingRecipe(
            SUBSTRATE,
            ModItems.DNA_HELIX.get(),
            DefaultGenes.BASIC,
            cellGrowthPotion
        )
        val mutationRecipe = ComplexBrewingRecipe(
            CELL_GROWTH,
            Items.FERMENTED_SPIDER_EYE,
            null,
            mutationPotion
        )
        val viralRecipe = ComplexBrewingRecipe(
            MUTATION,
            Items.CHORUS_FRUIT,
            null,
            viralAgentsPotion
        )

        BrewingRecipeRegistry.addRecipe(substrateRecipe)
        BrewingRecipeRegistry.addRecipe(cellGrowthRecipe)
        BrewingRecipeRegistry.addRecipe(mutationRecipe)
        BrewingRecipeRegistry.addRecipe(viralRecipe)

        //Substrate + Cell = More Cell
        val substrateDuplicationRecipe = ComplexBrewingRecipe(
            SUBSTRATE,
            ModItems.CELL.get(),
            null,
            ModItems.CELL.get().defaultInstance
        )

    }


}