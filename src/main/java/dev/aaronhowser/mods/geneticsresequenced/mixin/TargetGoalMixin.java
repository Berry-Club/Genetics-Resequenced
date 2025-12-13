package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.MobGenes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(TargetGoal.class)
public class TargetGoalMixin {

	@Shadow
	@Nullable
	protected LivingEntity targetMob;

	@Inject(
			method = "canContinueToUse",
			at = @At("HEAD"),
			cancellable = true
	)
	private void geneticsresequenced$placidPreventsTargeting(CallbackInfoReturnable<Boolean> cir) {
		if (MobGenes.shouldPlacidCancelGoal((TargetGoal) (Object) this)) {
			cir.setReturnValue(false);
		}
	}

}
