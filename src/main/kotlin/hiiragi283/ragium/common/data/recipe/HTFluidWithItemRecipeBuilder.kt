package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.base.HTFluidWithItemRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTFluidWithItemRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun bathing(output: RecipeOutput, builderAction: HTFluidWithItemRecipeBuilder.() -> Unit) {
            HTFluidWithItemRecipeBuilder(RagiumConst.BATHING, ::HTBathingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun solidifying(output: RecipeOutput, builderAction: HTFluidWithItemRecipeBuilder.() -> Unit) {
            HTFluidWithItemRecipeBuilder(RagiumConst.SOLIDIFYING, ::HTSolidifyingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var fluidIngredient: HTFluidIngredient
    lateinit var itemIngredient: HTItemIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTFluidWithItemRecipe = factory.create(
        fluidIngredient,
        itemIngredient,
        result,
        time,
        exp,
    )

    //    Factory    //

    fun interface Factory<RECIPE : HTFluidWithItemRecipe> {
        fun create(
            fluidIngredient: HTFluidIngredient,
            itemIngredient: HTItemIngredient,
            result: HTItemResult,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
