package dev.aaronhowser.mods.genetics_resequenced.mixin;

import dev.aaronhowser.mods.genetics_resequenced.AntiFieldCarrier;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Level.class)
public class LevelMixin implements AntiFieldCarrier {

	@Unique
	LongOpenHashSet genetics_resequenced$antiFieldPositions = new LongOpenHashSet();

	@Unique
	@Override
	public LongOpenHashSet genetics_resequenced$getAntiFieldPositions() {
		return genetics_resequenced$antiFieldPositions;
	}

}
