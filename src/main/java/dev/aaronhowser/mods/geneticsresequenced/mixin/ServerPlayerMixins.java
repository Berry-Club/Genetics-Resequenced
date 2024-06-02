package dev.aaronhowser.mods.geneticsresequenced.mixin;

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability;
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ServerPlayer.class)
class ServerPlayerMixins {

    @Inject(method = "restoreFrom", at = @At("HEAD"))
    public void handleKeepInventoryGene(ServerPlayer pThat, boolean pKeepEverything, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        @Nullable GenesCapability playerGenes = GenesCapability.Companion.getGenes(player);
        if (playerGenes == null) return;
        if (!playerGenes.hasGene(DefaultGenes.INSTANCE.getKEEP_INVENTORY())) return;

    }

}