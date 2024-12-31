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
- Mod commands can now use any of the following: "/geneticsresequenced", "/genetics", "gr"
- Renamed the command "/gr clearBioluminescenceBlocks" to "/gr clearBioGlow"
- Updated the Patchouli book to show changes to Flight's default required Genes (#49, #46 kinda)