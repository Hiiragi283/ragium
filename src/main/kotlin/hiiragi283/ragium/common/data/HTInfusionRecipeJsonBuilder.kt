package hiiragi283.ragium.common.data

import hiiragi283.ragium.common.recipe.HTInfusionRecipe
import hiiragi283.ragium.common.recipe.WeightedIngredient
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementRequirements
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTInfusionRecipeJsonBuilder(val output: ItemStack) : CraftingRecipeJsonBuilder {
    private val inputs: MutableList<WeightedIngredient> = mutableListOf()
    private val criteria: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf()
    private var suffixCache: Int = 0

    constructor(output: ItemConvertible, count: Int = 1) : this(ItemStack(output, count))

    //    Input    //

    private fun addInput(ingredient: WeightedIngredient): HTInfusionRecipeJsonBuilder = apply {
        inputs.add(ingredient)
    }

    fun addInput(ingredient: Ingredient, count: Int = 1): HTInfusionRecipeJsonBuilder = addInput(WeightedIngredient.of(ingredient, count))

    fun addInput(item: ItemConvertible, count: Int = 1): HTInfusionRecipeJsonBuilder = addInput(Ingredient.ofItems(item), count).apply {
        hasInput(item, suffixCache.toString())
        suffixCache++
    }

    fun addInput(tagKey: TagKey<Item>, count: Int = 1): HTInfusionRecipeJsonBuilder = addInput(Ingredient.fromTag(tagKey), count).apply {
        hasInput(tagKey, suffixCache.toString())
        suffixCache++
    }

    //    Criterion    //

    override fun criterion(name: String, criterion: AdvancementCriterion<*>): HTInfusionRecipeJsonBuilder = apply {
        criteria[name] = criterion
    }

    fun hasInput(item: ItemConvertible, suffix: String = ""): HTInfusionRecipeJsonBuilder =
        criterion("has_input$suffix", RecipeProvider.conditionsFromItem(item))

    fun hasInput(tagKey: TagKey<Item>, suffix: String = ""): HTInfusionRecipeJsonBuilder =
        criterion("has_input$suffix", RecipeProvider.conditionsFromTag(tagKey))

    //    CraftingRecipeJsonBuilder    //

    override fun group(group: String?): CraftingRecipeJsonBuilder = this

    override fun getOutputItem(): Item = output.item

    fun offerWith(exporter: RecipeExporter, suffix: String = "") {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(outputItem).withSuffixedPath(suffix))
    }

    override fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        check(inputs.size in 0..4) { "Invalid input count; ${inputs.size}!" }
        val prefix = "alchemy/infuse/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        exporter.accept(
            prefixedId,
            HTInfusionRecipe(inputs, output),
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
