package dev.aaronhowser.mods.genetics_resequenced;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

public interface AntiFieldCarrier {

	default LongOpenHashSet genetics_resequenced$getAntiFieldPositions() {
		throw new IllegalStateException();
	}

}
