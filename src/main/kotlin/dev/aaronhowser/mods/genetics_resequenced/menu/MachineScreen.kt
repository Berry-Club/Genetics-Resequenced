package dev.aaronhowser.mods.genetics_resequenced.menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.menu.components.EnergyBar
import dev.aaronhowser.mods.genetics_resequenced.menu.components.ProgressArrow
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

open class MachineScreen<T : MachineMenu>(
	menu: T,
	playerInventory: Inventory,
	title: Component,
	background: ScreenBackground
) : BaseScreen<T>(menu, playerInventory, title, background) {

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

	companion object {
		private fun background(path: String): ScreenBackground =
			ScreenBackground(GeneticsResequenced.modId("textures/gui/container/$path.png"), 176, 172)

		val BASIC_BACKGROUND = background("basic_machine_bg")
		val CELL_ANALYZER_BACKGROUND = background("cell_analyzer")
		val COAL_GENERATOR_BACKGROUND = background("coal_generator")
		val DNA_DECRYPTOR_BACKGROUND = background("dna_decryptor")
		val DNA_EXTRACTOR_BACKGROUND = background("dna_extractor")
		val INCUBATOR_BACKGROUND = background("incubator")
		val ADVANCED_INCUBATOR_BACKGROUND = background("incubator_advanced")
		val PLASMID_INFUSER_BACKGROUND = background("plasmid_infuser")
		val PLASMID_INJECTOR_BACKGROUND = background("plasmid_injector")
	}

}
