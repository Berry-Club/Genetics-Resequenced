
# Changelog

## 1.21

### 1.1.0

- Updated to 1.21
- Emerald Heart chat function only worked if Emerald Heart was DISABLED, instead of ENABLED. Fixed that.
- Improved command response messages, for example "Added Claws to Dev" instead of "Added Claws to 1 entities".
- Efficiency Attribute now uses modifiers instead of modifying the attribute base value. No idea why I was doing that originally, that's awful.
- Support Slime now only checks if it should despawn once every 40 ticks rather than every tick, which should improve performance.
- Changed the machines' energy texture to one made by TJKraft
- The Flight gene now is an Attribute, rather than changing the player's ability to fly. Fixes #10
- Item Magnet now has a delay against picking up items dropped by you
- Allowed the Gene Add/Remove commands to use the gene name string instead of the id Resource Location
- Increased the amount of Support Slimes spawned from 1-4 to 3-6
- Poison Proof Gene now uses the new NeoForge Poison damage, rather than checking for magic damage while you have poison effect
- Renamed The Cure to Panacea
- The Coal Generator now allows extracting items from any side (in case of fuels like Lava Buckets, which leave an empty Bucket behind)
- Machines now keep their progress if they run out of power
- MobGeneRegistry and GeneRequirementRegistry loggers are now debug instead of info
- The non-empty DNA Helices and Plasmids in the creative mode tab are now after everything else
- Fixed Incubator and Advanced Incubator from not working (hasRecipe() returned false hasEnoughEnergy was true rather than when false)
- The bubbles in the Incubator and Advanced Incubator now animate slower
- Fixed broken tooltip for Basic Genes in the Plasmid Infuser
- Changed Plasmid tooltip text color to Gray
- Item Magnet will show in the tooltip that an item is blacklisted (configurable)