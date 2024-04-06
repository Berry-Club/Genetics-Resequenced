package dev.aaronhowser.mods.geneticsresequenced.controls

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraftforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

object ModKeyMappings {

    private const val CATEGORY = "key.geneticsresequenced.category"

    val DRAGONS_BREATH = KeyMapping(
        "key.geneticsresequenced.dragons_breath",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        CATEGORY
    )

    val TELEPORT = KeyMapping(
        "key.geneticsresequenced.teleport",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        CATEGORY
    )

}