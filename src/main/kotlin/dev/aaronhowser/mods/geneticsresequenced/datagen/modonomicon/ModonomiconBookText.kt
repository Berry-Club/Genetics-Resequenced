package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon

import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookCategory
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBookEntry
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.provider.ModonomiconText

object ModonomiconBookText {

	fun doubleSpacedLines(vararg lines: String): String = ModonomiconText.paragraphs(*lines)

	fun lines(vararg lines: String): String = lines.joinToString("  \\\n")

	fun list(vararg items: String): String = items.joinToString("\n- ", prefix = "- ", postfix = "\n")

	fun major(text: String): String = colored("ff55ff", text)

	fun minor(text: String): String = colored("5555ff", text)

	fun bad(text: String): String = colored("ff5555", text)

	fun internalLink(entry: ModonomiconBookEntry, text: String): String {
		return ModonomiconText.entryLink(entry, text)
	}

	fun internalLink(entry: ModonomiconBookEntry, page: Int, text: String): String {
		return ModonomiconText.entryLink(entry, text, page.toString())
	}

	fun internalLink(category: ModonomiconBookCategory, text: String): String {
		return ModonomiconText.categoryLink(category, text)
	}

	fun italic(text: String): String = "_${text}_"

	fun bold(text: String): String = "**$text**"

	private fun colored(hexColor: String, text: String): String {
		return "[#]($hexColor)$text[#]()"
	}

	fun ModonomiconBookEntry.defaultWeightPages(vararg weights: String) {
		var firstWeightOnPage = 0
		while (firstWeightOnPage < weights.size) {
			val firstWeightOnNextPage = minOf(firstWeightOnPage + 6, weights.size)
			val pageWeights = weights
				.slice(firstWeightOnPage until firstWeightOnNextPage)
				.toTypedArray()

			val pageLines = if (firstWeightOnPage == 0) {
				arrayOf("These are the default chances. Modpacks may change them.", *pageWeights)
			} else {
				pageWeights
			}

			textPage(
				title = "Default Weights",
				text = lines(*pageLines)
			)

			firstWeightOnPage = firstWeightOnNextPage
		}
	}
}