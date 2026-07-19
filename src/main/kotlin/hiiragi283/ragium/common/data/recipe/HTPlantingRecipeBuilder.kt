@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.impl.data.recipe.HTMultiOutputRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.world.item.crafting.Ingredient

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

    var plant: Ingredient by HTDelegates.onceInitialize()
    var soil: Ingredient by HTDelegates.onceInitialize()

    inline fun plant(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        plant = IngredientBuilder().apply(builderAction).build()
    }

    inline fun soil(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        soil = IngredientBuilder().apply(builderAction).build()
    }

    override fun createRecipe(): RTPlantingRecipe = RTPlantingRecipe(plant, soil, results, progressData)
}
