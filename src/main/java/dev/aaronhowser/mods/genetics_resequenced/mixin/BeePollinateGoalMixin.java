package dev.aaronhowser.mods.genetics_resequenced.mixin;

import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;

//FIXME: this$0 doesn't get refmapped
@Mixin(Bee.BeePollinateGoal.class)
public abstract class BeePollinateGoalMixin {

//	@Shadow
//	@Final
//	Bee this$0;
//
//	@Shadow
//	private int successfulPollinatingTicks;
//
//	@Inject(
//			method = "hasPollinatedLongEnough",
//			at = @At("RETURN"),
//			cancellable = true
//	)
//	private void genetics_resequenced$modifyPollinationTime(CallbackInfoReturnable<Boolean> cir) {
//		if (!cir.getReturnValue()) {
//			var bee = this.this$0;
//			var success = successfulPollinatingTicks >= MobGenes.beeRequiredPollinationTime(bee);
//			cir.setReturnValue(success);
//		}
//	}

}