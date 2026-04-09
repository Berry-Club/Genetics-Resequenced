package dev.aaronhowser.mods.genetics_resequenced.datagen.lang

object ModConfigLang {
	fun add(provider: ModLanguageProvider) {
		provider.add(Client.WOOLY_REMOVES_CAPE, "Wooly removes cape")
		provider.add(Client.DISABLE_PARROT_NARRATOR, "Disable chatterbox narration")
		provider.add(Client.DISABLE_CRINGE_LANG_CHANGE, "Disable cringe language")
		provider.add(Client.SUPPORT_SLIME_RENDER_DEBUG, "Debug Support Slime rendering")
		provider.add(Client.ITEM_MAGNET_BLACKLIST_TOOLTIP, "Item Magnet blacklist tooltip")

		provider.add(Server.General.KEEP_GENES_ON_DEATH, "Keep Genes on death")
		provider.add(Server.General.MINIMUM_COOLDOWN_FOR_NOTIFICATION, "Minimum cooldown for notification")
		provider.add(Server.General.ANTIFIELD_BLOCK_RADIUS, "Anti-Field Block radius")

		provider.add(Server.Machine.MACHINES, "Machines")
		provider.add(Server.Machine.COAL_GENERATOR_ENERGY_PER_TICK, "Coal Generator Energy per tick")
		provider.add(Server.Machine.COAL_GENERATOR_ENERGY_TRANSFER_RATE, "Coal Generator Energy transfer rate")
		provider.add(Server.Machine.COAL_GENERATOR_ENERGY_CAPACITY, "Coal Generator Energy capacity")
		provider.add(Server.Machine.INCUBATOR_TICKS_PER_BREW, "Incubator ticks per brew")
		provider.add(Server.Machine.INCUBATOR_LOW_TEMP_TICK_FACTOR, "Incubator low temp tick factor")

		provider.add(Server.Gene.GENES, "Genes")

		provider.add(Server.Gene.DISABLED_GENES, "Disabled Genes")
		provider.add(Server.Gene.DISABLE_GIVING_PLAYERS_NEGATIVE_GENES, "Disable giving players negative Genes")

		provider.add(Server.Gene.BIOLUMINESCENCE_DURATION, "Bioluminescence duration")
		provider.add(Server.Gene.BIOLUMINESCENCE_COOLDOWN, "Bioluminescence cooldown")
		provider.add(Server.Gene.CHILL_DURATION, "Chill duration")
		provider.add(Server.Gene.CHILL_CHANCE, "Chill chance")
		provider.add(Server.Gene.MILKY_COOLDOWN, "Milky cooldown")
		provider.add(Server.Gene.THORNS_DAMAGE, "Thorns damage")
		provider.add(Server.Gene.THORNS_CHANCE, "Thorns chance")
		provider.add(Server.Gene.THORNS_HUNGER_DRAIN, "Thorns hunger drain")
		provider.add(Server.Gene.ITEM_MAGNET_COOLDOWN, "Item Magnet cooldown")
		provider.add(Server.Gene.XP_MAGNET_COOLDOWN, "XP Magnet cooldown")
		provider.add(Server.Gene.XP_MAGNET_RADIUS, "XP Magnet radius")
		provider.add(Server.Gene.ITEM_MAGNET_RADIUS, "Item Magnet radius")
		provider.add(Server.Gene.NO_HUNGER_COOLDOWN, "No Hunger cooldown")
		provider.add(Server.Gene.PHOTOSYNTHESIS_COOLDOWN, "Photosynthesis cooldown")
		provider.add(Server.Gene.PHOTOSYNTHESIS_SATURATION_AMOUNT, "Photosynthesis saturation amount")
		provider.add(Server.Gene.PHOTOSYNTHESIS_HUNGER_AMOUNT, "Photosynthesis hunger amount")
		provider.add(Server.Gene.NO_HUNGER_MINIMUM, "No Hunger minimum hunger")
		provider.add(Server.Gene.WALL_CLIMB_SPEED, "Wall Climb speed")
		provider.add(Server.Gene.PASSIVES_CHECK_COOLDOWN, "Passives check cooldown")
		provider.add(Server.Gene.MOB_SIGHT_COOLDOWN, "Mob Sight cooldown")
		provider.add(Server.Gene.MOB_SIGHT_RADIUS, "Mob Sight radius")
		provider.add(Server.Gene.JOHNNY_ATTACK_MULTIPLIER, "Johnny attack multiplier")
		provider.add(Server.Gene.SLIMY_DEATH_HEALTH_MULTIPLIER, "Slimy Death health multiplier")
		provider.add(Server.Gene.TELEPORT_DISTANCE, "Teleport distance")
		provider.add(Server.Gene.EGG_COOLDOWN, "Lay Egg cooldown")
		provider.add(Server.Gene.EMERALD_HEART_COOLDOWN, "Emerald Heart cooldown")
		provider.add(Server.Gene.SLIMY_DEATH_COOLDOWN, "Slimy Death cooldown")
		provider.add(Server.Gene.SLIMY_DEATH_DESPAWN_TIME, "Slimy Death despawn time")
		provider.add(Server.Gene.SLIMY_DEATH_DESPAWN_CHECK_TIMER, "Slimy Death despawn check timer")
		provider.add(Server.Gene.CLAWS_DAMAGE, "Claws damage")
		provider.add(Server.Gene.CLAWS_BASE_CHANCE, "Claws base chance")
		provider.add(Server.Gene.TELEPORT_COOLDOWN, "Teleport cooldown")
		provider.add(Server.Gene.MEATY_COOLDOWN, "Meaty cooldown")
		provider.add(Server.Gene.MEATY2_COOLDOWN, "Meaty II cooldown")
		provider.add(Server.Gene.WOOLY_COOLDOWN, "Wooly cooldown")
		provider.add(Server.Gene.EMERALD_HEART_CHAT_CHANCE, "Emerald Heart chat chance")
		provider.add(Server.Gene.DRAGONS_BREATH_COOLDOWN, "Dragon's Breath cooldown")
	}

