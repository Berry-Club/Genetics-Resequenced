package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced;
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.ExtensionKt;
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene;
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ForgeHooks.class)
public class WallClimbingMixin {

    @Inject(
            method = "isLivingOnLadder",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void checkWallClimb(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull LivingEntity entity,
            CallbackInfoReturnable<Optional<BlockPos>> cir
    ) {
        try {
            if (!(entity instanceof Player)) return;

            GenesCapability genes = ExtensionKt.getGenes(entity);
            assert genes != null;
            boolean hasWallClimbing = genes.hasGene(Gene.Companion.getWALL_CLIMBING());

            if (hasWallClimbing) {
                cir.setReturnValue(Optional.of(pos));
            }

        } catch (NullPointerException | ClassCastException | AssertionError e) {
            GeneticsResequenced.INSTANCE.getLOGGER().error("Error in WallClimbingMixin");
            GeneticsResequenced.INSTANCE.getLOGGER().error(e);
        }

    }

}
