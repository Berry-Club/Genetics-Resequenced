package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.defaultWeightPages
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.minor
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBook
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBookCategory
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object NegativeGenesPatchouliCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: PatchouliBookCategory
		private set

	fun generate(book: PatchouliBook, registries: HolderLookup.Provider) {
		this.registries = registries
		bookCategory = book.category(
			saveName = "genes/negative",
			name = "Negative Genes",
			description = doubleSpacedLines(
				"All the negative Genes in the mod",
				"Negative Genes are always lost on death, and there's a config to prevent Players from getting them."
			),
			icon = ModItems.ANTI_PLASMID.get(),
			parent = GenesPatchouliCategory.bookCategory
		) {
			sortNumber = 0
		}

		addEntries(book)
	}

	private fun addEntries(book: PatchouliBook) {
		book.entry(
			saveName = "blindness",
			category = bookCategory,
			name = "Blindness",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Blindness")} Gene gives entities the ${bad("Blindness")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Night Vision DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "cringe",
			category = bookCategory,
			name = "Cringe",
			icon = Items.PLAYER_HEAD
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Cringe")} Gene ${bad("uwufies your chat messages")}, and sets your language to LOLCAT.",
					"That second feature can be disabled in the client config.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"1/1 - Player"
			)
		}

		book.entry(
			saveName = "cursed",
			category = bookCategory,
			name = "Cursed",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Cursed")} Gene gives entities the ${bad("Bad Luck")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Luck DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "flambe",
			category = bookCategory,
			name = "Flambé",
			icon = Items.BLAZE_POWDER
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Flambé")} Gene ${bad("constantly lights entities on fire")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Fire Proof DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "hunger",
			category = bookCategory,
			name = "Hunger",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Hunger")} Gene gives entities the ${bad("Hunger")} effect.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a No Hunger DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "infested",
			category = bookCategory,
			name = "Infested",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Infested")} Gene gives entities the ${bad("Infested")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"4/11 - Silverfish"
			)
		}

		book.entry(
			saveName = "levitation",
			category = bookCategory,
			name = "Levitation",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Levitation")} Gene gives entities the ${bad("Levitation")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"4/11 - Shulker"
			)
		}

		book.entry(
			saveName = "mining_fatigue",
			category = bookCategory,
			name = "Mining Fatigue",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Mining Fatigue")} Gene gives entities the ${bad("Mining Fatigue")} effect.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Haste DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "nausea",
			category = bookCategory,
			name = "Nausea",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Nausea")} Gene gives entities the ${bad("Nausea")} effect.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Milky, Meaty, or Lay Egg DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "oozing",
			category = bookCategory,
			name = "Oozing",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Oozing")} Gene gives entities the ${bad("Oozing")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"5/16 - Slime"
			)
		}

		book.entry(
			saveName = "poison",
			category = bookCategory,
			name = "Poison",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Poison")} Gene gives entities the ${bad("Poison")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/16 - Pufferfish"
			)
		}

		book.entry(
			saveName = "slowness",
			category = bookCategory,
			name = "Slowness",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Slowness")} Gene gives entities the ${bad("Slowness")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Speed DNA Helix. Speed II makes Slowness IV; Speed IV makes Slowness VI.")
		}

		book.entry(
			saveName = "weakness",
			category = bookCategory,
			name = "Weakness",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Weakness")} Gene gives entities the ${bad("Weakness")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Strength DNA Helix in an Incubator.")
		}

		book.entry(
			saveName = "weaving",
			category = bookCategory,
			name = "Weaving",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Weaving")} Gene gives entities the ${bad("Weaving")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"4/15 - Spider"
			)
		}

		book.entry(
			saveName = "wind_charged",
			category = bookCategory,
			name = "Wind Charged",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Wind Charged")} Gene gives entities the ${bad("Wind Charged")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"4/11 - Breeze"
			)
		}

		book.entry(
			saveName = "wither",
			category = bookCategory,
			name = "Wither",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Wither")} Gene gives entities the ${bad("Wither")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Wither Proof DNA Helix in an Incubator.")
		}


	}

}