	object Client {
		const val WOOLY_REMOVES_CAPE = "genetics_resequenced.configuration.woolyRemovesCape"
		const val DISABLE_PARROT_NARRATOR = "genetics_resequenced.configuration.disableParrotNarrator"
		const val DISABLE_CRINGE_LANG_CHANGE = "genetics_resequenced.configuration.disableCringeLangChange"
		const val SUPPORT_SLIME_RENDER_DEBUG = "genetics_resequenced.configuration.supportSlimeRenderDebug"
		const val ITEM_MAGNET_BLACKLIST_TOOLTIP = "genetics_resequenced.configuration.itemMagnetBlacklistTooltip"
	}

	object Server {
		object General {
			const val KEEP_GENES_ON_DEATH = "genetics_resequenced.configuration.keepGenesOnDeath"
			const val MINIMUM_COOLDOWN_FOR_NOTIFICATION =
				"genetics_resequenced.configuration.minimumCooldownForNotification"
			const val ANTIFIELD_BLOCK_RADIUS = "genetics_resequenced.configuration.antifieldBlockRadius"
		}

		object Machine {
			const val MACHINES = "genetics_resequenced.configuration.machines"
			const val COAL_GENERATOR_ENERGY_PER_TICK =
				"genetics_resequenced.configuration.coalGeneratorEnergyPerTick"
			const val COAL_GENERATOR_ENERGY_TRANSFER_RATE =
				"genetics_resequenced.configuration.coalGeneratorEnergyTransferRate"
			const val COAL_GENERATOR_ENERGY_CAPACITY =
				"genetics_resequenced.configuration.coalGeneratorEnergyCapacity"

			const val INCUBATOR_TICKS_PER_BREW = "genetics_resequenced.configuration.incubatorTicksPerBrew"
			const val INCUBATOR_LOW_TEMP_TICK_FACTOR =
				"genetics_resequenced.configuration.incubatorLowTempTickFactor"
		}

		object Gene {
			const val GENES = "genetics_resequenced.configuration.genes"

			const val DISABLED_GENES = "genetics_resequenced.configuration.disabledGenes"
			const val DISABLE_GIVING_PLAYERS_NEGATIVE_GENES =
				"genetics_resequenced.configuration.disableGivingPlayersNegativeGenes"

