@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.data.recipe.FluidIngredientBuilder
import hiiragi283.lib.data.recipe.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.IngredientBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Option
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier

class RTElectrolyzingRecipeBuilder : HTProgressRecipeBuilder<RTElectrolyzingRecipe>(RagiumConstants.ELECTROLYZING) {
    override fun getPrimalId(): Identifier = results.first().getId()

    override fun createRecipe(): RTElectrolyzingRecipe = RTElectrolyzingRecipe(itemIngredient, fluidIngredient, results, progressData)

    // Ingredient
    @PublishedApi internal var itemIngredient: Option<HTItemIngredient> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    inline fun itemIngredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        itemIngredient = Option.some(IngredientBuilder().apply(builderAction).buildSized())
    }

    inline fun fluidIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        fluidIngredient = FluidIngredientBuilder().apply(builderAction).buildSized()
    }

    // Result
    @PublishedApi internal var results: MutableList<HTFluidResult> = mutableListOf()

    operator fun HTFluidResult.unaryPlus() {
        results += this
    }

    inline fun result(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        results += HTFluidResultBuilder().apply(builderAction).build()
    }
}
