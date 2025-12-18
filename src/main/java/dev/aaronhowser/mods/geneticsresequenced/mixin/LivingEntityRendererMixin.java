package dev.aaronhowser.mods.geneticsresequenced.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.aaronhowser.mods.geneticsresequenced.mixin_interfaces.CringeModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.joml.SimplexNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

	@Inject(
			method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getInstance()Lnet/minecraft/client/Minecraft;")
	)
	private void geneticsresequenced$cringeShake(
			T entity,
			float entityYaw,
			float partialTicks,
			PoseStack poseStack,
			MultiBufferSource buffer,
			int packedLight,
			CallbackInfo ci
	) {
		@SuppressWarnings("unchecked")
		var self = (LivingEntityRenderer<T, M>) (Object) this;

		if (self.getModel() instanceof CringeModel cm) {
			var time = entity.tickCount + partialTicks;

			var root = cm.geneticsresequenced$getRootPart();
			var children = root.getAllParts();

			children.forEach(part -> {
				var dx = SimplexNoise.noise(0, time);
				var dy = SimplexNoise.noise(10, time);
				var dz = SimplexNoise.noise(20, time);

				part.x += dx;
				part.y += dy;
				part.z += dz;
			});
		}

	}

}
