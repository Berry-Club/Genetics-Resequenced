package dev.aaronhowser.mods.geneticsresequenced.potions

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setBasic
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.SetPotionEntityRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.SubstrateCellRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.VirusRecipe
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
                Component
                    .translatable("tooltip.geneticsresequenced.gene", itemGene.nameComponent)
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
        }

        if (itemEntity != null) {
            event.toolTip.add(
                Component
                    .translatable("tooltip.geneticsresequenced.dna_item.filled", itemEntity.description)
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
        }

    }

    private val Potion.ingredient: Ingredient
        get() = Ingredient.of(PotionUtils.setPotion(ItemStack(Items.POTION), this))

    private val ItemStack.ingredient: Ingredient
        get() = Ingredient.of(this)

    private val Item.ingredient: Ingredient
        get() = Ingredient.of(ItemStack(this))

    val allRecipes: MutableList<IBrewingRecipe> = mutableListOf()

    fun potionStack(potion: Potion): ItemStack {
        return PotionUtils.setPotion(ItemStack(Items.POTION), potion)
    }

    val substratePotionStack by lazy { potionStack(SUBSTRATE) }
    val cellGrowthPotionStack by lazy { potionStack(CELL_GROWTH) }
    val mutationPotionStack by lazy { potionStack(MUTATION) }
    val viralAgentsPotionStack by lazy { potionStack(VIRAL_AGENTS) }
    val curePotionStack by lazy { potionStack(THE_CURE) }

    fun addRecipes() {

        val substrateRecipe = BrewingRecipe(
            Potions.MUNDANE.ingredient,
            ModItems.ORGANIC_MATTER.get().ingredient,
            substratePotionStack
        )
        val cellGrowthRecipe = BrewingRecipe(
            SUBSTRATE.ingredient,
            ModItems.DNA_HELIX.get().defaultInstance.setBasic().ingredient,
            cellGrowthPotionStack
        )
        val mutationRecipe = BrewingRecipe(
            CELL_GROWTH.ingredient,
            Items.FERMENTED_SPIDER_EYE.ingredient,
            mutationPotionStack
        )
        val viralRecipe = BrewingRecipe(
            MUTATION.ingredient,
            Items.CHORUS_FRUIT.ingredient,
            viralAgentsPotionStack
        )
        val cureRecipe = BrewingRecipe(
            VIRAL_AGENTS.ingredient,
            ModItems.DNA_HELIX.get().defaultInstance.setGene(DefaultGenes.EMERALD_HEART).ingredient,
            curePotionStack
        )

        allRecipes.addAll(
            listOf(
                substrateRecipe,
                cellGrowthRecipe,
                mutationRecipe,
                viralRecipe,
                cureRecipe
            )
        )

        val substrateDuplicationRecipe = SubstrateCellRecipe()
        allRecipes.add(substrateDuplicationRecipe)

        val setPcgEntityRecipe = SetPotionEntityRecipe()
        allRecipes.add(setPcgEntityRecipe)


        val geneFocusBrews = listOf(
            GmoRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, DefaultGenes.BIOLUMINESCENCE, 0.5f),
            GmoRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, DefaultGenes.BIOLUMINESCENCE, 0.5f),
            GmoRecipe(EntityType.VILLAGER, Items.EMERALD, DefaultGenes.EMERALD_HEART, 0.75f),
            GmoRecipe(EntityType.SHULKER, Items.EMERALD, DefaultGenes.KEEP_INVENTORY, 0.5f),
            GmoRecipe(EntityType.RABBIT, Items.REDSTONE, DefaultGenes.SPEED, 0.75f),
            GmoRecipe(EntityType.IRON_GOLEM, Items.APPLE, DefaultGenes.REGENERATION, 0.75f),
            GmoRecipe(EntityType.CHICKEN, Items.EGG, DefaultGenes.LAY_EGG, 0.75f),
            GmoRecipe(EntityType.PIG, Items.PORKCHOP, DefaultGenes.MEATY, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, DefaultGenes.TELEPORT, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.IRON_INGOT, DefaultGenes.ITEM_MAGNET, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, DefaultGenes.MORE_HEARTS, 0.75f),
            GmoRecipe(EntityType.SLIME, Items.GLOWSTONE_DUST, DefaultGenes.PHOTOSYNTHESIS, 0.5f)
        )
        allRecipes.addAll(geneFocusBrews)

        val mutationBrews = listOf(
            GmoRecipe(EntityType.BAT, Items.FEATHER, DefaultGenes.FLIGHT, 0.75f, true),
            GmoRecipe(EntityType.PARROT, Items.FEATHER, DefaultGenes.FLIGHT, 0.75f, true),
            GmoRecipe(EntityType.POLAR_BEAR, Items.EMERALD, DefaultGenes.STRENGTH, 0.5f, true),
            GmoRecipe(EntityType.LLAMA, Items.EMERALD, DefaultGenes.STRENGTH, 0.5f, true),
            GmoRecipe(EntityType.RABBIT, Items.EMERALD, DefaultGenes.LUCK, 0.5f, true),
            GmoRecipe(EntityType.SHULKER, Items.DIAMOND, DefaultGenes.RESISTANCE, 0.5f, true),
            GmoRecipe(EntityType.ZOMBIE, Items.DIAMOND, DefaultGenes.RESISTANCE, 0.5f, true),
            GmoRecipe(EntityType.POLAR_BEAR, Items.DIAMOND, DefaultGenes.CLAWS, 0.5f, true),
            GmoRecipe(EntityType.LLAMA, Items.DIAMOND, DefaultGenes.CLAWS, 0.5f, true),
            GmoRecipe(EntityType.WOLF, Items.DIAMOND, DefaultGenes.CLAWS, 0.5f, true),
            GmoRecipe(EntityType.RABBIT, Items.REDSTONE, DefaultGenes.SPEED_2, 0.5f, true),
            GmoRecipe(EntityType.OCELOT, Items.REDSTONE, DefaultGenes.SPEED_4, 0.5f, true),
            GmoRecipe(EntityType.RABBIT, Items.IRON_INGOT, DefaultGenes.HASTE, 0.5f, true),
            GmoRecipe(EntityType.SILVERFISH, Items.REDSTONE, DefaultGenes.EFFICIENCY, 0.5f, true),
            GmoRecipe(EntityType.ZOMBIE, Items.SPIDER_EYE, DefaultGenes.SCARE_CREEPERS, 0.75f, true),
            GmoRecipe(EntityType.SPIDER, Items.SPIDER_EYE, DefaultGenes.SCARE_SKELETONS, 0.75f, true),
            GmoRecipe(EntityType.ENDER_DRAGON, Items.REDSTONE, DefaultGenes.REGENERATION, 0.5f, true),
            GmoRecipe(EntityType.PIG, Items.BLAZE_POWDER, DefaultGenes.MEATY, 0.5f, true),
            GmoRecipe(EntityType.BAT, Items.ENDER_EYE, DefaultGenes.INVISIBLE, 0.5f, true),
            GmoRecipe(EntityType.SKELETON, Items.ENDER_EYE, DefaultGenes.INVISIBLE, 0.5f, true),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, DefaultGenes.MORE_HEARTS, 0.5f, true)
        )
        allRecipes.addAll(mutationBrews)

        val virusBrews = listOf(
            VirusRecipe(DefaultGenes.POISON_IMMUNITY, DefaultGenes.POISON),
            VirusRecipe(DefaultGenes.WITHER_HIT, DefaultGenes.POISON_4),
            VirusRecipe(DefaultGenes.WITHER_PROOF, DefaultGenes.WITHER),
            VirusRecipe(DefaultGenes.STRENGTH, DefaultGenes.WEAKNESS),
            VirusRecipe(DefaultGenes.NIGHT_VISION, DefaultGenes.BLINDNESS),
            VirusRecipe(DefaultGenes.SPEED, DefaultGenes.SLOWNESS),
            VirusRecipe(DefaultGenes.SPEED_2, DefaultGenes.SLOWNESS_4),
            VirusRecipe(DefaultGenes.SPEED_4, DefaultGenes.SLOWNESS_6),
            VirusRecipe(DefaultGenes.FLIGHT, DefaultGenes.SLOWNESS_6),
            VirusRecipe(DefaultGenes.MILKY, DefaultGenes.NAUSEA),
            VirusRecipe(DefaultGenes.MEATY, DefaultGenes.NAUSEA),
            VirusRecipe(DefaultGenes.LAY_EGG, DefaultGenes.NAUSEA),
            VirusRecipe(DefaultGenes.NO_HUNGER, DefaultGenes.HUNGER),
            VirusRecipe(DefaultGenes.FIRE_PROOF, DefaultGenes.FLAMBE),
            VirusRecipe(DefaultGenes.LUCK, DefaultGenes.CURSED),
            VirusRecipe(DefaultGenes.HASTE, DefaultGenes.MINING_WEAKNESS),
//            VirusRecipe(DefaultGenes.EMERALD_HEART, convert villager to zombie),
            VirusRecipe(DefaultGenes.SCARE_CREEPERS, DefaultGenes.GREEN_DEATH),
            VirusRecipe(DefaultGenes.SCARE_SKELETONS, DefaultGenes.UN_UNDEATH),
            VirusRecipe(DefaultGenes.SCARE_ZOMBIES, DefaultGenes.UN_UNDEATH),
//            VirusRecipe(DefaultGenes.RESISTANCE, instant death to passive mobs),
            VirusRecipe(DefaultGenes.DRAGONS_BREATH, DefaultGenes.WHITE_DEATH),
//            VirusRecipe(syringe with all negative effects, DefaultGenes.DEAD_ALL),
        )
        allRecipes.addAll(virusBrews)

        for (recipe in allRecipes) {
            BrewingRecipeRegistry.addRecipe(recipe)
        }

    }

}