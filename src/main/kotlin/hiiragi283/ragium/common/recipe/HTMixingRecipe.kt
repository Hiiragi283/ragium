package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

class HTMixingRecipe(
    val ingredient: Pair<HTItemIngredient, HTFluidIngredient>,
    result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTComplexRecipe(result, time, exp) {
    override fun matches(input: HTRecipeInput, level: Level): Boolean =
        input.testItem(0, getItemIngredient()) && input.testFluid(0, getFluidIngredient())

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()

    override fun getItemIngredient(): HTItemIngredient = ingredient.first

    override fun getFluidIngredient(): HTFluidIngredient = ingredient.second
}
