package dev.aaronhowser.mods.geneticsresequenced.default_genes

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry.addGenesToEntity
import net.minecraft.world.entity.EntityType

object DefaultMobGenes {

    fun registerDefaultGenes() {
        // Animals
        addGenesToEntity(
            EntityType.VILLAGER,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.emeraldHeart to 1,
                DefaultGenes.regeneration to 1
            )
        )
        addGenesToEntity(
            EntityType.SHEEP,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.eatGrass to 1,
                DefaultGenes.wooly to 1
            )
        )
        addGenesToEntity(
            EntityType.COW,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.eatGrass to 1,
                DefaultGenes.milky to 1
            )
        )
        addGenesToEntity(
            EntityType.PIG,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.meaty to 1
            )
        )
        addGenesToEntity(
            EntityType.HORSE,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.jumpBoost to 1,
                DefaultGenes.stepAssist to 1,
                DefaultGenes.speed to 1
            )
        )
        addGenesToEntity(
            EntityType.CHICKEN,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.noFallDamage to 1,
                DefaultGenes.layEgg to 1
            )
        )
        addGenesToEntity(
            EntityType.BAT,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.nightVision to 1
            )
        )
        addGenesToEntity(
            EntityType.PARROT,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.noFallDamage to 1
            )
        )
        addGenesToEntity(
            EntityType.OCELOT,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.speed to 1,
                DefaultGenes.scareCreepers to 1
            )
        )
        addGenesToEntity(
            EntityType.POLAR_BEAR,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.strength to 1,
                DefaultGenes.claws to 1,
                DefaultGenes.stepAssist to 1
            )
        )
        addGenesToEntity(
            EntityType.LLAMA,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.stepAssist to 1
            )
        )
        addGenesToEntity(
            EntityType.WOLF,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.scareSkeletons to 1,
                DefaultGenes.nightVision to 1,
                DefaultGenes.noHunger to 1
            )
        )
        addGenesToEntity(
            EntityType.RABBIT,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.jumpBoost to 1,
                DefaultGenes.speed to 1,
                DefaultGenes.luck to 1
            )
        )
        addGenesToEntity(
            EntityType.FOX,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.speed to 1,
                DefaultGenes.jumpBoost to 1,
                DefaultGenes.nightVision to 1
            )
        )
        addGenesToEntity(
            EntityType.SQUID,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.waterBreathing to 1
            )
        )
        addGenesToEntity(
            EntityType.GLOW_SQUID,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.waterBreathing to 1,
                DefaultGenes.bioluminescence to 1
            )
        )
        addGenesToEntity(
            EntityType.DOLPHIN,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.waterBreathing to 1,
                DefaultGenes.speed to 1,
                DefaultGenes.jumpBoost to 1
            )
        )
        addGenesToEntity(
            EntityType.IRON_GOLEM,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.moreHearts to 1,
                DefaultGenes.regeneration to 1,
                DefaultGenes.resistance to 1
            )
        )

        addGenesToEntity(
            EntityType.PUFFERFISH,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.poison to 1,
                DefaultGenes.waterBreathing to 1,
                DefaultGenes.thorns to 1
            )
        )

        //Overworld Hostiles
        addGenesToEntity(
            EntityType.ZOMBIE,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.resistance to 1
            )
        )
        addGenesToEntity(
            EntityType.CREEPER,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.explosiveExit to 1
            )
        )
        addGenesToEntity(
            EntityType.SPIDER,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.nightVision to 1,
                DefaultGenes.wallClimbing to 1
            )
        )
        addGenesToEntity(
            EntityType.CAVE_SPIDER,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.nightVision to 1,
                DefaultGenes.wallClimbing to 1,
                DefaultGenes.poisonImmunity to 1
            )
        )
        addGenesToEntity(
            EntityType.SLIME,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.noFallDamage to 1,
                DefaultGenes.slimyDeath to 1
            )
        )
        addGenesToEntity(
            EntityType.GUARDIAN,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.mobSight to 1,
                DefaultGenes.waterBreathing to 1
            )
        )
        addGenesToEntity(
            EntityType.ELDER_GUARDIAN,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.mobSight to 1,
                DefaultGenes.waterBreathing to 1
            )
        )
        addGenesToEntity(
            EntityType.SKELETON,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.infinity to 1
            )
        )
        addGenesToEntity(
            EntityType.SILVERFISH,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.haste to 1,
                DefaultGenes.efficiency to 1
            )
        )
        addGenesToEntity(
            EntityType.PHANTOM,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.invisible to 1,
            )
        )

        //End hostiles
        addGenesToEntity(
            EntityType.ENDERMITE,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.keepInventory to 1,
                DefaultGenes.itemMagnet to 1,
                DefaultGenes.xpMagnet to 1
            )
        )
        addGenesToEntity(
            EntityType.ENDERMAN,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.teleport to 1,
                DefaultGenes.moreHearts to 1
            )
        )
        addGenesToEntity(
            EntityType.SHULKER,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.resistance to 1,
                DefaultGenes.regeneration to 1
            )
        )

        //Nether hostiles
        addGenesToEntity(
            EntityType.WITHER_SKELETON,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.witherHit to 1
            )
        )
        addGenesToEntity(
            EntityType.BLAZE,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.shootFireballs to 1,
                DefaultGenes.fireProof to 1,
                DefaultGenes.bioluminescence to 1
            )
        )
        addGenesToEntity(
            EntityType.GHAST,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.shootFireballs to 1
            )
        )
        addGenesToEntity(
            EntityType.ZOMBIFIED_PIGLIN,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.fireProof to 1,
                DefaultGenes.meaty to 1
            )
        )
        addGenesToEntity(
            EntityType.HOGLIN,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.meaty to 1
            )
        )
        addGenesToEntity(
            EntityType.PIGLIN,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.meaty to 1
            )
        )
        addGenesToEntity(
            EntityType.MAGMA_CUBE,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.fireProof to 1,
                DefaultGenes.bioluminescence to 1
            )
        )

        //Bosses
        addGenesToEntity(
            EntityType.WITHER, mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.witherProof to 1
            )
        )
        addGenesToEntity(
            EntityType.ENDER_DRAGON,
            mapOf(
                DefaultGenes.basic to 5,
                DefaultGenes.dragonsBreath to 1,
                DefaultGenes.enderDragonHealth to 1,
                DefaultGenes.flight to 1
            )
        )
    }


}