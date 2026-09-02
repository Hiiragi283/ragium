@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.data.recipe.FluidIngredientBuilder
import hiiragi283.lib.data.recipe.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.HTItemResultBuilder
import hiiragi283.lib.data.recipe.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.IngredientBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
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
    override fun getPrimalId(): Identifier = fluidResult.getId()

    override fun createRecipe(): RTRefiningRecipe =
        RTRefiningRecipe(itemIngredient, fluidIngredient, itemResult, fluidResult, progressData)

    // Ingredient
    @PublishedApi internal var itemIngredient: Optional<HTItemIngredient> by HTDelegates.onceInitialize {
        Optional.empty()
    }

    @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredient = Optional.of(this)
    }

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this
    }

    inline fun itemIngredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun fluidIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +FluidIngredientBuilder().apply(builderAction).buildSized()
    }

    // Result
    @PublishedApi internal var itemResult: Optional<HTItemResult> by HTDelegates.onceInitialize { Optional.empty() }

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
        +HTItemResultBuilder().apply(builderAction).build()
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder().apply(builderAction).build()
    }
}
