@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

class HTItemToItemAndFluidRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    override fun getPrimalId(): Identifier = itemResult.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, itemResult, fluidResult, progressData)

    // Ingredient
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).buildSized()
    }

    // Result
    @PublishedApi internal var itemResult: HTItemResult by HTDelegates.onceInitialize()

    @PublishedApi internal var fluidResult: HTFluidResult by HTDelegates.onceInitialize()

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        itemResult = HTItemResultBuilder().apply(builderAction).build()
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        fluidResult = HTFluidResultBuilder().apply(builderAction).build()
    }

    //    Factory    //

    fun interface Factory<out RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, itemResult: HTItemResult, fluidResult: HTFluidResult, progressData: HTProgressData): RECIPE
    }
}
