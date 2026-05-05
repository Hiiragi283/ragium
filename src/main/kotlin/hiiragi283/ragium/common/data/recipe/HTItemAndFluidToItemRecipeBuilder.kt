package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTItemAndFluidToItemRecipeBuilder(prefix: String, private val factory: Factory<out HTSerializableRecipe<*>>) :
    HTProgressRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun bathing(output: RecipeOutput, builderAction: HTItemAndFluidToItemRecipeBuilder.() -> Unit) {
            HTItemAndFluidToItemRecipeBuilder(RagiumConst.BATHING, ::HTBathingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var itemIngredient: HTItemIngredient
    lateinit var fluidIngredient: HTFluidIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(
        itemIngredient,
        fluidIngredient,
        result,
        progressData,
    )

    //    Factory    //

    fun interface Factory<RECIPE : Any> {
        fun create(
            itemIngredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            result: HTItemResult,
            progressData: HTProgressData,
        ): RECIPE
    }
}
