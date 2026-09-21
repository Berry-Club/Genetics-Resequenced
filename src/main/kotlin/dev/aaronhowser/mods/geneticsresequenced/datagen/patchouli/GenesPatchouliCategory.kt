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

class GenesPatchouliCategory(
	private val registries: HolderLookup.Provider
) {

	private lateinit var category: PatchouliBookCategory

	fun generate(book: PatchouliBook) {
		category = book.category(
			saveName = "genes",
			name = "Genes",
			description = "All the Genes in the mod",
			icon = ModItems.PLASMID.get()
		) {
			sortNumber = 3
		}

		BOOK_CATEGORY = category

		book.addEntries()
	}

	private fun PatchouliBook.addEntries() {
		entry(
			saveName = BIOLUMINESCENCE.localSaveName,
			category = category,
			name = "Bioluminescence",
			icon = Items.GLOWSTONE
		) {
			textPage(
				text = "The ${major("Bioluminescence")} Gene causes entities to ${minor("spawn light sources")} when in the dark.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = CHATTERBOX.localSaveName,
			category = category,
			name = "Chatterbox",
			icon = Items.NOTE_BLOCK
		) {
			textPage(
				text = "The ${major("Chatterbox")} Gene causes your chat messages to be ${minor("read by the game's narrator")}.\$(br2)This only applies to players within 64 blocks of you, and can be disabled in the client config.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = CHILLING.localSaveName,
			category = category,
			name = "Chilling",
			icon = Items.POWDER_SNOW_BUCKET
		) {
			textPage(
				text = "The ${major("Chilling")} Gene makes your melee attacks have a chance of ${minor("inflicting a buildup of Freezing")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = CLAWS.localSaveName,
			category = category,
			name = "Claws",
			icon = Items.STONE_SWORD
		) {
			textPage(
				text = "The ${major("Claws")} Gene causes entities to have a chance to ${minor("inflict Bleeding on empty-handed melee attacks")}.\$(br2)When mutated into Claws 2, the chance is doubled.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = CLIMB_WALLS.localSaveName,
			category = category,
			name = "Climb Walls",
			icon = Items.STRING
		) {
			textPage(
				text = "The ${major("Climb Walls")} Gene allows players to ${minor("climb walls")}.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = DRAGONS_BREATH.localSaveName,
			category = category,
			name = "Dragon's Breath",
			icon = Items.DRAGON_EGG
		) {
			textPage(
				text = "The ${major("Dragon's Breath")} Gene allows players to ${minor("shoot a dragon fireball")} when the Dragon's Breath key (currently bound to \$(k:key.geneticsresequenced.dragons_breath)) is used.\$(br2)This has a configurable cooldown, with the default being 15 seconds.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = EAT_GRASS.localSaveName,
			category = category,
			name = "Eat Grass",
			icon = Items.GRASS_BLOCK
		) {
			textPage(
				text = "The ${major("Eat Grass")} Gene allows players to ${minor("right-click Grass Blocks to regain hunger")}.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = EFFICIENCY.localSaveName,
			category = category,
			name = "Efficiency",
			icon = Items.GOLDEN_PICKAXE
		) {
			textPage(
				text = "The ${major("Efficiency")} Gene ${minor("makes players mine faster")}. This stacks with enchantments and Haste.\$(br2)Can be mutated into Efficiency 4.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = EMERALD_HEART.localSaveName,
			category = category,
			name = "Emerald Heart",
			icon = Items.EMERALD
		) {
			textPage(
				text = "The ${major("Emerald Heart")} Gene causes entities to ${minor("drop an Emerald upon death")}.\$(br2)For players, this has a configurable cooldown, with the default being 60 seconds.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = ENDER_DRAGON_HEALTH.localSaveName,
			category = category,
			name = "Ender Dragon Health",
			icon = ModItems.DRAGON_HEALTH_CRYSTAL.get()
		) {
			textPage(
				text = "The ${major("Ender Dragon Health")} Gene allows players to ${minor("completely negate damage")} while a ${pageLink(ItemsPatchouliCategory.DRAGON_HEALTH_CRYSTAL, "Dragon Health Crystal")} is in their inventory.\$(br2)Any damage they would take is instead dealt to the Crystal's durability.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = EXPLOSIVE_EXIT.localSaveName,
			category = category,
			name = "Explosive Exit",
			icon = Items.TNT
		) {
			textPage(
				text = "The ${major("Explosive Exit")} Gene causes entities to ${minor("explode upon death")}.\$(br2)If a player has this Gene, they only explode if they have 5 Gunpowder in their inventory, which is removed upon detonation.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = FIRE_PROOF.localSaveName,
			category = category,
			name = "Fire Proof",
			icon = Items.FLINT_AND_STEEL
		) {
			textPage(
				text = "The ${major("Fire Proof")} Gene makes entities ${minor("completely immune to burning damage")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = FLIGHT.localSaveName,
			category = category,
			name = "Flight",
			icon = Items.ELYTRA
		) {
			textPage(
				text = "The ${major("Flight")} Gene ${minor("allows players to fly")} by double-tapping jump.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)

			textPage(
				text = "By default, this Gene requires the following other Genes:\$(li)${pageLink(GenesPatchouliCategory.TELEPORT, "Teleport")}\$(li)${pageLink(GenesPatchouliCategory.STEP_ASSIST, "Step Assist")}\$(li)${pageLink(GenesPatchouliCategory.NO_FALL_DAMAGE, "No Fall Damage")}"
			)
		}

		entry(
			saveName = HASTE.localSaveName,
			category = category,
			name = "Haste",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Haste")} Gene gives entities the ${minor("Haste effect")}.\$(br2)Can be mutated into Haste 2.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = INFINITY.localSaveName,
			category = category,
			name = "Infinity",
			icon = Items.BOW
		) {
			textPage(
				text = "The ${major("Infinity")} Gene allows players to ${minor("fire Arrows when none are in their inventory")}.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = INVISIBLE.localSaveName,
			category = category,
			name = "Invisible",
			icon = Items.GOLDEN_APPLE
		) {
			textPage(
				text = "The ${major("Invisible")} Gene gives entities the ${minor("Invisible effect")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = ITEM_ATTRACTION_FIELD.localSaveName,
			category = category,
			name = "Item Attraction Field",
			icon = Items.IRON_INGOT
		) {
			textPage(
				text = "The ${major("Item Attraction Field")} Gene causes players to ${minor("grab items from a much larger distance")}.\$(br2)This Gene is disabled when the player has an active ${pageLink(ItemsPatchouliCategory.ANTI_FIELD_ORB, "Anti-Field Orb")}, is near an active ${pageLink(BlocksPatchouliCategory.ANTI_FIELD_BLOCK, "Anti-Field Block")}, or when sneaking.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = JOHNNY.localSaveName,
			category = category,
			name = "Johnny",
			icon = Items.IRON_AXE
		) {
			textPage(
				text = "The ${major("Johnny")} Gene ${minor("increases your attack damage when using Axes")}.\$(br2)This gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = JUMP_BOOST.localSaveName,
			category = category,
			name = "Jump Boost",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Jump Boost")} Gene gives entities the ${minor("Jump Boost effect")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = KEEP_INVENTORY.localSaveName,
			category = category,
			name = "Keep Inventory",
			icon = Items.SKELETON_SKULL
		) {
			textPage(
				text = "The ${major("Keep Inventory")} Gene causes players to ${minor("keep their inventory upon death")}.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = KNOCKBACK.localSaveName,
			category = category,
			name = "Knockback",
			icon = Items.PISTON
		) {
			textPage(
				text = "The ${major("Knockback")} Gene ${minor("increases your attack knockback")}.\$(br2)This gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = LAY_EGG.localSaveName,
			category = category,
			name = "Lay Egg",
			icon = Items.EGG
		) {
			textPage(
				text = "The ${major("Lay Egg")} Gene causes entities to ${minor("spawn an Egg")} every so often.\$(br2)This time is configurable, but defaults to once every 5 minutes. \$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = LUCK.localSaveName,
			category = category,
			name = "Luck",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Luck")} Gene gives entities the ${minor("Luck effect")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = MEATY.localSaveName,
			category = category,
			name = "Meaty",
			icon = Items.PORKCHOP
		) {
			textPage(
				text = "The ${major("Meaty")} Gene allows the entity to be ${minor("Sheared for a Raw Porkchop")}.\$(br2)If a player has it, they can ${minor("shear themselves")} by sneak right-clicking with Shears.\$(br2)This has a configurable cooldown, with the default being 1 minute."
			)

			textPage(
				text = "This can be mutated into Meaty 2, which causes the entity to spawn a Cooked Porkchop once every 5 minutes.\$(br2)Both Meaty and Meaty 2 ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = MILKY.localSaveName,
			category = category,
			name = "Milky",
			icon = Items.MILK_BUCKET
		) {
			textPage(
				text = "The ${major("Milky")} Gene allows the entity to be ${minor("milked with a Bucket")}. If a player has it, they can ${minor("milk themselves")} by sneak right-clicking with a Bucket.\$(br2)This has a configurable cooldown, with the default being 1 tick.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = MOB_SIGHT.localSaveName,
			category = category,
			name = "Mob Sight",
			icon = Items.GOLDEN_CARROT
		) {
			textPage(
				text = "The ${major("Mob Sight")} Gene occasionally ${minor("gives all nearby mobs the Glowing effect")}.\$(br2)Both the cooldown and radius are configurable, defaulting to 1 second and 32 blocks respectively.\$(br2)This gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = MORE_HEARTS.localSaveName,
			category = category,
			name = "More Hearts",
			icon = Items.ENCHANTED_GOLDEN_APPLE
		) {
			textPage(
				text = "The ${major("More Hearts")} Gene gives entities ${minor("10 extra hearts")}. If the Gene is mutated, they instead get ${minor("20")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = NIGHT_VISION.localSaveName,
			category = category,
			name = "Night Vision",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Night Vision")} Gene gives entities the ${minor("Night Vision effect")}.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = NO_FALL_DAMAGE.localSaveName,
			category = category,
			name = "No Fall Damage",
			icon = Items.FEATHER
		) {
			textPage(
				text = "The ${major("No Fall Damage")} Gene ${minor("negates all fall damage")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = NO_HUNGER.localSaveName,
			category = category,
			name = "No Hunger",
			icon = Items.BREAD
		) {
			textPage(
				text = "The ${major("No Hunger")} Gene ${minor("prevents your hunger from draining")} below a configurable point, which defaults to half..\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = PHOTOSYNTHESIS.localSaveName,
			category = category,
			name = "Photosynthesis",
			icon = Items.SUNFLOWER
		) {
			textPage(
				text = "The ${major("Photosynthesis")} Gene ${minor("feeds the player when they're in direct sunlight")}.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)

			textPage(
				text = "By default, this Gene requires ${pageLink(GenesPatchouliCategory.THORNS, "Thorns")}."
			)
		}

		entry(
			saveName = POISON_IMMUNITY.localSaveName,
			category = category,
			name = "Poison Immunity",
			icon = Items.FERMENTED_SPIDER_EYE
		) {
			textPage(
				text = "The ${major("Poison Immunity")} Gene makes entities ${minor("immune to Poison")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = REGENERATION.localSaveName,
			category = category,
			name = "Regeneration",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Regeneration")} Gene gives entities the ${minor("Regeneration effect")}.\$(br2)Can be mutated into Regeneration 4.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = RESISTANCE.localSaveName,
			category = category,
			name = "Resistance",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Resistance")} Gene gives entities the ${minor("Resistance effect")}.\$(br2)This Gene can be ${minor("mutated into Resistance 2")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = SCARE_CREEPERS.localSaveName,
			category = category,
			name = "Scare Creepers",
			icon = Items.CREEPER_HEAD
		) {
			textPage(
				text = "The ${major("Scare Creepers")} Gene makes it so that ${minor("Creepers will run away from you")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = SCARE_SKELETONS.localSaveName,
			category = category,
			name = "Scare Skeletons",
			icon = Items.SKELETON_SKULL
		) {
			textPage(
				text = "The ${major("Scare Skeletons")} Gene makes it so that ${minor("Skeletons will run away from you")}.\$(br2)This Gene ${minor("can be given to mobs")}.."
			)
		}

		entry(
			saveName = SCARE_SPIDERS.localSaveName,
			category = category,
			name = "Scare Spiders",
			icon = Items.SPIDER_EYE
		) {
			textPage(
				text = "The ${major("Scare Spiders")} Gene makes it so that ${minor("Spiders will run away from you")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)

			textPage(
				text = "By default, this Gene requires \$(l:geneticsresequenced:genes/scare_skeletons)Scare Skeletons/R."
			)
		}

		entry(
			saveName = SCARE_ZOMBIES.localSaveName,
			category = category,
			name = "Scare Zombies",
			icon = Items.ZOMBIE_HEAD
		) {
			textPage(
				text = "The ${major("Scare Zombies")} Gene makes it so that ${minor("Zombies will run away from you")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)

			textPage(
				text = "By default, this Gene requires ${pageLink(GenesPatchouliCategory.SCARE_CREEPERS, "Scare Creepers")}."
			)
		}

		entry(
			saveName = SHOOT_FIREBALLS.localSaveName,
			category = category,
			name = "Shoot Fireballs",
			icon = Items.BLAZE_ROD
		) {
			textPage(
				text = "The ${major("Shoot Fireballs")} Gene allows players to ${minor("shoot small fireballs")} when a Blaze Rod is used.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = SLIMY_DEATH.localSaveName,
			category = category,
			name = "Slimy Death",
			icon = Items.SLIME_BALL
		) {
			textPage(
				text = "The ${major("Slimy Death")} Gene makes it so, ${minor("upon the player dying, they are instantly revived and several friendly Support Slimes spawn")}.\$(br2)This has a configurable cooldown, with the default being 5 minutes.\$(br2)This Gene ${bad("cannot be given to mobs")}"
			)
		}

		entry(
			saveName = SPEED.localSaveName,
			category = category,
			name = "Speed",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Speed")} Gene gives entities the ${minor("Speed effect")}.\$(br2)This Gene can be ${minor("mutated into Speed 2")} and ${minor("Speed 4")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = STEP_ASSIST.localSaveName,
			category = category,
			name = "Step Assist",
			icon = Items.COBBLESTONE_STAIRS
		) {
			textPage(
				text = "The ${major("Step Assist")} Gene allows players to ${minor("walk up single blocks")} as if they were stairs.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = STRENGTH.localSaveName,
			category = category,
			name = "Strength",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Strength")} Gene gives entities the ${minor("Strength effect")}.\$(br2)This Gene can be ${minor("mutated into Strength 2")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = TELEPORT.localSaveName,
			category = category,
			name = "Teleport",
			icon = Items.ENDER_PEARL
		) {
			textPage(
				text = "The ${major("Teleport")} Gene allows players to ${minor("teleport forward")} when the Teleport key (currently bound to \$(k:key.geneticsresequenced.teleport)) is used.\$(br2)This has a configurable cooldown, with the default being 1 second. The distance is also configurable, defaulting to 10 blocks.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}

		entry(
			saveName = THORNS.localSaveName,
			category = category,
			name = "Thorns",
			icon = Items.CACTUS
		) {
			textPage(
				text = "The ${major("Thorns")} Gene gives entities a chance to ${minor("damage melee attackers")}. This uses up some hunger.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = WATER_BREATHING.localSaveName,
			category = category,
			name = "Water Breathing",
			icon = Items.POTION
		) {
			textPage(
				text = "The ${major("Water Breathing")} Gene gives entities the ${minor("Water Breathing effect")}.\$(br2).\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = WITHER_HIT.localSaveName,
			category = category,
			name = "Wither Hit",
			icon = Items.WITHER_ROSE
		) {
			textPage(
				text = "The ${major("Wither Hit")} Gene causes entities to ${minor("inflict Wither when melee attacking")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = WITHER_PROOF.localSaveName,
			category = category,
			name = "Wither Proof",
			icon = Items.WITHER_SKELETON_SKULL
		) {
			textPage(
				text = "The ${major("Wither Proof")} Gene makes entities ${minor("immune to Wither")}.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = WOOLY.localSaveName,
			category = category,
			name = "Wooly",
			icon = Items.WHITE_WOOL
		) {
			textPage(
				text = "The ${major("Wooly")} Gene allows the entity to be ${minor("sheared for Wool")}. If a player has it, they can ${minor("shear themselves")} by sneak right-clicking with Shears.\$(br2)This has a configurable cooldown, with the default being 1 minute.\$(br2)This Gene ${minor("can be given to mobs")}."
			)
		}

		entry(
			saveName = XP_ATTRACTION_FIELD.localSaveName,
			category = category,
			name = "XP Attraction Field",
			icon = Items.EXPERIENCE_BOTTLE
		) {
			textPage(
				text = "The ${major("XP Attraction Field")} Gene causes players to ${minor("grab XP Orbs from a much larger distance")}.\$(br2)This Gene is disabled when the player has an active ${pageLink(ItemsPatchouliCategory.ANTI_FIELD_ORB, "Anti-Field Orb")}, is near an active ${pageLink(BlocksPatchouliCategory.ANTI_FIELD_BLOCK, "Anti-Field Block")}, or when sneaking.\$(br2)This Gene ${bad("cannot be given to mobs")}."
			)
		}
	}

	companion object {

		lateinit var BOOK_CATEGORY: PatchouliBookCategory
			private set

		val CATEGORY = PatchouliBookReference("genes")
		val BIOLUMINESCENCE = PatchouliBookReference("genes/bioluminescence")
		val CHATTERBOX = PatchouliBookReference("genes/chatterbox")
		val CHILLING = PatchouliBookReference("genes/chilling")
		val CLAWS = PatchouliBookReference("genes/claws")
		val CLIMB_WALLS = PatchouliBookReference("genes/climb_walls")
		val DRAGONS_BREATH = PatchouliBookReference("genes/dragons_breath")
		val EAT_GRASS = PatchouliBookReference("genes/eat_grass")
		val EFFICIENCY = PatchouliBookReference("genes/efficiency")
		val EMERALD_HEART = PatchouliBookReference("genes/emerald_heart")
		val ENDER_DRAGON_HEALTH = PatchouliBookReference("genes/ender_dragon_health")
		val EXPLOSIVE_EXIT = PatchouliBookReference("genes/explosive_exit")
		val FIRE_PROOF = PatchouliBookReference("genes/fire_proof")
		val FLIGHT = PatchouliBookReference("genes/flight")
		val HASTE = PatchouliBookReference("genes/haste")
		val INFINITY = PatchouliBookReference("genes/infinity")
		val INVISIBLE = PatchouliBookReference("genes/invisible")
		val ITEM_ATTRACTION_FIELD = PatchouliBookReference("genes/item_attraction_field")
		val JOHNNY = PatchouliBookReference("genes/johnny")
		val JUMP_BOOST = PatchouliBookReference("genes/jump_boost")
		val KEEP_INVENTORY = PatchouliBookReference("genes/keep_inventory")
		val KNOCKBACK = PatchouliBookReference("genes/knockback")
		val LAY_EGG = PatchouliBookReference("genes/lay_egg")
		val LUCK = PatchouliBookReference("genes/luck")
		val MEATY = PatchouliBookReference("genes/meaty")
		val MILKY = PatchouliBookReference("genes/milky")
		val MOB_SIGHT = PatchouliBookReference("genes/mob_sight")
		val MORE_HEARTS = PatchouliBookReference("genes/more_hearts")
		val NIGHT_VISION = PatchouliBookReference("genes/night_vision")
		val NO_FALL_DAMAGE = PatchouliBookReference("genes/no_fall_damage")
		val NO_HUNGER = PatchouliBookReference("genes/no_hunger")
		val PHOTOSYNTHESIS = PatchouliBookReference("genes/photosynthesis")
		val POISON_IMMUNITY = PatchouliBookReference("genes/poison_immunity")
		val REGENERATION = PatchouliBookReference("genes/regeneration")
		val RESISTANCE = PatchouliBookReference("genes/resistance")
		val SCARE_CREEPERS = PatchouliBookReference("genes/scare_creepers")
		val SCARE_SKELETONS = PatchouliBookReference("genes/scare_skeletons")
		val SCARE_SPIDERS = PatchouliBookReference("genes/scare_spiders")
		val SCARE_ZOMBIES = PatchouliBookReference("genes/scare_zombies")
		val SHOOT_FIREBALLS = PatchouliBookReference("genes/shoot_fireballs")
		val SLIMY_DEATH = PatchouliBookReference("genes/slimy_death")
		val SPEED = PatchouliBookReference("genes/speed")
		val STEP_ASSIST = PatchouliBookReference("genes/step_assist")
		val STRENGTH = PatchouliBookReference("genes/strength")
		val TELEPORT = PatchouliBookReference("genes/teleport")
		val THORNS = PatchouliBookReference("genes/thorns")
		val WATER_BREATHING = PatchouliBookReference("genes/water_breathing")
		val WITHER_HIT = PatchouliBookReference("genes/wither_hit")
		val WITHER_PROOF = PatchouliBookReference("genes/wither_proof")
		val WOOLY = PatchouliBookReference("genes/wooly")
		val XP_ATTRACTION_FIELD = PatchouliBookReference("genes/xp_attraction_field")
	}
}