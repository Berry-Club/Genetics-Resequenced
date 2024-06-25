
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