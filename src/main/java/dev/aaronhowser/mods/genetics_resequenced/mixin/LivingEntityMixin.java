package dev.aaronhowser.mods.genetics_resequenced.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyReturnValue(
			method = "isCurrentlyGlowing",
			at = @At("RETURN")
	)
	private boolean genetics_resequenced$glowFromMobSight(boolean original) {
		if (original) {
			return true;
		}

		var self = (LivingEntity) (Object) this;
		if (self.level().isClientSide()) {
			return ClientUtil.shouldMobGlow(self);
		} else {
			return original;
		}
	}

}
