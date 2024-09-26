package dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.recipe.EntityGeneWeightRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.Slots
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import java.util.*

class EntityGeneWeightRecipeBuilder(
    val entityType: EntityType<*>,
    val geneHolder: Holder<Gene>,
    val weight: Int
) : RecipeBuilder {

    override fun unlockedBy(p0: String, p1: Criterion<*>): RecipeBuilder {
        error("Unsupported")
    }

    override fun group(p0: String?): RecipeBuilder {
        error("Unsupported")
    }

    override fun getResult(): Item {
        return Items.AIR
    }

    override fun save(output: RecipeOutput, defaultId: ResourceLocation) {

        val stringBuilder = StringBuilder()
            .append("entity_gene_weight/")
            .append(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString().replace(":", "."))
            .append("/")
            .append(geneHolder.key!!.location().toString().replace(":", "."))

        val id = OtherUtil.modResource(stringBuilder.toString())

        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)

        val hasScraperCriterion = CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
            InventoryChangeTrigger.TriggerInstance(
                Optional.empty(),
                Slots.ANY,
                listOf(ItemPredicate.Builder.item().of(ModItems.SCRAPER).build())
            )
        )

        advancement.addCriterion("has_scraper", hasScraperCriterion)

        val recipe = EntityGeneWeightRecipe(entityType, geneHolder, weight)

        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
    }
}