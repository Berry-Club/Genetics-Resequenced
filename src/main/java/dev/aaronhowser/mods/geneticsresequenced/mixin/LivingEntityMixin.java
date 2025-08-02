package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DamageGenes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Inject(
			method = "isInvulnerableTo",
			at = @At("HEAD"),
			cancellable = true
	)
	public void geneticsresequenced$checkImmunityGenes(
			DamageSource source,
			CallbackInfoReturnable<Boolean> cir
	) {
		if (DamageGenes.checkDamageImmunities((LivingEntity) (Object) this, source)) {
			cir.setReturnValue(true);
		}
	}
}
