package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.monad.Either
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

class HTDryingRecipe(
    val ingredient: Either<HTItemIngredient, HTFluidIngredient>,
    result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTComplexRecipe(result, time, exp) {
    override fun matches(input: HTRecipeInput, level: Level): Boolean =
        ingredient.map({ input.testItem(0, it) }, { input.testFluid(0, it) })

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.DRYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.DRYING.get()

    override fun getItemIngredient(): HTItemIngredient? = ingredient.getLeft()

    override fun getFluidIngredient(): HTFluidIngredient? = ingredient.getRight()
}
