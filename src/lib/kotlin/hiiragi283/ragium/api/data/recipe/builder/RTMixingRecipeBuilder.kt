@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTMixingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTMixingRecipeBuilder : HTProgressRecipeBuilder<RTMixingRecipe>(RagiumConstants.MIXING) {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTMixingRecipe =
        RTMixingRecipe(itemIngredients.toNel(), fluidIngredient, result, progressData)

    // Ingredients
    private val itemIngredients: MutableList<HTItemIngredient> = ObjectArrayList()
    var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredients += this
    }

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this
    }

    inline fun itemIngredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    inline fun fluidIngredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidIngredientBuilder.build(builderAction)
    }

    // Results
    var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    var fluidResult: Optional<HTFluidResult> by HTDelegates.optionalInitialize()

    val result: HTItemOrFluidResult get() = Ior.fromNullable(itemResult, fluidResult)
        .map(::HTItemOrFluidResult)
        .orElseThrow { error("Either item or fluid result required") }

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
