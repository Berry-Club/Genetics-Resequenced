package dev.aaronhowser.mods.geneticsresequenced.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.OtherGenes;
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
	private boolean geneticsresequenced$glowFromMobSight(boolean original) {
		if (original) {
			return true;
		}

		var self = (LivingEntity) (Object) this;
		if (self.level().isClientSide) {
			return ClientUtil.shouldMobGlow(self);
		} else {
			return original;
		}
	}

	@Override
	public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
		var self = (LivingEntity) (Object) this;
		if (OtherGenes.shouldNegateSlownessFromBlock(self, state)) {
			return;
		}
		super.makeStuckInBlock(state, motionMultiplier);
	}

}
