# 1.0.0

- Initial Release

# 1.0.1

- Changed Virus damage source id from `virus` to `geneticsresequenced:virus`
- Mob Sight's Glowing effect now has a duration `maxOf(ServerConfig.mobSightCooldown.get() * 4, 20 * 30)`, as opposed to just 4 times the cooldown (fixes #8)