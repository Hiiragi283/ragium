package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTItemOrFluidRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTMeltingRecipe(
    ingredient: Either<HTItemIngredient, HTFluidIngredient>,
    result: Either<HTItemResult, HTFluidResult>,
    parameters: SubParameters,
) : HTItemOrFluidRecipe(ingredient, result, parameters) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MELTING.get()
}
