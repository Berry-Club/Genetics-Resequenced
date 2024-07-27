package dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class PlasmidInjectorScreen(
    pMenu: PlasmidInjectorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<PlasmidInjectorMenu>(pMenu, pPlayerInventory, pTitle) {
    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.PLASMID_INJECTOR

    override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting
    override fun progressArrowWidth(): Int = menu.getScaledProgress()
    override fun progressArrowHeight(): Int = ScreenTextures.Elements.ArrowRight.Position.Default.Y
}