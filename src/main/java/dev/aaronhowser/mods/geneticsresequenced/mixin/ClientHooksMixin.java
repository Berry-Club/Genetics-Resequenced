package dev.aaronhowser.mods.geneticsresequenced.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientHooks.class)
public abstract class ClientHooksMixin {

	@ModifyReturnValue(
			method = "shouldRenderEffect",
			at = @At("RETURN")
	)
	private static boolean geneticsresequenced$hideGenePotions(boolean original, MobEffectInstance effectInstance) {
		if (original) {
			return !ClientUtil.shouldHidePotionInInventory(effectInstance);
		} else {
			return false;
		}
	}

}
