package hiiragi283.ragium.common.data

import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.recipe.WeightedIngredient
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementRequirements
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTRecipeJsonBuilder(private val type: HTMachineType) {

    private val inputs: MutableList<WeightedIngredient> = mutableListOf()
    private val outputs: MutableList<HTRecipeResult> = mutableListOf()
    private val criteria: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf()

    fun addInput(ingredient: WeightedIngredient): HTRecipeJsonBuilder = apply {
        inputs.add(ingredient)
    }

    fun addInput(ingredient: Ingredient, count: Int = 1): HTRecipeJsonBuilder =
        addInput(WeightedIngredient.of(ingredient, count))

    fun addInput(item: ItemConvertible, count: Int = 1): HTRecipeJsonBuilder =
        addInput(Ingredient.ofItems(item), count)

    fun addInput(tagKey: TagKey<Item>, count: Int = 1): HTRecipeJsonBuilder =
        addInput(Ingredient.fromTag(tagKey), count)

    fun addOutput(result: HTRecipeResult): HTRecipeJsonBuilder = apply {
        outputs.add(result)
    }

    fun addOutput(item: ItemConvertible, count: Int = 1): HTRecipeJsonBuilder =
        addOutput(HTRecipeResult.item(item, count))

    fun addOutput(tagKey: TagKey<Item>, count: Int = 1): HTRecipeJsonBuilder =
        addOutput(HTRecipeResult.tag(tagKey, count))

    fun criterion(name: String, criterion: AdvancementCriterion<*>): HTRecipeJsonBuilder = apply {
        criteria[name] = criterion
    }

    fun hasInput(item: ItemConvertible): HTRecipeJsonBuilder =
        criterion("has_input", RecipeProvider.conditionsFromItem(item))

    fun hasInput(tagKey: TagKey<Item>): HTRecipeJsonBuilder =
        criterion("has_input", RecipeProvider.conditionsFromTag(tagKey))

    fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        check(inputs.size in 0..3) { "Invalid input count; ${inputs.size}!" }
        check(outputs.size in 0..3) { "Invalid output count; ${outputs.size}!" }
        val prefix = "${type.asString()}/"
        exporter.accept(
            recipeId.withPrefixedPath(prefix),
            HTMachineRecipe(type, inputs, outputs),
            exporter.advancementBuilder
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .apply { criteria.forEach(this::criterion) }
                .build(
                    recipeId.withPrefixedPath("recipes/misc/$prefix")
                )
        )
    }
}

