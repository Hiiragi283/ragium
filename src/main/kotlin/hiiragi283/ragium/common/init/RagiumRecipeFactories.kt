package hiiragi283.ragium.common.init

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.common.recipe.*
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

object RagiumRecipeFactories {
    @JvmStatic
    fun centrifuge(definition: HTRecipeDefinition): DataResult<HTCentrifugingRecipe> {
        val ingredient: Either<SizedIngredient, SizedFluidIngredient> =
            when {
                definition.getItemIngredient(0) != null -> Either.left(definition.getItemIngredient(0))
                definition.getFluidIngredient(0) != null -> Either.right(definition.getFluidIngredient(0))
                else -> return DataResult.error { "Either one item or fluid ingredient required!" }
            }
        return DataResult.success(HTCentrifugingRecipe(ingredient, definition.itemOutputs, definition.fluidOutputs))
    }

    //    ItemProcess    //

    @JvmStatic
    fun <R : HTItemProcessRecipe> itemProcess(
        factory: (SizedIngredient, HTItemOutput, HTFluidOutput?) -> R,
        definition: HTRecipeDefinition,
    ): DataResult<R> {
        val ingredient: SizedIngredient =
            definition.getItemIngredient(0) ?: return DataResult.error { "Required one item ingredient!" }
        val itemOutput: HTItemOutput =
            definition.getItemOutput(0) ?: return DataResult.error { "Required one item output!" }
        val fluidOutput: HTFluidOutput? = definition.getFluidOutput(0)
        return DataResult.success(factory(ingredient, itemOutput, fluidOutput))
    }

    @JvmStatic
    fun crushing(definition: HTRecipeDefinition): DataResult<HTCrushingRecipe> = itemProcess(::HTCrushingRecipe, definition)

    @JvmStatic
    fun extracting(definition: HTRecipeDefinition): DataResult<HTExtractingRecipe> = itemProcess(::HTExtractingRecipe, definition)

    @JvmStatic
    fun fermenting(definition: HTRecipeDefinition): DataResult<HTFermentingRecipe> = itemProcess(::HTFermentingRecipe, definition)
}
