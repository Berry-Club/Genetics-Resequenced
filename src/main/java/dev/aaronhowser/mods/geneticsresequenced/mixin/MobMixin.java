package dev.aaronhowser.mods.geneticsresequenced.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin {

	@ModifyReturnValue(
			method = "requiresCustomPersistence",
			at = @At("RETURN")
	)
	private boolean modifyRequiresCustomPersistence(boolean original) {
		if (original) {
			return true;
		}

		var self = (Mob) (Object) this;
		return !GenesData.getGeneHolders(self).isEmpty();
	}

}
