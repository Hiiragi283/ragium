@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTFreezingRecipeBuilder : HTProgressRecipeBuilder<RTFreezingRecipe>(RagiumConstants.FREEZING) {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTFreezingRecipe = RTFreezingRecipe(ingredient, catalyst, result, progressData)

    // Ingredient
    var ingredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun Ingredient.unaryPlus() {
        +HTItemCatalyst(this)
    }

    inline fun ingredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidIngredientBuilder.build(builderAction)
    }

    // Catalyst
    var catalyst: HTItemCatalyst by HTDelegates.onceInitialize(HTItemCatalyst::EMPTY)

    operator fun HTFluidIngredient.unaryPlus() {
        ingredient = this
    }

    operator fun HTItemCatalyst.unaryPlus() {
        catalyst = this
    }

    inline fun catalyst(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder.build(builderAction)
    }

    // Result
    var result: HTItemResult by HTDelegates.onceInitialize()

    operator fun HTItemResult.unaryPlus() {
        result = this
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder.build(builderAction)
    }
}
