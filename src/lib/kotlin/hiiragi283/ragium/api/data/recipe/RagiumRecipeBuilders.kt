@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTDoubleItemToItemRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemAndFluidToItemRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToDoubleItemRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToFluidRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTAssemblingRecipe
import hiiragi283.ragium.api.recipe.RTBrewingRecipe
import hiiragi283.ragium.api.recipe.RTCrushingRecipe
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
import hiiragi283.ragium.api.recipe.RTMeltingRecipe
import hiiragi283.ragium.api.recipe.RTSmeltingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data object RagiumRecipeBuilders {
    // Mechanical
    @JvmStatic
    inline fun assembling(builderAction: HTDoubleItemToItemRecipeBuilder<RTAssemblingRecipe>.() -> Unit): HTDoubleItemToItemRecipeBuilder<RTAssemblingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTDoubleItemToItemRecipeBuilder(RagiumConstants.ASSEMBLING, ::RTAssemblingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun crushing(builderAction: HTItemToDoubleItemRecipeBuilder<RTCrushingRecipe>.() -> Unit): HTItemToDoubleItemRecipeBuilder<RTCrushingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToDoubleItemRecipeBuilder(RagiumConstants.CRUSHING, ::RTCrushingRecipe).apply(builderAction)
    }

    // Heat
    @JvmStatic
    inline fun freezing(builderAction: HTItemAndFluidToItemRecipeBuilder<RTFreezingRecipe>.() -> Unit): HTItemAndFluidToItemRecipeBuilder<RTFreezingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToItemRecipeBuilder(RagiumConstants.FREEZING, ::RTFreezingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun melting(builderAction: HTItemToFluidRecipeBuilder<RTMeltingRecipe>.() -> Unit): HTItemToFluidRecipeBuilder<RTMeltingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToFluidRecipeBuilder(RagiumConstants.MELTING, ::RTMeltingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun smelting(builderAction: HTItemToItemRecipeBuilder<RTSmeltingRecipe>.() -> Unit): HTItemToItemRecipeBuilder<RTSmeltingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToItemRecipeBuilder(HTConstants.SMELTING, ::RTSmeltingRecipe).apply(builderAction)
    }

    // Chemical

    // Bio
    @JvmStatic
    inline fun brewing(builderAction: HTItemOrFluidRecipeBuilder<RTBrewingRecipe>.() -> Unit): HTItemOrFluidRecipeBuilder<RTBrewingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemOrFluidRecipeBuilder(RagiumConstants.BREWING, ::RTBrewingRecipe).apply(builderAction)
    }
}
