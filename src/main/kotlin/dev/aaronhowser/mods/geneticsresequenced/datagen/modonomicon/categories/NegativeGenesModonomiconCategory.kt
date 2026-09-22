package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories

import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.defaultWeightPages
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.minor
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBook
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookCategory
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModonomiconBookText.doubleSpacedLines
import net.minecraft.core.HolderLookup
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions

object NegativeGenesModonomiconCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: ModonomiconBookCategory

	fun generate(book: ModonomiconBook, registries: HolderLookup.Provider) {
		this.registries = registries
		bookCategory = book.category(
			saveName = "genes/negative",
			name = "Negative Genes",
			description = doubleSpacedLines(
				"All the negative Genes in the mod",
				"Negative Genes are always lost on death, and there's a config to prevent Players from getting them."
			),
			icon = ModItems.ANTI_PLASMID.get()
		) {
			sortNumber = 4
		}

		bookCategory.entry(
			saveName = "blindness",
			name = "Blindness",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.BLINDNESS)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Blindness")} Gene gives entities the ${bad("Blindness")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Night Vision DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "cringe",
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

		bookCategory.entry(
			saveName = "cursed",
			name = "Cursed",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.UNLUCK)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Cursed")} Gene gives entities the ${bad("Bad Luck")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Luck DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "flambe",
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

		bookCategory.entry(
			saveName = "hunger",
			name = "Hunger",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.HUNGER)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Hunger")} Gene gives entities the ${bad("Hunger")} effect.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a No Hunger DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "infested",
			name = "Infested",
			icon = OtherUtil.getPotionStack(Potions.INFESTED)
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

		bookCategory.entry(
			saveName = "levitation",
			name = "Levitation",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.LEVITATION)
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

		bookCategory.entry(
			saveName = "mining_fatigue",
			name = "Mining Fatigue",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.DIG_SLOWDOWN)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Mining Fatigue")} Gene gives entities the ${bad("Mining Fatigue")} effect.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Haste DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "nausea",
			name = "Nausea",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.CONFUSION)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Nausea")} Gene gives entities the ${bad("Nausea")} effect.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Milky, Meaty, or Lay Egg DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "oozing",
			name = "Oozing",
			icon = OtherUtil.getPotionStack(Potions.OOZING)
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

		bookCategory.entry(
			saveName = "poison",
			name = "Poison",
			icon = OtherUtil.getPotionStack(Potions.POISON)
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

		bookCategory.entry(
			saveName = "slowness",
			name = "Slowness",
			icon = OtherUtil.getPotionStack(Potions.SLOWNESS)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Slowness")} Gene gives entities the ${bad("Slowness")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Speed DNA Helix. Speed II makes Slowness IV; Speed IV makes Slowness VI.")
		}

		bookCategory.entry(
			saveName = "weakness",
			name = "Weakness",
			icon = OtherUtil.getPotionStack(Potions.WEAKNESS)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Weakness")} Gene gives entities the ${bad("Weakness")} effect.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(text = "By default, brew Viral Agents with a Strength DNA Helix in an Incubator.")
		}

		bookCategory.entry(
			saveName = "weaving",
			name = "Weaving",
			icon = OtherUtil.getPotionStack(Potions.WEAVING)
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

		bookCategory.entry(
			saveName = "wind_charged",
			name = "Wind Charged",
			icon = OtherUtil.getPotionStack(Potions.WIND_CHARGED)
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

		bookCategory.entry(
			saveName = "wither",
			name = "Wither",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.WITHER)
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