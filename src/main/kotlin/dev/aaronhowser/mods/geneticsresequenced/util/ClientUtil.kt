package dev.aaronhowser.mods.geneticsresequenced.util

import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.PlayerModelPart

object ClientUtil {

    val localPlayer: LocalPlayer?
        get() = Minecraft.getInstance().player

    fun playerIsCreative(): Boolean = localPlayer?.isCreative == true

    private val options: Options = Minecraft.getInstance().options

    private var removedSkinLayers: Set<PlayerModelPart> = emptySet()

}