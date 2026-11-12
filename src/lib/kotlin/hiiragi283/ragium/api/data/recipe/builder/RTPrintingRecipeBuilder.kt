@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.data.recipe.builder.HTItemCatalystAcceptor
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTPrintingRecipe
import net.minecraft.resources.Identifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTPrintingRecipeBuilder :
    HTProgressRecipeBuilder<RTPrintingRecipe>(RagiumConstants.PRINTING),
    HTItemCatalystAcceptor {
    override fun getRecipeId(): Identifier? = null

    override fun createRecipe(): RTPrintingRecipe = RTPrintingRecipe(ingredient, catalyst, progressData)

    // Ingredient
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    // Catalyst
    @PublishedApi internal var catalyst: HTItemCatalyst by HTDelegates.onceInitialize(HTItemCatalyst::EMPTY)

    override operator fun HTItemCatalyst.unaryPlus() {
        catalyst = this
    }
}
