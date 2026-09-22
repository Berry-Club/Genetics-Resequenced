package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.bad
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.defaultWeightPages
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.major
import dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli.ModPatchouliBookProvider.Companion.minor
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.patchoulidatagen.patchouli.book_element.PatchouliBook
import dev.aaronhowser.mods.patchoulidatagen.patchouli.book_element.PatchouliBookCategory
import dev.aaronhowser.mods.patchoulidatagen.patchouli.book_element.PatchouliBookEntry
import dev.aaronhowser.mods.patchoulidatagen.patchouli.provider.PatchouliBookProvider.Companion.doubleSpacedLines
import dev.aaronhowser.mods.patchoulidatagen.patchouli.provider.PatchouliBookProvider.Companion.internalLink
import dev.aaronhowser.mods.patchoulidatagen.patchouli.provider.PatchouliBookProvider.Companion.keybind
import dev.aaronhowser.mods.patchoulidatagen.patchouli.provider.PatchouliBookProvider.Companion.list
import net.minecraft.core.HolderLookup
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions

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
			saveName = "basic",
			category = bookCategory,
			name = "Basic",
			icon = ModItems.DNA_HELIX.get()
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Basic")} Gene is filler DNA and ${bad("cannot be injected")}.",
					"In the ${internalLink(BlocksPatchouliCategory.plasmidInfuser, "Plasmid Infuser")}, Basic Helices contribute 1 DNA Point. Mobs without a listed Gene pool always provide Basic."
				)
			)

			defaultWeightPages(
				"3/8 - Allay", "2/7 - Axolotl", "4/8 - Bat", "5/8 - Bee", "5/12 - Blaze", "2/11 - Breeze",
				"5/7 - Cat", "7/17 - Cave Spider", "5/10 - Chicken", "5/7 - Cod", "5/15 - Cow", "5/8 - Creeper",
				"7/11 - Dolphin", "9/18 - Donkey", "5/9 - Drowned", "5/9 - Enderman", "4/7 - Evoker", "5/9 - Fox",
				"5/8 - Frog", "5/9 - Ghast", "5/13 - Glow Squid", "5/15 - Goat", "5/12 - Guardian", "5/10 - Hoglin",
				"7/16 - Horse", "5/9 - Husk", "5/19 - Iron Golem", "5/8 - Llama", "5/12 - Magma Cube", "5/10 - Ocelot",
				"5/17 - Parrot", "5/11 - Phantom", "5/7 - Pig", "5/9 - Piglin Brute", "5/8 - Piglin", "5/7 - Pillager",
				"5/16 - Polar Bear", "5/16 - Pufferfish", "5/20 - Rabbit", "5/17 - Ravager", "5/8 - Salmon", "5/12 - Sheep",
				"3/11 - Shulker", "3/11 - Silverfish", "5/6 - Skeleton", "5/16 - Slime", "5/7 - Snow Golem", "4/15 - Spider",
				"5/7 - Squid", "5/8 - Stray", "5/11 - Strider", "5/9 - Tadpole", "5/7 - Tropical Fish", "5/7 - Turtle",
				"5/9 - Villager", "5/10 - Vindicator", "5/7 - Witch", "5/7 - Wither Skeleton", "5/15 - Wither", "5/11 - Wolf",
				"5/12 - Zoglin", "5/9 - Zombie Villager", "7/8 - Zombie", "5/11 - Zombified Piglin"
			)
		}

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

			defaultWeightPages(
				"3/12 - Blaze",
				"4/13 - Glow Squid",
				"4/12 - Magma Cube"
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

			defaultWeightPages(
				"6/17 - Parrot"
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

			defaultWeightPages(
				"4/9 - Husk",
				"2/7 - Snow Golem"
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
					"The ${major("Claws")} Gene adds 4 hearts of unarmed damage and a 33% chance to inflict Bleeding on empty-handed hits.",
					"Claws II adds another 4 hearts and raises the Bleeding chance to 66%. Bleeding deals half a heart each second.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/16 - Polar Bear"
			)
		}

		book.entry(
			saveName = "climb_walls",
			category = bookCategory,
			name = "Wall Climbing",
			icon = Items.STRING
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Wall Climbing")} Gene lets players climb walls. Sneak to stop rising and cling to ceilings.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"2/17 - Cave Spider",
				"2/15 - Spider"
			)
		}

		book.entry(
			saveName = "dragons_breath",
			category = bookCategory,
			name = "Dragon Breath",
			icon = Items.DRAGON_EGG
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Dragon Breath")} Gene lets players shoot a Dragon Fireball with its keybind, which is unset by default.",
					"This has a configurable cooldown, with the default being 15 seconds.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"6/11 - Ender Dragon"
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
					"The ${major("Eat Grass")} Gene lets players eat Grass Blocks with an empty hand, restoring 1 hunger and 1 saturation. Grass and Mycelium become Dirt; Nylium becomes Netherrack.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/15 - Cow",
				"3/15 - Goat",
				"4/13 - Mooshroom",
				"3/12 - Sheep"
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

			defaultWeightPages(
				"1/11 - Silverfish"
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
					"The ${major("Emerald Heart")} Gene causes mobs to ${minor("drop an Emerald upon death")}.",
					"Players with it may make special sounds when chatting.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/7 - Evoker",
				"2/7 - Pillager",
				"2/9 - Villager",
				"2/10 - Vindicator",
				"4/9 - Zombie Villager"
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

			defaultWeightPages(
				"3/11 - Ender Dragon"
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

			defaultWeightPages(
				"3/8 - Creeper"
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
					"The ${major("Fire Proof")} Gene makes entities ${minor("immune to fire damage")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"1/12 - Blaze",
				"3/12 - Magma Cube",
				"4/11 - Strider",
				"3/11 - Zombified Piglin"
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

			defaultWeightPages(
				"2/11 - Ender Dragon",
				"3/15 - Wither"
			)
		}

		book.entry(
			saveName = "haste",
			category = bookCategory,
			name = "Haste",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.DIG_SPEED)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Haste")} Gene gives entities the ${minor("Haste effect")}.",
					"Can be mutated into Haste 2.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/11 - Silverfish"
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
					"The ${major("Infinity")} Gene lets players use Bows and Crossbows without carrying Arrows.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"1/6 - Skeleton",
				"3/8 - Stray"
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

			defaultWeightPages(
				"3/11 - Phantom"
			)
		}

		itemAttractionField = book.entry(
			saveName = "item_attraction_field",
			category = bookCategory,
			name = "Item Magnet",
			icon = Items.IRON_INGOT
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Item Magnet")} Gene pulls nearby items toward players. By default it checks an 8-block radius every 10 ticks; both values are configurable.",
					"This Gene is disabled when the player has an active ${internalLink(ItemsPatchouliCategory.antiFieldOrb, "Anti-Field Orb")}, is near an active ${internalLink(BlocksPatchouliCategory.antiFieldBlock, "Anti-Field Block")}, or when sneaking.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"5/8 - Allay",
				"2/6 - Endermite"
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
					"The ${major("Johnny")} Gene increases Axe melee damage by 25% by default. This amount is configurable.",
					"This gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/10 - Vindicator"
			)
		}

		book.entry(
			saveName = "jump_boost",
			category = bookCategory,
			name = "Jump Boost",
			icon = OtherUtil.getPotionStack(Potions.LEAPING)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Jump Boost")} Gene gives entities the ${minor("Jump Boost effect")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"5/11 - Breeze",
				"2/11 - Dolphin",
				"3/18 - Donkey",
				"2/9 - Fox",
				"3/8 - Frog",
				"3/16 - Horse",
				"5/20 - Rabbit",
				"4/12 - Skeleton Horse",
				"4/12 - Zombie Horse"
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

			defaultWeightPages(
				"1/6 - Endermite"
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

			defaultWeightPages(
				"3/18 - Donkey",
				"4/15 - Goat"
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

			defaultWeightPages(
				"4/10 - Chicken",
				"4/17 - Parrot"
			)
		}

		book.entry(
			saveName = "luck",
			category = bookCategory,
			name = "Luck",
			icon = OtherUtil.getPotionStack(Potions.LUCK)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Luck")} Gene gives entities the ${minor("Luck effect")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"4/20 - Rabbit"
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

			defaultWeightPages(
				"5/10 - Hoglin",
				"2/7 - Pig",
				"4/9 - Piglin Brute",
				"3/8 - Piglin",
				"4/12 - Zoglin",
				"3/11 - Zombified Piglin"
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

			defaultWeightPages(
				"4/15 - Cow",
				"4/13 - Mooshroom"
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

			defaultWeightPages(
				"3/8 - Bat",
				"3/7 - Elder Guardian",
				"3/12 - Guardian",
				"5/10 - Warden"
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

			defaultWeightPages(
				"1/9 - Enderman",
				"2/19 - Iron Golem",
				"3/17 - Ravager",
				"5/10 - Warden"
			)
		}

		book.entry(
			saveName = "night_vision",
			category = bookCategory,
			name = "Night Vision",
			icon = OtherUtil.getPotionStack(Potions.NIGHT_VISION)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Night Vision")} Gene gives entities the ${minor("Night Vision effect")}.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"1/8 - Bat",
				"5/17 - Cave Spider",
				"3/15 - Spider",
				"3/11 - Wolf"
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

			defaultWeightPages(
				"1/10 - Chicken",
				"2/17 - Parrot",
				"4/16 - Slime"
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

			defaultWeightPages(
				"1/11 - Wolf"
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

			defaultWeightPages(
				"3/13 - Mooshroom"
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

			defaultWeightPages(
				"1/17 - Cave Spider",
				"2/7 - Witch"
			)
		}

		book.entry(
			saveName = "regeneration",
			category = bookCategory,
			name = "Regeneration",
			icon = OtherUtil.getPotionStack(Potions.REGENERATION)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Regeneration")} Gene gives entities the ${minor("Regeneration effect")}.",
					"Can be mutated into Regeneration 4.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"2/19 - Iron Golem"
			)
		}

		book.entry(
			saveName = "resistance",
			category = bookCategory,
			name = "Resistance",
			icon = OtherUtil.getPotionStackForEffect(MobEffects.DAMAGE_RESISTANCE)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Resistance")} Gene gives entities the ${minor("Resistance effect")}.",
					"This Gene can be ${minor("mutated into Resistance 2")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"4/9 - Drowned",
				"4/19 - Iron Golem",
				"4/17 - Ravager",
				"4/11 - Shulker",
				"1/8 - Zombie"
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

			defaultWeightPages(
				"2/7 - Cat",
				"3/10 - Ocelot"
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

			defaultWeightPages(
				"2/11 - Wolf"
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
			name = "Shoot Fireball",
			icon = Items.BLAZE_ROD
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Shoot Fireball")} Gene lets players fire small Fireballs by using a Blaze Rod or another configured activating item.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/12 - Blaze",
				"4/9 - Ghast"
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

			defaultWeightPages(
				"2/16 - Slime",
				"1/1 - Support Slime"
			)
		}

		book.entry(
			saveName = "speed",
			category = bookCategory,
			name = "Speed",
			icon = OtherUtil.getPotionStack(Potions.SWIFTNESS)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Speed")} Gene gives entities the ${minor("Speed effect")}.",
					"This Gene can be ${minor("mutated into Speed 2")} and ${minor("Speed 4")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"2/11 - Dolphin",
				"2/9 - Fox",
				"3/16 - Horse",
				"2/10 - Ocelot",
				"3/20 - Rabbit",
				"4/12 - Skeleton Horse",
				"4/12 - Zombie Horse"
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

			defaultWeightPages(
				"3/18 - Donkey",
				"3/16 - Horse",
				"3/8 - Llama",
				"4/16 - Polar Bear",
				"4/12 - Skeleton Horse",
				"4/12 - Zombie Horse"
			)
		}

		book.entry(
			saveName = "strength",
			category = bookCategory,
			name = "Strength",
			icon = OtherUtil.getPotionStack(Potions.STRENGTH)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Strength")} Gene gives entities the ${minor("Strength effect")}.",
					"This Gene can be ${minor("mutated into Strength 2")}.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/19 - Iron Golem",
				"4/16 - Polar Bear",
				"5/17 - Ravager"
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

			defaultWeightPages(
				"3/9 - Enderman",
				"3/11 - Phantom"
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
					"The ${major("Thorns")} Gene can reflect melee damage while the chest slot is empty or holds Leather armor. By default, it has a 15% chance to deal 0.75 hearts and costs 1 hunger.",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/8 - Bee",
				"4/16 - Pufferfish"
			)
		}

		book.entry(
			saveName = "water_breathing",
			category = bookCategory,
			name = "Water Breathing",
			icon = OtherUtil.getPotionStack(Potions.WATER_BREATHING)
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("Water Breathing")} Gene gives entities the ${minor("Water Breathing effect")}.",
					".",
					"This Gene ${minor("can be given to mobs")}."
				)
			)

			defaultWeightPages(
				"5/7 - Axolotl",
				"2/7 - Cod",
				"4/7 - Elder Guardian",
				"4/13 - Glow Squid",
				"4/12 - Guardian",
				"4/16 - Pufferfish",
				"3/8 - Salmon",
				"2/7 - Squid",
				"4/9 - Tadpole",
				"2/7 - Tropical Fish",
				"2/7 - Turtle"
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

			defaultWeightPages(
				"2/7 - Wither Skeleton",
				"4/15 - Wither"
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

			defaultWeightPages(
				"3/15 - Wither"
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

			defaultWeightPages(
				"3/15 - Goat",
				"4/12 - Sheep"
			)
		}

		xpAttractionField = book.entry(
			saveName = "xp_attraction_field",
			category = bookCategory,
			name = "XP Magnet",
			icon = Items.EXPERIENCE_BOTTLE
		) {
			textPage(
				text = doubleSpacedLines(
					"The ${major("XP Magnet")} Gene pulls nearby Experience Orbs toward players. By default it checks an 8-block radius every 10 ticks; both values are configurable.",
					"This Gene is disabled when the player has an active ${internalLink(ItemsPatchouliCategory.antiFieldOrb, "Anti-Field Orb")}, is near an active ${internalLink(BlocksPatchouliCategory.antiFieldBlock, "Anti-Field Block")}, or when sneaking.",
					"This Gene ${bad("cannot be given to mobs")}."
				)
			)

			defaultWeightPages(
				"3/6 - Endermite"
			)
		}

		book.entry(
			saveName = "bountiful",
			category = bookCategory,
			name = "Bountiful",
			icon = Items.GOLD_INGOT
		) {
			textPage(text = "The ${major("Bountiful")} Gene gives a mob +1 effective Looting when it dies. Bountiful II gives +2. This Gene is for mobs only.")
			defaultWeightPages("2/13 - Mooshroom", "3/7 - Sniffer (Bountiful II)")
		}

		book.entry(
			saveName = "experienced",
			category = bookCategory,
			name = "Experienced",
			icon = Items.EXPERIENCE_BOTTLE
		) {
			textPage(text = "The ${major("Experienced")} Gene makes a mob drop ${minor("twice as much experience")} when it dies. This Gene is for mobs only.")
			defaultWeightPages("2/9 - Villager", "4/7 - Sniffer")
		}

		book.entry(
			saveName = "fertile",
			category = bookCategory,
			name = "Fertile",
			icon = Items.EGG
		) {
			textPage(text = "Each ${major("Fertile")} parent adds one extra child when breeding. Two Fertile parents produce three children total. This Gene is for mobs only.")
			defaultWeightPages("3/20 - Rabbit")
		}

		book.entry(
			saveName = "frenzied",
			category = bookCategory,
			name = "Frenzied",
			icon = Items.IRON_SWORD
		) {
			textPage(text = "The ${major("Frenzied")} Gene makes a mob attack nearby creatures. Normally passive mobs deal 1.5 hearts. This Gene is for mobs only.")
			defaultWeightPages("3/12 - Zoglin")
		}

		book.entry(
			saveName = "placid",
			category = bookCategory,
			name = "Placid",
			icon = Items.POPPY
		) {
			textPage(text = "The ${major("Placid")} Gene prevents most mob targeting behavior. Some special attacks, such as a Creeper swelling, are unaffected. This Gene is for mobs only.")
			defaultWeightPages("3/15 - Cow")
		}

		book.entry(
			saveName = "lava_proof",
			category = bookCategory,
			name = "Lava Proof",
			icon = Items.LAVA_BUCKET
		) {
			textPage(text = "The ${major("Lava Proof")} Gene makes entities ${minor("immune to lava damage")}.")
			defaultWeightPages("2/11 - Strider")
		}

		book.entry(
			saveName = "reaching",
			category = bookCategory,
			name = "Reaching",
			icon = Items.PISTON
		) {
			textPage(text = "The ${major("Reaching")} Gene increases a player's block and entity reach by ${minor("25%")}.")
			defaultWeightPages("3/19 - Iron Golem")
		}

		book.entry(
			saveName = "web_defense",
			category = bookCategory,
			name = "Web Defense",
			icon = Items.COBWEB
		) {
			textPage(text = "The ${major("Web Defense")} Gene has a chance to trap attackers in a temporary Cobweb.")
			defaultWeightPages("1/17 - Cave Spider", "1/15 - Spider")
		}

		book.entry(
			saveName = "web_walker",
			category = bookCategory,
			name = "Web Walker",
			icon = Items.COBWEB
		) {
			textPage(text = "The ${major("Web Walker")} Gene lets entities move through Cobwebs without being slowed.")
			defaultWeightPages("1/17 - Cave Spider", "1/15 - Spider")
		}


	}

}