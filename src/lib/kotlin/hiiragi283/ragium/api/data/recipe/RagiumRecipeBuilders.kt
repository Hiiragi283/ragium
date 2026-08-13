@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTDoubleItemToItemRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToDoubleItemRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToFluidRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTAssemblingRecipe
import hiiragi283.ragium.api.recipe.RTCrushingRecipe
import hiiragi283.ragium.api.recipe.RTMeltingRecipe
import hiiragi283.ragium.api.recipe.RTSmeltingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data object RagiumRecipeBuilders {
    // Mechanical
    @JvmStatic
    fun assembling(builderAction: HTDoubleItemToItemRecipeBuilder<RTAssemblingRecipe>.() -> Unit): HTDoubleItemToItemRecipeBuilder<RTAssemblingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTDoubleItemToItemRecipeBuilder(RagiumConstants.ASSEMBLING, ::RTAssemblingRecipe).apply(builderAction)
    }

    @JvmStatic
    fun crushing(builderAction: HTItemToDoubleItemRecipeBuilder<RTCrushingRecipe>.() -> Unit): HTItemToDoubleItemRecipeBuilder<RTCrushingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToDoubleItemRecipeBuilder(RagiumConstants.CRUSHING, ::RTCrushingRecipe).apply(builderAction)
    }

    // Heat
    @JvmStatic
    fun melting(builderAction: HTItemToFluidRecipeBuilder<RTMeltingRecipe>.() -> Unit): HTItemToFluidRecipeBuilder<RTMeltingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToFluidRecipeBuilder(RagiumConstants.MELTING, ::RTMeltingRecipe).apply(builderAction)
    }

    @JvmStatic
    fun smelting(builderAction: HTItemToItemRecipeBuilder<RTSmeltingRecipe>.() -> Unit): HTItemToItemRecipeBuilder<RTSmeltingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToItemRecipeBuilder(HTConstants.SMELTING, ::RTSmeltingRecipe).apply(builderAction)
    }
}
