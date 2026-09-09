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
import hiiragi283.ragium.api.recipe.RTRefiningRecipe
import net.minecraft.resources.Identifier
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTRefiningRecipeBuilder : HTProgressRecipeBuilder<RTRefiningRecipe>(RagiumConstants.REFINING) {
    override fun getPrimalId(): Identifier = primaryResult.getId()

    override fun createRecipe(): RTRefiningRecipe =
        RTRefiningRecipe(ingredient, itemResult, primaryResult, secondaryResult, progressData)

    // Ingredient
    @PublishedApi internal var ingredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun HTFluidIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +FluidIngredientBuilder.buildSized(builderAction)
    }

    // Result
    @PublishedApi internal var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    @PublishedApi internal var primaryResult: HTFluidResult by HTDelegates.onceInitialize()

    @PublishedApi internal var secondaryResult: Optional<HTFluidResult> by HTDelegates.optionalInitialize()

    operator fun HTItemResult.unaryPlus() {
        itemResult = Optional.of(this)
    }

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder.build(builderAction)
    }

    inline fun primaryResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        primaryResult = HTFluidResultBuilder.build(builderAction)
    }

    inline fun secondaryResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        secondaryResult = Optional.of(HTFluidResultBuilder.build(builderAction))
    }
}
