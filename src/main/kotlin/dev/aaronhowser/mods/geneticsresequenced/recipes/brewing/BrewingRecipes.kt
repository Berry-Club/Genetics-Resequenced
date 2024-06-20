package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing

import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setBasic
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions.CELL_GROWTH
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions.MUTATION
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions.POTION_REGISTRY
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions.SUBSTRATE
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions.THE_CURE
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions.VIRAL_AGENTS
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions.ZOMBIFY_VILLAGER
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
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

object BrewingRecipes {

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
            GmoRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, ModGenes.bioluminescence, 0.85f),
            GmoRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, ModGenes.bioluminescence, 0.85f),
            GmoRecipe(EntityType.VILLAGER, Items.EMERALD, ModGenes.emeraldHeart, 0.85f),
            GmoRecipe(EntityType.SHULKER, Items.EMERALD_BLOCK, ModGenes.keepInventory, 0.45f),
            GmoRecipe(EntityType.RABBIT, Items.GOLDEN_BOOTS, ModGenes.speed, 0.65f),
            GmoRecipe(EntityType.RABBIT, Items.EMERALD, ModGenes.luck, 0.75f),
            GmoRecipe(EntityType.IRON_GOLEM, Items.GOLDEN_APPLE, ModGenes.regeneration, 0.3f),
            GmoRecipe(EntityType.CHICKEN, Items.EGG, ModGenes.layEgg, 1f),
            GmoRecipe(EntityType.PIG, Items.PORKCHOP, ModGenes.meaty, 1f),
            GmoRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, ModGenes.teleport, 0.45f),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.moreHearts, 0.2f),
            GmoRecipe(EntityType.MOOSHROOM, Items.MUSHROOM_STEM, ModGenes.photosynthesis, 0.7f),
            )
        allRecipes.addAll(geneFocusBrews)

        val mutationBrews = listOf(
            GmoRecipe(EntityType.ENDER_DRAGON, Items.ELYTRA, ModGenes.flight, 0.55f, isMutation = true),
            GmoRecipe(EntityType.POLAR_BEAR, Items.NETHERITE_SWORD, ModGenes.strengthTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.SHULKER, Items.NETHERITE_CHESTPLATE, ModGenes.resistanceTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.POLAR_BEAR, Items.DIAMOND_SWORD, ModGenes.clawsTwo, 0.75f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.DIAMOND_BOOTS, ModGenes.speedTwo, 0.5f, isMutation = true),
            GmoRecipe(EntityType.OCELOT, Items.NETHERITE_BOOTS, ModGenes.speedFour, 0.5f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.NETHERITE_PICKAXE, ModGenes.hasteTwo, 0.35f, isMutation = true),
            GmoRecipe(EntityType.SILVERFISH, Items.NETHERITE_PICKAXE, ModGenes.efficiencyFour, 0.25f, isMutation = true),
            GmoRecipe(EntityType.ZOMBIE, Items.FERMENTED_SPIDER_EYE, ModGenes.scareZombies, 0.5f, isMutation = true),
            GmoRecipe(EntityType.SPIDER, Items.FERMENTED_SPIDER_EYE, ModGenes.scareSpiders, 0.5f, isMutation = true),
            GmoRecipe(EntityType.ENDER_DRAGON, Items.ENCHANTED_GOLDEN_APPLE, ModGenes.regenerationFour, 0.35f, isMutation = true),
            GmoRecipe(EntityType.PIG, Items.BLAZE_POWDER, ModGenes.meatyTwo, 0.75f, isMutation = true),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.moreHeartsTwo, 0.25f, true)
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
            VirusRecipe(ModGenes.resistance, ModGenes.grayDeath),
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