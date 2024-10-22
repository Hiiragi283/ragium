package hiiragi283.ragium.api.data.recipe

import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object HTCookingRecipeJsonBuilder {
    @JvmStatic
    fun smeltAndBlast(
        exporter: RecipeExporter,
        input: TagKey<Item>,
        output: ItemConvertible,
        exp: Float = 0.0f,
        time: Int = 200,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
        suffix: String = "",
        wrapper: (RecipeExporter, TagKey<Item>) -> RecipeExporter = { exporter1: RecipeExporter, _: TagKey<Item> -> exporter1 },
    ) {
        CookingRecipeJsonBuilder
            .createSmelting(
                Ingredient.fromTag(input),
                RecipeCategory.MISC,
                output,
                exp,
                time,
            ).criterion("has_input", RecipeProvider.conditionsFromTag(input))
            .offerTo(wrapper(exporter, input), id.withPrefixedPath("smelting/").withSuffixedPath(suffix))
        CookingRecipeJsonBuilder
            .createBlasting(
                Ingredient.fromTag(input),
                RecipeCategory.MISC,
                output,
                exp,
                time / 2,
            ).criterion("has_input", RecipeProvider.conditionsFromTag(input))
            .offerTo(wrapper(exporter, input), id.withPrefixedPath("blasting/").withSuffixedPath(suffix))
    }

    @JvmStatic
    fun smeltAndBlast(
        exporter: RecipeExporter,
        input: ItemConvertible,
        output: ItemConvertible,
        exp: Float = 0.0f,
        time: Int = 200,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
        suffix: String = "",
    ) {
        CookingRecipeJsonBuilder
            .createSmelting(
                Ingredient.ofItems(input),
                RecipeCategory.MISC,
                output,
                exp,
                time,
            ).criterion("has_input", RecipeProvider.conditionsFromItem(input))
            .offerTo(exporter, id.withPrefixedPath("smelting/").withSuffixedPath(suffix))
        CookingRecipeJsonBuilder
            .createBlasting(
                Ingredient.ofItems(input),
                RecipeCategory.MISC,
                output,
                exp,
                time / 2,
            ).criterion("has_input", RecipeProvider.conditionsFromItem(input))
            .offerTo(exporter, id.withPrefixedPath("blasting/").withSuffixedPath(suffix))
    }

    @JvmStatic
    fun smeltAndSmoke(
        exporter: RecipeExporter,
        input: Ingredient,
        output: ItemConvertible,
        condition: AdvancementCriterion<InventoryChangedCriterion.Conditions>,
        exp: Float = 0.0f,
        time: Int = 200,
        id: Identifier = CraftingRecipeJsonBuilder.getItemId(output),
        suffix: String = "",
    ) {
        CookingRecipeJsonBuilder
            .createSmelting(
                input,
                RecipeCategory.FOOD,
                output,
                exp,
                time,
            ).criterion("has_input", condition)
            .offerTo(exporter, id.withPrefixedPath("smelting/").withSuffixedPath(suffix))
        CookingRecipeJsonBuilder
            .createCampfireCooking(
                input,
                RecipeCategory.FOOD,
                output,
                exp,
                time,
            ).criterion("has_input", condition)
            .offerTo(exporter, id.withPrefixedPath("campfire/").withSuffixedPath(suffix))
        CookingRecipeJsonBuilder
            .createSmoking(
                input,
                RecipeCategory.FOOD,
                output,
                exp,
                time / 2,
            ).criterion("has_input", condition)
            .offerTo(exporter, id.withPrefixedPath("smoking/").withSuffixedPath(suffix))
    }
}
