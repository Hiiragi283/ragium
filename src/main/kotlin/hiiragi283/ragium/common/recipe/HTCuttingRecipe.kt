package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicSingleMultiOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCuttingRecipe(ingredient: HTItemIngredient, results: List<HTItemResult>, time: Int) :
    HTBasicSingleMultiOutputRecipe(ingredient, results, time) {
    companion object {
        @JvmField
        val OUTPUT_RANGE: IntRange = 1..2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CUTTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CUTTING.get()
}
