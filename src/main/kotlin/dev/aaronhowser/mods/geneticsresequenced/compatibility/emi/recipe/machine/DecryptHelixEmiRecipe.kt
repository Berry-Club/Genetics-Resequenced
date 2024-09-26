package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.EntityGeneWeightRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class DecryptHelixEmiRecipe(
    val entityType: EntityType<*>,
    val geneHolder: Holder<Gene>,
    val chance: Float
) : EmiRecipe {

    companion object {
        fun getAllRecipes(): List<DecryptHelixEmiRecipe> {
            val recipes = mutableListOf<DecryptHelixEmiRecipe>()

            for ((entityType, map) in EntityGeneWeightRecipe.getEntityGeneMap(ClientUtil.localLevel!!.recipeManager)) {
                val totalWeight = map.values.sum()

                for ((geneHolder, weight) in map) {
                    val chance = weight.toFloat() / totalWeight

                    recipes.add(DecryptHelixEmiRecipe(entityType, geneHolder, chance))
                }
            }

            return recipes.distinctBy { it.id }
        }
    }

    private val encryptedHelix: EmiIngredient
    private val decryptedHelix: EmiStack

    init {
        val helixStack = ModItems.DNA_HELIX.toStack()
        EntityDnaItem.setEntityType(helixStack, entityType)
        encryptedHelix = EmiIngredient.of(DataComponentIngredient.of(true, helixStack))

        val decryptedHelixStack = DnaHelixItem.getHelixStack(geneHolder)
        decryptedHelix = EmiStack.of(decryptedHelixStack)
    }

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.DNA_DECRYPTOR_CATEGORY
    }

    override fun getId(): ResourceLocation {
        val entityTypeRl = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
        val entityString = entityTypeRl.toString().replace(':', '/')
        val geneString = geneHolder.key!!.location().toString().replace(':', '/')

        return OtherUtil.modResource("/dna_extractor/$entityString/to/$geneString")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(encryptedHelix)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(decryptedHelix)
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {

        widgets.addText(
            Component.literal(
                String.format("%.2f%%", chance * 100)
            ),
            -8 * 5, 4, 0x3E3E3E, false
        )

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
        widgets.addSlot(encryptedHelix, 0, 0)
        widgets.addSlot(decryptedHelix, 58, 0).recipeContext(this);
    }
}