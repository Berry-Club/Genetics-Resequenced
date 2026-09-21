package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.activeAntiFieldOrb
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.entityStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.geneStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.gmoCellStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.minor
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.pageLink
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.plasmidStack
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBook
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBookCategory
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items

class PlaguesPatchouliCategory(
	private val registries: HolderLookup.Provider
) {

	private lateinit var category: PatchouliBookCategory

	fun generate(book: PatchouliBook) {
		category = book.category(
			saveName = "genes/plague",
			name = "Plagues",
			description = "Plagues are a special type of negative Gene that ${bad("kills")} the host.",
			icon = Items.WITHER_SKELETON_SKULL,
			parent = NegativeGenesPatchouliCategory.BOOK_CATEGORY
		) {
			sortNumber = 0
		}

		BOOK_CATEGORY = category

		book.addEntries()
	}

	private fun PatchouliBook.addEntries() {
		entry(
			saveName = "black_death",
			category = category,
			name = "Black Death",
			icon = Items.WITHER_ROSE
		) {
			sortNumber = 5

			textPage(
				text = "The ${major("Black Death")} Gene ${bad("instantly kills whoever has it")}.\$(br2)Given the strength of this Gene, it's much harder to acquire than the other plagues."
			)

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.BLACK_DEATH)) {
				text = "To craft this DNA Helix, you'll have to get a full Syringe that has ${bad("\$(bold)every other negative Gene")} in the game. This includes all the other plagues, but excludes any disabled Genes.\$(br2)From there, simply brew that Syringe into a Potion of Viral Agents."
			}
		}

		entry(
			saveName = GRAY_DEATH.localSaveName,
			category = category,
			name = "Gray Death",
			icon = Items.POISONOUS_POTATO
		) {
			textPage(
				text = "The ${major("Gray Death")} Gene ${bad("kills any mob that can age")}."
			)
		}

		entry(
			saveName = GREEN_DEATH.localSaveName,
			category = category,
			name = "Green Death",
			icon = Items.CREEPER_HEAD
		) {
			textPage(
				text = "The ${major("Green Death")} Gene ${bad("kills Creepers")}."
			)
		}

		entry(
			saveName = UN_UNDEATH.localSaveName,
			category = category,
			name = "Un-Undeath",
			icon = Items.ZOMBIE_HEAD
		) {
			textPage(
				text = "The ${major("Un-Undeath")} Gene ${bad("kills the Undead")}."
			)
		}

		entry(
			saveName = WHITE_DEATH.localSaveName,
			category = category,
			name = "White Death",
			icon = Items.SHIELD
		) {
			textPage(
				text = "The ${major("White Death")} Gene ${bad("kills all Monsters")}."
			)
		}
	}

	companion object {

		lateinit var BOOK_CATEGORY: PatchouliBookCategory
			private set

		val CATEGORY = PatchouliBookReference("genes/plague")
		val GRAY_DEATH = PatchouliBookReference("genes/plague/gray_death")
		val GREEN_DEATH = PatchouliBookReference("genes/plague/green_death")
		val UN_UNDEATH = PatchouliBookReference("genes/plague/un_undeath")
		val WHITE_DEATH = PatchouliBookReference("genes/plague/white_death")
	}
}