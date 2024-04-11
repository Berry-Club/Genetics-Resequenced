package dev.aaronhowser.mods.geneticsresequenced.util

import net.minecraft.client.Minecraft

object ClientHelper {

    fun playerIsCreative(): Boolean = Minecraft.getInstance().player?.isCreative == true
    fun playerIsSpectator(): Boolean = Minecraft.getInstance().player?.isSpectator == true

}