package dev.aaronhowser.mods.genetics_resequenced.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.MobGenes;
import net.minecraft.world.entity.animal.bee.Bee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.BeePollinateGoal.class)
public abstract class BeePollinateGoalMixin {

	@Shadow
	@Final
	Bee this$0;

	@Shadow
	private int successfulPollinatingTicks;

	@ModifyReturnValue(
			method = "hasPollinatedLongEnough",
			at = @At("RETURN")
	)
	private boolean modifyPollinationTime(boolean original) {
		if (original) return true;
		var bee = this.this$0;
		return successfulPollinatingTicks >= MobGenes.beeRequiredPollinationTime(bee);
	}

}
