package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
    output: PackOutput
) : LanguageProvider(output, GeneticsResequenced.ID, "en_us") {

    companion object {

        object Items {

            const val CREATIVE_TAB = "itemGroup.geneticsresequenced"

            const val GUIDE_BOOK = "item.geneticsresequenced.guide_book"
            const val DRAGON_HEALTH_CRYSTAL = "item.geneticsresequenced.dragon_health_crystal"
            const val ORGANIC_MATTER = "item.geneticsresequenced.organic_matter"
            const val CELL = "item.geneticsresequenced.cell"
            const val GMO_CELL = "item.geneticsresequenced.gmo_cell"
            const val ANTI_FIELD_ORB = "item.geneticsresequenced.anti_field_orb"
            const val SCRAPER = "item.geneticsresequenced.scraper"
            const val SYRINGE_EMPTY = "item.geneticsresequenced.syringe.empty"
            const val SYRINGE_FULL = "item.geneticsresequenced.syringe.full"
            const val METAL_SYRINGE_EMPTY = "item.geneticsresequenced.metal_syringe.empty"
            const val METAL_SYRINGE_FULL = "item.geneticsresequenced.metal_syringe.full"
            const val DNA_HELIX = "item.geneticsresequenced.dna_helix"
            const val PLASMID = "item.geneticsresequenced.plasmid"
            const val OVERCLOCKER = "item.geneticsresequenced.overclocker"
            const val SUPPORT_SLIME_EGG = "item.geneticsresequenced.support_slime_spawn_egg"
            const val ANTI_PLASMID = "item.geneticsresequenced.anti_plasmid"

            const val POTION_SUBSTRATE = "item.minecraft.potion.effect.geneticsresequenced.substrate"
            const val POTION_CELL_GROWTH = "item.minecraft.potion.effect.geneticsresequenced.cell_growth"
            const val POTION_MUTATION = "item.minecraft.potion.effect.geneticsresequenced.mutation"
            const val POTION_VIRAL_AGENTS = "item.minecraft.potion.effect.geneticsresequenced.viral_agents"
            const val POTION_THE_CURE = "item.minecraft.potion.effect.geneticsresequenced.the_cure"
            const val POTION_ZOMBIFY_VILLAGER = "item.minecraft.potion.effect.geneticsresequenced.zombify_villager"
            const val SPLASH_POTION_SUBSTRATE = "item.minecraft.splash_potion.effect.geneticsresequenced.substrate"
            const val SPLASH_POTION_CELL_GROWTH = "item.minecraft.splash_potion.effect.geneticsresequenced.cell_growth"
            const val SPLASH_POTION_MUTATION = "item.minecraft.splash_potion.effect.geneticsresequenced.mutation"
            const val SPLASH_POTION_VIRAL_AGENTS =
                "item.minecraft.splash_potion.effect.geneticsresequenced.viral_agents"
            const val SPLASH_POTION_THE_CURE = "item.minecraft.splash_potion.effect.geneticsresequenced.the_cure"
            const val SPLASH_POTION_ZOMBIFY_VILLAGER =
                "item.minecraft.splash_potion.effect.geneticsresequenced.zombify_villager"
            const val LINGERING_POTION_SUBSTRATE =
                "item.minecraft.lingering_potion.effect.geneticsresequenced.substrate"
            const val LINGERING_POTION_CELL_GROWTH =
                "item.minecraft.lingering_potion.effect.geneticsresequenced.cell_growth"
            const val LINGERING_POTION_MUTATION = "item.minecraft.lingering_potion.effect.geneticsresequenced.mutation"
            const val LINGERING_POTION_VIRAL_AGENTS =
                "item.minecraft.lingering_potion.effect.geneticsresequenced.viral_agents"
            const val LINGERING_POTION_THE_CURE = "item.minecraft.lingering_potion.effect.geneticsresequenced.the_cure"
            const val LINGERING_POTION_ZOMBIFY_VILLAGER =
                "item.minecraft.lingering_potion.effect.geneticsresequenced.zombify_villager"
            const val TIPPED_ARROW_SUBSTRATE = "item.minecraft.tipped_arrow.effect.geneticsresequenced.substrate"
            const val TIPPED_ARROW_CELL_GROWTH = "item.minecraft.tipped_arrow.effect.geneticsresequenced.cell_growth"
            const val TIPPED_ARROW_MUTATION = "item.minecraft.tipped_arrow.effect.geneticsresequenced.mutation"
            const val TIPPED_ARROW_VIRAL_AGENTS = "item.minecraft.tipped_arrow.effect.geneticsresequenced.viral_agents"
            const val TIPPED_ARROW_THE_CURE = "item.minecraft.tipped_arrow.effect.geneticsresequenced.the_cure"
            const val TIPPED_ARROW_ZOMBIFY_VILLAGER =
                "item.minecraft.tipped_arrow.effect.geneticsresequenced.zombify_villager"
        }

        object Effects {
            const val BLEED = "effect.geneticsresequenced.bleed"
            const val SUBSTRATE = "effect.geneticsresequenced.substrate"
            const val CELL_GROWTH = "effect.geneticsresequenced.cell_growth"
            const val MUTATION = "effect.geneticsresequenced.mutation"
            const val VIRAL_AGENTS = "effect.geneticsresequenced.viral_agents"
            const val THE_CURE = "effect.geneticsresequenced.the_cure"
            const val ZOMBIFY_VILLAGER = "effect.geneticsresequenced.zombify_villager"
        }

        object Messages {
            const val SCRAPER_CANT_SCRAPE = "message.geneticsresequenced.scraper.cant_scrape"
            const val COOLDOWN_EMERALD_HEART = "message.geneticsresequenced.emerald_heart.cooldown"
            const val DEATH_GENE_REMOVAL = "message.geneticsresequenced.death_gene_removal"
            const val RECENT_WOOLY = "message.geneticsresequenced.wooly.recent"
            const val RECENT_MEATY = "message.geneticsresequenced.meaty.recent"
            const val RECENT_MILKY = "message.geneticsresequenced.milk.recent"
            const val MILK_MILKED = "message.geneticsresequenced.milk.milked"
            const val SYRINGE_INJECTED = "message.geneticsresequenced.syringe.injected"
            const val SYRINGE_FAILED = "message.geneticsresequenced.syringe.failed"
            const val SYRINGE_CONTAMINATED = "message.geneticsresequenced.syringe.contaminated"
            const val METAL_SYRINGE_CONTAMINATED = "message.geneticsresequenced.metal_syringe.contaminated"
            const val SUPPORT_SLIME_CREATIVE = "message.geneticsresequenced.support_slime_creative"
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

        object Blocks {
            const val BIOLUMINESCENCE = "block.geneticsresequenced.bioluminescence"
            const val CELL_ANALYZER = "block.geneticsresequenced.cell_analyzer"
            const val COAL_GENERATOR = "block.geneticsresequenced.coal_generator"
            const val DNA_DECRYPTOR = "block.geneticsresequenced.dna_decryptor"
            const val DNA_EXTRACTOR = "block.geneticsresequenced.dna_extractor"
            const val AIR_DISPERSAL = "block.geneticsresequenced.air_dispersal"
            const val BLOOD_PURIFIER = "block.geneticsresequenced.blood_purifier"
            const val CLONING_MACHINE = "block.geneticsresequenced.cloning_machine"
            const val INCUBATOR = "block.geneticsresequenced.incubator"
            const val ADVANCED_INCUBATOR = "block.geneticsresequenced.advanced_incubator"
            const val PLASMID_INFUSER = "block.geneticsresequenced.plasmid_infuser"
            const val PLASMID_INJECTOR = "block.geneticsresequenced.plasmid_injector"
            const val ANTI_FIELD_BLOCK = "block.geneticsresequenced.anti_field_block"
        }

        const val DELICATE_TOUCH = "enchantment.geneticsresequenced.delicate_touch"
        const val SUPPORT_SLIME = "entity.geneticsresequenced.support_slime"

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
            const val PLASMID_AMOUNT = "tooltip.geneticsresequenced.plasmid.amount"
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
            const val ADDING_GENES = "tooltip.geneticsresequenced.syringe.adding"
            const val REMOVING_GENES = "tooltip.geneticsresequenced.syringe.removing"
            const val GMO_LINE_ONE = "tooltip.geneticsresequenced.gmo_recipe.line1"
            const val GMO_LINE_TWO = "tooltip.geneticsresequenced.gmo_recipe.line2"
            const val GMO_LINE_THREE = "tooltip.geneticsresequenced.gmo_recipe.line3"
        }

        object Commands {
            const val GENE_LIST = "command.geneticsresequenced.geneList"
            const val NO_GENES = "command.geneticsresequenced.list.no_genes"
            const val THEIR_GENES = "command.geneticsresequenced.list.genes"
            const val REMOVED_LIGHTS = "command.geneticsresequenced.remove_nearby_lights.success"
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
            const val GMO = "recipe.geneticsresequenced.gmo"
            const val CELL_GROWTH = "recipe.geneticsresequenced.cell_growth"
            const val SUBSTRATE = "recipe.geneticsresequenced.substrate"
            const val VIRUS = "recipe.geneticsresequenced.virus"
            const val BLACK_DEATH = "recipe.geneticsresequenced.black_death"
        }

        object Info {
            const val = "info.geneticsresequenced.organic_matter"
            const val = "info.geneticsresequenced.mob_gene.line1"
            const val = "info.geneticsresequenced.mob_gene.line2"
            const val = "info.geneticsresequenced.requires_genes"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:claws_2"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:efficiency_4"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:flight"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:haste_2"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:meaty_2"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:photosynthesis"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:regeneration_4"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:resistance_2"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:speed_4"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:speed_2"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:strength_2"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:scare_zombies"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:scare_spiders"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:bioluminescence"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:claws"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:dragons_breath"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:eat_grass"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:efficiency"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:emerald_heart"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:ender_dragon_health"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:explosive_exit"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:fire_proof"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:haste"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:infinity"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:invisible"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:item_magnet"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:jump_boost"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:keep_inventory"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:lay_egg"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:luck"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:meaty"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:milky"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:mob_sight"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:more_hearts"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:more_hearts_2"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:night_vision"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:no_fall_damage"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:no_hunger"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:poison_immunity"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:regeneration"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:resistance"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:scare_creepers"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:scare_skeletons"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:shoot_fireballs"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:slimy_death"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:speed"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:step_assist"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:strength"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:teleport"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:thorns"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:wall_climbing"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:water_breathing"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:wither_hit"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:wither_proof"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:wooly"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:xp_magnet"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:blindness"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:cursed"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:flambe"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:hunger"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:levitation"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:mining_fatigue"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:nausea"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:poison"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:poison_4"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:slowness"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:slowness_4"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:slowness_6"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:weakness"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:wither"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:black_death"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:green_death"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:white_death"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:gray_death"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:un_undeath"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:chatterbox"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:cringe"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:knockback"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:bad_omen"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:johnny"
            const val = "info.geneticsresequenced.gene_description.geneticsresequenced:chilling"
        }

        object Genes {
            const val = "gene.geneticsresequenced.unknown"
            const val = "gene.geneticsresequenced.basic"
            const val = "gene.geneticsresequenced.haste_2"
            const val = "gene.geneticsresequenced.efficiency_4"
            const val = "gene.geneticsresequenced.regeneration_4"
            const val = "gene.geneticsresequenced.speed_4"
            const val = "gene.geneticsresequenced.speed_2"
            const val = "gene.geneticsresequenced.resistance_2"
            const val = "gene.geneticsresequenced.strength_2"
            const val = "gene.geneticsresequenced.meaty_2"
            const val = "gene.geneticsresequenced.more_hearts_2"
            const val = "gene.geneticsresequenced.invisible"
            const val = "gene.geneticsresequenced.flight"
            const val = "gene.geneticsresequenced.luck"
            const val = "gene.geneticsresequenced.scare_zombies"
            const val = "gene.geneticsresequenced.scare_spiders"
            const val = "gene.geneticsresequenced.thorns"
            const val = "gene.geneticsresequenced.claws_2"
            const val = "gene.geneticsresequenced.chatterbox"
            const val = "gene.geneticsresequenced.chilling"
            const val = "gene.geneticsresequenced.dragons_breath"
            const val = "gene.geneticsresequenced.eat_grass"
            const val = "gene.geneticsresequenced.emerald_heart"
            const val = "gene.geneticsresequenced.ender_dragon_health"
            const val = "gene.geneticsresequenced.explosive_exit"
            const val = "gene.geneticsresequenced.fire_proof"
            const val = "gene.geneticsresequenced.item_magnet"
            const val = "gene.geneticsresequenced.johnny"
            const val = "gene.geneticsresequenced.jump_boost"
            const val = "gene.geneticsresequenced.milky"
            const val = "gene.geneticsresequenced.more_hearts"
            const val = "gene.geneticsresequenced.night_vision"
            const val = "gene.geneticsresequenced.no_fall_damage"
            const val = "gene.geneticsresequenced.photosynthesis"
            const val = "gene.geneticsresequenced.poison_immunity"
            const val = "gene.geneticsresequenced.resistance"
            const val = "gene.geneticsresequenced.knockback"
            const val = "gene.geneticsresequenced.keep_inventory"
            const val = "gene.geneticsresequenced.scare_creepers"
            const val = "gene.geneticsresequenced.scare_skeletons"
            const val = "gene.geneticsresequenced.shoot_fireballs"
            const val = "gene.geneticsresequenced.slimy_death"
            const val = "gene.geneticsresequenced.speed"
            const val = "gene.geneticsresequenced.strength"
            const val = "gene.geneticsresequenced.teleport"
            const val = "gene.geneticsresequenced.water_breathing"
            const val = "gene.geneticsresequenced.wooly"
            const val = "gene.geneticsresequenced.wither_hit"
            const val = "gene.geneticsresequenced.wither_proof"
            const val = "gene.geneticsresequenced.xp_magnet"
            const val = "gene.geneticsresequenced.step_assist"
            const val = "gene.geneticsresequenced.infinity"
            const val = "gene.geneticsresequenced.bioluminescence"
            const val = "gene.geneticsresequenced.cybernetic"
            const val = "gene.geneticsresequenced.lay_egg"
            const val = "gene.geneticsresequenced.meaty"
            const val = "gene.geneticsresequenced.no_hunger"
            const val = "gene.geneticsresequenced.claws"
            const val = "gene.geneticsresequenced.haste"
            const val = "gene.geneticsresequenced.efficiency"
            const val = "gene.geneticsresequenced.wall_climbing"
            const val = "gene.geneticsresequenced.mob_sight"
            const val = "gene.geneticsresequenced.regeneration"
            const val = "gene.geneticsresequenced.bad_omen"
            const val = "gene.geneticsresequenced.cringe"
            const val = "gene.geneticsresequenced.poison"
            const val = "gene.geneticsresequenced.poison_2"
            const val = "gene.geneticsresequenced.poison_4"
            const val = "gene.geneticsresequenced.wither"
            const val = "gene.geneticsresequenced.weakness"
            const val = "gene.geneticsresequenced.blindness"
            const val = "gene.geneticsresequenced.slowness"
            const val = "gene.geneticsresequenced.slowness_4"
            const val = "gene.geneticsresequenced.slowness_6"
            const val = "gene.geneticsresequenced.nausea"
            const val = "gene.geneticsresequenced.hunger"
            const val = "gene.geneticsresequenced.flambe"
            const val = "gene.geneticsresequenced.cursed"
            const val = "gene.geneticsresequenced.levitation"
            const val = "gene.geneticsresequenced.mining_fatigue"
            const val = "gene.geneticsresequenced.green_death"
            const val = "gene.geneticsresequenced.un_undeath"
            const val = "gene.geneticsresequenced.gray_death"
            const val = "gene.geneticsresequenced.white_death"
            const val = "gene.geneticsresequenced.black_death"
            const val = "gene.geneticsresequenced.void_death"
            const val = "gene.geneticsresequenced.gene_disabled"
        }

        object Advancements {
            const val = "advancement.geneticsresequenced.scraper.desc"
            const val = "advancement.geneticsresequenced.syringe.title"
            const val = "advancement.geneticsresequenced.syringe.desc"
            const val = "advancement.geneticsresequenced.cell_analyzer.title"
            const val = "advancement.geneticsresequenced.cell_analyzer.desc"
            const val = "advancement.geneticsresequenced.dna_extractor.title"
            const val = "advancement.geneticsresequenced.dna_extractor.desc"
            const val = "advancement.geneticsresequenced.dna_decryptor.title"
            const val = "advancement.geneticsresequenced.dna_decryptor.desc"
            const val = "advancement.geneticsresequenced.plasmid_infuser.title"
            const val = "advancement.geneticsresequenced.plasmid_infuser.desc"
            const val = "advancement.geneticsresequenced.decrypt_dna.title"
            const val = "advancement.geneticsresequenced.decrypt_dna.desc"
            const val = "advancement.geneticsresequenced.plasmid_injector.title"
            const val = "advancement.geneticsresequenced.plasmid_injector.desc"
            const val = "advancement.geneticsresequenced.blood_purifier.title"
            const val = "advancement.geneticsresequenced.blood_purifier.desc"
            const val = "advancement.geneticsresequenced.get_gene.title"
            const val = "advancement.geneticsresequenced.get_gene.desc"
            const val = "advancement.geneticsresequenced.get_milked.title"
            const val = "advancement.geneticsresequenced.get_milked.desc"
            const val = "advancement.geneticsresequenced.pathowogen.title"
            const val = "advancement.geneticsresequenced.pathowogen.desc"
            const val = "advancement.geneticsresequenced.slimy_death.title"
            const val = "advancement.geneticsresequenced.slimy_death.desc"
            const val = "advancement.geneticsresequenced.flight.title"
            const val = "advancement.geneticsresequenced.flight.desc"
            const val = "advancement.geneticsresequenced.scare.title"
            const val = "advancement.geneticsresequenced.scare.desc"
            const val = "advancement.geneticsresequenced.black_death.title"
            const val = "advancement.geneticsresequenced.black_death.desc"
        }


        const val = "death.attack.scraper"
        const val = "death.attack.syringe"
        const val = "death.attack.syringeDrop"
        const val = "key.geneticsresequenced.category"
        const val = "key.geneticsresequenced.dragons_breath"
        const val = "key.geneticsresequenced.teleport"
        const val = "book.geneticsresequenced.landing_text"

    }


    override fun addTranslations() {

        add(Items.CREATIVE_TAB, "Genetics Resequenced")
        add(Items.GUIDE_BOOK, "Big Book of Genetics")
        add(Items.DRAGON_HEALTH_CRYSTAL, "Dragon Health Crystal")
        add(Items.ORGANIC_MATTER, "Organic Matter")
        add(Items.CELL, "Cell")
        add(Items.GMO_CELL, "Genetically Modified Cell")
        add(Items.ANTI_FIELD_ORB, "Anti-Field Orb")
        add(Items.SCRAPER, "Scraper")
        add(Items.SYRINGE_EMPTY, "Empty Syringe")
        add(Items.SYRINGE_FULL, "Full Syringe")
        add(Items.METAL_SYRINGE_EMPTY, "Empty Metal Syringe")
        add(Items.METAL_SYRINGE_FULL, "Full Metal Syringe")
        add(Items.DNA_HELIX, "DNA Helix")
        add(Items.PLASMID, "Plasmid")
        add(Items.OVERCLOCKER, "Overclocker")
        add(Items.SUPPORT_SLIME_EGG, "Support Slime Spawn Egg")
        add(Items.ANTI_PLASMID, "Anti-Plasmid")

        add(Effects.BLEED, "Bleed")
        add(Effects.SUBSTRATE, "Substrate")
        add(Effects.CELL_GROWTH, "Cell Growth")
        add(Effects.MUTATION, "Mutation")
        add(Effects.VIRAL_AGENTS, "Viral Agents")
        add(Effects.THE_CURE, "The Cure")
        add(Effects.ZOMBIFY_VILLAGER, "Zombify Villager")

        add(Items.POTION_SUBSTRATE, "Organic Substrate")
        add(Items.POTION_CELL_GROWTH, "Potion of Cell Growth")
        add(Items.POTION_MUTATION, "Potion of Mutation")
        add(Items.POTION_VIRAL_AGENTS, "Potion of Viral Agents")
        add(Items.POTION_THE_CURE, "Potion of the Cure")
        add(Items.POTION_ZOMBIFY_VILLAGER, "Potion of Zombify Villager")

        add(Items.SPLASH_POTION_SUBSTRATE, "Splash Potion of Organic Substrate")
        add(Items.SPLASH_POTION_CELL_GROWTH, "Splash Potion of Cell Growth")
        add(Items.SPLASH_POTION_MUTATION, "Splash Potion of Mutation")
        add(Items.SPLASH_POTION_VIRAL_AGENTS, "Splash Potion of Viral Agents")
        add(Items.SPLASH_POTION_THE_CURE, "Splash Potion of the Cure")
        add(Items.SPLASH_POTION_ZOMBIFY_VILLAGER, "Splash Potion of Zombify Villager")
        add(Items.LINGERING_POTION_SUBSTRATE, "Lingering Potion of Organic Substrate")
        add(Items.LINGERING_POTION_CELL_GROWTH, "Lingering Potion of Cell Growth")
        add(Items.LINGERING_POTION_MUTATION, "Lingering Potion of Mutation")
        add(Items.LINGERING_POTION_VIRAL_AGENTS, "Lingering Potion of Viral Agents")
        add(Items.LINGERING_POTION_THE_CURE, "Lingering Potion of the Cure")
        add(Items.LINGERING_POTION_ZOMBIFY_VILLAGER, "Lingering Potion of Zombify Villager")
        add(Items.TIPPED_ARROW_SUBSTRATE, "Arrow of Organic Substrate")
        add(Items.TIPPED_ARROW_CELL_GROWTH, "Arrow of Cell Growth")
        add(Items.TIPPED_ARROW_MUTATION, "Arrow of Mutation")
        add(Items.TIPPED_ARROW_VIRAL_AGENTS, "Arrow of Viral Agents")
        add(Items.TIPPED_ARROW_THE_CURE, "Arrow of the Cure")
        add(Items.TIPPED_ARROW_ZOMBIFY_VILLAGER, "Arrow of Zombify Villager")

        add(Messages.SCRAPER_CANT_SCRAPE, "This mob cannot be scraped.")
        add(Messages.COOLDOWN_EMERALD_HEART, "Emerald Heart is on cooldown!")
        add(Messages.DEATH_GENE_REMOVAL, "Death has reset your Genes!")
        add(Messages.RECENT_WOOLY, "This entity has already been sheared recently!")
        add(Messages.RECENT_MEATY, "This entity has already been meated recently!")
        add(Messages.RECENT_MILKY, "This entity has already been milked recently!")
        add(Messages.MILK_MILKED, "You have been milked!")
        add(Messages.SYRINGE_INJECTED, "You have gained the %1\$s Gene!")
        add(Messages.SYRINGE_FAILED, "You have failed to gain the %1\$s Gene!")
        add(Messages.SYRINGE_CONTAMINATED, "You can't inject yourself with contaminated blood!")
        add(Messages.METAL_SYRINGE_CONTAMINATED, "You can't inject entities with contaminated blood!")
        add(
            Messages.SUPPORT_SLIME_CREATIVE,
            "Support Slimes despawn with no owner! Give yourself the %1\$s Gene to stop them from despawning!"
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

        add(Blocks.BIOLUMINESCENCE, "Bioluminescence Glow")
        add(Blocks.CELL_ANALYZER, "Cell Analyzer")
        add(Blocks.COAL_GENERATOR, "Coal Generator")
        add(Blocks.DNA_DECRYPTOR, "DNA Decryptor")
        add(Blocks.DNA_EXTRACTOR, "DNA Extractor")
        add(Blocks.AIR_DISPERSAL, "Air Dispersal")
        add(Blocks.BLOOD_PURIFIER, "Blood Purifier")
        add(Blocks.CLONING_MACHINE, "Cloning Machine")
        add(Blocks.INCUBATOR, "Incubator")
        add(Blocks.ADVANCED_INCUBATOR, "Advanced Incubator")
        add(Blocks.PLASMID_INFUSER, "Plasmid Infuser")
        add(Blocks.PLASMID_INJECTOR, "Plasmid Injector")
        add(Blocks.ANTI_FIELD_BLOCK, "Anti-Field Block")

        add(DELICATE_TOUCH, "Delicate Touch")
        add(SUPPORT_SLIME, "Support Slime")

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
        add(Tooltips.PLASMID_AMOUNT, "Gene Points: %d/%d")
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
        add(Tooltips.ADDING_GENES, "Adding Genes:")
        add(Tooltips.REMOVING_GENES, "Removing:")
        add(
            Tooltips.GMO_LINE_ONE,
            "If in a low-temperature Advanced Incubator, it has a %1\$s chance of producing a %2\$s Gene."
        )
        add(Tooltips.GMO_LINE_TWO, "Otherwise, it always produces a %s Gene.")
        add(Tooltips.GMO_LINE_THREE, "Overclockers lower your chance, but Chorus Fruits increase it!")

        add(Commands.GENE_LIST, "Gene List:")
        add(Commands.NO_GENES, "No Genes found!")
        add(Commands.THEIR_GENES, "%1\$s's Genes:")
        add(Commands.REMOVED_LIGHTS, "Removed %d nearby bioluminescence light sources.")
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
        add(Cooldown.ENDED, " has come off cooldown!")

        add(Recipe.MOB, "Mob: %1\$s")
        add(Recipe.GENE, "Gene: %1\$s")
        add(Recipe.CHANCE, "Chance: %d%%")
        add(Recipe.REQUIRES_POINTS, "%1\$s requires %2\$d points of DNA.")
        add(Recipe.BASIC_WORTH, "Basic Genes = 1 point")
        add(Recipe.MATCHING_WORTH, "Matching Genes = 2 points")
        add(Recipe.INJECTOR_GENES, "Syringes can have as many Genes as you want!")
        add(Recipe.INJECTOR_ANTIGENES, "Syringes can have as many Genes (or Anti-Genes) as you want!")
        add(Recipe.GMO, "GMO Cell Incubating")
        add(Recipe.CELL_GROWTH, "Cell Growth")
        add(Recipe.SUBSTRATE, "Substrate Cell Duplication")
        add(Recipe.VIRUS, "Virus Cultivation")
        add(Recipe.BLACK_DEATH, "Black Death")

        add(,"Acquired by clicking a %1\$s with a Scraper.")
        add(,"%1\$s has these Genes:")
        add("\n%d%% of %2\$s")
        add("Required Genes:")
        add("Doubles the chances of inflicting Bleeding on hit.")
        add("Increases your mining speed as if you were using an Efficiency IV tool.\n\nStacks with Haste, as well as the actual Efficiency enchantment!")
        add("Gives you Creative mode style flight.")
        add("Gives you the Haste II potion effect.")
        add("Causes you to occasionally drop a cooked Porkchop.")
        add("Slowly feeds you when in direct sunlight.")
        add("Gives you the Regeneration IV potion effect.")
        add("Give you the Resistance II potion effect.")
        add("Give you the Speed IV potion effect.")
        add("Give you the Speed II potion effect.")
        add("Give you the Strength II potion effect.")
        add("Makes Zombies run away from you.")
        add("Makes Spiders and Cave Spiders run away from you.")
        add("Makes you leave behind a trail of light, which lasts a short while.")
        add("Has a chance of inflicting Bleeding on hit.\n\nBleeding deals damage over time.")
        add("Allows you to use the \"Dragon's Breath\" keybind to fire a dragon fireball.")
        add("Allows you to right-click Grass Blocks to regain hunger.")
        add("Increases your mining speed as if you were using an Efficiency tool.\n\nStacks with Haste, as well as the actual Efficiency enchantment!")
        add("Spawns an Emerald when you die.")
        add("Blocks damage if you're holding a Dragon Health Crystal.\n\nDamage is instead dealt to the Crystal's durability.")
        add("Makes you explode when you die, if you're holding at least 5 Gunpowder.\n\nEntities with the Gene do not need the Gunpowder.")
        add("Makes you immune to fire damage, and immediately extinguishes you.")
        add("Gives you the Haste potion effect.")
        add("Allows you to use Bows without any Arrows in your inventory.")
        add("Gives you the Invisibility potion effect.")
        add("Makes you pick up all nearby items.\n\nDisables when sneaking, when holding an active Anti-Field Orb, or near an active Anti-Field Block.")
        add("Gives you the Jump Boost potion effect.")
        add("Makes you keep your inventory when you die.")
        add("Makes you lay an Egg occasionally.")
        add("Gives you the Luck potion effect.")
        add("Allows you to be sheared for a raw Porkchop.")
        add("Allows you to be milked with a Bucket.")
        add("Gives all nearby entities the Glowing potion effect.")
        add("Gives you 10 additional hearts.")
        add("Gives you 10 MORE additional hearts.")
        add("Gives you the Night Vision potion effect.")
        add("Makes you immune to fall damage.")
        add("Prevents your hunger from going below halfway.")
        add("Automatically removes Poison.")
        add("Gives you the Regeneration potion effect.")
        add("Gives you the Resistance potion effect.")
        add("Makes Creepers run away from you.")
        add("Makes Skeletons run away from you.")
        add("Lets you shoot Fire Charges when you right-click Blaze Rods.")
        add("When you die, cancels it and spawns some Support Slimes to help you fight.\n\nHas a long cooldown.")
        add("Gives you the Speed potion effect.")
        add("Lets you step up full blocks.")
        add("Gives you the Strength potion effect.")
        add("Allows you to use the \"Teleport\" keybind to teleport a short distance where you're looking.")
        add("If you take damage while wearing either no Chestplate or a Leather Chestplate, there's a chance of reflecting some of the damage back.")
        add("Lets you climb walls like a Spider.")
        add("Keeps your air meter full.")
        add("Melee attacks inflict the Wither potion effect.")
        add("Makes you immune to the Wither potion effect.")
        add("Lets you be sheared for Wool.")
        add("Makes you pick up all nearby xp.\n\nDisables when sneaking, when holding an active Anti-Field Orb, or near an active Anti-Field Block.")
        add("Inflicts the Blindness potion effect.")
        add("Inflicts the Cursed potion effect.")
        add("Constantly lights you on fire")
        add("Inflicts the Hunger potion effect.")
        add("Inflicts the Levitation potion effect.")
        add("Inflicts the Mining Fatigue potion effect.")
        add("Inflicts the Nausea potion effect.")
        add("Inflicts the Poison potion effect.")
        add("Inflicts the Poison IV potion effect.")
        add("Inflicts the Slowness potion effect.")
        add("Inflicts the Slowness IV potion effect.")
        add("Inflicts the Slowness VI potion effect.")
        add("Inflicts the Weakness potion effect.")
        add("Inflicts the Wither potion effect.")
        add("Instantly kills.")
        add("Slowly kills Creepers.")
        add("Slowly kills monsters.")
        add("Slowly kills ageable mobs.")
        add("Slowly kills the undead.")
        add("Your chat messages are automatically read by the narrator (within 64 blocks).\n\nCan be disabled in the client config.")
        add("Makes you a Discord moderator.\n\n(UwU-fies your outgoing chat messages, and sets your language to LOLCAT)")
        add("Increases the knockback of your attacks.")
        add("Inflicts the Bad Omen potion effect.")
        add("Deal more damage using Axes")
        add("Has a chance of inflicting freezing damage on hit.")
        add("Unknown")
        add("Basic")
        add("Haste II")
        add("Efficiency IV")
        add("Regeneration IV")
        add("Speed IV")
        add("Speed II")
        add("Resistance II")
        add("Strength II")
        add("Meaty II")
        add("More Hearts II")
        add("Invisibility")
        add("Flight")
        add("Luck")
        add("Scare Zombies")
        add("Scare Spiders")
        add("Thorns")
        add("Claws II")
        add("Chatterbox")
        add("Chilling")
        add("Dragon's Breath")
        add("Eat Grass")
        add("Emerald Heart")
        add("Ender Dragon Health")
        add("Explosive Exit")
        add("Fire Proof")
        add("Item Magnet")
        add("Johnny")
        add("Jump Boost")
        add("Milky")
        add("More Hearts")
        add("Night Vision")
        add("No Fall Damage")
        add("Photosynthesis")
        add("Poison Immunity")
        add("Resistance")
        add("Knockback")
        add("Keep Inventory")
        add("Scare Creepers")
        add("Scare Skeletons")
        add("Shoot Fireballs")
        add("Slimy Death")
        add("Speed")
        add("Strength")
        add("Teleport")
        add("Water Breathing")
        add("Wooly")
        add("Wither Hit")
        add("Wither Proof")
        add("XP Magnet")
        add("Step Assist")
        add("Infinity")
        add("Bioluminescence")
        "Cybernetic"
        "Lay Eggs"
        "Meaty"
        "No Hunger"
        "Claws"
        "Haste"
        "Efficiency"
        "Climb Walls"
        "Mob Sight"
        "Regeneration"
        "Bad Omen"
        "Cringe"
        "Poison"
        "Poison II"
        "Poison IV"
        "Wither"
        "Weakness"
        "Blindness"
        "Slowness"
        "Slowness IV"
        "Slowness VI"
        "Nausea"
        "Hunger"
        "Flambé"
        "Cursed"
        "Levitation"
        "Mining Weakness"
        "Green Death"
        "Un-Undeath"
        "Gray Death"
        "White Death"
        "Black Death"
        "Void Death"
        " (Disabled)"
        "Craft a Scraper.\n\nScrapers are used to get Organic Matter from mobs.\n\nCraft the Big Book of Genetics for a more detailed guide!"
        "You'll Feel a Small Prick"
        "Craft a Syringe.\n\nSyringes are used to extract blood from yourself.\n\nA Metal Syringe can be used to extract blood from others, instead."
        "Cells at Work"
        "Craft a Cell Analyzer.\n\nIt converts Organic Matter into Cells."
        "DNA Extractor"
        "Craft a DNA Extractor.\n\nIt converts Cells into encrypted DNA Helices."
        "DNA Decryptor"
        "Craft a DNA Decryptor.\n\nIt discovers what Gene is inside a DNA Helix."
        "Plasmid Infuser"
        "Craft a Plasmid Infuser.\n\nUse it to set a Plasmid's Gene using DNA Helices."
        "In-Gene-ius!"
        "Decrypt a DNA Helix to find out its Gene."
        "Plasmid Injector"
        "Craft a Plasmid Injector.\n\nUse it to inject full Plasmids into into decontaminated Syringes."
        "B Positive!"
        "Craft a Blood Purifier.\n\nUse it to decontaminate Syringes."
        "What Are Midichlorians?"
        "Inject yourself with any Gene."
        "Level 5 Non-Milk Substance"
        "Get milked with the Milky Gene.\n\nYou can milk yourself by sneaking!"
        "The PathOwOgen "
        "Get the Cringe Gene. Embrace your inner self."
        "Me and the Boys"
        "Trigger the Slimy Death Gene."
        "To Infinity and Beyond!"
        "Get the Flight Gene."
        "Boo!                 "
        "Get the Scare Zombies, Scare Spiders, Scare Creepers, and Scare Skeletons Genes."
        "La Grand Mort"
        "Craft a DNA Helix that has The Black Death."
        "%1\$s was scraped to the bone"
        "%1\$s died of blood loss"
        "%1\$s stepped on a loose Syringe"
        "Genetics: Resequenced"
        "Dragon's Breath"
        "Teleport"
        "Welcome to Genetics: Resequenced! This guide book will help you get started with the mod."

    }
}