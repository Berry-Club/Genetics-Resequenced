package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isMutation
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

object ModGenes {

	val GENE_REGISTRY_KEY: ResourceKey<Registry<Gene>> =
		ResourceKey.createRegistryKey(OtherUtil.modResource("gene"))

	@JvmStatic
	fun getGeneRegistry(registries: HolderLookup.Provider): HolderLookup.RegistryLookup<Gene> {
		return registries.lookupOrThrow(GENE_REGISTRY_KEY)
	}

	@JvmStatic
	fun getAllGeneHolders(registries: HolderLookup.Provider): Stream<Holder.Reference<Gene>> {
		return getGeneRegistry(registries).listElements()
	}

	@JvmStatic
	fun fromResourceKey(registries: HolderLookup.Provider, rk: ResourceKey<Gene>): Holder.Reference<Gene>? {
		return getGeneRegistry(registries).get(rk).getOrNull()
	}

	@JvmStatic
	fun fromResourceLocation(registries: HolderLookup.Provider, rl: ResourceLocation): Holder.Reference<Gene>? {
		return fromResourceKey(registries, ResourceKey.create(GENE_REGISTRY_KEY, rl))
	}

	@JvmStatic
	fun fromString(registries: HolderLookup.Provider, id: String): Holder<Gene>? {
		return fromResourceLocation(registries, ResourceLocation(id))
	}

	@JvmStatic
	fun fromIdPath(registries: HolderLookup.Provider, path: String): Holder.Reference<Gene>? {
		return getAllGeneHolders(registries).filter { it.key().location().path == path }.findFirst().orElse(null)
	}

	fun getRegistrySorted(
		registries: HolderLookup.Provider,
		includeHelixOnly: Boolean = false,
		includeDisabled: Boolean = false
	): List<Holder<Gene>> {
		val mutations = mutableListOf<Holder.Reference<Gene>>()
		val negatives = mutableListOf<Holder.Reference<Gene>>()
		val other = mutableListOf<Holder.Reference<Gene>>()

		for (geneHolder in getAllGeneHolders(registries)) {
			if (geneHolder.isDisabled && !includeDisabled) continue
			if (geneHolder.isHelixOnly && !includeHelixOnly) continue

			when {
				geneHolder.isMutation -> mutations.add(geneHolder)
				geneHolder.isNegative -> negatives.add(geneHolder)
				else -> other.add(geneHolder)
			}
		}

		return other.sortedBy { it.key() } + mutations.sortedBy { it.key() } + negatives.sortedBy { it.key() }
	}

	private fun resourceKey(geneName: String): ResourceKey<Gene> {
		return ResourceKey.create(GENE_REGISTRY_KEY, OtherUtil.modResource(geneName))
	}

	val BASIC = resourceKey("basic")

	// @formatter:off

	//Standard list

	@JvmField val BIOLUMINESCENCE = resourceKey("bioluminescence")
	@JvmField val CHATTERBOX = resourceKey("chatterbox")
	@JvmField val CHILLING = resourceKey("chilling")
	@JvmField val CLAWS = resourceKey("claws")
	@JvmField val DRAGON_BREATH = resourceKey("dragons_breath")
	@JvmField val EAT_GRASS = resourceKey("eat_grass")
	@JvmField val EFFICIENCY = resourceKey("efficiency")
	@JvmField val EMERALD_HEART = resourceKey("emerald_heart")
	@JvmField val ENDER_DRAGON_HEALTH = resourceKey("ender_dragon_health")
	@JvmField val EXPLOSIVE_EXIT = resourceKey("explosive_exit")
	@JvmField val FIRE_PROOF = resourceKey("fire_proof")
	@JvmField val HASTE = resourceKey("haste")
	@JvmField val INFINITY = resourceKey("infinity")
	@JvmField val INVISIBLE = resourceKey("invisible")
	@JvmField val ITEM_MAGNET = resourceKey("item_magnet")
	@JvmField val JUMP_BOOST = resourceKey("jump_boost")
	@JvmField val JOHNNY = resourceKey("johnny")
	@JvmField val KEEP_INVENTORY = resourceKey("keep_inventory")
	@JvmField val KNOCKBACK = resourceKey("knockback")
	@JvmField val LAY_EGG = resourceKey("lay_egg")
	@JvmField val LUCK = resourceKey("luck")
	@JvmField val MEATY = resourceKey("meaty")
	@JvmField val MILKY = resourceKey("milky")
	@JvmField val MOB_SIGHT = resourceKey("mob_sight")
	@JvmField val MORE_HEARTS = resourceKey("more_hearts")
	@JvmField val NIGHT_VISION = resourceKey("night_vision")
	@JvmField val NO_FALL_DAMAGE = resourceKey("no_fall_damage")
	@JvmField val NO_HUNGER = resourceKey("no_hunger")
	@JvmField val POISON_IMMUNITY = resourceKey("poison_immunity")
	@JvmField val REGENERATION = resourceKey("regeneration")
	@JvmField val REACHING = resourceKey("reaching")
	@JvmField val RESISTANCE = resourceKey("resistance")
	@JvmField val SCARE_CREEPERS = resourceKey("scare_creepers")
	@JvmField val SCARE_SKELETONS = resourceKey("scare_skeletons")
	@JvmField val SHOOT_FIREBALLS = resourceKey("shoot_fireballs")
	@JvmField val SLIMY_DEATH = resourceKey("slimy_death")
	@JvmField val SPEED = resourceKey("speed")
	@JvmField val STEP_ASSIST = resourceKey("step_assist")
	@JvmField val STRENGTH = resourceKey("strength")
	@JvmField val TELEPORT = resourceKey("teleport")
	@JvmField val THORNS = resourceKey("thorns")
	@JvmField val WALL_CLIMBING = resourceKey("wall_climbing")
	@JvmField val WATER_BREATHING = resourceKey("water_breathing")
	@JvmField val WEB_DEFENSE = resourceKey("web_defense")
	@JvmField val WEB_WALKER = resourceKey("web_walker")
	@JvmField val WITHER_HIT = resourceKey("wither_hit")
	@JvmField val WITHER_PROOF = resourceKey("wither_proof")
	@JvmField val WOOLY = resourceKey("wooly")
	@JvmField val XP_MAGNET = resourceKey("xp_magnet")

