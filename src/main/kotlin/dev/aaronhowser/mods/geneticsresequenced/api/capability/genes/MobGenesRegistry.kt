package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.entity.EntityType

object MobGenesRegistry {

    private val mobGenes: MutableMap<EntityType<*>, MutableSet<Gene>> = mutableMapOf()

    fun addGenesToEntity(entityType: EntityType<*>, vararg genes: Gene) {
        val geneList = mobGenes.getOrPut(entityType) { mutableSetOf() }
        geneList.addAll(genes)
        GeneticsResequenced.LOGGER.debug("Added genes to $entityType: ${genes.joinToString(", ") { it.id }}")
    }

    fun removeGenesFromEntity(entityType: EntityType<*>, vararg genes: Gene) {
        val geneList = mobGenes[entityType] ?: run {
            GeneticsResequenced.LOGGER.warn("Tried to remove genes from $entityType, but it has no genes")
            return
        }
        geneList.removeAll(genes.toSet())
        GeneticsResequenced.LOGGER.debug("Removed genes from $entityType: ${genes.joinToString(", ")}")
    }

    fun getGenesForEntity(entityType: EntityType<*>): Set<Gene> {
        return mobGenes[entityType] ?: emptySet()
    }

    fun getRegistry(): Map<EntityType<*>, Set<Gene>> {
        return mobGenes.toMap()
    }

    fun registerDefaultGenes() {
        // Animals
        addGenesToEntity(EntityType.VILLAGER, Gene.MOB_SIGHT, Gene.EMERALD_HEART, Gene.REGENERATION)
        addGenesToEntity(EntityType.SHEEP, Gene.EAT_GRASS, Gene.WOOLY)
        addGenesToEntity(EntityType.COW, Gene.EAT_GRASS, Gene.MILKY)
        addGenesToEntity(EntityType.PIG, Gene.MEATY)
        addGenesToEntity(EntityType.HORSE, Gene.JUMP_BOOST, Gene.STEP_ASSIST, Gene.SPEED)
        addGenesToEntity(EntityType.CHICKEN, Gene.NO_FALL_DAMAGE, Gene.LAY_EGG)
        addGenesToEntity(EntityType.BAT, Gene.MOB_SIGHT, Gene.NIGHT_VISION)
        addGenesToEntity(EntityType.PARROT, Gene.NO_FALL_DAMAGE)
        addGenesToEntity(EntityType.OCELOT, Gene.SPEED, Gene.SCARE_CREEPERS)
        addGenesToEntity(EntityType.POLAR_BEAR, Gene.STRENGTH, Gene.CLAWS, Gene.STEP_ASSIST)
        addGenesToEntity(EntityType.LLAMA, Gene.STRENGTH, Gene.STEP_ASSIST)
        addGenesToEntity(EntityType.WOLF, Gene.SCARE_SKELETONS, Gene.NIGHT_VISION, Gene.NO_HUNGER)
        addGenesToEntity(EntityType.RABBIT, Gene.JUMP_BOOST, Gene.SPEED)
        addGenesToEntity(EntityType.FOX, Gene.SPEED, Gene.JUMP_BOOST, Gene.NIGHT_VISION)
        addGenesToEntity(EntityType.SQUID, Gene.WATER_BREATHING)
        addGenesToEntity(EntityType.GLOW_SQUID, Gene.WATER_BREATHING, Gene.BIOLUMINESCENCE)
        addGenesToEntity(EntityType.DOLPHIN, Gene.WATER_BREATHING, Gene.SPEED, Gene.JUMP_BOOST)
        addGenesToEntity(EntityType.IRON_GOLEM, Gene.MORE_HEARTS, Gene.REGENERATION, Gene.RESISTANCE)

        //Overworld Hostiles
        addGenesToEntity(EntityType.ZOMBIE, Gene.RESISTANCE)
        addGenesToEntity(EntityType.CREEPER, Gene.EXPLOSIVE_EXIT)
        addGenesToEntity(EntityType.SPIDER, Gene.NIGHT_VISION, Gene.WALL_CLIMBING)
        addGenesToEntity(EntityType.CAVE_SPIDER, Gene.NIGHT_VISION, Gene.WALL_CLIMBING, Gene.POISON_IMMUNITY)
        addGenesToEntity(EntityType.SLIME, Gene.NO_FALL_DAMAGE, Gene.SLIMY_DEATH)
        addGenesToEntity(EntityType.GUARDIAN, Gene.WATER_BREATHING)
        addGenesToEntity(EntityType.ELDER_GUARDIAN, Gene.MOB_SIGHT, Gene.WATER_BREATHING)
        addGenesToEntity(EntityType.SKELETON, Gene.INFINITY)
        addGenesToEntity(EntityType.SILVERFISH, Gene.HASTE, Gene.EFFICIENCY)

        //End hostiles
        addGenesToEntity(EntityType.ENDERMITE, Gene.KEEP_INVENTORY, Gene.ITEM_MAGNET, Gene.XP_MAGNET)
        addGenesToEntity(EntityType.ENDERMAN, Gene.TELEPORT, Gene.MORE_HEARTS)
        addGenesToEntity(EntityType.SHULKER, Gene.RESISTANCE, Gene.REGENERATION)

        //Nether hostiles
        addGenesToEntity(EntityType.WITHER_SKELETON, Gene.WITHER_HIT, Gene.INFINITY)
        addGenesToEntity(EntityType.BLAZE, Gene.SHOOT_FIREBALLS, Gene.FIRE_PROOF, Gene.BIOLUMINESCENCE)
        addGenesToEntity(EntityType.GHAST, Gene.MOB_SIGHT, Gene.SHOOT_FIREBALLS)
        addGenesToEntity(EntityType.ZOMBIFIED_PIGLIN, Gene.FIRE_PROOF, Gene.MEATY)
        addGenesToEntity(EntityType.HOGLIN, Gene.MEATY)
        addGenesToEntity(EntityType.PIGLIN, Gene.MEATY)
        addGenesToEntity(EntityType.MAGMA_CUBE, Gene.FIRE_PROOF, Gene.BIOLUMINESCENCE)
        //Bosses
        addGenesToEntity(EntityType.WITHER, Gene.WITHER_PROOF)
        addGenesToEntity(EntityType.ENDER_DRAGON, Gene.DRAGONS_BREATH, Gene.ENDER_DRAGON_HEALTH, Gene.FLIGHT)
    }

}