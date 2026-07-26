@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HTFreezingRecipeBuilder : HTProgressRecipeBuilder<HTFreezingRecipe>(RagiumConst.FREEZING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTFreezingRecipeBuilder.() -> Unit): HTFreezingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTFreezingRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal var ingredient: HTFluidIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var catalyst: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()

    operator fun HTFluidIngredient.unaryPlus() {
        ingredient = this
    }

    operator fun Ingredient.unaryPlus() {
        catalyst = this
    }

    inline fun ingredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = FluidIngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun catalyst(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        catalyst = IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = HTItemResultBuilder().apply(builderAction).build()
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTFreezingRecipe = HTFreezingRecipe(ingredient, catalyst, result, progressData)
}
