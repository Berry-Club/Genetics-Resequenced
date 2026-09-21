package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.minor
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBook
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBookCategory
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.book_element.PatchouliBookEntry
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.keybind
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.list
import dev.aaronhowser.mods.patchoulidatagen.provider.PatchouliBookProvider.Companion.internalLink
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object GenesPatchouliCategory {

	private lateinit var registries: HolderLookup.Provider

	lateinit var bookCategory: PatchouliBookCategory
		private set

	lateinit var milky: PatchouliBookEntry
		private set

	lateinit var itemAttractionField: PatchouliBookEntry
		private set

	lateinit var noFallDamage: PatchouliBookEntry
		private set

	lateinit var enderDragonHealth: PatchouliBookEntry
		private set

	lateinit var scareCreepers: PatchouliBookEntry
		private set

	lateinit var wooly: PatchouliBookEntry
		private set

	lateinit var stepAssist: PatchouliBookEntry
		private set

	lateinit var thorns: PatchouliBookEntry
		private set

	lateinit var teleport: PatchouliBookEntry
		private set

	lateinit var xpAttractionField: PatchouliBookEntry
		private set

	lateinit var scareSkeletons: PatchouliBookEntry
		private set

	fun generate(book: PatchouliBook, registries: HolderLookup.Provider) {
		this.registries = registries
		bookCategory = book.category(
			saveName = "genes",
			name = "Genes",
			description = "All the Genes in the mod",
			icon = ModItems.PLASMID.get()
		) {
			sortNumber = 3
		}


		addEntries(book)
	}

	private fun addEntries(book: PatchouliBook) {
		book.entry(
			saveName = "bioluminescence",
			category = bookCategory,
			name = "Bioluminescence",
			icon = Items.GLOWSTONE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Bioluminescence")} Gene causes entities to ${minor("spawn light sources")} when in the dark.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "chatterbox",
			category = bookCategory,
			name = "Chatterbox",
			icon = Items.NOTE_BLOCK
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Chatterbox")} Gene causes your chat messages to be ${minor("read by the game's narrator")}.",
					"This only applies to players within 64 blocks of you, and can be disabled in the client config.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "chilling",
			category = bookCategory,
			name = "Chilling",
			icon = Items.POWDER_SNOW_BUCKET
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Chilling")} Gene makes your melee attacks have a chance of ${minor("inflicting a buildup of Freezing")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "claws",
			category = bookCategory,
			name = "Claws",
			icon = Items.STONE_SWORD
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Claws")} Gene causes entities to have a chance to ${minor("inflict Bleeding on empty-handed melee attacks")}.",
					"When mutated into Claws 2, the chance is doubled.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "climb_walls",
			category = bookCategory,
			name = "Climb Walls",
			icon = Items.STRING
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Climb Walls")} Gene allows players to ${minor("climb walls")}.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "dragons_breath",
			category = bookCategory,
			name = "Dragon's Breath",
			icon = Items.DRAGON_EGG
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Dragon's Breath")} Gene allows players to ${minor("shoot a dragon fireball")} when the Dragon's Breath key (currently bound to ${keybind("key.geneticsresequenced.dragons_breath")}) is used.",
					"This has a configurable cooldown, with the default being 15 seconds.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "eat_grass",
			category = bookCategory,
			name = "Eat Grass",
			icon = Items.GRASS_BLOCK
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Eat Grass")} Gene allows players to ${minor("right-click Grass Blocks to regain hunger")}.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "efficiency",
			category = bookCategory,
			name = "Efficiency",
			icon = Items.GOLDEN_PICKAXE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Efficiency")} Gene ${minor("makes players mine faster")}. This stacks with enchantments and Haste.",
					"Can be mutated into Efficiency 4.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "emerald_heart",
			category = bookCategory,
			name = "Emerald Heart",
			icon = Items.EMERALD
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Emerald Heart")} Gene causes entities to ${minor("drop an Emerald upon death")}.",
					"For players, this has a configurable cooldown, with the default being 60 seconds.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		enderDragonHealth = book.entry(
			saveName = "ender_dragon_health",
			category = bookCategory,
			name = "Ender Dragon Health",
			icon = ModItems.DRAGON_HEALTH_CRYSTAL.get()
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Ender Dragon Health")} Gene allows players to ${minor("completely negate damage")} while a ${internalLink(ItemsPatchouliCategory.dragonHealthCrystal, "Dragon Health Crystal")} is in their inventory.",
					"Any damage they would take is instead dealt to the Crystal's durability.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "explosive_exit",
			category = bookCategory,
			name = "Explosive Exit",
			icon = Items.TNT
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Explosive Exit")} Gene causes entities to ${minor("explode upon death")}.",
					"If a player has this Gene, they only explode if they have 5 Gunpowder in their inventory, which is removed upon detonation.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "fire_proof",
			category = bookCategory,
			name = "Fire Proof",
			icon = Items.FLINT_AND_STEEL
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Fire Proof")} Gene makes entities ${minor("completely immune to burning damage")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "flight",
			category = bookCategory,
			name = "Flight",
			icon = Items.ELYTRA
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Flight")} Gene ${minor("allows players to fly")} by double-tapping jump.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			textPage(
				text = "By default, this Gene requires the following other Genes:" + list(
					internalLink(teleport, "Teleport"),
					internalLink(stepAssist, "Step Assist"),
					internalLink(noFallDamage, "No Fall Damage")
				)
			)
		}

		book.entry(
			saveName = "haste",
			category = bookCategory,
			name = "Haste",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Haste")} Gene gives entities the ${minor("Haste effect")}.",
					"Can be mutated into Haste 2.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "infinity",
			category = bookCategory,
			name = "Infinity",
			icon = Items.BOW
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Infinity")} Gene allows players to ${minor("fire Arrows when none are in their inventory")}.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "invisible",
			category = bookCategory,
			name = "Invisible",
			icon = Items.GOLDEN_APPLE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Invisible")} Gene gives entities the ${minor("Invisible effect")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		itemAttractionField = book.entry(
			saveName = "item_attraction_field",
			category = bookCategory,
			name = "Item Attraction Field",
			icon = Items.IRON_INGOT
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Item Attraction Field")} Gene causes players to ${minor("grab items from a much larger distance")}.",
					"This Gene is disabled when the player has an active ${internalLink(ItemsPatchouliCategory.antiFieldOrb, "Anti-Field Orb")}, is near an active ${internalLink(BlocksPatchouliCategory.antiFieldBlock, "Anti-Field Block")}, or when sneaking.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "johnny",
			category = bookCategory,
			name = "Johnny",
			icon = Items.IRON_AXE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Johnny")} Gene ${minor("increases your attack damage when using Axes")}.",
					"This gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "jump_boost",
			category = bookCategory,
			name = "Jump Boost",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Jump Boost")} Gene gives entities the ${minor("Jump Boost effect")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "keep_inventory",
			category = bookCategory,
			name = "Keep Inventory",
			icon = Items.SKELETON_SKULL
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Keep Inventory")} Gene causes players to ${minor("keep their inventory upon death")}.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "knockback",
			category = bookCategory,
			name = "Knockback",
			icon = Items.PISTON
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Knockback")} Gene ${minor("increases your attack knockback")}.",
					"This gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "lay_egg",
			category = bookCategory,
			name = "Lay Egg",
			icon = Items.EGG
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Lay Egg")} Gene causes entities to ${minor("spawn an Egg")} every so often.",
					"This time is configurable, but defaults to once every 5 minutes. ",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "luck",
			category = bookCategory,
			name = "Luck",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Luck")} Gene gives entities the ${minor("Luck effect")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "meaty",
			category = bookCategory,
			name = "Meaty",
			icon = Items.PORKCHOP
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Meaty")} Gene allows the entity to be ${minor("Sheared for a Raw Porkchop")}.",
					"If a player has it, they can ${minor("shear themselves")} by sneak right-clicking with Shears.",
					"This has a configurable cooldown, with the default being 1 minute."
				)
			)

			textPage(
				text = doubleSpacedLines(
					"This can be mutated into Meaty 2, which causes the entity to spawn a Cooked Porkchop once every 5 minutes.",
					"Both Meaty and Meaty 2 ${minor("can be given to mobs")}."
				)
			)
		}

		milky = book.entry(
			saveName = "milky",
			category = bookCategory,
			name = "Milky",
			icon = Items.MILK_BUCKET
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Milky")} Gene allows the entity to be ${minor("milked with a Bucket")}. If a player has it, they can ${minor("milk themselves")} by sneak right-clicking with a Bucket.",
					"This has a configurable cooldown, with the default being 1 tick.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "mob_sight",
			category = bookCategory,
			name = "Mob Sight",
			icon = Items.GOLDEN_CARROT
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Mob Sight")} Gene occasionally ${minor("gives all nearby mobs the Glowing effect")}.",
					"Both the cooldown and radius are configurable, defaulting to 1 second and 32 blocks respectively.",
					"This gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "more_hearts",
			category = bookCategory,
			name = "More Hearts",
			icon = Items.ENCHANTED_GOLDEN_APPLE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("More Hearts")} Gene gives entities ${minor("10 extra hearts")}. If the Gene is mutated, they instead get ${minor("20")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "night_vision",
			category = bookCategory,
			name = "Night Vision",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Night Vision")} Gene gives entities the ${minor("Night Vision effect")}.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		noFallDamage = book.entry(
			saveName = "no_fall_damage",
			category = bookCategory,
			name = "No Fall Damage",
			icon = Items.FEATHER
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("No Fall Damage")} Gene ${minor("negates all fall damage")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "no_hunger",
			category = bookCategory,
			name = "No Hunger",
			icon = Items.BREAD
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("No Hunger")} Gene ${minor("prevents your hunger from draining")} below a configurable point, which defaults to half..",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "photosynthesis",
			category = bookCategory,
			name = "Photosynthesis",
			icon = Items.SUNFLOWER
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Photosynthesis")} Gene ${minor("feeds the player when they're in direct sunlight")}.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			textPage(
				text = "By default, this Gene requires ${internalLink(thorns, "Thorns")}."
			)
		}

		book.entry(
			saveName = "poison_immunity",
			category = bookCategory,
			name = "Poison Immunity",
			icon = Items.FERMENTED_SPIDER_EYE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Poison Immunity")} Gene makes entities ${minor("immune to Poison")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "regeneration",
			category = bookCategory,
			name = "Regeneration",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Regeneration")} Gene gives entities the ${minor("Regeneration effect")}.",
					"Can be mutated into Regeneration 4.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "resistance",
			category = bookCategory,
			name = "Resistance",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Resistance")} Gene gives entities the ${minor("Resistance effect")}.",
					"This Gene can be ${minor("mutated into Resistance 2")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		scareCreepers = book.entry(
			saveName = "scare_creepers",
			category = bookCategory,
			name = "Scare Creepers",
			icon = Items.CREEPER_HEAD
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Scare Creepers")} Gene makes it so that ${minor("Creepers will run away from you")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		scareSkeletons = book.entry(
			saveName = "scare_skeletons",
			category = bookCategory,
			name = "Scare Skeletons",
			icon = Items.SKELETON_SKULL
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Scare Skeletons")} Gene makes it so that ${minor("Skeletons will run away from you")}.",
					"This Gene ${minor("can be given to mobs")}.."
				)
			)
		}

		book.entry(
			saveName = "scare_spiders",
			category = bookCategory,
			name = "Scare Spiders",
			icon = Items.SPIDER_EYE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Scare Spiders")} Gene makes it so that ${minor("Spiders will run away from you")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(
				text = "By default, this Gene requires ${internalLink(scareSkeletons, "Scare Skeletons")}."
			)
		}

		book.entry(
			saveName = "scare_zombies",
			category = bookCategory,
			name = "Scare Zombies",
			icon = Items.ZOMBIE_HEAD
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Scare Zombies")} Gene makes it so that ${minor("Zombies will run away from you")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			textPage(
				text = "By default, this Gene requires ${internalLink(scareCreepers, "Scare Creepers")}."
			)
		}

		book.entry(
			saveName = "shoot_fireballs",
			category = bookCategory,
			name = "Shoot Fireballs",
			icon = Items.BLAZE_ROD
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Shoot Fireballs")} Gene allows players to ${minor("shoot small fireballs")} when a Blaze Rod is used.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "slimy_death",
			category = bookCategory,
			name = "Slimy Death",
			icon = Items.SLIME_BALL
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Slimy Death")} Gene makes it so, ${minor("upon the player dying, they are instantly revived and several friendly Support Slimes spawn")}.",
					"This has a configurable cooldown, with the default being 5 minutes.",
					"This Gene ${bad("cannot be given to mobs")}"
				)
			)
		}

		book.entry(
			saveName = "speed",
			category = bookCategory,
			name = "Speed",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Speed")} Gene gives entities the ${minor("Speed effect")}.",
					"This Gene can be ${minor("mutated into Speed 2")} and ${minor("Speed 4")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		stepAssist = book.entry(
			saveName = "step_assist",
			category = bookCategory,
			name = "Step Assist",
			icon = Items.COBBLESTONE_STAIRS
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Step Assist")} Gene allows players to ${minor("walk up single blocks")} as if they were stairs.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "strength",
			category = bookCategory,
			name = "Strength",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Strength")} Gene gives entities the ${minor("Strength effect")}.",
					"This Gene can be ${minor("mutated into Strength 2")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		teleport = book.entry(
			saveName = "teleport",
			category = bookCategory,
			name = "Teleport",
			icon = Items.ENDER_PEARL
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Teleport")} Gene allows players to ${minor("teleport forward")} when the Teleport key (currently bound to ${keybind("key.geneticsresequenced.teleport")}) is used.",
					"This has a configurable cooldown, with the default being 1 second. The distance is also configurable, defaulting to 10 blocks.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}

		thorns = book.entry(
			saveName = "thorns",
			category = bookCategory,
			name = "Thorns",
			icon = Items.CACTUS
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Thorns")} Gene gives entities a chance to ${minor("damage melee attackers")}. This uses up some hunger.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "water_breathing",
			category = bookCategory,
			name = "Water Breathing",
			icon = Items.POTION
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Water Breathing")} Gene gives entities the ${minor("Water Breathing effect")}.",
					".",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "wither_hit",
			category = bookCategory,
			name = "Wither Hit",
			icon = Items.WITHER_ROSE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Wither Hit")} Gene causes entities to ${minor("inflict Wither when melee attacking")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		book.entry(
			saveName = "wither_proof",
			category = bookCategory,
			name = "Wither Proof",
			icon = Items.WITHER_SKELETON_SKULL
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Wither Proof")} Gene makes entities ${minor("immune to Wither")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		wooly = book.entry(
			saveName = "wooly",
			category = bookCategory,
			name = "Wooly",
			icon = Items.WHITE_WOOL
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Wooly")} Gene allows the entity to be ${minor("sheared for Wool")}. If a player has it, they can ${minor("shear themselves")} by sneak right-clicking with Shears.",
					"This has a configurable cooldown, with the default being 1 minute.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)
		}

		xpAttractionField = book.entry(
			saveName = "xp_attraction_field",
			category = bookCategory,
			name = "XP Attraction Field",
			icon = Items.EXPERIENCE_BOTTLE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("XP Attraction Field")} Gene causes players to ${minor("grab XP Orbs from a much larger distance")}.",
					"This Gene is disabled when the player has an active ${internalLink(ItemsPatchouliCategory.antiFieldOrb, "Anti-Field Orb")}, is near an active ${internalLink(BlocksPatchouliCategory.antiFieldBlock, "Anti-Field Block")}, or when sneaking.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)
		}


	}

}