			const val BIOLUMINESCENCE_DURATION = "genetics_resequenced.configuration.bioluminescenceDuration"
			const val BIOLUMINESCENCE_COOLDOWN = "genetics_resequenced.configuration.bioluminescenceCooldown"

			const val CHILL_DURATION = "genetics_resequenced.configuration.chillDuration"
			const val MILKY_COOLDOWN = "genetics_resequenced.configuration.milkyCooldown"
			const val THORNS_HUNGER_DRAIN = "genetics_resequenced.configuration.thornsHungerDrain"
			const val ITEM_MAGNET_RADIUS = "genetics_resequenced.configuration.itemMagnetRadius"
			const val NO_HUNGER_MINIMUM = "genetics_resequenced.configuration.noHungerMinimum"
			const val THORNS_DAMAGE = "genetics_resequenced.configuration.thornsDamage"
			const val WALL_CLIMB_SPEED = "genetics_resequenced.configuration.wallClimbSpeed"
			const val PASSIVES_CHECK_COOLDOWN = "genetics_resequenced.configuration.passivesCheckCooldown"
			const val MOB_SIGHT_RADIUS = "genetics_resequenced.configuration.mobSightRadius"
			const val PHOTOSYNTHESIS_SATURATION_AMOUNT =
				"genetics_resequenced.configuration.photosynthesisSaturationAmount"
			const val JOHNNY_ATTACK_MULTIPLIER = "genetics_resequenced.configuration.johnnyAttackMultiplier"
			const val SLIMY_DEATH_HEALTH_MULTIPLIER = "genetics_resequenced.configuration.slimyDeathHealthMultiplier"
			const val TELEPORT_DISTANCE = "genetics_resequenced.configuration.teleportDistance"
			const val XP_MAGNET_RADIUS = "genetics_resequenced.configuration.xpMagnetRadius"
			const val XP_MAGNET_COOLDOWN = "genetics_resequenced.configuration.xpMagnetCooldown"
			const val MEATY2_COOLDOWN = "genetics_resequenced.configuration.meaty2Cooldown"
			const val EGG_COOLDOWN = "genetics_resequenced.configuration.eggCooldown"
			const val EMERALD_HEART_COOLDOWN = "genetics_resequenced.configuration.emeraldHeartCooldown"
			const val SLIMY_DEATH_COOLDOWN = "genetics_resequenced.configuration.slimyDeathCooldown"
			const val THORNS_CHANCE = "genetics_resequenced.configuration.thornsChance"
			const val SLIMY_DEATH_DESPAWN_TIME = "genetics_resequenced.configuration.slimyDeathDespawnTime"
			const val PHOTOSYNTHESIS_COOLDOWN = "genetics_resequenced.configuration.photosynthesisCooldown"
			const val MOB_SIGHT_COOLDOWN = "genetics_resequenced.configuration.mobSightCooldown"
			const val CLAWS_DAMAGE = "genetics_resequenced.configuration.clawsDamage"
			const val NO_HUNGER_COOLDOWN = "genetics_resequenced.configuration.noHungerCooldown"
			const val ITEM_MAGNET_COOLDOWN = "genetics_resequenced.configuration.itemMagnetCooldown"
			const val PHOTOSYNTHESIS_HUNGER_AMOUNT = "genetics_resequenced.configuration.photosynthesisHungerAmount"
			const val TELEPORT_COOLDOWN = "genetics_resequenced.configuration.teleportCooldown"
			const val MEATY_COOLDOWN = "genetics_resequenced.configuration.meatyCooldown"
			const val CHILL_CHANCE = "genetics_resequenced.configuration.chillChance"
			const val SLIMY_DEATH_DESPAWN_CHECK_TIMER =
				"genetics_resequenced.configuration.slimyDeathDespawnCheckTimer"
			const val WOOLY_COOLDOWN = "genetics_resequenced.configuration.woolyCooldown"
			const val EMERALD_HEART_CHAT_CHANCE = "genetics_resequenced.configuration.emeraldHeartChatChance"
			const val CLAWS_BASE_CHANCE = "genetics_resequenced.configuration.clawsBaseChance"
			const val DRAGONS_BREATH_COOLDOWN = "genetics_resequenced.configuration.dragonsBreathCooldown"

		}
	}

}