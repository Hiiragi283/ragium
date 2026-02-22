package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTBasicItemAndItemRecipe
import net.minecraft.resources.ResourceLocation

class HTItemAndItemRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    lateinit var first: HTItemIngredient
    lateinit var second: HTItemIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTBasicItemAndItemRecipe = factory.create(first, second, result, time)

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicItemAndItemRecipe> {
        fun create(
            first: HTItemIngredient,
            second: HTItemIngredient,
            result: HTItemResult,
            time: Int,
        ): RECIPE
    }
}
