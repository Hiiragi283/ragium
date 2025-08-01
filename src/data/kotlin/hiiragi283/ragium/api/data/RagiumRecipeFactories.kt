package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.data.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
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
    fun melting(definition: HTRecipeDefinition): HTMeltingRecipe {
        // Item Input
        val itemInputs: List<SizedIngredient> = definition.itemInputs
        check(itemInputs.size == 1) { "Melting Recipe requires 1 item ingredient!" }
        // Fluid Output
        val fluidOutput: HTFluidOutput = checkPresent(definition.getFluidOutput(0), "Melting Recipe requires 1 fluid output!")
        return HTMeltingRecipe(itemInputs[0], fluidOutput)
    }

    @JvmStatic
    fun pressing(definition: HTRecipeDefinition): HTPressingRecipe {
        // Item Input
        val itemInputs: List<SizedIngredient> = definition.itemInputs
        val top: SizedIngredient = itemInputs.getOrNull(0) ?: error("Pressing Recipe requires 2 ingredient!")
        if (itemInputs.size > 1) {
            error("Pressing Recipe accepts only 1 item ingredients!")
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
        return HTSolidifyingRecipe(ingredient, definition.getItemIngredient(0), output)
    }

    //    Extension    //

    @JvmStatic
    private fun <T : Any> checkPresent(optional: Optional<T>, message: String): T = checkNotNull(optional.getOrNull()) { message }
}
