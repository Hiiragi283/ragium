package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.machines.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.machines.HTMachineRecipeBase
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier

// typealias AdvBuilder = Advancement.Builder.() -> Unit

object HTMachineRecipeJsonBuilders {
    @JvmStatic
    fun createGrinder(
        exporter: RecipeExporter,
        input: HTItemIngredient,
        firstOutput: HTItemResult,
        secondOutput: HTItemResult? = null,
        catalyst: HTItemIngredient = HTItemIngredient.EMPTY_ITEM,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
        recipeId: Identifier = createRecipeId(firstOutput.entryValue),
    ) {
        createRecipe(
            exporter,
            HTGrinderRecipe(
                tier,
                input,
                buildList {
                    add(firstOutput)
                    secondOutput?.let(::add)
                },
                catalyst
            ),
            recipeId.withPrefixedPath("grinder/"),
        )
    }

    @JvmStatic
    fun createRecipeId(recipe: HTMachineRecipeBase<*>): Identifier =
        createRecipeId(recipe.firstOutput.item)

    @JvmStatic
    fun createRecipeId(item: ItemConvertible): Identifier =
        CraftingRecipeJsonBuilder.getItemId(item)
            .path
            .let { RagiumAPI.id(it) }

    @JvmStatic
    private fun createRecipe(
        exporter: RecipeExporter,
        recipe: HTMachineRecipeBase<*>,
        recipeId: Identifier = createRecipeId(recipe),
    ) {
        exporter.accept(
            recipeId,
            recipe,
            null
            /*exporter.advancementBuilder
                .apply(builderAction)
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .build(recipeId.withPrefixedPath("recipes/misc/")),*/
        )
    }
}
