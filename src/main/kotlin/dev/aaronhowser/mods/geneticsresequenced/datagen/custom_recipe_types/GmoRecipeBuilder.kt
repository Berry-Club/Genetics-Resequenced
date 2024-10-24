package dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

class GmoRecipeBuilder(
    private val entityType: EntityType<*>,
    private val ingredient: Ingredient,
    private val idealGeneRk: ResourceKey<Gene>,
    private val geneChance: Float,
    private val needsMutationPotion: Boolean = false
) : RecipeBuilder {

    constructor(
        entityType: EntityType<*>,
        ingredientItem: Item,
        idealGeneRk: ResourceKey<Gene>,
        geneChance: Float,
        isMutation: Boolean = false
    ) : this(
        entityType,
        Ingredient.of(ingredientItem),
        idealGeneRk,
        geneChance,
        isMutation
    )

    private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
        criteria[name] = criterion
        return this
    }

    override fun group(p0: String?): RecipeBuilder {
        error("Unsupported")
    }

    override fun getResult(): Item {
        return ModItems.GMO_CELL.get()
    }

    override fun save(output: RecipeOutput, defaultId: ResourceLocation) {
        val entityString = EntityType.getKey(entityType).path
        val geneString = idealGeneRk.location().path

        val chanceString = if (geneChance == 1f) {
            "100"
        } else {
            (geneChance * 100).toInt().toString()
        }

        val pathBuilder = StringBuilder()
            .append("incubator/")
            .append("gmo/")

        if (needsMutationPotion) {
            pathBuilder.append("mutation/")
        }

        pathBuilder
            .append(geneString)
            .append("_from_")
            .append(entityString)
            .append("_with_")
            .append(chanceString)
            .append("_chance")

        val id = ResourceLocation.fromNamespaceAndPath(
            defaultId.namespace,
            pathBuilder.toString()
        )

        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)

        criteria.forEach { (name, criterion) -> advancement.addCriterion(name, criterion) }

        val recipe = GmoRecipe(
            entityType,
            ingredient,
            idealGeneRk,
            geneChance,
            needsMutationPotion
        )

        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
    }
}