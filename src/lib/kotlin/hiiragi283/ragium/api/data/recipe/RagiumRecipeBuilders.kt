@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTItemToFluidRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTMeltingRecipe
import hiiragi283.ragium.api.recipe.RTSmeltingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data object RagiumRecipeBuilders {
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
