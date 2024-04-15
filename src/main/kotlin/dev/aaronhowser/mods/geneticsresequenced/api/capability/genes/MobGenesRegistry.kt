package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

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
        return mobGenes[entityType] ?: emptyMap()
    }

    fun getRegistry(): Map<EntityType<*>, Map<Gene?, Int>> {
        return mobGenes.toMap()
    }

    fun registerDefaultGenes() {
        // Animals
        addGenesToEntity(
            EntityType.VILLAGER,
            mapOf(
                Gene.MOB_SIGHT to 1,
                Gene.EMERALD_HEART to 1,
                Gene.REGENERATION to 1
            )
        )
        addGenesToEntity(
            EntityType.SHEEP,
            mapOf(
                Gene.EAT_GRASS to 1,
                Gene.WOOLY to 1
            )
        )
        addGenesToEntity(
            EntityType.COW,
            mapOf(
                Gene.EAT_GRASS to 1,
                Gene.MILKY to 1
            )
        )
        addGenesToEntity(
            EntityType.PIG,
            mapOf(Gene.MEATY to 1)
        )
        addGenesToEntity(
            EntityType.HORSE,
            mapOf(
                Gene.JUMP_BOOST to 1,
                Gene.STEP_ASSIST to 1, Gene.SPEED to 1
            )
        )
        addGenesToEntity(
            EntityType.CHICKEN,
            mapOf(
                Gene.NO_FALL_DAMAGE to 1,
                Gene.LAY_EGG to 1
            )
        )
        addGenesToEntity(
            EntityType.BAT,
            mapOf(
                Gene.MOB_SIGHT to 1,
                Gene.NIGHT_VISION to 1
            )
        )
        addGenesToEntity(
            EntityType.PARROT,
            mapOf(Gene.NO_FALL_DAMAGE to 1)
        )
        addGenesToEntity(
            EntityType.OCELOT,
            mapOf(
                Gene.SPEED to 1,
                Gene.SCARE_CREEPERS to 1
            )
        )
        addGenesToEntity(
            EntityType.POLAR_BEAR,
            mapOf(
                Gene.STRENGTH to 1,
                Gene.CLAWS to 1, Gene.STEP_ASSIST to 1
            )
        )
        addGenesToEntity(
            EntityType.LLAMA,
            mapOf(
                Gene.STRENGTH to 1,
                Gene.STEP_ASSIST to 1
            )
        )
        addGenesToEntity(
            EntityType.WOLF,
            mapOf(
                Gene.SCARE_SKELETONS to 1,
                Gene.NIGHT_VISION to 1, Gene.NO_HUNGER to 1
            )
        )
        addGenesToEntity(
            EntityType.RABBIT,
            mapOf(
                Gene.JUMP_BOOST to 1,
                Gene.SPEED to 1
            )
        )
        addGenesToEntity(
            EntityType.FOX,
            mapOf(
                Gene.SPEED to 1,
                Gene.JUMP_BOOST to 1,
                Gene.NIGHT_VISION to 1
            )
        )
        addGenesToEntity(
            EntityType.SQUID,
            mapOf(Gene.WATER_BREATHING to 1)
        )
        addGenesToEntity(
            EntityType.GLOW_SQUID,
            mapOf(
                Gene.WATER_BREATHING to 1,
                Gene.BIOLUMINESCENCE to 1
            )
        )
        addGenesToEntity(
            EntityType.DOLPHIN,
            mapOf(
                Gene.WATER_BREATHING to 1,
                Gene.SPEED to 1,
                Gene.JUMP_BOOST to 1
            )
        )
        addGenesToEntity(
            EntityType.IRON_GOLEM,
            mapOf(
                Gene.MORE_HEARTS to 1,
                Gene.REGENERATION to 1,
                Gene.RESISTANCE to 1
            )
        )

        //Overworld Hostiles
        addGenesToEntity(
            EntityType.ZOMBIE,
            mutableMapOf(Gene.RESISTANCE to 1)
        )
        addGenesToEntity(
            EntityType.CREEPER,
            mutableMapOf(Gene.EXPLOSIVE_EXIT to 1)
        )
        addGenesToEntity(
            EntityType.SPIDER,
            mutableMapOf(
                Gene.NIGHT_VISION to 1,
                Gene.WALL_CLIMBING to 1
            )
        )
        addGenesToEntity(
            EntityType.CAVE_SPIDER,
            mutableMapOf(
                Gene.NIGHT_VISION to 1,
                Gene.WALL_CLIMBING to 1,
                Gene.POISON_IMMUNITY to 1
            )
        )
        addGenesToEntity(
            EntityType.SLIME,
            mutableMapOf(
                Gene.NO_FALL_DAMAGE to 1,
                Gene.SLIMY_DEATH to 1
            )
        )
        addGenesToEntity(
            EntityType.GUARDIAN,
            mutableMapOf(Gene.WATER_BREATHING to 1)
        )
        addGenesToEntity(
            EntityType.ELDER_GUARDIAN,
            mutableMapOf(
                Gene.MOB_SIGHT to 1,
                Gene.WATER_BREATHING to 1
            )
        )
        addGenesToEntity(
            EntityType.SKELETON,
            mutableMapOf(Gene.INFINITY to 1)
        )
        addGenesToEntity(
            EntityType.SILVERFISH,
            mutableMapOf(
                Gene.HASTE to 1,
                Gene.EFFICIENCY to 1
            )
        )

        //End hostiles
        addGenesToEntity(
            EntityType.ENDERMITE,
            mutableMapOf(
                Gene.KEEP_INVENTORY to 1,
                Gene.ITEM_MAGNET to 1,
                Gene.XP_MAGNET to 1
            )
        )
        addGenesToEntity(
            EntityType.ENDERMAN,
            mutableMapOf(
                Gene.TELEPORT to 1,
                Gene.MORE_HEARTS to 1
            )
        )
        addGenesToEntity(
            EntityType.SHULKER,
            mutableMapOf(
                Gene.RESISTANCE to 1,
                Gene.REGENERATION to 1
            )
        )

        //Nether hostiles
        addGenesToEntity(
            EntityType.WITHER_SKELETON,
            mutableMapOf(
                Gene.WITHER_HIT to 1,
                Gene.INFINITY to 1
            )
        )
        addGenesToEntity(
            EntityType.BLAZE,
            mutableMapOf(
                Gene.SHOOT_FIREBALLS to 1,
                Gene.FIRE_PROOF to 1,
                Gene.BIOLUMINESCENCE to 1
            )
        )
        addGenesToEntity(
            EntityType.GHAST,
            mutableMapOf(
                Gene.MOB_SIGHT to 1,
                Gene.SHOOT_FIREBALLS to 1
            )
        )
        addGenesToEntity(
            EntityType.ZOMBIFIED_PIGLIN,
            mutableMapOf(
                Gene.FIRE_PROOF to 1,
                Gene.MEATY to 1
            )
        )
        addGenesToEntity(
            EntityType.HOGLIN,
            mutableMapOf(Gene.MEATY to 1)
        )
        addGenesToEntity(
            EntityType.PIGLIN,
            mutableMapOf(Gene.MEATY to 1)
        )
        addGenesToEntity(
            EntityType.MAGMA_CUBE,
            mutableMapOf(
                Gene.FIRE_PROOF to 1,
                Gene.BIOLUMINESCENCE to 1
            )
        )

        //Bosses
        addGenesToEntity(EntityType.WITHER, mapOf(Gene.WITHER_PROOF to 1))
        addGenesToEntity(
            EntityType.ENDER_DRAGON,
            mapOf(
                Gene.DRAGONS_BREATH to 1,
                Gene.ENDER_DRAGON_HEALTH to 1,
                Gene.FLIGHT to 1
            )
        )
    }

}