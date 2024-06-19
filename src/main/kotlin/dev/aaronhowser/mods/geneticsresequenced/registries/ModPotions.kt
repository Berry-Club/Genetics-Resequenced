package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setBasic
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.*
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.brewing.BrewingRecipe
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.common.brewing.IBrewingRecipe
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

@Suppress("MemberVisibilityCanBePrivate")
object ModPotions {

    val POTION_REGISTRY: DeferredRegister<Potion> =
        DeferredRegister.create(ForgeRegistries.POTIONS, GeneticsResequenced.ID)

    //TODO: Remove splash, lingering, and arrow potions (apparently needs mixins >:( ) Maybe just tooltip them instead? Add a recipe back to regular potion?

    val SUBSTRATE
            by register("substrate") {
                Potion(addId("substrate"), MobEffectInstance(ModEffects.SUBSTRATE))
            }
    val CELL_GROWTH
            by register("cell_growth") {
                Potion(addId("cell_growth"), MobEffectInstance(ModEffects.CELL_GROWTH))
            }
    val MUTATION
            by register("mutation") {
                Potion(addId("mutation"), MobEffectInstance(ModEffects.MUTATION))
            }
    val VIRAL_AGENTS
            by register("viral_agents") {
                Potion(addId("viral_agents"), MobEffectInstance(ModEffects.VIRAL_AGENTS))
            }
    val THE_CURE
            by register("the_cure") {
                Potion(addId("the_cure"), MobEffectInstance(ModEffects.THE_CURE))
            }

    val ZOMBIFY_VILLAGER
            by register("zombify_villager") {
                Potion(
                    addId("zombify_villager"),
                    MobEffectInstance(ModEffects.ZOMBIFY_VILLAGER)
                )
            }

    private fun register(
        name: String,
        supplier: () -> Potion
    ): ObjectHolderDelegate<Potion> {
        val potion = POTION_REGISTRY.registerObject(name, supplier)
        return potion
    }

    private fun addId(name: String): String {
        return GeneticsResequenced.ID + "." + name
    }
}