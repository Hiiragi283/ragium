@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTFluidResultBuilder
import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.some
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HTRefiningRecipeBuilder : HTProgressRecipeBuilder<HTRefiningRecipe>(RagiumConst.REFINING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTRefiningRecipeBuilder.() -> Unit): HTRefiningRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTRefiningRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal var ingredient: HTFluidIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var catalyst: Option<Ingredient> by HTDelegates.optionalOnceInitialize()

    @PublishedApi internal val fluidResults: MutableList<HTFluidResult> = mutableListOf()

    @PublishedApi internal var itemResult: Option<HTItemResult> by HTDelegates.optionalOnceInitialize()

    operator fun HTFluidIngredient.unaryPlus() {
        ingredient = this
    }

    operator fun Ingredient.unaryPlus() {
        catalyst = this.some()
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResults += this
    }

    operator fun HTItemResult.unaryPlus() {
        itemResult = this.some()
    }

    inline fun fluidIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +FluidIngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun catalyst(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder().apply(builderAction).build()
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder().apply(builderAction).build()
    }

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder().apply(builderAction).build()
    }

    override fun getPrimalId(): ResourceLocation = fluidResults[0].getId()

    override fun createRecipe(): HTRefiningRecipe = HTRefiningRecipe(ingredient, catalyst, fluidResults, itemResult, progressData)
}
