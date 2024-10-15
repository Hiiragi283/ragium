package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.api.util.BothEither
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementRequirements
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion
import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentType
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTMachineRecipeJsonBuilder private constructor(
    private val type: HTMachineType,
    private val minTier: HTMachineTier = HTMachineTier.PRIMITIVE,
    private val requireScan: Boolean = false,
) {
    companion object {
        @JvmStatic
        fun create(
            type: HTMachineConvertible,
            minTier: HTMachineTier = HTMachineTier.PRIMITIVE,
            requireScan: Boolean = false,
        ): HTMachineRecipeJsonBuilder = type
            .asProcessor()
            ?.let { HTMachineRecipeJsonBuilder(it, minTier, requireScan) }
            ?: throw IllegalStateException("Machine Type;  ${type.asMachine().id} must be Processor!")
    }

    private val inputs: MutableList<HTIngredient> = mutableListOf()
    private val outputs: MutableList<HTRecipeResult> = mutableListOf()
    private var catalyst: Ingredient = Ingredient.EMPTY
    private val customData: ComponentChanges.Builder = ComponentChanges.builder()
    private val criteria: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf()
    private var suffixCache: Int = 0

    //    Input    //

    private fun addInput(ingredient: HTIngredient): HTMachineRecipeJsonBuilder = apply {
        inputs.add(ingredient)
    }

    fun addInput(either: BothEither<ItemConvertible, TagKey<Item>>, count: Int = 1): HTMachineRecipeJsonBuilder = apply {
        either.ifBoth({ addInput(it, count) }, { addInput(it, count) }, BothEither.Priority.RIGHT)
    }

    fun addInput(item: ItemConvertible, count: Int = 1): HTMachineRecipeJsonBuilder = addInput(HTIngredient.of(item, count)).apply {
        hasInput(item, suffixCache.toString())
        suffixCache++
    }

    fun addInput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeJsonBuilder = addInput(HTIngredient.of(tagKey, count)).apply {
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

    //    Custom Data    //

    fun <T : Any> setCustomData(type: ComponentType<T>, value: T): HTMachineRecipeJsonBuilder = apply {
        customData.add(type, value)
    }

    //    Criterion    //

    fun criterion(name: String, criterion: AdvancementCriterion<*>): HTMachineRecipeJsonBuilder = apply {
        criteria[name] = criterion
    }

    fun hasInput(item: ItemConvertible, suffix: String = ""): HTMachineRecipeJsonBuilder =
        criterion("has_input$suffix", RecipeProvider.conditionsFromItem(item))

    fun hasInput(tagKey: TagKey<Item>, suffix: String = ""): HTMachineRecipeJsonBuilder =
        criterion("has_input$suffix", RecipeProvider.conditionsFromTag(tagKey))

    fun offerSuffix(exporter: RecipeExporter, suffix: String = "") {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(outputs[0].value).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, recipeId: Identifier = CraftingRecipeJsonBuilder.getItemId(outputs[0].value)) {
        check(inputs.size in 0..3) { "Invalid input count; ${inputs.size}!" }
        check(outputs.size in 0..3) { "Invalid output count; ${outputs.size}!" }
        if (requireScan) {
            setCustomData(HTRecipeComponentTypes.REQUIRE_SCAN, true)
        }
        val prefix = "${type.id.path}/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        exporter.accept(
            prefixedId,
            HTMachineRecipe(type, minTier, inputs, outputs, catalyst, customData.build()),
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
