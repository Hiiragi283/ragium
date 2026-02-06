package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTCompressingRecipeBuilder : HTRecipeBuilder(RagiumConst.COMPRESSING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTCompressingRecipeBuilder.() -> Unit) {
            HTCompressingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    var power: Int = 0
    var catalyst: HTItemIngredient? = null
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTCompressingRecipe = HTCompressingRecipe(ingredient, power, catalyst, result)
}
