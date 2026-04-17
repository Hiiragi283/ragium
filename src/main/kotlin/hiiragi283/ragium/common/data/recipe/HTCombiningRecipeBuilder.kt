package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTCombiningRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun alloying(output: RecipeOutput, builderAction: HTCombiningRecipeBuilder.() -> Unit) {
            HTCombiningRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe).apply(builderAction).save(output)
        }
    }

    val ingredients: MutableList<HTItemIngredient> = mutableListOf()
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(
        ingredients,
        result,
        time,
    )

    //    Factory    //

    fun interface Factory<RECIPE : HTSerializableRecipe<*>> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult, time: Int): RECIPE
    }
}
