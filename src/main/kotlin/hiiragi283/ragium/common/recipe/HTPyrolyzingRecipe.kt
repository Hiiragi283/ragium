package hiiragi283.ragium.common.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import org.apache.commons.lang3.math.Fraction

class HTPyrolyzingRecipe(
    ingredient: Either<HTItemIngredient, HTFluidIngredient>,
    result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTComplexRecipe(ingredient, result, time, exp) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PYROLYZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PYROLYZING.get()
}
