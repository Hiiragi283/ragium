package hiiragi283.ragium.common.init

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.common.recipe.HTCentrifugingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

object RagiumRecipeFactories {
    @JvmStatic
    fun centrifuging(definition: HTRecipeDefinition): DataResult<HTCentrifugingRecipe> {
        val ingredient: Either<SizedIngredient, SizedFluidIngredient> =
            when {
                definition.getItemIngredient(0) != null -> Either.left(definition.getItemIngredient(0))
                definition.getFluidIngredient(0) != null -> Either.right(definition.getFluidIngredient(0))
                else -> return DataResult.error { "Either one item or fluid ingredient required!" }
            }
        return DataResult.success(HTCentrifugingRecipe(ingredient, definition.itemOutputs, definition.fluidOutputs))
    }

    //    Fluid -> Fluid    //

    @JvmStatic
    fun <R : HTSimpleFluidRecipe> fluidProcess(
        factory: (SizedFluidIngredient, HTFluidOutput) -> R,
        definition: HTRecipeDefinition,
    ): DataResult<R> {
        val ingredient: SizedFluidIngredient =
            definition.getFluidIngredient(0) ?: return DataResult.error { "Required one fluid ingredient!" }
        val output: HTFluidOutput =
            definition.getFluidOutput(0) ?: return DataResult.error { "Required one fluid output!" }
        return DataResult.success(factory(ingredient, output))
    }

    @JvmStatic
    fun refining(definition: HTRecipeDefinition): DataResult<HTRefiningRecipe> = fluidProcess(::HTRefiningRecipe, definition)

    //    Item -> Item    //

    @JvmStatic
    fun <R : HTSimpleItemRecipe> itemProcess(
        factory: (SizedIngredient, HTItemOutput) -> R,
        definition: HTRecipeDefinition,
    ): DataResult<R> {
        val ingredient: SizedIngredient =
            definition.getItemIngredient(0) ?: return DataResult.error { "Required one item ingredient!" }
        val output: HTItemOutput =
            definition.getItemOutput(0) ?: return DataResult.error { "Required one item output!" }
        return DataResult.success(factory(ingredient, output))
    }

    @JvmStatic
    fun crushing(definition: HTRecipeDefinition): DataResult<HTCrushingRecipe> = itemProcess(::HTCrushingRecipe, definition)

    @JvmStatic
    fun extracting(definition: HTRecipeDefinition): DataResult<HTExtractingRecipe> = itemProcess(::HTExtractingRecipe, definition)
}
