package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.mixin_interfaces.CringeModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin implements CringeModel {

	@Unique
	private ModelPart geneticsresequenced$rootPart;

	@Override
	public ModelPart geneticsresequenced$getRootPart() {
		return this.geneticsresequenced$rootPart;
	}

	@Inject(
			method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V",
			at = @At("RETURN")
	)
	private void geneticsresequenced$captureRootPart(ModelPart root, Function<ResourceLocation, RenderType> renderType, CallbackInfo ci) {
		this.geneticsresequenced$rootPart = root;
	}

}
