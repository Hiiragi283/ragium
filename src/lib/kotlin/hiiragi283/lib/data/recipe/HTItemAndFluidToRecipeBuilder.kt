@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTCatalystOrIngredient
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

abstract class HTItemAndFluidToRecipeBuilder<RESULT : HTRecipeResult<*>, out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RESULT, RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    final override fun getPrimalId(): Identifier = result.getId()

    final override fun createRecipe(): RECIPE = factory.create(itemIngredient, fluidIngredient, result, progressData)

    // Ingredient
    @PublishedApi internal var itemIngredient: HTCatalystOrIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    inline fun catalyst(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        itemIngredient = Either.Left(IngredientBuilder().apply(builderAction).build())
    }

    inline fun itemIngredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        itemIngredient = Either.Right(IngredientBuilder().apply(builderAction).buildSized())
    }

    inline fun fluidIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        fluidIngredient = FluidIngredientBuilder().apply(builderAction).buildSized()
    }

    // Result
    @PublishedApi internal var result: RESULT by HTDelegates.onceInitialize()

    operator fun RESULT.unaryPlus() {
        result = this
    }

    //    ToFluid    //

    class ToFluid<out RECIPE : Recipe<*>>(prefix: String, factory: Factory<HTFluidResult, RECIPE>) : HTItemAndFluidToRecipeBuilder<HTFluidResult, RECIPE>(prefix, factory) {
        inline fun result(builderAction: HTFluidResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            result = HTFluidResultBuilder().apply(builderAction).build()
        }
    }

    //    ToItem    //

    class ToItem<out RECIPE : Recipe<*>>(prefix: String, factory: Factory<HTItemResult, RECIPE>) : HTItemAndFluidToRecipeBuilder<HTItemResult, RECIPE>(prefix, factory) {
        inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            result = HTItemResultBuilder().apply(builderAction).build()
        }
    }

    //    Factory    //

    fun interface Factory<RESULT : HTRecipeResult<*>, out RECIPE : Any> {
        fun create(itemIngredient: HTCatalystOrIngredient, fluidIngredient: HTFluidIngredient, result: RESULT, progressData: HTProgressData): RECIPE
    }
}
