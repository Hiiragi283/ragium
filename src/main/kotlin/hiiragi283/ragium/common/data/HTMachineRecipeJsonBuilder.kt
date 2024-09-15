package hiiragi283.ragium.common.data

import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.recipe.HTRecipeResult
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

class HTMachineRecipeJsonBuilder(private val type: HTMachineType) {
    private val inputs: MutableList<WeightedIngredient> = mutableListOf()
    private val outputs: MutableList<HTRecipeResult> = mutableListOf()
    private var catalyst: Ingredient = Ingredient.EMPTY
    private val criteria: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf()
    private var suffixCache: Int = 0

    //    Input    //

    private fun addInput(ingredient: WeightedIngredient): HTMachineRecipeJsonBuilder = apply {
        inputs.add(ingredient)
    }

    fun addInput(ingredient: Ingredient, count: Int = 1): HTMachineRecipeJsonBuilder = addInput(WeightedIngredient.of(ingredient, count))

    fun addInput(item: ItemConvertible, count: Int = 1): HTMachineRecipeJsonBuilder = addInput(Ingredient.ofItems(item), count).apply {
        hasInput(item, suffixCache.toString())
        suffixCache++
    }

    fun addInput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeJsonBuilder = addInput(Ingredient.fromTag(tagKey), count).apply {
        hasInput(tagKey, suffixCache.toString())
        suffixCache++
    }

    //    Output    //

    private fun addOutput(result: HTRecipeResult): HTMachineRecipeJsonBuilder = apply {
        outputs.add(result)
    }

    fun addOutput(item: ItemConvertible, count: Int = 1): HTMachineRecipeJsonBuilder = addOutput(HTRecipeResult.item(item, count))

    fun addOutput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeJsonBuilder = addOutput(HTRecipeResult.tag(tagKey, count))

    //    Catalyst    //

    fun setCatalyst(catalyst: Ingredient): HTMachineRecipeJsonBuilder = apply {
        this.catalyst = catalyst
    }

    fun setCatalyst(item: ItemConvertible): HTMachineRecipeJsonBuilder = setCatalyst(Ingredient.ofItems(item))

    fun setCatalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilder = setCatalyst(Ingredient.fromTag(tagKey))

    //    Criterion    //

    fun criterion(name: String, criterion: AdvancementCriterion<*>): HTMachineRecipeJsonBuilder = apply {
        criteria[name] = criterion
    }

    fun hasInput(item: ItemConvertible, suffix: String = ""): HTMachineRecipeJsonBuilder =
        criterion("has_input$suffix", RecipeProvider.conditionsFromItem(item))

    fun hasInput(tagKey: TagKey<Item>, suffix: String = ""): HTMachineRecipeJsonBuilder =
        criterion("has_input$suffix", RecipeProvider.conditionsFromTag(tagKey))

    fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        check(inputs.size in 0..3) { "Invalid input count; ${inputs.size}!" }
        check(outputs.size in 0..3) { "Invalid output count; ${outputs.size}!" }
        val prefix = "${type.asString()}/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        exporter.accept(
            prefixedId,
            HTMachineRecipe(type, inputs, outputs, catalyst),
            exporter.advancementBuilder
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(prefixedId))
                .rewards(AdvancementRewards.Builder.recipe(prefixedId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .apply { criteria.forEach(this::criterion) }
                .build(
                    recipeId.withPrefixedPath("recipes/misc/$prefix"),
                ),
        )
    }
}
