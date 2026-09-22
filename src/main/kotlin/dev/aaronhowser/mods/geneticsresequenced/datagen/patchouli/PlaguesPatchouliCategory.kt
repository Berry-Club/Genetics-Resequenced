package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.geneStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.major
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.patchoulidatagen.patchouli.book_element.PatchouliBook
import dev.aaronhowser.mods.patchoulidatagen.patchouli.book_element.PatchouliBookCategory
import dev.aaronhowser.mods.patchoulidatagen.patchouli.provider.PatchouliBookProvider.Companion.bold
import dev.aaronhowser.mods.patchoulidatagen.patchouli.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object PlaguesPatchouliCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: PatchouliBookCategory

	fun generate(book: PatchouliBook, registries: HolderLookup.Provider) {
		this.registries = registries
		bookCategory = book.category(
			saveName = "genes/plague",
			name = "Plagues",
			description = "Plagues are a special type of negative Gene that ${bad("kills")} the host.",
			icon = Items.WITHER_SKELETON_SKULL,
			parent = NegativeGenesPatchouliCategory.bookCategory
		) {
			sortNumber = 0
		}

		addEntries(book)
	}

	private fun addEntries(book: PatchouliBook) {
		book.entry(
			saveName = "gray_death",
			category = bookCategory,
			name = "Gray Death",
			icon = Items.POISONOUS_POTATO
		) {
			textPage(
				text = "The ${major("Gray Death")} Gene ${bad("kills any mob that can age")}."
			)

			textPage(text = "By default, brew Viral Agents with a Resistance DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "green_death",
			category = bookCategory,
			name = "Green Death",
			icon = Items.CREEPER_HEAD
		) {
			textPage(
				text = "The ${major("Green Death")} Gene ${bad("kills Creepers")}."
			)

			textPage(text = "By default, brew Viral Agents with a Scare Creepers DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "un_undeath",
			category = bookCategory,
			name = "Un-Undeath",
			icon = Items.ZOMBIE_HEAD
		) {
			textPage(
				text = "The ${major("Un-Undeath")} Gene ${bad("kills the Undead")}."
			)

			textPage(text = "By default, brew Viral Agents with a Scare Skeletons or Scare Zombies DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "white_death",
			category = bookCategory,
			name = "White Death",
			icon = Items.SHIELD
		) {
			textPage(
				text = "The ${major("White Death")} Gene ${bad("kills all Monsters")}."
			)

			textPage(text = "By default, brew Viral Agents with a Dragon Breath DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "black_death",
			category = bookCategory,
			name = "Black Death",
			icon = Items.WITHER_ROSE
		) {
			sortNumber = 5

			textPage(
				text = doubleSpacedLines(
					"The ${major("Black Death")} Gene ${bad("instantly kills whoever has it")}.",
					"Given the strength of this Gene, it's much harder to acquire than the other plagues."
				)
			)

			spotlightPage(geneStack(registries, ModItems.DNA_HELIX.get(), ModGenes.BLACK_DEATH)) {
				text = doubleSpacedLines(
					"To craft this DNA Helix, you'll have to get a full Syringe that has ${bad(bold("every other negative Gene"))} in the game. This includes all the other plagues, but excludes any disabled Genes.",
					"From there, simply brew that Syringe into a Potion of Viral Agents."
				)
			}

			textPage(text = "By default, combine Viral Agents with every enabled negative Gene in an Incubator.")
		}


	}

}