package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.mixin_interfaces.CringeModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin implements CringeModel {

	@Unique
	private ModelPart geneticsresequenced$rootPart;

	@Unique
	public ModelPart getGeneticsresequenced$rootPart() {
		return this.geneticsresequenced$rootPart;
	}

	@Inject(
			method = "<init>*",
			at = @At("HEAD")
	)
	private void geneticsresequenced$captureRootPart(ModelPart root) {
		this.geneticsresequenced$rootPart = root;
	}

}
