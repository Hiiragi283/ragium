package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.data.recipe.builder.HTMultiOutputRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.impl.recipe.HTBasicItemFluidMultiOutputRecipe
import net.minecraft.data.recipes.RecipeOutput

class HTItemFluidMultiOutputRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTMultiOutputRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun washing(output: RecipeOutput, builderAction: HTItemFluidMultiOutputRecipeBuilder.() -> Unit) {
            HTItemFluidMultiOutputRecipeBuilder(RagiumConst.WASHING, ::HTWashingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var itemIngredient: HTItemIngredient
    lateinit var fluidIngredient: HTFluidIngredient

    override fun createRecipe(): HTBasicItemFluidMultiOutputRecipe = factory.create(itemIngredient, fluidIngredient, results, time)

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicItemFluidMultiOutputRecipe> {
        fun create(
            itemIngredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            results: List<HTItemResult>,
            time: Int,
        ): RECIPE
    }
}
