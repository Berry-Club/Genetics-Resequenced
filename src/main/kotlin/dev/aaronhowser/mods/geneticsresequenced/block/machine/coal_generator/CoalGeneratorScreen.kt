package dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.GeneratorBurn
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class CoalGeneratorScreen(
    pMenu: CoalGeneratorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<CoalGeneratorMenu>(pMenu, pPlayerInventory, pTitle) {

    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.COAL_GENERATOR

    private lateinit var generatorBurn: GeneratorBurn

    override fun init() {
        super.init()

        generatorBurn = GeneratorBurn(
            x = leftPos + ScreenTextures.Elements.Burn.Position.X,
            y = topPos + ScreenTextures.Elements.Burn.Position.Y,
            shouldRender = { menu.isBurning },
            percentDone = { menu.getPercentDone() }
        )

        addRenderableWidget(generatorBurn)
    }

    override val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.CoalGen.X
    override val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.CoalGen.Y

    override val arrowLeftPos = ScreenTextures.Elements.ArrowRight.Position.CoalGen.X
    override val arrowTopPos = ScreenTextures.Elements.ArrowRight.Position.CoalGen.Y

    override fun shouldRenderProgressArrow(): Boolean = menu.isBurning


}