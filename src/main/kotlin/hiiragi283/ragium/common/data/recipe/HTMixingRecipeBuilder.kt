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
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation

class HTMixingRecipeBuilder : HTProgressRecipeBuilder<HTMixingRecipe>(RagiumConst.MIXING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTMixingRecipeBuilder.() -> Unit): HTMixingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTMixingRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()

    @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var itemResult: Option<HTItemResult> by HTDelegates.optionalOnceInitialize()

    @PublishedApi internal var fluidResult: Option<HTFluidResult> by HTDelegates.optionalOnceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredients += this
    }

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this
    }

    operator fun HTItemResult.unaryPlus() {
        itemResult = this.some()
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResult = this.some()
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

    private fun toIorResult(): Ior<HTItemResult, HTFluidResult> = Ior.fromOption(itemResult, fluidResult).getOrNull() ?: error("Either item or fluid result required")

    override fun getPrimalId(): ResourceLocation = toIorResult().swap().map(HTFluidResult::getId, HTItemResult::getId)

    override fun createRecipe(): HTMixingRecipe = HTMixingRecipe(itemIngredients, fluidIngredient, toIorResult(), progressData)
}
