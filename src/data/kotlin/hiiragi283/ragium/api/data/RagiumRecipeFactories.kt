package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.data.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*
import kotlin.jvm.optionals.getOrNull

internal object RagiumRecipeFactories {
    @JvmStatic
    fun alloying(definition: HTRecipeDefinition): HTAlloyingRecipe {
        // Item Input
        val itemInputs: List<SizedIngredient> = definition.itemInputs
        check(itemInputs.size == 2) { "Alloying Recipe requires 2 item ingredients!" }
        // Item Output
        val itemOutputs: List<HTItemOutput> = definition.itemOutputs
        check(itemOutputs.isNotEmpty()) { "Alloying Recipe requires 1 item output at least!" }
        check(itemOutputs.size <= 4) { "Alloying Recipe accepts 4 or less item outputs!" }
        return HTAlloyingRecipe(itemInputs, itemOutputs)
    }

    @JvmStatic
    fun crushing(definition: HTRecipeDefinition): HTCrushingRecipe {
        // Item Input
        val itemInputs: List<SizedIngredient> = definition.itemInputs
        check(itemInputs.size == 1) { "Crushing Recipe requires 1 item ingredient!" }
        // Item Output
        val itemOutputs: List<HTItemOutput> = definition.itemOutputs
        check(itemOutputs.isNotEmpty()) { "Crushing Recipe requires 1 item output at least!" }
        check(itemOutputs.size <= 4) { "Crushing Recipe accepts 4 or less item outputs!" }
        // Fluid
        check(definition.fluidInputs.isEmpty() && definition.fluidOutputs.isEmpty()) { "Crushing Recipe does not support fluids!" }
        return HTCrushingRecipe(itemInputs[0], itemOutputs)
    }

    @JvmStatic
    fun extracting(definition: HTRecipeDefinition): HTExtractingRecipe {
        val ingredient: SizedIngredient =
            checkPresent(definition.getItemIngredient(0), "Extracting Recipe requires 1 item ingredient!")
        val itemOutput: HTItemOutput =
            checkPresent(definition.getItemOutput(0), "Extracting Recipe requires 1 item output!")
        return HTExtractingRecipe(ingredient, itemOutput)
    }

    @JvmStatic
    fun melting(definition: HTRecipeDefinition): HTMeltingRecipe {
        // Item Input
        val itemInputs: List<SizedIngredient> = definition.itemInputs
        check(itemInputs.size == 1) { "Melting Recipe requires 1 item ingredient!" }
        // Fluid Output
        val fluidOutput: HTFluidOutput = checkPresent(definition.getFluidOutput(0), "Melting Recipe requires 1 fluid output!")
        return HTMeltingRecipe(itemInputs[0], fluidOutput)
    }

    fun pressing(definition: HTRecipeDefinition): HTPressingRecipe {
        // Item Input
        val itemInputs: List<SizedIngredient> = definition.itemInputs
        val top: SizedIngredient = itemInputs.getOrNull(0) ?: error("Pressing Recipe requires 2 ingredient!")
        if (itemInputs.size > 2) {
            error("Pressing Recipe accepts only 2 item ingredients!")
        }
        // Item Output
        val output: HTItemOutput = checkPresent(definition.getItemOutput(0), "Required one item output!")
        // Fluid
        if (definition.fluidInputs.isNotEmpty() || definition.fluidOutputs.isNotEmpty()) {
            error("Crushing Recipe does not support fluids!")
        }
        return HTPressingRecipe(top, definition.catalyst, output)
    }

    @JvmStatic
    fun refining(definition: HTRecipeDefinition): HTRefiningRecipe {
        val ingredient: SizedFluidIngredient = checkPresent(definition.getFluidIngredient(0), "Required one fluid ingredient!")
        val fluidOutputs: List<HTFluidOutput> = definition.fluidOutputs
        check(fluidOutputs.isNotEmpty()) { "Crushing Recipe requires 1 fluid output at least!" }
        check(fluidOutputs.size <= 2) { "Crushing Recipe accepts 2 or less fluid outputs!" }
        return HTRefiningRecipe(ingredient, definition.getItemOutput(0), fluidOutputs)
    }

    @JvmStatic
    fun solidifying(definition: HTRecipeDefinition): HTSolidifyingRecipe {
        val ingredient: SizedFluidIngredient = checkPresent(definition.getFluidIngredient(0), "Required one fluid ingredient!")
        val output: HTItemOutput = checkPresent(definition.getItemOutput(0), "Required one item output!")
        return HTSolidifyingRecipe(ingredient, definition.catalyst, output)
    }

    //    Extension    //

    @JvmStatic
    private fun <T : Any> checkPresent(optional: Optional<T>, message: String): T = checkNotNull(optional.getOrNull()) { message }
}
