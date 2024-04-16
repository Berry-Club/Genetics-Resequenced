package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.entity.EntityType

object MobGenesRegistry {

    private val mobGenes: MutableMap<EntityType<*>, MutableMap<Gene?, Int>> = mutableMapOf()

    fun addGenesToEntity(entityType: EntityType<*>, weightToGeneMap: Map<Gene?, Int>) {
        val geneList: MutableMap<Gene?, Int> = mobGenes[entityType] ?: mutableMapOf()
        geneList.putAll(weightToGeneMap)
        mobGenes[entityType] = geneList
    }

    fun removeGenesFromEntity(entityType: EntityType<*>, vararg genes: Gene) {
        val geneList = mobGenes[entityType] ?: run {
            GeneticsResequenced.LOGGER.warn("Tried to remove genes from $entityType, but it has no genes")
            return
        }

        genes.forEach { geneList.remove(key = it) }
    }

    fun getGenesForEntity(entityType: EntityType<*>): Map<Gene?, Int> {
        return mobGenes[entityType] ?: mapOf(null to 1)
    }

    fun getRegistry(): Map<EntityType<*>, Map<Gene?, Int>> {
        return mobGenes.toMap()
    }

    fun registerDefaultGenes() {
        // Animals
        addGenesToEntity(
            EntityType.VILLAGER,
            mapOf(
                null to 5,
                DefaultGenes.MOB_SIGHT to 1,
                DefaultGenes.EMERALD_HEART to 1,
                DefaultGenes.REGENERATION to 1
            )
        )
        addGenesToEntity(
            EntityType.SHEEP,
            mapOf(
                null to 5,
                DefaultGenes.EAT_GRASS to 1,
                DefaultGenes.WOOLY to 1
            )
        )
        addGenesToEntity(
            EntityType.COW,
            mapOf(
                null to 5,
                DefaultGenes.EAT_GRASS to 1,
                DefaultGenes.MILKY to 1
            )
        )
        addGenesToEntity(
            EntityType.PIG,
            mapOf(
                null to 5,
                DefaultGenes.MEATY to 1
            )
        )
        addGenesToEntity(
            EntityType.HORSE,
            mapOf(
                null to 5,
                DefaultGenes.JUMP_BOOST to 1,
                DefaultGenes.STEP_ASSIST to 1,
                DefaultGenes.SPEED to 1
            )
        )
        addGenesToEntity(
            EntityType.CHICKEN,
            mapOf(
                null to 5,
                DefaultGenes.NO_FALL_DAMAGE to 1,
                DefaultGenes.LAY_EGG to 1
            )
        )
        addGenesToEntity(
            EntityType.BAT,
            mapOf(
                null to 5,
                DefaultGenes.MOB_SIGHT to 1,
                DefaultGenes.NIGHT_VISION to 1
            )
        )
        addGenesToEntity(
            EntityType.PARROT,
            mapOf(
                null to 5,
                DefaultGenes.NO_FALL_DAMAGE to 1
            )
        )
        addGenesToEntity(
            EntityType.OCELOT,
            mapOf(
                null to 5,
                DefaultGenes.SPEED to 1,
                DefaultGenes.SCARE_CREEPERS to 1
            )
        )
        addGenesToEntity(
            EntityType.POLAR_BEAR,
            mapOf(
                null to 5,
                DefaultGenes.STRENGTH to 1,
                DefaultGenes.CLAWS to 1,
                DefaultGenes.STEP_ASSIST to 1
            )
        )
        addGenesToEntity(
            EntityType.LLAMA,
            mapOf(
                null to 5,
                DefaultGenes.STRENGTH to 1,
                DefaultGenes.STEP_ASSIST to 1
            )
        )
        addGenesToEntity(
            EntityType.WOLF,
            mapOf(
                null to 5,
                DefaultGenes.SCARE_SKELETONS to 1,
                DefaultGenes.NIGHT_VISION to 1,
                DefaultGenes.NO_HUNGER to 1
            )
        )
        addGenesToEntity(
            EntityType.RABBIT,
            mapOf(
                null to 5,
                DefaultGenes.JUMP_BOOST to 1,
                DefaultGenes.SPEED to 1
            )
        )
        addGenesToEntity(
            EntityType.FOX,
            mapOf(
                null to 5,
                DefaultGenes.SPEED to 1,
                DefaultGenes.JUMP_BOOST to 1,
                DefaultGenes.NIGHT_VISION to 1
            )
        )
        addGenesToEntity(
            EntityType.SQUID,
            mapOf(
                null to 5,
                DefaultGenes.WATER_BREATHING to 1
            )
        )
        addGenesToEntity(
            EntityType.GLOW_SQUID,
            mapOf(
                null to 5,
                DefaultGenes.WATER_BREATHING to 1,
                DefaultGenes.BIOLUMINESCENCE to 1
            )
        )
        addGenesToEntity(
            EntityType.DOLPHIN,
            mapOf(
                null to 5,
                DefaultGenes.WATER_BREATHING to 1,
                DefaultGenes.SPEED to 1,
                DefaultGenes.JUMP_BOOST to 1
            )
        )
        addGenesToEntity(
            EntityType.IRON_GOLEM,
            mapOf(
                null to 5,
                DefaultGenes.MORE_HEARTS to 1,
                DefaultGenes.REGENERATION to 1,
                DefaultGenes.RESISTANCE to 1
            )
        )

        //Overworld Hostiles
        addGenesToEntity(
            EntityType.ZOMBIE,
            mutableMapOf(
                null to 5,
                DefaultGenes.RESISTANCE to 1
            )
        )
        addGenesToEntity(
            EntityType.CREEPER,
            mutableMapOf(
                null to 5,
                DefaultGenes.EXPLOSIVE_EXIT to 1
            )
        )
        addGenesToEntity(
            EntityType.SPIDER,
            mutableMapOf(
                null to 5,
                DefaultGenes.NIGHT_VISION to 1,
                DefaultGenes.WALL_CLIMBING to 1
            )
        )
        addGenesToEntity(
            EntityType.CAVE_SPIDER,
            mutableMapOf(
                null to 5,
                DefaultGenes.NIGHT_VISION to 1,
                DefaultGenes.WALL_CLIMBING to 1,
                DefaultGenes.POISON_IMMUNITY to 1
            )
        )
        addGenesToEntity(
            EntityType.SLIME,
            mutableMapOf(
                null to 5,
                DefaultGenes.NO_FALL_DAMAGE to 1,
                DefaultGenes.SLIMY_DEATH to 1
            )
        )
        addGenesToEntity(
            EntityType.GUARDIAN,
            mutableMapOf(
                null to 5,
                DefaultGenes.WATER_BREATHING to 1
            )
        )
        addGenesToEntity(
            EntityType.ELDER_GUARDIAN,
            mutableMapOf(
                null to 5,
                DefaultGenes.MOB_SIGHT to 1,
                DefaultGenes.WATER_BREATHING to 1
            )
        )
        addGenesToEntity(
            EntityType.SKELETON,
            mutableMapOf(
                null to 5,
                DefaultGenes.INFINITY to 1
            )
        )
        addGenesToEntity(
            EntityType.SILVERFISH,
            mutableMapOf(
                null to 5,
                DefaultGenes.HASTE to 1,
                DefaultGenes.EFFICIENCY to 1
            )
        )

        //End hostiles
        addGenesToEntity(
            EntityType.ENDERMITE,
            mutableMapOf(
                null to 5,
                DefaultGenes.KEEP_INVENTORY to 1,
                DefaultGenes.ITEM_MAGNET to 1,
                DefaultGenes.XP_MAGNET to 1
            )
        )
        addGenesToEntity(
            EntityType.ENDERMAN,
            mutableMapOf(
                null to 5,
                DefaultGenes.TELEPORT to 1,
                DefaultGenes.MORE_HEARTS to 1
            )
        )
        addGenesToEntity(
            EntityType.SHULKER,
            mutableMapOf(
                null to 5,
                DefaultGenes.RESISTANCE to 1,
                DefaultGenes.REGENERATION to 1
            )
        )

        //Nether hostiles
        addGenesToEntity(
            EntityType.WITHER_SKELETON,
            mutableMapOf(
                null to 5,
                DefaultGenes.WITHER_HIT to 1
            )
        )
        addGenesToEntity(
            EntityType.BLAZE,
            mutableMapOf(
                null to 5,
                DefaultGenes.SHOOT_FIREBALLS to 1,
                DefaultGenes.FIRE_PROOF to 1,
                DefaultGenes.BIOLUMINESCENCE to 1
            )
        )
        addGenesToEntity(
            EntityType.GHAST,
            mutableMapOf(
                null to 5,
                DefaultGenes.MOB_SIGHT to 1,
                DefaultGenes.SHOOT_FIREBALLS to 1
            )
        )
        addGenesToEntity(
            EntityType.ZOMBIFIED_PIGLIN,
            mutableMapOf(
                null to 5,
                DefaultGenes.FIRE_PROOF to 1,
                DefaultGenes.MEATY to 1
            )
        )
        addGenesToEntity(
            EntityType.HOGLIN,
            mutableMapOf(
                null to 5,
                DefaultGenes.MEATY to 1
            )
        )
        addGenesToEntity(
            EntityType.PIGLIN,
            mutableMapOf(
                null to 5,
                DefaultGenes.MEATY to 1
            )
        )
        addGenesToEntity(
            EntityType.MAGMA_CUBE,
            mutableMapOf(
                null to 5,
                DefaultGenes.FIRE_PROOF to 1,
                DefaultGenes.BIOLUMINESCENCE to 1
            )
        )

        //Bosses
        addGenesToEntity(
            EntityType.WITHER, mapOf(
                null to 5,
                DefaultGenes.WITHER_PROOF to 1
            )
        )
        addGenesToEntity(
            EntityType.ENDER_DRAGON,
            mapOf(
                null to 5,
                DefaultGenes.DRAGONS_BREATH to 1,
                DefaultGenes.ENDER_DRAGON_HEALTH to 1,
                DefaultGenes.FLIGHT to 1
            )
        )
    }

}