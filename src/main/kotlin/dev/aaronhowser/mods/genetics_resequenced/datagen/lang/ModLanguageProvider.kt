package dev.aaronhowser.mods.genetics_resequenced.datagen.lang

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import dev.aaronhowser.mods.genetics_resequenced.registry.ModEffects
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
	output: PackOutput
) : LanguageProvider(output, GeneticsResequenced.MOD_ID, "en_us") {

	companion object {
		fun String.toComponent(vararg args: Any): MutableComponent = Component.translatable(this, *args)
	}

	override fun addTranslations() {

		ModItemLang.add(this)
		ModMessageLang.add(this)
		ModTooltipLang.add(this)
		ModConfigLang.add(this)
		ModInfoLang.add(this)
		ModGeneLang.add(this)
		ModAdvancementLang.add(this)
		ModRecipeLang.add(this)

		addEffect(ModEffects.BLEED, "Bleed")
		addEffect(ModEffects.SUBSTRATE, "Substrate")
		addEffect(ModEffects.CELL_GROWTH, "Cell Growth")
		addEffect(ModEffects.MUTATION, "Mutation")
		addEffect(ModEffects.VIRAL_AGENTS, "Viral Agents")
		addEffect(ModEffects.PANACEA, "Panacea")
		addEffect(ModEffects.ZOMBIFY_VILLAGER, "Zombify Villager")

		addBlock(ModBlocks.ANTI_FIELD_BLOCK, "Anti-Field Block")
		addBlock(ModBlocks.BIOLUMINESCENCE_BLOCK, "Bioluminescence Glow")
		addBlock(ModBlocks.WEB_DEFENSE_BLOCK, "Web Defense Cobweb")
		addBlock(ModBlocks.CELL_ANALYZER, "Cell Analyzer")
		addBlock(ModBlocks.COAL_GENERATOR, "Coal Generator")
		addBlock(ModBlocks.DNA_DECRYPTOR, "DNA Decryptor")
		addBlock(ModBlocks.DNA_EXTRACTOR, "DNA Extractor")
		addBlock(ModBlocks.BLOOD_PURIFIER, "Blood Purifier")
		addBlock(ModBlocks.INCUBATOR, "Incubator")
		addBlock(ModBlocks.ADVANCED_INCUBATOR, "Advanced Incubator")
		addBlock(ModBlocks.PLASMID_INFUSER, "Plasmid Infuser")
		addBlock(ModBlocks.PLASMID_INJECTOR, "Plasmid Injector")

		add(Keys.CATEGORY, "Genetics: Resequenced")
		add(Keys.DRAGONS_BREATH, "Dragon's Breath")
		add(Keys.TELEPORT, "Teleport")

		add(Other.DELICATE_TOUCH, "Delicate Touch")
		add(Other.SURGICAL_PRECISION, "Surgical Precision")
		add(Other.SUPPORT_SLIME, "Support Slime")
		add(Other.BOOK_LANDING_TEXT, "Welcome to Genetics: Resequenced! This guide book will help you get started with the mod.")
		add(Other.ORACLE_INDEX_TITLE, "Genetics: Resequenced")
	}

	object Other {
		const val DELICATE_TOUCH = "enchantment.genetics_resequenced.delicate_touch"
		const val SURGICAL_PRECISION = "enchantment.genetics_resequenced.surgical_precision"
		const val SUPPORT_SLIME = "entity.genetics_resequenced.support_slime"

		const val BOOK_LANDING_TEXT = "book.genetics_resequenced.landing_text"
		const val ORACLE_INDEX_TITLE = "oracle_index.title.genetics_resequenced"
	}

	object Keys {
		const val CATEGORY = "key.category.genetics_resequenced.genetics_resequenced"
		const val DRAGONS_BREATH = "key.genetics_resequenced.dragons_breath"
		const val TELEPORT = "key.genetics_resequenced.teleport"
	}

}
