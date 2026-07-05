package dev.aaronhowser.mods.genetics_resequenced.mixin;

import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.MobGenes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetGoal.class)
public abstract class TargetGoalMixin {

	@Inject(
			method = "canContinueToUse",
			at = @At("HEAD"),
			cancellable = true
	)
	private void genetics_resequenced$placidPreventsTargeting(CallbackInfoReturnable<Boolean> cir) {
		if (MobGenes.shouldPlacidCancelTargetGoal((TargetGoal) (Object) this)) {
			cir.setReturnValue(false);
		}
	}

}
