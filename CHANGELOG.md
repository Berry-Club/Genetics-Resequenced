# 1.12.3

### Changes

- Made the max version of Aaron not explicit

# 1.12.2

### Changes

- Updated Aaron, and made the max version explicit

### Fixed

- Fixed Cringe shake config causing a crash

# 1.12.1

### Fixed

- Fixed it requiring a dev version of Aaron instead of the release version

# 1.12.0

### Added

- New events for when Gene requirements and entity Genes are being calculated
	- https://moddedmc.wiki/en/project/geneticsresequenced/latest/docs/devs/kubejs

### Changed

- Improved the message when you get a Gene you're missing the requirements for
	- The red text is now in [brackets], so you know it can be hovered over to see which Genes are missing
- When using Delicate Touch, the Scraper no longer has a cooldown (#81)
	- That means you can spam it as fast as you want
- Cell Duplication recipes now make more than 1 Cell (#82)
	- A regular Cell will craft 8
	- A GMO Cell will craft 4
- Gene Requirements are now a datapack registry
- Entity Genes are now a datapack registry

### Fixed

- The config for disabling Cringe shake now actually works

# 1.11.1

### Changed

- Modified the `/gr remove-gene` command
	- It's now `/gr remove-gene <entities> <gene>`
	- It also now automatically suggests the Genes held by those entities!

# 1.11.0

### Added

- The concept of incompatible Genes
	- Some Genes will refuse to be added to en entity if they have an incompatible Gene
	- For example, you can't have both Placid and Frenzied at the same time
- The concept of Temporary Genes
	- A set of Genes entirely separate from the permanent Genes (which is what the old set is referred to now)
		- Temporary Genes have a duration that goes down every tick, and then are removed when the duration reaches zero
		- Not kept on death
	- Currently only implemented via commands, but I plan on making ways to give them in-game
		- The current way of giving Genes is very bulky and absolutely not sneaky at all, but also giving Genes "non-consensually" is a pretty major thing.
		- The idea is that you can temporarily give targets a Gene to hinder them in some way, without having to extract and purify the target's blood first
		- I might remove the config to prevent players from being given negative genes, and instead have them be given via temporary genes instead

### Changed

- Renamed most commands
	- `/gr gene add` is now `/gr add-gene` etc
- Made Bioluminescence Blocks removing themselves be safer
- When hovering over an Anti-Plasmid in the Plasmid Infuser, it now says the correct way to set Anti-Plasmids
- Mobs that have Genes now should not despawn
	- Only applies to mobs that respect MobDespawnEvent

### Fixed

- Set Anti-Plasmids can now go into the Plasmid Injector again
	- There was a bug in SetAntiPlasmidRecipe that was resetting the dna_points part of the plasmid progress component
	- This was causing it to be marked as a not-finished Plasmid, so it didn't fit into the Plasmid Injector

# 1.10.1

### Changed

- The Bountiful Gene now also makes Bees pollinate faster
	- Default is 400 ticks, tier 1 is 200 ticks, tier 2 is 100 ticks
- Entities with the Cringe Gene will now vibrate, Temmie style
	- The speed and intensity (and if it happens at all) is configurable in the client config
- The Sniffer now has the following Gene weights:
	- Experienced: 4
	- Bountiful II: 3
- Bountiful II now requires Bountiful

### Fixed

- Fixed some Genes not having info pages or wiki entries

# 1.10.0

### Added

- New Genes, designed to be given to mobs instead of players:
	- Bountiful
		- Their loot drops are treated as if they were killed with Looting one/two tiers higher than they were
		- Comes in 2 tiers
	- Fertile
		- Increases the amount of babies from breeding
		- +1 baby for each parent that has it
	- Experienced
		- Increases the amount of XP dropped upon death
	- Placid
		- Prevents mobs from attacking things (in theory)
			- Uses a mixin in TargetGoal#canContinueToUse
			- Doesn't work on anything that doesn't call that method, such as the Creeper's SwellGoal
	- Frenzied
		- Makes mobs attack everything nearby
		- Can be given to mobs that can't normally attack, such as Cows
			- In that case, they do 3 damage
	- Both Bountiful and Experienced use new Attributes, rather than hardcoded values, so theoretically other things could also modify them
	- Remember, Genes are passed down when breeding!

### Fixed

- Fixed Web Walker and Web Defense having no name

# 1.9.1

### Changed

- Explosive Exit is no longer negative
	- I forgot that it doesn't effect blocks, which is why I marked it as negative
	- I also forgot that players cant get negative genes by default

### Fixed

- Fixed the Oracle Index files being copied to the wrong location, resulting in them not being visible
- Fixed some wiki files not existing, so things weren't visible in Oracle Index
- Various other wiki changes
- Fixed Web Walker and Web Defense not having any entities that give it

# 1.9.0

### Added

- Made a wiki at moddedmc.wiki
	- https://moddedmc.wiki/en/project/geneticsresequenced/latest
	- Also added in-game support via [Oracle Index](https://modrinth.com/mod/oracle-index), which has been added as an optional dependency

### Changes

- The Dragon Health Crystal doesn't show its durability bar when it's full
- The Dragon Health Crystal now shows its remaining damage in the tooltip
- Flambe and Explosive Exit are now negative Genes
- Renamed the Mining Weakness Gene to Mining Fatigue
- Moved the GeneChangeEvents out of the CustomEvents object
	- `CustomEvents.GeneChangeEvent.Pre` is now `GeneChangeEvent.Pre`

### Fixed

- Fixed the Metal Syringe giving the blood owner Glowing instead of using the mixin
- Fixed an issue with the Dragon Health Crystal continuing to iterate over all crystals even after fully negating incoming damage
- The Dragon Health Crystal now stacks to 1 again
- Fixed the Claws Gene not actually inflicting extra damage, only the Bleeding effect
	- Now it inflicts +8 damage with Claws, and +16 with Claws II (if you have an empty hand)

# 1.8.2

### Fixed

- Fixed an issue with Wall Climbing that pulled you upwards while sneaking instead of making you stop moving
- Fixed some machines voiding their output (#78)
	- I moved the logic for what items can go in what slots from the ItemHandler to the Container, but because I didn't want the player to be able to insert items into the output slot, it also didn't allow the *machine* to put items in the output slot either

# 1.8.1

### Fixed

- The potion icon change in 1.8.0 no longer applies to potions that are marked as `show_icon: true` in the Gene
	- No default genes have that set, but just in case 🤷
- Fixed items being able to be put into machine slots when they shouldn't be
- Fixed an issue where the Advanced Incubator's slots didn't exist on the server (#77)

# 1.8.0

### Added

- Web Walker
	- Makes you immune to being slowed by Cobwebs
	- Requires Weaving Gene
- Web Defense
	- If you get hit, spawns a temporary Cobweb on your attacker
	- You are immune to your own temporary Cobwebs
	- Requires Weaving Gene

### Changed

- Mob Effects given by Genes will now not be shown in the player's inventory screen
	- That was annoying because Genes are permanent, so the effects would always be there
	- I noticed that this doesn't work with EMI currently, it might not work with some other things that change how potion effects render in the inventory either
- Changed some advancement types to be challenge or goal
- Weaving, Wind Charged, and Oozing are no longer considered negative genes
- Genes can now have multiple potions instead of just 0 or 1 (#75)
	- It now uses a List<PotionDetails> rather than an Optional<PotionDetails>
	- Pack devs that have custom Genes will have to update to match this change, if they use potions

### Fixed

- Mob Sight no longer works by giving nearby mobs the Glowing effect (#74)
	- Now the mod uses a mixin on LivingEntity#isCurrentlyGlowing, on the client, checking if the client player has the Gene and the mob isn't too far away

# 1.7.2

### Fixed

- Fixed machines voiding their contents when mined, now they drop their contents when broken

# 1.7.1

### Fixed

- Marked the version range required for Kotlin for Forge and Aaron
- Fixed MachineMenu looking for a class in Irregular Implements instead of Aaron (#73)
- Fixed an issue when loading 1.6.0 worlds where machines would default to having maxProgress of 0, so crafts would complete on the first tick

# 1.7.0

### Changed

- The mod now requires the library [Aaron](https://www.curseforge.com/minecraft/mc-mods/aaron)
	- There have been massive reworks to this mod, a lot of stuff was moved to Aaron
	- Every machine works completely differently, internally. This was my first mod that had machines and guis, and I was doing them in a really bad way. I do them much better now.
	- I spent days tearing this mod apart and cleaning it back up. I am honestly shocked that it ever worked to begin with. I have no idea if this will improve performance, but the code is now infinitely more maintainable.
- The Advanced Incubator now has a more easily visible button to toggle the temperature
- The Wall Climb Gene now allows you to cling to ceilings by sneaking (#70)

# 1.6.0

- Update NeoForge to 21.1.197
- Update KFF to 5.9.0
- Added a Lava Proof Gene, which makes you immune to direct Lava damage (#67)
- Dragon Health Crystal no longer uses durability, but has its own separate data component (#58)
- The Metal Syringe now increases your entity interaction range attribute by 3 blocks
- Baby mobs inherit their parents' Genes, with a 100% if both parents have it, and a 50% if only one parent has it (#56)
- The Gene Checker now shows what Genes the target can provide, in addition to what it has (#66)
- Immunity Genes now use isInvulnerableTo rather than setting the damage amount to 0, so you no longer flinch from those damages
- Fixed Antigenes not being removed from Syringes when used (#53)
- Fix Black Death recipe (#63)

# 1.1.0

- Updated to 1.21
- Genes are now an actual Registry
- Fixed Incubator and Advanced Incubator from not working (hasRecipe() returned false when hasEnoughEnergy was true rather than when false)
- Fixed the Black Death recipe being broken
- Item Magnet will show in the tooltip that an item is blacklisted (configurable)
- Properly sync the player's Genes on client and server on login
- Allowed the Gene Add/Remove commands to use the gene name string instead of the id Resource Location
- The Keep Inventory Gene now uses Data Attachment, which means it persists across server stops
- Efficiency Attribute now uses modifiers instead of modifying the attribute base value. No idea why I was doing that originally, that's awful.
- The Flight gene now is an Attribute, rather than changing the player's ability to fly. Fixes #10
- Wall Climbing is no longer an Attribute (Why was it? What was I thinking?)
- The non-empty DNA Helices and Plasmids in the creative mode tab are now after everything else
- Attribute Modifiers given by Genes are now kept on respawn
- Changed the machines' energy texture to one made by TJKraft
- Support Slime now only checks if it should despawn once every 40 ticks rather than every tick, which should improve performance.
- Emerald Heart chat function only worked if Emerald Heart was DISABLED, instead of ENABLED. Fixed that.
- Improved command response messages, for example "Added Claws to Dev" instead of "Added Claws to 1 entities".
- Item Magnet now has a delay against picking up items dropped by you
- Increased the amount of Support Slimes spawned from 1-4 to 3-6
- Poison Proof Gene now uses the new NeoForge Poison damage, rather than checking for magic damage while you have poison effect
- Renamed The Cure to Panacea
- The Coal Generator now allows extracting items from any side (in case of fuels like Lava Buckets, which leave an empty Bucket behind)
- Machines now keep their progress if they run out of power
- MobGeneRegistry and GeneRequirementRegistry loggers are now debug instead of info
- The bubbles in the Incubator and Advanced Incubator now animate slower
- Fixed broken tooltip for Basic Genes in the Plasmid Infuser
- Changed Plasmid tooltip text color to Gray
- Added a recipe for unsetting Anti-Plasmids, if for whatever reason you want to
- Plasmids and Anti-Plasmids now only stack to 1
- Stepping on a contaminated Syringe will now give you poison
- Changed the background texture for the advancements
- Dragon's Breath cooldown now is actually for Dragon's Breath, and not for Shoot Fireballs
- Claws inflicts Bleeding for 5 seconds rather than 300
- Fixed the duplicate message send when you get a Gene that you're missing the required Genes for
- Players with Meaty can now shear themselves while sneaking
- Knockback Gene strength is no longer configurable
- When you have Max Health 1 or 2, you'll respawn with completely full health rather than just the vanilla 10 hearts
- Lowered default cooldown for Dragon's Breath from 15 seconds to 5
- Improved messages for Genes on cooldown
- Scare Genes now have a longer distance, and scared mobs run away with more speed

# 1.1.1

- Updated to NeoForge 21.0.53-beta
- Added EMI support
- Added 4 new Genes: Infested, Oozing, Weaving, and Wind Charged
- Fixed a recipe conflict between Panacea and Zombify Villager potions, Panacea now requires a Regeneration Helix rather than Emerald Heart
- Sorted Genes in several places (Sorted firstly by regular/mutation/negative, then by id)
- Fixed a typo in a tooltip
- Forgot the missing : in several places in the mod name

# 1.1.2

- Added loot tables for all the blocks, now they should actually drop themselves!

# 1.1.3

- Updated NeoForge to 21.0.87-beta
- Updated Kotlin for Forge to 5.4.0
- Fixed error spam on server close

# 1.1.4

- Fix issues with JEI

# 1.1.5

- Blocks are no longer instamine
- Blocks now require an Iron Pickaxe to mine
- Fixed chat spam from Lay Egg Gene, possibly others

# 1.1.6

- Update to NeoForge 21.0.113-beta
- Improvements to Infinity Gene. Turns out most of the code wasn't actually reachable, and also was pointless besides. Much simpler now. Also, fixed picking up fired arrows (#13)
- Added support for NeoForge mod config menu
- Rearranged configs

# 1.1.7

- Fixed crash on servers

# 1.1.8

- Update to NeoForge 21.0.140-beta
- Fixed crash from trying to scrape invalid entities
- Made every machine GUI 6 pixels taller, with all elements being 6 pixels lower, to prevent the machine name clipping through the energy bar
- Fixed the FE bar filling from top to bottom, rather than bottom to top
- Fixed the Coal Generator's burn progress rendering incorrectly
- Fixed the progress arrows in the Incubator and Advanced Incubator rendering incorrectly
- All machine Screen classes now extend the abstract class MachineScreen, vastly reducing code duplication
- Cells are no longer separated as far as EMI goes, so looking at any Cell will show all Cells
- Mob Spawn Eggs now have the EMI info recipes for what genes they have

# 1.1.9

- Fixed the Potion of Cell Growth recipe setting the entity when it shouldn't (#14)
- Support Slimes now have a 100% chance to give the Slimy Death Gene. The file previously pointed at the entity `minecraft:slime` rather than `geneticsresequenced:support_slime`
- Localized tags for EMI
- Removed the reference to the Patchouli book from the first advancement, as Patchouli isn't on 1.21 yet
- Moved to better practices (using less lazy values, mostly)

# 1.1.10

- Update to NeoForge 21.1.4
- Fixed the Scraper not working on the Ender Dragon (#15)
- Made it so that removing Overclockers from the Advanced Incubator will reset its progress
- Fixed the Incubator not being able to output
- Fixed the Plasmid Infuser giving +1 DNA Point for Helices of the same Gene rather than +2 (#18)
- Fixed the Advanced Incubator ignoring Chorus Fruit (#17)
- In the Plasmid Infuser, the initial DNA Helix infused into an empty Plasmid does not give it any DNA Points, previously it gave +1
- The GMO chance increase/decrease per Overclocker and Chorus Fruit is now configurable
- Massively improved the code for most Screens, moving a lot of things to Widgets rather than reimplementing them every Screen
- All machine menu progress arrows now show the percentage completion in the tooltip
- Fixed Scrapers being unable to be enchanted (#16)

# 1.2.0

- Support for Modonomicon!
- You can now dupe Genetically Modified Cells using Organic Substrate
- Fixed Genetically Modified Cells not having EMI recipe pages for turning into DNA Helices (#23)
- GMO Recipes will have their logic printed in the tooltip of the Potion of Cell Growth in the Advanced Incubator
- The Scare Genes can now be given to entities
- The Scare Genes now apply based on an entity's tags (#geneticsresequenced:avoids_scare_creeper_gene etc) rather than based on their class (is or extends Creeper etc)
- Reimplemented Curios support for the Keep Inventory gene (#20)
- If the items given by the Keep Inventory Gene don't fit in your inventory, they're dropped at your feet instead of being tossed from your camera
- Updated Patchouli support to use item components rather than NBT
- Updated the Patchouli book to reflect changes since 1.19
- The Patchouli book is now in the creative tab
- Fixed the Blood Purifier page in the Patchouli book having the Plasmid Injector instead

# 1.2.1

- Added a recipe for the Modonomicon book
- Added a recipe for the Patchouli book

# 1.2.2

- Updated to Minecraft 1.21.1
- Updated to NeoForge 21.1.42
- Added a recipe for the Metal Syringe
- Moved most recipes to use item tags where possible
- Made it so you can use the Metal Syringe and Scraper on Villagers without opening their menu (if the item is `#geneticsresequenced:prevents_some_mob_interaction` and the entity type is `#geneticsresequenced:allows_preventing_interaction`)
- Fixed the Metal Syringe telling you that the wrong Genes can't be injected into mobs (it was displaying negative
  genes, rather than filtering against Gene.canMobsHave)
- Fixed decrypted DNA Helices being able to be put through the DNA Decryptor
- Added the failed Basic Gene GM Cells to EMI's GMO Cell Incubating recipe page

# 1.2.3

- Added the Metal Syringe to the Syringe book entry
- Fixed some weird phrasing in the Potion of Mutation entry
- Major overhaul of the GMO Cell Incubating EMI recipe page
- Fixed the Advanced Incubator screen bubbles not animating slower in low-temperature mode

# 1.3.0

## Data

- Genes are now data-driven! They're in `/data/_____/geneticsresequenced/gene/`
- They follow the following structure, all fields are optional:
	- `dna_points_required`: The amount of DNA Points required to complete a Plasmid. Defaults to 0
	- `requires_genes`: A list of Resource Location IDs for Genes that are required to have this Gene
	- `allowed_entities`: A filter of entity types that the Gene can be injected into
		- Defaults to `{"type": "neoforge:any"}`.
		- Can also accept a single entity type (`"minecraft:player"`) or a list of entity types (`["minecraft:cow","minecraft:chicken"]`)
	- `potion_details`: An object with the following fields, for a potion effect that's granted at all times:
		- `effect`: The effect to apply
		- `level`: The level of the effect (optional, defaults to 1)
		- `duration`: The duration of the effect (optional, defaults to -1, infinite)
		- `showIcon`: If the effect icon should be shown (optional, defaults to false)
	- `attribute_modifiers`: A list of objects with the following fields, for attribute modifiers that are granted at all times:
		- `attribute`: The attribute to modify
		- `id`: A Resource Location ID for the modifier
		- `operation`: The operation to apply to the attribute (`"add_value"`, `"add_multiplied_base"`, or `"add_multiplied_total"`)
		- `amount`: The amount to modify the attribute by
	- `scares_entities_with_tag`: An entity type tag that the Gene will scare
- Incubator recipes are no longer actually Brewing recipes, and can therefore no longer be used in a Brewing Stand
	- Consequently, you can now make custom recipes that use the Incubator! See `/data/geneticsresequeced/recipe/incubator/`
	- There are 3 types (technically 5, but 2 of them are hardcoded with no parameters):
		- `geneticsresequenced:incubator_basic`
			- Requires two ingredients `top_slot` and `bottom_slot`, and an output itemstack `output`
			- Optionally can have `is_low_temperature`, which makes it require low temperature. Defaults to false, making it require high temperature
		- `geneticsresequenced:incubator_gmo`
			- Requires `entity_type` that the Cell Growth or Mutation Potion must be set to
			- Requires `ingredient` for the item in the top slot
			- Requires `ideal_gene` for the Gene that a successful Cell will have
			- Optionally can have `gene_chance`, which is a number 0-1 for the chance of getting the Gene (giving Basic if it fails). Defaults to 1.
			- Optionally can have `needs_mutation_potion` which makes it require a Potion of Mutation instead of a Potion of Cell Growth. Defaults to false.
		- `geneticsresequenced:incubator_virus`
			- Takes in an input Gene and an output Gene, and makes a recipe that converts when crafted with Viral Agents
- Disabling Genes now uses the Gene tag `#geneticsresequenced:disabled`
- Genes requiring other Genes is now handled in the Gene's definition json, rather than a file in /gene_requirements/
- Changed some default Gene requirements:
	- Flight no longer requires Jump Boost, but now requires Step Assist
	- Photosynthesis now requires Eat Grass
	- Scare Spiders and Scare Zombies are no longer Mutation genes, and don't require Scare Creepers or Scare Skeletons
- Added the following entity types to `geneticsresequenced:allows_preventing_interaction`: Horse, Donkey, Mule, Llama, Trader Llama

## Additions / Changes

- Added Reaching Gene, which allows you to reach 1.25 times further
- Support Slime Spawn Eggs have been added to the Spawn Eggs creative tab
- Removed the potion outlines from the Incubator background slots
- The Plasmid Injector EMI recipe page now says in the tooltip that you can have multiple Genes/Antigenes in the same Syringe
- Updated Incubator EMI recipe pages

## Fixes

- Fixed an issue where the Photosynthesis Gene would stop working when enabled instead of when not enabled
- Fixed an issue in the coloring of Anti-Plasmid tooltips
- Fixed an issue where Genes that require 1 DNA Point would show as requiring 0 DNA Helices instead of 1 in EMI
- Removed a rather large unused texture that was taking up space

# 1.3.1

- Moved some nullable things from the creative tab's builder to the BuildCreativeModeTabContentsEvent, where hopefully they won't be null, and fixed an NPE if it is
- Changed Gene.PotionDetails.CODEC to use `show_icon` instead of `showIcon`

# 1.4.0

- Added the Gene Checker, which lets you see the Genes of either yourself or the entity you're looking at
	- Since this exists, `/geneticsresequenced list` now requires op permissions
- Reworked how the entity genes datapack system worked
	- Previously, it would simply set the gene weights
	- Now, it adds adds the weight to the gene, so you can have multiple files adding weight to the same gene
- Re-included the lang file for the Modonomicon book, so now you can actually read it
- Changed some tags:
	- `#geneticsresequenced:syringe` is now `#geneticsresequenced:syringes`
	- `#geneticsresequenced:fireball` is now `#geneticsresequenced:activates_shoot_fireball_gene`
	- `#geneticsresequenced:magnet_blacklist` is now `#geneticsresequenced:item_magnet_gene_blacklist`
- Removed the tag `#geneticsresequenced:wooly`, it just uses `#c:tools/shear` now
- Added EMI tag translations
- The command `/geneticsresequenced removeNearbyLights` has been renamed to `/geneticsresequenced clearBioluminescenceBlocks`
	- Additionally, it no longer requires op permissions. Its range argument does, however

# 1.5.0

- Updated NeoForge from 21.1.36 to 21.1.73
- Moved Gene requirements from the Gene constructor to a separate datapack system, located in `/data/____/geneticsresequenced/geme_requirements/`
- Combined the Gene commands to be arguments of `/geneticsresequenced gene`
- Localized into Simplified Chinese, thanks to @shenyx110! (#31)
- Fixed a crash with Jade (and probably similar mods) (#37)
- Minor improvements to the Entity Genes datapack loader
- Entity Genes are now datagenned rather than manually typed
- The Ender Dragon no longer can give the Basic Gene
- The info pages that show what entities give what genes now uses an Organic Matter item rather than a DNA Helix item
- EMI now considers each type of Organic Matter and Cell to be separate
	- Hid the empty ones from EMI, but added the filled one for each set to Pig
- In entity gene info pages, the left stack now shows both the Organic Matter and the Cell for that entity type
- Improved the method used to make mobs that don't have any Gene weights always give the Basic Gene
	- Consequently, their info pages now show that they have a 100% chance of giving the Basic Gene, rather than not having an info page at all
- Fixed info pages for required Genes, now it lists the actually required Genes rather than repeating the Gene itself
	- Previously, Claws II would say it needs Claws II, rather than needing Claws I. That's fixed now
- The following Genes can now be held by any mob, rather than only players: Haste, More Hearts II, Night Vision
- Removed classes for several Data Components, because they really weren't needed
	- This should be a non-breaking change
- Moved several Data Component keys to snake_case from camelCase (for example, `dnaPoints` is now `dna_points`)
	- This should also be a non-breaking change
- Renamed the Data Component `geneticsresequenced:active` to `geneticsresequenced:is_active`
	- This is a breaking change, but only for Anti-Field Orbs, and it basically just resets them to false
- The Data Component `geneticsresequenced:specific_entity` now uses a Component instead of a String for the name field.
	- Consequently, Syringe tooltips should look better in some cases
	- This shouldn't be a breaking change, but if it is, it only affects filled Syringes
- The Gene tag `#geneticsresequenced:hidden` is now `#geneticsresequenced:helix_only`
	- It's only for Genes that can only be held by DNA Helices, and not Plasmids or entities
	- This fixes a point of confusion, because the only Gene currently set to this is the Basic Gene. It being called "hidden" made it look like it should be hidden from EMI etc too, which isn't the case
		- As a result, a DNA Helix with the Basic Gene now shows up in EMI. It also now has an information page.

# 1.5.1

- Re-added the empty Plasmid to the creative tab, and therefore EMI
- Fixed not being able to infuse Basic Genes into Plasmids for +1 DNA Point
- Bioluminescent Blocks now have the block tag `#minecraft:air`

# 1.5.2

- Fixed a bug that would cause a LOT of Genes to not work properly!
	- The problem was that I was comparing the Holder<Gene> to the ResourceKey<Gene>, which would always return false
	- This was effecting:
		- Water Breathing
		- Flambé
		- Lay Egg
		- Meaty II
		- Green Death
		- Un-Undeath
		- Gray Death
		- White Death
		- Black Death
		- Advancements that require Genes
- Fixed a bug causing the Wither Hit to proc when you damage yourself, such as via Syringes
- Fixed high-temperature Incubator recipes actually requiring low-temperature
	- This effected Cell Growth, Panacea, and Zombify Villager recipes
- Made a new advancement ItemSubPredicate, removing the need for the custom PlayerInventoryChangeEvent
	- This event was only used for two advancements, and the InventoryListener that called it had a memory leak (#44)

# 1.5.3

- Fixed basic Incubator recipes being _incredibly broken_
	- Previously, basic Incubator recipes would use the same instance of the output ItemStack every time.
	- This means that mutating the ItemStack (by, for example, removing it from the machine) would also mutate the one in the recipe.
	- The recipe would then output an ItemStack with size 0, which would be treated as if it had no output at all.
- Fixed Ender Dragon Health Gene not detecting Dragon Health Crystals

# 1.5.4

- Fixed Genes desyncing on client when you change dimensions or respawn (#51)
- Incubators now work as Brewing Stands again
- Substrate Cell Duplication recipes no longer consume the input Cell
- Set Potion Entity recipes are invalid if the potion already has the cell's entity type
- Fixed Set/Unset Anti-Plasmid recipes not working (#50)
- Fixed Virus recipes not working (#48)
- Added these tags to all custom damage types: `#minecraft:no_impact`, `#minecraft:no_anger`, `#minecraft:no_knockback`
- Mod commands can now use any of the following: `/geneticsresequenced`, `/genetics`, `/gr`
- Renamed the command `/gr clearBioluminescenceBlocks` to `/gr clearBioGlow`
- Updated the Patchouli book to show changes to Flight's default required Genes (#49, #46 kinda)

# 1.6.0

- Update NeoForge to 21.1.197
- Update KFF to 5.9.0
- Added a Lava Proof Gene, which makes you immune to direct Lava damage (#67)
- Dragon Health Crystal no longer uses durability, but has its own separate data component (#58)
- The Metal Syringe now increases your entity interaction range attribute by 3 blocks
- Baby mobs inherit their parents' Genes, with a 100% if both parents have it, and a 50% if only one parent has it (#56)
- The Gene Checker now shows what Genes the target can provide, in addition to what it has (#66)
- Immunity Genes now use isInvulnerableTo rather than setting the damage amount to 0, so you no longer flinch from those damages
- Fixed Antigenes not being removed from Syringes when used (#53)
- Fix Black Death recipe (#63)

# 1.6.1

- Removed the Bad Omen Gene, since it functions radically differently than other effects
- Reworked the give genes command, it now uses the syntax `/genetics gene give <gene_id> <targets>`
	- Removes the old `/genetics gene add [fromString/fromRl] <gene> <targets>` thing, which was disgusting