package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.OtherGenes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(
			method = "makeStuckInBlock",
			at = @At("HEAD"),
			cancellable = true
	)
	private void geneticsresequenced$makeStuckInBlock(BlockState state, Vec3 motionMultiplier, CallbackInfo ci) {
		if (OtherGenes.shouldNegateSlownessFromBlock((Entity) (Object) this, state)) {
			ci.cancel();
		}
	}

}
