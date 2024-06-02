package dev.aaronhowser.mods.geneticsresequenced.compatibility.curios

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.IItemHandlerModifiable
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.type.util.ICuriosHelper
import java.util.*

object CuriosKeepInventory {

    private val curiosHelper: ICuriosHelper = CuriosApi.getCuriosHelper()

    private val entityCurioMap: MutableMap<UUID, Map<Int, ItemStack>> = mutableMapOf()

    fun savePlayerCurios(player: Player) {
        val playerCurios: IItemHandlerModifiable = curiosHelper.getEquippedCurios(player).resolve().get()

        val curiosMap = mutableMapOf<Int, ItemStack>()
        for (i in 0 until playerCurios.slots) {
            val stack = playerCurios.getStackInSlot(i)

            if (!stack.isEmpty) {
                curiosMap[i] = stack.copy()
                playerCurios.setStackInSlot(i, ItemStack.EMPTY)
            }
        }

        entityCurioMap[player.uuid] = curiosMap
    }

    fun loadPlayerCurios(player: Player) {
        val curiosMap = entityCurioMap[player.uuid] ?: return

        val playerCurios: IItemHandlerModifiable = curiosHelper.getEquippedCurios(player).resolve().get()

        for ((slot, stack) in curiosMap) {
            playerCurios.setStackInSlot(slot, stack)
        }

        entityCurioMap.remove(player.uuid)
    }

}