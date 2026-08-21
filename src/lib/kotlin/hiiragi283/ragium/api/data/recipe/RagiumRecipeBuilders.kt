@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTDoubleItemToItemRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemAndFluidToRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToDoubleItemRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToFluidRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToItemAndFluidRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTAssemblingRecipe
import hiiragi283.ragium.api.recipe.RTBathingRecipe
import hiiragi283.ragium.api.recipe.RTBrewingRecipe
import hiiragi283.ragium.api.recipe.RTCrushingRecipe
import hiiragi283.ragium.api.recipe.RTCuttingRecipe
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
import hiiragi283.ragium.api.recipe.RTMeltingRecipe
import hiiragi283.ragium.api.recipe.RTPyrolyzingRecipe
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

    @JvmStatic
    inline fun cutting(builderAction: HTItemToDoubleItemRecipeBuilder<RTCuttingRecipe>.() -> Unit): HTItemToDoubleItemRecipeBuilder<RTCuttingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToDoubleItemRecipeBuilder(RagiumConstants.CUTTING, ::RTCuttingRecipe).apply(builderAction)
    }

    // Heat
    @JvmStatic
    inline fun freezing(builderAction: HTItemAndFluidToRecipeBuilder.ToItem<RTFreezingRecipe>.() -> Unit): HTItemAndFluidToRecipeBuilder.ToItem<RTFreezingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToRecipeBuilder.ToItem(RagiumConstants.FREEZING, ::RTFreezingRecipe).apply(builderAction)
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

    @JvmStatic
    inline fun pyrolyzing(builderAction: HTItemToItemAndFluidRecipeBuilder<RTPyrolyzingRecipe>.() -> Unit): HTItemToItemAndFluidRecipeBuilder<RTPyrolyzingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToItemAndFluidRecipeBuilder(RagiumConstants.PYROLYZING, ::RTPyrolyzingRecipe).apply(builderAction)
    }

    // Chemical
    @JvmStatic
    inline fun bathing(builderAction: HTItemAndFluidToRecipeBuilder.ToItem<RTBathingRecipe>.() -> Unit): HTItemAndFluidToRecipeBuilder.ToItem<RTBathingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToRecipeBuilder.ToItem(RagiumConstants.BATHING, ::RTBathingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun electrolyzing(builderAction: RTElectrolyzingRecipeBuilder.() -> Unit): RTElectrolyzingRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return RTElectrolyzingRecipeBuilder().apply(builderAction)
    }

    // Bio
    @JvmStatic
    inline fun brewing(builderAction: HTItemAndFluidToRecipeBuilder.ToFluid<RTBrewingRecipe>.() -> Unit): HTItemAndFluidToRecipeBuilder.ToFluid<RTBrewingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToRecipeBuilder.ToFluid(RagiumConstants.BREWING, ::RTBrewingRecipe).apply(builderAction)
    }
}
