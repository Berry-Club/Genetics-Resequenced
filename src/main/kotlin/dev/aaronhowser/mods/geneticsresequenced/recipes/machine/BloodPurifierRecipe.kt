package dev.aaronhowser.mods.geneticsresequenced.recipes.machine

import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.recipes.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class BloodPurifierRecipe : CustomRecipe(
    OtherUtil.modResource(RECIPE_TYPE_NAME),
) {

    override fun matches(container: CraftingContainer, p1: Level): Boolean {
        val containerOutput = container.getItem(CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX)
        if (!containerOutput.isEmpty) return false

        val containerInput = container.getItem(CraftingMachineBlockEntity.INPUT_SLOT_INDEX)
        return SyringeItem.isContaminated(containerInput)
    }

    override fun assemble(container: CraftingContainer): ItemStack {
        val syringeStack = container.getItem(CraftingMachineBlockEntity.INPUT_SLOT_INDEX)
        SyringeItem.setContaminated(syringeStack, false)

        return syringeStack
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = false

    override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.BLOOD_PURIFIER_RECIPE_SERIALIZER.get()

    companion object {
        const val RECIPE_TYPE_NAME = "blood_purifier"

        val recipeType = object : RecipeType<BloodPurifierRecipe> {}
    }

}