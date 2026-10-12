@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTCentrifugingRecipe
import net.minecraft.resources.Identifier
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTCentrifugingRecipeBuilder : HTProgressRecipeBuilder<RTCentrifugingRecipe.Basic>(RagiumConstants.CENTRIFUGING) {
    override fun getRecipeId(): Identifier? = itemResults.first().getId()

    override fun createRecipe(): RTCentrifugingRecipe.Basic = RTCentrifugingRecipe.Basic(
        ingredient,
        RTCentrifugingRecipe.Result(itemResults.toNel(), fluidResult),
        progressData
    )

    // Ingredient
    var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    // Results
    var itemResults: MutableList<HTItemResult> = mutableListOf()

    var fluidResult: Optional<HTFluidResult> by HTDelegates.optionalInitialize()

    operator fun HTItemResult.unaryPlus() {
        itemResults += this
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResult = Optional.of(this)
    }

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder.build(builderAction)
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder.build(builderAction)
    }
}
