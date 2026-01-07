package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.MobGenes;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Bee.BeePollinateGoal.class)
public abstract class BeePollinateGoalMixin {

	@Shadow
	@Final
	Bee this$0;

	@Shadow
	private int successfulPollinatingTicks;

	@Inject(
			method = "hasPollinatedLongEnough",
			at = @At("RETURN"),
			cancellable = true
	)
	private void geneticsresequenced$modifyPollinationTime(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			var bee = this.this$0;
			var success = successfulPollinatingTicks >= MobGenes.beeRequiredPollinationTime(bee);
			cir.setReturnValue(success);
		}
	}

}
