package dev.aaronhowser.mods.genetics_resequenced.mixin;

import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooksClient.class)
public abstract class ForgeHooksClientMixin {

	@Inject(
			method = "shouldRenderEffect",
			remap = false,
			at = @At("RETURN"),
			cancellable = true
	)
	private static void genetics_resequenced$hideGenePotions(MobEffectInstance effectInstance, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			cir.setReturnValue(!ClientUtil.shouldHidePotionInInventory(effectInstance));
		}
	}

}