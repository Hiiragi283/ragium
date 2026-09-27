@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTWashingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTWashingRecipeBuilder : HTProgressRecipeBuilder<RTWashingRecipe>(RagiumConstants.WASHING) {
    override fun getRecipeId(): Identifier? = results.first().getId()

    override fun createRecipe(): RTWashingRecipe =
        RTWashingRecipe(itemIngredient, fluidIngredient, results.toNel(), progressData)

    // Ingredient
    var itemIngredient: HTItemIngredient by HTDelegates.onceInitialize()

    var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredient = this
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

    // Result
    @PublishedApi internal val results: MutableList<HTItemResult> = ObjectArrayList()

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        results += HTItemResultBuilder.build(builderAction)
    }
}
