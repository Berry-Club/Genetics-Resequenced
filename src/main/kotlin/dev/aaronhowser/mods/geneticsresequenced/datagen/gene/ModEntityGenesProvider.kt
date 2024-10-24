package dev.aaronhowser.mods.geneticsresequenced.datagen.gene

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.PackType
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.JsonCodecProvider
import java.util.concurrent.CompletableFuture

class ModEntityGenesProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : JsonCodecProvider<EntityGenes.EntityGenesData>(
    output,
    PackOutput.Target.DATA_PACK,
    EntityGenes.DIRECTORY,
    PackType.SERVER_DATA,
    EntityGenes.EntityGenesData.CODEC,
    lookupProvider,
    GeneticsResequenced.ID,
    existingFileHelper
) {

    private fun addEntityGenes(
        entityType: EntityType<*>,
        vararg geneWeights: Pair<ResourceKey<Gene>, Int>
    ) {
        val entityRk = ResourceKey.create(Registries.ENTITY_TYPE, EntityType.getKey(entityType))

        this.unconditional(
            entityRk.location(),
            EntityGenes.EntityGenesData(
                entityRk,
                geneWeights.toMap()
            )
        )
    }

    override fun gather() {
        this.addEntityGenes(EntityType.ALLAY, ModGenes.BASIC to 3, ModGenes.ITEM_MAGNET to 5)
        this.addEntityGenes(EntityType.AXOLOTL, ModGenes.BASIC to 2, ModGenes.WATER_BREATHING to 5)
        this.addEntityGenes(EntityType.BAT, ModGenes.BASIC to 4, ModGenes.NIGHT_VISION to 1, ModGenes.MOB_SIGHT to 3)
        this.addEntityGenes(EntityType.BEE, ModGenes.BASIC to 5, ModGenes.THORNS to 3)
        this.addEntityGenes(EntityType.BLAZE, ModGenes.BASIC to 5, ModGenes.SHOOT_FIREBALLS to 3, ModGenes.FIRE_PROOF to 1, ModGenes.BIOLUMINESCENCE to 3)
        this.addEntityGenes(EntityType.BREEZE, ModGenes.BASIC to 2, ModGenes.JUMP_BOOST to 5, ModGenes.WIND_CHARGED to 4)
        this.addEntityGenes(EntityType.CAT, ModGenes.BASIC to 5, ModGenes.SCARE_CREEPERS to 2)
        this.addEntityGenes(EntityType.CAVE_SPIDER, ModGenes.BASIC to 7, ModGenes.NIGHT_VISION to 5, ModGenes.WALL_CLIMBING to 2, ModGenes.POISON_IMMUNITY to 1)
        this.addEntityGenes(EntityType.CHICKEN, ModGenes.BASIC to 5, ModGenes.NO_FALL_DAMAGE to 1, ModGenes.LAY_EGG to 4)
        this.addEntityGenes(EntityType.COD, ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 2)
        this.addEntityGenes(EntityType.COW, ModGenes.BASIC to 5, ModGenes.EAT_GRASS to 3, ModGenes.MILKY to 4)
        this.addEntityGenes(EntityType.CREEPER, ModGenes.BASIC to 5, ModGenes.EXPLOSIVE_EXIT to 3)
        this.addEntityGenes(EntityType.DOLPHIN, ModGenes.BASIC to 7, ModGenes.SPEED to 2, ModGenes.JUMP_BOOST to 2)
        this.addEntityGenes(EntityType.DONKEY, ModGenes.BASIC to 9, ModGenes.JUMP_BOOST to 3, ModGenes.STEP_ASSIST to 3, ModGenes.KNOCKBACK to 3)
        this.addEntityGenes(EntityType.DROWNED, ModGenes.BASIC to 5, ModGenes.RESISTANCE to 4)
        this.addEntityGenes(EntityType.ELDER_GUARDIAN, ModGenes.MOB_SIGHT to 3, ModGenes.WATER_BREATHING to 4)
        this.addEntityGenes(EntityType.ENDER_DRAGON, ModGenes.DRAGON_BREATH to 6, ModGenes.ENDER_DRAGON_HEALTH to 3, ModGenes.FLIGHT to 2)
        this.addEntityGenes(EntityType.ENDERMAN, ModGenes.BASIC to 5, ModGenes.MORE_HEARTS to 1, ModGenes.TELEPORT to 3)
        this.addEntityGenes(EntityType.ENDERMITE, ModGenes.KEEP_INVENTORY to 1, ModGenes.ITEM_MAGNET to 2, ModGenes.XP_MAGNET to 3)
//        this.addEntityGenes(EntityType., ModGenes.BASIC to)
//        this.addEntityGenes(EntityType., ModGenes.BASIC to)
//        this.addEntityGenes(EntityType., ModGenes.BASIC to)
//        this.addEntityGenes(EntityType., ModGenes.BASIC to)


    }
}