package dev.aaronhowser.mods.geneticsresequenced.potions

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.recipes.ComplexBrewingRecipe
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

        val substrateDuplicationRecipe = ComplexBrewingRecipe(
            SUBSTRATE,
            ModItems.CELL.get(),
            null,
            ModItems.CELL.get().defaultInstance
        )

        BrewingRecipeRegistry.addRecipe(substrateDuplicationRecipe)
        fun growthRecipe(item: Item, entityType: EntityType<*>, gene: Gene): ComplexBrewingRecipe {
            return ComplexBrewingRecipe(
                CELL_GROWTH,
                item,
                entityType,
                gene
            )
        }

        val geneFocusBrews = listOf(
            growthRecipe(Items.GLOWSTONE_DUST, EntityType.BLAZE, DefaultGenes.BIOLUMINESCENCE),
            growthRecipe(Items.GLOWSTONE_DUST, EntityType.MAGMA_CUBE, DefaultGenes.BIOLUMINESCENCE),

            growthRecipe(Items.EMERALD, EntityType.VILLAGER, DefaultGenes.EMERALD_HEART),
            growthRecipe(Items.EMERALD, EntityType.SHULKER, DefaultGenes.KEEP_INVENTORY),

            growthRecipe(Items.REDSTONE, EntityType.RABBIT, DefaultGenes.SPEED),
            growthRecipe(Items.APPLE, EntityType.IRON_GOLEM, DefaultGenes.REGENERATION),
            growthRecipe(Items.EGG, EntityType.CHICKEN, DefaultGenes.LAY_EGG),
            growthRecipe(Items.PORKCHOP, EntityType.PIG, DefaultGenes.MEATY),
            growthRecipe(Items.ENDER_PEARL, EntityType.ENDERMAN, DefaultGenes.TELEPORT),
            growthRecipe(Items.IRON_INGOT, EntityType.ENDERMAN, DefaultGenes.ITEM_MAGNET),
            growthRecipe(Items.GOLDEN_APPLE, EntityType.ENDERMAN, DefaultGenes.MORE_HEARTS),
            growthRecipe(Items.GLOWSTONE_DUST, EntityType.SLIME, DefaultGenes.PHOTOSYNTHESIS)
        )
        for (brew in geneFocusBrews) {
            BrewingRecipeRegistry.addRecipe(brew)
        }

        fun mutationBrew(item: Item, entityType: EntityType<*>, gene: Gene): ComplexBrewingRecipe {
            return ComplexBrewingRecipe(
                MUTATION,
                item,
                entityType,
                gene
            )
        }

        val mutationBrews = listOf(
            mutationBrew(Items.FEATHER, EntityType.BAT, DefaultGenes.FLIGHT),
            mutationBrew(Items.FEATHER, EntityType.PARROT, DefaultGenes.FLIGHT),

            mutationBrew(Items.EMERALD, EntityType.POLAR_BEAR, DefaultGenes.STRENGTH),
            mutationBrew(Items.EMERALD, EntityType.LLAMA, DefaultGenes.STRENGTH),
            mutationBrew(Items.EMERALD, EntityType.RABBIT, DefaultGenes.LUCK),

            mutationBrew(Items.DIAMOND, EntityType.SHULKER, DefaultGenes.RESISTANCE),
            mutationBrew(Items.DIAMOND, EntityType.ZOMBIE, DefaultGenes.RESISTANCE),
            mutationBrew(Items.DIAMOND, EntityType.POLAR_BEAR, DefaultGenes.CLAWS),
            mutationBrew(Items.DIAMOND, EntityType.LLAMA, DefaultGenes.CLAWS),
            mutationBrew(Items.DIAMOND, EntityType.WOLF, DefaultGenes.CLAWS),

            mutationBrew(Items.REDSTONE, EntityType.RABBIT, DefaultGenes.SPEED_2),
            mutationBrew(Items.REDSTONE, EntityType.OCELOT, DefaultGenes.SPEED_4),

            mutationBrew(Items.IRON_INGOT, EntityType.RABBIT, DefaultGenes.HASTE),

            mutationBrew(Items.REDSTONE, EntityType.SILVERFISH, DefaultGenes.EFFICIENCY),

            mutationBrew(Items.SPIDER_EYE, EntityType.ZOMBIE, DefaultGenes.SCARE_CREEPERS),
            mutationBrew(Items.SPIDER_EYE, EntityType.SPIDER, DefaultGenes.SCARE_SKELETONS),

            mutationBrew(Items.REDSTONE, EntityType.ENDER_DRAGON, DefaultGenes.REGENERATION),

            mutationBrew(Items.BLAZE_POWDER, EntityType.PIG, DefaultGenes.MEATY),

            mutationBrew(Items.ENDER_EYE, EntityType.BAT, DefaultGenes.INVISIBLE),
            mutationBrew(Items.ENDER_EYE, EntityType.SKELETON, DefaultGenes.INVISIBLE),

            mutationBrew(Items.GOLDEN_APPLE, EntityType.ENDERMAN, DefaultGenes.MORE_HEARTS)
        )
        for (brew in mutationBrews) {
            BrewingRecipeRegistry.addRecipe(brew)
        }

    }


}