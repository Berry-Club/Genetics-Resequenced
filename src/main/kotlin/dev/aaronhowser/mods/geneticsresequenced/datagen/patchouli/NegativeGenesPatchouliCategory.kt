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

class NegativeGenesPatchouliCategory(
	private val registries: HolderLookup.Provider
) {

	private lateinit var category: PatchouliBookCategory

	fun generate(book: PatchouliBook) {
		category = book.category(
			saveName = "genes/negative",
			name = "Negative Genes",
			description = "All the negative Genes in the mod\$(br2)Negative Genes are always lost on death, and there's a config to prevent Players from getting them.",
			icon = ModItems.ANTI_PLASMID.get(),
			parent = GenesPatchouliCategory.BOOK_CATEGORY
		) {
			sortNumber = 0
		}

		BOOK_CATEGORY = category

		book.addEntries()
	}

	private fun PatchouliBook.addEntries() {
		entry(
			saveName = "bad_omen",
			category = category,
			name = "Bad Omen",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Bad Omen")} Gene gives entities the ${bad("Bad Omen")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = BLINDNESS.localSaveName,
			category = category,
			name = "Blindness",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Blindness")} Gene gives entities the ${bad("Blindness")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = CRINGE.localSaveName,
			category = category,
			name = "Cringe",
			icon = Items.PLAYER_HEAD
		) {
			textPage(
				text = "The ${major("Cringe")} Gene ${bad("uwufies your chat messages")}, and sets your language to LOLCAT.\$(br2)That second feature can be disabled in the client config.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = CURSED.localSaveName,
			category = category,
			name = "Cursed",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Cursed")} Gene gives entities the ${bad("Bad Luck")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = FLAMBE.localSaveName,
			category = category,
			name = "Flambé",
			icon = Items.BLAZE_POWDER
		) {
			textPage(
				text = "The ${major("Flambé")} Gene ${bad("constantly lights entities on fire")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = HUNGER.localSaveName,
			category = category,
			name = "Hunger",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Hunger")} Gene gives entities the ${bad("Hunger")} effect.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = INFESTED.localSaveName,
			category = category,
			name = "Infested",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Infested")} Gene gives entities the ${bad("Infested")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = LEVITATION.localSaveName,
			category = category,
			name = "Levitation",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Levitation")} Gene gives entities the ${bad("Levitation")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = MINING_FATIGUE.localSaveName,
			category = category,
			name = "Mining Fatigue",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Mining Fatigue")} Gene gives entities the ${bad("Mining Fatigue")} effect.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = NAUSEA.localSaveName,
			category = category,
			name = "Nausea",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Nausea")} Gene gives entities the ${bad("Nausea")} effect.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = OOZING.localSaveName,
			category = category,
			name = "Oozing",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Oozing")} Gene gives entities the ${bad("Oozing")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = POISON.localSaveName,
			category = category,
			name = "Poison",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Poison")} Gene gives entities the ${bad("Poison")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = SLOWNESS.localSaveName,
			category = category,
			name = "Slowness",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Slowness")} Gene gives entities the ${bad("Slowness")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = WEAKNESS.localSaveName,
			category = category,
			name = "Weakness",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Weakness")} Gene gives entities the ${bad("Weakness")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = WEAVING.localSaveName,
			category = category,
			name = "Weaving",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Weaving")} Gene gives entities the ${bad("Weaving")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = WIND_CHARGED.localSaveName,
			category = category,
			name = "Wind Charged",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Wind Charged")} Gene gives entities the ${bad("Wind Charged")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = WITHER.localSaveName,
			category = category,
			name = "Wither",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Wither")} Gene gives entities the ${bad("Wither")} effect.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}
	}

	companion object {

		lateinit var BOOK_CATEGORY: PatchouliBookCategory
			private set

		val CATEGORY = PatchouliBookReference("genes/negative")
		val BLINDNESS = PatchouliBookReference("genes/negative/blindness")
		val CRINGE = PatchouliBookReference("genes/negative/cringe")
		val CURSED = PatchouliBookReference("genes/negative/cursed")
		val FLAMBE = PatchouliBookReference("genes/negative/flambe")
		val HUNGER = PatchouliBookReference("genes/negative/hunger")
		val INFESTED = PatchouliBookReference("genes/negative/infested")
		val LEVITATION = PatchouliBookReference("genes/negative/levitation")
		val MINING_FATIGUE = PatchouliBookReference("genes/negative/mining_fatigue")
		val NAUSEA = PatchouliBookReference("genes/negative/nausea")
		val OOZING = PatchouliBookReference("genes/negative/oozing")
		val POISON = PatchouliBookReference("genes/negative/poison")
		val SLOWNESS = PatchouliBookReference("genes/negative/slowness")
		val WEAKNESS = PatchouliBookReference("genes/negative/weakness")
		val WEAVING = PatchouliBookReference("genes/negative/weaving")
		val WIND_CHARGED = PatchouliBookReference("genes/negative/wind_charged")
		val WITHER = PatchouliBookReference("genes/negative/wither")
	}
}