	// For Mobs
	@JvmField val BOUNTIFUL = resourceKey("bountiful")

	// Mutations
	@JvmField val CLAWS_TWO = resourceKey("claws_2")
	@JvmField val EFFICIENCY_FOUR = resourceKey("efficiency_4")
	@JvmField val FLIGHT = resourceKey("flight")
	@JvmField val HASTE_TWO = resourceKey("haste_2")
	@JvmField val MEATY_TWO = resourceKey("meaty_2")
	@JvmField val MORE_HEARTS_TWO = resourceKey("more_hearts_2")
	@JvmField val PHOTOSYNTHESIS = resourceKey("photosynthesis")
	@JvmField val REGENERATION_FOUR = resourceKey("regeneration_4")
	@JvmField val RESISTANCE_TWO = resourceKey("resistance_2")
	@JvmField val SPEED_FOUR = resourceKey("speed_4")
	@JvmField val SPEED_TWO = resourceKey("speed_2")
	@JvmField val STRENGTH_TWO = resourceKey("strength_2")
	@JvmField val SCARE_ZOMBIES = resourceKey("scare_zombies")
	@JvmField val SCARE_SPIDERS = resourceKey("scare_spiders")
	@JvmField val LAVA_PROOF = resourceKey("lava_proof")

	//Negative effects

	@JvmField val BLINDNESS = resourceKey("blindness")
	@JvmField val CRINGE = resourceKey("cringe")
	@JvmField val CURSED = resourceKey("cursed")
	@JvmField val FLAMBE = resourceKey("flambe")
	@JvmField val HUNGER = resourceKey("hunger")
	@JvmField val INFESTED = resourceKey("infested")
	@JvmField val LEVITATION = resourceKey("levitation")
	@JvmField val MINING_FATIGUE = resourceKey("mining_fatigue")
	@JvmField val NAUSEA = resourceKey("nausea")
	@JvmField val OOZING = resourceKey("oozing")
	@JvmField val POISON = resourceKey("poison")
	@JvmField val POISON_FOUR = resourceKey("poison_4")
	@JvmField val SLOWNESS = resourceKey("slowness")
	@JvmField val SLOWNESS_FOUR = resourceKey("slowness_4")
	@JvmField val SLOWNESS_SIX = resourceKey("slowness_6")
	@JvmField val WEAVING = resourceKey("weaving")
	@JvmField val WEAKNESS = resourceKey("weakness")
	@JvmField val WIND_CHARGED = resourceKey("wind_charged")
	@JvmField val WITHER = resourceKey("wither")

	// Plagues

	@JvmField val BLACK_DEATH = resourceKey("black_death")
	@JvmField val GREEN_DEATH = resourceKey("green_death")
	@JvmField val WHITE_DEATH = resourceKey("white_death")
	@JvmField val GRAY_DEATH = resourceKey("gray_death")
	@JvmField val UN_UNDEATH = resourceKey("un_undeath")

	// @formatter:on

	fun ResourceKey<Gene>.getHolder(registries: HolderLookup.Provider): Holder.Reference<Gene>? =
		fromResourceKey(registries, this)

	fun ResourceKey<Gene>.getHolderOrThrow(registries: HolderLookup.Provider): Holder.Reference<Gene> =
		getHolder(registries) ?: throw IllegalArgumentException("Gene ${location()} not found")

}