package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.base.HTFluidWithItemRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTFluidWithItemRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun solidifying(output: RecipeOutput, builderAction: HTFluidWithItemRecipeBuilder.() -> Unit) {
            HTFluidWithItemRecipeBuilder(RagiumConst.SOLIDIFYING, ::HTSolidifyingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var fluidIngredient: HTFluidIngredient
    lateinit var itemIngredient: HTItemIngredient
    lateinit var result: HTItemResult

    init {
        time /= 2
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTFluidWithItemRecipe = factory.create(
        fluidIngredient,
        itemIngredient,
        result,
        subParameters(),
    )

    //    Factory    //

    fun interface Factory<RECIPE : HTFluidWithItemRecipe> {
        fun create(
            fluidIngredient: HTFluidIngredient,
            itemIngredient: HTItemIngredient,
            result: HTItemResult,
            parameters: HTProcessingRecipe.SubParameters,
        ): RECIPE
    }
}
