package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.blood_purifier

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class PurifySyringeEmiRecipe(
    val isMetal: Boolean = false
) : EmiRecipe {

    private val contaminatedSyringe: EmiIngredient
    private val decontaminatedSyringe: EmiStack

    init {
        val syringeStack = if (isMetal) {
            ModItems.METAL_SYRINGE.toStack()
        } else {
            ModItems.SYRINGE.toStack()
        }

        val localPlayer = ClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")

        val entity = if (isMetal) {
            Cow(EntityType.COW, localPlayer.level())
        } else {
            localPlayer
        }

        SyringeItem.setEntity(syringeStack, entity, setContaminated = true)
        contaminatedSyringe = EmiIngredient.of(DataComponentIngredient.of(true, syringeStack))

        SyringeItem.setContaminated(syringeStack, false)
        decontaminatedSyringe = EmiStack.of(syringeStack)
    }

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.BLOOD_PURIFIER_CATEGORY
    }

    override fun getId(): ResourceLocation {
        val type = if (isMetal) "metal" else "glass"
        return OtherUtil.modResource("/purify_syringe/$type")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(contaminatedSyringe)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(decontaminatedSyringe)
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
        widgets.addSlot(contaminatedSyringe, 0, 0);
        widgets.addSlot(decontaminatedSyringe, 58, 0).recipeContext(this);
    }
}