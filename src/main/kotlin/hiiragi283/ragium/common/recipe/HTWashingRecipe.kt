package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTWashingRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    result: HTItemResult,
    extraResults: List<HTChancedItemResult>,
    parameters: SubParameters,
) : HTChancedRecipe(result, extraResults, parameters) {
    constructor(
        pair: Pair<HTItemIngredient, HTFluidIngredient>,
        result: HTItemResult,
        extraResults: List<HTChancedItemResult>,
        parameters: SubParameters,
    ) : this(
        pair.first,
        pair.second,
        result,
        extraResults,
        parameters,
    )

    override fun matches(input: HTViewRecipeInput, level: Level): Boolean =
        itemIngredient.test(input.getItemView(0)) && fluidIngredient.test(input.getFluidView(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.WASHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.WASHING.get()
}
