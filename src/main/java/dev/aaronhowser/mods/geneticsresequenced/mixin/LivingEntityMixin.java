package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(
			method = "isCurrentlyGlowing",
			at = @At("RETURN"),
			cancellable = true
	)
	private void geneticsresequenced$glowFromMobSight(CallbackInfoReturnable<Boolean> cir) {

		if (!cir.getReturnValue()) {
			var self = (LivingEntity) (Object) this;
			if (self.level().isClientSide) {
				cir.setReturnValue(ClientUtil.shouldMobGlow(self));
			}
		}
	}

}
