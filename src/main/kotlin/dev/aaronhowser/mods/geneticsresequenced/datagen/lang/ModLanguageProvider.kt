package dev.aaronhowser.mods.geneticsresequenced.datagen.lang

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEffects
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
	output: PackOutput
) : LanguageProvider(output, GeneticsResequenced.MOD_ID, "en_us") {

	companion object {
		fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args)
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
		add(Other.SUPPORT_SLIME, "Support Slime")
		add(Other.BOOK_LANDING_TEXT, "Welcome to Genetics: Resequenced! This guide book will help you get started with the mod.")
		add(Other.ORACLE_INDEX_TITLE, "Genetics: Resequenced")
	}

	object Other {
		const val DELICATE_TOUCH = "enchantment.geneticsresequenced.delicate_touch"
		const val SUPPORT_SLIME = "entity.geneticsresequenced.support_slime"

		const val BOOK_LANDING_TEXT = "book.geneticsresequenced.landing_text"
		const val ORACLE_INDEX_TITLE = "oracle_index.title.geneticsresequenced"
	}

	object Keys {
		const val CATEGORY = "key.geneticsresequenced.category"
		const val DRAGONS_BREATH = "key.geneticsresequenced.dragons_breath"
		const val TELEPORT = "key.geneticsresequenced.teleport"
	}

}