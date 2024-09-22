package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
    output: PackOutput
) : LanguageProvider(output, GeneticsResequenced.ID, "en_us") {

    companion object {
        fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args)

        fun getGeneTranslationKey(gene: ResourceKey<Gene>): String {
            val namespace = gene.location().namespace
            val path = gene.location().path

            return "gene.$namespace.$path"
        }
    }

    object Items {
        const val CREATIVE_TAB = "itemGroup.geneticsresequenced"

        const val GUIDE_BOOK = "item.geneticsresequenced.guide_book"
        const val SYRINGE_EMPTY = "item.geneticsresequenced.syringe.empty"
        const val SYRINGE_FULL = "item.geneticsresequenced.syringe.full"
        const val METAL_SYRINGE_EMPTY = "item.geneticsresequenced.metal_syringe.empty"
        const val METAL_SYRINGE_FULL = "item.geneticsresequenced.metal_syringe.full"

        const val POTION_SUBSTRATE = "item.minecraft.potion.effect.geneticsresequenced.substrate"
        const val POTION_CELL_GROWTH = "item.minecraft.potion.effect.geneticsresequenced.cell_growth"
        const val POTION_MUTATION = "item.minecraft.potion.effect.geneticsresequenced.mutation"
        const val POTION_VIRAL_AGENTS = "item.minecraft.potion.effect.geneticsresequenced.viral_agents"
        const val POTION_PANACEA = "item.minecraft.potion.effect.geneticsresequenced.panacea"
        const val POTION_ZOMBIFY_VILLAGER = "item.minecraft.potion.effect.geneticsresequenced.zombify_villager"
        const val SPLASH_POTION_SUBSTRATE = "item.minecraft.splash_potion.effect.geneticsresequenced.substrate"
        const val SPLASH_POTION_CELL_GROWTH = "item.minecraft.splash_potion.effect.geneticsresequenced.cell_growth"
        const val SPLASH_POTION_MUTATION = "item.minecraft.splash_potion.effect.geneticsresequenced.mutation"
        const val SPLASH_POTION_VIRAL_AGENTS =
            "item.minecraft.splash_potion.effect.geneticsresequenced.viral_agents"
        const val SPLASH_POTION_PANACEA = "item.minecraft.splash_potion.effect.geneticsresequenced.panacea"
        const val SPLASH_POTION_ZOMBIFY_VILLAGER =
            "item.minecraft.splash_potion.effect.geneticsresequenced.zombify_villager"
        const val LINGERING_POTION_SUBSTRATE =
            "item.minecraft.lingering_potion.effect.geneticsresequenced.substrate"
        const val LINGERING_POTION_CELL_GROWTH =
            "item.minecraft.lingering_potion.effect.geneticsresequenced.cell_growth"
        const val LINGERING_POTION_MUTATION = "item.minecraft.lingering_potion.effect.geneticsresequenced.mutation"
        const val LINGERING_POTION_VIRAL_AGENTS =
            "item.minecraft.lingering_potion.effect.geneticsresequenced.viral_agents"
        const val LINGERING_POTION_PANACEA = "item.minecraft.lingering_potion.effect.geneticsresequenced.panacea"
        const val LINGERING_POTION_ZOMBIFY_VILLAGER =
            "item.minecraft.lingering_potion.effect.geneticsresequenced.zombify_villager"
        const val TIPPED_ARROW_SUBSTRATE = "item.minecraft.tipped_arrow.effect.geneticsresequenced.substrate"
        const val TIPPED_ARROW_CELL_GROWTH = "item.minecraft.tipped_arrow.effect.geneticsresequenced.cell_growth"
        const val TIPPED_ARROW_MUTATION = "item.minecraft.tipped_arrow.effect.geneticsresequenced.mutation"
        const val TIPPED_ARROW_VIRAL_AGENTS = "item.minecraft.tipped_arrow.effect.geneticsresequenced.viral_agents"
        const val TIPPED_ARROW_PANACEA = "item.minecraft.tipped_arrow.effect.geneticsresequenced.panacea"
        const val TIPPED_ARROW_ZOMBIFY_VILLAGER =
            "item.minecraft.tipped_arrow.effect.geneticsresequenced.zombify_villager"
    }

    object Messages {
        const val SCRAPER_CANT_SCRAPE = "message.geneticsresequenced.scraper.cant_scrape"
        const val CANT_SET_ENTITY = "message.geneticsresequenced.cant_set_entity"
        const val DEATH_GENE_REMOVAL = "message.geneticsresequenced.death_gene_removal"
        const val DEATH_NEGATIVE_GENE_REMOVAL = "message.geneticsresequenced.death_negative_gene_removal"
        const val RECENT_WOOLY = "message.geneticsresequenced.wooly.recent"
        const val RECENT_MEATY = "message.geneticsresequenced.meaty.recent"
        const val RECENT_MILKY = "message.geneticsresequenced.milk.recent"
        const val MILK_MILKED = "message.geneticsresequenced.milk.milked"
        const val SYRINGE_INJECTED = "message.geneticsresequenced.syringe.injected"
        const val SYRINGE_FAILED = "message.geneticsresequenced.syringe.failed"
        const val SYRINGE_CONTAMINATED = "message.geneticsresequenced.syringe.contaminated"
        const val METAL_SYRINGE_MISMATCH = "message.geneticsresequenced.metal_syringe.mismatch"
        const val METAL_SYRINGE_CONTAMINATED = "message.geneticsresequenced.metal_syringe.contaminated"
        const val METAL_SYRINGE_NO_MOBS = "message.geneticsresequenced.metal_syringe.no_mobs"
        const val SUPPORT_SLIME_CREATIVE = "message.geneticsresequenced.support_slime_creative"
        const val SUPPORT_SLIME_PEACEFUL = "message.geneticsresequenced.support_slime_peaceful"
        const val SYRINGE_REMOVE_GENES_SUCCESS = "message.geneticsresequenced.syringe.anti_gene.success"
        const val SYRINGE_REMOVE_GENES_FAIL = "message.geneticsresequenced.syringe.anti_gene.fail"
        const val MISSING_GENE_REQUIREMENTS = "message.geneticsresequenced.gene_missing_requirements"
        const val MISSING_GENE_REQUIREMENTS_LIST = "message.geneticsresequenced.gene_missing_requirements.list"
        const val CRINGE_GRASS = "message.geneticsresequenced.cringe.cured"
        const val CRINGE_ADDED = "message.geneticsresequenced.cringe.resources.add"
        const val CRINGE_REMOVED = "message.geneticsresequenced.cringe.resources.remove"
        const val CRINGE_CONFIG = "message.geneticsresequenced.cringe.resources.tooltip"
        const val CRINGE_RELOADING = "message.geneticsresequenced.cringe.resources.reloading"
        const val SLIME_SPAM = "message.geneticsresequenced.slimy_spam"
    }

    object Tooltips {
        const val GENE = "tooltip.geneticsresequenced.gene"
        const val HELIX_ENTITY = "tooltip.geneticsresequenced.helix_entity"
        const val COPY_GENE = "tooltip.geneticsresequenced.copy_gene_id"
        const val ACTIVE = "tooltip.geneticsresequenced.antifield_active"
        const val INACTIVE = "tooltip.geneticsresequenced.antifield_inactive"
        const val CELL_MOB = "tooltip.geneticsresequenced.dna_item.filled"
        const val CELL_NO_MOB = "tooltip.geneticsresequenced.dna_item.empty"
        const val CELL_CREATIVE = "tooltip.geneticsresequenced.dna_item.creative"
        const val PLASMID_EMPTY = "tooltip.geneticsresequenced.plasmid.empty"
        const val ANTI_PLASMID_EMPTY = "tooltip.geneticsresequenced.anti_plasmid.empty"
        const val PLASMID_GENE = "tooltip.geneticsresequenced.plasmid.gene"
        const val PLASMID_COMPLETE = "tooltip.geneticsresequenced.plasmid.complete"
        const val PLASMID_PROGRESS = "tooltip.geneticsresequenced.plasmid.amount"
        const val SYRINGE_CONTAMINATED = "tooltip.geneticsresequenced.syringe.contaminated"
        const val SYRINGE_OWNER = "tooltip.geneticsresequenced.syringe.blood_owner"
        const val INFUSER_BASIC = "tooltip.geneticsresequenced.plasmid_infuser.basic_gene"
        const val INFUSER_MATCHING = "tooltip.geneticsresequenced.plasmid_infuser.matching_gene"
        const val INFUSER_MISMATCH = "tooltip.geneticsresequenced.plasmid_infuser.different_gene"
        const val INFUSER_CONTAMINATED = "tooltip.geneticsresequenced.plasmid_injector.contaminated"
        const val COAL_GEN_TOTAL_FE = "tooltip.geneticsresequenced.coal_generator.total_fe"
        const val IGNORE_POTION = "tooltip.geneticsresequenced.potion.ignore"
        const val GMO_CHANCE = "tooltip.geneticsresequenced.gmo_cell.chance"
        const val SUBSTRATE_RECIPE = "tooltip.geneticsresequenced.substrate_recipe"
        const val INCUBATOR_SET_HIGH = "tooltip.geneticsresequenced.advanced_incubator.set_high"
        const val INCUBATOR_SET_LOW = "tooltip.geneticsresequenced.advanced_incubator.set_low"
        const val BLACK_DEATH_RECIPE = "tooltip.geneticsresequenced.black_death_recipe"
        const val SYRINGE_ADDING_GENES = "tooltip.geneticsresequenced.syringe.adding"
        const val SYRINGE_REMOVING_GENES = "tooltip.geneticsresequenced.syringe.removing"
        const val GMO_TEMPERATURE_REQUIREMENT = "tooltip.geneticsresequenced.gmo_temperature_requirement"
        const val GMO_CHORUS = "tooltip.geneticsresequenced.gmo_recipe.line3"
        const val GMO_SUCCESS = "tooltip.geneticsresequenced.gmo_recipe.success"
        const val GMO_FAILURE = "tooltip.geneticsresequenced.gmo_recipe.failure"
        const val ITEM_MAGNET_BLACKLIST = "tooltip.geneticsresequenced.item_magnet_blacklist"
        const val FE = "tooltip.geneticsresequenced.fe"
        const val GMO_BASE_CHANCE = "tooltip.geneticsresequenced.gmo.chance"
        const val GMO_OVERCLOCKER_CHANCE = "tooltip.geneticsresequenced.gmo.overclocker"
        const val GMO_CHORUS_CHANCE = "tooltip.geneticsresequenced.gmo.chorus"
    }

    object Commands {
        const val LIST_ALL_GENES = "command.geneticsresequenced.list_all_genes"
        const val NO_GENES = "command.geneticsresequenced.list.no_genes"
        const val THEIR_GENES = "command.geneticsresequenced.list.genes"
        const val REMOVED_LIGHTS = "command.geneticsresequenced.remove_nearby_lights.success"
        const val REMOVED_LIGHTS_RANGE_TOO_HIGH = "command.geneticsresequenced.remove_nearby_lights.range_too_high"
        const val ADD_SINGLE_SUCCESS = "command.geneticsresequenced.add_gene.single_target.success"
        const val ADD_SINGLE_FAIL = "command.geneticsresequenced.add_gene.single_target.fail"
        const val ADD_MULTIPLE_SUCCESS = "command.geneticsresequenced.add_gene.multiple_targets.success"
        const val ADD_MULTIPLE_FAIL = "command.geneticsresequenced.add_gene.multiple_targets.fail"
        const val ADD_ALL_SINGLE = "command.geneticsresequenced.add_all.single_target"
        const val ADD_ALL_MULTIPLE = "command.geneticsresequenced.add_all.multiple_targets"
        const val REMOVE_MULTIPLE_SUCCESS = "command.geneticsresequenced.remove_gene.multiple_targets.success"
        const val REMOVE_MULTIPLE_FAIL = "command.geneticsresequenced.remove_gene.multiple_targets.fail"
        const val REMOVE_SINGLE_SUCCESS = "command.geneticsresequenced.remove_gene.single_target.success"
        const val REMOVE_SINGLE_FAIL = "command.geneticsresequenced.remove_gene.single_target.fail"
        const val REMOVE_ALL_SINGLE = "command.geneticsresequenced.remove_all.single_target"
        const val REMOVE_ALL_MULTIPLE = "command.geneticsresequenced.remove_all.multiple_targets"
    }

    object Cooldown {
        const val STARTED = "cooldown.geneticsresequenced.started"
        const val ENDED = "cooldown.geneticsresequenced.ended"
        const val ON_COOLDOWN = "cooldown.geneticsresequenced.on_cooldown"
    }

    object EMI {
        const val BLOOD_PURIFIER = "emi.category.geneticsresequenced.blood_purifier"
        const val CELL_ANALYZER = "emi.category.geneticsresequenced.cell_analyzer"
        const val DNA_EXTRACTOR = "emi.category.geneticsresequenced.dna_extractor"
        const val DNA_DECRYPTOR = "emi.category.geneticsresequenced.dna_decryptor"
        const val PLASMID_INFUSER = "emi.category.geneticsresequenced.plasmid_infuser"
        const val PLASMID_INJECTOR = "emi.category.geneticsresequenced.plasmid_injector"
        const val INCUBATOR = "emi.category.geneticsresequenced.incubator"

        const val GMO = "emi.category.geneticsresequenced.gmo"
        const val SUBSTRATE_DUPE = "emi.category.geneticsresequenced.cell_dupe"
        const val SET_ENTITY = "emi.category.geneticsresequenced.set_entity"
        const val VIRUS = "emi.category.geneticsresequenced.virus"

        const val DELICATE_TOUCH_TAG = "tag.item.geneticsresequenced.enchantable.delicate_touch"
        const val FIREBALL_TAG = "tag.item.geneticsresequenced.fireball"
        const val MAGNET_BLACKLIST_TAG = "tag.item.geneticsresequenced.magnet_blacklist"
        const val WOOLY_TAG = "tag.item.geneticsresequenced.wooly"
    }

    object Recipe {
        const val MOB = "recipe.geneticsresequenced.mob_gene.mob"
        const val GENE = "recipe.geneticsresequenced.mob_gene.gene"
        const val CHANCE = "recipe.geneticsresequenced.mob_gene.chance"
        const val REQUIRES_POINTS = "recipe.geneticsresequenced.plasmid_infuser.points_required"
        const val BASIC_WORTH = "recipe.geneticsresequenced.plasmid_infuser.basic"
        const val MATCHING_WORTH = "recipe.geneticsresequenced.plasmid_infuser.matching"
        const val INJECTOR_GENES = "recipe.geneticsresequenced.plasmid_injector.genes"
        const val INJECTOR_ANTIGENES = "recipe.geneticsresequenced.plasmid_injector.anti_genes"
        const val SUBSTRATE = "recipe.geneticsresequenced.substrate"
        const val BLACK_DEATH = "recipe.geneticsresequenced.black_death"
    }

    object Info {
        const val ORGANIC_MATTER = "info.geneticsresequenced.organic_matter"
        const val MOB_GENE_ONE = "info.geneticsresequenced.mob_gene.line1"
        const val MOB_GENE_TWO = "info.geneticsresequenced.mob_gene.line2"
        const val REQUIRED_GENES = "info.geneticsresequenced.requires_genes"
    }

    object Genes {
        const val UNKNOWN = "gene.geneticsresequenced.unknown"
        const val DISABLED = "gene.geneticsresequenced.disabled"
    }

    fun addGene(gene: ResourceKey<Gene>, name: String) {
        add(getGeneTranslationKey(gene), name)
    }

    fun addGeneInfo(gene: ResourceKey<Gene>, info: String) {
        add("info.${getGeneTranslationKey(gene)}", info)
    }

    object Advancements {
        const val SCRAPER_DESC = "advancement.geneticsresequenced.scraper.desc"
        const val SYRINGE_TITLE = "advancement.geneticsresequenced.syringe.title"
        const val SYRINGE_DESC = "advancement.geneticsresequenced.syringe.desc"
        const val ANALYZER_TITLE = "advancement.geneticsresequenced.cell_analyzer.title"
        const val ANALYZER_DESC = "advancement.geneticsresequenced.cell_analyzer.desc"
        const val EXTRACTOR_TITLE = "advancement.geneticsresequenced.dna_extractor.title"
        const val EXTRACTOR_DESC = "advancement.geneticsresequenced.dna_extractor.desc"
        const val DECRYPTOR_TITLE = "advancement.geneticsresequenced.dna_decryptor.title"
        const val DECRYPTOR_DESC = "advancement.geneticsresequenced.dna_decryptor.desc"
        const val INFUSER_TITLE = "advancement.geneticsresequenced.plasmid_infuser.title"
        const val INFUSER_DESC = "advancement.geneticsresequenced.plasmid_infuser.desc"
        const val DECRYPT_TITLE = "advancement.geneticsresequenced.decrypt_dna.title"
        const val DECRYPT_DESC = "advancement.geneticsresequenced.decrypt_dna.desc"
        const val INJECTOR_TITLE = "advancement.geneticsresequenced.plasmid_injector.title"
        const val INJECTOR_DESC = "advancement.geneticsresequenced.plasmid_injector.desc"
        const val PURIFIER_TITLE = "advancement.geneticsresequenced.blood_purifier.title"
        const val PURIFIER_DESC = "advancement.geneticsresequenced.blood_purifier.desc"
        const val GET_GENE_TITLE = "advancement.geneticsresequenced.get_gene.title"
        const val GET_GENE_DESC = "advancement.geneticsresequenced.get_gene.desc"
        const val GET_MILKED_TITLE = "advancement.geneticsresequenced.get_milked.title"
        const val GET_MILKED_DESC = "advancement.geneticsresequenced.get_milked.desc"
        const val CRINGE_TITLE = "advancement.geneticsresequenced.pathowogen.title"
        const val CRINGE_DESC = "advancement.geneticsresequenced.pathowogen.desc"
        const val SLIMY_TITLE = "advancement.geneticsresequenced.slimy_death.title"
        const val SLIMY_DESC = "advancement.geneticsresequenced.slimy_death.desc"
        const val FLIGHT_TITLE = "advancement.geneticsresequenced.flight.title"
        const val FLIGHT_DESC = "advancement.geneticsresequenced.flight.desc"
        const val SCARE_TITLE = "advancement.geneticsresequenced.scare.title"
        const val SCARE_DESC = "advancement.geneticsresequenced.scare.desc"
        const val BLACK_DEATH_TITLE = "advancement.geneticsresequenced.black_death.title"
        const val BLACK_DEATH_DESC = "advancement.geneticsresequenced.black_death.desc"
    }

    object Other {
        const val DEATH_SCRAPER = "death.attack.gr_scraper"
        const val DEATH_SYRINGE = "death.attack.gr_syringe"
        const val DEATH_SYRINGE_PICKUP = "death.attack.gr_syringe_pickup"
        const val BLEED_DEATH = "death.attack.gr_bleed"
        const val VIRUS_DEATH = "death.attack.gr_virus"

        const val DELICATE_TOUCH = "enchantment.geneticsresequenced.delicate_touch"
        const val SUPPORT_SLIME = "entity.geneticsresequenced.support_slime"

        const val BOOK_LANDING_TEXT = "book.geneticsresequenced.landing_text"
    }

    object Keys {
        const val CATEGORY = "key.geneticsresequenced.category"
        const val DRAGONS_BREATH = "key.geneticsresequenced.dragons_breath"
        const val TELEPORT = "key.geneticsresequenced.teleport"
    }

    object Config {

        object Client {
            const val WOOLY_REMOVES_CAPE = "geneticsresequenced.configuration.woolyRemovesCape"
            const val DISABLE_PARROT_NARRATOR = "geneticsresequenced.configuration.disableParrotNarrator"
            const val DISABLE_CRINGE_LANG_CHANGE = "geneticsresequenced.configuration.disableCringeLangChange"
            const val SUPPORT_SLIME_RENDER_DEBUG = "geneticsresequenced.configuration.supportSlimeRenderDebug"
            const val ITEM_MAGNET_BLACKLIST_TOOLTIP = "geneticsresequenced.configuration.itemMagnetBlacklistTooltip"
        }

        object Server {
            object General {
                const val KEEP_GENES_ON_DEATH = "geneticsresequenced.configuration.keepGenesOnDeath"
                const val MINIMUM_COOLDOWN_FOR_NOTIFICATION =
                    "geneticsresequenced.configuration.minimumCooldownForNotification"
                const val ANTIFIELD_BLOCK_RADIUS = "geneticsresequenced.configuration.antifieldBlockRadius"
            }

            object Machine {
                const val MACHINES = "geneticsresequenced.configuration.machines"
                const val COAL_GENERATOR_ENERGY_PER_TICK =
                    "geneticsresequenced.configuration.coalGeneratorEnergyPerTick"
                const val COAL_GENERATOR_ENERGY_TRANSFER_RATE =
                    "geneticsresequenced.configuration.coalGeneratorEnergyTransferRate"
                const val COAL_GENERATOR_ENERGY_CAPACITY =
                    "geneticsresequenced.configuration.coalGeneratorEnergyCapacity"

                const val INCUBATOR_TICKS_PER_BREW = "geneticsresequenced.configuration.incubatorTicksPerBrew"
                const val INCUBATOR_LOW_TEMP_TICK_FACTOR =
                    "geneticsresequenced.configuration.incubatorLowTempTickFactor"
            }

            object Gene {
                const val GENES = "geneticsresequenced.configuration.genes"

                const val DISABLED_GENES = "geneticsresequenced.configuration.disabledGenes"
                const val DISABLE_GIVING_PLAYERS_NEGATIVE_GENES =
                    "geneticsresequenced.configuration.disableGivingPlayersNegativeGenes"

                const val BIOLUMINESCENCE_DURATION = "geneticsresequenced.configuration.bioluminescenceDuration"
                const val BIOLUMINESCENCE_COOLDOWN = "geneticsresequenced.configuration.bioluminescenceCooldown"

                const val CHILL_DURATION = "geneticsresequenced.configuration.chillDuration"
                const val MILKY_COOLDOWN = "geneticsresequenced.configuration.milkyCooldown"
                const val THORNS_HUNGER_DRAIN = "geneticsresequenced.configuration.thornsHungerDrain"
                const val ITEM_MAGNET_RADIUS = "geneticsresequenced.configuration.itemMagnetRadius"
                const val NO_HUNGER_MINIMUM = "geneticsresequenced.configuration.noHungerMinimum"
                const val THORNS_DAMAGE = "geneticsresequenced.configuration.thornsDamage"
                const val WALL_CLIMB_SPEED = "geneticsresequenced.configuration.wallClimbSpeed"
                const val PASSIVES_CHECK_COOLDOWN = "geneticsresequenced.configuration.passivesCheckCooldown"
                const val MOB_SIGHT_RADIUS = "geneticsresequenced.configuration.mobSightRadius"
                const val PHOTOSYNTHESIS_SATURATION_AMOUNT =
                    "geneticsresequenced.configuration.photosynthesisSaturationAmount"
                const val JOHNNY_ATTACK_MULTIPLIER = "geneticsresequenced.configuration.johnnyAttackMultiplier"
                const val SLIMY_DEATH_HEALTH_MULTIPLIER = "geneticsresequenced.configuration.slimyDeathHealthMultiplier"
                const val TELEPORT_DISTANCE = "geneticsresequenced.configuration.teleportDistance"
                const val XP_MAGNET_RADIUS = "geneticsresequenced.configuration.xpMagnetRadius"
                const val XP_MAGNET_COOLDOWN = "geneticsresequenced.configuration.xpMagnetCooldown"
                const val MEATY2_COOLDOWN = "geneticsresequenced.configuration.meaty2Cooldown"
                const val EGG_COOLDOWN = "geneticsresequenced.configuration.eggCooldown"
                const val EMERALD_HEART_COOLDOWN = "geneticsresequenced.configuration.emeraldHeartCooldown"
                const val SLIMY_DEATH_COOLDOWN = "geneticsresequenced.configuration.slimyDeathCooldown"
                const val THORNS_CHANCE = "geneticsresequenced.configuration.thornsChance"
                const val SLIMY_DEATH_DESPAWN_TIME = "geneticsresequenced.configuration.slimyDeathDespawnTime"
                const val PHOTOSYNTHESIS_COOLDOWN = "geneticsresequenced.configuration.photosynthesisCooldown"
                const val MOB_SIGHT_COOLDOWN = "geneticsresequenced.configuration.mobSightCooldown"
                const val CLAWS_DAMAGE = "geneticsresequenced.configuration.clawsDamage"
                const val NO_HUNGER_COOLDOWN = "geneticsresequenced.configuration.noHungerCooldown"
                const val ITEM_MAGNET_COOLDOWN = "geneticsresequenced.configuration.itemMagnetCooldown"
                const val PHOTOSYNTHESIS_HUNGER_AMOUNT = "geneticsresequenced.configuration.photosynthesisHungerAmount"
                const val TELEPORT_COOLDOWN = "geneticsresequenced.configuration.teleportCooldown"
                const val MEATY_COOLDOWN = "geneticsresequenced.configuration.meatyCooldown"
                const val CHILL_CHANCE = "geneticsresequenced.configuration.chillChance"
                const val SLIMY_DEATH_DESPAWN_CHECK_TIMER =
                    "geneticsresequenced.configuration.slimyDeathDespawnCheckTimer"
                const val WOOLY_COOLDOWN = "geneticsresequenced.configuration.woolyCooldown"
                const val EMERALD_HEART_CHAT_CHANCE = "geneticsresequenced.configuration.emeraldHeartChatChance"
                const val CLAWS_BASE_CHANCE = "geneticsresequenced.configuration.clawsBaseChance"
                const val DRAGONS_BREATH_COOLDOWN = "geneticsresequenced.configuration.dragonsBreathCooldown"

            }
        }
    }


    override fun addTranslations() {

        add(Items.CREATIVE_TAB, "Genetics: Resequenced")
        add(Items.GUIDE_BOOK, "Big Book of Genetics")
        add(Items.SYRINGE_EMPTY, "Empty Syringe")
        add(Items.SYRINGE_FULL, "Full Syringe")
        add(Items.METAL_SYRINGE_EMPTY, "Empty Metal Syringe")
        add(Items.METAL_SYRINGE_FULL, "Full Metal Syringe")
        addItem(ModItems.DRAGON_HEALTH_CRYSTAL, "Dragon Health Crystal")
        addItem(ModItems.ORGANIC_MATTER, "Organic Matter")
        addItem(ModItems.CELL, "Cell")
        addItem(ModItems.GMO_CELL, "Genetically Modified Cell")
        addItem(ModItems.ANTI_FIELD_ORB, "Anti-Field Orb")
        addItem(ModItems.SCRAPER, "Scraper")
        addItem(ModItems.DNA_HELIX, "DNA Helix")
        addItem(ModItems.PLASMID, "Plasmid")
        addItem(ModItems.OVERCLOCKER, "Overclocker")
        addItem(ModItems.FRIENDLY_SLIME_SPAWN_EGG, "Support Slime Spawn Egg")
        addItem(ModItems.ANTI_PLASMID, "Anti-Plasmid")

        addEffect(ModEffects.BLEED, "Bleed")
        addEffect(ModEffects.SUBSTRATE, "Substrate")
        addEffect(ModEffects.CELL_GROWTH, "Cell Growth")
        addEffect(ModEffects.MUTATION, "Mutation")
        addEffect(ModEffects.VIRAL_AGENTS, "Viral Agents")
        addEffect(ModEffects.PANACEA, "Panacea")
        addEffect(ModEffects.ZOMBIFY_VILLAGER, "Zombify Villager")

        add(Items.POTION_SUBSTRATE, "Organic Substrate")
        add(Items.POTION_CELL_GROWTH, "Potion of Cell Growth")
        add(Items.POTION_MUTATION, "Potion of Mutation")
        add(Items.POTION_VIRAL_AGENTS, "Potion of Viral Agents")
        add(Items.POTION_PANACEA, "Potion of Panacea")
        add(Items.POTION_ZOMBIFY_VILLAGER, "Potion of Zombify Villager")

        add(Items.SPLASH_POTION_SUBSTRATE, "Splash Potion of Organic Substrate")
        add(Items.SPLASH_POTION_CELL_GROWTH, "Splash Potion of Cell Growth")
        add(Items.SPLASH_POTION_MUTATION, "Splash Potion of Mutation")
        add(Items.SPLASH_POTION_VIRAL_AGENTS, "Splash Potion of Viral Agents")
        add(Items.SPLASH_POTION_PANACEA, "Splash Potion of Panacea")
        add(Items.SPLASH_POTION_ZOMBIFY_VILLAGER, "Splash Potion of Zombify Villager")
        add(Items.LINGERING_POTION_SUBSTRATE, "Lingering Potion of Organic Substrate")
        add(Items.LINGERING_POTION_CELL_GROWTH, "Lingering Potion of Cell Growth")
        add(Items.LINGERING_POTION_MUTATION, "Lingering Potion of Mutation")
        add(Items.LINGERING_POTION_VIRAL_AGENTS, "Lingering Potion of Viral Agents")
        add(Items.LINGERING_POTION_PANACEA, "Lingering Potion of Panacea")
        add(Items.LINGERING_POTION_ZOMBIFY_VILLAGER, "Lingering Potion of Zombify Villager")
        add(Items.TIPPED_ARROW_SUBSTRATE, "Arrow of Organic Substrate")
        add(Items.TIPPED_ARROW_CELL_GROWTH, "Arrow of Cell Growth")
        add(Items.TIPPED_ARROW_MUTATION, "Arrow of Mutation")
        add(Items.TIPPED_ARROW_VIRAL_AGENTS, "Arrow of Viral Agents")
        add(Items.TIPPED_ARROW_PANACEA, "Arrow of Panacea")
        add(Items.TIPPED_ARROW_ZOMBIFY_VILLAGER, "Arrow of Zombify Villager")

        add(Messages.SCRAPER_CANT_SCRAPE, "%s cannot be scraped.")
        add(Messages.CANT_SET_ENTITY, "Cannot set to this entity.")
        add(Messages.DEATH_GENE_REMOVAL, "Death has reset your Genes!")
        add(Messages.DEATH_NEGATIVE_GENE_REMOVAL, "Death has remove your negative Genes!")
        add(Messages.RECENT_WOOLY, "This entity has already been sheared recently!")
        add(Messages.RECENT_MEATY, "This entity has already been meated recently!")
        add(Messages.RECENT_MILKY, "This entity has already been milked recently!")
        add(Messages.MILK_MILKED, "You have been milked!")
        add(Messages.SYRINGE_INJECTED, "You have gained the %1\$s Gene!")
        add(Messages.SYRINGE_FAILED, "You have failed to gain the %1\$s Gene!")
        add(Messages.SYRINGE_CONTAMINATED, "You can't inject yourself with contaminated blood!")
        add(Messages.METAL_SYRINGE_MISMATCH, "This Syringe is for a different entity!")
        add(Messages.METAL_SYRINGE_CONTAMINATED, "You can't inject entities with contaminated blood!")
        add(Messages.METAL_SYRINGE_NO_MOBS, "Mobs cannot have the %1\$s Gene!")
        add(
            Messages.SUPPORT_SLIME_CREATIVE,
            "Support Slimes despawn with no owner! Give yourself the %1\$s Gene to stop them from despawning!"
        )
        add(
            Messages.SUPPORT_SLIME_PEACEFUL,
            "Support slimes are technically Slimes, which means they can't exist in Peaceful mode!"
        )
        add(Messages.SYRINGE_REMOVE_GENES_SUCCESS, "You have removed the %s Gene!")
        add(Messages.SYRINGE_REMOVE_GENES_FAIL, "The %s Gene was not removed as you did not have it!")
        add(
            Messages.MISSING_GENE_REQUIREMENTS,
            "You feel the %s Gene fade away...\nIt seems you §cdo not meet the requirements§r for it."
        )
        add(Messages.MISSING_GENE_REQUIREMENTS_LIST, "Required Genes:")
        add(Messages.CRINGE_GRASS, "With the touch of Grass, you feel the cringe leave your body.")
        add(
            Messages.CRINGE_ADDED,
            "You feel the cringe entering your body, taking over. Your perception of the world changes in %d..."
        )
        add(Messages.CRINGE_REMOVED, "You feel yourself become more based, the cringe leaving the world in %d...")
        add(
            Messages.CRINGE_CONFIG,
            "You can disable this in the client config!\n\nYour language is reset to normal when you leave the game."
        )
        add(Messages.CRINGE_RELOADING, "Reloading resources now!")
        add(Messages.SLIME_SPAM, "%s's Slime %d")

        addBlock(ModBlocks.BIOLUMINESCENCE_BLOCK, "Bioluminescence Glow")
        addBlock(ModBlocks.CELL_ANALYZER, "Cell Analyzer")
        addBlock(ModBlocks.COAL_GENERATOR, "Coal Generator")
        addBlock(ModBlocks.DNA_DECRYPTOR, "DNA Decryptor")
        addBlock(ModBlocks.DNA_EXTRACTOR, "DNA Extractor")
        addBlock(ModBlocks.BLOOD_PURIFIER, "Blood Purifier")
        addBlock(ModBlocks.INCUBATOR, "Incubator")
        addBlock(ModBlocks.ADVANCED_INCUBATOR, "Advanced Incubator")
        addBlock(ModBlocks.PLASMID_INFUSER, "Plasmid Infuser")
        addBlock(ModBlocks.PLASMID_INJECTOR, "Plasmid Injector")
        addBlock(ModBlocks.ANTI_FIELD_BLOCK, "Anti-Field Block")

        add(Tooltips.GENE, "Gene: %1\$s")
        add(Tooltips.HELIX_ENTITY, "Entity: %1\$s")
        add(Tooltips.COPY_GENE, "Click to copy ID:\n %s")
        add(Tooltips.ACTIVE, "Active")
        add(Tooltips.INACTIVE, "Inactive")
        add(Tooltips.CELL_MOB, "Cell type: %s")
        add(Tooltips.CELL_NO_MOB, "Empty")
        add(Tooltips.CELL_CREATIVE, "Creative: Right-click mob to set.")
        add(Tooltips.PLASMID_EMPTY, "Empty Plasmid")
        add(Tooltips.ANTI_PLASMID_EMPTY, "Empty Anti-Plasmid")
        add(Tooltips.PLASMID_GENE, "Contains Gene: %s")
        add(Tooltips.PLASMID_COMPLETE, "Plasmid is complete!")
        add(Tooltips.PLASMID_PROGRESS, "DNA Points: %d/%d")
        add(Tooltips.SYRINGE_CONTAMINATED, "Contaminated Blood")
        add(Tooltips.SYRINGE_OWNER, "Blood: %1\$s")
        add(Tooltips.INFUSER_BASIC, "+1 DNA Point")
        add(Tooltips.INFUSER_MATCHING, "+2 DNA Points")
        add(Tooltips.INFUSER_MISMATCH, "Not applicable")
        add(Tooltips.INFUSER_CONTAMINATED, "You can't inject Genes into Contaminated Blood!")
        add(Tooltips.COAL_GEN_TOTAL_FE, "Stack total: %d FE")
        add(Tooltips.IGNORE_POTION, "Don't craft this! It does nothing!")
        add(Tooltips.GMO_CHANCE, "Chance: %d%%")
        add(Tooltips.SUBSTRATE_RECIPE, "Each Organic Substrate is replaced with a copy of the Cell.")
        add(Tooltips.INCUBATOR_SET_HIGH, "Set temperature to high")
        add(Tooltips.INCUBATOR_SET_LOW, "Set temperature to low")
        add(Tooltips.BLACK_DEATH_RECIPE, "Requires a Syringe that has every negative Gene.")
        add(Tooltips.SYRINGE_ADDING_GENES, "Adding Genes:")
        add(Tooltips.SYRINGE_REMOVING_GENES, "Removing:")
        add(Tooltips.GMO_TEMPERATURE_REQUIREMENT, "Advanced Incubator §lmust be low temperature§r")
        add(Tooltips.GMO_CHORUS, "Overclockers lower your chance, but Chorus Fruits increase it!")
        add(Tooltips.ITEM_MAGNET_BLACKLIST, "Blacklisted from Item Magnet Gene")
        add(Tooltips.FE, "%1\$s/%2\$s FE")
        add(Tooltips.GMO_BASE_CHANCE, "%1\$s Gene base chance: %2\$d%%")
        add(Tooltips.GMO_OVERCLOCKER_CHANCE, "%1\$d Overclockers lower chance to %2\$d%%")
        add(Tooltips.GMO_CHORUS_CHANCE, "%1\$d Chorus Fruit increase chance to %2\$d%%")
        add(Tooltips.GMO_SUCCESS, "Result on success")
        add(Tooltips.GMO_FAILURE, "Result on failure")

        add(Commands.LIST_ALL_GENES, "Gene List:")
        add(Commands.NO_GENES, "No Genes found!")
        add(Commands.THEIR_GENES, "%1\$s's Genes:")
        add(Commands.REMOVED_LIGHTS, "Removed %d nearby bioluminescence light sources.")
        add(Commands.REMOVED_LIGHTS_RANGE_TOO_HIGH, "Range too high! Must be between 1 and 100.")
        add(Commands.ADD_SINGLE_SUCCESS, "Added %1\$s to %2\$s!")
        add(Commands.ADD_SINGLE_FAIL, "Failed to add %1\$s to %2\$s!")
        add(Commands.ADD_MULTIPLE_SUCCESS, "Added %1\$s to %2\$d entities!")
        add(Commands.ADD_MULTIPLE_FAIL, "Failed to add %1\$s to %2\$d entities!")
        add(Commands.ADD_ALL_SINGLE, "Added all positive Genes to %s!")
        add(Commands.ADD_ALL_MULTIPLE, "Added all positive Genes to %d entities!")
        add(Commands.REMOVE_MULTIPLE_SUCCESS, "Removed %1\$s from %2\$d entities!")
        add(Commands.REMOVE_MULTIPLE_FAIL, "Failed to remove %1\$s from %2\$d entities!")
        add(Commands.REMOVE_SINGLE_SUCCESS, "Removed %1\$s from %2\$s!")
        add(Commands.REMOVE_SINGLE_FAIL, "Failed to remove %1\$s from %2\$s!")
        add(Commands.REMOVE_ALL_SINGLE, "Removed all Genes from %s!")
        add(Commands.REMOVE_ALL_MULTIPLE, "Removed all Genes from %d entities!")

        add(Cooldown.STARTED, " triggered! Cooldown started: %s")
        add(Cooldown.ENDED, "%s has come off cooldown!")
        add(Cooldown.ON_COOLDOWN, "%s is on cooldown!")

        add(Recipe.MOB, "Mob: %1\$s")
        add(Recipe.GENE, "Gene: %1\$s")
        add(Recipe.CHANCE, "Chance: %d%%")
        add(Recipe.REQUIRES_POINTS, "%1\$s requires %2\$d points of DNA")
        add(Recipe.BASIC_WORTH, "Basic Genes = 1 point")
        add(Recipe.MATCHING_WORTH, "Matching Genes = 2 points")
        add(Recipe.INJECTOR_GENES, "Syringes can have as many Genes as you want!")
        add(Recipe.INJECTOR_ANTIGENES, "Syringes can have as many Genes (or Anti-Genes) as you want!")
        add(Recipe.BLACK_DEATH, "Requires all negative Genes")
        add(Recipe.SUBSTRATE, "Duplicates the Cell for each Organic Substrate used")

        add(EMI.BLOOD_PURIFIER, "Blood Purifier")
        add(EMI.CELL_ANALYZER, "Cell Analyzer")
        add(EMI.DNA_EXTRACTOR, "DNA Extractor")
        add(EMI.DNA_DECRYPTOR, "DNA Decryptor")
        add(EMI.PLASMID_INFUSER, "Plasmid Infuser")
        add(EMI.PLASMID_INJECTOR, "Plasmid Injector")
        add(EMI.GMO, "GMO Cell Incubating")
        add(EMI.SET_ENTITY, "Set Potion Entity")
        add(EMI.SUBSTRATE_DUPE, "Substrate Cell Duplication")
        add(EMI.VIRUS, "Virus Cultivation")

        add(EMI.DELICATE_TOUCH_TAG, "Delicate Touch Enchantable")
        add(EMI.FIREBALL_TAG, "Usable for \"Shoot Fireballs\" Gene")
        add(EMI.MAGNET_BLACKLIST_TAG, "Blacklisted from \"Item Magnet\" Gene")
        add(EMI.WOOLY_TAG, "Can shear entities with the \"Wooly\" or \"Meaty\" Genes")

        add(Info.ORGANIC_MATTER, "Acquired by clicking a %1\$s with a Scraper.")
        add(Info.MOB_GENE_ONE, "%1\$s has these Genes:")
        add(Info.MOB_GENE_TWO, "\n%d%% of %2\$s")
        add(Info.REQUIRED_GENES, "Required Genes:")
        addGeneInfo(ModGenes.CLAWS_TWO, "Doubles the chances of inflicting Bleeding on hit.")
        addGeneInfo(
            ModGenes.EFFICIENCY_FOUR,
            "Increases your mining speed as if you were using an Efficiency IV tool.\n\nStacks with Haste, as well as the actual Efficiency enchantment!"
        )
        addGeneInfo(ModGenes.FLIGHT, "Gives you Creative mode style flight.")
        addGeneInfo(ModGenes.HASTE_TWO, "Gives you the Haste II potion effect.")
        addGeneInfo(ModGenes.MEATY_TWO, "Causes you to occasionally drop a cooked Porkchop.")
        addGeneInfo(ModGenes.PHOTOSYNTHESIS, "Slowly feeds you when in direct sunlight.")
        addGeneInfo(ModGenes.REGENERATION_FOUR, "Gives you the Regeneration IV potion effect.")
        addGeneInfo(ModGenes.RESISTANCE_TWO, "Give you the Resistance II potion effect.")
        addGeneInfo(ModGenes.SPEED_FOUR, "Give you the Speed IV potion effect.")
        addGeneInfo(ModGenes.SPEED_TWO, "Give you the Speed II potion effect.")
        addGeneInfo(ModGenes.STRENGTH_TWO, "Give you the Strength II potion effect.")
        addGeneInfo(ModGenes.SCARE_ZOMBIES, "Makes Zombies run away from you.")
        addGeneInfo(ModGenes.SCARE_SPIDERS, "Makes Spiders and Cave Spiders run away from you.")
        addGeneInfo(ModGenes.BIOLUMINESCENCE, "Makes you leave behind a trail of light, which lasts a short while.")
        addGeneInfo(ModGenes.CLAWS, "Has a chance of inflicting Bleeding on hit.\n\nBleeding deals damage over time.")
        addGeneInfo(
            ModGenes.DRAGON_BREATH,
            "Allows you to use the \"Dragon's Breath\" keybind to fire a dragon fireball."
        )
        addGeneInfo(ModGenes.EAT_GRASS, "Allows you to right-click Grass Blocks to regain hunger.")
        addGeneInfo(
            ModGenes.EFFICIENCY,
            "Increases your mining speed as if you were using an Efficiency tool.\n\nStacks with Haste, as well as the actual Efficiency enchantment!"
        )
        addGeneInfo(ModGenes.EMERALD_HEART, "Spawns an Emerald when you die.")
        addGeneInfo(
            ModGenes.ENDER_DRAGON_HEALTH,
            "Blocks damage if you're holding a Dragon Health Crystal.\n\nDamage is instead dealt to the Crystal's durability."
        )
        addGeneInfo(
            ModGenes.EXPLOSIVE_EXIT,
            "Makes you explode when you die, if you're holding at least 5 Gunpowder.\n\nEntities with the Gene do not need the Gunpowder."
        )
        addGeneInfo(ModGenes.FIRE_PROOF, "Makes you immune to fire damage, and immediately extinguishes you.")
        addGeneInfo(ModGenes.HASTE, "Gives you the Haste potion effect.")
        addGeneInfo(ModGenes.INFINITY, "Allows you to use Bows without any Arrows in your inventory.")
        addGeneInfo(ModGenes.INVISIBLE, "Gives you the Invisibility potion effect.")
        addGeneInfo(
            ModGenes.ITEM_MAGNET,
            "Makes you pick up all nearby items.\n\nDisables when sneaking, when holding an active Anti-Field Orb, or near an active Anti-Field Block."
        )
        addGeneInfo(ModGenes.JUMP_BOOST, "Gives you the Jump Boost potion effect.")
        addGeneInfo(ModGenes.KEEP_INVENTORY, "Makes you keep your inventory when you die.")
        addGeneInfo(ModGenes.LAY_EGG, "Makes you lay an Egg occasionally.")
        addGeneInfo(ModGenes.LUCK, "Gives you the Luck potion effect.")
        addGeneInfo(ModGenes.MEATY, "Allows you to be sheared for a raw Porkchop.")
        addGeneInfo(ModGenes.MILKY, "Allows you to be milked with a Bucket.")
        addGeneInfo(ModGenes.MOB_SIGHT, "Gives all nearby entities the Glowing potion effect.")
        addGeneInfo(ModGenes.MORE_HEARTS, "Gives you 10 additional hearts.")
        addGeneInfo(ModGenes.MORE_HEARTS_TWO, "Gives you 10 MORE additional hearts.")
        addGeneInfo(ModGenes.NIGHT_VISION, "Gives you the Night Vision potion effect.")
        addGeneInfo(ModGenes.NO_FALL_DAMAGE, "Makes you immune to fall damage.")
        addGeneInfo(ModGenes.NO_HUNGER, "Prevents your hunger from going below halfway.")
        addGeneInfo(ModGenes.POISON_IMMUNITY, "Automatically removes Poison.")
        addGeneInfo(ModGenes.REGENERATION, "Gives you the Regeneration potion effect.")
        addGeneInfo(ModGenes.RESISTANCE, "Gives you the Resistance potion effect.")
        addGeneInfo(ModGenes.SCARE_CREEPERS, "Makes Creepers run away from you.")
        addGeneInfo(ModGenes.SCARE_SKELETONS, "Makes Skeletons run away from you.")
        addGeneInfo(ModGenes.SHOOT_FIREBALLS, "Lets you shoot Fire Charges when you right-click Blaze Rods.")
        addGeneInfo(
            ModGenes.SLIMY_DEATH,
            "When you die, cancels it and spawns some Support Slimes to help you fight.\n\nHas a long cooldown."
        )
        addGeneInfo(ModGenes.SPEED, "Gives you the Speed potion effect.")
        addGeneInfo(ModGenes.STEP_ASSIST, "Lets you step up full blocks.")
        addGeneInfo(ModGenes.STRENGTH, "Gives you the Strength potion effect.")
        addGeneInfo(
            ModGenes.TELEPORT,
            "Allows you to use the \"Teleport\" keybind to teleport a short distance where you're looking."
        )
        addGeneInfo(
            ModGenes.THORNS,
            "If you take damage while wearing either no Chestplate or a Leather Chestplate, there's a chance of reflecting some of the damage back."
        )
        addGeneInfo(ModGenes.WALL_CLIMBING, "Lets you climb walls like a Spider.")
        addGeneInfo(ModGenes.WATER_BREATHING, "Keeps your air meter full.")
        addGeneInfo(ModGenes.WITHER_HIT, "Melee attacks inflict the Wither potion effect.")
        addGeneInfo(ModGenes.WITHER_PROOF, "Makes you immune to the Wither potion effect.")
        addGeneInfo(ModGenes.WOOLY, "Lets you be sheared for Wool.")
        addGeneInfo(
            ModGenes.XP_MAGNET,
            "Makes you pick up all nearby xp.\n\nDisables when sneaking, when holding an active Anti-Field Orb, or near an active Anti-Field Block."
        )
        addGeneInfo(ModGenes.BLINDNESS, "Inflicts the Blindness potion effect.")
        addGeneInfo(ModGenes.CURSED, "Inflicts the Cursed potion effect.")
        addGeneInfo(ModGenes.FLAMBE, "Constantly lights you on fire")
        addGeneInfo(ModGenes.HUNGER, "Inflicts the Hunger potion effect.")
        addGeneInfo(ModGenes.LEVITATION, "Inflicts the Levitation potion effect.")
        addGeneInfo(ModGenes.MINING_FATIGUE, "Inflicts the Mining Fatigue potion effect.")
        addGeneInfo(ModGenes.NAUSEA, "Inflicts the Nausea potion effect.")
        addGeneInfo(ModGenes.POISON, "Inflicts the Poison potion effect.")
        addGeneInfo(ModGenes.POISON_FOUR, "Inflicts the Poison IV potion effect.")
        addGeneInfo(ModGenes.SLOWNESS, "Inflicts the Slowness potion effect.")
        addGeneInfo(ModGenes.SLOWNESS_SIX, "Inflicts the Slowness IV potion effect.")
        addGeneInfo(ModGenes.SLOWNESS_FOUR, "Inflicts the Slowness VI potion effect.")
        addGeneInfo(ModGenes.WEAKNESS, "Inflicts the Weakness potion effect.")
        addGeneInfo(ModGenes.WITHER, "Inflicts the Wither potion effect.")
        addGeneInfo(ModGenes.BLACK_DEATH, "Instantly kills.")
        addGeneInfo(ModGenes.GREEN_DEATH, "Slowly kills Creepers.")
        addGeneInfo(ModGenes.WHITE_DEATH, "Slowly kills monsters.")
        addGeneInfo(ModGenes.GRAY_DEATH, "Slowly kills ageable mobs.")
        addGeneInfo(ModGenes.UN_UNDEATH, "Slowly kills the undead.")
        addGeneInfo(
            ModGenes.CHATTERBOX,
            "Your chat messages are automatically read by the narrator (within 64 blocks).\n\nCan be disabled in the client config."
        )
        addGeneInfo(
            ModGenes.CRINGE,
            "Makes you a Discord moderator.\n\n(UwU-fies your outgoing chat messages, and sets your language to LOLCAT)"
        )
        addGeneInfo(ModGenes.KNOCKBACK, "Increases the knockback of your attacks.")
        addGeneInfo(ModGenes.BAD_OMEN, "Inflicts the Bad Omen potion effect.")
        addGeneInfo(ModGenes.JOHNNY, "Deal more damage using Axes")
        addGeneInfo(ModGenes.CHILLING, "Has a chance of inflicting freezing damage on hit.")
        addGeneInfo(ModGenes.REACHING, "Increases your reach by 1.25 times.")

        add(Genes.UNKNOWN, "Unknown")
        add(Genes.DISABLED, " (Disabled)")

        addGene(ModGenes.BASIC, "Basic")
        addGene(ModGenes.HASTE_TWO, "Haste II")
        addGene(ModGenes.EFFICIENCY_FOUR, "Efficiency IV")
        addGene(ModGenes.REGENERATION_FOUR, "Regeneration IV")
        addGene(ModGenes.SPEED_FOUR, "Speed IV")
        addGene(ModGenes.SPEED_TWO, "Speed II")
        addGene(ModGenes.RESISTANCE_TWO, "Resistance II")
        addGene(ModGenes.STRENGTH_TWO, "Strength II")
        addGene(ModGenes.MEATY_TWO, "Meaty II")
        addGene(ModGenes.MORE_HEARTS_TWO, "More Hearts II")
        addGene(ModGenes.INVISIBLE, "Invisibility")
        addGene(ModGenes.FLIGHT, "Flight")
        addGene(ModGenes.LUCK, "Luck")
        addGene(ModGenes.SCARE_ZOMBIES, "Scare Zombies")
        addGene(ModGenes.SCARE_SPIDERS, "Scare Spiders")
        addGene(ModGenes.THORNS, "Thorns")
        addGene(ModGenes.CLAWS_TWO, "Claws II")
        addGene(ModGenes.CHATTERBOX, "Chatterbox")
        addGene(ModGenes.CHILLING, "Chilling")
        addGene(ModGenes.DRAGON_BREATH, "Dragon's Breath")
        addGene(ModGenes.EAT_GRASS, "Eat Grass")
        addGene(ModGenes.EMERALD_HEART, "Emerald Heart")
        addGene(ModGenes.ENDER_DRAGON_HEALTH, "Ender Dragon Health")
        addGene(ModGenes.EXPLOSIVE_EXIT, "Explosive Exit")
        addGene(ModGenes.FIRE_PROOF, "Fire Proof")
        addGene(ModGenes.ITEM_MAGNET, "Item Magnet")
        addGene(ModGenes.JOHNNY, "Johnny")
        addGene(ModGenes.JUMP_BOOST, "Jump Boost")
        addGene(ModGenes.MILKY, "Milky")
        addGene(ModGenes.MORE_HEARTS, "More Hearts")
        addGene(ModGenes.NIGHT_VISION, "Night Vision")
        addGene(ModGenes.NO_FALL_DAMAGE, "No Fall Damage")
        addGene(ModGenes.PHOTOSYNTHESIS, "Photosynthesis")
        addGene(ModGenes.POISON_IMMUNITY, "Poison Immunity")
        addGene(ModGenes.RESISTANCE, "Resistance")
        addGene(ModGenes.KNOCKBACK, "Knockback")
        addGene(ModGenes.KEEP_INVENTORY, "Keep Inventory")
        addGene(ModGenes.SCARE_CREEPERS, "Scare Creepers")
        addGene(ModGenes.SCARE_SKELETONS, "Scare Skeletons")
        addGene(ModGenes.SHOOT_FIREBALLS, "Shoot Fireballs")
        addGene(ModGenes.SLIMY_DEATH, "Slimy Death")
        addGene(ModGenes.SPEED, "Speed")
        addGene(ModGenes.STRENGTH, "Strength")
        addGene(ModGenes.TELEPORT, "Teleport")
        addGene(ModGenes.WATER_BREATHING, "Water Breathing")
        addGene(ModGenes.WOOLY, "Wooly")
        addGene(ModGenes.WITHER_HIT, "Wither Hit")
        addGene(ModGenes.WITHER_PROOF, "Wither Proof")
        addGene(ModGenes.XP_MAGNET, "XP Magnet")
        addGene(ModGenes.STEP_ASSIST, "Step Assist")
        addGene(ModGenes.INFINITY, "Infinity")
        addGene(ModGenes.BIOLUMINESCENCE, "Bioluminescence")
        addGene(ModGenes.LAY_EGG, "Lay Eggs")
        addGene(ModGenes.MEATY, "Meaty")
        addGene(ModGenes.NO_HUNGER, "No Hunger")
        addGene(ModGenes.CLAWS, "Claws")
        addGene(ModGenes.HASTE, "Haste")
        addGene(ModGenes.EFFICIENCY, "Efficiency")
        addGene(ModGenes.WALL_CLIMBING, "Climb Walls")
        addGene(ModGenes.MOB_SIGHT, "Mob Sight")
        addGene(ModGenes.REACHING, "Reaching")
        addGene(ModGenes.REGENERATION, "Regeneration")
        addGene(ModGenes.BAD_OMEN, "Bad Omen")
        addGene(ModGenes.CRINGE, "Cringe")
        addGene(ModGenes.POISON, "Poison")
        addGene(ModGenes.POISON_FOUR, "Poison IV")
        addGene(ModGenes.WITHER, "Wither")
        addGene(ModGenes.WEAKNESS, "Weakness")
        addGene(ModGenes.BLINDNESS, "Blindness")
        addGene(ModGenes.SLOWNESS, "Slowness")
        addGene(ModGenes.SLOWNESS_FOUR, "Slowness IV")
        addGene(ModGenes.SLOWNESS_SIX, "Slowness VI")
        addGene(ModGenes.NAUSEA, "Nausea")
        addGene(ModGenes.HUNGER, "Hunger")
        addGene(ModGenes.FLAMBE, "Flambé")
        addGene(ModGenes.CURSED, "Cursed")
        addGene(ModGenes.LEVITATION, "Levitation")
        addGene(ModGenes.MINING_FATIGUE, "Mining Weakness")
        addGene(ModGenes.GREEN_DEATH, "Green Death")
        addGene(ModGenes.UN_UNDEATH, "Un-Undeath")
        addGene(ModGenes.GRAY_DEATH, "Gray Death")
        addGene(ModGenes.WHITE_DEATH, "White Death")
        addGene(ModGenes.BLACK_DEATH, "Black Death")
        addGene(ModGenes.WIND_CHARGED, "Wind Charged")
        addGene(ModGenes.WEAVING, "Weaving")
        addGene(ModGenes.OOZING, "Oozing")
        addGene(ModGenes.INFESTED, "Infested")

        add(
            Advancements.SCRAPER_DESC,
            "Craft a Scraper.\n\nScrapers are used to get Organic Matter from mobs."
        )
        add(Advancements.SYRINGE_TITLE, "You'll Feel a Small Prick")
        add(
            Advancements.SYRINGE_DESC,
            "Craft a Syringe.\n\nSyringes are used to extract blood from yourself.\n\nA Metal Syringe can be used to extract blood from others, instead."
        )
        add(Advancements.ANALYZER_TITLE, "Cells at Work")
        add(Advancements.ANALYZER_DESC, "Craft a Cell Analyzer.\n\nIt converts Organic Matter into Cells.")
        add(Advancements.EXTRACTOR_TITLE, "DNA Extractor")
        add(Advancements.EXTRACTOR_DESC, "Craft a DNA Extractor.\n\nIt converts Cells into encrypted DNA Helices.")
        add(Advancements.DECRYPTOR_TITLE, "DNA Decryptor")
        add(Advancements.DECRYPTOR_DESC, "Craft a DNA Decryptor.\n\nIt discovers what Gene is inside a DNA Helix.")
        add(Advancements.INFUSER_TITLE, "Plasmid Infuser")
        add(Advancements.INFUSER_DESC, "Craft a Plasmid Infuser.\n\nUse it to set a Plasmid's Gene using DNA Helices.")
        add(Advancements.DECRYPT_TITLE, "In-Gene-ius!")
        add(Advancements.DECRYPT_DESC, "Decrypt a DNA Helix to find out its Gene.")
        add(Advancements.INJECTOR_TITLE, "Plasmid Injector")
        add(
            Advancements.INJECTOR_DESC,
            "Craft a Plasmid Injector.\n\nUse it to inject full Plasmids into into decontaminated Syringes."
        )
        add(Advancements.PURIFIER_TITLE, "B Positive!")
        add(Advancements.PURIFIER_DESC, "Craft a Blood Purifier.\n\nUse it to decontaminate Syringes.")
        add(Advancements.GET_GENE_TITLE, "What Are Midichlorians?")
        add(Advancements.GET_GENE_DESC, "Inject yourself with any Gene.")
        add(Advancements.GET_MILKED_TITLE, "Level 5 Non-Milk Substance")
        add(Advancements.GET_MILKED_DESC, "Get milked with the Milky Gene.\n\nYou can milk yourself by sneaking!")
        add(Advancements.CRINGE_TITLE, "The PathOwOgen ")
        add(Advancements.CRINGE_DESC, "Get the Cringe Gene. Embrace your inner self.")
        add(Advancements.SLIMY_TITLE, "Me and the Boys")
        add(Advancements.SLIMY_DESC, "Trigger the Slimy Death Gene.")
        add(Advancements.FLIGHT_TITLE, "To Infinity and Beyond!")
        add(Advancements.FLIGHT_DESC, "Get the Flight Gene.")
        add(Advancements.SCARE_TITLE, "Boo!                 ")
        add(Advancements.SCARE_DESC, "Get the Scare Zombies, Scare Spiders, Scare Creepers, and Scare Skeletons Genes.")
        add(Advancements.BLACK_DEATH_TITLE, "La Grand Mort")
        add(Advancements.BLACK_DEATH_DESC, "Craft a DNA Helix that has The Black Death.")


        add(Keys.CATEGORY, "Genetics: Resequenced")
        add(Keys.DRAGONS_BREATH, "Dragon's Breath")
        add(Keys.TELEPORT, "Teleport")

        add(Other.DELICATE_TOUCH, "Delicate Touch")
        add(Other.SUPPORT_SLIME, "Support Slime")
        add(Other.DEATH_SCRAPER, "%s was scraped to the bone")
        add(Other.DEATH_SYRINGE, "%s died of blood loss")
        add(Other.DEATH_SYRINGE_PICKUP, "%s stepped on a loose Syringe")
        add(Other.BLEED_DEATH, "%s bled out!")
        add(Other.VIRUS_DEATH, "%s succumbed to a Virus!")
        add(
            Other.BOOK_LANDING_TEXT,
            "Welcome to Genetics: Resequenced! This guide book will help you get started with the mod."
        )

        add(Config.Client.WOOLY_REMOVES_CAPE, "Wooly removes cape")
        add(Config.Client.DISABLE_PARROT_NARRATOR, "Disable chatterbox narration")
        add(Config.Client.DISABLE_CRINGE_LANG_CHANGE, "Disable cringe language")
        add(Config.Client.SUPPORT_SLIME_RENDER_DEBUG, "Debug Support Slime rendering")
        add(Config.Client.ITEM_MAGNET_BLACKLIST_TOOLTIP, "Item Magnet blacklist tooltip")

        add(Config.Server.General.KEEP_GENES_ON_DEATH, "Keep Genes on death")
        add(Config.Server.General.MINIMUM_COOLDOWN_FOR_NOTIFICATION, "Minimum cooldown for notification")
        add(Config.Server.General.ANTIFIELD_BLOCK_RADIUS, "Anti-Field Block radius")

        add(Config.Server.Machine.MACHINES, "Machines")
        add(Config.Server.Machine.COAL_GENERATOR_ENERGY_PER_TICK, "Coal Generator Energy per tick")
        add(Config.Server.Machine.COAL_GENERATOR_ENERGY_TRANSFER_RATE, "Coal Generator Energy transfer rate")
        add(Config.Server.Machine.COAL_GENERATOR_ENERGY_CAPACITY, "Coal Generator Energy capacity")
        add(Config.Server.Machine.INCUBATOR_TICKS_PER_BREW, "Incubator ticks per brew")
        add(Config.Server.Machine.INCUBATOR_LOW_TEMP_TICK_FACTOR, "Incubator low temp tick factor")

        add(Config.Server.Gene.GENES, "Genes")

        add(Config.Server.Gene.DISABLED_GENES, "Disabled Genes")
        add(Config.Server.Gene.DISABLE_GIVING_PLAYERS_NEGATIVE_GENES, "Disable giving players negative Genes")

        add(Config.Server.Gene.BIOLUMINESCENCE_DURATION, "Bioluminescence duration")
        add(Config.Server.Gene.BIOLUMINESCENCE_COOLDOWN, "Bioluminescence cooldown")
        add(Config.Server.Gene.CHILL_DURATION, "Chill duration")
        add(Config.Server.Gene.CHILL_CHANCE, "Chill chance")
        add(Config.Server.Gene.MILKY_COOLDOWN, "Milky cooldown")
        add(Config.Server.Gene.THORNS_DAMAGE, "Thorns damage")
        add(Config.Server.Gene.THORNS_CHANCE, "Thorns chance")
        add(Config.Server.Gene.THORNS_HUNGER_DRAIN, "Thorns hunger drain")
        add(Config.Server.Gene.ITEM_MAGNET_COOLDOWN, "Item Magnet cooldown")
        add(Config.Server.Gene.XP_MAGNET_COOLDOWN, "XP Magnet cooldown")
        add(Config.Server.Gene.XP_MAGNET_RADIUS, "XP Magnet radius")
        add(Config.Server.Gene.ITEM_MAGNET_RADIUS, "Item Magnet radius")
        add(Config.Server.Gene.NO_HUNGER_COOLDOWN, "No Hunger cooldown")
        add(Config.Server.Gene.PHOTOSYNTHESIS_COOLDOWN, "Photosynthesis cooldown")
        add(Config.Server.Gene.PHOTOSYNTHESIS_SATURATION_AMOUNT, "Photosynthesis saturation amount")
        add(Config.Server.Gene.PHOTOSYNTHESIS_HUNGER_AMOUNT, "Photosynthesis hunger amount")
        add(Config.Server.Gene.NO_HUNGER_MINIMUM, "No Hunger minimum hunger")
        add(Config.Server.Gene.WALL_CLIMB_SPEED, "Wall Climb speed")
        add(Config.Server.Gene.PASSIVES_CHECK_COOLDOWN, "Passives check cooldown")
        add(Config.Server.Gene.MOB_SIGHT_COOLDOWN, "Mob Sight cooldown")
        add(Config.Server.Gene.MOB_SIGHT_RADIUS, "Mob Sight radius")
        add(Config.Server.Gene.JOHNNY_ATTACK_MULTIPLIER, "Johnny attack multiplier")
        add(Config.Server.Gene.SLIMY_DEATH_HEALTH_MULTIPLIER, "Slimy Death health multiplier")
        add(Config.Server.Gene.TELEPORT_DISTANCE, "Teleport distance")
        add(Config.Server.Gene.EGG_COOLDOWN, "Lay Egg cooldown")
        add(Config.Server.Gene.EMERALD_HEART_COOLDOWN, "Emerald Heart cooldown")
        add(Config.Server.Gene.SLIMY_DEATH_COOLDOWN, "Slimy Death cooldown")
        add(Config.Server.Gene.SLIMY_DEATH_DESPAWN_TIME, "Slimy Death despawn time")
        add(Config.Server.Gene.SLIMY_DEATH_DESPAWN_CHECK_TIMER, "Slimy Death despawn check timer")
        add(Config.Server.Gene.CLAWS_DAMAGE, "Claws damage")
        add(Config.Server.Gene.CLAWS_BASE_CHANCE, "Claws base chance")
        add(Config.Server.Gene.TELEPORT_COOLDOWN, "Teleport cooldown")
        add(Config.Server.Gene.MEATY_COOLDOWN, "Meaty cooldown")
        add(Config.Server.Gene.MEATY2_COOLDOWN, "Meaty II cooldown")
        add(Config.Server.Gene.WOOLY_COOLDOWN, "Wooly cooldown")
        add(Config.Server.Gene.EMERALD_HEART_CHAT_CHANCE, "Emerald Heart chat chance")
        add(Config.Server.Gene.DRAGONS_BREATH_COOLDOWN, "Dragon's Breath cooldown")

    }
}