package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTPressingRecipeBuilder(private val copyComponent: Boolean, prefix: String) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun pressing(output: RecipeOutput, builderAction: HTPressingRecipeBuilder.() -> Unit) {
            HTPressingRecipeBuilder(false, RagiumConst.PRESSING).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun printing(output: RecipeOutput, builderAction: HTPressingRecipeBuilder.() -> Unit) {
            HTPressingRecipeBuilder(true, RagiumConst.PRINTING).apply(builderAction).save(output)
        }
    }

    lateinit var top: HTItemIngredient
    lateinit var bottom: HTItemIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTPressingRecipe = HTPressingRecipe(
        top,
        bottom,
        result,
        copyComponent,
        subParameters(),
    )
}
