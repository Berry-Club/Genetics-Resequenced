package dev.aaronhowser.mods.geneticsresequenced.potions

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setBasic
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.CellGrowthRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.SetCellGrowthEntityRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.SubstrateCellRecipe
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

        if (itemPotion in modPotions && stack.item != Items.POTION) {
            event.toolTip.add(
                Component.translatable("tooltip.geneticsresequenced.potion.ignore")
                    .withStyle { it.withColor(ChatFormatting.RED) }
            )
        }

        val itemGene = stack.getGene()
        val itemEntity = EntityDnaItem.getEntityType(stack)

        if (itemGene != null) {
            event.toolTip.add(
                itemGene.nameComponent
            )
        }

        if (itemEntity != null) {
            event.toolTip.add(
                itemEntity.description
            )
        }

    }

    private val Potion.ingredient: Ingredient
        get() = Ingredient.of(PotionUtils.setPotion(ItemStack(Items.POTION), this))

    private val ItemStack.ingredient: Ingredient
        get() = Ingredient.of(this)

    private val Item.ingredient: Ingredient
        get() = Ingredient.of(ItemStack(this))

    fun addRecipes() {

        fun potionStack(potion: Potion): ItemStack {
            return PotionUtils.setPotion(ItemStack(Items.POTION), potion)
        }


        // Basic recipes
        val substratePotion = potionStack(SUBSTRATE)
        val cellGrowthPotion = potionStack(CELL_GROWTH)
        val mutationPotion = potionStack(MUTATION)
        val viralAgentsPotion = potionStack(VIRAL_AGENTS)

        val substrateRecipe = BrewingRecipe(
            Potions.MUNDANE.ingredient,
            ModItems.ORGANIC_MATTER.get().ingredient,
            substratePotion
        )
        val cellGrowthRecipe = BrewingRecipe(
            SUBSTRATE.ingredient,
            ModItems.DNA_HELIX.get().defaultInstance.setBasic().ingredient,
            cellGrowthPotion
        )
        val mutationRecipe = BrewingRecipe(
            CELL_GROWTH.ingredient,
            Items.FERMENTED_SPIDER_EYE.ingredient,
            mutationPotion
        )
        val viralRecipe = BrewingRecipe(
            MUTATION.ingredient,
            Items.CHORUS_FRUIT.ingredient,
            viralAgentsPotion
        )

        BrewingRecipeRegistry.addRecipe(substrateRecipe)
        BrewingRecipeRegistry.addRecipe(cellGrowthRecipe)
        BrewingRecipeRegistry.addRecipe(mutationRecipe)
        BrewingRecipeRegistry.addRecipe(viralRecipe)

        val substrateDuplicationRecipe = SubstrateCellRecipe()
        BrewingRecipeRegistry.addRecipe(substrateDuplicationRecipe)

        val setPcgEntityRecipe = SetCellGrowthEntityRecipe()
        BrewingRecipeRegistry.addRecipe(setPcgEntityRecipe)


        val geneFocusBrews = listOf(
            CellGrowthRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, DefaultGenes.BIOLUMINESCENCE, 0.5f),
            CellGrowthRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, DefaultGenes.BIOLUMINESCENCE, 0.5f),
            CellGrowthRecipe(EntityType.VILLAGER, Items.EMERALD, DefaultGenes.EMERALD_HEART, 0.5f),
            CellGrowthRecipe(EntityType.SHULKER, Items.EMERALD, DefaultGenes.KEEP_INVENTORY, 0.5f),
            CellGrowthRecipe(EntityType.RABBIT, Items.REDSTONE, DefaultGenes.SPEED, 0.5f),
            CellGrowthRecipe(EntityType.IRON_GOLEM, Items.APPLE, DefaultGenes.REGENERATION, 0.5f),
            CellGrowthRecipe(EntityType.CHICKEN, Items.EGG, DefaultGenes.LAY_EGG, 0.5f),
            CellGrowthRecipe(EntityType.PIG, Items.PORKCHOP, DefaultGenes.MEATY, 0.5f),
            CellGrowthRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, DefaultGenes.TELEPORT, 0.5f),
            CellGrowthRecipe(EntityType.ENDERMAN, Items.IRON_INGOT, DefaultGenes.ITEM_MAGNET, 0.5f),
            CellGrowthRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, DefaultGenes.MORE_HEARTS, 0.5f),
            CellGrowthRecipe(EntityType.SLIME, Items.GLOWSTONE_DUST, DefaultGenes.PHOTOSYNTHESIS, 0.5f)
        )
        for (brew in geneFocusBrews) {
            BrewingRecipeRegistry.addRecipe(brew)
        }

//        fun mutationBrew(item: Item, entityType: EntityType<*>, gene: Gene): ComplexBrewingRecipe {
//            return ComplexBrewingRecipe(
//                MUTATION,
//                item,
//                entityType,
//                gene
//            )
//        }
//
//        val mutationBrews = listOf(
//            mutationBrew(Items.FEATHER, EntityType.BAT, DefaultGenes.FLIGHT),
//            mutationBrew(Items.FEATHER, EntityType.PARROT, DefaultGenes.FLIGHT),
//
//            mutationBrew(Items.EMERALD, EntityType.POLAR_BEAR, DefaultGenes.STRENGTH),
//            mutationBrew(Items.EMERALD, EntityType.LLAMA, DefaultGenes.STRENGTH),
//            mutationBrew(Items.EMERALD, EntityType.RABBIT, DefaultGenes.LUCK),
//
//            mutationBrew(Items.DIAMOND, EntityType.SHULKER, DefaultGenes.RESISTANCE),
//            mutationBrew(Items.DIAMOND, EntityType.ZOMBIE, DefaultGenes.RESISTANCE),
//            mutationBrew(Items.DIAMOND, EntityType.POLAR_BEAR, DefaultGenes.CLAWS),
//            mutationBrew(Items.DIAMOND, EntityType.LLAMA, DefaultGenes.CLAWS),
//            mutationBrew(Items.DIAMOND, EntityType.WOLF, DefaultGenes.CLAWS),
//
//            mutationBrew(Items.REDSTONE, EntityType.RABBIT, DefaultGenes.SPEED_2),
//            mutationBrew(Items.REDSTONE, EntityType.OCELOT, DefaultGenes.SPEED_4),
//
//            mutationBrew(Items.IRON_INGOT, EntityType.RABBIT, DefaultGenes.HASTE),
//
//            mutationBrew(Items.REDSTONE, EntityType.SILVERFISH, DefaultGenes.EFFICIENCY),
//
//            mutationBrew(Items.SPIDER_EYE, EntityType.ZOMBIE, DefaultGenes.SCARE_CREEPERS),
//            mutationBrew(Items.SPIDER_EYE, EntityType.SPIDER, DefaultGenes.SCARE_SKELETONS),
//
//            mutationBrew(Items.REDSTONE, EntityType.ENDER_DRAGON, DefaultGenes.REGENERATION),
//
//            mutationBrew(Items.BLAZE_POWDER, EntityType.PIG, DefaultGenes.MEATY),
//
//            mutationBrew(Items.ENDER_EYE, EntityType.BAT, DefaultGenes.INVISIBLE),
//            mutationBrew(Items.ENDER_EYE, EntityType.SKELETON, DefaultGenes.INVISIBLE),
//
//            mutationBrew(Items.GOLDEN_APPLE, EntityType.ENDERMAN, DefaultGenes.MORE_HEARTS)
//        )
//        for (brew in mutationBrews) {
////            BrewingRecipeRegistry.addRecipe(brew)
//        }

    }

}