package dev.aaronhowser.mods.geneticsresequenced.datagen.lang

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.translationKey
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.resources.ResourceKey

object ModInfoLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(ORGANIC_MATTER, "Acquired by clicking a %1\$s with a Scraper.")
			add(ORGANIC_MATTER_EMPTY, "Acquired by clicking an entity with a Scraper.\n\nFor more information, look at one that actually has an entity set 😉")
			add(MOB_GENE_ONE, "%1\$s has these Genes:")
			add(MOB_GENE_TWO, "\n%d%% of %2\$s")
			add(REQUIRED_GENES, "Required Genes:")

			fun addGeneInfo(gene: ResourceKey<Gene>, info: String) {
				add("info.${gene.translationKey}", info)
			}

			addGeneInfo(ModGenes.BASIC, "The most basic Gene, which does nothing, and can't even be injected.\n\nIt can be used as a crafting ingredient, as it's worth 1 DNA Point in the Plasmid Infuser.")
			addGeneInfo(ModGenes.CLAWS_TWO, "Doubles the chances of inflicting Bleeding on hit.")
			addGeneInfo(ModGenes.EFFICIENCY_FOUR, "Increases your mining speed as if you were using an Efficiency IV tool.\n\nStacks with Haste, as well as the actual Efficiency enchantment!")
			addGeneInfo(ModGenes.FLIGHT, "Gives you Creative mode style flight.")
			addGeneInfo(ModGenes.HASTE_TWO, "Gives you the Haste II potion effect.")
			addGeneInfo(ModGenes.MEATY_TWO, "Causes you to occasionally drop a cooked Porkchop.")
			addGeneInfo(ModGenes.PHOTOSYNTHESIS, "Slowly feeds you when in direct sunlight.")
			addGeneInfo(ModGenes.REGENERATION_FOUR, "Gives you the Regeneration IV potion effect.")
			addGeneInfo(ModGenes.RESISTANCE_TWO, "Give you the Resistance II potion effect.")
			addGeneInfo(ModGenes.SPEED_FOUR, "Give you the Speed IV potion effect.")
			addGeneInfo(ModGenes.SPEED_TWO, "Give you the Speed II potion effect.")
			addGeneInfo(ModGenes.STRENGTH_TWO, "Give you the Strength II potion effect.")
			addGeneInfo(ModGenes.SCARE_ZOMBIES, "Makes Zombies run away from you.")
			addGeneInfo(ModGenes.SCARE_SPIDERS, "Makes Spiders and Cave Spiders run away from you.")
			addGeneInfo(ModGenes.BIOLUMINESCENCE, "Makes you leave behind a trail of light, which lasts a short while.")
			addGeneInfo(ModGenes.CLAWS, "Has a chance of inflicting Bleeding on hit.\n\nBleeding deals damage over time.")
			addGeneInfo(ModGenes.DRAGON_BREATH, "Allows you to use the \"Dragon's Breath\" keybind to fire a dragon fireball.")
			addGeneInfo(ModGenes.EAT_GRASS, "Allows you to right-click Grass Blocks to regain hunger.")
			addGeneInfo(ModGenes.EFFICIENCY, "Increases your mining speed as if you were using an Efficiency tool.\n\nStacks with Haste, as well as the actual Efficiency enchantment!")
			addGeneInfo(ModGenes.EMERALD_HEART, "Spawns an Emerald when you die.")
			addGeneInfo(ModGenes.ENDER_DRAGON_HEALTH, "Blocks damage if you're holding a Dragon Health Crystal.\n\nDamage is instead dealt to the Crystal's durability.")
			addGeneInfo(ModGenes.EXPLOSIVE_EXIT, "Makes you explode when you die, if you're holding at least 5 Gunpowder.\n\nEntities with the Gene do not need the Gunpowder.")
			addGeneInfo(ModGenes.FIRE_PROOF, "Makes you immune to fire damage, and immediately extinguishes you.")
			addGeneInfo(ModGenes.LAVA_PROOF, "Makes you immune to direct lava damage")
			addGeneInfo(ModGenes.HASTE, "Gives you the Haste potion effect.")
			addGeneInfo(ModGenes.INFINITY, "Allows you to use Bows without any Arrows in your inventory.")
			addGeneInfo(ModGenes.INVISIBLE, "Gives you the Invisibility potion effect.")
			addGeneInfo(ModGenes.ITEM_MAGNET, "Makes you pick up all nearby items.\n\nDisables when sneaking, when holding an active Anti-Field Orb, or near an active Anti-Field Block.")
			addGeneInfo(ModGenes.JUMP_BOOST, "Gives you the Jump Boost potion effect.")
			addGeneInfo(ModGenes.KEEP_INVENTORY, "Makes you keep your inventory when you die.")
			addGeneInfo(ModGenes.LAY_EGG, "Makes you lay an Egg occasionally.")
			addGeneInfo(ModGenes.LUCK, "Gives you the Luck potion effect.")
			addGeneInfo(ModGenes.MEATY, "Allows you to be sheared for a raw Porkchop.")
			addGeneInfo(ModGenes.MILKY, "Allows you to be milked with a Bucket.")
			addGeneInfo(ModGenes.MOB_SIGHT, "Gives all nearby entities the Glowing potion effect.")
			addGeneInfo(ModGenes.MORE_HEARTS, "Gives you 10 additional hearts.")
			addGeneInfo(ModGenes.MORE_HEARTS_TWO, "Gives you 10 MORE additional hearts.")
			addGeneInfo(ModGenes.NIGHT_VISION, "Gives you the Night Vision potion effect.")
			addGeneInfo(ModGenes.NO_FALL_DAMAGE, "Makes you immune to fall damage.")
			addGeneInfo(ModGenes.NO_HUNGER, "Prevents your hunger from going below halfway.")
			addGeneInfo(ModGenes.POISON_IMMUNITY, "Automatically removes Poison.")
			addGeneInfo(ModGenes.REGENERATION, "Gives you the Regeneration potion effect.")
			addGeneInfo(ModGenes.RESISTANCE, "Gives you the Resistance potion effect.")
			addGeneInfo(ModGenes.SCARE_CREEPERS, "Makes Creepers run away from you.")
			addGeneInfo(ModGenes.SCARE_SKELETONS, "Makes Skeletons run away from you.")
			addGeneInfo(ModGenes.SHOOT_FIREBALLS, "Lets you shoot Fire Charges when you right-click Blaze Rods.")
			addGeneInfo(ModGenes.SLIMY_DEATH, "When you die, cancels it and spawns some Support Slimes to help you fight.\n\nHas a long cooldown.")
			addGeneInfo(ModGenes.SPEED, "Gives you the Speed potion effect.")
			addGeneInfo(ModGenes.STEP_ASSIST, "Lets you step up full blocks.")
			addGeneInfo(ModGenes.STRENGTH, "Gives you the Strength potion effect.")
			addGeneInfo(ModGenes.TELEPORT, "Allows you to use the \"Teleport\" keybind to teleport a short distance where you're looking.")
			addGeneInfo(ModGenes.THORNS, "If you take damage while wearing either no Chestplate or a Leather Chestplate, there's a chance of reflecting some of the damage back.")
			addGeneInfo(ModGenes.WALL_CLIMBING, "Lets you climb walls like a Spider.")
			addGeneInfo(ModGenes.WATER_BREATHING, "Keeps your air meter full.")
			addGeneInfo(ModGenes.WITHER_HIT, "Melee attacks inflict the Wither potion effect.")
			addGeneInfo(ModGenes.WITHER_PROOF, "Makes you immune to the Wither potion effect.")
			addGeneInfo(ModGenes.WOOLY, "Lets you be sheared for Wool.")
			addGeneInfo(ModGenes.XP_MAGNET, "Makes you pick up all nearby xp.\n\nDisables when sneaking, when holding an active Anti-Field Orb, or near an active Anti-Field Block.")
			addGeneInfo(ModGenes.BLINDNESS, "Inflicts the Blindness potion effect.")
			addGeneInfo(ModGenes.CURSED, "Inflicts the Cursed potion effect.")
			addGeneInfo(ModGenes.FLAMBE, "Constantly lights you on fire")
			addGeneInfo(ModGenes.HUNGER, "Inflicts the Hunger potion effect.")
			addGeneInfo(ModGenes.LEVITATION, "Inflicts the Levitation potion effect.")
			addGeneInfo(ModGenes.MINING_FATIGUE, "Inflicts the Mining Fatigue potion effect.")
			addGeneInfo(ModGenes.NAUSEA, "Inflicts the Nausea potion effect.")
			addGeneInfo(ModGenes.POISON, "Inflicts the Poison potion effect.")
			addGeneInfo(ModGenes.POISON_FOUR, "Inflicts the Poison IV potion effect.")
			addGeneInfo(ModGenes.SLOWNESS, "Inflicts the Slowness potion effect.")
			addGeneInfo(ModGenes.SLOWNESS_SIX, "Inflicts the Slowness IV potion effect.")
			addGeneInfo(ModGenes.SLOWNESS_FOUR, "Inflicts the Slowness VI potion effect.")
			addGeneInfo(ModGenes.WEAKNESS, "Inflicts the Weakness potion effect.")
			addGeneInfo(ModGenes.WITHER, "Inflicts the Wither potion effect.")
			addGeneInfo(ModGenes.BLACK_DEATH, "Instantly kills.")
			addGeneInfo(ModGenes.GREEN_DEATH, "Slowly kills Creepers.")
			addGeneInfo(ModGenes.WHITE_DEATH, "Slowly kills monsters.")
			addGeneInfo(ModGenes.GRAY_DEATH, "Slowly kills ageable mobs.")
			addGeneInfo(ModGenes.UN_UNDEATH, "Slowly kills the undead.")
			addGeneInfo(ModGenes.CHATTERBOX, "Your chat messages are automatically read by the narrator (within 64 blocks).\n\nCan be disabled in the client config.")
			addGeneInfo(ModGenes.CRINGE, "Makes you a Discord moderator.\n\n(UwU-fies your outgoing chat messages, and sets your language to LOLCAT)")
			addGeneInfo(ModGenes.KNOCKBACK, "Increases the knockback of your attacks.")
			addGeneInfo(ModGenes.JOHNNY, "Deal more damage using Axes")
			addGeneInfo(ModGenes.CHILLING, "Has a chance of inflicting freezing damage on hit.")
			addGeneInfo(ModGenes.REACHING, "Increases your reach by 1.25 times.")
			addGeneInfo(ModGenes.BOUNTIFUL, "Mobs with this Gene will drop loot as if they were killed with Looting one level higher than it was.")
			addGeneInfo(ModGenes.BOUNTIFUL_TWO, "Mobs with this Gene will drop loot as if they were killed with Looting two levels higher than it was.")
			addGeneInfo(ModGenes.EXPERIENCED, "Mobs with this Gene will drop more XP when killed.")
			addGeneInfo(ModGenes.FERTILE, "Mobs with this Gene will produce more offspring when bred.\n\nEach parent with this Gene adds +1 child.")
			addGeneInfo(ModGenes.FRENZIED, "Mobs with this Gene will attack everything nearby.")
			addGeneInfo(ModGenes.PLACID, "Mobs with this Gene will not be able to target anything.")
			addGeneInfo(ModGenes.WEB_DEFENSE, "When hurt, has a chance to spawn a temporary web on the attacker.")
			addGeneInfo(ModGenes.WEB_WALKER, "Allows walking through webs without being slowed.")
		}

	}

	const val ORGANIC_MATTER = "info.geneticsresequenced.organic_matter"
	const val ORGANIC_MATTER_EMPTY = "info.geneticsresequenced.organic_matter.empty"
	const val MOB_GENE_ONE = "info.geneticsresequenced.mob_gene.line1"
	const val MOB_GENE_TWO = "info.geneticsresequenced.mob_gene.line2"
	const val REQUIRED_GENES = "info.geneticsresequenced.requires_genes"

}