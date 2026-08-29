@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTFluidResultBuilder
import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.some
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTChemicalReactingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation

class HTChemicalReactingRecipeBuilder : HTProgressRecipeBuilder<HTChemicalReactingRecipe>(RagiumConst.CHEMICAL_REACTING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTChemicalReactingRecipeBuilder.() -> Unit): HTChemicalReactingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTChemicalReactingRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal val ingredients: MutableList<HTFluidIngredient> = mutableListOf()

    @PublishedApi internal var catalyst: Option<HTItemIngredient> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal val fluidResults: MutableList<HTFluidResult> = mutableListOf()

    @PublishedApi internal var itemResult: Option<HTItemResult> by HTDelegates.onceInitialize { Option.none() }

    operator fun HTFluidIngredient.unaryPlus() {
        ingredients += this
    }

    operator fun HTItemIngredient.unaryPlus() {
        catalyst = this.some()
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResults += this
    }

    inline fun ingredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredients += FluidIngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun catalyst(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        fluidResults += HTFluidResultBuilder().apply(builderAction).build()
    }

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        itemResult = Option.some(HTItemResultBuilder().apply(builderAction).build())
    }

    override fun getPrimalId(): ResourceLocation = fluidResults[0].getId()

    override fun createRecipe(): HTChemicalReactingRecipe = HTChemicalReactingRecipe(
        ingredients[0],
        Ior.fromNullable(ingredients.getOrNull(1), catalyst.getOrNull()) ?: error("Either second fluid ingredient or catalyst required"),
        fluidResults,
        itemResult,
        progressData,
    )
}
