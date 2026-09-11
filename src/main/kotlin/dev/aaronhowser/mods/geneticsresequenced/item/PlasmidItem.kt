package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ItemStackNbt
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class PlasmidItem(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		pStack: ItemStack,
		pLevel: Level?,
		pTooltipComponents: MutableList<Component>,
		pIsAdvanced: TooltipFlag
	) {
		val geneRk = getGeneRk(pStack)

		if (geneRk == null) {
			pTooltipComponents.add(
				ModTooltipLang.PLASMID_EMPTY
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
			return
		}

		val registryAccess = pLevel?.registryAccess() ?: return
		val geneHolder = geneRk.getHolderOrThrow(registryAccess)

		pTooltipComponents.add(
			ModTooltipLang.PLASMID_GENE
				.toComponent(geneHolder.getName())
				.withStyle(ChatFormatting.GRAY)
		)

		if (isComplete(pStack, registryAccess)) {
			pTooltipComponents.add(
				ModTooltipLang.PLASMID_COMPLETE
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			)
		} else {
			val amountNeeded = geneHolder.value().dnaPointsRequired
			val amount = getDnaPoints(pStack)

			pTooltipComponents.add(
				ModTooltipLang.PLASMID_PROGRESS
					.toComponent(amount, amountNeeded)
					.withStyle(ChatFormatting.GRAY)
			)
		}
	}

	companion object {
		private const val PLASMID_PROGRESS = "geneticsresequenced:plasmid_progress"
		private const val GENE = "gene"
		private const val DNA_POINTS = "dna_points"

		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		fun hasGene(itemStack: ItemStack): Boolean = getProgressTag(itemStack) != null

		fun getGeneRk(itemStack: ItemStack): ResourceKey<Gene>? {
			val progressTag = getProgressTag(itemStack) ?: return null
			val geneId = progressTag.getString(GENE)
			if (geneId.isEmpty()) return null
			val geneLocation = ResourceLocation.tryParse(geneId) ?: return null

			return ResourceKey.create(ModGenes.GENE_REGISTRY_KEY, geneLocation)
		}

		fun setGene(itemStack: ItemStack, geneRk: ResourceKey<Gene>, amount: Int = 0) {
			val progressTag = CompoundTag()
			progressTag.putString(GENE, geneRk.location().toString())
			progressTag.putInt(DNA_POINTS, amount)
			ItemStackNbt.put(itemStack, PLASMID_PROGRESS, progressTag)
		}

		fun getDnaPoints(itemStack: ItemStack): Int {
			return getProgressTag(itemStack)?.getInt(DNA_POINTS) ?: 0
		}

		fun setDnaPoints(itemStack: ItemStack, amount: Int) {
			val progressTag = getProgressTag(itemStack) ?: return
			progressTag.putInt(DNA_POINTS, amount)
		}

		fun copyProgress(from: ItemStack, to: ItemStack): Boolean {
			val progressTag = getProgressTag(from) ?: return false
			ItemStackNbt.put(to, PLASMID_PROGRESS, progressTag.copy())
			return true
		}

		private fun getProgressTag(itemStack: ItemStack): CompoundTag? {
			return ItemStackNbt.getCompound(itemStack, PLASMID_PROGRESS)
		}

		fun increaseDnaPoints(itemStack: ItemStack, amount: Int = 1) {
			setDnaPoints(itemStack, getDnaPoints(itemStack) + amount)
		}

		fun isComplete(itemStack: ItemStack, registryAccess: RegistryAccess): Boolean {
			val geneRk = getGeneRk(itemStack) ?: return false
			val geneHolder = geneRk.getHolderOrThrow(registryAccess)
			return getDnaPoints(itemStack) >= geneHolder.value().dnaPointsRequired
		}

		fun getCompletedPlasmid(geneHolder: Holder<Gene>): ItemStack {
			val stack = ModItems.PLASMID.getDefaultInstance()
			setGene(stack, geneHolder.unwrapKey().get(), geneHolder.value().dnaPointsRequired)
			return stack
		}

		fun getAllPlasmids(registries: HolderLookup.Provider): List<ItemStack> {
			return ModGenes.getRegistrySorted(registries, includeHelixOnly = false).map(::getCompletedPlasmid)
		}

	}

}