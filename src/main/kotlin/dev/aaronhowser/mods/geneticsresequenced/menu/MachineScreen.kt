package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.components.EnergyBar
import dev.aaronhowser.mods.geneticsresequenced.menu.components.ProgressArrow
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

abstract class MachineScreen<T : MachineMenu>(
	menu: T,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<T>(menu, playerInventory, title) {

	protected lateinit var energyBar: EnergyBar
	protected open val energyPosLeft: Int = 7
	protected open val energyPosTop: Int = 14

	protected lateinit var progressArrow: ProgressArrow
	protected open val arrowDirection: ProgressArrow.ArrowDirection = ProgressArrow.ArrowDirection.RIGHT
	protected open val arrowLeftPos: Int = 83
	protected open val arrowTopPos: Int = 43

	protected open fun arrowPercentDone(): Float = menu.getPercentDone()
	protected open fun shouldRenderProgressArrow(): Boolean = true

	override fun baseInit() {
		super.baseInit()
	}

}