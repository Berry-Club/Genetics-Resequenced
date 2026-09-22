package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookItems.geneStack
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.major
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBook
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookCategory
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.bold
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.doubleSpacedLines
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object PlaguesModonomiconCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: ModonomiconBookCategory

	fun generate(book: ModonomiconBook, registries: HolderLookup.Provider) {
		this.registries = registries
		bookCategory = book.category(
			saveName = "genes/plague",
			name = "Plagues",
			description = "Plagues are a special type of negative Gene that ${bad("kills")} the host.",
			icon = Items.WITHER_SKELETON_SKULL
		) {
			sortNumber = 5
		}

		bookCategory.entry(
			saveName = "gray_death",
			name = "Gray Death",
			icon = Items.POISONOUS_POTATO
		) {
			textPage(
				text = "The ${major("Gray Death")} Gene ${bad("kills any mob that can age")}."
			)

			textPage(text = "By default, brew Viral Agents with a Resistance DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "green_death",
			name = "Green Death",
			icon = Items.CREEPER_HEAD
		) {
			textPage(
				text = "The ${major("Green Death")} Gene ${bad("kills Creepers")}."
			)

			textPage(text = "By default, brew Viral Agents with a Scare Creepers DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "un_undeath",
			name = "Un-Undeath",
			icon = Items.ZOMBIE_HEAD
		) {
			textPage(
				text = "The ${major("Un-Undeath")} Gene ${bad("kills the Undead")}."
			)

			textPage(text = "By default, brew Viral Agents with a Scare Skeletons or Scare Zombies DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "white_death",
			name = "White Death",
			icon = Items.SHIELD
		) {
			textPage(
				text = "The ${major("White Death")} Gene ${bad("kills all Monsters")}."
			)

			textPage(text = "By default, brew Viral Agents with a Dragon Breath DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "black_death",
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