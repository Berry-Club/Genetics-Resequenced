package dev.aaronhowser.mods.genetics_resequenced.command

import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.permissions.Permission
import net.minecraft.server.permissions.PermissionLevel

fun CommandSourceStack.hasGamemasterPermission(): Boolean {
	return permissions().hasPermission(Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS))
}
