# 1.0.0

- Initial Release

# 1.0.1

- Changed Virus damage source id from `virus` to `geneticsresequenced:virus`
- Mob Sight's Glowing effect now has a duration `maxOf(ServerConfig.mobSightCooldown.get() * 4, 20 * 30)`, as opposed to just 4 times the cooldown (fixes #8)

# 1.0.2

- Emerald Heart chat function only worked if Emerald Heart was DISABLED, instead of ENABLED. Fixed that.
- Attributes Modifiers now use a UUID rather than just the name. This may cause problems if you already had that attribute modifier.
- Support Slime now only checks if it should despawn once every 40 ticks rather than every tick, which should improve performance.
- Fixed Virus recipes being broken