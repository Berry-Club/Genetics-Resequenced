package dev.aaronhowser.mods.geneticsresequenced.entities.client

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.world.entity.Entity

class SupportSlimeModel<T : Entity>(
    private val root: ModelPart
) : HierarchicalModel<T>() {

    fun createOuterBodyLayer(): LayerDefinition {
        val meshDef = MeshDefinition()
        val partDefinition = meshDef.root
        partDefinition.addOrReplaceChild(
            "cube",
            CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, 16.0f, -4.0f, 8.0f, 8.0f, 8.0f),
            PartPose.ZERO
        )

        return LayerDefinition.create(meshDef, 64, 32)
    }

    override fun setupAnim(p0: T, p1: Float, p2: Float, p3: Float, p4: Float, p5: Float) {}

    override fun root(): ModelPart = root

}