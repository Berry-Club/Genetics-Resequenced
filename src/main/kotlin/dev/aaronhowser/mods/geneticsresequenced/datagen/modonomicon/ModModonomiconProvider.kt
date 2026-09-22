package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon

import com.klikli_dev.modonomicon.book.BookDisplayMode
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.categories.*
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.book_element.ModonomiconBook
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.dsl.modonomiconBook
import dev.aaronhowser.mods.patchoulidatagen.modonomicon.provider.ModonomiconBookProvider
import java.util.function.BiConsumer

class ModModonomiconProvider(
	defaultLanguage: BiConsumer<String, String>
) : ModonomiconBookProvider(
	bookId = "guide",
	namespace = GeneticsResequenced.MOD_ID,
	name = "Big Book of Genetics",
	tooltip = "A guide to all things genetic",
	defaultLanguage = defaultLanguage
) {

	override fun buildBook(): ModonomiconBook = modonomiconBook(
		namespace = GeneticsResequenced.MOD_ID,
		saveName = "guide",
		name = "Big Book of Genetics",
		tooltip = "A guide to all things genetic",
		registries = registries()
	) {
		description = "A guide to all things genetic"
		creativeTab = modLoc("creative_tab")
		displayMode = BookDisplayMode.INDEX
		bookTextOffsetX = 3
		bookTextOffsetY = 3
		bookTextOffsetWidth = -3

		GettingStartedModonomiconCategory.generate(this, registries())
		BlocksModonomiconCategory.generate(this, registries())
		ItemsModonomiconCategory.generate(this, registries())
		GenesModonomiconCategory.generate(this, registries())
		NegativeGenesModonomiconCategory.generate(this, registries())
		PlaguesModonomiconCategory.generate(this, registries())
	}
}