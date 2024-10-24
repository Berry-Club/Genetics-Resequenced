package dev.aaronhowser.mods.geneticsresequenced.datagen.gene

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
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
        this.addEntityGenes(EntityType.EVOKER, ModGenes.BASIC to 4, ModGenes.EMERALD_HEART to 3, ModGenes.BAD_OMEN to 4)
        this.addEntityGenes(EntityType.FOX, ModGenes.BASIC to 5, ModGenes.SPEED to 2, ModGenes.JUMP_BOOST to 2)
        this.addEntityGenes(EntityType.FROG, ModGenes.BASIC to 5, ModGenes.JUMP_BOOST to 3)
        this.addEntityGenes(EntityType.GHAST, ModGenes.BASIC to 5, ModGenes.SHOOT_FIREBALLS to 4)
        this.addEntityGenes(EntityType.GLOW_SQUID, ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 4, ModGenes.BIOLUMINESCENCE to 4)
        this.addEntityGenes(EntityType.GOAT, ModGenes.BASIC to 5, ModGenes.EAT_GRASS to 3, ModGenes.WOOLY to 3, ModGenes.KNOCKBACK to 4)
        this.addEntityGenes(EntityType.GUARDIAN, ModGenes.BASIC to 5, ModGenes.MOB_SIGHT to 3, ModGenes.WATER_BREATHING to 4)
        this.addEntityGenes(EntityType.HOGLIN, ModGenes.BASIC to 5, ModGenes.MEATY to 5)
        this.addEntityGenes(EntityType.HORSE, ModGenes.BASIC to 7, ModGenes.JUMP_BOOST to 3, ModGenes.STEP_ASSIST to 3, ModGenes.SPEED to 3)
        this.addEntityGenes(EntityType.HUSK, ModGenes.BASIC to 5, ModGenes.CHILLING to 4)
        this.addEntityGenes(EntityType.IRON_GOLEM, ModGenes.BASIC to 5, ModGenes.MORE_HEARTS to 2, ModGenes.REGENERATION to 2, ModGenes.RESISTANCE to 4, ModGenes.STRENGTH to 3, ModGenes.REACHING to 3)
        this.addEntityGenes(EntityType.LLAMA, ModGenes.BASIC to 5, ModGenes.STEP_ASSIST to 3)
        this.addEntityGenes(EntityType.MAGMA_CUBE, ModGenes.BASIC to 5, ModGenes.FIRE_PROOF to 3, ModGenes.BIOLUMINESCENCE to 4)
        this.addEntityGenes(EntityType.MOOSHROOM, ModGenes.EAT_GRASS to 4, ModGenes.MILKY to 4, ModGenes.PHOTOSYNTHESIS to 3)
        this.addEntityGenes(EntityType.OCELOT, ModGenes.BASIC to 5, ModGenes.SPEED to 2, ModGenes.SCARE_CREEPERS to 3)
        this.addEntityGenes(EntityType.PARROT, ModGenes.BASIC to 5, ModGenes.NO_FALL_DAMAGE to 2, ModGenes.LAY_EGG to 4, ModGenes.CHATTERBOX to 6)
        this.addEntityGenes(EntityType.PHANTOM, ModGenes.BASIC to 5, ModGenes.INVISIBLE to 3, ModGenes.TELEPORT to 3)
        this.addEntityGenes(EntityType.PIG, ModGenes.BASIC to 5, ModGenes.MEATY to 2)
        this.addEntityGenes(EntityType.PIGLIN_BRUTE, ModGenes.BASIC to 5, ModGenes.MEATY to 4)
        this.addEntityGenes(EntityType.PIGLIN, ModGenes.BASIC to 5, ModGenes.MEATY to 3)
        this.addEntityGenes(EntityType.PILLAGER, ModGenes.BASIC to 5, ModGenes.EMERALD_HEART to 2, ModGenes.BAD_OMEN to 3)
        this.addEntityGenes(EntityType.PLAYER, ModGenes.CRINGE to 1)
        this.addEntityGenes(EntityType.POLAR_BEAR, ModGenes.BASIC to 5, ModGenes.STRENGTH to 4, ModGenes.CLAWS to 3, ModGenes.STEP_ASSIST to 4)
        this.addEntityGenes(EntityType.PUFFERFISH, ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 4, ModGenes.POISON to 3, ModGenes.THORNS to 4)
        this.addEntityGenes(EntityType.RABBIT, ModGenes.BASIC to 5, ModGenes.JUMP_BOOST to 5, ModGenes.SPEED to 3, ModGenes.LUCK to 4)
        this.addEntityGenes(EntityType.RAVAGER, ModGenes.BASIC to 5, ModGenes.STRENGTH to 5, ModGenes.RESISTANCE to 4, ModGenes.MORE_HEARTS to 3)
        this.addEntityGenes(EntityType.SALMON, ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 3)
        this.addEntityGenes(EntityType.SHEEP, ModGenes.BASIC to 5, ModGenes.EAT_GRASS to 3, ModGenes.WOOLY to 4)
        this.addEntityGenes(EntityType.SHULKER, ModGenes.BASIC to 3, ModGenes.RESISTANCE to 4, ModGenes.LEVITATION to 4)
        this.addEntityGenes(EntityType.SILVERFISH, ModGenes.BASIC to 3, ModGenes.HASTE to 3, ModGenes.EFFICIENCY to 1, ModGenes.INFESTED to 4)
        this.addEntityGenes(EntityType.SKELETON_HORSE, ModGenes.JUMP_BOOST to 4, ModGenes.STEP_ASSIST to 4, ModGenes.SPEED to 4)
        this.addEntityGenes(EntityType.SKELETON, ModGenes.BASIC to 5, ModGenes.INFINITY to 1)
        this.addEntityGenes(EntityType.SLIME, ModGenes.BASIC to 5, ModGenes.NO_FALL_DAMAGE to 4, ModGenes.SLIMY_DEATH to 2, ModGenes.OOZING to 5)
        this.addEntityGenes(EntityType.SNOW_GOLEM, ModGenes.BASIC to 5, ModGenes.CHILLING to 2)
        this.addEntityGenes(EntityType.SPIDER, ModGenes.BASIC to 4, ModGenes.NIGHT_VISION to 3, ModGenes.WALL_CLIMBING to 2, ModGenes.WEAVING to 4)
        this.addEntityGenes(EntityType.SQUID, ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 2)
        this.addEntityGenes(EntityType.STRAY, ModGenes.BASIC to 5, ModGenes.INFINITY to 3)
        this.addEntityGenes(EntityType.STRIDER, ModGenes.BASIC to 5, ModGenes.FIRE_PROOF to 4)
        this.addEntityGenes(ModEntityTypes.SUPPORT_SLIME.get(), ModGenes.SLIMY_DEATH to 1)
        this.addEntityGenes(EntityType.TADPOLE, ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 4)
        this.addEntityGenes(EntityType.TROPICAL_FISH, ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 2)
        this.addEntityGenes(EntityType.TURTLE, ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 2)
        this.addEntityGenes(EntityType.VILLAGER, ModGenes.BASIC to 5, ModGenes.EMERALD_HEART to 2)
        this.addEntityGenes(EntityType.VINDICATOR, ModGenes.BASIC to 5, ModGenes.EMERALD_HEART to 2, ModGenes.BAD_OMEN to 3, ModGenes.JOHNNY to 3)
        this.addEntityGenes(EntityType.WARDEN, ModGenes.MORE_HEARTS to 5, ModGenes.MOB_SIGHT to 5)
        this.addEntityGenes(EntityType.WITCH, ModGenes.BASIC to 5, ModGenes.POISON_IMMUNITY to 2)
        this.addEntityGenes(EntityType.WITHER_SKELETON, ModGenes.BASIC to 5, ModGenes.WITHER_HIT to 2)
        this.addEntityGenes(EntityType.WITHER, ModGenes.BASIC to 5, ModGenes.WITHER_PROOF to 3, ModGenes.WITHER_HIT to 4, ModGenes.FLIGHT to 3)
        this.addEntityGenes(EntityType.WOLF, ModGenes.BASIC to 5, ModGenes.SCARE_SKELETONS to 2, ModGenes.NIGHT_VISION to 3, ModGenes.NO_HUNGER to 1)
        this.addEntityGenes(EntityType.ZOGLIN, ModGenes.BASIC to 5, ModGenes.MEATY to 4)
        this.addEntityGenes(EntityType.ZOMBIE_HORSE, ModGenes.JUMP_BOOST to 4, ModGenes.STEP_ASSIST to 4, ModGenes.SPEED to 4)
        this.addEntityGenes(EntityType.ZOMBIE_VILLAGER, ModGenes.BASIC to 5, ModGenes.EMERALD_HEART to 4)
        this.addEntityGenes(EntityType.ZOMBIE, ModGenes.BASIC to 7, ModGenes.RESISTANCE to 1)
        this.addEntityGenes(EntityType.ZOMBIFIED_PIGLIN, ModGenes.BASIC to 5, ModGenes.FIRE_PROOF to 3, ModGenes.MEATY to 3)
    }
}