package dev.aaronhowser.mods.genetics_resequenced.mixin;

import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.OtherGenes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Inject(
			method = "makeStuckInBlock",
			at = @At("HEAD"),
			cancellable = true
	)
	private void genetics_resequenced$makeStuckInBlock(
			BlockState blockState,
			Vec3 speedMultiplier,
			CallbackInfo ci
	) {
		if (OtherGenes.shouldNegateSlownessFromBlock((Entity) (Object) this, blockState)) {
			ci.cancel();
		}
	}

}
