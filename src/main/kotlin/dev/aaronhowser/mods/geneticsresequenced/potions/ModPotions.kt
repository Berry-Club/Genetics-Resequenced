package dev.aaronhowser.mods.geneticsresequenced.potions

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setBasic
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects.ModEffects
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

object ModPotions {

    val POTION_REGISTRY: DeferredRegister<Potion> =
        DeferredRegister.create(ForgeRegistries.POTIONS, GeneticsResequenced.ID)

    private fun addId(name: String): String {
        return GeneticsResequenced.ID + "." + name
    }

    //TODO: Remove splash, lingering, and arrow potions (apparently needs mixins >:( ) Maybe just tooltip them instead? Add a recipe back to regular potion?

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

    private val modPotions: List<Potion> by lazy {
        POTION_REGISTRY.entries.map { it.get() }
    }

    fun tooltip(event: ItemTooltipEvent) {
        val stack = event.itemStack

        val itemPotion = PotionUtils.getPotion(stack)
        if (itemPotion == Potions.EMPTY || itemPotion == ZOMBIFY_VILLAGER) return

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
        return PotionUtils.setPotion(
            ItemStack(Items.POTION),
            potion
        )
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
            ModItems.DNA_HELIX.itemStack.setBasic().ingredient,
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
            ModItems.DNA_HELIX.itemStack.setGene(DefaultGenes.emeraldHeart).ingredient,
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
            GmoRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, DefaultGenes.bioluminescence, 0.5f),
            GmoRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, DefaultGenes.bioluminescence, 0.5f),
            GmoRecipe(EntityType.VILLAGER, Items.EMERALD, DefaultGenes.emeraldHeart, 0.75f),
            GmoRecipe(EntityType.SHULKER, Items.EMERALD, DefaultGenes.keepInventory, 0.5f),
            GmoRecipe(EntityType.RABBIT, Items.REDSTONE, DefaultGenes.speed, 0.75f),
            GmoRecipe(EntityType.IRON_GOLEM, Items.APPLE, DefaultGenes.regeneration, 0.75f),
            GmoRecipe(EntityType.CHICKEN, Items.EGG, DefaultGenes.layEgg, 0.75f),
            GmoRecipe(EntityType.PIG, Items.PORKCHOP, DefaultGenes.meaty, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, DefaultGenes.teleport, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.IRON_INGOT, DefaultGenes.itemMagnet, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, DefaultGenes.moreHearts, 0.75f),
            GmoRecipe(EntityType.SLIME, Items.GLOWSTONE_DUST, DefaultGenes.photosynthesis, 0.5f)
        )
        allRecipes.addAll(geneFocusBrews)

        val mutationBrews = listOf(
            GmoRecipe(EntityType.BAT, Items.FEATHER, DefaultGenes.flight, 0.75f, true),
            GmoRecipe(EntityType.PARROT, Items.FEATHER, DefaultGenes.flight, 0.75f, true),
            GmoRecipe(EntityType.POLAR_BEAR, Items.EMERALD, DefaultGenes.strength, 0.5f, true),
            GmoRecipe(EntityType.LLAMA, Items.EMERALD, DefaultGenes.strength, 0.5f, true),
            GmoRecipe(EntityType.RABBIT, Items.EMERALD, DefaultGenes.luck, 0.5f, true),
            GmoRecipe(EntityType.SHULKER, Items.DIAMOND, DefaultGenes.resistance, 0.5f, true),
            GmoRecipe(EntityType.ZOMBIE, Items.DIAMOND, DefaultGenes.resistance, 0.5f, true),
            GmoRecipe(EntityType.POLAR_BEAR, Items.DIAMOND, DefaultGenes.claws, 0.5f, true),
            GmoRecipe(EntityType.LLAMA, Items.DIAMOND, DefaultGenes.claws, 0.5f, true),
            GmoRecipe(EntityType.WOLF, Items.DIAMOND, DefaultGenes.claws, 0.5f, true),
            GmoRecipe(EntityType.RABBIT, Items.REDSTONE, DefaultGenes.speedTwo, 0.5f, true),
            GmoRecipe(EntityType.OCELOT, Items.REDSTONE, DefaultGenes.speedFour, 0.5f, true),
            GmoRecipe(EntityType.RABBIT, Items.IRON_INGOT, DefaultGenes.haste, 0.5f, true),
            GmoRecipe(EntityType.SILVERFISH, Items.REDSTONE, DefaultGenes.efficiency, 0.5f, true),
            GmoRecipe(EntityType.ZOMBIE, Items.SPIDER_EYE, DefaultGenes.scareCreepers, 0.75f, true),
            GmoRecipe(EntityType.SPIDER, Items.SPIDER_EYE, DefaultGenes.scareSkeletons, 0.75f, true),
            GmoRecipe(EntityType.ENDER_DRAGON, Items.REDSTONE, DefaultGenes.regeneration, 0.5f, true),
            GmoRecipe(EntityType.PIG, Items.BLAZE_POWDER, DefaultGenes.meaty, 0.5f, true),
            GmoRecipe(EntityType.BAT, Items.ENDER_EYE, DefaultGenes.invisible, 0.5f, true),
            GmoRecipe(EntityType.SKELETON, Items.ENDER_EYE, DefaultGenes.invisible, 0.5f, true),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, DefaultGenes.moreHearts, 0.5f, true)
        )
        allRecipes.addAll(mutationBrews)

        val virusBrews = listOf(
            VirusRecipe(DefaultGenes.poisonImmunity, DefaultGenes.poison),
            VirusRecipe(DefaultGenes.witherHit, DefaultGenes.poisonFour),
            VirusRecipe(DefaultGenes.witherProof, DefaultGenes.wither),
            VirusRecipe(DefaultGenes.strength, DefaultGenes.weakness),
            VirusRecipe(DefaultGenes.nightVision, DefaultGenes.blindness),
            VirusRecipe(DefaultGenes.speed, DefaultGenes.slowness),
            VirusRecipe(DefaultGenes.speedTwo, DefaultGenes.slownessFour),
            VirusRecipe(DefaultGenes.speedFour, DefaultGenes.slownessSix),
            VirusRecipe(DefaultGenes.flight, DefaultGenes.slownessSix),
            VirusRecipe(DefaultGenes.milky, DefaultGenes.nausea),
            VirusRecipe(DefaultGenes.meaty, DefaultGenes.nausea),
            VirusRecipe(DefaultGenes.layEgg, DefaultGenes.nausea),
            VirusRecipe(DefaultGenes.noHunger, DefaultGenes.hunger),
            VirusRecipe(DefaultGenes.fireProof, DefaultGenes.flambe),
            VirusRecipe(DefaultGenes.luck, DefaultGenes.cursed),
            VirusRecipe(DefaultGenes.haste, DefaultGenes.miningFatigue),
            VirusRecipe(DefaultGenes.scareCreepers, DefaultGenes.greenDeath),
            VirusRecipe(DefaultGenes.scareSkeletons, DefaultGenes.unUndeath),
            VirusRecipe(DefaultGenes.scareZombies, DefaultGenes.unUndeath),
            //TODO VirusRecipe(DefaultGenes.RESISTANCE, instant death to passive mobs),
            VirusRecipe(DefaultGenes.dragonsBreath, DefaultGenes.whiteDeath),

            BlackDeathRecipe(),

            BrewingRecipe(
                viralAgentsPotionStack.ingredient,
                ModItems.DNA_HELIX.itemStack.setGene(DefaultGenes.emeraldHeart).ingredient,
                potionStack(ZOMBIFY_VILLAGER)
            )
        )
        allRecipes.addAll(virusBrews)

        for (recipe in allRecipes) {
            BrewingRecipeRegistry.addRecipe(recipe)
        }

    }

}