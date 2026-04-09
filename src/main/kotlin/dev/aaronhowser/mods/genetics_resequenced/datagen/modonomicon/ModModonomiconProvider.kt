package dev.aaronhowser.mods.genetics_resequenced.datagen.modonomicon

import com.klikli_dev.modonomicon.api.datagen.SingleBookSubProvider
import com.klikli_dev.modonomicon.api.datagen.book.BookModel
import com.klikli_dev.modonomicon.book.BookDisplayMode
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.modonomicon.categories.*
import java.util.function.BiConsumer

class ModModonomiconProvider(
	defaultLang: BiConsumer<String, String>,
) : SingleBookSubProvider("guide", GeneticsResequenced.MOD_ID, defaultLang) {

	override fun additionalSetup(book: BookModel): BookModel {
		return book
			.withCreativeTab(modLoc("creative_tab"))
			.withDisplayMode(BookDisplayMode.INDEX)
			.withBookTextOffsetX(3)
			.withBookTextOffsetY(3)
			.withBookTextOffsetWidth(-3)
	}

	override fun generateCategories() {
		this.add(GettingStartedCategoryProvider(this).generate())
		this.add(ItemsCategoryProvider(this).generate())
		this.add(BlocksCategoryProvider(this).generate())
		this.add(GenesCategoryProvider(this).generate())
		this.add(NegativeGenesCategoryProvider(this).generate())
	}

	override fun bookName(): String {
		return "Big Book of Genetics"
	}

	override fun bookTooltip(): String {
		return "A guide to all things genetic"
	}

	override fun registerDefaultMacros() {
		// Nothing
	}

}
