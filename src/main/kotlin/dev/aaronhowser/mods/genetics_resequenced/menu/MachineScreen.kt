package dev.aaronhowser.mods.genetics_resequenced.menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.components.EnergyBar
import dev.aaronhowser.mods.genetics_resequenced.menu.components.ProgressArrow
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
	protected open val arrowPosLeft: Int = 83
	protected open val arrowPosTop: Int = 43

	protected open fun arrowPercentDone(): Float = menu.getPercentDone()
	protected open fun clickedProgressArrow(mouseX: Double, mouseY: Double, button: Int) {}
	protected open fun shouldRenderProgressArrow(): Boolean {
		val m = menu
		return m is CraftingMachineMenu && m.isCrafting()
	}

	override fun baseInit() {
		super.baseInit()

		inventoryLabelY += 6

		this.progressArrow = ProgressArrow(
			x = leftPos + arrowPosLeft,
			y = topPos + arrowPosTop,
			arrowDirection = arrowDirection,
			font = font,
			percentDoneFunction = ::arrowPercentDone,
			shouldRenderProgress = ::shouldRenderProgressArrow,
			onClickFunction = ::clickedProgressArrow
		)

		this.energyBar = EnergyBar(
			x = leftPos + energyPosLeft,
			y = topPos + energyPosTop,
			maxGetter = { menu.getMaxEnergy() },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		addRenderableWidget(progressArrow)
		addRenderableWidget(energyBar)
	}

}