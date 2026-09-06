@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.data.recipe.FluidIngredientBuilder
import hiiragi283.lib.data.recipe.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.HTItemResultBuilder
import hiiragi283.lib.data.recipe.HTProgressRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import net.minecraft.resources.Identifier
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTReactingRecipeBuilder : HTProgressRecipeBuilder<RTReactingRecipe>(RagiumConstants.REACTING) {
    override fun getPrimalId(): Identifier = fluidResult.getId()

    override fun createRecipe(): RTReactingRecipe =
        RTReactingRecipe(primaryIngredient, secondaryIngredient, itemResult, fluidResult, progressData)

    // Ingredient
    @PublishedApi internal var primaryIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var secondaryIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    inline fun primaryIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        primaryIngredient = FluidIngredientBuilder.buildSized(builderAction)
    }

    inline fun secondaryIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        secondaryIngredient = FluidIngredientBuilder.buildSized(builderAction)
    }

    // Result
    @PublishedApi internal var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    @PublishedApi internal var fluidResult: HTFluidResult by HTDelegates.onceInitialize()

    operator fun HTItemResult.unaryPlus() {
        itemResult = Optional.of(this)
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResult = this
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
