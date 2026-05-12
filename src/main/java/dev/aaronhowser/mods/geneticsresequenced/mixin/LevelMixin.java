package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.AntiFieldCarrier;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Level.class)
public class LevelMixin implements AntiFieldCarrier {

	@Unique
	LongOpenHashSet geneticsresequenced$antiFieldPositions = new LongOpenHashSet();

	@Unique
	@Override
	public LongOpenHashSet geneticsresequenced$getAntiFieldPositions() {
		return geneticsresequenced$antiFieldPositions;
	}

}
