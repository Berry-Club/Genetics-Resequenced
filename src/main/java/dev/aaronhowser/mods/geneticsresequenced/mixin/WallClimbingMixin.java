package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced;
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.ExtensionKt;
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene;
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class WallClimbingMixin {

    @Inject(method = "onClimbable", at = @At("HEAD"), cancellable = true)
    private void checkWallClimb(CallbackInfoReturnable<Boolean> cir) {
        System.out.println("Checking Wall Climbing");
        try {
            //noinspection DataFlowIssue
            LivingEntity entity = (LivingEntity) (Object) this;

            GenesCapability genes = ExtensionKt.getGenes(entity);
            boolean hasWallClimbing = genes.hasGene(Gene.Companion.getWALL_CLIMBING());

            if (hasWallClimbing) {
                System.out.println("Wall Climbing");
                cir.setReturnValue(true);
            }

        } catch (NullPointerException | ClassCastException e) {
            GeneticsResequenced.INSTANCE.getLOGGER().error("Error in WallClimbingMixin");
            GeneticsResequenced.INSTANCE.getLOGGER().error(e);
        }

    }

}
