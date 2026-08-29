@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.support.data.recipe.HTMultiOutputRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class HTPlantingRecipeBuilder : HTMultiOutputRecipeBuilder<RTPlantingRecipe>(RagiumConst.PLANTING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTPlantingRecipeBuilder.() -> Unit): HTPlantingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTPlantingRecipeBuilder().apply(builderAction)
        }
    }

    init {
        time /= 2
    }

    var plant: HTItemIngredient by HTDelegates.onceInitialize()
    var soil: HTItemIngredient by HTDelegates.onceInitialize()

    inline fun plant(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        plant = IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun soil(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        soil = IngredientBuilder().apply(builderAction).buildSized()
    }

    override fun createRecipe(): RTPlantingRecipe = RTPlantingRecipe(plant, soil, results, progressData)
}
