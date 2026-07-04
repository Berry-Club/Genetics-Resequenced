package dev.aaronhowser.mods.geneticsresequenced.control

import com.mojang.blaze3d.platform.InputConstants
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import net.minecraft.client.KeyMapping
import net.neoforged.neoforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

object ModKeyMappings {

	private val CATEGORY = KeyMapping.Category.register(GeneticsResequenced.modResource("genetics_resequenced"))

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
