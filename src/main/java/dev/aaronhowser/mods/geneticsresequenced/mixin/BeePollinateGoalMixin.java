package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.MobGenes;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Bee.BeePollinateGoal.class)
public abstract class BeePollinateGoalMixin {

	@Shadow
	@Final
	Bee this$0;

	@ModifyConstant(
			method = "hasPollinatedLongEnough",
			constant = @Constant(intValue = 400)
	)
	private int modifyPollinationTime(int original) {
		var bee = this.this$0;
		return MobGenes.modifyBeePollinationTime(bee);
	}

}
