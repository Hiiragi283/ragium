package hiiragi283.ragium.api.data

import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

/**
 * Register [net.minecraft.recipe.StonecuttingRecipe] with recipe id prefixed with "stonecutting/"
 */
object HTStonecuttingRecipeJsonBuilder {
    @JvmStatic
    fun register(
        exporter: RecipeExporter,
        input: ItemConvertible,
        output: ItemConvertible,
        count: Int = 1,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
        category: RecipeCategory = RecipeCategory.MISC,
        suffix: String = "",
    ) {
        StonecuttingRecipeJsonBuilder
            .createStonecutting(
                Ingredient.ofItems(input),
                category,
                output,
                count,
            ).criterion("has_input", RecipeProvider.conditionsFromItem(input))
            .offerTo(exporter, id.withPrefixedPath("stonecutting/").withSuffixedPath(suffix))
    }

    @JvmStatic
    fun register(
        exporter: RecipeExporter,
        input: TagKey<Item>,
        output: ItemConvertible,
        count: Int = 1,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
        category: RecipeCategory = RecipeCategory.MISC,
        suffix: String = "",
    ) {
        StonecuttingRecipeJsonBuilder
            .createStonecutting(
                Ingredient.fromTag(input),
                category,
                output,
                count,
            ).criterion("has_input", RecipeProvider.conditionsFromTag(input))
            .offerTo(exporter, id.withPrefixedPath("stonecutting/").withSuffixedPath(suffix))
    }

    @JvmStatic
    fun registerExchange(
        exporter: RecipeExporter,
        input: ItemConvertible,
        output: ItemConvertible,
        category: RecipeCategory = RecipeCategory.MISC,
    ) {
        register(exporter, input, output, category = category, suffix = "_to")
        register(exporter, output, input, category = category, suffix = "_from")
    }
}
