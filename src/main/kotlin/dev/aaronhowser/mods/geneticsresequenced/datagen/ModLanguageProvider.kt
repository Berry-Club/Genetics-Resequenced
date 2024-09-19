package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
    output: PackOutput
) : LanguageProvider(output, GeneticsResequenced.ID, "en_us") {

    companion object {
        fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args)
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
        const val CLAWS_TWO = "info.geneticsresequenced.gene_description.geneticsresequenced:claws_2"
        const val EFFICIENCY_FOUR = "info.geneticsresequenced.gene_description.geneticsresequenced:efficiency_4"
        const val FLIGHT = "info.geneticsresequenced.gene_description.geneticsresequenced:flight"
        const val HASTE_2 = "info.geneticsresequenced.gene_description.geneticsresequenced:haste_2"
        const val MEATY_2 = "info.geneticsresequenced.gene_description.geneticsresequenced:meaty_2"
        const val PHOTOSYNTHESIS = "info.geneticsresequenced.gene_description.geneticsresequenced:photosynthesis"
        const val REGENERATION_FOUR = "info.geneticsresequenced.gene_description.geneticsresequenced:regeneration_4"
        const val RESISTANCE_TWO = "info.geneticsresequenced.gene_description.geneticsresequenced:resistance_2"
        const val SPEED_FOUR = "info.geneticsresequenced.gene_description.geneticsresequenced:speed_4"
        const val SPEED_TWO = "info.geneticsresequenced.gene_description.geneticsresequenced:speed_2"
        const val STRENGTH_TWO = "info.geneticsresequenced.gene_description.geneticsresequenced:strength_2"
        const val SCARE_ZOMBIES = "info.geneticsresequenced.gene_description.geneticsresequenced:scare_zombies"
        const val SCARE_SPIDERS = "info.geneticsresequenced.gene_description.geneticsresequenced:scare_spiders"
        const val BIOLUMINESCENCE = "info.geneticsresequenced.gene_description.geneticsresequenced:bioluminescence"
        const val CLAWS = "info.geneticsresequenced.gene_description.geneticsresequenced:claws"
        const val DRAGONS_BREATH = "info.geneticsresequenced.gene_description.geneticsresequenced:dragons_breath"
        const val EAT_GRASS = "info.geneticsresequenced.gene_description.geneticsresequenced:eat_grass"
        const val EFFICIENCY = "info.geneticsresequenced.gene_description.geneticsresequenced:efficiency"
        const val EMERALD_HEART = "info.geneticsresequenced.gene_description.geneticsresequenced:emerald_heart"
        const val ENDER_DRAGON_HEALTH =
            "info.geneticsresequenced.gene_description.geneticsresequenced:ender_dragon_health"
        const val EXPLOSIVE_EXIT = "info.geneticsresequenced.gene_description.geneticsresequenced:explosive_exit"
        const val FIRE_PROOF = "info.geneticsresequenced.gene_description.geneticsresequenced:fire_proof"
        const val HASTE = "info.geneticsresequenced.gene_description.geneticsresequenced:haste"
        const val INFINITY = "info.geneticsresequenced.gene_description.geneticsresequenced:infinity"
        const val INVISIBLE = "info.geneticsresequenced.gene_description.geneticsresequenced:invisible"
        const val ITEM_MAGNET = "info.geneticsresequenced.gene_description.geneticsresequenced:item_magnet"
        const val JUMP_BOOST = "info.geneticsresequenced.gene_description.geneticsresequenced:jump_boost"
        const val KEEP_INVENTORY = "info.geneticsresequenced.gene_description.geneticsresequenced:keep_inventory"
        const val LAY_EGG = "info.geneticsresequenced.gene_description.geneticsresequenced:lay_egg"
        const val LUCK = "info.geneticsresequenced.gene_description.geneticsresequenced:luck"
        const val MEATY = "info.geneticsresequenced.gene_description.geneticsresequenced:meaty"
        const val MILKY = "info.geneticsresequenced.gene_description.geneticsresequenced:milky"
        const val MOB_SIGHT = "info.geneticsresequenced.gene_description.geneticsresequenced:mob_sight"
        const val MORE_HEARTS = "info.geneticsresequenced.gene_description.geneticsresequenced:more_hearts"
        const val MORE_HEARTS_TWO = "info.geneticsresequenced.gene_description.geneticsresequenced:more_hearts_2"
        const val NIGHT_VISION = "info.geneticsresequenced.gene_description.geneticsresequenced:night_vision"
        const val NO_FALL_DAMAGE = "info.geneticsresequenced.gene_description.geneticsresequenced:no_fall_damage"
        const val NO_HUNGER = "info.geneticsresequenced.gene_description.geneticsresequenced:no_hunger"
        const val POISON_IMMUNITY = "info.geneticsresequenced.gene_description.geneticsresequenced:poison_immunity"
        const val REGENERATION = "info.geneticsresequenced.gene_description.geneticsresequenced:regeneration"
        const val RESISTANCE = "info.geneticsresequenced.gene_description.geneticsresequenced:resistance"
        const val SCARE_CREEPERS = "info.geneticsresequenced.gene_description.geneticsresequenced:scare_creepers"
        const val SCARE_SKELETONS = "info.geneticsresequenced.gene_description.geneticsresequenced:scare_skeletons"
        const val SHOOT_FIREBALLS = "info.geneticsresequenced.gene_description.geneticsresequenced:shoot_fireballs"
        const val SLIMY_DEATH = "info.geneticsresequenced.gene_description.geneticsresequenced:slimy_death"
        const val SPEED = "info.geneticsresequenced.gene_description.geneticsresequenced:speed"
        const val STEP_ASSIST = "info.geneticsresequenced.gene_description.geneticsresequenced:step_assist"
        const val STRENGTH = "info.geneticsresequenced.gene_description.geneticsresequenced:strength"
        const val TELEPORT = "info.geneticsresequenced.gene_description.geneticsresequenced:teleport"
        const val THORNS = "info.geneticsresequenced.gene_description.geneticsresequenced:thorns"
        const val WALL_CLIMBING = "info.geneticsresequenced.gene_description.geneticsresequenced:wall_climbing"
        const val WATER_BREATTHING = "info.geneticsresequenced.gene_description.geneticsresequenced:water_breathing"
        const val WITHER_HIT = "info.geneticsresequenced.gene_description.geneticsresequenced:wither_hit"
        const val WITHER_PROOF = "info.geneticsresequenced.gene_description.geneticsresequenced:wither_proof"
        const val WOOLY = "info.geneticsresequenced.gene_description.geneticsresequenced:wooly"
        const val XP_MAGNET = "info.geneticsresequenced.gene_description.geneticsresequenced:xp_magnet"
        const val BLINDNESS = "info.geneticsresequenced.gene_description.geneticsresequenced:blindness"
        const val CURSED = "info.geneticsresequenced.gene_description.geneticsresequenced:cursed"
        const val FLAMBE = "info.geneticsresequenced.gene_description.geneticsresequenced:flambe"
        const val HUNGER = "info.geneticsresequenced.gene_description.geneticsresequenced:hunger"
        const val LEVITATION = "info.geneticsresequenced.gene_description.geneticsresequenced:levitation"
        const val MINING_FATIGUE = "info.geneticsresequenced.gene_description.geneticsresequenced:mining_fatigue"
        const val NAUSEA = "info.geneticsresequenced.gene_description.geneticsresequenced:nausea"
        const val POISON = "info.geneticsresequenced.gene_description.geneticsresequenced:poison"
        const val POISON_FOUR = "info.geneticsresequenced.gene_description.geneticsresequenced:poison_4"
        const val SLOWNESS = "info.geneticsresequenced.gene_description.geneticsresequenced:slowness"
        const val SLOWNESS_SIX = "info.geneticsresequenced.gene_description.geneticsresequenced:slowness_6"
        const val SLOWNESS_FOUR = "info.geneticsresequenced.gene_description.geneticsresequenced:slowness_4"
        const val WEAKNESS = "info.geneticsresequenced.gene_description.geneticsresequenced:weakness"
        const val WITHER = "info.geneticsresequenced.gene_description.geneticsresequenced:wither"
        const val BLACK_DEATH = "info.geneticsresequenced.gene_description.geneticsresequenced:black_death"
        const val GREEN_DEATH = "info.geneticsresequenced.gene_description.geneticsresequenced:green_death"
        const val WHITE_DEATH = "info.geneticsresequenced.gene_description.geneticsresequenced:white_death"
        const val GRAY_DEATH = "info.geneticsresequenced.gene_description.geneticsresequenced:gray_death"
        const val UN_UNDEATH = "info.geneticsresequenced.gene_description.geneticsresequenced:un_undeath"
        const val CHATTERBOX = "info.geneticsresequenced.gene_description.geneticsresequenced:chatterbox"
        const val CRINGE = "info.geneticsresequenced.gene_description.geneticsresequenced:cringe"
        const val KNOCKBACK = "info.geneticsresequenced.gene_description.geneticsresequenced:knockback"
        const val BAD_OMEN = "info.geneticsresequenced.gene_description.geneticsresequenced:bad_omen"
        const val JOHNNY = "info.geneticsresequenced.gene_description.geneticsresequenced:johnny"
        const val CHILLING = "info.geneticsresequenced.gene_description.geneticsresequenced:chilling"
        const val REACHING = "info.geneticsresequenced.gene_description.geneticsresequenced:reaching"
    }

    object Genes {
        const val UNKNOWN = "gene.geneticsresequenced.unknown"
        const val BASIC = "gene.geneticsresequenced.basic"
        const val HASTE_TWO = "gene.geneticsresequenced.haste_2"
        const val EFFICIENCY_FOUR = "gene.geneticsresequenced.efficiency_4"
        const val REGENERATION_FOUR = "gene.geneticsresequenced.regeneration_4"
        const val SPEED_FOUR = "gene.geneticsresequenced.speed_4"
        const val SPEED_TWO = "gene.geneticsresequenced.speed_2"
        const val RESISTANCE_TWO = "gene.geneticsresequenced.resistance_2"
        const val STRENGTH_TWO = "gene.geneticsresequenced.strength_2"
        const val MEATY_TWO = "gene.geneticsresequenced.meaty_2"
        const val MORE_HEARTS_TWO = "gene.geneticsresequenced.more_hearts_2"
        const val INVISIBLE = "gene.geneticsresequenced.invisible"
        const val FLIGHT = "gene.geneticsresequenced.flight"
        const val LUCK = "gene.geneticsresequenced.luck"
        const val SCARE_ZOMBIES = "gene.geneticsresequenced.scare_zombies"
        const val SCARE_SPIDERS = "gene.geneticsresequenced.scare_spiders"
        const val THORNS = "gene.geneticsresequenced.thorns"
        const val CLAWS_TWO = "gene.geneticsresequenced.claws_2"
        const val CHATTERBOX = "gene.geneticsresequenced.chatterbox"
        const val CHILLING = "gene.geneticsresequenced.chilling"
        const val DRAGONS_BREATH = "gene.geneticsresequenced.dragons_breath"
        const val EAT_GRASS = "gene.geneticsresequenced.eat_grass"
        const val EMERALD_HEART = "gene.geneticsresequenced.emerald_heart"
        const val ENDER_DRAGON_HEALTH = "gene.geneticsresequenced.ender_dragon_health"
        const val EXPLOSIVE_EXIT = "gene.geneticsresequenced.explosive_exit"
        const val FIRE_PROOF = "gene.geneticsresequenced.fire_proof"
        const val ITEM_MAGNET = "gene.geneticsresequenced.item_magnet"
        const val JOHNNY = "gene.geneticsresequenced.johnny"
        const val JUMP_BOOST = "gene.geneticsresequenced.jump_boost"
        const val MILKY = "gene.geneticsresequenced.milky"
        const val MORE_HEARTS = "gene.geneticsresequenced.more_hearts"
        const val NIGHT_VISION = "gene.geneticsresequenced.night_vision"
        const val NO_FALL_DAMAGE = "gene.geneticsresequenced.no_fall_damage"
        const val PHOTOSYNTHESIS = "gene.geneticsresequenced.photosynthesis"
        const val POISON_IMMUNITY = "gene.geneticsresequenced.poison_immunity"
        const val RESISTANCE = "gene.geneticsresequenced.resistance"
        const val KNOCKBACK = "gene.geneticsresequenced.knockback"
        const val KEEP_INVENTORY = "gene.geneticsresequenced.keep_inventory"
        const val SCARE_CREEPERS = "gene.geneticsresequenced.scare_creepers"
        const val SCARE_SKELETONS = "gene.geneticsresequenced.scare_skeletons"
        const val SHOOT_FIREBALLS = "gene.geneticsresequenced.shoot_fireballs"
        const val SLIMY_DEATH = "gene.geneticsresequenced.slimy_death"
        const val SPEED = "gene.geneticsresequenced.speed"
        const val STRENGTH = "gene.geneticsresequenced.strength"
        const val TELEPORT = "gene.geneticsresequenced.teleport"
        const val WATER_BREATHING = "gene.geneticsresequenced.water_breathing"
        const val WOOLY = "gene.geneticsresequenced.wooly"
        const val WITHER_HIT = "gene.geneticsresequenced.wither_hit"
        const val WITHER_PROOF = "gene.geneticsresequenced.wither_proof"
        const val XP_MAGNET = "gene.geneticsresequenced.xp_magnet"
        const val STEP_ASSIST = "gene.geneticsresequenced.step_assist"
        const val INFINITY = "gene.geneticsresequenced.infinity"
        const val BIOLUMINESCENCE = "gene.geneticsresequenced.bioluminescence"
        const val LAY_EGG = "gene.geneticsresequenced.lay_egg"
        const val MEATY = "gene.geneticsresequenced.meaty"
        const val NO_HUNGER = "gene.geneticsresequenced.no_hunger"
        const val CLAWS = "gene.geneticsresequenced.claws"
        const val HASTE = "gene.geneticsresequenced.haste"
        const val EFFICIENCY = "gene.geneticsresequenced.efficiency"
        const val WALL_CLIMBING = "gene.geneticsresequenced.wall_climbing"
        const val MOB_SIGHT = "gene.geneticsresequenced.mob_sight"
        const val REACHING = "gene.geneticsresequenced.reaching"
        const val REGENERATION = "gene.geneticsresequenced.regeneration"
        const val BAD_OMEN = "gene.geneticsresequenced.bad_omen"
        const val CRINGE = "gene.geneticsresequenced.cringe"
        const val POISON = "gene.geneticsresequenced.poison"
        const val POISON_TWO = "gene.geneticsresequenced.poison_2"
        const val POISON_FOUR = "gene.geneticsresequenced.poison_4"
        const val WITHER = "gene.geneticsresequenced.wither"
        const val WEAKNESS = "gene.geneticsresequenced.weakness"
        const val BLINDNESS = "gene.geneticsresequenced.blindness"
        const val SLOWNESS = "gene.geneticsresequenced.slowness"
        const val SLOWNESS_FOUR = "gene.geneticsresequenced.slowness_4"
        const val SLOWNESS_SIX = "gene.geneticsresequenced.slowness_6"
        const val NAUSEA = "gene.geneticsresequenced.nausea"
        const val HUNGER = "gene.geneticsresequenced.hunger"
        const val FLAMBE = "gene.geneticsresequenced.flambe"
        const val CURSED = "gene.geneticsresequenced.cursed"
        const val LEVITATION = "gene.geneticsresequenced.levitation"
        const val FATIGUE = "gene.geneticsresequenced.mining_fatigue"
        const val GREEN_DEATH = "gene.geneticsresequenced.green_death"
        const val UN_UNDEATH = "gene.geneticsresequenced.un_undeath"
        const val GRAY_DEATH = "gene.geneticsresequenced.gray_death"
        const val WHITE_DEATH = "gene.geneticsresequenced.white_death"
        const val BLACK_DEATH = "gene.geneticsresequenced.black_death"
        const val VOID_DEATH = "gene.geneticsresequenced.void_death"
        const val GENE_DISABLED = "gene.geneticsresequenced.gene_disabled"
        const val WIND_CHARGED = "gene.geneticsresequenced.wind_charged"
        const val WEAVING = "gene.geneticsresequenced.weaving"
        const val OOZING = "gene.geneticsresequenced.oozing"
        const val INFESTED = "gene.geneticsresequenced.infested"
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
        add(Tooltips.COPY_GENE, "Click to copy ID: %s")
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
        add(Info.CLAWS_TWO, "Doubles the chances of inflicting Bleeding on hit.")
        add(
            Info.EFFICIENCY_FOUR,
            "Increases your mining speed as if you were using an Efficiency IV tool.\n\nStacks with Haste, as well as the actual Efficiency enchantment!"
        )
        add(Info.FLIGHT, "Gives you Creative mode style flight.")
        add(Info.HASTE_2, "Gives you the Haste II potion effect.")
        add(Info.MEATY_2, "Causes you to occasionally drop a cooked Porkchop.")
        add(Info.PHOTOSYNTHESIS, "Slowly feeds you when in direct sunlight.")
        add(Info.REGENERATION_FOUR, "Gives you the Regeneration IV potion effect.")
        add(Info.RESISTANCE_TWO, "Give you the Resistance II potion effect.")
        add(Info.SPEED_FOUR, "Give you the Speed IV potion effect.")
        add(Info.SPEED_TWO, "Give you the Speed II potion effect.")
        add(Info.STRENGTH_TWO, "Give you the Strength II potion effect.")
        add(Info.SCARE_ZOMBIES, "Makes Zombies run away from you.")
        add(Info.SCARE_SPIDERS, "Makes Spiders and Cave Spiders run away from you.")
        add(Info.BIOLUMINESCENCE, "Makes you leave behind a trail of light, which lasts a short while.")
        add(Info.CLAWS, "Has a chance of inflicting Bleeding on hit.\n\nBleeding deals damage over time.")
        add(Info.DRAGONS_BREATH, "Allows you to use the \"Dragon's Breath\" keybind to fire a dragon fireball.")
        add(Info.EAT_GRASS, "Allows you to right-click Grass Blocks to regain hunger.")
        add(
            Info.EFFICIENCY,
            "Increases your mining speed as if you were using an Efficiency tool.\n\nStacks with Haste, as well as the actual Efficiency enchantment!"
        )
        add(Info.EMERALD_HEART, "Spawns an Emerald when you die.")
        add(
            Info.ENDER_DRAGON_HEALTH,
            "Blocks damage if you're holding a Dragon Health Crystal.\n\nDamage is instead dealt to the Crystal's durability."
        )
        add(
            Info.EXPLOSIVE_EXIT,
            "Makes you explode when you die, if you're holding at least 5 Gunpowder.\n\nEntities with the Gene do not need the Gunpowder."
        )
        add(Info.FIRE_PROOF, "Makes you immune to fire damage, and immediately extinguishes you.")
        add(Info.HASTE, "Gives you the Haste potion effect.")
        add(Info.INFINITY, "Allows you to use Bows without any Arrows in your inventory.")
        add(Info.INVISIBLE, "Gives you the Invisibility potion effect.")
        add(
            Info.ITEM_MAGNET,
            "Makes you pick up all nearby items.\n\nDisables when sneaking, when holding an active Anti-Field Orb, or near an active Anti-Field Block."
        )
        add(Info.JUMP_BOOST, "Gives you the Jump Boost potion effect.")
        add(Info.KEEP_INVENTORY, "Makes you keep your inventory when you die.")
        add(Info.LAY_EGG, "Makes you lay an Egg occasionally.")
        add(Info.LUCK, "Gives you the Luck potion effect.")
        add(Info.MEATY, "Allows you to be sheared for a raw Porkchop.")
        add(Info.MILKY, "Allows you to be milked with a Bucket.")
        add(Info.MOB_SIGHT, "Gives all nearby entities the Glowing potion effect.")
        add(Info.MORE_HEARTS, "Gives you 10 additional hearts.")
        add(Info.MORE_HEARTS_TWO, "Gives you 10 MORE additional hearts.")
        add(Info.NIGHT_VISION, "Gives you the Night Vision potion effect.")
        add(Info.NO_FALL_DAMAGE, "Makes you immune to fall damage.")
        add(Info.NO_HUNGER, "Prevents your hunger from going below halfway.")
        add(Info.POISON_IMMUNITY, "Automatically removes Poison.")
        add(Info.REGENERATION, "Gives you the Regeneration potion effect.")
        add(Info.RESISTANCE, "Gives you the Resistance potion effect.")
        add(Info.SCARE_CREEPERS, "Makes Creepers run away from you.")
        add(Info.SCARE_SKELETONS, "Makes Skeletons run away from you.")
        add(Info.SHOOT_FIREBALLS, "Lets you shoot Fire Charges when you right-click Blaze Rods.")
        add(
            Info.SLIMY_DEATH,
            "When you die, cancels it and spawns some Support Slimes to help you fight.\n\nHas a long cooldown."
        )
        add(Info.SPEED, "Gives you the Speed potion effect.")
        add(Info.STEP_ASSIST, "Lets you step up full blocks.")
        add(Info.STRENGTH, "Gives you the Strength potion effect.")
        add(
            Info.TELEPORT,
            "Allows you to use the \"Teleport\" keybind to teleport a short distance where you're looking."
        )
        add(
            Info.THORNS,
            "If you take damage while wearing either no Chestplate or a Leather Chestplate, there's a chance of reflecting some of the damage back."
        )
        add(Info.WALL_CLIMBING, "Lets you climb walls like a Spider.")
        add(Info.WATER_BREATTHING, "Keeps your air meter full.")
        add(Info.WITHER_HIT, "Melee attacks inflict the Wither potion effect.")
        add(Info.WITHER_PROOF, "Makes you immune to the Wither potion effect.")
        add(Info.WOOLY, "Lets you be sheared for Wool.")
        add(
            Info.XP_MAGNET,
            "Makes you pick up all nearby xp.\n\nDisables when sneaking, when holding an active Anti-Field Orb, or near an active Anti-Field Block."
        )
        add(Info.BLINDNESS, "Inflicts the Blindness potion effect.")
        add(Info.CURSED, "Inflicts the Cursed potion effect.")
        add(Info.FLAMBE, "Constantly lights you on fire")
        add(Info.HUNGER, "Inflicts the Hunger potion effect.")
        add(Info.LEVITATION, "Inflicts the Levitation potion effect.")
        add(Info.MINING_FATIGUE, "Inflicts the Mining Fatigue potion effect.")
        add(Info.NAUSEA, "Inflicts the Nausea potion effect.")
        add(Info.POISON, "Inflicts the Poison potion effect.")
        add(Info.POISON_FOUR, "Inflicts the Poison IV potion effect.")
        add(Info.SLOWNESS, "Inflicts the Slowness potion effect.")
        add(Info.SLOWNESS_SIX, "Inflicts the Slowness IV potion effect.")
        add(Info.SLOWNESS_FOUR, "Inflicts the Slowness VI potion effect.")
        add(Info.WEAKNESS, "Inflicts the Weakness potion effect.")
        add(Info.WITHER, "Inflicts the Wither potion effect.")
        add(Info.BLACK_DEATH, "Instantly kills.")
        add(Info.GREEN_DEATH, "Slowly kills Creepers.")
        add(Info.WHITE_DEATH, "Slowly kills monsters.")
        add(Info.GRAY_DEATH, "Slowly kills ageable mobs.")
        add(Info.UN_UNDEATH, "Slowly kills the undead.")
        add(
            Info.CHATTERBOX,
            "Your chat messages are automatically read by the narrator (within 64 blocks).\n\nCan be disabled in the client config."
        )
        add(
            Info.CRINGE,
            "Makes you a Discord moderator.\n\n(UwU-fies your outgoing chat messages, and sets your language to LOLCAT)"
        )
        add(Info.KNOCKBACK, "Increases the knockback of your attacks.")
        add(Info.BAD_OMEN, "Inflicts the Bad Omen potion effect.")
        add(Info.JOHNNY, "Deal more damage using Axes")
        add(Info.CHILLING, "Has a chance of inflicting freezing damage on hit.")
        add(Info.REACHING, "Increases your reach by 1.5 times.")

        add(Genes.UNKNOWN, "Unknown")
        add(Genes.BASIC, "Basic")
        add(Genes.HASTE_TWO, "Haste II")
        add(Genes.EFFICIENCY_FOUR, "Efficiency IV")
        add(Genes.REGENERATION_FOUR, "Regeneration IV")
        add(Genes.SPEED_FOUR, "Speed IV")
        add(Genes.SPEED_TWO, "Speed II")
        add(Genes.RESISTANCE_TWO, "Resistance II")
        add(Genes.STRENGTH_TWO, "Strength II")
        add(Genes.MEATY_TWO, "Meaty II")
        add(Genes.MORE_HEARTS_TWO, "More Hearts II")
        add(Genes.INVISIBLE, "Invisibility")
        add(Genes.FLIGHT, "Flight")
        add(Genes.LUCK, "Luck")
        add(Genes.SCARE_ZOMBIES, "Scare Zombies")
        add(Genes.SCARE_SPIDERS, "Scare Spiders")
        add(Genes.THORNS, "Thorns")
        add(Genes.CLAWS_TWO, "Claws II")
        add(Genes.CHATTERBOX, "Chatterbox")
        add(Genes.CHILLING, "Chilling")
        add(Genes.DRAGONS_BREATH, "Dragon's Breath")
        add(Genes.EAT_GRASS, "Eat Grass")
        add(Genes.EMERALD_HEART, "Emerald Heart")
        add(Genes.ENDER_DRAGON_HEALTH, "Ender Dragon Health")
        add(Genes.EXPLOSIVE_EXIT, "Explosive Exit")
        add(Genes.FIRE_PROOF, "Fire Proof")
        add(Genes.ITEM_MAGNET, "Item Magnet")
        add(Genes.JOHNNY, "Johnny")
        add(Genes.JUMP_BOOST, "Jump Boost")
        add(Genes.MILKY, "Milky")
        add(Genes.MORE_HEARTS, "More Hearts")
        add(Genes.NIGHT_VISION, "Night Vision")
        add(Genes.NO_FALL_DAMAGE, "No Fall Damage")
        add(Genes.PHOTOSYNTHESIS, "Photosynthesis")
        add(Genes.POISON_IMMUNITY, "Poison Immunity")
        add(Genes.RESISTANCE, "Resistance")
        add(Genes.KNOCKBACK, "Knockback")
        add(Genes.KEEP_INVENTORY, "Keep Inventory")
        add(Genes.SCARE_CREEPERS, "Scare Creepers")
        add(Genes.SCARE_SKELETONS, "Scare Skeletons")
        add(Genes.SHOOT_FIREBALLS, "Shoot Fireballs")
        add(Genes.SLIMY_DEATH, "Slimy Death")
        add(Genes.SPEED, "Speed")
        add(Genes.STRENGTH, "Strength")
        add(Genes.TELEPORT, "Teleport")
        add(Genes.WATER_BREATHING, "Water Breathing")
        add(Genes.WOOLY, "Wooly")
        add(Genes.WITHER_HIT, "Wither Hit")
        add(Genes.WITHER_PROOF, "Wither Proof")
        add(Genes.XP_MAGNET, "XP Magnet")
        add(Genes.STEP_ASSIST, "Step Assist")
        add(Genes.INFINITY, "Infinity")
        add(Genes.BIOLUMINESCENCE, "Bioluminescence")
        add(Genes.LAY_EGG, "Lay Eggs")
        add(Genes.MEATY, "Meaty")
        add(Genes.NO_HUNGER, "No Hunger")
        add(Genes.CLAWS, "Claws")
        add(Genes.HASTE, "Haste")
        add(Genes.EFFICIENCY, "Efficiency")
        add(Genes.WALL_CLIMBING, "Climb Walls")
        add(Genes.MOB_SIGHT, "Mob Sight")
        add(Genes.REACHING, "Reaching")
        add(Genes.REGENERATION, "Regeneration")
        add(Genes.BAD_OMEN, "Bad Omen")
        add(Genes.CRINGE, "Cringe")
        add(Genes.POISON, "Poison")
        add(Genes.POISON_TWO, "Poison II")
        add(Genes.POISON_FOUR, "Poison IV")
        add(Genes.WITHER, "Wither")
        add(Genes.WEAKNESS, "Weakness")
        add(Genes.BLINDNESS, "Blindness")
        add(Genes.SLOWNESS, "Slowness")
        add(Genes.SLOWNESS_FOUR, "Slowness IV")
        add(Genes.SLOWNESS_SIX, "Slowness VI")
        add(Genes.NAUSEA, "Nausea")
        add(Genes.HUNGER, "Hunger")
        add(Genes.FLAMBE, "Flambé")
        add(Genes.CURSED, "Cursed")
        add(Genes.LEVITATION, "Levitation")
        add(Genes.FATIGUE, "Mining Weakness")
        add(Genes.GREEN_DEATH, "Green Death")
        add(Genes.UN_UNDEATH, "Un-Undeath")
        add(Genes.GRAY_DEATH, "Gray Death")
        add(Genes.WHITE_DEATH, "White Death")
        add(Genes.BLACK_DEATH, "Black Death")
        add(Genes.VOID_DEATH, "Void Death")
        add(Genes.GENE_DISABLED, " (Disabled)")
        add(Genes.WIND_CHARGED, "Wind Charged")
        add(Genes.WEAVING, "Weaving")
        add(Genes.OOZING, "Oozing")
        add(Genes.INFESTED, "Infested")

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