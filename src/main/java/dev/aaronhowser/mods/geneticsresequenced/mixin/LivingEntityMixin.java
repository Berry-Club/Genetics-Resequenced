package dev.aaronhowser.mods.geneticsresequenced.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.OtherGenes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@ModifyReturnValue(
			method = "isCurrentlyGlowing",
			at = @At("RETURN")
	)
	private boolean geneticsresequenced$glowFromMobSight(boolean original) {
		if (original) {
			return true;
		}

		var self = (LivingEntity) (Object) this;
		return OtherGenes.shouldMobGlowFromMobSight(self);
	}

}
