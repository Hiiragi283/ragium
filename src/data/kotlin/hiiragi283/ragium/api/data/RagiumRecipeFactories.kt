package hiiragi283.ragium.api.data

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

internal object RagiumRecipeFactories {
    @JvmStatic
    fun crushing(definition: HTRecipeDefinition): DataResult<HTCrushingRecipe> {
        val ingredient: SizedIngredient =
            definition.getItemIngredient(0).getOrNull() ?: return DataResult.error { "Required one item ingredient!" }
        val output: HTItemOutput =
            definition.getItemOutput(0).getOrNull() ?: return DataResult.error { "Required one item output!" }
        return DataResult.success(HTCrushingRecipe(ingredient, output, definition.getItemOutput(1)))
    }

    @JvmStatic
    fun extracting(definition: HTRecipeDefinition): DataResult<HTExtractingRecipe> {
        val ingredient: SizedIngredient =
            definition.getItemIngredient(0).getOrNull() ?: return DataResult.error { "Required one item ingredient!" }
        val itemOutput: Optional<HTItemOutput> = definition.getItemOutput(0)
        val fluidOutput: Optional<HTFluidOutput> = definition.getFluidOutput(0)
        if (itemOutput.isEmpty && fluidOutput.isEmpty) {
            return DataResult.error { "Either one fluid or item output required!" }
        }
        return DataResult.success(HTExtractingRecipe(ingredient, itemOutput, fluidOutput))
    }

    @JvmStatic
    fun infusing(definition: HTRecipeDefinition): DataResult<HTInfusingRecipe> {
        val itemIng: SizedIngredient =
            definition.getItemIngredient(0).getOrNull() ?: return DataResult.error { "Required one item ingredient!" }
        val fluidIng: SizedFluidIngredient =
            definition.getFluidIngredient(0).getOrNull() ?: return DataResult.error { "Required one fluid ingredient!" }
        val output: HTItemOutput =
            definition.getItemOutput(0).getOrNull() ?: return DataResult.error { "Required one item output!" }
        return DataResult.success(
            HTInfusingRecipe(
                itemIng,
                fluidIng,
                output,
            ),
        )
    }

    @JvmStatic
    fun refining(definition: HTRecipeDefinition): DataResult<HTRefiningRecipe> {
        val ingredient: SizedFluidIngredient =
            definition.getFluidIngredient(0).getOrNull() ?: return DataResult.error { "Required one fluid ingredient!" }
        val itemOutput: Optional<HTItemOutput> = definition.getItemOutput(0)
        val fluidOutput: HTFluidOutput =
            definition.getFluidOutput(0).getOrNull() ?: return DataResult.error { "Required one fluid output!" }
        return DataResult.success(HTRefiningRecipe(ingredient, fluidOutput, itemOutput))
    }

    @JvmStatic
    fun solidifying(definition: HTRecipeDefinition): DataResult<HTSolidifyingRecipe> {
        val ingredient: SizedFluidIngredient =
            definition.getFluidIngredient(0).getOrNull() ?: return DataResult.error { "Required one fluid ingredient!" }
        val output: HTItemOutput =
            definition.getItemOutput(0).getOrNull() ?: return DataResult.error { "Required one item output!" }
        return DataResult.success(HTSolidifyingRecipe(ingredient, definition.catalyst, output))
    }
}
