package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBook
import dev.aaronhowser.mods.patchoulidatagen.dsl.patchouliBook
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider
import dev.aaronhowser.mods.patchoulidatagen.provider.TextColor
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Unit
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

class ModPatchouliBookProvider(
	generator: DataGenerator,
	private val registries: HolderLookup.Provider
) : PatchouliBookProvider(generator, registries, "guide") {

	override fun buildBook(): PatchouliBook = patchouliBook(
		namespace = GeneticsResequenced.MOD_ID,
		name = "item.geneticsresequenced.guide_book",
		landingText = "book.geneticsresequenced.landing_text"
	) {
		version = "2"
		creativeTab = "geneticsresequenced:creative_tab"
		showProgress = false
		bookTexture = ResourceLocation.fromNamespaceAndPath("patchouli", "textures/gui/book_gray.png")

		GettingStartedPatchouliCategory.generate(this, registries)
		BlocksPatchouliCategory.generate(this, registries)
		ItemsPatchouliCategory.generate(this, registries)
		GenesPatchouliCategory.generate(this, registries)
		NegativeGenesPatchouliCategory.generate(this, registries)
		PlaguesPatchouliCategory.generate(this, registries)
	}

	companion object {

		@JvmStatic
		fun activeAntiFieldOrb(): ItemStack {
			val stack = ModItems.ANTI_FIELD_ORB.toStack()
			stack.set(ModDataComponents.IS_ACTIVE, Unit.INSTANCE)
			return stack
		}

		@JvmStatic
		fun entityStack(item: ItemLike, entityType: EntityType<*>): ItemStack {
			return entityStack(item.itemStack, entityType)
		}

		@JvmStatic
		fun entityStack(stack: ItemStack, entityType: EntityType<*>): ItemStack {
			stack.set(ModDataComponents.ENTITY_TYPE, entityType)
			return stack
		}

		@JvmStatic
		fun geneStack(
			registries: HolderLookup.Provider,
			item: ItemLike,
			geneKey: ResourceKey<Gene>
		): ItemStack {
			val stack = item.itemStack
			stack.set(ModDataComponents.GENE, geneHolder(registries, geneKey))
			return stack
		}

		@JvmStatic
		fun gmoCellStack(
			registries: HolderLookup.Provider,
			geneKey: ResourceKey<Gene>,
			entityType: EntityType<*>
		): ItemStack {
			val stack = geneStack(registries, ModItems.GMO_CELL.get(), geneKey)
			return entityStack(stack, entityType)
		}

		@JvmStatic
		fun plasmidStack(
			registries: HolderLookup.Provider,
			geneKey: ResourceKey<Gene>,
			dnaPoints: Int
		): ItemStack {
			val stack = ModItems.PLASMID.toStack()
			PlasmidItem.setGene(stack, geneHolder(registries, geneKey), dnaPoints)
			return stack
		}

		@JvmStatic
		fun major(text: String): String {
			return colored(TextColor.LIGHT_PURPLE, text)
		}

		@JvmStatic
		fun minor(text: String): String {
			return colored(TextColor.BLUE, text)
		}

		@JvmStatic
		fun bad(text: String): String {
			return colored(TextColor.RED, text)
		}

		private fun geneHolder(
			registries: HolderLookup.Provider,
			geneKey: ResourceKey<Gene>
		): Holder.Reference<Gene> {
			return ModGenes
				.getGeneRegistry(registries)
				.getOrThrow(geneKey)
		}
	}

}