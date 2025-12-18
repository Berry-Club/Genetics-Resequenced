package dev.aaronhowser.mods.geneticsresequenced.mixin_interfaces;

import net.minecraft.client.model.geom.ModelPart;

public interface CringeModel {

	default ModelPart geneticsresequenced$getRootPart() {
		throw new IllegalStateException();
	}

}
