package dev.aaronhowser.mods.geneticsresequenced.recipe

import dev.aaronhowser.mods.aaron.AaronExtensions.asIngredient
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneDataComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Style
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.registries.RegistryObject

object BrewingRecipes {

	private val modPotions: List<Potion>
		get() = ModPotions.POTION_REGISTRY.entries.map(RegistryObject<Potion>::get)

	fun tooltip(event: ItemTooltipEvent) {
		val stack = event.itemStack
		val potion = PotionUtils.getPotion(stack)

		if (potion == ModPotions.ZOMBIFY_VILLAGER.get()) return
		if (potion == ModPotions.PANACEA.get()) return
		if (potion !in modPotions) return

		if (stack.item != Items.POTION) {
			event.toolTip.add(
				ModTooltipLang.IGNORE_POTION
					.toComponent()
					.withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
			)
		}

		val itemGeneRk = GeneDataComponent.getGeneRk(stack)
		if (itemGeneRk != null) {
			event.toolTip.add(
				ModTooltipLang.GENE
					.toComponent(
						Gene.getNameComponent(itemGeneRk)
							.withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
					)
			)
		}

		val itemEntity = EntityDnaItem.getEntityType(stack)
		if (itemEntity != null) {
			event.toolTip.add(
				ModTooltipLang.HELIX_ENTITY
					.toComponent(itemEntity.description)
					.withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
			)
		}

	}

	private fun Potion.asIngredient(): Ingredient = OtherUtil.getPotionStack(this).asIngredient()

	val substratePotionStack
		get() = OtherUtil.getPotionStack(ModPotions.SUBSTRATE.get())
	val cellGrowthPotionStack
		get() = OtherUtil.getPotionStack(ModPotions.CELL_GROWTH.get())
	val mutationPotionStack
		get() = OtherUtil.getPotionStack(ModPotions.MUTATION.get())
	val viralAgentsPotionStack
		get() = OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS.get())
	val panaceaPotionStack
		get() = OtherUtil.getPotionStack(ModPotions.PANACEA.get())

	fun setRecipes() {
		BrewingRecipeRegistry.addRecipe(
			Potions.MUNDANE.asIngredient(),
			ModItems.ORGANIC_MATTER.asIngredient(),
			substratePotionStack
		)

		BrewingRecipeRegistry.addRecipe(
			ModPotions.CELL_GROWTH.get().asIngredient(),
			Items.FERMENTED_SPIDER_EYE.asIngredient(),
			mutationPotionStack
		)


		BrewingRecipeRegistry.addRecipe(
			ModPotions.MUTATION.get().asIngredient(),
			Items.CHORUS_FRUIT.asIngredient(),
			viralAgentsPotionStack
		)

	}

}