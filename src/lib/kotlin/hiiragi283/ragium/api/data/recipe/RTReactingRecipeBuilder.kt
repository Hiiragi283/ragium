@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.data.recipe.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.HTItemResultBuilder
import hiiragi283.lib.data.recipe.HTProgressRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import net.minecraft.resources.Identifier
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTReactingRecipeBuilder : HTProgressRecipeBuilder<RTReactingRecipe.Basic>(RagiumConstants.REACTING) {
    override fun getPrimalId(): Identifier = results.map(HTItemResult::getId, HTFluidResult::getId)

    override fun createRecipe(): RTReactingRecipe.Basic =
        RTReactingRecipe.Basic(primaryIngredient, secondaryIngredient, results, progressData)

    // Ingredient
    @PublishedApi internal var primaryIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var secondaryIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    inline fun primaryIngredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        primaryIngredient = HTFluidIngredientBuilder.build(builderAction)
    }

    inline fun secondaryIngredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        secondaryIngredient = HTFluidIngredientBuilder.build(builderAction)
    }

    // Result
    @PublishedApi internal var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    @PublishedApi internal var fluidResult: Optional<HTFluidResult> by HTDelegates.onceInitialize()

    private val results: Ior<HTItemResult, HTFluidResult> by lazy {
        Ior.fromNullable(itemResult, fluidResult).orElseThrow { error("Either item or fluid result required") }
    }

    operator fun HTItemResult.unaryPlus() {
        itemResult = Optional.of(this)
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
