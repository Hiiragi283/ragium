package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult

fun interface HTMachineRecipeValidator {
    fun validate(recipe: HTMachineRecipe): DataResult<HTMachineRecipe>
}
