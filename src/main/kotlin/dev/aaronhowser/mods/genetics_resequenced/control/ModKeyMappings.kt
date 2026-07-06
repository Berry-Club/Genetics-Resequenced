package dev.aaronhowser.mods.genetics_resequenced.control

import com.mojang.blaze3d.platform.InputConstants
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider
import net.minecraft.client.KeyMapping
import net.neoforged.neoforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

object ModKeyMappings {

	val CATEGORY: KeyMapping.Category = KeyMapping.Category(GeneticsResequenced.modId("genetics_resequenced"))

	val DRAGONS_BREATH = KeyMapping(
		ModLanguageProvider.Keys.DRAGONS_BREATH,
		KeyConflictContext.IN_GAME,
		InputConstants.Type.KEYSYM,
		GLFW.GLFW_KEY_UNKNOWN,
		CATEGORY
	)

	val TELEPORT = KeyMapping(
		ModLanguageProvider.Keys.TELEPORT,
		KeyConflictContext.IN_GAME,
		InputConstants.Type.KEYSYM,
		GLFW.GLFW_KEY_UNKNOWN,
		CATEGORY
	)

}
