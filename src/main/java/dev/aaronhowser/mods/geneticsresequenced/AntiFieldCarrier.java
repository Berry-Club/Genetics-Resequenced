package dev.aaronhowser.mods.geneticsresequenced;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

public interface AntiFieldCarrier {

	default LongOpenHashSet geneticsresequenced$getAntiFieldPositions() {
		throw new IllegalStateException();
	}

}
