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
            ModItems.DNA_HELIX.itemStack.setGene(ModGenes.emeraldHeart).ingredient,
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
            GmoRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, ModGenes.bioluminescence, 0.5f),
            GmoRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, ModGenes.bioluminescence, 0.5f),
            GmoRecipe(EntityType.VILLAGER, Items.EMERALD, ModGenes.emeraldHeart, 0.75f),
            GmoRecipe(EntityType.SHULKER, Items.EMERALD, ModGenes.keepInventory, 0.5f),
            GmoRecipe(EntityType.RABBIT, Items.REDSTONE, ModGenes.speed, 0.75f),
            GmoRecipe(EntityType.IRON_GOLEM, Items.APPLE, ModGenes.regeneration, 0.75f),
            GmoRecipe(EntityType.CHICKEN, Items.EGG, ModGenes.layEgg, 0.75f),
            GmoRecipe(EntityType.PIG, Items.PORKCHOP, ModGenes.meaty, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, ModGenes.teleport, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.IRON_INGOT, ModGenes.itemMagnet, 0.75f),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.moreHearts, 0.75f),
            GmoRecipe(EntityType.SLIME, Items.GLOWSTONE_DUST, ModGenes.photosynthesis, 0.5f)
        )
        allRecipes.addAll(geneFocusBrews)

        val mutationBrews = listOf(
            GmoRecipe(EntityType.BAT, Items.FEATHER, ModGenes.flight, 0.75f, isMutation = true),
            GmoRecipe(EntityType.PARROT, Items.FEATHER, ModGenes.flight, 0.75f, isMutation = true),
            GmoRecipe(EntityType.POLAR_BEAR, Items.EMERALD, ModGenes.strengthTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.LLAMA, Items.EMERALD, ModGenes.strengthTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.EMERALD, ModGenes.luck, 0.5f, isMutation = true),
            GmoRecipe(EntityType.SHULKER, Items.DIAMOND, ModGenes.resistanceTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.ZOMBIE, Items.DIAMOND, ModGenes.resistanceTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.POLAR_BEAR, Items.DIAMOND, ModGenes.clawsTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.LLAMA, Items.DIAMOND, ModGenes.clawsTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.WOLF, Items.DIAMOND, ModGenes.clawsTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.REDSTONE, ModGenes.speedTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.OCELOT, Items.REDSTONE, ModGenes.speedFour, 0.5f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.IRON_INGOT, ModGenes.hasteTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.SILVERFISH, Items.REDSTONE, ModGenes.efficiencyFour, 0.5f, isMutation = true),
            GmoRecipe(EntityType.ZOMBIE, Items.SPIDER_EYE, ModGenes.scareZombies, 0.75f, isMutation = true),
            GmoRecipe(EntityType.SPIDER, Items.SPIDER_EYE, ModGenes.scareSpiders, 0.75f, isMutation = true),
            GmoRecipe(EntityType.ENDER_DRAGON, Items.REDSTONE, ModGenes.regenerationFour, 0.5f, isMutation = true),
            GmoRecipe(EntityType.PIG, Items.BLAZE_POWDER, ModGenes.meatyTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.BAT, Items.ENDER_EYE, ModGenes.invisible, 0.5f, isMutation = true),
            GmoRecipe(EntityType.SKELETON, Items.ENDER_EYE, ModGenes.invisible, 0.5f, isMutation = true),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.moreHeartsTwo, 0.5f, true)
        )
        allRecipes.addAll(mutationBrews)

        val virusBrews = listOf(
            VirusRecipe(ModGenes.poisonImmunity, ModGenes.poison),
            VirusRecipe(ModGenes.witherHit, ModGenes.poisonFour),
            VirusRecipe(ModGenes.witherProof, ModGenes.wither),
            VirusRecipe(ModGenes.strength, ModGenes.weakness),
            VirusRecipe(ModGenes.nightVision, ModGenes.blindness),
            VirusRecipe(ModGenes.speed, ModGenes.slowness),
            VirusRecipe(ModGenes.speedTwo, ModGenes.slownessFour),
            VirusRecipe(ModGenes.speedFour, ModGenes.slownessSix),
            VirusRecipe(ModGenes.flight, ModGenes.slownessSix),
            VirusRecipe(ModGenes.milky, ModGenes.nausea),
            VirusRecipe(ModGenes.meaty, ModGenes.nausea),
            VirusRecipe(ModGenes.layEgg, ModGenes.nausea),
            VirusRecipe(ModGenes.noHunger, ModGenes.hunger),
            VirusRecipe(ModGenes.fireProof, ModGenes.flambe),
            VirusRecipe(ModGenes.luck, ModGenes.cursed),
            VirusRecipe(ModGenes.haste, ModGenes.miningFatigue),
            VirusRecipe(ModGenes.scareCreepers, ModGenes.greenDeath),
            VirusRecipe(ModGenes.scareSkeletons, ModGenes.unUndeath),
            VirusRecipe(ModGenes.scareZombies, ModGenes.unUndeath),
            //TODO VirusRecipe(DefaultGenes.RESISTANCE, instant death to passive mobs),
            VirusRecipe(ModGenes.dragonsBreath, ModGenes.whiteDeath),

            BlackDeathRecipe(),

            BrewingRecipe(
                viralAgentsPotionStack.ingredient,
                ModItems.DNA_HELIX.itemStack.setGene(ModGenes.emeraldHeart).ingredient,
                potionStack(ZOMBIFY_VILLAGER)
            )
        )
        allRecipes.addAll(virusBrews)

        for (recipe in allRecipes) {
            BrewingRecipeRegistry.addRecipe(recipe)
        }

    }

}