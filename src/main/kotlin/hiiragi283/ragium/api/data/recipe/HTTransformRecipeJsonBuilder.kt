package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.alchemy.HTTransformRecipe
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementRequirements
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion
import net.minecraft.component.ComponentChanges
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTTransformRecipeJsonBuilder(val result: HTRecipeResult) : CraftingRecipeJsonBuilder {
    private val upgrades: MutableList<HTIngredient> = mutableListOf()
    private val criteria: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf()
    private val builder: ComponentChanges.Builder = ComponentChanges.builder()
    private var suffixCache: Int = 0

    constructor(item: ItemConvertible, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY) : this(
        HTRecipeResult.of(item, count, components),
    )

    /*constructor(tagKey: TagKey<Item>, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY) : this(
        HTRecipeResult.of(tagKey, count, components),
    )*/

    //    Input    //

    private lateinit var target: HTIngredient

    private fun setTarget(ingredient: HTIngredient): HTTransformRecipeJsonBuilder = apply {
        target = ingredient
    }

    fun setTarget(item: ItemConvertible, count: Int = 1): HTTransformRecipeJsonBuilder = setTarget(HTIngredient.of(item, count))

    fun setTarget(tagKey: TagKey<Item>, count: Int = 1): HTTransformRecipeJsonBuilder = setTarget(HTIngredient.of(tagKey, count))

    private fun addUpgrade(ingredient: HTIngredient): HTTransformRecipeJsonBuilder = apply {
        upgrades.add(ingredient)
    }

    fun addUpgrade(item: ItemConvertible, count: Int = 1): HTTransformRecipeJsonBuilder = addUpgrade(HTIngredient.of(item, count)).apply {
        hasInput(item, suffixCache.toString())
        suffixCache++
    }

    fun addUpgrade(tagKey: TagKey<Item>, count: Int = 1): HTTransformRecipeJsonBuilder = addUpgrade(HTIngredient.of(tagKey, count)).apply {
        hasInput(tagKey, suffixCache.toString())
        suffixCache++
    }

    fun modifyComponents(action: ComponentChanges.Builder.() -> Unit): HTTransformRecipeJsonBuilder = apply {
        builder.action()
    }

    //    Criterion    //

    override fun criterion(name: String, criterion: AdvancementCriterion<*>): HTTransformRecipeJsonBuilder = apply {
        criteria[name] = criterion
    }

    fun hasInput(item: ItemConvertible, suffix: String = ""): HTTransformRecipeJsonBuilder =
        criterion("has_input$suffix", RecipeProvider.conditionsFromItem(item))

    fun hasInput(tagKey: TagKey<Item>, suffix: String = ""): HTTransformRecipeJsonBuilder =
        criterion("has_input$suffix", RecipeProvider.conditionsFromTag(tagKey))

    //    CraftingRecipeJsonBuilder    //

    override fun group(group: String?): CraftingRecipeJsonBuilder = this

    override fun getOutputItem(): Item = result.firstItem

    fun offerWith(exporter: RecipeExporter, suffix: String = "") {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(outputItem).withSuffixedPath(suffix))
    }

    override fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        check(upgrades.size in 0..3) { "Invalid input count; ${upgrades.size}!" }
        val prefix = "alchemy/transform/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        exporter.accept(
            prefixedId,
            HTTransformRecipe(target, upgrades, result),
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
