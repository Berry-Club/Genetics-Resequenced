package dev.aaronhowser.mods.geneticsresequenced.compatibility.curios

import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.clearSavedInventory
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.getSavedInventory
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.saveInventory
import net.minecraft.world.entity.player.Player
import top.theillusivec4.curios.api.CuriosApi
import kotlin.jvm.optionals.getOrNull

object KeepCurioInventory {

    fun saveCurios(player: Player) {
        val playerCuriosInventory = CuriosApi.getCuriosInventory(player).getOrNull() ?: return

        val equippedCurios = playerCuriosInventory.equippedCurios
        val curioStacks = buildList {
            for (i in 0 until equippedCurios.slots) {
                val stack = equippedCurios.getStackInSlot(i)
                if (!stack.isEmpty) {
                    add(stack.copy())
                    equippedCurios.extractItem(i, stack.count, false)
                }
            }
        }

        val keptInventory = player.getSavedInventory()
        val newList = keptInventory + curioStacks

        player.clearSavedInventory()
        player.saveInventory(newList)
    }

}