@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Option
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

class HTItemToDoubleItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    override fun getPrimalId(): Identifier = primary.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, primary, secondary, progressData)

    // Ingredient
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder().apply(builderAction).buildSized()
    }

    // Result
    @PublishedApi internal var primary: HTItemResult by HTDelegates.onceInitialize()

    @PublishedApi internal var secondary: Option<HTItemResult> by HTDelegates.onceInitialize { Option.none() }

    inline fun primary(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        primary = HTItemResultBuilder().apply(builderAction).build()
    }

    inline fun secondary(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        secondary = Option.some(HTItemResultBuilder().apply(builderAction).build())
    }

    //    Factory    //

    fun interface Factory<out RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, primary: HTItemResult, secondary: Option<HTItemResult>, progressData: HTProgressData): RECIPE
    }
}
