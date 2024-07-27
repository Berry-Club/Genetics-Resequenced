# Changelog

## 1.21

### 1.1.0

- Updated to 1.21
- Genes are now an actual Registry
- Fixed Incubator and Advanced Incubator from not working (hasRecipe() returned false when hasEnoughEnergy was true
  rather than when false)
- Fixed the Black Death recipe being broken
- Item Magnet will show in the tooltip that an item is blacklisted (configurable)
- Properly sync the player's Genes on client and server on login
- Allowed the Gene Add/Remove commands to use the gene name string instead of the id Resource Location
- The Keep Inventory Gene now uses Data Attachment, which means it persists across server stops
- Efficiency Attribute now uses modifiers instead of modifying the attribute base value. No idea why I was doing that
  originally, that's awful.
- The Flight gene now is an Attribute, rather than changing the player's ability to fly. Fixes #10
- Wall Climbing is no longer an Attribute (Why was it? What was I thinking?)
- The non-empty DNA Helices and Plasmids in the creative mode tab are now after everything else
- Attribute Modifiers given by Genes are now kept on respawn
- Changed the machines' energy texture to one made by TJKraft
- Support Slime now only checks if it should despawn once every 40 ticks rather than every tick, which should improve
  performance.
- Emerald Heart chat function only worked if Emerald Heart was DISABLED, instead of ENABLED. Fixed that.
- Improved command response messages, for example "Added Claws to Dev" instead of "Added Claws to 1 entities".
- Item Magnet now has a delay against picking up items dropped by you
- Increased the amount of Support Slimes spawned from 1-4 to 3-6
- Poison Proof Gene now uses the new NeoForge Poison damage, rather than checking for magic damage while you have poison
  effect
- Renamed The Cure to Panacea
- The Coal Generator now allows extracting items from any side (in case of fuels like Lava Buckets, which leave an empty
  Bucket behind)
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

### 1.1.1

- Updated to NeoForge 21.0.53-beta
- Added EMI support
- Added 4 new Genes: Infested, Oozing, Weaving, and Wind Charged
- Fixed a recipe conflict between Panacea and Zombify Villager potions, Panacea now requires a Regeneration Helix rather
  than Emerald Heart
- Sorted Genes in several places (Sorted firstly by regular/mutation/negative, then by id)
- Fixed a typo in a tooltip
- Forgot the missing : in several places in the mod name

### 1.1.2

- Added loot tables for all the blocks, now they should actually drop themselves!

### 1.1.3

- Updated NeoForge to 21.0.87-beta
- Updated Kotlin for Forge to 5.4.0
- Fixed error spam on server close

### 1.1.4

- Fix issues with JEI

### 1.1.5

- Blocks are no longer instamine
- Blocks now require an Iron Pickaxe to mine
- Fixed chat spam from Lay Egg Gene, possibly others

### 1.1.6

- Update to NeoForge 21.0.113-beta
- Improvements to Infinity Gene. Turns out most of the code wasn't actually reachable, and also was pointless besides. Much simpler now. Also, fixed picking up fired arrows (#13)
- Added support for NeoForge mod config menu
- Rearranged configs

### 1.1.7

- Fixed crash on servers

### 1.1.8

- Fixed crash from trying to scrape invalid entities
- Fixed the Coal Generator, so FE goes up instead of down
- All machine Screen classes now extend the abstract class MachineScreen